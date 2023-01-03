package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.controller.BeheerLopersController;
import be.kuleuven.vrolijkezweters.controller.BeheerMedewerkersController;
import be.kuleuven.vrolijkezweters.controller.BeheerWedstrijdenController;
import be.kuleuven.vrolijkezweters.jdbc.ConnectionManager;
import be.kuleuven.vrolijkezweters.model.*;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class JPanelFactory {
    public Object createJPanel(List<String> items, String operation, Class caller){
        if(caller.equals(BeheerWedstrijdenController.class)){
            return wedstrijdPanel(items);
        }
        if(caller.equals(BeheerLopersController.class)){
            return loperPanel(items, operation);
        }
        if(caller.equals(BeheerMedewerkersController.class)){
            return medewerkerPanel(items, operation);
        }
        return null;
    }

    private Wedstrijd wedstrijdPanel(List<String> items){
        JTextField naam = new JTextField();
        JXDatePicker picker = new JXDatePicker();
        JTextField plaats = new JTextField();
        JTextField inschrijvingsGeld = new JTextField();
        List<Categorie> categorieList = ConnectionManager.handle.createQuery("SELECT * FROM Categorie")
                .mapToBean(Categorie.class)
                .list();
        String[] choices = new String[categorieList.size()];
        for(int i = 0 ; i < categorieList.size(); i++){
            choices[i] = categorieList.get(i).toString().replace("Categorie{categorie'","").replace("}","");
        }
        final JComboBox<String> category = new JComboBox<>(choices);

        picker.setDate(Calendar.getInstance().getTime());
        picker.setFormats(new SimpleDateFormat("dd/MM/yyyy"));

        if (items != null){ // if an item is selected, automatically pre-fill boxes
            naam.setText(items.get(0).substring(1));
            plaats.setText(items.get(2));
            inschrijvingsGeld.setText(items.get(3).substring(0, items.get(3).length() - 1));
            category.setSelectedItem(items.get(4));
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
    private Medewerker medewerkerPanel(List<String> items, String operation){
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

        List<Functie> functieList = ConnectionManager.handle.createQuery("SELECT * FROM Functie")
                .mapToBean(Functie.class)
                .list();
        String[] choices = new String[functieList.size()];
        for(int i = 0 ; i < functieList.size(); i++){
            choices[i] = functieList.get(i).toString().replace("Functie{functie'","").replace("}","");
        }
        final JComboBox<String> functie = new JComboBox<String>(choices);

        if (items != null){ // if an item is selected, automatically pre-fill boxes
            voornaam.setText(items.get(1));
            naam.setText(items.get(2));
            sex.setSelectedItem(items.get(3));
            functie.setSelectedItem(items.get(5));
            telefoonnummer.setText(items.get(6));
            eMail.setText(items.get(7));
            gemeente.setText(items.get(8));
            straatEnNummer.setText(items.get(9).substring(0, items.get(9).length() - 1));
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
    private Loper loperPanel(List<String> items, String operation){
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
