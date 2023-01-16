package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.Categorie;

import java.util.List;

public class CategorieJdbi {
    private final ConnectionManager connectionManager;

    public CategorieJdbi(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }

    public List<Categorie> getAll() {
        return connectionManager.handle.createQuery("SELECT * FROM Categorie")
                .mapToBean(Categorie.class)
                .list();
    }

    public void insert(Categorie categorie) {
        ConnectionManager.handle.createUpdate("INSERT INTO Categorie (categorie) VALUES (:categorie)")
                .bindBean(categorie)
                .execute();
    }

    public void update(Categorie categorieNew, String naamOud) {
        String updateQuery = "UPDATE Categorie SET " +
                " categorie ='" + categorieNew.getCategorie() +
                "' WHERE categorie= '" + naamOud + "'";
        ConnectionManager.handle.execute(updateQuery);
    }

    public void delete(Categorie categorie) {
        String q = "DELETE FROM Categorie WHERE categorie = '" + categorie.getCategorie() + "'";
        ConnectionManager.handle.execute(q);
    }

    public Categorie selectByNaam(String naam) {
        return connectionManager.handle.createQuery("Select * FROM Categorie WHERE categorie = '" + naam +"'")
                .mapToBean(Categorie.class)
                .list().get(0);
    }
}