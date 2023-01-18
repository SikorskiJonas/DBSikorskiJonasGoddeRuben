package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Medewerker;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class InputChecker {

    public ArrayList<String> checkInput(Object o) {
        ArrayList<String> fouten = new ArrayList<>();
        if (o.getClass() == Loper.class) {
            Loper l = (Loper) o;
            if (stringChecker(l.getNaam())) {
                fouten.add("Naam");
            }
            if (stringChecker(l.getVoornaam())) {
                fouten.add("Voornaam");
            }
            if (stringChecker(l.getLengte())) {
                fouten.add("Lengte");
            }
            if (stringChecker(l.getTelefoonnummer())) {
                fouten.add("Telefoonnummer");
            }
            if (stringChecker(l.getGemeente())) {
                fouten.add("Gemeente");
            }
            if (stringChecker(l.getStraatEnNr())) {
                fouten.add("Straat En Nr");
            }
            if (stringChecker(l.getWachtwoord())) {
                fouten.add("Wachtwoord");
            }
            if (emailChecker(l.geteMail())) {
                fouten.add("E-mail");
            }
            if (sexChecker(l.getSex())) {
                fouten.add("Sex");
            }
            if (dateChecker(l.getGeboortedatum())) {
                fouten.add("Geboortedatum");
            }
            if (telephoneChecker(l.getTelefoonnummer())) {
                fouten.add("Telefoonnummer");
            }
        }
        if (o.getClass() == Medewerker.class) {
            Medewerker m = (Medewerker) o;
            if (stringChecker(m.getNaam())) {
                fouten.add("Naam");
            }
            if (stringChecker(m.getVoornaam())) {
                fouten.add("Voornaam");
            }
            if (stringChecker(m.getTelefoonnummer())) {
                fouten.add("Telefoonnummer");
            }
            if (stringChecker(m.getGemeente())) {
                fouten.add("Gemeente");
            }
            if (stringChecker(m.getStraatEnNr())) {
                fouten.add("Straat En Nr");
            }
            if (stringChecker(m.getWachtwoord())) {
                fouten.add("Wachtwoord");
            }
            if (emailChecker(m.geteMail())) {
                fouten.add("E-mail");
            }
            if (sexChecker(m.getSex())) {
                fouten.add("Sex");
            }
            if (dateChecker(m.getGeboortedatum())) {
                fouten.add("Geboortedatum");
            }
            if (dateChecker(m.getDatumTewerkstelling())) {
                fouten.add("Werkdatum");
            }
            if (telephoneChecker(m.getTelefoonnummer())) {
                fouten.add("Telefoonnummer");
            }
        }
        if (o.getClass() == Wedstrijd.class) {
            Wedstrijd w = (Wedstrijd) o;
            if (stringChecker(w.getPlaats())) {
                fouten.add("Plaats");
            }
            if (stringChecker(w.getNaam())) {
                fouten.add("Naam");
            }
            if (!doubleChecker(w.getInschrijvingsgeld())) {
                fouten.add("Inschrijfprijs");
            }
            if (dateChecker(w.getDatum())) {
                fouten.add("Datum");
            }
        }
        return fouten;
    }

    private boolean stringChecker(String s) {
        return s.isEmpty() || s.length() > 100;
    }

    private boolean emailChecker(String s) {
        return s.isEmpty() || s.length() > 100 || !s.matches("(.*)@(.*).(.*)");
    }

    private boolean sexChecker(String s) {
        return s.isEmpty() || (!Objects.equals(s, "M") && !Objects.equals(s, "F") && !Objects.equals(s, "X"));
    }

    private boolean telephoneChecker(String s) {
        s = s.replace(" ", "");
        return s.isEmpty() || s.length() > 100 || !s.matches("\\+[0-9]+$");
    }

    private boolean doubleChecker(String s) {
        if (!s.isEmpty() && s.length() <= 100) {
            s = s.replace(" ", "");
            try {
                Double.parseDouble(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    private boolean dateChecker(String s) {
        if (!s.isEmpty() && s.length() <= 100) {
            Date date = null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                date = sdf.parse(s);
                if (!s.equals(sdf.format(date))) {
                    date = null;
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            return date == null;
        }
        return true;
    }
}

