package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.Etappe;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

@SuppressWarnings("unused")
public class EtappeDao {

    private final Jdbi jdbi;

    public EtappeDao() {
        this.jdbi = JdbiManager.getJdbi();
    }

    public List<Etappe> getAll() {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Etappe").mapToBean(Etappe.class).list());
    }

    public void insert(Etappe etappe) {
        jdbi.useHandle(handle -> handle.createUpdate("INSERT INTO Etappe (afstandMeter, startPlaats, eindPlaats, wedstrijdId, naam) VALUES (:afstandMeter, :startPlaats, :eindPlaats, :wedstrijdId, :naam)").bindBean(etappe).execute());
    }
    public void delete(Etappe etappe) {
        jdbi.useHandle(handle -> handle.createUpdate("DELETE FROM Etappe WHERE wedstrijdId = :wedstrijdId AND naam = :naam").bind("wedstrijdId", etappe.getWedstrijdId()).bind("naam", etappe.getNaam()).execute());
    }

    public List<Etappe> getByWedstrijdId(int id) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Etappe WHERE WedstrijdId = :id").bind("id", id).mapToBean(Etappe.class).list());
    }

    public int getIdByName(String naam) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT id FROM Etappe WHERE naam = :naam").bind("naam", naam).mapToBean(Integer.class).findFirst().orElse(null));
    }

}