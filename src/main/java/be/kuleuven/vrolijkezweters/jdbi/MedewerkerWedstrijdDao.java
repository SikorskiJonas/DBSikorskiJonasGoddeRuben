package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.Medewerker;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class MedewerkerWedstrijdDao {

    private final Jdbi jdbi;

    public MedewerkerWedstrijdDao() {
        this.jdbi = JdbiManager.getJdbi();
    }

    public void insert(int medewerkerid, int wedstrijdId) {
        jdbi.useHandle(handle -> handle.createUpdate("INSERT INTO MedewerkerWedstrijd (MedewerkerID, WedstrijdID) VALUES (:medewerkerId, :wedstrijdId)").bind("medewerkerId", medewerkerid).bind("wedstrijdId", wedstrijdId).execute());
    }

    public void deleteAllForWedstrijd(Wedstrijd w){
        WedstrijdDao wedstrijdDao = new WedstrijdDao();
        jdbi.useHandle(handle -> handle.createUpdate("DELETE FROM MedewerkerWedstrijd WHERE wedstrijdId = :wedstrijdId").bind("wedstrijdId", wedstrijdDao.getId(w)).execute());
    }

    public void deleteAllForMedewerker(Medewerker m){
        MedewerkerDao medewerkerDao = new MedewerkerDao();
        jdbi.useHandle(handle -> handle.createUpdate("DELETE FROM MedewerkerWedstrijd WHERE medewerkerId = :medewerkerId").bind("medewerkerId", medewerkerDao.getId(m)).execute());
    }

    public List<Integer> getAllForMedewerkerWedstrijd(Wedstrijd w, Medewerker m) {
        WedstrijdDao wedstrijdDao = new WedstrijdDao();
        MedewerkerDao medewerkerDao = new MedewerkerDao();
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM MedewerkerWedstrijd WHERE medewerkerId = :medewerkerId AND wedstrijdId = :wedstrijdId").bind("medewerkerId", medewerkerDao.getId(m)).bind("wedstrijdId", wedstrijdDao.getId(w)).mapTo(Integer.class).list());
    }
}
