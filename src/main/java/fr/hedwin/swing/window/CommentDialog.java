/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: CommentDialog.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.window;

import fr.hedwin.swing.panel.comment.CommentsPanel;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class CommentDialog extends JDialog {

    public CommentDialog(JDialog parent, String title, boolean modal, UUID movie) {
        super(parent, modal);
        initComponents(movie);
    }

    public CommentDialog(JFrame parent, String title, boolean modal, UUID movie) {
        super(parent, title, modal);
        initComponents(movie);
    }

    public void initComponents(UUID movie){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(new CommentsPanel(movie));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
