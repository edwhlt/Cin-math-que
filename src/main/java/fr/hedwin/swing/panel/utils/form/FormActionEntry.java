/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: FormActionEntry.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.form;

import fr.hedwin.utils.Callable;

import javax.swing.*;
import java.util.function.Consumer;

public class FormActionEntry extends FormEntry<Void, Callable> {

    private final Consumer<Throwable> error;

    public FormActionEntry(String label, Callable callable, Consumer<Throwable> error) {
        super(label, null, null, s -> callable, r -> true);
        this.error = error;
        initComponents();
        updateValue.accept(callable);
    }

    public Consumer<Throwable> getError() {
        return error;
    }

    @Override
    void initComponents() {
        JButton applyButton = new JButton(getLabel());
        components.add(applyButton);
        updateValue = r -> {
            applyButton.addActionListener(evt -> {
                try {
                    r.run();
                } catch (Exception e) {
                    getError().accept(e);
                }
            });
            entry = () -> r;
        };
    }
}
