package org.example.GUI;

import com.google.inject.Inject;
import org.example.Connector.DatabaseType;
import org.example.DAO.IKundenDAO;
import org.example.GUI.Dialog.KundenDialog;
import org.example.Model.Kunde;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KundenGUI  extends JFrame {
    private JTextField vornameField, nachnameField, adresseField, plzField,kundennummerField;
    // inject IKundeDAO with IOC Guice
    private final IKundenDAO kundeDAO;

    @Inject
    public KundenGUI(IKundenDAO kundeDAO) {
        this.kundeDAO = kundeDAO;

        setTitle("Kunden Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 2, 10, 10));

        kundennummerField = createFieldWithLabel("Kundennummer:");
        vornameField = createFieldWithLabel("Vorname:");
        nachnameField = createFieldWithLabel("Nachname:");
        adresseField = createFieldWithLabel("Adresse:");
        plzField = createFieldWithLabel("PLZ:");

        // Create the button and bind the event
        JButton saveButton = new JButton("Speichern");

        JButton showAllButton = new JButton("Alle Kunden anzeigen");

        add(new JLabel()); // Empty label to occupy space
        add(saveButton);

        add(new JLabel()); // Empty label to occupy space
        add(showAllButton);

        saveButton.addActionListener(new SaveButtonListener()); //add button event listener
        showAllButton.addActionListener(e -> showAllKundenDialog());

        pack();
        setLocationRelativeTo(null);// Window centering
        setVisible(true);
    }

    // Show All Kunden Dialog
    private void showAllKundenDialog() {
        // Create a new dialog and display it
        KundenDialog kundenDialog = new KundenDialog(this, kundeDAO);
        kundenDialog.setVisible(true);  // Display the dialog
    }

    // Tool method: create labeled fields
    private JTextField createFieldWithLabel(String labelText) {
        JLabel label = new JLabel(labelText);
        JTextField textField = new JTextField();
        add(label);
        add(textField);
        return textField;
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                saveData();
                JOptionPane.showMessageDialog(KundenGUI.this, "Daten erfolgreich gespeichert!");
                clearFields();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(KundenGUI.this, ex.getMessage(),
                        "Eingabefehler", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(KundenGUI.this, "Fehler beim Speichern: " + ex.getMessage(),
                        "Datenbankfehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Methods for saving data to the database (via DAO)
    private void saveData() {
        String kundennummerText = kundennummerField.getText().trim();
        String vorname = vornameField.getText().trim();
        String nachname = nachnameField.getText().trim();
        String adresse = adresseField.getText().trim();
        String plz = plzField.getText().trim();

        //validate input
        if (kundennummerText.isEmpty() || vorname.isEmpty() || nachname.isEmpty() || adresse.isEmpty() || plz.isEmpty()) {
            throw new IllegalArgumentException("Alle Felder müssen ausgefüllt sein!");
        }

        // Convert the Kundennummer text to an integer
        int kundennummer;
        try {
            kundennummer = Integer.parseInt(kundennummerText);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Kundennummer muss eine gültige Zahl sein!");
        }

        // save date with DAO
        Kunde kunde = new Kunde();
        kunde.setKundennummer(kundennummer);  // Set the Kundennummer
        kunde.setVorname(vorname);
        kunde.setNachname(nachname);
        kunde.setAdresse(adresse);
        kunde.setPlz(plz);
        kundeDAO.save(kunde, DatabaseType.DERBY);
    }

    // Clear the input fields
    private void clearFields() {
        kundennummerField.setText("");
        vornameField.setText("");
        nachnameField.setText("");
        adresseField.setText("");
        plzField.setText("");
    }
}

