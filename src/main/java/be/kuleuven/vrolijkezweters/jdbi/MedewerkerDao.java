package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.Functie;
import be.kuleuven.vrolijkezweters.model.Medewerker;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class MedewerkerDao {

    private final Jdbi jdbi;

    public MedewerkerDao() {
        this.jdbi = JdbiManager.getJdbi();
    }

    public List<Medewerker> getAll() {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM Medewerker")
                .mapToBean(Medewerker.class)
                .list());
    }

    public void insert(Medewerker medewerker) {
        jdbi.useHandle(handle -> handle.createUpdate("INSERT INTO Medewerker (geboortedatum, voornaam, naam, sex, datumTewerkstelling, functieId, telefoonnummer, eMail, gemeente, straatEnNr, wachtwoord, isAdmin) VALUES (:geboorteDatum, :voornaam, :naam, :sex, :datumTewerkstelling, :functieId, :telefoonNummer, :eMail, :gemeente, :StraatEnNr, :wachtwoord, :isAdmin)")
                .bind("geboorteDatum", medewerker.getGeboorteDatum())
                .bind("voornaam", medewerker.getVoornaam())
                .bind("naam", medewerker.getNaam())
                .bind("sex", medewerker.getSex())
                .bind("datumTewerkstelling", medewerker.getDatumTewerkstelling())
                .bind("functieId", medewerker.getFunctieId())
                .bind("telefoonNummer", medewerker.getTelefoonNummer())
                .bind("eMail", medewerker.getEmail())
                .bind("gemeente", medewerker.getGemeente())
                .bind("straatEnNr", medewerker.getStraatEnNr())
                .bind("wachtwoord", medewerker.getWachtwoord())
                .bind("isadmin", medewerker.getIsAdmin())
                .execute());
    }

    public void update(Medewerker medewerkerNew, String geboortedatum, String naam, String voornaam) {
        String updateQuery = "UPDATE Medewerker SET " + " geboorteDatum ='" + medewerkerNew.getGeboorteDatum() + "' , voornaam='" + medewerkerNew.getVoornaam() + "' , naam='" + medewerkerNew.getNaam() + "' , sex='" + medewerkerNew.getSex() + "' , datumTewerkstelling='" + medewerkerNew.getDatumTewerkstelling() + "' , functieId='" + medewerkerNew.getFunctieId() + "' , telefoonnummer='" + medewerkerNew.getTelefoonNummer() + "' , eMail='" + medewerkerNew.getEmail() + "' , gemeente='" + medewerkerNew.getGemeente() + "' , straatEnNr='" + medewerkerNew.getStraatEnNr() + "' WHERE geboorteDatum= '" + geboortedatum + "' AND naam= '" + naam + "' AND voornaam= '" + voornaam + "'";
        ConnectionManager.handle.execute(updateQuery);
    }

    public void delete(Medewerker medewerker) {
        String deleteMedewerker = "DELETE FROM Medewerker WHERE geboortedatum = '" + medewerker.getGeboorteDatum() + "' AND voornaam = '" + medewerker.getVoornaam() + "' AND naam = '" + medewerker.getNaam() + "'";
        ConnectionManager.handle.execute(deleteMedewerker);
    }

    public Medewerker selectByVoornaamNaamGeboortedatum(String voornaam, String naam, String geboortedatum) {
        return ConnectionManager.handle.createQuery("Select * FROM Medewerker WHERE voornaam = '" + voornaam + "' AND naam = '" + naam + "' AND geboortedatum ='" + geboortedatum + "'").mapToBean(Medewerker.class).list().get(0);
    }
}