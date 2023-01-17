package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.*;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class WedstrijdDao {

    private final Jdbi jdbi;

    public WedstrijdDao()  {this.jdbi = JdbiManager.getJdbi();}

    public List<Wedstrijd> getAll() {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Wedstrijd")
                .mapToBean(Wedstrijd.class)
                .list());
    }

    public Wedstrijd getByNaam(String naam) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Wedstrijd WHERE naam = :naam")
                .bind("naam", naam)
                .mapToBean(Wedstrijd.class)
                .list().get(0));
    }

    public void insert(Wedstrijd wedstrijd) {
        jdbi.useHandle(handle -> handle.createUpdate("INSERT INTO Wedstrijd (naam, datum, plaats, inschrijvingsgeld, categorieID) VALUES (:naam, :datum, :plaats, :inschrijvingsgeld, :categorieID)")
                .bindBean(wedstrijd)
                .execute());
    }

    public void update(Wedstrijd wedstrijdNew, Wedstrijd wedstrijdOud) {
        jdbi.useHandle(handle -> handle.createUpdate("UPDATE Wedstrijd SET (naam, datum, plaats, inschrijvingsgeld, categorieID) = (:naam, :datum, :plaats, :inschrijvingsgeld, :categorieID) WHERE naam = :naamOud AND plaats = :plaatsOud")
                .bindBean(wedstrijdNew)
                .bind("naamOud", wedstrijdOud.getNaam())
                .bind("plaatsOud", wedstrijdOud.getPlaats())
                .execute());
    }

    public void delete(Wedstrijd wedstrijd) {
        jdbi.useHandle(handle -> handle.createUpdate("DELETE FROM Wedstrijd WHERE naam = :naam AND plaats = :plaats")
                .bind("naam", wedstrijd.getNaam())
                .bind("plaats", wedstrijd.getPlaats())
                .execute());
    }

    public Wedstrijd selectByNaamDatum(String naam, String datum) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Wedstrijd WHERE naam = :naam AND datum = :datum")
                .bind("naam", naam)
                .bind("datum", datum)
                .mapToBean(Wedstrijd.class)
                .list().get(0));
    }


    public int getTotaleAfstand(Wedstrijd wedstrijd) {
        String wedstrijdId = ConnectionManager.handle.createQuery("Select id FROM Wedstrijd WHERE naam = '" + wedstrijd.getNaam() + "' AND datum = '" + wedstrijd.getDatum() + "'").mapTo(String.class).list().get(0);
        List<Etappe> etappeList = ConnectionManager.handle.createQuery("Select * FROM Etappe WHERE wedstrijdID = '" + wedstrijdId + "'").mapToBean(Etappe.class).list();
        int totaleAfstand = 0;
        for (Etappe etappe : etappeList) {
            totaleAfstand = totaleAfstand + etappe.getAfstandMeter();
        }
        return totaleAfstand;
    }

    public void schrijfIn(Object user, Wedstrijd w) {
        if (user.getClass() == Loper.class){
            LoopNummerDao loopNummerDao = new LoopNummerDao();
            LoperDao loperDao = new LoperDao();
            Loper l = (Loper) user;
            List<LoopNummer> bestaandeNummers = loopNummerDao.getAllSorted();
            int nieuwNummer = bestaandeNummers.get(0).getNummer() + 1;
            int loperID = loperDao.getId(l);
            EtappeDao etappeDao = new EtappeDao();
            for (Etappe etappe : etappeDao.getWedstrijdEtappes(w)) {
                int etappeID = ConnectionManager.handle.createQuery("Select id FROM Etappe WHERE naam = '" + etappe.getNaam() + "'").mapTo(Integer.class).list().get(0);
                LoopNummer loopNummer = new LoopNummer(nieuwNummer, 0, loperID, etappeID);
                loopNummerDao.insert(loopNummer);
                }
        }
        if (user.getClass() == Medewerker.class){
            MedewerkerDao medewerkerDao = new MedewerkerDao();
            WedstrijdDao wedstrijdDao = new WedstrijdDao();
            Medewerker m = (Medewerker) user;
            ConnectionManager.handle.execute("INSERT INTO MedewerkerWedstrijd (MedewerkerID, WedstrijdID) VALUES ('"+ medewerkerDao.getId(m) +"', '" + wedstrijdDao.getId(w) + "')");
        }
    }

    public List<Wedstrijd> getInschreven(Object user) {
        String query = null;
        if (user.getClass() == Loper.class){
            query = "SELECT Wedstrijd.* FROM Wedstrijd " + "INNER JOIN Etappe ON Etappe.wedstrijdId = Wedstrijd.id " + "INNER JOIN LoopNummer ON LoopNummer.etappeId = Etappe.id " + "INNER JOIN Loper ON Loper.id = LoopNummer.loperId " + "WHERE Loper.eMail = '" + ((Loper) user).geteMail() + "'";
        }
        if (user.getClass() == Medewerker.class){
            query = "SELECT Wedstrijd.* FROM Wedstrijd " + "INNER JOIN MedewerkerWedstrijd ON Wedstrijd.id = MedewerkerWedstrijd.WedstrijdID " + "INNER JOIN Medewerker ON MedewerkerWedstrijd.medewerkerID = Medewerker.id " + "WHERE Medewerker.eMail = '" + ((Medewerker) user).geteMail() + "'";
        }
        return ConnectionManager.handle.createQuery(query).mapToBean(Wedstrijd.class).list();
    }

    public int getId(Wedstrijd w) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT id FROM Wedstrijd WHERE naam = :naam AND datum = :datum")
                .bind("naam", w.getNaam())
                .bind("datum", w.getDatum())
                .mapTo(Integer.class)
                .list().get(0));
    }
}