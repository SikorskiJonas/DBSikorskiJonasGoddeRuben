package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.Loper;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class MedewerkerWedstrijdDao {

    private final Jdbi jdbi;
    public MedewerkerWedstrijdDao() {this.jdbi = JdbiManager.getJdbi();}

    public void insert(int medewerkerid, int wedstrijdId) {
        jdbi.useHandle(handle -> handle.createUpdate("INSERT INTO MedewerkerWedstrijd (MedewerkerID, WedstrijdID) VALUES (:medewerkerId, :wedstrijdId)")
                .bind("medewerkerId", medewerkerid)
                .bind("wedstrijdId", wedstrijdId)
                .execute());
    }
}
