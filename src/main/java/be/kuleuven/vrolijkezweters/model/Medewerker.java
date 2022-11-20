package be.kuleuven.vrolijkezweters.model;

import java.sql.Date;

public class Medewerker {
    private String geboorteDatum;
    private String voornaam;
    private String naam;
    private String sex;
    private Date datumTewerkstelling;
    private int functieId;
    private String gsmNummer;
    private String email;
    private String gemeente;
    private String straatplusnr;

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
                ", gsmNummer='" + gsmNummer + '\'' +
                ", email='" + email + '\'' +
                ", gemeente='" + gemeente + '\'' +
                ", straatplusnr='" + straatplusnr +
                '}';
    }

    public Medewerker(String geboorteDatum, String voornaam, String naam, String sex, Date datumTewerkstelling, int functieId, String gsmNummer, String email, String gemeente, String straatplusnr) {
        this.geboorteDatum = geboorteDatum;
        this.voornaam = voornaam;
        this.naam = naam;
        this.sex = sex;
        this.datumTewerkstelling = datumTewerkstelling;
        this.functieId = functieId;
        this.gsmNummer = gsmNummer;
        this.email = email;
        this.gemeente = gemeente;
        this.straatplusnr = straatplusnr;

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

    public Date getDatumTewerkstelling() {
        return datumTewerkstelling;
    }

    public void setDatumTewerkstelling(Date datumTewerkstelling) {
        this.datumTewerkstelling = datumTewerkstelling;
    }

    public int getFunctieId() {
        return functieId;
    }

    public void setFunctieId(int functieId) {
        this.functieId = functieId;
    }

    public String getGemeente() {
        return gemeente;
    }

    public void setGemeente(String gemeente) {
        this.gemeente = gemeente;
    }

    public String getStraatplusnr() {
        return straatplusnr;
    }

    public void setStraatplusnr(String straatplusnr) {
        this.straatplusnr = straatplusnr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGsmNummer() {
        return gsmNummer;
    }

    public void setGsmNummer(String gsmNummer) {
        this.gsmNummer = gsmNummer;
    }



}
