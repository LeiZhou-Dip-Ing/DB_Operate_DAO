package org.example.DAO;

import com.google.inject.Inject;
import org.example.Config.IDatabaseConfig;
import org.example.Connector.DatabaseType;
import org.example.Connector.IDatabaseConnectorFactory;
import org.example.Model.Kunde;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KundenDAO extends AbstractDAO implements IKundenDAO {

    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE Kunden (" +
                    "KUNDENNUMMER INT PRIMARY KEY, " +
                    "VORNAME VARCHAR(100), " +
                    "NACHNAME VARCHAR(100), " +
                    "ADRESSE VARCHAR(200), " +
                    "PLZ VARCHAR(10))";

    private static final String INSERT_SQL = "INSERT INTO Kunden (KUNDENNUMMER, VORNAME, NACHNAME, ADRESSE, PLZ) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM Kunden WHERE KUNDENNUMMER = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM Kunden";
    private static final String DELETE_SQL = "DELETE FROM Kunden WHERE KUNDENNUMMER = ?";
    private static final String TABLE_NAME ="Kunden";


    @Inject
    protected KundenDAO(IDatabaseConnectorFactory connectorFactory, IDatabaseConfig config) {
        super(connectorFactory, config);
    }

    @Override
    protected boolean doesTableExist(Connection connection, String tableName) throws SQLException {
        String checkTableSQL = "SELECT * FROM " + tableName;
        try (Statement stmt = connection.createStatement()) {
            stmt.executeQuery(checkTableSQL);
            return true;
        } catch (SQLException e) {
            if (e.getSQLState().equals("42X05")) {  // 42X05: Table not found error in Derby
                return false;
            }
            throw e;
        }
    }

    @Override
    public void save(Kunde kunde , DatabaseType dbType) {
        ensureTableInitialized(dbType);
        try (Connection connection = getConnection(dbType);
             PreparedStatement stmt = connection.prepareStatement(INSERT_SQL)) {

            stmt.setInt(1, kunde.getKundennummer());
            stmt.setString(2, kunde.getVorname());
            stmt.setString(3, kunde.getNachname());
            stmt.setString(4, kunde.getAdresse());
            stmt.setString(5, kunde.getPlz());
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
        super.initializeTable(TABLE_NAME, CREATE_TABLE_SQL, dbType);
    }

    private Kunde mapToKunde(ResultSet rs) throws SQLException {
        Kunde kunde = new Kunde();
        kunde.setKundennummer(rs.getInt("KUNDENNUMMER"));
        kunde.setVorname(rs.getString("VORNAME"));
        kunde.setNachname(rs.getString("NACHNAME"));
        kunde.setAdresse(rs.getString("ADRESSE"));
        kunde.setPlz(rs.getString("PLZ"));
        return kunde;
    }
}
