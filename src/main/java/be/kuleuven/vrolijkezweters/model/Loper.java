package be.kuleuven.vrolijkezweters.model;

import java.sql.Date;

public class Loper {
    private String geboorteDatum;
    private String voornaam;
    private String naam;
    private String sex;
    private String lengte;
    private String telefoonNummer;
    private String email;
    private String gemeente;
    private String straatplusnr;

    public Loper(){

    }

    @Override
    public String toString() {
        return "Medewerker{" +
                "geboorteDatum='" + geboorteDatum + '\'' +
                ", voornaam='" + voornaam + '\'' +
                ", naam='" + naam + '\'' +
                ", sex='" + sex + '\'' +
                ", lengte='" + lengte + '\'' +
                ", telefoonNummer='" + telefoonNummer + '\'' +
                ", email='" + email + '\'' +
                ", gemeente='" + gemeente + '\'' +
                ", straatplusnr='" + straatplusnr +
                '}';
    }

    public Loper(String geboorteDatum, String voornaam, String naam, String sex, String lengte, String telefoonNummer, String email, String gemeente, String straatplusnr) {
        this.geboorteDatum = geboorteDatum;
        this.voornaam = voornaam;
        this.naam = naam;
        this.sex = sex;
        this.lengte = lengte;
        this.telefoonNummer = telefoonNummer;
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

    public String getLengte() {
        return lengte;
    }

    public void setLengte(String lengte) {
        this.lengte = lengte;
    }

    public String getTelefoonNummer() {
        return telefoonNummer;
    }

    public void setTelefoonNummer(String telefoonNummer) {
        this.telefoonNummer = telefoonNummer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
