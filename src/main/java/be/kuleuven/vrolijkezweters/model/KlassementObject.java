package be.kuleuven.vrolijkezweters.model;

public class KlassementObject {
    String Voornaam;
    String Naam;
    int Looptijd;

    public KlassementObject(){

    }

    @Override
    public String toString() {
        return "KlassementObject{" +
                "voornaam=" + Voornaam +
                ", naam=" + Naam +
                ", loopTijd=" + Looptijd +
                '}';
    }

    public KlassementObject(String voornaam, String naam, int loopTijd) {
        this.Voornaam = voornaam;
        this.Naam = naam;
        this.Looptijd = loopTijd;
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
}
