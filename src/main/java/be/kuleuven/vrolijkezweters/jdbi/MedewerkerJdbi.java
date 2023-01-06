package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.Medewerker;

import java.util.List;

public class MedewerkerJdbi {
    private ConnectionManager connectionManager;

    public MedewerkerJdbi(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }

    public List<Medewerker> getAll() {
        return connectionManager.handle.createQuery("SELECT * FROM Medewerker")
                .mapToBean(Medewerker.class)
                .list();
    }

    public void insert(Medewerker medewerker) {
        //connectionManager.handle.createUpdate("INSERT INTO Medewerker (geboortedatum, voornaam, naam, sex, datumTewerkstelling, functieId, telefoonnummer, eMail, gemeente, straatEnNr, wachtwoord, isAdmin) VALUES (:geboortedatum, :voornam, :naam, :sex, :datumTewerkstelling, :functieId, :telefoonnummmer, :eMail, :gemeente, :straatEnNr, :wachtwoord, :isAdmin)")
          //      .bindBean(medewerker)
            //    .execute();
        ConnectionManager.handle.execute("INSERT INTO Medewerker (geboortedatum, voornaam, naam, sex, datumTewerkstelling, functieId, telefoonnummer, eMail, gemeente, straatEnNr, wachtwoord, isAdmin) VALUES ('" +
                medewerker.getGeboorteDatum() +"' , '" +
                medewerker.getVoornaam() +"' , '" +
                medewerker.getNaam() +"' , '" +
                medewerker.getSex() +"' , '" +
                medewerker.getDatumTewerkstelling() +"' , '" +
                medewerker.getFunctieId() +"' , '" +
                medewerker.getTelefoonNummer() +"' , '" +
                medewerker.getEmail() +"' , '" +
                medewerker.getGemeente() +"' , '" +
                medewerker.getStraatEnNr() +"' , '" +
                medewerker.getWachtwoord() +"' , '" +
                medewerker.getIsAdmin() +" ') " );
    }

    public void update(Medewerker medewerkerNew, String geboortedatum, String naam, String voornaam) {
        String updateQuery = "UPDATE Medewerker SET " +
                " geboorteDatum ='" + medewerkerNew.getGeboorteDatum() +
                "' , voornaam='" + medewerkerNew.getVoornaam() +
                "' , naam='" + medewerkerNew.getNaam() +
                "' , sex='" + medewerkerNew.getSex() +
                "' , datumTewerkstelling='" + medewerkerNew.getDatumTewerkstelling() +
                "' , functieId='" + medewerkerNew.getFunctieId() +
                "' , telefoonnummer='" + medewerkerNew.getTelefoonNummer() +
                "' , eMail='" + medewerkerNew.getEmail() +
                "' , gemeente='" + medewerkerNew.getGemeente() +
                "' , straatEnNr='" +medewerkerNew.getStraatEnNr() +
                "' WHERE geboorteDatum= '" + geboortedatum + "' AND naam= '"+ naam + "' AND voornaam= '"+ voornaam +"'";
        ConnectionManager.handle.execute(updateQuery);
    }

    public void delete(Medewerker medewerker) {
        String deleteMedewerker = "DELETE FROM Medewerker WHERE geboortedatum = '" + medewerker.getGeboorteDatum() + "' AND voornaam = '" + medewerker.getVoornaam() + "' AND naam = '" + medewerker.getNaam() + "'";
        ConnectionManager.handle.execute(deleteMedewerker);
    }

    public Medewerker selectByVoornaamNaamGeboortedatum(String voornaam, String naam, String geboortedatum) {
        return connectionManager.handle.createQuery("Select * FROM Medewerker WHERE voornaam = '" + voornaam + "' AND naam = '" + naam + "' AND geboortedatum ='"+ geboortedatum +"'")
                .mapToBean(Medewerker.class)
                .list().get(0);
    }
}