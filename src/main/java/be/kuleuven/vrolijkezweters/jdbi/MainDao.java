package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.*;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class MainDao {

    private final Jdbi jdbi;

    public MainDao() {
        this.jdbi = JdbiManager.getJdbi();
    }

    public List<KlassementObject> getLoopTijdenByWedstrijd(String selectedWedstrijd) {
        return jdbi.withHandle((handle -> handle.createQuery("SELECT LoperId, Loper.voornaam, Loper.naam, Sum(looptijd) AS looptijd FROM loopNummer INNER JOIN Etappe on Etappe.id = loopNummer.etappeId INNER JOIN Loper on Loper.id = loopNummer.loperId INNER JOIN Wedstrijd on Wedstrijd.id = Etappe.wedstrijdId WHERE Wedstrijd.naam = :wedstrijdNaam GROUP BY loperId " + "ORDER BY looptijd ASC").bind("wedstrijdNaam", selectedWedstrijd).mapToBean(KlassementObject.class).list()));
    }

    public List<KlassementObject> getGemiddeldeSnelheid() {
        return jdbi.withHandle((handle -> handle.createQuery("SELECT LoperId, Loper.voornaam, Loper.naam, ROUND((SUM(Etappe.afstandMeter) / ROUND(SUM(looptijd),4)) * 3.6,2) AS snelheid FROM loopNummer INNER JOIN Etappe on Etappe.id = loopNummer.etappeId INNER JOIN Loper on Loper.id = loopNummer.loperId INNER JOIN Wedstrijd on Wedstrijd.id = Etappe.wedstrijdId WHERE looptijd IS NOT NULL GROUP BY loperId ORDER BY snelheid DESC").mapToBean(KlassementObject.class).list()));
    }

    public List<LoopNummer> selectByLoperWedstrijd(String loperNaam, String wedstrijdNaam) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM LoopNummer INNER JOIN Loper ON Loopnummer.loperId = Loper.id INNER JOIN Etappe ON Loopnummer.etappeId = Etappe.id INNER JOIN Wedstrijd ON Wedstrijd.id = Etappe.wedstrijdId WHERE Loper.naam = :loperNaam AND Wedstrijd.naam = :wedstrijdNaam").bind("loperNaam", loperNaam).bind("wedstrijdNaam", wedstrijdNaam).mapToBean(LoopNummer.class).list());
    }

    public List<Wedstrijd> getWedstrijdenByLoperEmail(Object user) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT DISTINCT Wedstrijd.* FROM Wedstrijd INNER JOIN Etappe ON Etappe.wedstrijdId = Wedstrijd.id INNER JOIN LoopNummer ON LoopNummer.etappeId = Etappe.id INNER JOIN Loper ON Loper.id = LoopNummer.loperId WHERE Loper.eMail = :eMail").bind("eMail", ((Loper) user).geteMail()).mapToBean(Wedstrijd.class).list());
    }

    public List<Wedstrijd> getWedstrijdenByMedewerkerEmail(Object user) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT DISTINCT Wedstrijd.* FROM Wedstrijd INNER JOIN MedewerkerWedstrijd ON Wedstrijd.id = MedewerkerWedstrijd.WedstrijdID INNER JOIN Medewerker ON MedewerkerWedstrijd.medewerkerID = Medewerker.id WHERE Medewerker.eMail = :eMail").bind("eMail", ((Medewerker) user).geteMail()).mapToBean(Wedstrijd.class).list());
    }
}
