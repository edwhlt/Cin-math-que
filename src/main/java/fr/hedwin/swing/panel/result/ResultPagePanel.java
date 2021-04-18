/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: ResultPagePanel.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.result;

import fr.hedwin.db.model.IdElement;
import fr.hedwin.db.model.TmdbElement;
import fr.hedwin.db.object.DbSerie;
import fr.hedwin.db.utils.Future;
import fr.hedwin.db.object.DbMovie;
import fr.hedwin.db.object.ResultsPage;
import fr.hedwin.swing.IHM;
import fr.hedwin.swing.other.LoadDataBar;
import fr.hedwin.swing.panel.result.properties.ResultEnumProperties;
import fr.hedwin.utils.StringTraitement;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ResultPagePanel<T> extends ResultPanel<ResultsPage<T>> {

    private JTabbedPane tabbedPane;
    private SeveralResultPanel<T> severalResultPanel;

    public ResultPagePanel(float fraction, SeveralResultPanel<T> severalResultPanel, ResultsPage<T> result, LoadDataBar loadDataBar) throws Exception {
        super(fraction, result, loadDataBar);
        this.severalResultPanel = severalResultPanel;
        updateSucessEnabled();
    }

    @Override
    public void initComponents(){
        tabbedPane = new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.putClientProperty("JTabbedPane.tabAlignment", "trailing");

        List<T> resultsPage = result.getResults();
        float lastFraction = loadDataBar.getFraction();
        resultsPage.forEach(m -> {
            if(m == null) return;
            String title = "<html><div style=\"text-align: right;\">"+ StringTraitement.parseHTML(ResultEnumProperties.getTitleElement(m), 30)+"</div></html>";
            try {
                tabbedPane.addTab(title, ResultEnumProperties.getPanelElement(lastFraction + fraction*(float) (resultsPage.indexOf(m)+1) / resultsPage.size(), m, loadDataBar));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        tabbedPane.addChangeListener(e -> updateSucessEnabled());

        center_panel.add(tabbedPane, BorderLayout.CENTER);
        add(center_panel, BorderLayout.CENTER);
    }

    @Override
    public void onClose() {
        Arrays.stream(tabbedPane.getComponents()).filter(ResultPanel.class::isInstance).map(ResultPanel.class::cast).forEach(ResultPanel::onClose);
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public T getSelectedElement(){
        ResultPanel<T> resultPanel = (ResultPanel<T>) tabbedPane.getSelectedComponent();
        return resultPanel == null ? null : resultPanel.result;
    }

    public void updateSucessEnabled(){
        T t = getSelectedElement();
        if(t == null || severalResultPanel.getResultPanelProperties() == null) return;
        if(t instanceof DbMovie || t instanceof DbSerie){
            severalResultPanel.getResultPanelProperties().setEnabledSucess(!IHM.isAlreadyAdded(((IdElement) t).getId()));
        }else severalResultPanel.getResultPanelProperties().setVisibleSucess(false);
    }

}
