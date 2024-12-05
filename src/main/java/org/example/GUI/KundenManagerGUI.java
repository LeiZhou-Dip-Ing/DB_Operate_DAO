package org.example.GUI;

import com.google.inject.Inject;
import org.example.DAO.IKundenDAO;
import org.example.GUI.Dialog.KundenEditDialog;
import org.example.Model.Kunde;
import org.example.Connector.DatabaseType;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.List;

public class KundenManagerGUI extends JFrame {
    private final IKundenDAO kundenDAO;
    private final DefaultMutableTreeNode rootNode;
    private final JTree treeList;
    private final JLabel lblKundenID, lblVorname, lblNachname, lblAdresse, lblPLZ, lblOrt;
    private final JTextField txtKundenIDSearch;
    private final JComboBox<DatabaseType> databaseTypeComboBox;
    //private final DatabaseType dbType;

    @Inject
    public KundenManagerGUI(IKundenDAO dao) {
        this.kundenDAO = dao;
        //this.dbType = dbType;


        setTitle("Kundenverwaltung");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        rootNode = new DefaultMutableTreeNode("Kundenliste");


        // Create the combo box for selecting database type
        databaseTypeComboBox = new JComboBox<>(DatabaseType.values());
        databaseTypeComboBox.setSelectedItem(DatabaseType.DERBY);  // Set DERBY as the default selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));  // Panel for ComboBox
        topPanel.add(new JLabel("Datenbanktyp:"));
        topPanel.add(databaseTypeComboBox);

        // initial treeList
        treeList = new JTree(rootNode);
        treeList.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        refreshKundenList(); //  Reload customer list data
        JScrollPane treeScrollPane = new JScrollPane(treeList);
        treeScrollPane.setBorder(BorderFactory.createTitledBorder("Kundenliste"));

        //customer details area
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Kunde Details"));

        lblKundenID = new JLabel("KundenID: ");
        lblVorname = new JLabel("Vorname: ");
        lblNachname = new JLabel("Nachname: ");
        lblAdresse = new JLabel("Adresse: ");
        lblPLZ = new JLabel("PLZ: ");
        lblOrt = new JLabel("Ort: ");

        detailsPanel.add(lblKundenID);
        detailsPanel.add(lblVorname);
        detailsPanel.add(lblNachname);
        detailsPanel.add(lblAdresse);
        detailsPanel.add(lblPLZ);
        detailsPanel.add(lblOrt);

        JScrollPane detailsScrollPane = new JScrollPane(detailsPanel);
        detailsScrollPane.setBorder(BorderFactory.createTitledBorder("Kunde Details"));

        // Search box and buttons
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtKundenIDSearch = new JTextField(10);
        JButton btnSearch = new JButton("Suchen");

        searchPanel.add(new JLabel("KundenID:"));
        searchPanel.add(txtKundenIDSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(topPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, detailsScrollPane);
        splitPane.setDividerLocation(200);
        add(splitPane, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);

        // add, display and update
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAdd = new JButton("Hinzufügen");
        JButton btnShow = new JButton("Anzeigen");
        JButton btnUpdate = new JButton("Aktualisieren");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnShow);
        buttonPanel.add(btnUpdate);
        add(buttonPanel, BorderLayout.SOUTH);
        // display all customer
        btnShow.addActionListener(e -> showAllKundenDetails());

        treeList.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeList.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode.isLeaf()) {
                Kunde selectedKunde = (Kunde) selectedNode.getUserObject();
                // clear customers
                clearDetailsPanel();
                updateDetailsPanel(selectedKunde);
            } else {
                clearDetailsPanel();
            }
        });

        btnAdd.addActionListener(e -> {
            KundenEditDialog addDialog = new KundenEditDialog(this);
            addDialog.addConfirmListener(e1 -> handleAddKunde(addDialog));
            addDialog.setVisible(true);
        });

        btnUpdate.addActionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeList.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode.isLeaf()) {
                Kunde selectedKunde = (Kunde) selectedNode.getUserObject();
                KundenEditDialog updateDialog = new KundenEditDialog(this);
                updateDialog.setKundeData(selectedKunde);  // 填充当前客户数据到对话框
                updateDialog.addConfirmListener(e1 -> {
                    // Getting changed data from a dialog box
                    selectedKunde.setVorname(updateDialog.getVorname());
                    selectedKunde.setNachname(updateDialog.getNachname());
                    selectedKunde.setAdresse(updateDialog.getAdresse());
                    selectedKunde.setPlz(updateDialog.getPLZ());
                    selectedKunde.setOrt(updateDialog.getOrt());
                    // Updating the database
                    kundenDAO.update(selectedKunde, (DatabaseType) databaseTypeComboBox.getSelectedItem());
                    // Refresh the client list on the JTree
                    refreshKundenList();  // Reload the customer list to ensure that the data on the JTree is updated
                    // Close dialog box
                    updateDialog.dispose();
                });
                updateDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Bitte wählen Sie einen Kunden aus der Liste.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSearch.addActionListener(e -> {
            String kundenIDText = txtKundenIDSearch.getText().trim();
            try {
                int kundenID = Integer.parseInt(kundenIDText);
                Kunde foundKunde = kundenDAO.findById(kundenID, (DatabaseType) databaseTypeComboBox.getSelectedItem()); // Finding Customers with DAO
                if (foundKunde != null) {
                    // Clear the details panel on the right
                    clearDetailsPanel();

                    updateDetailsPanel(foundKunde);
                    selectKundeInTree(foundKunde);
                } else {
                    JOptionPane.showMessageDialog(this, "Kunde mit der ID " + kundenID + " nicht gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Bitte geben Sie eine gültige KundenID ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        setSize(800, 500);
        setLocationRelativeTo(null);
    }

    private void refreshKundenList() {
        rootNode.removeAllChildren();  // Empty existing tree nodes
        // Get the latest customer data from the database
        List<Kunde> kundenList = kundenDAO.findAll((DatabaseType) databaseTypeComboBox.getSelectedItem());
        // Iterate over all clients and re-add them to the tree
        for (Kunde kunde : kundenList) {
            rootNode.add(new DefaultMutableTreeNode(kunde));
        }
        // Updating the tree model and refreshing the tree view
        ((DefaultTreeModel) treeList.getModel()).reload();
        // Force a redraw of the JTree to ensure updates are visible
        treeList.repaint();
        // If there are selected nodes, they need to be reselected
        if (treeList.getSelectionPath() != null) {
            treeList.setSelectionPath(treeList.getSelectionPath());
        }
    }

    private void handleAddKunde(KundenEditDialog addDialog) {
        String vorname = addDialog.getVorname();
        String nachname = addDialog.getNachname();
        String adresse = addDialog.getAdresse();
        String plz = addDialog.getPLZ();
        String ort = addDialog.getOrt();

        Kunde newKunde = new Kunde(0, vorname, nachname, adresse, plz, ort); // IDs are automatically generated by the database
        kundenDAO.save(newKunde, (DatabaseType) databaseTypeComboBox.getSelectedItem());
        refreshKundenList();
        addDialog.dispose();
    }

    private void updateDetailsPanel(Kunde selectedKunde) {
        lblKundenID.setText("KundenID: " + selectedKunde.getKundenID());
        lblVorname.setText("Vorname: " + selectedKunde.getVorname());
        lblNachname.setText("Nachname: " + selectedKunde.getNachname());
        lblAdresse.setText("Adresse: " + selectedKunde.getAdresse());
        lblPLZ.setText("PLZ: " + selectedKunde.getPlz());
        lblOrt.setText("Ort: " + selectedKunde.getOrt());
    }

    private void clearDetailsPanel() {
        lblKundenID.setText("KundenID: ");
        lblVorname.setText("Vorname: ");
        lblNachname.setText("Nachname: ");
        lblAdresse.setText("Adresse: ");
        lblPLZ.setText("PLZ: ");
        lblOrt.setText("Ort: ");
    }

    private void selectKundeInTree(Kunde kunde) {
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            if (((Kunde) node.getUserObject()).getKundenID() == kunde.getKundenID()) {
                treeList.setSelectionPath(treeList.getPathForRow(i+1));
                break;
            }
        }
    }

    private void showAllKundenDetails() {
        clearDetailsPanel();
        // Access to all customer data
        List<Kunde> kundenList = kundenDAO.findAll((DatabaseType) databaseTypeComboBox.getSelectedItem());
        // Clear the right display area first
        lblKundenID.setText("");
        lblVorname.setText("");
        lblNachname.setText("");
        lblAdresse.setText("");
        lblPLZ.setText("");
        lblOrt.setText("");

        // Splice all customer details
        StringBuilder allDetails = new StringBuilder();
        for (Kunde kunde : kundenList) {
            allDetails.append("KundenID: ").append(kunde.getKundenID()).append("\n")
                    .append("Vorname: ").append(kunde.getVorname()).append("\n")
                    .append("Nachname: ").append(kunde.getNachname()).append("\n")
                    .append("Adresse: ").append(kunde.getAdresse()).append("\n")
                    .append("PLZ: ").append(kunde.getPlz()).append("\n")
                    .append("Ort: ").append(kunde.getOrt()).append("\n\n");
        }

        System.out.println(allDetails);
        // Display all customer details in the details area
        lblKundenID.setText("<html>" + allDetails.toString().replaceAll("\n", "<br>") + "</html>");
        lblVorname.setText("");
        lblNachname.setText("");
        lblAdresse.setText("");
        lblPLZ.setText("");
        lblOrt.setText("");
    }
}
