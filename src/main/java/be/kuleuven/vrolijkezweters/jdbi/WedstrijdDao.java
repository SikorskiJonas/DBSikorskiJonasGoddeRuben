package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Medewerker;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class WedstrijdDao {

    private final Jdbi jdbi;

    public WedstrijdDao() {
        this.jdbi = JdbiManager.getJdbi();
    }

    public List<Wedstrijd> getAll() {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Wedstrijd").mapToBean(Wedstrijd.class).list());
    }

    public Wedstrijd getByNaam(String naam) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Wedstrijd WHERE naam = :naam").bind("naam", naam).mapToBean(Wedstrijd.class).list().get(0));
    }

    public void insert(Wedstrijd wedstrijd) {
        jdbi.useHandle(handle -> handle.createUpdate("INSERT INTO Wedstrijd (naam, datum, plaats, inschrijvingsgeld, categorieID) VALUES (:naam, :datum, :plaats, :inschrijvingsgeld, :categorieID)").bindBean(wedstrijd).execute());
    }

    public void update(Wedstrijd wedstrijdNew, Wedstrijd wedstrijdOud) {
        jdbi.useHandle(handle -> handle.createUpdate("UPDATE Wedstrijd SET (naam, datum, plaats, inschrijvingsgeld, categorieID) = (:naam, :datum, :plaats, :inschrijvingsgeld, :categorieID) WHERE naam = :naamOud AND plaats = :plaatsOud").bindBean(wedstrijdNew).bind("naamOud", wedstrijdOud.getNaam()).bind("plaatsOud", wedstrijdOud.getPlaats()).execute());
    }

    public void delete(Wedstrijd wedstrijd) {
        jdbi.useHandle(handle -> handle.createUpdate("DELETE FROM Wedstrijd WHERE naam = :naam AND plaats = :plaats").bind("naam", wedstrijd.getNaam()).bind("plaats", wedstrijd.getPlaats()).execute());
    }

    public Wedstrijd selectByNaamDatum(String naam, String datum) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Wedstrijd WHERE naam = :naam AND datum = :datum").bind("naam", naam).bind("datum", datum).mapToBean(Wedstrijd.class).list().get(0));
    }

    public int getIdByNameAndDate(String name, String date) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT id FROM Wedstrijd WHERE naam = :naam AND datum = :datum").bind("naam", name).bind("datum", date).mapTo(Integer.class).findFirst().orElse(null));
    }

    public int getId(Wedstrijd w) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT id FROM Wedstrijd WHERE naam = :naam AND datum = :datum").bind("naam", w.getNaam()).bind("datum", w.getDatum()).mapTo(Integer.class).list().get(0));
    }
}