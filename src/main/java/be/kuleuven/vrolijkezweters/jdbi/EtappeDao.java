package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.Etappe;
import be.kuleuven.vrolijkezweters.model.Functie;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

@SuppressWarnings("unused")
public class EtappeDao {

    private final Jdbi jdbi;

    public EtappeDao() {this.jdbi = JdbiManager.getJdbi();}

    public List<Etappe> getAll() {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Etappe")
                .mapToBean(Etappe.class)
                .list());
    }

    public void insert(Etappe etappe) {
        jdbi.useHandle(handle -> handle.createUpdate("INSERT INTO Etappe (afstandMeter, startPlaats, eindPlaats, wedstrijdId, naam) VALUES (:afstandMeter, :startPlaats, :eindPlaats, :wedstrijdId, :naam)")
                .bindBean(etappe)
                .execute());
        }

    public void delete(Etappe etappe) {
        jdbi.useHandle(handle -> handle.createUpdate("DELETE FROM Etappe WHERE wedstrijdId = :wedstrijdId AND naam = :naam")
                .bind("wedstrijdId", etappe.getWedstrijdId())
                .bind("naam", etappe.getNaam())
                .execute());
    }

    public List<Etappe> getWedstrijdEtappes(Wedstrijd w) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Etappe INNER JOIN Wedstrijd on Etappe.wedstrijdId = Wedstrijd.id WHERE Wedstrijd.naam = :naam AND Wedstrijd.datum = :datum")
                .bind("naam", w.getNaam())
                .bind("datum", w.getDatum())
                .mapToBean(Etappe.class)
                .list());
    }
}