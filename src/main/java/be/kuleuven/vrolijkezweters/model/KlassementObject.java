package be.kuleuven.vrolijkezweters.model;

public class KlassementObject {
    String Voornaam;
    String Naam;
    int Looptijd;
    double Snelheid;

    public KlassementObject() {

    }


    public KlassementObject(String voornaam, String naam, int loopTijd, double snelheid) {
        this.Voornaam = voornaam;
        this.Naam = naam;
        this.Looptijd = loopTijd;
        this.Snelheid = snelheid;
    }

    @Override
    public String toString() {
        return "KlassementObject{" + "voornaam=" + Voornaam + ", naam=" + Naam + ", loopTijd=" + Looptijd + ", snelheid=" + Snelheid + '}';
    }

    public String getVoornaam() {
        return Voornaam;
    }

    public void setVoornaam(String voornaam) {
        Voornaam = voornaam;
    }

    public String getNaam() {
        return Naam;
    }

    public void setNaam(String naam) {
        Naam = naam;
    }

    public int getLooptijd() {
        return Looptijd;
    }

    public void setLooptijd(int looptijd) {
        Looptijd = looptijd;
    }

    public double getSnelheid() {
        return Snelheid;
    }

    public void setSnelheid(double snelheid) {
        Snelheid = snelheid;
    }
}
