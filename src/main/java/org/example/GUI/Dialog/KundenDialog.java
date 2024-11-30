package org.example.GUI.Dialog;

import org.example.Connector.DatabaseType;
import org.example.DAO.IKundenDAO;
import org.example.Model.Kunde;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class KundenDialog extends JDialog {
    private IKundenDAO kundenDAO;
    public KundenDialog(Frame parent, IKundenDAO kundeDAO) {
        super(parent, "Alle Kunden", true); // true makes it modal (blocks interaction with other windows)
        this.kundenDAO = kundeDAO;

        // Set up dialog window size and layout
        setSize(600, 400);
        setLocationRelativeTo(parent);  // Center the dialog relative to parent window
        // Create components to show customers' data
        displayAllKunden();
    }

    // Method to display all customers in the dialog
    private void displayAllKunden() {
        List<Kunde> allKunden = kundenDAO.findAll(DatabaseType.DERBY);
        // Prepare a string to display all customers
        StringBuilder displayText = new StringBuilder();
        for (Kunde kunde : allKunden) {
            displayText.append("Kundennummer: ").append(kunde.getKundennummer()).append("\n")
                    .append("Vorname: ").append(kunde.getVorname()).append("\n")
                    .append("Nachname: ").append(kunde.getNachname()).append("\n")
                    .append("Adresse: ").append(kunde.getAdresse()).append("\n")
                    .append("PLZ: ").append(kunde.getPlz()).append("\n\n");
        }

        //display kunden in console
        System.out.println(displayText);

        // Create a JTextArea to display the customer data
        JTextArea textArea = new JTextArea(displayText.toString());
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
    }
}
