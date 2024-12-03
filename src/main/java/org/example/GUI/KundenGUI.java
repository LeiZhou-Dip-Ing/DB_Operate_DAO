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

public class KundenGUI extends JFrame {
    private final JTextField vornameField, nachnameField, adresseField, plzField, kundennummerField;
    private final IKundenDAO kundeDAO;
    private final JComboBox<DatabaseType> databaseTypeComboBox;

    @Inject
    public KundenGUI(IKundenDAO kundeDAO) {
        this.kundeDAO = kundeDAO;

        setTitle("Kunden Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Create a panel to hold the form fields (Group Box)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 10)); // Grid layout for form fields
        formPanel.setBorder(BorderFactory.createTitledBorder("Kunden Informationen"));

        // Create the fields inside the form panel
        kundennummerField = createFieldWithLabel(formPanel, "Kundennummer:");
        vornameField = createFieldWithLabel(formPanel, "Vorname:");
        nachnameField = createFieldWithLabel(formPanel, "Nachname:");
        adresseField = createFieldWithLabel(formPanel, "Adresse:");
        plzField = createFieldWithLabel(formPanel, "PLZ:");

        // Create the combo box for selecting database type
        databaseTypeComboBox = new JComboBox<>(DatabaseType.values());
        databaseTypeComboBox.setSelectedItem(DatabaseType.DERBY);  // Set DERBY as the default selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));  // Panel for ComboBox
        topPanel.add(new JLabel("Datenbanktyp:"));
        topPanel.add(databaseTypeComboBox);

        // Create buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Speichern");
        JButton showAllButton = new JButton("Anzeigen");
        JButton Aktualisieren = new JButton("Aktualisieren");
        buttonPanel.add(saveButton);
        buttonPanel.add(showAllButton);
        buttonPanel.add(Aktualisieren);

        // Add panels to the frame
        add(topPanel, BorderLayout.NORTH);  // ComboBox at the top
        add(formPanel, BorderLayout.CENTER);  // Form fields in the center
        add(buttonPanel, BorderLayout.SOUTH); // Buttons at the bottom

        // Event listeners
        saveButton.addActionListener(new SaveButtonListener());
        Aktualisieren.addActionListener(new AktualisierenButtonListener());
        showAllButton.addActionListener(e -> showAllKundenDialog());

        // Final UI setup
        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private JTextField createFieldWithLabel(JPanel panel, String labelText) {
        JLabel label = new JLabel(labelText);
        JTextField textField = new JTextField();
        panel.add(label);
        panel.add(textField);
        return textField;
    }

    // Show All Kunden Dialog
    private void showAllKundenDialog() {
        KundenDialog kundenDialog = new KundenDialog(this, kundeDAO,(DatabaseType) databaseTypeComboBox.getSelectedItem());
        kundenDialog.setVisible(true);
    }

    // Aktualisieren button listener
    private static class AktualisierenButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    // Save button listener
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

    private void saveData() {
        String kundennummerText = kundennummerField.getText().trim();
        String vorname = vornameField.getText().trim();
        String nachname = nachnameField.getText().trim();
        String adresse = adresseField.getText().trim();
        String plz = plzField.getText().trim();

        // Validate input
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

        // Save the data with DAO
        Kunde kunde = new Kunde();
        kunde.setKundennummer(kundennummer);
        kunde.setVorname(vorname);
        kunde.setNachname(nachname);
        kunde.setAdresse(adresse);
        kunde.setPlz(plz);
        kundeDAO.save(kunde, (DatabaseType) databaseTypeComboBox.getSelectedItem());
    }

    // Clear input fields
    private void clearFields() {
        kundennummerField.setText("");
        vornameField.setText("");
        nachnameField.setText("");
        adresseField.setText("");
        plzField.setText("");
    }
}
