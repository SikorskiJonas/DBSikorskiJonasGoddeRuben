package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.Etappe;
import be.kuleuven.vrolijkezweters.model.LoopNummer;
import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;

import java.util.ArrayList;
import java.util.List;

public class WedstrijdJdbi {
    private ConnectionManager connectionManager;

    public WedstrijdJdbi(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }
    
    public List<Wedstrijd> getAll() {
        return connectionManager.handle.createQuery("SELECT * FROM Wedstrijd")
                .mapToBean(Wedstrijd.class)
                .list();
    }

    public void insert(Wedstrijd wedstrijd) {
        ConnectionManager.handle.createUpdate("INSERT INTO Wedstrijd (naam, datum, plaats, inschrijvingsgeld, categorieID) VALUES (:naam, :datum, :plaats, :inschrijvingsgeld, :categorieID)")
                .bindBean(wedstrijd)
                .execute();
    }

    public void update(Wedstrijd wedstrijdNew, String naam, String plaats) {
        String updateQuery = "UPDATE Wedstrijd SET " +
                " naam ='" + wedstrijdNew.getNaam() +
                "' , datum='" + wedstrijdNew.getDatum() +
                "' , plaats='" + wedstrijdNew.getPlaats() +
                "' , inschrijvingsgeld='" + wedstrijdNew.getInschrijvingsgeld() +
                "' , categorieID='" + wedstrijdNew.getCategorieID() +
                "' WHERE naam= '" + naam + "' AND plaats= '" + plaats + "'";
        ConnectionManager.handle.execute(updateQuery);
    }

    public void delete(Wedstrijd wedstrijd) {
        String q = "DELETE FROM Wedstrijd WHERE datum = '" + wedstrijd.getDatum() +"' AND naam = '"+ wedstrijd.getNaam() +"'";
        ConnectionManager.handle.execute(q);
    }

    public Wedstrijd selectByNaamDatum(String naam, String datum) {
        return connectionManager.handle.createQuery("Select * FROM Wedstrijd WHERE naam = '" + naam + "' AND datum = '" + datum +"'")
                .mapToBean(Wedstrijd.class)
                .list().get(0);
    }

    public int getTotaleAfstand(Wedstrijd wedstrijd) {
        String wedstrijdId = connectionManager.handle.createQuery("Select id FROM Wedstrijd WHERE naam = '" + wedstrijd.getNaam() + "' AND datum = '" + wedstrijd.getDatum() +"'")
                .mapTo(String.class)
                .list().get(0);
        List<Etappe> etappeList = connectionManager.handle.createQuery("Select * FROM Etappe WHERE wedstrijdID = '" + String.valueOf(wedstrijdId) +"'")
                .mapToBean(Etappe.class)
                .list();
        int totaleAfstand = 0;
        for (Etappe etappe : etappeList){
            totaleAfstand = totaleAfstand + etappe.getAfstandMeter();
        }
        return totaleAfstand;
    }

    public List<Etappe> getEtappes(Wedstrijd wedstrijd) {
        String wedstrijdId = connectionManager.handle.createQuery("Select id FROM Wedstrijd WHERE naam = '" + wedstrijd.getNaam() + "' AND datum = '" + wedstrijd.getDatum() +"'")
                .mapTo(String.class)
                .list().get(0);
        return connectionManager.handle.createQuery("Select naam FROM Etappe WHERE wedstrijdID = '" + wedstrijdId +"'")
                .mapToBean(Etappe.class)
                .list();
    }

    public void schrijfIn(Loper l, Wedstrijd w) {
        List<Integer> bestaandeNummers = connectionManager.handle.createQuery("Select nummer FROM Loopnummer ORDER BY nummer DESC")
                .mapTo(Integer.class)
                .list();
        int nieuwNummer = bestaandeNummers.get(0) + 1;
        int loperID = connectionManager.handle.createQuery("Select id FROM Loper WHERE naam = '" + l.getNaam() + "' AND geboortedatum = '" + l.getGeboorteDatum() + "'")
                .mapTo(Integer.class)
                .list().get(0);
        for(Etappe etappe : getEtappes(w)){
            int etappeID = connectionManager.handle.createQuery("Select id FROM Etappe WHERE naam = '" + etappe.getNaam() + "'")
                    .mapTo(Integer.class)
                    .list().get(0);
            LoopNummer loopNummer = new LoopNummer(nieuwNummer,0, loperID, etappeID);
            ConnectionManager.handle.createUpdate("INSERT INTO LoopNummer (nummer, looptijd, loperId, etappeId) VALUES (" +
                            "'" + loopNummer.getNummer() +
                            "', '" + loopNummer.getLoopTijd() +
                            "', '" + loperID +
                            "', '" + etappeID + "')")
                    .execute();
        }
    }

    public List<Wedstrijd> getInschreven(Object user) {
        String query = "SELECT Wedstrijd.* FROM Wedstrijd " +
                "INNER JOIN Etappe ON Etappe.wedstrijdId = Wedstrijd.id " +
                "INNER JOIN LoopNummer ON LoopNummer.etappeId = Etappe.id " +
                "INNER JOIN Loper ON Loper.id = LoopNummer.loperId " +
                "WHERE Loper.eMail = '" + ((Loper)user).getEmail() +"'";
        List<Wedstrijd> wedstrijdList = connectionManager.handle.createQuery(query)
                .mapToBean(Wedstrijd.class)
                .list();
        return wedstrijdList;
    }
}