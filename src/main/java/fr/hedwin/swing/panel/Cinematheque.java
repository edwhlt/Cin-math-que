/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: Cinematheque.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import fr.hedwin.Main;
import fr.hedwin.db.TMDB;
import fr.hedwin.db.model.IdElement;
import fr.hedwin.db.model.TmdbElement;
import fr.hedwin.db.object.DbMovie;
import fr.hedwin.db.utils.CompletableFuture;
import fr.hedwin.db.utils.Future;
import fr.hedwin.objects.Movie;
import fr.hedwin.swing.IHM;
import fr.hedwin.swing.jlist.ListCategorie;
import fr.hedwin.swing.jlist.RequestListForm;
import fr.hedwin.swing.panel.result.MultipleResultPanel;
import fr.hedwin.swing.panel.result.ResultPanel;
import fr.hedwin.swing.panel.result.properties.ResultEnumProperties;
import fr.hedwin.swing.panel.result.properties.ResultPanelProperties;
import fr.hedwin.swing.panel.utils.form.Form;
import fr.hedwin.swing.panel.utils.form.FormActionEntry;
import fr.hedwin.swing.panel.utils.form.FormSingleEntry;
import fr.hedwin.swing.panel.utils.table.*;
import fr.hedwin.swing.window.FormDialog;
import fr.hedwin.swing.window.ResultsDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Cinematheque extends JPanel {

    private final Table<Movie> table;
    private IHM ihm;

    public Cinematheque(IHM ihm){
        this.ihm = ihm;
        setLayout(new BorderLayout());
        Column[] columnList = new Column[]{
                new ColumnObject<>("Nom", Movie::getNom),
                new ColumnInteger<>("TMDB ID", Movie::getIdTMDBLink, i -> i != -1),
                new ColumnObject<>("Format", Movie::getFormat),
                new ColumnInteger<>("Note (/100)", Movie::getNote),
                new ColumnDate<>("Date d'ajout", new SimpleDateFormat("dd/MM/yyyy"), Movie::getDate),
                new ColumnAction<Movie>(new FlatSVGIcon("images/suggestedRefactoringBulb_dark.svg"), "Rediger ou modifier un avis", (row, movie) -> {
                }),
                new ColumnAction<Movie>(new FlatSVGIcon("images/find_dark.svg"), "Rechercher dans TMDB", (row, movie) -> {
                    ihm.getProgressData().initialize();
                    Consumer<ResultPanel<?>> openDialog = (panel) -> new ResultsDialog(ihm, movie.getNom(), true, panel).setVisible(true);
                    if (movie.getIdTMDBLink() != -1)
                        ResultEnumProperties.getPanelElementFuture(1, TMDB.getMovie(movie.getIdTMDBLink()), ihm.getProgressData()).then(openDialog).error((ex) -> {
                           ResultEnumProperties.getPanelElementFuture(1, TMDB.getTvSeries(movie.getIdTMDBLink()), ihm.getProgressData()).then(openDialog).error((ex2) -> {
                               JPanel jPanel = new JPanel();
                               jPanel.add(new JLabel(ex.getMessage()));
                               jPanel.add(new JLabel(ex2.getMessage()));
                               JOptionPane.showMessageDialog(this, jPanel,
                                       "L'identifiant "+movie.getIdTMDBLink()+" associé à "+movie.getNom()+" n'est associé à aucun film ou série", JOptionPane.WARNING_MESSAGE);
                           });
                        });
                    else {
                        Map<String, Future<?>> futureMap = new HashMap<String, Future<?>>(){{{
                            put("Film", TMDB.searchMovie(movie.getNom()));
                            put("Serie", TMDB.searchTvSerie(movie.getNom()));
                        }}};
                        ResultPanelProperties<TmdbElement> resultPanelProperties = new ResultPanelProperties<>("Associer ce film", (o) -> {
                            if(o instanceof IdElement){
                                movie.setIdTMDBLink(((IdElement) o).getId());
                                row.update();
                            }
                        });
                        CompletableFuture.async(() -> futureMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> {
                            try {
                                return e.getValue().call();
                            } catch (Exception exception) {
                                return null;
                            }
                        }))).then((map) -> {
                            try {
                                openDialog.accept(new MultipleResultPanel(map, ihm.getProgressData(), resultPanelProperties));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }),
                new ColumnAction<Movie>(new FlatSVGIcon("images/edit_dark.svg"), "Modifier le film/série", (row, movie) -> {
                    FormDialog formDialog = new FormDialog(ihm, "Modifier "+movie.getNom(), true);
                    FormSingleEntry<String> formEntrie = new FormSingleEntry<>("NAME", movie.getNom(), s->s, s->s);
                    FormSingleEntry<Movie.Format> btn_group = new FormSingleEntry<>("FORMAT", null, Movie.Format::toString, Movie.Format::getIndice, r -> true, FormSingleEntry.Type.RADIOBUTTON, Movie.Format.values());
                    FormActionEntry update = new FormActionEntry("Modifier", () -> {
                        try {
                            movie.setNom(formEntrie.getValue());
                            movie.setFormat(btn_group.getValue());
                            row.update();
                            formDialog.dispose();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.WARNING_MESSAGE);
                        }
                    }, e -> {});
                    Form form = new Form("Modifier", formEntrie, btn_group, update);
                    form.setPreferredSize(new Dimension(300, 300));
                    formDialog.initComponents(form);
                }),
                new ColumnAction<Movie>(new FlatSVGIcon("images/remove_dark.svg"), "Supprimer le film/série", (row, movie) -> {
                    int r = JOptionPane.showConfirmDialog(ihm, "Etes-vous sur de vouloir supprimer ?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (r == JOptionPane.YES_OPTION) {
                        row.remove();
                        Main.movies.remove(IHM.getUUIDFromMovie(movie));
                    }
                })
        };
        table = new Table<Movie>(40, columnList).generate();
        add(table, BorderLayout.CENTER);
    }

    public Table<Movie> getTable() {
        return table;
    }

}
