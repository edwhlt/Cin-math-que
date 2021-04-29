/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: IHMLogin.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing;

import com.fasterxml.jackson.core.type.TypeReference;
import fr.hedwin.Main;
import fr.hedwin.objects.Comment;
import fr.hedwin.objects.Movie;
import fr.hedwin.objects.User;
import fr.hedwin.swing.other.LoadDataBar;
import fr.hedwin.swing.panel.utils.form.Form;
import fr.hedwin.swing.panel.utils.form.FormActionEntry;
import fr.hedwin.swing.panel.utils.form.FormEntryPassword;
import fr.hedwin.swing.panel.utils.form.FormSingleEntry;
import fr.hedwin.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.*;

public class IHMLogin extends JFrame {

    private static Logger logger = LoggerFactory.getLogger(IHMLogin.class);

    public IHMLogin(){
        super("Connexion");
        initComponents();
    }

    public void initComponents() {
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icon.png"));
        setIconImage(icon);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        try {
            Utils.loadJSON("users.json", new TypeReference<Map<UUID, User>>() {}, users -> Main.users = users);
        }catch (Exception e){

        }

        FormSingleEntry<String> name = new FormSingleEntry<>("IDENTIFIANT", null, s->s, s->s);
        FormEntryPassword pass = new FormEntryPassword("MOT_DE_PASSE", null);
        FormActionEntry login = new FormActionEntry("CONNEXION");
        login.setValue(() -> {
            User user = null;
            for (User u : Main.users.values()) {
                if(u.getUsername().equals(name.getValue()) && u.getPassword().equals(pass.getValue())) user = u;
            }
            if(user == null) throw new Exception("Identifiant ou mot de passe incorrect !");
            //OUVERTURE DE LA FENETRE

            IHM ihm = new IHM(user);
            ihm.setVisible(true);
            dispose();

            //CHARGEMENT DES DONNEES
            Utils.loadJSON("datas.json", new TypeReference<Map<UUID, Movie>>() {}, movies -> Main.movies = movies);
            Main.movies.forEach(ihm.getCinematheque().getTable()::addRow);

        }, (e) -> {
            name.setOutline("error");
            pass.setOutline("error");
            JOptionPane.showMessageDialog(this, e.getMessage(), e.getMessage(), JOptionPane.WARNING_MESSAGE);
            logger.error("Erreur connection : ", e);
        });
        FormActionEntry singin = new FormActionEntry("INSCRIPTION", () -> {
            IHMRegister ihmRegister = new IHMRegister(this);
            ihmRegister.setVisible(true);
            setVisible(false);
        }, (e) -> {});
        Form form = new Form("Login", name, pass, login, singin);
        form.setPreferredSize(new Dimension(250, 200));
        add(form);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
