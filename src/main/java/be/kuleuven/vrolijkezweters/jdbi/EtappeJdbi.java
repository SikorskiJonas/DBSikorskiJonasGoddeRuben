package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.Etappe;
import be.kuleuven.vrolijkezweters.model.LoopNummer;
import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;

import java.util.ArrayList;
import java.util.List;

public class EtappeJdbi {
    private ConnectionManager connectionManager;

    public EtappeJdbi(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }

    public List<Etappe> getAll() {
        return connectionManager.handle.createQuery("SELECT * FROM Etappe")
                .mapToBean(Etappe.class)
                .list();
    }

    public void insert(Etappe etappe) {
        ConnectionManager.handle.createUpdate("INSERT INTO Etappe (afstandMeter, startPlaats, eindPlaats, wedstrijdId, naam) VALUES (:afstandMeter, :startPlaats, :eindPlaats, :wedstrijdId, :naam)")
                .bindBean(etappe)
                .execute();
    }

    public void delete(Etappe etappe) {
        String q = "DELETE FROM Etappe WHERE wedstrijdId = '" + etappe.getWedstrijdId() +"' AND naam = '"+ etappe.getNaam() +"'";
        ConnectionManager.handle.execute(q);
    }

    public Etappe selectByNaamWedstrijdId(String naam, String wedstrijdId) {
        return connectionManager.handle.createQuery("Select * FROM Etappe WHERE naam = '" + naam + "' AND datum = '" + wedstrijdId +"'")
                .mapToBean(Etappe.class)
                .list().get(0);
    }

    public int getId(Etappe e){
        return connectionManager.handle.createQuery("Select id FROM Etappe WHERE naam = '" + e.getNaam() + "' AND Startplaats = '" + e.getStartPlaats() +"'")
                .mapTo(Integer.class)
                .list().get(0);
    }
}