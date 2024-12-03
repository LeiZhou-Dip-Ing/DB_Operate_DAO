package org.example.GUI.Dialog;

import org.example.Connector.DatabaseType;
import org.example.DAO.IKundenDAO;
import org.example.Model.Kunde;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class KundenDialog extends JDialog {
    private final IKundenDAO kundenDAO;
    private final DatabaseType databaseType;
    public KundenDialog(Frame parent, IKundenDAO kundeDAO, DatabaseType databaseType) {
        super(parent, "Alle Kunden", true); // true makes it modal (blocks interaction with other windows)
        this.kundenDAO = kundeDAO;
        this.databaseType = databaseType;
        // Set up dialog window size and layout
        setSize(600, 400);
        setLocationRelativeTo(parent);  // Center the dialog relative to parent window
        // Create components to show customers' data
        displayAllKunden();
    }

    // Method to display all customers in the dialog
    private void displayAllKunden() {
        // Create a JTextArea to display customer data
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);  // Make the text area read-only

        // Wrap the JTextArea in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(560, 300));  // Set a preferred size for the scroll pane

        // Create a panel for buttons (optional)
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Schließen");
        closeButton.addActionListener(e -> dispose());  // Close the dialog when button is clicked
        buttonPanel.add(closeButton);

        // Set layout and add components to dialog
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Display the customers or error message
        try {
            // Attempt to fetch all customers from the database
            List<Kunde> allKunden = kundenDAO.findAll(databaseType);

            // Prepare a string to display all customers
            StringBuilder displayText = new StringBuilder();
            for (Kunde kunde : allKunden) {
                displayText.append("Kundennummer: ").append(kunde.getKundennummer()).append("\n")
                        .append("Vorname: ").append(kunde.getVorname()).append("\n")
                        .append("Nachname: ").append(kunde.getNachname()).append("\n")
                        .append("Adresse: ").append(kunde.getAdresse()).append("\n")
                        .append("PLZ: ").append(kunde.getPlz()).append("\n\n");
            }

            // Set the text of the JTextArea
            textArea.setText(displayText.toString());

        } catch (Exception ex) {
            // If an error occurs, display the error message in the JTextArea
            textArea.setText("Fehler beim Abrufen der Kundendaten: " + ex.getMessage());
        }
    }
}
