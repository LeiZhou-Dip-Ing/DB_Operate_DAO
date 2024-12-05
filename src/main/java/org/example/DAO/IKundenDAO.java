package org.example.DAO;

import org.example.Connector.DatabaseType;
import org.example.Model.Kunde;

import java.util.List;

public interface IKundenDAO {
    void save(Kunde kunde, DatabaseType dbType);
    List<Kunde> findAll(DatabaseType dbType);
    Kunde findById(int id, DatabaseType dbType);
    void update(Kunde kunde, DatabaseType dbType);
    void delete(int id, DatabaseType dbType);
}
