/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: SearchPanel.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel;

import fr.hedwin.db.SortBy;
import fr.hedwin.db.model.IdElement;
import fr.hedwin.db.model.TmdbElement;
import fr.hedwin.db.object.DbSerie;
import fr.hedwin.db.object.Genre;
import fr.hedwin.db.utils.Future;
import fr.hedwin.db.TMDB;
import fr.hedwin.db.object.DbMovie;
import fr.hedwin.objects.Movie;
import fr.hedwin.swing.IHM;
import fr.hedwin.swing.jlist.ListCategorie;
import fr.hedwin.swing.jlist.RequestListForm;
import fr.hedwin.swing.panel.utils.form.*;
import fr.hedwin.swing.window.FormDialog;
import fr.hedwin.swing.panel.result.properties.ResultEnumProperties;
import fr.hedwin.swing.panel.result.properties.ResultPanelProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;

public class SearchPanel extends JPanel {

    private final JList<RequestListForm> jlist;
    private final FormSingleEntry<String> nameEntry;
    private final FormListEntry<Genre> genresEntry;
    private final FormSingleEntry<String> personsEntry;
    private final FormSingleEntry<SortBy> sortByEntry;
    private IHM ihm;

    public SearchPanel(IHM ihm) {
        this.ihm = ihm;
        setLayout(new BorderLayout());
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        jSplitPane.setDividerSize(2);
        jSplitPane.setAutoscrolls(true);
        add(jSplitPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BorderLayout());

        FormSingleEntry<String> name = new FormSingleEntry<>("VALEUR", null, s->s, s->s);
        this.nameEntry = name;
        FormListEntry<Genre> genres = new FormListEntry<>("GENRES", null, Genre::getName, TMDB.getGenresSorted().toArray(new Genre[]{}));
        this.genresEntry = genres;
        FormSingleEntry<String> persons = new FormSingleEntry<>("ACTEUR(S)", null, s->s, s->s);
        this.personsEntry = persons;
        FormSingleEntry<SortBy> sortBy = new FormSingleEntry<>("TRIER PAR", null, SortBy::getName, SortBy::getSortByName, s -> true, FormSingleEntry.Type.COMBOBOX, SortBy.values());
        this.sortByEntry = sortBy;

        RequestListForm[] items = {
                new ListCategorie("Recherche"),
                new RequestListForm(formPanel, "Titre de film", () -> generatePanel(jSplitPane, TMDB.searchMovie(name.getValue())), name),
                new RequestListForm(formPanel, "Titre de Série", () -> generatePanel(jSplitPane, TMDB.searchTvSerie(name.getValue())), name),
                new RequestListForm(formPanel, "Nom d'acteur", () -> generatePanel(jSplitPane, TMDB.searchPerson(name.getValue())), name),
                new RequestListForm(formPanel, "Recherche companies", () -> {}),
                new RequestListForm(formPanel, "Recherche multiple", () -> {}),
                new ListCategorie("Récupérer"),
                new RequestListForm(formPanel, "Id de film", () -> generatePanel(jSplitPane, TMDB.getMovie(Integer.parseInt(name.getValue()))), name),
                new RequestListForm(formPanel, "Id d'acteur", () -> generatePanel(jSplitPane, TMDB.getPerson(Integer.parseInt(name.getValue()))), name),
                new RequestListForm(formPanel, "Id de série TV", () -> generatePanel(jSplitPane, TMDB.getTvSeries(Integer.parseInt(name.getValue()))), name),
                new ListCategorie("Découvrir"),
                new RequestListForm(formPanel, "Films",
                        () -> generatePanel(jSplitPane, TMDB.discoverMovie(sortBy.getValue(), genres.getValue(), persons.getValue())), persons, sortBy, genres)
        };

        JScrollPane jScrollPane = new JScrollPane();
        JList<RequestListForm> jList = new JList<>(items);
        this.jlist = jList;
        jList.setBorder(null);
        jList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> jList, Object requestListForm, int i, boolean b, boolean b1) {
                JComponent component;
                if(requestListForm instanceof ListCategorie){
                    component = (JComponent) super.getListCellRendererComponent( jList, "――― "+((ListCategorie) requestListForm).getName()+" ―――", i, b, b1 );
                }
                else component = (JComponent) super.getListCellRendererComponent( jList, ((RequestListForm) requestListForm).getName(), i, b, b1 );
                //component.setToolTipText("Essai");

                return component;
            }
        });
        jList.addListSelectionListener(listSelectionEvent -> {
            if(jList.getSelectedValue() instanceof ListCategorie) {
                jList.setSelectedIndex(jList.getSelectedIndex()+1);
            }else jList.getSelectedValue().open();
        });
        jScrollPane.setViewportView(jList);

        jSplitPane.setLeftComponent(new JPanel(new BorderLayout()){{
            setBorder(new EmptyBorder(10, 10, 10, 10));
            add(jScrollPane, BorderLayout.WEST);
            add(formPanel, BorderLayout.CENTER);
        }});
        jSplitPane.setRightComponent(new JPanel(){{{setPreferredSize(new Dimension(700, 100));}}});
        jSplitPane.setResizeWeight(1.0);
        jSplitPane.setDividerLocation(0.5);
        jList.setSelectedIndex(1);
    }

    public void generatePanel(JSplitPane parent, Future<?> result){
        ihm.getProgressData().setVisible(true);
        ResultPanelProperties<TmdbElement> resultPanelProperties = new ResultPanelProperties<>("Ajouter ce film à la cinémathèque", (ob) -> {
            if(!(ob instanceof DbMovie) && !(ob instanceof DbSerie)) return;
            String title = ob instanceof DbMovie ? ((DbMovie) ob).getTitle() : ((DbSerie) ob).getName();
            FormDialog formDialog = new FormDialog(ihm, "Choisir un format", true);
            FormSingleEntry<Movie.Format> btn_group = new FormSingleEntry<>("FORMAT", null, Movie.Format::toString, Movie.Format::getIndice, r -> true, FormSingleEntry.Type.RADIOBUTTON, Movie.Format.values());
            FormActionEntry add = new FormActionEntry("Ajouter "+title, () -> {
                try {
                    Movie movie = new Movie(((IdElement) ob).getId(), title, Movie.Format.getIndice(btn_group.getValue().toString()), new Date());
                    IHM.INSTANCE.addMovie(UUID.randomUUID(), movie);
                    IHM.INSTANCE.addNotifCinematheque();
                    formDialog.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.WARNING_MESSAGE);
                }
            }, e -> {});
            Form formFormat = new Form("Ajouter", btn_group, add);
            formFormat.setPreferredSize(new Dimension(300, 200));
            formDialog.initComponents(formFormat);
        });
        ResultEnumProperties.getPanelElementFuture(1, result, ihm.getProgressData(), resultPanelProperties).then(panel -> {
            parent.setRightComponent(panel);
            ihm.getProgressData().setVisible(false);
        });
    }


    public FormSingleEntry<String> getNameEntry() {
        return nameEntry;
    }

    public FormListEntry<Genre> getGenresEntry() {
        return genresEntry;
    }

    public FormSingleEntry<String> getPersonsEntry() {
        return personsEntry;
    }

    public FormSingleEntry<SortBy> getSortByEntry() {
        return sortByEntry;
    }

    public JList<RequestListForm> getJlist() {
        return jlist;
    }

}
