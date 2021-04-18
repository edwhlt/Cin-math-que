/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: ResultPanelProperties.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.result.properties;

import javax.swing.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ResultPanelProperties<T> {

    private final Consumer<T> onSucess;
    private final Runnable onCancel;
    private final JButton successBtn;
    private final JButton cancelBtn;

    public ResultPanelProperties(String successTitle, Consumer<T> onSucess){
        this(successTitle, onSucess, null, null);
    }

    public ResultPanelProperties(String cancelTitle, Runnable onCancel){
        this(null, null, cancelTitle, onCancel);
    }

    public ResultPanelProperties(String successTitle, Consumer<T> onSucess, String cancelTitle, Runnable onCancel){
        this.onSucess = onSucess;
        this.successBtn = new JButton(successTitle);
        this.onCancel = onCancel;
        this.cancelBtn = new JButton(cancelTitle);
    }

    public JButton getSuccessBtn(Supplier<T> getReturn){
        if(onSucess == null) return null;
        successBtn.addActionListener(evt -> {
            onSucess.accept(getReturn.get());
            //Si le(s) résultat est dans une fenêtre :
            if(SwingUtilities.getWindowAncestor(successBtn) instanceof JDialog) SwingUtilities.getWindowAncestor(successBtn).dispose();
        });
        return successBtn;
    }

    public JButton getCancelBtn() {
        if(onCancel == null) return null;
        cancelBtn.addActionListener(evt -> {
            cancel();
            //Si le(s) résultat est dans une fenêtre :
            if(SwingUtilities.getWindowAncestor(successBtn) instanceof JDialog) SwingUtilities.getWindowAncestor(successBtn).dispose();
        });
        return cancelBtn;
    }

    public void setEnabledSucess(boolean enabled){
        successBtn.setEnabled(enabled);
    }

    public void setVisibleSucess(boolean enabled){
        successBtn.setVisible(enabled);
    }

    public void cancel(){
        if(onCancel != null) onCancel.run();
    }

}
