package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.Loper;

import java.util.List;

public class LoperJdbi {
    private ConnectionManager connectionManager;

    public LoperJdbi(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }

    public List<Loper> getAll() {
        return connectionManager.handle.createQuery("SELECT * FROM Loper")
                .mapToBean(Loper.class)
                .list();
    }

    public void insert(Loper loper) {
        //connectionManager.handle.createUpdate("INSERT INTO Loper (geboortedatum, voornaam, naam, sex, lengte, telefoonnummer, eMail, gemeente, straatEnNr, wachtwoord) VALUES (:geboortedatum, :voornam, :naam, :sex, :lengte, :telefoonnummmer, :eMail, :gemeente, :straatEnNr, :wachtwoord)")
          //      .bindBean(loper)
            //    .execute();
        ConnectionManager.handle.execute("INSERT INTO Loper (geboortedatum, voornaam, naam, sex, lengte, telefoonnummer, eMail, gemeente, straatEnNr, wachtwoord) VALUES ('" +
                loper.getGeboorteDatum() +"' , '" +
                loper.getVoornaam() +"' , '" +
                loper.getNaam() +"' , '" +
                loper.getSex() +"' , '" +
                loper.getLengte() +"' , '" +
                loper.getTelefoonNummer() +"' , '" +
                loper.getEmail() +"' , '" +
                loper.getGemeente() +"' , '" +
                loper.getStraatEnNr() +"' , '" +
                loper.getWachtwoord() +" ') " );
    }

    public void update(Loper loperNew, String geboortedatum, String naam, String voornaam) {
        String updateQuery = "UPDATE Loper SET " +
                " geboorteDatum ='" + loperNew.getGeboorteDatum() +
                "' , voornaam='" + loperNew.getVoornaam() +
                "' , naam='" + loperNew.getNaam() +
                "' , sex='" + loperNew.getSex() +
                "' , lengte='" + loperNew.getLengte() +
                "' , telefoonnummer='" + loperNew.getTelefoonNummer() +
                "' , eMail='" + loperNew.getEmail() +
                "' , gemeente='" + loperNew.getGemeente() +
                "' , straatEnNr='" +loperNew.getStraatEnNr() +
                "' WHERE geboorteDatum= '" + geboortedatum + "' AND naam= '"+ naam + "' AND voornaam= '"+ voornaam +"'";
        ConnectionManager.handle.execute(updateQuery);
    }

    public void delete(Loper loper) {
        String deleteLoper = "DELETE FROM Loper WHERE geboortedatum = '" + loper.getGeboorteDatum() + "' AND voornaam = '" + loper.getVoornaam() + "' AND naam = '" + loper.getNaam() + "'";
        ConnectionManager.handle.execute(deleteLoper);
    }

    public Loper selectByVoornaamNaamGeboortedatum(String voornaam, String naam, String geboortedatum) {
        return connectionManager.handle.createQuery("Select * FROM Loper WHERE voornaam = '" + voornaam + "' AND naam = '" + naam + "' AND geboortedatum ='"+ geboortedatum +"'")
                .mapToBean(Loper.class)
                .list().get(0);
    }
}
