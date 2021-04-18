/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: FormEntryPassword.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.form;

import javax.swing.*;

public class FormEntryPassword extends FormSingleEntry<String> {

    public FormEntryPassword(String label, String value) {
        super(label, value, s -> s, s -> s);
    }

    @Override
    public void initComponents() {
        components.add(new JLabel(getLabel()+" : "));
        JPasswordField jPasswordField = new JPasswordField();
        if(value != null) jPasswordField.setText(value);
        components.add(jPasswordField);
        //return
        updateValue = r -> jPasswordField.setText(setter.apply(r));
        entry = () -> getter.apply(new String(jPasswordField.getPassword()));
    }

}
