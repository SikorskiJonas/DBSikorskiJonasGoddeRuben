package be.kuleuven.vrolijkezweters.model;

public class Etappe {
    private int afstandMeter;
    private String startPlaats;
    private String eindPlaats;
    private int wedstrijdId;
    private String naam;

    public Etappe() {

    }

    public Etappe(int afstandMeter, String startPlaats, String eindPlaats, int wedstrijdId, String naam) {
        this.afstandMeter = afstandMeter;
        this.startPlaats = startPlaats;
        this.eindPlaats = eindPlaats;
        this.wedstrijdId = wedstrijdId;
        this.naam = naam;

    }

    @Override
    public String toString() {
        return "Etappe{" + "afstandMeter='" + afstandMeter + '\'' + ", startPlaats='" + startPlaats + '\'' + ", eindPlaats='" + eindPlaats + '\'' + ", wedstrijdId=" + wedstrijdId + ", naam='" + naam + '\'' + '}';
    }

    public int getAfstandMeter() {
        return afstandMeter;
    }

    public void setAfstandMeter(int afstandMeter) {
        this.afstandMeter = afstandMeter;
    }

    public String getStartPlaats() {
        return startPlaats;
    }

    public void setStartPlaats(String startPlaats) {
        this.startPlaats = startPlaats;
    }

    public String getEindPlaats() {
        return eindPlaats;
    }

    public void setEindPlaats(String eindPlaats) {
        this.eindPlaats = eindPlaats;
    }

    public int getWedstrijdId() {
        return wedstrijdId;
    }

    public void setWedstrijdId(int wedstrijdId) {
        this.wedstrijdId = wedstrijdId;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }
}
