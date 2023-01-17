package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.Categorie;
import be.kuleuven.vrolijkezweters.model.Functie;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

@SuppressWarnings("unused")
public class CategorieDao {

    private final Jdbi jdbi;

    public CategorieDao() {this.jdbi = JdbiManager.getJdbi();
    }

    public List<Categorie> getAll() {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Categorie")
                .mapToBean(Categorie.class)
                .list());
    }

    public void insert(Categorie categorie) {
        jdbi.useHandle(handle -> handle.createUpdate("INSERT INTO Categorie (categorie) VALUES (:categorie)")
                .bind("categorie", categorie)
                .execute());
    }

    public void update(Categorie categorieNew, String naamOud) {
        jdbi.useHandle(handle -> handle.createUpdate("UPDATE Categorie SET categorie = :categorieNew WHERE categorie= :categorieOld")
                .bind("categorieNew", categorieNew)
                .bind("categorieOld", naamOud)
                .execute());
    }

    public void delete(Categorie categorie) {
        jdbi.useHandle(handle -> handle.createUpdate("DELETE FROM Categorie WHERE categorie = :categorie")
                .bind("categorie", categorie.getCategorie())
                .execute());
    }

}