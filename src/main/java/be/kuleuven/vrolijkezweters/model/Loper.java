package be.kuleuven.vrolijkezweters.model;

public class Loper {
    private String geboortedatum;
    private String voornaam;
    private String naam;
    private String sex;
    private String lengte;
    private String telefoonNummer;
    private String email;
    private String gemeente;
    private String StraatEnNr;
    private String wachtwoord;

    public Loper() {

    }

    public Loper(String geboortedatum, String voornaam, String naam, String sex, String lengte, String telefoonNummer, String email, String gemeente, String StraatEnNr, String wachtwoord) {
        this.geboortedatum = geboortedatum;
        this.voornaam = voornaam;
        this.naam = naam;
        this.sex = sex;
        this.lengte = lengte;
        this.telefoonNummer = telefoonNummer;
        this.email = email;
        this.gemeente = gemeente;
        this.StraatEnNr = StraatEnNr;
        this.wachtwoord = wachtwoord;
    }

    @Override
    public String toString() {
        return "Loper{" + "geboortedatum='" + geboortedatum + "', voornaam='" + voornaam + "', naam='" + naam + "', sex='" + sex + "', lengte='" + lengte + "', telefoonnummer='" + telefoonNummer + "', eMail='" + email + "', gemeente='" + gemeente + "', straatEnNr='" + StraatEnNr + "', wachtwoord='" + wachtwoord + "'}";
    }

    public String getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum (String geboortedatum) {this.geboortedatum = geboortedatum;}

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

    public String getTelefoonnummer() {
        return telefoonNummer;
    }

    public void setTelefoonnummer(String telefoonNummer) {
        this.telefoonNummer = telefoonNummer;
    }

    public String geteMail() {return email;}

    public void seteMail(String email) {
        this.email = email;
    }

    public String getGemeente() {
        return gemeente;
    }

    public void setGemeente(String gemeente) {
        this.gemeente = gemeente;
    }

    public String getStraatEnNr() {
        return StraatEnNr;
    }

    public void setStraatEnNr(String StraatEnNr) {
        this.StraatEnNr = StraatEnNr;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }
}
