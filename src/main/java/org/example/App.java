package org.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.example.GUI.KundenGUI;
import org.example.GUI.KundenManagerGUI;
import org.example.utils.IOCFramework;
import javax.swing.*;

public class App 
{
    public static void main( String[] args )
    {
        // Start and inject dependencies with Guice
        Injector injector = Guice.createInjector(new IOCFramework());
        // Instantiate KundenGUI through the Guice injector and launch the window
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // make it aktive to test demo in java-tpl-5
                //KundenGUI kundenGUI = injector.getInstance(KundenGUI.class);
                //kundenGUI.setVisible(true);

                // make it aktive to test demo in java-tpl-6
                KundenManagerGUI kundenManagerGUI = injector.getInstance(KundenManagerGUI.class);
                kundenManagerGUI.setVisible(true);
            }
        });
    }
}
