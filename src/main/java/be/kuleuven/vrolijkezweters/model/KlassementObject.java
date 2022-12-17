package be.kuleuven.vrolijkezweters.model;

public class KlassementObject {
    String voornaam;
    String naam;
    int loopTijd;

    public KlassementObject(){

    }

    @Override
    public String toString() {
        return "KlassementObject{" +
                "voornaam=" + voornaam +
                ", naam=" + naam +
                ", loopTijd=" + loopTijd +
                '}';
    }

    public KlassementObject(String voornaam, String naam, int loopTijd) {
        this.voornaam = voornaam;
        this.naam = naam;
        this.loopTijd = loopTijd;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public int getLoopTijd() {
        return loopTijd;
    }

    public void setLoopTijd(int loopTijd) {
        this.loopTijd = loopTijd;
    }
}
