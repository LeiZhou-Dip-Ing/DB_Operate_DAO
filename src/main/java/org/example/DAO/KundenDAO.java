package org.example.DAO;

import com.google.inject.Inject;
import org.example.Config.UserConfig;
import org.example.Connector.DatabaseType;
import org.example.Connector.IDatabaseConnectorFactory;
import org.example.Model.Kunde;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KundenDAO extends AbstractDAO implements IKundenDAO {

    private static final String CREATE_TABLE_SQL_MYSQL =
            "CREATE TABLE Kunden (" +
                    "KundenID INT AUTO_INCREMENT PRIMARY KEY, " +  // Use self-incrementing KundenID
                    "Vorname VARCHAR(255), " +  // Vorname field
                    "Nachname VARCHAR(255), " +  // Nachname field
                    "Adresse VARCHAR(255), " +  // Adresse field
                    "PLZ VARCHAR(5), " +  // PLZ field
                    "Ort VARCHAR(255))";  // Ort field

    private static final String CREATE_TABLE_SQL_DERBY =
            "CREATE TABLE Kunden (" +
                    "KundenID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +  // Use self-incrementing KundenID
                    "Vorname VARCHAR(255), " +  // Vorname field
                    "Nachname VARCHAR(255), " +  // Nachname field
                    "Adresse VARCHAR(255), " +  // Adresse field
                    "PLZ VARCHAR(5), " +  // PLZ field
                    "Ort VARCHAR(255))";  // Ort field

    private static final String updateSQL = "UPDATE Kunden SET " +
            "Vorname = ?, " +
            "Nachname = ?, " +
            "Adresse = ?, " +
            "PLZ = ?, " +
            "Ort = ? " +
            "WHERE KundenID = ?";

    private static final String INSERT_SQL_MYSQL =
            "INSERT INTO Kunden (Vorname, Nachname, Adresse, PLZ, Ort) VALUES (?, ?, ?, ?, ?)";

    private static final String INSERT_SQL_DERBY =
            "INSERT INTO Kunden (Vorname, Nachname, Adresse, PLZ, Ort) VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM Kunden WHERE KundenID = ?";  // 使用 KundenID

    private static final String SELECT_ALL_SQL =
            "SELECT * FROM Kunden";

    private static final String DELETE_SQL =
            "DELETE FROM Kunden WHERE KundenID = ?";  // 使用 KundenID

    private static final String TABLE_NAME = "Kunden";


    @Inject
    protected KundenDAO(IDatabaseConnectorFactory connectorFactory, UserConfig userConfig) {
        super(connectorFactory, userConfig.getConfigFileName(), userConfig.getConfigType());
    }

    @Override
    protected boolean doesTableExist(Connection connection, String tableName) {
        String checkTableSQL = "SELECT * FROM " + tableName;
        try (Statement stmt = connection.createStatement()) {
            stmt.executeQuery(checkTableSQL);
            return true;
        } catch (SQLException e) {
            if (e.getSQLState().equals("42X05")) {  // 42X05: Table not found error in Derby
                return false;
            }
            //other database
            return false;
        }
    }

    @Override
    public void save(Kunde kunde , DatabaseType dbType) {
        ensureTableInitialized(dbType);
        try (Connection connection = getConnection(dbType);
             PreparedStatement stmt = connection.prepareStatement(getInsertSQL(dbType))) {

            stmt.setString(1, kunde.getVorname());
            stmt.setString(2, kunde.getNachname());
            stmt.setString(3, kunde.getAdresse());
            stmt.setString(4, kunde.getPlz());
            stmt.setString(5, kunde.getOrt());
            stmt.executeUpdate();
            System.out.println("Kunde gespeichert: " + kunde);
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Speichern des Kunden", e);
        }
    }

    @Override
    public List<Kunde> findAll(DatabaseType dbType) {
        ensureTableInitialized(dbType);
        List<Kunde> kundenList = new ArrayList<>();

        try (Connection connection = getConnection(dbType);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                kundenList.add(mapToKunde(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Abrufen aller Kunden", e);
        }

        return kundenList;
    }

    @Override
    public Kunde findById(int id , DatabaseType dbType) {
        ensureTableInitialized(dbType);
        try (Connection connection = getConnection(dbType);
             PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapToKunde(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Abrufen des Kunden", e);
        }
        return null;
    }

    @Override
    public void update(Kunde kunde , DatabaseType dbType) {
        ensureTableInitialized(dbType);
        try (Connection connection = getConnection(dbType);
             PreparedStatement stmt = connection.prepareStatement(updateSQL)) {

            // Setting the updated data
            stmt.setString(1, kunde.getVorname());
            stmt.setString(2, kunde.getNachname());
            stmt.setString(3, kunde.getAdresse());
            stmt.setString(4, kunde.getPlz());
            stmt.setString(5, kunde.getOrt());
            stmt.setInt(6, kunde.getKundenID());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Kunde mit ID " + kunde.getKundenID() + " wurde aktualisiert.");
            } else {
                System.out.println("Kein Kunde mit ID " + kunde.getKundenID() + " gefunden.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Aktualisieren des Kunden", e);
        }
    }

    @Override
    public void delete(int id , DatabaseType dbType) {
        ensureTableInitialized(dbType);
        try (Connection connection = getConnection(dbType);
             PreparedStatement stmt = connection.prepareStatement(DELETE_SQL)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Kunde mit ID " + id + " wurde gelöscht.");
            } else {
                System.out.println("Kein Kunde mit ID " + id + " gefunden.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Löschen des Kunden", e);
        }
    }

    private void ensureTableInitialized(DatabaseType dbType) {
        super.initializeTable(TABLE_NAME, getCreateTableSQL(dbType), dbType);
    }

    private Kunde mapToKunde(ResultSet rs) throws SQLException {
        Kunde kunde = new Kunde();
        kunde.setKundenID(rs.getInt("KundenID"));
        kunde.setVorname(rs.getString("Vorname"));
        kunde.setNachname(rs.getString("Nachname"));
        kunde.setAdresse(rs.getString("Adresse"));
        kunde.setPlz(rs.getString("PLZ"));
        kunde.setOrt(rs.getString("Ort"));
        return kunde;
    }


    private String getInsertSQL(DatabaseType dbType) {
        switch (dbType) {
            case MYSQL:
                return INSERT_SQL_MYSQL;
            case DERBY:
                return INSERT_SQL_DERBY;
            default:
                throw new IllegalArgumentException("Unsupported database type: " + dbType);
        }
    }

    private String getCreateTableSQL(DatabaseType dbType){
        switch (dbType) {
            case MYSQL:
                return CREATE_TABLE_SQL_MYSQL;
            case DERBY:
                return CREATE_TABLE_SQL_DERBY;
            default:
                throw new IllegalArgumentException("Unsupported database type: " + dbType);
        }
    }
}
