package be.kuleuven.vrolijkezweters.model;

import java.sql.Date;

public class Medewerker {
    private String geboorteDatum;
    private String voornaam;
    private String naam;
    private String sex;
    private String datumTewerkstelling;
    private String functieId;
    private String telefoonNummer;
    private String eMail;
    private String gemeente;
    private String straatEnNr;
    private String wachtwoord;
    private String isAdmin;

    public Medewerker(){

    }

    @Override
    public String toString() {
        return "Medewerker{" +
                "geboorteDatum='" + geboorteDatum + '\'' +
                ", voornaam='" + voornaam + '\'' +
                ", naam='" + naam + '\'' +
                ", sex='" + sex + '\'' +
                ", datumTewerkstelling=" + datumTewerkstelling + '\'' +
                ", functieId='" + functieId + '\'' +
                ", telefoonNummer='" + telefoonNummer + '\'' +
                ", eMail='" + eMail + '\'' +
                ", gemeente='" + gemeente + '\'' +
                ", straatEnNr='" + straatEnNr + '\'' +
                ", wachtwoord='" + wachtwoord + '\'' +
                ", isAdmin='" + isAdmin +
                '}';
    }

    public Medewerker(String geboorteDatum, String voornaam, String naam, String sex, String datumTewerkstelling, String functieId, String telefoonNummer, String eMail, String gemeente, String straatEnNr, String wachtwoord, String isAdmin) {
        this.geboorteDatum = geboorteDatum;
        this.voornaam = voornaam;
        this.naam = naam;
        this.sex = sex;
        this.datumTewerkstelling = datumTewerkstelling;
        this.functieId = functieId;
        this.telefoonNummer = telefoonNummer;
        this.eMail = eMail;
        this.gemeente = gemeente;
        this.straatEnNr = straatEnNr;
        this.wachtwoord = wachtwoord;
        this.isAdmin = isAdmin;

    }

    public String getGeboorteDatum() {
        return geboorteDatum;
    }

    public void setGeboorteDatum(String geboorteDatum) {
        this.geboorteDatum = geboorteDatum;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDatumTewerkstelling() {
        return datumTewerkstelling;
    }

    public void setDatumTewerkstelling(String datumTewerkstelling) {
        this.datumTewerkstelling = datumTewerkstelling;
    }

    public String getFunctieId() {
        return functieId;
    }

    public void setFunctieId(String functieId) {
        this.functieId = functieId;
    }

    public String getGemeente() {
        return gemeente;
    }

    public void setGemeente(String gemeente) {
        this.gemeente = gemeente;
    }

    public String getStraatEnNr() {
        return straatEnNr;
    }

    public void setStraatEnNr(String straatEnNr) {
        this.straatEnNr = straatEnNr;
    }

    public String getEmail() {
        return eMail;
    }

    public void setEmail(String eMail) {
        this.eMail = eMail;
    }

    public String getTelefoonNummer() {
        return telefoonNummer;
    }

    public void setTelefoonNummer(String telefoonNummer) {
        this.telefoonNummer = telefoonNummer;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {this.wachtwoord = wachtwoord;}

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {this.isAdmin = isAdmin;}

}
