/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: FormSingleNumberEntry.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.form;

import java.util.function.Function;

public class FormSingleNumberEntry extends FormSingleEntry<Integer> {

    public FormSingleNumberEntry(String label, Integer value) {
        this(label, value, r -> true);
    }

    public FormSingleNumberEntry(String label, Integer value, Function<Integer, Boolean> conditionOnResult) {
        this(label, value, conditionOnResult, null);
    }

    public FormSingleNumberEntry(String label, Integer value, Function<Integer, Boolean> conditionOnResult, Type type, Integer... options) {
        super(label, value, String::valueOf, s -> {
            try {
                return Integer.parseInt(s);
            }catch (NumberFormatException ex){
                return null;
            }
        }, conditionOnResult, type, options);
    }

}
