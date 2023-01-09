package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.Functie;

import java.util.List;

public class FunctieJdbi {
    private ConnectionManager connectionManager;

    public FunctieJdbi(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }

    public List<Functie> getAll() {
        return connectionManager.handle.createQuery("SELECT * FROM Functie")
                .mapToBean(Functie.class)
                .list();
    }

    public void insert(Functie functie) {
        ConnectionManager.handle.createUpdate("INSERT INTO Functie (functie) VALUES (:functie)")
                .bindBean(functie)
                .execute();
    }

    public void update(Functie functieNew, String naamOud) {
        String updateQuery = "UPDATE Functie SET " +
                " functie ='" + functieNew.getFunctie() +
                "' WHERE functie= '" + naamOud + "'";
        ConnectionManager.handle.execute(updateQuery);
    }

    public void delete(Functie functie) {
        String q = "DELETE FROM Functie WHERE functie = '" + functie.getFunctie() + "'";
        ConnectionManager.handle.execute(q);
    }

    public Functie selectByNaam(String naam) {
        return connectionManager.handle.createQuery("Select * FROM Functie WHERE functie = '" + naam +"'")
                .mapToBean(Functie.class)
                .list().get(0);
    }

}