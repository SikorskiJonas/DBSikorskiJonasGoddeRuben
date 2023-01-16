package be.kuleuven.vrolijkezweters.model;

public class Functie {
    String functie;

    public Functie() {

    }

    public Functie(String functie) {
        this.functie = functie;
    }

    @Override
    public String toString() {
        return "Functie{" + "functie'" + functie + '}';
    }

    public String getFunctie() {
        return functie;
    }

    public void setFunctie(String functie) {
        this.functie = functie;
    }
}
