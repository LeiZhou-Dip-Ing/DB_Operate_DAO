package org.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.example.GUI.KundenGUI;
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
                //  Injecting KundenGUI
                KundenGUI kundenGUI = injector.getInstance(KundenGUI.class);
            }
        });
    }
}
