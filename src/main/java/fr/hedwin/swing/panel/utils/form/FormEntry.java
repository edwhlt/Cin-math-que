/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: FormEntry.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.form;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class FormEntry<T, R> {

    protected List<Component> components = new LinkedList<>();
    protected String label;
    protected T value;
    protected Function<T, String> setter;
    protected Function<String, R> getter;
    protected Consumer<R> updateValue;
    private Function<R, Boolean> conditionOnResult;
    protected T[] options;
    protected Supplier<R> entry;

    @SafeVarargs
    public FormEntry(String label, T value, Function<T, String> setter, Function<String, R> getter, Function<R, Boolean> conditionOnResult, T... options){
        this.label = label;
        this.value = value;
        this.setter = setter;
        this.getter = getter;
        this.conditionOnResult = conditionOnResult;
        this.options = options;
    }

    public R getValue() throws Exception {
        R r = entry.get();
        if(conditionOnResult.apply(r)){
            setOutline(null);
            return r;
        }else{
            setOutline("error");
            throw new Exception("Le champ '"+getLabel()+"' est mal renseigné !");
        }
    }

    public void setValue(R r){
        updateValue.accept(r);
    }

    public void setOutline(String value){
        components.stream().filter(JTextField.class::isInstance).map(JTextField.class::cast).forEach(c -> c.putClientProperty("JComponent.outline", value));
    }

    public List<Component> getComponents() {
        return components;
    }

    public String getLabel(){
        return (label.charAt(0) + label.substring(1).toLowerCase()).replace("_", " ");
    }

    abstract void initComponents();

}
