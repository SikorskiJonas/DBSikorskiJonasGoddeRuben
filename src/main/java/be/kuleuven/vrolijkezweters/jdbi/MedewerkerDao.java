package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.Medewerker;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class MedewerkerDao {

    private final Jdbi jdbi;

    public MedewerkerDao() {
        this.jdbi = JdbiManager.getJdbi();
    }

    public List<Medewerker> getAll() {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Medewerker").mapToBean(Medewerker.class).list());
    }

    public void insert(Medewerker medewerker) {
        jdbi.useHandle(handle -> handle.createUpdate("INSERT INTO Medewerker (geboortedatum, voornaam, naam, sex, datumTewerkstelling, functieId, telefoonnummer, eMail, gemeente, straatEnNr, wachtwoord) VALUES (:geboortedatum, :voornaam, :naam, :sex, :datumTewerkstelling, :functieId, :telefoonnummer, :eMail, :gemeente, :straatEnNr, :wachtwoord)").bindBean(medewerker).execute());
    }

    public void update(Medewerker medewerkerNew, Medewerker medewerkerOud) {
        jdbi.useHandle(handle -> handle.createUpdate("UPDATE Medewerker SET(geboortedatum, voornaam, naam, sex, datumTewerkstelling, functieId, telefoonnummer, eMail, gemeente, straatEnNr, wachtwoord) = (:geboortedatum, :voornaam, :naam, :sex, :datumTewerkstelling, :functieId, :telefoonnummer, :eMail, :gemeente, :straatEnNr, :wachtwoord) WHERE eMail = :eMailOud").bindBean(medewerkerNew).bind("eMailOud", medewerkerOud.geteMail()).execute());
    }

    public void delete(Medewerker medewerker) {
        jdbi.useHandle(handle -> handle.createUpdate("DELETE FROM Medewerker WHERE eMail = :eMail").bind("eMail", medewerker.geteMail()).execute());
    }

    public Medewerker selectByVoornaamNaamGeboortedatum(String voornaam, String naam, String geboortedatum) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Medewerker WHERE voornaam = :voornaam AND naam = :naam AND geboortedatum = :geboortedatum").bind("voornaam", voornaam).bind("geboortedatum", geboortedatum).bind("naam", naam).mapToBean(Medewerker.class).list().get(0));
    }


    public int getId(Medewerker m) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT id FROM Medewerker WHERE voornaam = :voornaam AND naam = :naam AND geboortedatum = :geboortedatum").bind("voornaam", m.getVoornaam()).bind("geboortedatum", m.getGeboortedatum()).bind("naam", m.getNaam()).mapTo(Integer.class).list().get(0));
    }

    public List<Medewerker> getMedewerkerLogin(String eMail, String wachtwoord) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Medewerker WHERE eMail = :eMail AND wachtwoord = :password").bind("eMail", eMail).bind("password", wachtwoord).mapToBean(Medewerker.class).list());
    }
}
