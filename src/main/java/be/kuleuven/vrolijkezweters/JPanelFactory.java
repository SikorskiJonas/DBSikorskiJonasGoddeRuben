package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.controller.BeheerLopersController;
import be.kuleuven.vrolijkezweters.controller.BeheerMedewerkersController;
import be.kuleuven.vrolijkezweters.controller.BeheerWedstrijdenController;
import be.kuleuven.vrolijkezweters.controller.ProjectMainController;
import be.kuleuven.vrolijkezweters.jdbi.CategorieDao;
import be.kuleuven.vrolijkezweters.jdbi.FunctieDao;
import be.kuleuven.vrolijkezweters.jdbi.WedstrijdDao;
import be.kuleuven.vrolijkezweters.model.*;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JPanelFactory {
    final WedstrijdDao wedstrijdDao = new WedstrijdDao();
    final CategorieDao categorieDao = new CategorieDao();
    final FunctieDao functieDao = new FunctieDao();

    public Object createJPanel(Object o, String operation, Class caller) {
        if (caller.equals(BeheerWedstrijdenController.class)) {
            return wedstrijdPanel((Wedstrijd) o);
        }
        if (caller.equals(BeheerLopersController.class)) {
            return loperPanel((Loper) o, operation);
        }
        if (caller.equals(BeheerMedewerkersController.class)) {
            return medewerkerPanel((Medewerker) o, operation);
        }
        return null;
    }

    private Wedstrijd wedstrijdPanel(Wedstrijd wedstrijdIn) {
        JTextField naam = new JTextField();
        JXDatePicker picker = new JXDatePicker();
        JTextField plaats = new JTextField();
        JTextField inschrijvingsGeld = new JTextField();
        List<Categorie> categorieList = categorieDao.getAll();
        String[] choices = new String[categorieList.size()];
        for (int i = 0; i < categorieList.size(); i++) {
            choices[i] = categorieList.get(i).toString().replace("Categorie{categorie'", "").replace("}", "");
        }
        final JComboBox<String> category = new JComboBox<>(choices);

        picker.setDate(Calendar.getInstance().getTime());
        picker.setFormats(new SimpleDateFormat("dd/MM/yyyy"));

        if (wedstrijdIn != null) { // if an item is selected, automatically pre-fill boxes
            naam.setText(wedstrijdIn.getNaam());
            plaats.setText(wedstrijdIn.getPlaats());
            inschrijvingsGeld.setText(wedstrijdIn.getInschrijvingsgeld());
            category.setSelectedItem(wedstrijdIn.getCategorieID());
            try {
                picker.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(wedstrijdIn.getDatum()));
            }catch (ParseException e){
            }
        }

        Object[] message = {"Naam: ", naam, "datum: ", picker, "Locatie: ", plaats, "Inschrijfprijs: ", inschrijvingsGeld, "Categorie", category};
        String[] buttons = {"Save", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, message, "Add Wedstrijd", JOptionPane.OK_CANCEL_OPTION, 0, null, buttons, buttons[0]);

        Date date = picker.getDate();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateFormatted = format.format(date);
        Wedstrijd wedstrijd = new Wedstrijd();
        wedstrijd.setNaam(naam.getText());
        wedstrijd.setDatum(dateFormatted);
        wedstrijd.setPlaats(plaats.getText());
        wedstrijd.setInschrijvingsgeld(inschrijvingsGeld.getText());
        wedstrijd.setCategorieID(Objects.requireNonNull(Objects.requireNonNull(category.getSelectedItem())).toString());
        return wedstrijd;
    }

    private Medewerker medewerkerPanel(Medewerker medewerkerIn, String operation) {
        JXDatePicker geboortedatum = new JXDatePicker();
        JTextField voornaam = new JTextField(5);
        JTextField naam = new JTextField(5);
        String[] geslactKeuzes = {"M", "F", "X"};
        JComboBox<String> sex = new JComboBox<>(geslactKeuzes);
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

        List<Functie> functieList = functieDao.getAll();
        String[] choices = new String[functieList.size()];
        for (int i = 0; i < functieList.size(); i++) {
            choices[i] = functieList.get(i).toString().replace("Functie{functie'", "").replace("}", "");
        }
        final JComboBox<String> functie = new JComboBox<>(choices);

        if (medewerkerIn != null) { // if an item is selected, automatically pre-fill boxes
            voornaam.setText(medewerkerIn.getVoornaam());
            naam.setText(medewerkerIn.getNaam());
            sex.setSelectedItem(medewerkerIn.getSex());
            functie.setSelectedItem(medewerkerIn.getFunctieId());
            telefoonnummer.setText(medewerkerIn.getTelefoonnummer());
            eMail.setText(medewerkerIn.geteMail());
            gemeente.setText(medewerkerIn.getGemeente());
            straatEnNummer.setText(medewerkerIn.getStraatEnNr());
            try {
                geboortedatum.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(medewerkerIn.getGeboortedatum()));
                werkDatum.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(medewerkerIn.getDatumTewerkstelling()));
            }catch (ParseException e){
            }
        }
        String[] buttons = {"Save", "Cancel"};
        if (operation.equals("add")) {
            wachtwoord = generatePassword();
            Object[] message = {"Geboortedatum: ", geboortedatum, "Voornaam: ", voornaam, "Naam: ", naam, "Geslacht: ", sex, "Datum Tewerkstelling: ", werkDatum, "Functie", functie, "Telefoon: ", telefoonnummer, "E-mail: ", eMail, "Gemeente: ", gemeente, "Straat + nr: ", straatEnNummer, "is Admin: ", isAdmin, "Wachtwoord: ", wachtwoord};
            int option = JOptionPane.showOptionDialog(null, message, "Add Medewerker", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);

        } else if (operation.equals("modify")) {
            Object[] message = {"Geboortedatum: ", geboortedatum, "Voornaam: ", voornaam, "Naam: ", naam, "Geslacht: ", sex, "Functie", functie, "Telefoon: ", telefoonnummer, "E-mail: ", eMail, "Gemeente: ", gemeente, "Straat + nr: ", straatEnNummer, "is Admin: ", isAdmin};
            int option = JOptionPane.showOptionDialog(null, message, "Bewerk Medewerker", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);
        }

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String geboorteDatumFormatted = format.format(geboortedatum.getDate());
        String werkDatumFormatted = format.format(werkDatum.getDate());
        Medewerker medewerker = new Medewerker();
        medewerker.setGeboortedatum(geboorteDatumFormatted);
        medewerker.setVoornaam(voornaam.getText());
        medewerker.setNaam(naam.getText());
        medewerker.setSex(Objects.requireNonNull(sex.getSelectedItem()).toString());
        medewerker.setDatumTewerkstelling(werkDatumFormatted);
        medewerker.setFunctieId(Objects.requireNonNull(functie.getSelectedItem()).toString());
        medewerker.setTelefoonnummer(telefoonnummer.getText());
        medewerker.seteMail(eMail.getText());
        medewerker.setGemeente(gemeente.getText());
        medewerker.setStraatEnNr(straatEnNummer.getText());
        medewerker.setIsAdmin(String.valueOf(isAdmin.isSelected()));
        medewerker.setWachtwoord(wachtwoord);
        return medewerker;
    }

    private Loper loperPanel(Loper loperIn, String operation) {
        JXDatePicker geboortedatum = new JXDatePicker();
        JTextField voornaam = new JTextField(5);
        JTextField naam = new JTextField(5);
        String[] geslactKeuzes = {"M", "F", "X"};
        JComboBox<String> sex = new JComboBox<>(geslactKeuzes);
        JTextField lengte = new JTextField(3);
        JTextField telefoonnummer = new JTextField(5);
        JTextField eMail = new JTextField(5);
        JTextField gemeente = new JTextField(5);
        JTextField straatEnNummer = new JTextField(5);
        String wachtwoord = "";

        geboortedatum.setDate(Calendar.getInstance().getTime());
        geboortedatum.setFormats(new SimpleDateFormat("dd/MM/yyyy"));

        if (loperIn != null) { // if an item is selected, automatically pre-fill boxes
            voornaam.setText(loperIn.getVoornaam());
            naam.setText(loperIn.getNaam());
            sex.setSelectedItem(loperIn.getSex());
            lengte.setText(loperIn.getLengte());
            telefoonnummer.setText(loperIn.getTelefoonnummer());
            eMail.setText(loperIn.geteMail());
            gemeente.setText(loperIn.getGemeente());
            straatEnNummer.setText(loperIn.getStraatEnNr());
            try {
                geboortedatum.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(loperIn.getGeboortedatum()));
            }catch (ParseException e){
            }
        }
        String[] buttons = {"Save", "Cancel"};

        if (operation.equals("add")) {
            wachtwoord = generatePassword();
            Object[] message = {"Geboortedatum: ", geboortedatum, "Voornaam: ", voornaam, "Naam: ", naam, "Geslacht: ", sex, "Lengte: ", lengte, "Telefoon: ", telefoonnummer, "E-mail: ", eMail, "Gemeente: ", gemeente, "Straat + nr: ", straatEnNummer, "Generated Wachtwoord:", wachtwoord};
            int option = JOptionPane.showOptionDialog(null, message, "Add Loper", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);

        } else if (operation.equals("modify")) {
            Object[] message = {"Geboortedatum: ", geboortedatum, "Voornaam: ", voornaam, "Naam: ", naam, "Geslacht: ", sex, "Lengte: ", lengte, "Telefoon: ", telefoonnummer, "E-mail: ", eMail, "Gemeente: ", gemeente, "Straat + nr: ", straatEnNummer};
            int option = JOptionPane.showOptionDialog(null, message, "Add Loper", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);

        }


        Date date = geboortedatum.getDate();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateFormatted = format.format(date);
        Loper loper = new Loper();
        loper.setGeboortedatum(dateFormatted);
        loper.setVoornaam(voornaam.getText());
        loper.setNaam(naam.getText());
        loper.setSex(Objects.requireNonNull(sex.getSelectedItem()).toString());
        loper.setLengte(lengte.getText());
        loper.setTelefoonnummer(telefoonnummer.getText());
        loper.seteMail(eMail.getText());
        loper.setGemeente(gemeente.getText());
        loper.setStraatEnNr(straatEnNummer.getText());
        loper.setWachtwoord(wachtwoord);
        return loper;
    }

    private ArrayList<String> createJPanel(List<String> items) {
        JXDatePicker geboortedatum = new JXDatePicker();
        JTextField voornaam = new JTextField(5);
        JTextField naam = new JTextField(5);
        String[] geslactKeuzes = {"M", "F", "X"};
        JComboBox<String> sex = new JComboBox<>(geslactKeuzes);
        JTextField lengte = new JTextField(3);
        JTextField telefoonnummer = new JTextField(5);
        JTextField eMail = new JTextField(5);
        JTextField gemeente = new JTextField(5);
        JTextField straatEnNummer = new JTextField(5);

        geboortedatum.setDate(Calendar.getInstance().getTime());
        geboortedatum.setFormats(new SimpleDateFormat("dd/MM/yyyy"));

        if (items != null) { // if an item is selected, automatically pre-fill boxes
            voornaam.setText(items.get(1));
            naam.setText(items.get(2));
            sex.setSelectedItem(items.get(3));
            lengte.setText(items.get(4));
            telefoonnummer.setText(items.get(5));
            eMail.setText(items.get(6));
            gemeente.setText(items.get(7));
            straatEnNummer.setText(items.get(8).substring(0, items.get(8).length() - 1));
        }

        Object[] message = {"Geboortedatum: ", geboortedatum, "Voornaam: ", voornaam, "Naam: ", naam, "Geslacht: ", sex, "Lengte: ", lengte, "Telefoon: ", telefoonnummer, "E-mail: ", eMail, "Gemeente: ", gemeente, "Straat + nr: ", straatEnNummer};
        String[] buttons = {"Save", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, message, "Add Loper", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);

        Date date = geboortedatum.getDate();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateFormatted = format.format(date);
        ArrayList r = new ArrayList();
        r.add(dateFormatted);
        r.add(voornaam.getText());
        r.add(naam.getText());
        r.add(Objects.requireNonNull(sex.getSelectedItem()).toString());
        r.add(lengte.getText());
        r.add(telefoonnummer.getText());
        r.add(eMail.getText());
        r.add(gemeente.getText());
        r.add(straatEnNummer.getText());
        return r;
    }

    public Etappe etappePanel() {
        JTextField naam = new JTextField();
        JTextField startPlaats = new JTextField();
        JTextField eindPlaats = new JTextField();
        JTextField afstandMeter = new JTextField();
        List<Wedstrijd> wedstrijdList = wedstrijdDao.getAll();
        String[] choices = new String[wedstrijdList.size()];
        for (int i = 0; i < wedstrijdList.size(); i++) {
            choices[i] = wedstrijdList.get(i).getNaam();
        }
        final JComboBox<String> wedstrijd = new JComboBox<>(choices);

        Object[] message = {"Naam: ", naam, "startlocatie: ", startPlaats, "eindlocatie: ", eindPlaats, "afstand in meter: ", afstandMeter, "deel van", wedstrijd};
        String[] buttons = {"Save", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, message, "Add Etappe", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);

        Etappe etappe = new Etappe();
        etappe.setNaam(naam.getText());
        etappe.setAfstandMeter(Integer.parseInt(afstandMeter.getText()));
        etappe.setStartPlaats(startPlaats.getText());
        etappe.setEindPlaats(eindPlaats.getText());
        etappe.setWedstrijdId(wedstrijdDao.getId(wedstrijdDao.getByNaam(wedstrijd.getSelectedItem().toString())));
        return etappe;
    }

    public Categorie categoriePanel() {
        JTextField naam = new JTextField();

        Object[] message = {"Naam: ", naam};
        String[] buttons = {"Save", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, message, "Create Categorie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);

        Categorie categorie = new Categorie();
        categorie.setCategorie(naam.getText());
        return categorie;
    }

    public Functie functiePanel() {
        JTextField naam = new JTextField();
        Object[] message = {"Naam: ", naam};
        String[] buttons = {"Save", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, message, "Create Categorie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);

        Functie functie = new Functie();
        functie.setFunctie(naam.getText());
        return functie;
    }

    public String generatePassword() {
        char[] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRST".toCharArray();

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }

        return sb.toString();
    }
}
