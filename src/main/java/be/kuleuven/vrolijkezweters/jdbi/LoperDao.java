package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Medewerker;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class LoperDao {

    private final Jdbi jdbi;
    public LoperDao() {this.jdbi = JdbiManager.getJdbi();}

    public List<Loper> getAll() {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Loper")
                .mapToBean(Loper.class)
                .list());
    }

    public void insert(Loper loper) {
        jdbi.useHandle(handle -> handle.createUpdate("INSERT INTO Loper (geboortedatum, voornaam, naam, sex, lengte, telefoonnummer, eMail, gemeente, straatEnNr, wachtwoord) VALUES (:geboortedatum, :voornaam, :naam, :sex, :lengte, :telefoonnummer, :eMail, :gemeente, :straatEnNr, :wachtwoord)")
                .bindBean(loper)
                .execute());
    }
    public void update(Loper loperNew, Loper loperOud) {
        jdbi.useHandle(handle -> handle.createUpdate("UPDATE Loper SET (geboortedatum, voornaam, naam, sex, lengte, telefoonnummer, eMail, gemeente, straatEnNr, wachtwoord) = (:geboortedatum, :voornaam, :naam, :sex, :lengte, :telefoonnummer, :eMail, :gemeente, :straatEnNr, :wachtwoord) WHERE eMail = :eMailOud")
                .bindBean(loperNew)
                .bind("eMailOud", loperOud.geteMail())
                .execute());
    }

    public void delete(Loper loper) {
        jdbi.useHandle(handle -> handle.createUpdate("DELETE FROM Loper WHERE eMail = :eMail")
                .bind("eMail", loper.geteMail())
                .execute());
    }

    public Loper selectByVoornaamNaamGeboortedatum(String voornaam, String naam, String geboortedatum) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Loper WHERE voornaam = :voornaam AND naam = :naam AND geboortedatum = :geboortedatum")
                .bind("voornaam", voornaam)
                .bind("geboortedatum", geboortedatum)
                .bind("naam", naam)
                .mapToBean(Loper.class)
                .list().get(0));
    }

    public int getId(Loper l){
        return jdbi.withHandle(handle -> handle.createQuery("SELECT id FROM Loper WHERE voornaam = :voornaam AND naam = :naam AND geboortedatum = :geboortedatum")
                .bind("voornaam", l.getVoornaam())
                .bind("geboortedatum", l.getGeboortedatum())
                .bind("naam", l.getNaam())
                .mapToBean(Integer.class)
                .list().get(0));
    }

    public List<Loper> getLoperLogin(String eMail, String wachtwoord){
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Loper WHERE eMail = :eMail AND wachtwoord = :password")
                .bind("eMail", eMail)
                .bind("password", wachtwoord)
                .mapToBean(Loper.class)
                .list());
    }
}
