/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: IHMLogin.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing;

import fr.hedwin.Main;
import fr.hedwin.swing.panel.utils.form.Form;
import fr.hedwin.swing.panel.utils.form.FormActionEntry;
import fr.hedwin.swing.panel.utils.form.FormEntryPassword;
import fr.hedwin.swing.panel.utils.form.FormSingleEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

import static fr.hedwin.Main.loadDatas;

public class IHMLogin extends JFrame {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public IHMLogin(){
        super("Connexion");
        initComponents();
    }

    public void initComponents() {
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icon.png"));
        setIconImage(icon);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        FormSingleEntry<String> name = new FormSingleEntry<>("IDENTIFIANT", null, s->s, s->s);
        FormEntryPassword pass = new FormEntryPassword("MOT_DE_PASSE", null);
        FormActionEntry login = new FormActionEntry("CONNEXION", () -> {
            if(!(name.getValue().equals("edwin") && pass.getValue().equals("admin"))) {
                throw new Exception("Identifiant ou mot de passe incorrect !");
            }
            //OUVERTURE DE LA FENETRE
            IHM ihm = new IHM();
            ihm.setVisible(true);
            dispose();

            //CHARGEMENT DES DONNEES
            loadDatas();
            Main.movies.forEach(ihm.getCinematheque().getTable()::addRow);
        }, (e) -> {
            name.setOutline("error");
            pass.setOutline("error");
            JOptionPane.showMessageDialog(this, e.getMessage(), e.getMessage(), JOptionPane.WARNING_MESSAGE);
            logger.error("Erreur connection : ", e);
        });
        FormActionEntry singin = new FormActionEntry("INSCRIPTION", () -> {

        }, (e) -> {});
        Form form = new Form("Login", name, pass, login, singin);
        form.setPreferredSize(new Dimension(250, 200));
        add(form);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
