/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: CardPanel.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.other;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import fr.hedwin.db.TMDB;
import fr.hedwin.db.utils.CompletableFuture;
import fr.hedwin.db.utils.Future;
import fr.hedwin.swing.panel.result.ResultElementPanel;
import fr.hedwin.utils.StringTraitement;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class CardPanel<T> extends JPanel {

    private final GridBagConstraints gbc;
    private final T t;
    private Consumer<T> moreInfo;

    public CardPanel(T t, Consumer<T> moreInfo){
        this.t = t;
        this.moreInfo = moreInfo;
        this.gbc = new GridBagConstraints();
        initComponents();
    }

    private void initComponents(){
        setLayout(new FlowLayout());
        JButton jButton = new JButton(new FlatSVGIcon("images/informationDialog_dark.svg"));
        jButton.addActionListener(evt -> moreInfo.accept(t));
        add(jButton);
    }

    public void setImage(String path){
        JLabel img = new JLabel();
        CompletableFuture.async(() -> TMDB.getImage(path, 350)).then(image -> img.setIcon(new ImageIcon(image)));
        add(img);
    }

    private CardPanel<T> addElementEntry(String label, String value){
        if(value == null) value = "Indéfinie";
        JLabel date = new JLabel("<html><b>"+label+" </b>"+ StringTraitement.parseHTML(value, 70) +"</html>");
        date.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        add(date);
        return this;
    }

    public CardPanel<T> addElementEntry(String label, Object value){
        addElementEntry(label, value == null ? null : value.toString());
        return this;
    }

}
