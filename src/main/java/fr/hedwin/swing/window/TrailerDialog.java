/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: TrailerDialog.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.window;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TrailerDialog extends JDialog {

    private static final CefSettings settings = new CefSettings(){{
        windowless_rendering_enabled = false;
    }};

    private static final CefApp cefApp = CefApp.getInstance(settings);

    public static void launchTrailer(Window parent, String title, String youtubeId){
        new TrailerDialog(parent, title, youtubeId);
    }

    public TrailerDialog(Window parent, String title, String youtubeId){
        super(parent, title, Dialog.DEFAULT_MODALITY_TYPE);
        generate(youtubeId);
    }

    private void generate(String youtubeId){
        /*Engine engins = Engine.newInstance(EngineOptions.newBuilder(HARDWARE_ACCELERATED).licenseKey("1BNDHFSC1FZ18NPW81L23QYLBV875KQF21EQI2YAQQZ78LGFU7T176KTPRNSC9BCX627ZR").build());
        Browser browser = engins.newBrowser();
        browser.navigation().loadUrl("https://www.youtube.com/embed/"+youtubeId);
        BrowserView view = BrowserView.newInstance(browser);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                engins.close();
            }
        });
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        add(view, BorderLayout.CENTER);*/

        /*JourneyBrowserView browser = new JourneyBrowserView("https://www.youtube.com/embed/"+youtubeId);
        add(browser, BorderLayout.CENTER);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                browser.getCefApp().dispose();
                dispose();
            }
        });*/
        final CefClient client = cefApp.createClient();

        final CefBrowser browser = client.createBrowser("https://www.youtube.com/embed/"+youtubeId, false, false);

        add(browser.getUIComponent(), BorderLayout.CENTER);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                client.dispose();
                dispose();
            }
        });

        setSize(1280, 720);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
