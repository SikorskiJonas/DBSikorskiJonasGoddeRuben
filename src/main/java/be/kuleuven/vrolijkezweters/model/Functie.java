package be.kuleuven.vrolijkezweters.model;

public class Functie {
    String functie;

    public Functie(){

    }

    @Override
    public String toString() {
        return "Functie{" +
                "functie'" + functie +
                '}';
    }

    public Functie(String functie) {
        this.functie = functie;
    }

    public String getFunctie() {
        return functie;
    }

    public void setFunctie(String functie) {
        this.functie = functie;
    }
}
