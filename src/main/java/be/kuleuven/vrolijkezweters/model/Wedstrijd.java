package be.kuleuven.vrolijkezweters.model;

public class Wedstrijd {
    private String naam;
    private String datum;
    private String plaats;
    private String inschrijvingsgeld;
    private String categorieId;

    public Wedstrijd() {

    }

    @Override
    public String toString() {
        return "Wedstrijd{" +
                "naam='" + naam + '\'' +
                ", datum='" + datum + '\'' +
                ", plaats=" + plaats +
                ", inschrijvingsgeld=" + inschrijvingsgeld +
                ", categorieId=" + categorieId +
                '}';
    }

    public Wedstrijd(String naam, String datum, String plaats, String inschrijvingsgeld, String categorieId) {
        this.naam = naam;
        this.datum = datum;
        this.plaats = plaats;
        this.inschrijvingsgeld = inschrijvingsgeld;
        this.categorieId = categorieId;
    }

    public String getNaam() {
        return naam;
    }

    public String getDatum() {
        return datum;
    }

    public String getPlaats() {
        return plaats;
    }

    public String getInschrijvingsgeld() {
        return inschrijvingsgeld;
    }

    public String getCategorieId() {
        return categorieId;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public void setPlaats(String plaats) {
        this.plaats = plaats;
    }

    public void setInschrijvingsgeld(String inschrijvingsgeld) {
        this.inschrijvingsgeld = inschrijvingsgeld;
    }

    public void setCategorieId(String categorieId) {
        this.categorieId = categorieId;
    }
}
