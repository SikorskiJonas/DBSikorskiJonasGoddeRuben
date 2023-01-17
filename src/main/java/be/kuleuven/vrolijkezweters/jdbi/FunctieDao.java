package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.Functie;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class FunctieDao {

    private final Jdbi jdbi;

    public FunctieDao() {
      this.jdbi = JdbiManager.getJdbi();
    }

    public List<Functie> getAll() {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Functie")
                .mapToBean(Functie.class)
                .list());
    }

    public void insert(Functie functie) {
        jdbi.useHandle(handle -> handle.createUpdate("INSERT INTO Functie (functie) VALUES (:functie)")
                .bind("functie", functie)
                .execute());
    }

    public void update(Functie naamNew, String naamOud) {
        jdbi.useHandle(handle -> handle.createUpdate("UPDATE Functie SET functie = :functieNew WHERE functie= :functieOld")
                .bind("functieNew", naamNew)
                .bind("funtieOld", naamOud)
                .execute());
    }

    public void delete(Functie functie) {
        jdbi.useHandle(handle -> handle.createUpdate("DELETE FROM Functie WHERE functie = :functie")
                .bind("functie", functie.getFunctie())
                .execute());
    }

}