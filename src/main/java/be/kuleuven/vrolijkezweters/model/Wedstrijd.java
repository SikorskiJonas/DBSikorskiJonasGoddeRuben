package be.kuleuven.vrolijkezweters.model;

public class Wedstrijd {
    private String naam;
    private String datum;
    private String plaats;
    private String inschrijvingsgeld;
    private String categorieID;

    public Wedstrijd() {

    }

    public Wedstrijd(String naam, String datum, String plaats, String inschrijvingsgeld, String categorieID) {
        this.naam = naam;
        this.datum = datum;
        this.plaats = plaats;
        this.inschrijvingsgeld = inschrijvingsgeld;
        this.categorieID = categorieID;
    }

    @Override
    public String toString() {
        return "Wedstrijd{" + "naam=" + naam + ", datum=" + datum + ", plaats=" + plaats + ", inschrijvingsgeld=" + inschrijvingsgeld + ", categorieID=" + categorieID + '}';
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getPlaats() {
        return plaats;
    }

    public void setPlaats(String plaats) {
        this.plaats = plaats;
    }

    public String getInschrijvingsgeld() {
        return inschrijvingsgeld;
    }

    public void setInschrijvingsgeld(String inschrijvingsgeld) {
        this.inschrijvingsgeld = inschrijvingsgeld;
    }

    public String getCategorieID() {
        return categorieID;
    }

    public void setCategorieID(String categorieID) {
        this.categorieID = categorieID;
    }
}
