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

    //Test demo
    /*
    public static void main(String[] args) {
        // Initialize Guice injector with the correct module
        Injector injector = Guice.createInjector(new KundeModule());

        // Obtain an instance of Ikunden from the injector
        Ikunden kundenDAO = injector.getInstance(Ikunden.class);  // Guice will handle the instantiation and injection

        // Set database type
        DatabaseType dbType = DatabaseType.DERBY;

        // Test data
        Kunde kunde1 = new Kunde(1, "Max", "Mustermann", "Musterstraße 1", "12345");
        Kunde kunde2 = new Kunde(2, "Erika", "Mustermann", "Musterstraße 2", "54321");

        // Test DAO functionality
        try {
            // Ensure table is initialized
            kundenDAO.save(kunde1, dbType);   // Save the first customer
            kundenDAO.save(kunde2, dbType);   // Save the second customer

            // Retrieve all customers
            List<Kunde> allKunden = kundenDAO.findAll(dbType);
            System.out.println("All Kunden:");
            allKunden.forEach(System.out::println);

            // Find a customer by ID
            Kunde foundKunde = kundenDAO.findById(1, dbType);
            System.out.println("Found Kunde with ID 1: " + foundKunde);

            // Delete a customer by ID
            kundenDAO.delete(2, dbType);
            System.out.println("Kunde with ID 2 deleted.");

            // Retrieve remaining customers
            List<Kunde> remainingKunden = kundenDAO.findAll(dbType);
            System.out.println("Remaining Kunden:");
            remainingKunden.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     */
}
