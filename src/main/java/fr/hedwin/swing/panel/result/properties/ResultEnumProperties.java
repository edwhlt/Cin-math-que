/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: ResultEnumProperties.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.result.properties;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import fr.hedwin.db.Results;
import fr.hedwin.db.model.MovieSortBy;
import fr.hedwin.db.object.*;
import fr.hedwin.db.utils.CompletableFuture;
import fr.hedwin.db.utils.Future;
import fr.hedwin.db.TMDB;
import fr.hedwin.db.model.NamedIdElement;
import fr.hedwin.swing.IHM;
import fr.hedwin.swing.jlist.RequestListForm;
import fr.hedwin.swing.panel.SearchPanel;
import fr.hedwin.swing.panel.utils.table.Column;
import fr.hedwin.swing.panel.utils.table.ColumnAction;
import fr.hedwin.swing.panel.utils.table.ColumnObject;
import fr.hedwin.swing.panel.utils.table.Table;
import fr.hedwin.swing.window.ResultsDialog;
import fr.hedwin.swing.window.TrailerDialog;
import fr.hedwin.swing.other.LoadDataBar;
import fr.hedwin.swing.panel.result.ResultElementPanel;
import fr.hedwin.swing.panel.result.ResultPanel;
import fr.hedwin.swing.panel.result.SeveralResultPanel;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;
import java.util.function.Consumer;

public class ResultEnumProperties {

    public static Future<String> getTitleElementFuture(Future<?> future) {
        return CompletableFuture.async(() -> getTitleElement(future.call()));
    }

    @Deprecated
    public static <T> ResultPanel<?> getPanelElement(float fraction, T t, LoadDataBar loadDataBar) throws Exception {
        return getPanelElement(fraction, t, loadDataBar, null);
    }

    public static <T> Future<ResultPanel<?>> getPanelElementFuture(float fraction, Future<T> future, LoadDataBar loadDataBar) {
        return getPanelElementFuture(fraction, future, loadDataBar, null);
    }

    public static <T> Future<ResultPanel<?>> getPanelElementFuture(float fraction, Future<T> future, LoadDataBar loadDataBar, ResultPanelProperties<?> properties) {
        return CompletableFuture.async(() ->  getPanelElement(fraction, future.call(), loadDataBar, properties));
    }

    @Deprecated
    public static <T> String getTitleElement(T t) {
        if(t instanceof DbMovie)  return ((DbMovie) t).getTitle();
        if(t instanceof NamedIdElement) return ((NamedIdElement) t).getName();
        else return "Indéfinie";
    }

    @Deprecated
    public static <T> ResultPanel<?> getPanelElement(float fraction, T t, LoadDataBar loadDataBar, ResultPanelProperties<?> properties) throws Exception {
        if(t instanceof Results) return new SeveralResultPanel(fraction, (Results<?>)t, loadDataBar, properties);
        else if(t instanceof DbMovie) {
            DbMovie dbMovie = (DbMovie) t;
            ResultElementPanel<DbMovie> resultElementPanel = new ResultElementPanel<>(fraction, dbMovie, loadDataBar).setImage(dbMovie.getPosterPath())
                    .addElementEntry("Notation :", dbMovie.getVoteAverage())
                    .addElementEntry("Identifiant :", dbMovie.getId())
                    .addElementEntry("Titre :", dbMovie.getTitle())
                    .addElementEntry("Description :", dbMovie.getOverview())
                    .addElementEntry("Date de sortie :", dbMovie.getReleaseDate());
            resultElementPanel.addButton(new FlatSVGIcon("images/recording_2_dark.svg"), "Toute les vidéos", () -> TMDB.getMovieVideos(dbMovie.getId()).then(videos -> {
                        JDialog jDialog = new JDialog(IHM.INSTANCE, true);
                        jDialog.setPreferredSize(new Dimension(700, 500));

                        Column[] columns = {
                                new ColumnObject<>("Titre", Videos.Video::getName), new ColumnObject<>("Type", Videos.Video::getType),
                                new ColumnAction<Videos.Video>(new FlatSVGIcon("images/execute_dark.svg"), "Voir la vidéo", (row, video) -> {
                                    new TrailerDialog(jDialog, video.getKey());
                                })
                        };
                        Table<Videos.Video> table = new Table<Videos.Video>(30, columns).generate();
                        videos.getVideoList().forEach(video -> table.addRow(UUID.randomUUID(), video));
                        jDialog.add(table);
                        jDialog.pack();
                        jDialog.setLocationRelativeTo(null);
                        jDialog.setVisible(true);
            }));
            resultElementPanel.addButton(new FlatSVGIcon("images/users_dark.svg"), "Voir les acteurs/actrices", () -> {
                TMDB.getMovieCredits(dbMovie.getId()).then(credits -> {
                    JDialog jDialog = new JDialog(IHM.INSTANCE, true);
                    jDialog.setPreferredSize(new Dimension(700, 500));

                    Column[] columns = {
                            new ColumnObject<>("Acteur", Cast::getName), new ColumnObject<>("Rôle", Cast::getCharacter),
                            new ColumnAction<Cast>(new FlatSVGIcon("images/informationDialog_dark.svg"), "Plus d'informations sur l'acteur", (row, cast) -> {
                                loadDataBar.initialize();
                                Consumer<ResultPanel<?>> openDialog = (panel) -> new ResultsDialog(IHM.INSTANCE, cast.getName(), true, panel).setVisible(true);
                                ResultEnumProperties.getPanelElementFuture(1, TMDB.getPerson(cast.getId()), loadDataBar).then(openDialog);
                            })
                    };
                    Table<Cast> table = new Table<Cast>(30, columns).generate();
                    credits.getCast().forEach(cast -> table.addRow(UUID.randomUUID(), cast));
                    jDialog.add(table);
                    jDialog.pack();
                    jDialog.setLocationRelativeTo(null);
                    jDialog.setVisible(true);
                });
            });
            resultElementPanel.addButton("Genres", "Afficher les genres", () -> {
                TMDB.getMovie(dbMovie.getId()).then(movie -> {
                    JDialog jDialog = new JDialog(IHM.INSTANCE, true);
                    jDialog.setPreferredSize(new Dimension(400, 500));

                    Column[] columns = {
                            new ColumnObject<>("ID", Genre::getId), new ColumnObject<>("Name", Genre::getName),
                            new ColumnAction<Genre>(new FlatSVGIcon("images/find_dark.svg"), "Rechercher les films de ce genre", (row, genre) -> {
                                jDialog.dispose();
                                SearchPanel searchPanel = IHM.INSTANCE.getSearchPanel();
                                JList<RequestListForm> jList = searchPanel.getJlist();
                                jList.setSelectedIndex(11);
                                searchPanel.getGenresMovieEntry().setValue(genre);
                                searchPanel.getPersonsEntry().setValue("");
                                searchPanel.getMovieSortByEntry().setValue(MovieSortBy.getDefault());
                                searchPanel.getYearsEntry().setValue(null);
                                try {
                                    jList.getSelectedValue().actionSucess().getValue().run();
                                } catch (Exception e) {
                                    jList.getSelectedValue().actionSucess().getError().accept(e);
                                }
                                IHM.INSTANCE.selectedTab(1);
                            })
                    };
                    Table<Genre> table = new Table<Genre>(30, columns).generate();
                    movie.getGenres().forEach(genre -> table.addRow(UUID.randomUUID(), genre));
                    jDialog.add(table);
                    jDialog.pack();
                    jDialog.setLocationRelativeTo(null);
                    jDialog.setVisible(true);
                });
            });
            return resultElementPanel;
        }
        else if(t instanceof Person) {
            Person person = (Person) t;
            return new ResultElementPanel<>(fraction, person, loadDataBar).setImage(person.getProfilePath())
                    .addElementEntry("Identifiant :", person.getId())
                    .addElementEntry("Nom :", person.getName())
                    .addElementEntry("Sexe :", person.getGender().getString())
                    .addElementEntry("Biographie :", person.getBiography())
                    .addElementEntry("Date de naissance :", person.getBirthday())
                    .addButton(new FlatSVGIcon("images/recording_2_dark.svg"), "A joué dans les films", () -> {
                        loadDataBar.initialize();
                        Consumer<ResultPanel<?>> openDialog = (panel) -> new ResultsDialog(IHM.INSTANCE, "Film de "+person.getName(), true, panel).setVisible(true);
                        ResultEnumProperties.getPanelElementFuture(1, TMDB.discoverMovie(null, null, (String) null, person.getId()+""), loadDataBar).then(openDialog);
                    });
        }
        else if(t instanceof DbSerie){
            DbSerie dbSerie = (DbSerie) t;
            ResultElementPanel<DbSerie> resultElementPanel = new ResultElementPanel<>(fraction, dbSerie, loadDataBar).setImage(dbSerie.getPosterPath())
                    .addElementEntry("Titre :", dbSerie.getName())
                    .addElementEntry("Description :", dbSerie.getOverview())
                    .addElementEntry("Date de sortie :", dbSerie.getFirstAirDate());
            resultElementPanel.addButton(
                    new FlatSVGIcon("images/execute_dark.svg"), "Voir la bande d'annonce",
                    () -> TMDB.getTvSeriesVideos(dbSerie.getId()).then(videos -> {
                        if(videos != null && !videos.getTrailers().isEmpty())
                            new TrailerDialog(resultElementPanel, videos.getVideo(Videos.VideoType.Trailer, 0).getKey());
                    }));
            return resultElementPanel;
        }
        else return new ResultElementPanel<>(fraction, t, loadDataBar).addElementEntry("Aucun panel associé à ce type de résultat :", t.toString());
    }

}
