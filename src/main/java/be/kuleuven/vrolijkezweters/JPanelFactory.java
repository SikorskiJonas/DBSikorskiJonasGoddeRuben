package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.controller.BeheerLopersController;
import be.kuleuven.vrolijkezweters.controller.BeheerMedewerkersController;
import be.kuleuven.vrolijkezweters.controller.BeheerWedstrijdenController;
import be.kuleuven.vrolijkezweters.controller.ProjectMainController;
import be.kuleuven.vrolijkezweters.jdbi.CategorieJdbi;
import be.kuleuven.vrolijkezweters.jdbi.ConnectionManager;
import be.kuleuven.vrolijkezweters.jdbi.FunctieJdbi;
import be.kuleuven.vrolijkezweters.jdbi.WedstrijdJdbi;
import be.kuleuven.vrolijkezweters.model.*;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class JPanelFactory {
    WedstrijdJdbi wedstrijdJdbi = new WedstrijdJdbi(ProjectMainController.connectionManager);
    CategorieJdbi categorieJdbi = new CategorieJdbi(ProjectMainController.connectionManager);
    FunctieJdbi functieJdbi = new FunctieJdbi(ProjectMainController.connectionManager);

    public Object createJPanel(Object o, String operation, Class caller){
        if(caller.equals(BeheerWedstrijdenController.class)){
            return wedstrijdPanel((Wedstrijd) o);
        }
        if(caller.equals(BeheerLopersController.class)){
            return loperPanel((Loper) o, operation);
        }
        if(caller.equals(BeheerMedewerkersController.class)){
            return medewerkerPanel((Medewerker) o, operation);
        }
        return null;
    }

    private Wedstrijd wedstrijdPanel(Wedstrijd wedstrijdIn){
        JTextField naam = new JTextField();
        JXDatePicker picker = new JXDatePicker();
        JTextField plaats = new JTextField();
        JTextField inschrijvingsGeld = new JTextField();
        List<Categorie> categorieList = categorieJdbi.getAll();
        String[] choices = new String[categorieList.size()];
        for(int i = 0 ; i < categorieList.size(); i++){
            choices[i] = categorieList.get(i).toString().replace("Categorie{categorie'","").replace("}","");
        }
        final JComboBox<String> category = new JComboBox<>(choices);

        picker.setDate(Calendar.getInstance().getTime());
        picker.setFormats(new SimpleDateFormat("dd/MM/yyyy"));

        if (wedstrijdIn != null){ // if an item is selected, automatically pre-fill boxes
            naam.setText(wedstrijdIn.getNaam());
            plaats.setText(wedstrijdIn.getPlaats());
            inschrijvingsGeld.setText(wedstrijdIn.getInschrijvingsgeld());
            category.setSelectedItem(wedstrijdIn.getCategorieID());
        }

        Object[] message = { "Naam: ", naam,
                "datum: ", picker,
                "Locatie: ", plaats,
                "Inschrijfprijs: ", inschrijvingsGeld,
                "Categorie", category};
        String[] buttons = { "Save", "Cancel" };
        int option = JOptionPane.showOptionDialog(null, message, "Add Loper", JOptionPane.OK_CANCEL_OPTION, 0, null, buttons, buttons[0]);

        Date date = picker.getDate();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateFormatted = format.format(date);
        Wedstrijd wedstrijd = new Wedstrijd();
        wedstrijd.setNaam(naam.getText());
        wedstrijd.setDatum(dateFormatted);
        wedstrijd.setPlaats(plaats.getText());
        wedstrijd.setInschrijvingsgeld(inschrijvingsGeld.getText());
        wedstrijd.setCategorieID(category.getSelectedItem().toString());
        return wedstrijd;
    }

    private Medewerker medewerkerPanel(Medewerker medewerkerIn, String operation){
        JXDatePicker geboortedatum = new JXDatePicker();
        JTextField voornaam = new JTextField(5);
        JTextField naam = new JTextField(5);
        String[] geslactKeuzes = {"M", "F", "X"};
        JComboBox sex = new JComboBox<>(geslactKeuzes);
        JXDatePicker werkDatum = new JXDatePicker();
        JTextField telefoonnummer = new JTextField(5);
        JTextField eMail = new JTextField(5);
        JTextField gemeente = new JTextField(5);
        JTextField straatEnNummer = new JTextField(5);
        JCheckBox isAdmin = new JCheckBox();
        String wachtwoord = "";

        geboortedatum.setDate(Calendar.getInstance().getTime());
        geboortedatum.setFormats(new SimpleDateFormat("dd/MM/yyyy"));
        werkDatum.setDate(Calendar.getInstance().getTime());
        werkDatum.setFormats(new SimpleDateFormat("dd/MM/yyyy"));

        List<Functie> functieList = functieJdbi.getAll();
        String[] choices = new String[functieList.size()];
        for(int i = 0 ; i < functieList.size(); i++){
            choices[i] = functieList.get(i).toString().replace("Functie{functie'","").replace("}","");
        }
        final JComboBox<String> functie = new JComboBox<String>(choices);

        if (medewerkerIn != null){ // if an item is selected, automatically pre-fill boxes
            voornaam.setText(medewerkerIn.getVoornaam());
            naam.setText(medewerkerIn.getNaam());
            sex.setSelectedItem(medewerkerIn.getSex());
            functie.setSelectedItem(medewerkerIn.getFunctieId());
            telefoonnummer.setText(medewerkerIn.getTelefoonNummer());
            eMail.setText(medewerkerIn.getEmail());
            gemeente.setText(medewerkerIn.getGemeente());
            straatEnNummer.setText(medewerkerIn.getStraatEnNr());
        }
        String[] buttons = { "Save", "Cancel" };
        if(operation.equals("add")){
            wachtwoord = generatePassword();
            Object[] message = { "Geboortedatum: ", geboortedatum,
                    "Voornaam: ", voornaam,
                    "Naam: ", naam,
                    "Geslacht: ", sex,
                    "Datum Tewerkstelling: ", werkDatum,
                    "Functie", functie,
                    "Telefoon: ", telefoonnummer,
                    "E-mail: ", eMail,
                    "Gemeente: ", gemeente,
                    "Straat + nr: ", straatEnNummer,
                    "is Admin: ", isAdmin,
                    "Wachtwoord: ", wachtwoord};
            int option = JOptionPane.showOptionDialog(null, message, "Add Medewerker", JOptionPane.OK_CANCEL_OPTION, 0, null, buttons, buttons[0]);

        }
        else if(operation.equals("modify")){
            Object[] message = { "Geboortedatum: ", geboortedatum,
                    "Voornaam: ", voornaam,
                    "Naam: ", naam,
                    "Geslacht: ", sex,
                    "Functie", functie,
                    "Telefoon: ", telefoonnummer,
                    "E-mail: ", eMail,
                    "Gemeente: ", gemeente,
                    "Straat + nr: ", straatEnNummer,
                    "is Admin: ", isAdmin};
            int option = JOptionPane.showOptionDialog(null, message, "Bewerk Medewerker", JOptionPane.OK_CANCEL_OPTION, 0, null, buttons, buttons[0]);
        }

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String geboorteDatumFormatted = format.format(geboortedatum.getDate());
        String werkDatumFormatted = format.format(werkDatum.getDate());
        Medewerker medewerker = new Medewerker();
        medewerker.setGeboorteDatum(geboorteDatumFormatted);
        medewerker.setVoornaam(voornaam.getText());
        medewerker.setNaam(naam.getText());
        medewerker.setSex(sex.getSelectedItem().toString());
        medewerker.setDatumTewerkstelling(werkDatumFormatted);
        medewerker.setFunctieId(functie.getSelectedItem().toString());
        medewerker.setTelefoonNummer(telefoonnummer.getText());
        medewerker.setEmail(eMail.getText());
        medewerker.setGemeente(gemeente.getText());
        medewerker.setStraatEnNr(straatEnNummer.getText());
        medewerker.setIsAdmin(String.valueOf(isAdmin.isSelected()));
        medewerker.setWachtwoord(wachtwoord);
        return medewerker;
    }

    private Loper loperPanel(Loper loperIn, String operation){
        JXDatePicker geboortedatum = new JXDatePicker();
        JTextField voornaam = new JTextField(5);
        JTextField naam = new JTextField(5);
        String[] geslactKeuzes = {"M", "F", "X"};
        JComboBox sex = new JComboBox<String>(geslactKeuzes);
        JTextField lengte = new JTextField(3);
        JTextField telefoonnummer = new JTextField(5);
        JTextField eMail = new JTextField(5);
        JTextField gemeente = new JTextField(5);
        JTextField straatEnNummer = new JTextField(5);
        String wachtwoord = "";

        geboortedatum.setDate(Calendar.getInstance().getTime());
        geboortedatum.setFormats(new SimpleDateFormat("dd/MM/yyyy"));

        if (loperIn != null){ // if an item is selected, automatically pre-fill boxes
            voornaam.setText(loperIn.getVoornaam());
            naam.setText(loperIn.getNaam());
            sex.setSelectedItem(loperIn.getSex());
            lengte.setText(loperIn.getLengte());
            telefoonnummer.setText(loperIn.getTelefoonNummer());
            eMail.setText(loperIn.getEmail());
            gemeente.setText(loperIn.getGemeente());
            straatEnNummer.setText(loperIn.getStraatEnNr());
        }
        String[] buttons = { "Save", "Cancel" };

        if(operation.equals("add")){
            wachtwoord = generatePassword();
            Object[] message = { "Geboortedatum: ", geboortedatum,
                    "Voornaam: ", voornaam,
                    "Naam: ", naam,
                    "Geslacht: ", sex,
                    "Lengte: ", lengte,
                    "Telefoon: ", telefoonnummer,
                    "E-mail: ", eMail,
                    "Gemeente: ", gemeente,
                    "Straat + nr: ", straatEnNummer,
                    "Generated Wachtwoord:", wachtwoord};
            int option = JOptionPane.showOptionDialog(null, message, "Add Loper", JOptionPane.OK_CANCEL_OPTION, 0, null, buttons, buttons[0]);

        }
        else if(operation.equals("modify")){
            Object[] message = { "Geboortedatum: ", geboortedatum,
                    "Voornaam: ", voornaam,
                    "Naam: ", naam,
                    "Geslacht: ", sex,
                    "Lengte: ", lengte,
                    "Telefoon: ", telefoonnummer,
                    "E-mail: ", eMail,
                    "Gemeente: ", gemeente,
                    "Straat + nr: ", straatEnNummer};
            int option = JOptionPane.showOptionDialog(null, message, "Add Loper", JOptionPane.OK_CANCEL_OPTION, 0, null, buttons, buttons[0]);

        }


        Date date = geboortedatum.getDate();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateFormatted = format.format(date);
        Loper loper = new Loper();
        loper.setGeboorteDatum(dateFormatted);
        loper.setVoornaam(voornaam.getText());
        loper.setNaam(naam.getText());
        loper.setSex(sex.getSelectedItem().toString());
        loper.setLengte(lengte.getText());
        loper.setTelefoonNummer(telefoonnummer.getText());
        loper.setEmail(eMail.getText());
        loper.setGemeente(gemeente.getText());
        loper.setStraatEnNr(straatEnNummer.getText());
        loper.setWachtwoord(wachtwoord);
        return loper;
    }

    public ArrayList<Etappe> schrijfIn(List<Etappe> etappeList){
        ArrayList<String> choices = new ArrayList<>();
        ArrayList<JCheckBox> etappeSelectie = new ArrayList<>();
        for(int i = 0 ; i < etappeList.size(); i++){
            choices.add(etappeList.get(i).getNaam());
            etappeSelectie.add(new JCheckBox());
        }
        Object[] message = new Object[2 * choices.size()];
        for (int i = 0; i < 2 * choices.size(); i =i+2){
            message[i] = choices.get(i/2);
            message[i+1] = new JCheckBox();
        }
        String[] buttons = { "Save", "Cancel" };
        int option = JOptionPane.showOptionDialog(null, message, "Aan welke etappes neem je deel?", JOptionPane.OK_CANCEL_OPTION, 0, null, buttons, buttons[0]);
        ArrayList<Etappe> gekozenEtappes = new ArrayList<>();
        for (int i = 0; i < choices.size(); i++){
            if (((JCheckBox) message[(i*2)+1]).isSelected()){
                gekozenEtappes.add(etappeList.get(i));
            }
        }
        return gekozenEtappes;
    }

    private ArrayList<String> createJPanel(List<String> items){
        JXDatePicker geboortedatum = new JXDatePicker();
        JTextField voornaam = new JTextField(5);
        JTextField naam = new JTextField(5);
        String[] geslactKeuzes = {"M", "F", "X"};
        JComboBox sex = new JComboBox<String>(geslactKeuzes);
        JTextField lengte = new JTextField(3);
        JTextField telefoonnummer = new JTextField(5);
        JTextField eMail = new JTextField(5);
        JTextField gemeente = new JTextField(5);
        JTextField straatEnNummer = new JTextField(5);

        geboortedatum.setDate(Calendar.getInstance().getTime());
        geboortedatum.setFormats(new SimpleDateFormat("dd/MM/yyyy"));

        if (items != null){ // if an item is selected, automatically pre-fill boxes
            voornaam.setText(items.get(1));
            naam.setText(items.get(2));
            sex.setSelectedItem(items.get(3));
            lengte.setText(items.get(4));
            telefoonnummer.setText(items.get(5));
            eMail.setText(items.get(6));
            gemeente.setText(items.get(7));
            straatEnNummer.setText(items.get(8).substring(0, items.get(8).length() - 1));
        }

        Object[] message = { "Geboortedatum: ", geboortedatum,
                "Voornaam: ", voornaam,
                "Naam: ", naam,
                "Geslacht: ", sex,
                "Lengte: ", lengte,
                "Telefoon: ", telefoonnummer,
                "E-mail: ", eMail,
                "Gemeente: ", gemeente,
                "Straat + nr: ", straatEnNummer};
        String[] buttons = { "Save", "Cancel" };
        int option = JOptionPane.showOptionDialog(null, message, "Add Loper", JOptionPane.OK_CANCEL_OPTION, 0, null, buttons, buttons[0]);

        Date date = geboortedatum.getDate();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateFormatted = format.format(date);
        ArrayList<String> r = new ArrayList();
        r.add(dateFormatted);
        r.add(voornaam.getText());
        r.add(naam.getText());
        r.add(sex.getSelectedItem().toString());
        r.add(lengte.getText());
        r.add(telefoonnummer.getText());
        r.add(eMail.getText());
        r.add(gemeente.getText());
        r.add(straatEnNummer.getText());
        return r;
    }
    public String generatePassword(){
        char[] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRST".toCharArray();

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String randomStr = sb.toString();

        return randomStr;
    }
}
