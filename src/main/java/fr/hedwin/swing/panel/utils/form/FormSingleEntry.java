/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: FormSingleEntry.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.form;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FormSingleEntry<T> extends FormEntry<T, T> {

    private Type type;

    public enum Type{COMBOBOX, RADIOBUTTON}

    public FormSingleEntry(String label, T value, Function<T, String> setter, Function<String, T> getter){
        this(label, value, setter, getter, t -> true);
    }

    public FormSingleEntry(String label, T value, Function<T, String> setter, Function<String, T> getter, Function<T, Boolean> conditionOnResult){
        this(label, value, setter, getter, conditionOnResult, null);
    }

    @SafeVarargs
    public FormSingleEntry(String label, T value, Function<T, String> setter, Function<String, T> getter, Function<T, Boolean> conditionOnResult, Type type, T... options){
        super(label, value, setter, getter, conditionOnResult, options);
        this.type = type;
        initComponents();
    }

    @Override
    public void initComponents(){
        components.add(new JLabel(getLabel()+" : "){{
            setMinimumSize(new Dimension(Integer.MAX_VALUE, 30));
        }});
        if(options.length == 0){
            JTextField jTextField = new JTextField();
            if(value != null) jTextField.setText(setter.apply(value));
            components.add(jTextField);
            updateValue = r -> jTextField.setText(setter.apply(r));
            entry = () -> getter.apply(jTextField.getText());
        }else{
            if(type.equals(Type.RADIOBUTTON)){
                ButtonGroup btn_group = new ButtonGroup();
                Map<T, JRadioButton> radioButtonTemp = new HashMap<>();
                for(T type : options){
                    JRadioButton jRadioButton1 = new JRadioButton(setter.apply(type));
                    btn_group.add(jRadioButton1);
                    radioButtonTemp.put(type, jRadioButton1);
                    if(type == value) jRadioButton1.setSelected(true);
                    jRadioButton1.setMargin(new Insets(0, 20, 0, 0));
                    components.add(jRadioButton1);
                }
                updateValue = (r) -> radioButtonTemp.get(r).setSelected(true);
                //return
                entry = () -> {
                    for(Enumeration<AbstractButton> abs = btn_group.getElements(); abs.hasMoreElements();){
                        AbstractButton abstractButton = abs.nextElement();
                        if(abstractButton.isSelected()) return getter.apply(abstractButton.getText());
                    }
                    return null;
                };
            }else if(type.equals(Type.COMBOBOX)){
                JComboBox<T> jComboBox = new JComboBox<>(options);
                jComboBox.setRenderer(new DefaultListCellRenderer(){
                    @Override
                    public Component getListCellRendererComponent(JList<?> jList, Object requestListForm, int i, boolean b, boolean b1) {
                        return super.getListCellRendererComponent( jList, setter.apply(((T) requestListForm)), i, b, b1 );
                    }
                });
                components.add(jComboBox);
                updateValue = jComboBox::setSelectedItem;
                entry = () -> jComboBox.getItemAt(jComboBox.getSelectedIndex());
            }
        }
    }

}
