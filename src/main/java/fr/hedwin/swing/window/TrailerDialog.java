/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: TrailerDialog.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.window;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.view.swing.BrowserView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;

public class TrailerDialog extends JDialog {

    public static void launchTrailer(Window parent, String title, String youtubeId){
        new TrailerDialog(parent, title, youtubeId);
    }

    public TrailerDialog(Window parent, String title, String youtubeId){
        super(parent, title, Dialog.DEFAULT_MODALITY_TYPE);
        generate(youtubeId);
    }

    private void generate(String youtubeId){
        Engine engins = Engine.newInstance(EngineOptions.newBuilder(HARDWARE_ACCELERATED).licenseKey("1BNDHFSC1FYQKGPO8IJFOA0L7A3M3RG2RWOTTKK6Y5Y42M1HEEX0MQ10ZJ5XGAF82HYGZJ").build());
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
        add(view, BorderLayout.CENTER);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setVisible(true);
    }


}
