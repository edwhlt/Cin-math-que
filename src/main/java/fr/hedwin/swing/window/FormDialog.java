/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: FormDialog.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.window;

import fr.hedwin.swing.panel.utils.form.Form;

import javax.swing.*;

public class FormDialog extends JDialog {

    public FormDialog(JFrame parent, String title, boolean modal, Form form) {
        super(parent, title, modal);
        initComponents(form);
    }

    public FormDialog(JDialog parent, boolean modal, Form form) {
        super(parent, modal);
        initComponents(form);
    }

    public FormDialog(JFrame parent, String title, boolean modal) {
        super(parent, title, modal);
    }

    public void initComponents(Form form){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(form);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


}
