/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: MultipleResultPanel.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.result;

import fr.hedwin.db.model.TmdbElement;
import fr.hedwin.swing.other.LoadDataBar;
import fr.hedwin.swing.panel.result.properties.ResultEnumProperties;
import fr.hedwin.swing.panel.result.properties.ResultPanelProperties;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("unchecked")
public class MultipleResultPanel extends ResultPanel<Map<String, ?>> {

    private JTabbedPane tabbedPane;

    public MultipleResultPanel(Map<String, ?> result, LoadDataBar loadDataBar, ResultPanelProperties<TmdbElement> resultPanelProperties) throws Exception {
        super(result, loadDataBar);

        if(resultPanelProperties != null){
            addElementBottom(resultPanelProperties.getSuccessBtn(this::getSelectedElement));
            addElementBottom(resultPanelProperties.getCancelBtn());
        }
    }

    @Override
    protected void initComponents() throws Exception {
        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        //tabbedPane.putClientProperty("JTabbedPane.tabAlignment", "trailing");

        boolean success = false;
        for(Map.Entry<String, ?> entry : result.entrySet()){
            if(entry.getValue() == null) return;
            try{
                tabbedPane.addTab(entry.getKey(), ResultEnumProperties.getPanelElement((float) 1/result.size(), entry.getValue(), loadDataBar));
                success = true;
            }catch (Exception ignored){}
        }
        if(!success) throw new Exception("Aucun résultat");
        center_panel.add(tabbedPane, BorderLayout.CENTER);
        add(center_panel, BorderLayout.CENTER);
    }

    @Override
    public void onClose() {
        Arrays.stream(tabbedPane.getComponents()).filter(ResultPanel.class::isInstance).map(ResultPanel.class::cast).forEach(ResultPanel::onClose);
    }

    public TmdbElement getSelectedElement(){
        Component component = tabbedPane.getSelectedComponent();
        if(component instanceof SeveralResultPanel){
            return ((SeveralResultPanel<TmdbElement>) component).getSelectedElement();
        }else if(component instanceof ResultElementPanel) return ((ResultElementPanel<TmdbElement>) component).result;
        return null;
    }

}
