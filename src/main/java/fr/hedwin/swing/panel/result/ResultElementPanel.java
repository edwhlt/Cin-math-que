/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: ResultElementPanel.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.result;

import fr.hedwin.db.model.NamedIdElement;
import fr.hedwin.db.model.TmdbElement;
import fr.hedwin.db.utils.CompletableFuture;
import fr.hedwin.db.TMDB;
import fr.hedwin.swing.other.LoadDataBar;
import fr.hedwin.swing.panel.result.properties.ResultPanelProperties;
import fr.hedwin.utils.StringTraitement;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ResultElementPanel<T> extends ResultPanel<T> {

    public ResultElementPanel(T result, LoadDataBar loadDataBar) throws Exception {
        this(1, result, loadDataBar);
    }

    public ResultElementPanel(float fraction, T result, LoadDataBar loadDataBar) throws Exception {
        super(fraction, result, loadDataBar);
        loadDataBar.setFraction(fraction*1);
    }

    @Override
    protected void initComponents() {
        JScrollPane jScrollPane = new JScrollPane(center_panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setBorder(BorderFactory.createEmptyBorder());
        jScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        center_panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        center_panel.setLayout(new BoxLayout(center_panel, BoxLayout.Y_AXIS));
        add(jScrollPane, BorderLayout.CENTER);
    }

    @Override
    public void onClose() {
        //engins.close();
    }

    private void addElementEntry(String label, String value){
        if(value == null) value = "Indéfinie";
        JLabel date = new JLabel("<html><b>"+label+" </b>"+ StringTraitement.parseHTML(value, 70) +"</html>");
        date.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        center_panel.add(date);
    }

    public void addButton(ImageIcon btn, String tooltip, Runnable runnable){
        JButton jButton = new JButton(btn);
        jButton.setBorderPainted(false);
        jButton.setFocusable(false);
        jButton.setToolTipText(tooltip);
        jButton.addActionListener(evt -> runnable.run());
        addElementTop(jButton);
    }

    public ResultElementPanel<T> addButton(String btn, String tooltip, Runnable runnable){
        JButton jButton = new JButton(btn);
        jButton.setBorderPainted(false);
        jButton.setToolTipText(tooltip);
        jButton.addActionListener(evt -> runnable.run());
        addElementTop(jButton);
        return this;
    }

    public ResultElementPanel<T> addElementEntry(String label, Object value){
        addElementEntry(label, value == null ? null : value.toString());
        return this;
    }

    public ResultElementPanel<T> addListElementEntry(String label, List<? extends NamedIdElement> value){
        if(value == null) addElementEntry(label, null);
        else addElementEntry(label, value.stream().map(NamedIdElement::getName).collect(Collectors.joining(", ")));
        return this;
    }

    public ResultElementPanel<T> setBackdrop(String path) {
        JLabel img = new JLabel();
        CompletableFuture.async(() -> TMDB.getImage(path, 700)).then(image -> img.setIcon(new ImageIcon(image))).thenFinally(() -> {
            try {
                Image image = TMDB.getImage(path, null);
                img.setIcon(new ImageIcon(image.getScaledInstance(700, 400, Image.SCALE_SMOOTH)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        center_panel.add(img, 0);
        return this;
    }

    public ResultElementPanel<T> setImage(String path){
        JLabel img = new JLabel();
        CompletableFuture.async(() -> TMDB.getImage(path, 200)).then(image -> img.setIcon(new ImageIcon(image)));
        center_panel.add(img);
        return this;
    }

}
