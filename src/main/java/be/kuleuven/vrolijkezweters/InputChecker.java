package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.jdbi.ConnectionManager;
import be.kuleuven.vrolijkezweters.jdbi.LoperJdbi;
import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Medewerker;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;

import java.util.ArrayList;
import java.util.Objects;

public class InputChecker {

    public boolean checkInput(Object o){
        Boolean b = false;
        if (o.getClass() == Loper.class){
                if( ((Loper) o).getNaam().length() <= 100 && !((Loper) o).getNaam().isEmpty() &&
                        ((Loper) o).getVoornaam().length() <= 100 && !((Loper) o).getVoornaam().isEmpty() &&
                        (Objects.equals(((Loper) o).getSex(), "M") || Objects.equals(((Loper) o).getSex(), "F") || Objects.equals(((Loper) o).getSex(), "X")) && ((Loper) o).getSex() != null &&
                        ((Loper) o).getLengte().length() <= 100 && !((Loper) o).getLengte().isEmpty() &&
                        ((Loper) o).getTelefoonNummer().length() <= 100 && !((Loper) o).getTelefoonNummer().isEmpty() &&
                        ((Loper) o).getEmail().length() <= 100 && !((Loper) o).getEmail().isEmpty() && ((Loper) o).getEmail().matches("(.*)@(.*).(.*)") &&
                        ((Loper) o).getGemeente().length() <= 100 && !((Loper) o).getGemeente().isEmpty() &&
                        ((Loper) o).getStraatEnNr().length() <= 100 && !((Loper) o).getStraatEnNr().isEmpty() &&
                        ((Loper) o).getWachtwoord().length() <= 100 && !((Loper) o).getWachtwoord().isEmpty()){
                    b = true;
                }
            LoperJdbi loperJdbi = new LoperJdbi(new ConnectionManager());
                if(loperJdbi.getAll().contains((Loper) o)){
                    b = false;
                }
                return b;
        }
        if (o.getClass() == Medewerker.class){
            return ((Medewerker) o).getNaam().length() <= 100 && !((Medewerker) o).getNaam().isEmpty() &&
                    ((Medewerker) o).getVoornaam().length() <= 100 && !((Medewerker) o).getVoornaam().isEmpty() &&
                    (Objects.equals(((Medewerker) o).getSex(), "M") || Objects.equals(((Medewerker) o).getSex(), "F") || Objects.equals(((Medewerker) o).getSex(), "X")) && ((Medewerker) o).getSex() != null &&
                    ((Medewerker) o).getTelefoonNummer().length() <= 100 && !((Medewerker) o).getTelefoonNummer().isEmpty() &&
                    ((Medewerker) o).getEmail().length() <= 100 && !((Medewerker) o).getEmail().isEmpty() && ((Medewerker) o).getEmail().matches("(.*)@(.*).(.*)") &&
                    ((Medewerker) o).getGemeente().length() <= 100 && !((Medewerker) o).getGemeente().isEmpty() &&
                    ((Medewerker) o).getStraatEnNr().length() <= 100 && !((Medewerker) o).getStraatEnNr().isEmpty() &&
                    ((Medewerker) o).getWachtwoord().length() <= 100 && !((Medewerker) o).getWachtwoord().isEmpty();
        }
        if (o.getClass() == Wedstrijd.class){
            if(((Wedstrijd) o).getNaam().length() <= 100 && !((Wedstrijd) o).getNaam().isEmpty() &&
                    ((Wedstrijd) o).getPlaats().length() <= 100 && !((Wedstrijd) o).getPlaats().isEmpty() &&
                    ((Wedstrijd) o).getInschrijvingsgeld().length() <= 100 && !((Wedstrijd) o).getInschrijvingsgeld().isEmpty() && ((Wedstrijd) o).getInschrijvingsgeld().matches("\\d+\\.\\d+") &&
                    ((Wedstrijd) o).getCategorieID().length() <= 100 && !((Wedstrijd) o).getCategorieID().isEmpty()){
                if (Double.parseDouble(((Wedstrijd) o).getInschrijvingsgeld()) >= 0){
                    return true;
                }
            }
        }
        return false;
    }
}

