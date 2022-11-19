package be.kuleuven.vrolijkezweters;

public class Wedstrijd {
    private String naam;
    private String datum;
    private String plaats;
    private String inschrijvingsgeld;
    private String categorie;

    public Wedstrijd() {

    }

    @Override
    public String toString() {
        return "Wedstrijd{" +
                "naam='" + naam + '\'' +
                ", datum='" + datum + '\'' +
                ", plaats=" + plaats +
                ", inschrijvingsgeld=" + inschrijvingsgeld +
                ", categorie=" + categorie +
                '}';
    }

    public Wedstrijd(String naam, String datum, String plaats, String inschrijvingsgeld, String categorie) {
        this.naam = naam;
        this.datum = datum;
        this.plaats = plaats;
        this.inschrijvingsgeld = inschrijvingsgeld;
        this.categorie = categorie;
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

    public String getCategorie() {
        return categorie;
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

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
}
