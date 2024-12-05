package org.example.GUI.Dialog;

import org.example.Model.Kunde;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class KundenEditDialog extends JDialog {
    private final JTextField txtVorname;
    private final JTextField txtNachname;
    private final JTextField txtAdresse;
    private final JTextField txtPLZ;
    private final JTextField txtOrt;
    private final JButton btnConfirm;
    private Kunde currentKunde;  // Save current clients to be added/updated

    public KundenEditDialog(Frame parent) {
        super(parent, "Kunde hinzufügen / aktualisieren", true);
        setLayout(new BorderLayout());

        // Input field area
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5)); // 5x2 grid layout, spacing of 5
        inputPanel.add(new JLabel("Vorname:"));
        txtVorname = new JTextField();
        inputPanel.add(txtVorname);

        inputPanel.add(new JLabel("Nachname:"));
        txtNachname = new JTextField();
        inputPanel.add(txtNachname);

        inputPanel.add(new JLabel("Adresse:"));
        txtAdresse = new JTextField();
        inputPanel.add(txtAdresse);

        inputPanel.add(new JLabel("PLZ:"));
        txtPLZ = new JTextField();
        inputPanel.add(txtPLZ);

        inputPanel.add(new JLabel("Ort:"));
        txtOrt = new JTextField();
        inputPanel.add(txtOrt);

        // Wrap the input field area in a grouping panel
        JPanel groupPanel = new JPanel(new BorderLayout());
        groupPanel.setBorder(BorderFactory.createTitledBorder("Kundendaten eingeben"));
        groupPanel.add(inputPanel, BorderLayout.CENTER);
        add(groupPanel, BorderLayout.CENTER);

        // button area
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Center-aligned button area
        btnConfirm = new JButton("Bestätigen");
        JButton btnCancel = new JButton("Abbrechen");
        buttonPanel.add(btnConfirm);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        // Close dialog box
        btnCancel.addActionListener(e -> dispose());
        setSize(350, 300);
        setLocationRelativeTo(parent);
    }

    // Setting current customer data (for updates)
    public void setKundeData(Kunde kunde) {
        currentKunde = kunde;
        if (currentKunde != null) {
            txtVorname.setText(kunde.getVorname());
            txtNachname.setText(kunde.getNachname());
            txtAdresse.setText(kunde.getAdresse());
            txtPLZ.setText(kunde.getPlz());
            txtOrt.setText(kunde.getOrt());
        }
    }

    // Getting user input
    public String getVorname() {
        return txtVorname.getText();
    }

    public String getNachname() {
        return txtNachname.getText();
    }

    public String getAdresse() {
        return txtAdresse.getText();
    }

    public String getPLZ() {
        return txtPLZ.getText();
    }

    public String getOrt() {
        return txtOrt.getText();
    }

    // Adding a Confirm Button Listener
    public void addConfirmListener(ActionListener listener) {
        btnConfirm.addActionListener(listener);
    }

    // Get current client (for modifying data when updating)
    public Kunde getCurrentKunde() {
        return currentKunde;
    }
}
