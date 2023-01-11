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

    public void schrijfIn(Loper l, List<Etappe> e) {

    }
}