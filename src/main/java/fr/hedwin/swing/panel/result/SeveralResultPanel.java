/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: SeveralResultPanel.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.result;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import fr.hedwin.db.model.TmdbElement;
import fr.hedwin.db.utils.Future;
import fr.hedwin.db.Results;
import fr.hedwin.db.object.ResultsPage;
import fr.hedwin.swing.other.LoadDataBar;
import fr.hedwin.swing.panel.result.properties.ResultEnumProperties;
import fr.hedwin.swing.panel.result.properties.ResultPanelProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SeveralResultPanel<T> extends ResultPanel<Results<T>> {

    private final ResultPanelProperties<T> resultPanelProperties;
    private Map<Integer, ResultPagePanel<T>> pagePanel;
    private int actualPage;
    private JLabel pageLabel;

    private static Logger logger = LoggerFactory.getLogger(SeveralResultPanel.class);

    public SeveralResultPanel(Results<T> result, LoadDataBar loadDataBar) throws Exception {
        this(result, loadDataBar, null);
    }

    public SeveralResultPanel(Results<T> result, LoadDataBar loadDataBar, ResultPanelProperties<T> resultPanelProperties) throws Exception {
        this(1, result, loadDataBar, resultPanelProperties);
    }

    public SeveralResultPanel(float fraction, Results<T> result, LoadDataBar loadDataBar, ResultPanelProperties<T> resultPanelProperties) throws Exception {
        super(fraction, result, loadDataBar);
        this.resultPanelProperties = resultPanelProperties;

        if(result.getTotalResults() <= 0) throw new Exception("Aucun résultat !");
        if(resultPanelProperties != null){
            addElementBottom(resultPanelProperties.getSuccessBtn(this::getSelectedElement));
            addElementBottom(resultPanelProperties.getCancelBtn());
        }
    }

    @Override
    protected void initComponents() throws Exception{
        this.pagePanel = new HashMap<>();
        this.pageLabel = new JLabel();

        if(!result.getResultsPage(1).call().isEmpty()){
            if(result.getTotalResults() == 1) {
                center_panel.add(ResultEnumProperties.getPanelElement(fraction, (TmdbElement) result.getFirstPage().getResults().get(0), getLoadDataBar()), BorderLayout.CENTER);
                getLoadDataBar().setFraction(1);
            } else openPage(1);

            if(result.getTotalPage() > 1){
                JButton left = new JButton(new FlatSVGIcon("images/arrowLeft_dark.svg"));
                left.addActionListener(evt -> openPage(actualPage-1));

                JButton right = new JButton(new FlatSVGIcon("images/arrowRight_dark.svg"));
                right.addActionListener(evt -> openPage(actualPage+1));

                addElementBottom(left);
                addElementBottom(pageLabel);
                addElementBottom(right);
            }
        }
        add(center_panel, BorderLayout.CENTER);
    }

    @Override
    public void onClose() {
        pagePanel.values().forEach(ResultPagePanel::onClose);
        if(resultPanelProperties != null) resultPanelProperties.cancel();
    }

    public void openPage(int page){
        if(pagePanel.containsKey(page)){
            if(center_panel.getComponents().length > 0) {
                pagePanel.get(actualPage).setVisible(false);
            }
            if(Arrays.asList(center_panel.getComponents()).contains(pagePanel.get(page))) {
                pagePanel.get(page).setVisible(true);
            }else center_panel.add(pagePanel.get(page));
            center_panel.repaint();
            center_panel.revalidate();
            setNumberPageLabel(page);
            this.actualPage = page;
        }
        else {
            if(page == 1) {
                try {
                    pagePanel.put(page, new ResultPagePanel<>(fraction, this, result.getFirstPage(), getLoadDataBar()));
                    openPage(page);
                } catch (Exception e) {
                    logger.error("Impossible de générer un panel d'une page :", e);
                }
            }
            else{
                if(page <= 0 || page > result.getTotalPage()) return;
                getLoadDataBar().initialize();
                Future<ResultsPage<T>> future = result.getPage(page);
                future.then((result) -> {
                    try {
                        //On met la fraction à 1 parceque qu'on charge plus tard
                        pagePanel.put(page, new ResultPagePanel<>(1, this, result, getLoadDataBar()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getLoadDataBar().close();
                    //le panel a été ajouté dans la map donc on peut appellé la méthode où le if sera executé
                    openPage(page);
                });
            }
        }
    }

    public void setNumberPageLabel(int page){
        this.pageLabel.setText(page+"/"+result.getTotalPage());
    }

    public ResultPanelProperties<T> getResultPanelProperties() {
        return resultPanelProperties;
    }

    public T getSelectedElement() {
        if(pagePanel.isEmpty()){
            return ((ResultElementPanel<T>) center_panel.getComponents()[0]).result;
        }else{
            return pagePanel.get(actualPage).getSelectedElement();
        }
    }
}
