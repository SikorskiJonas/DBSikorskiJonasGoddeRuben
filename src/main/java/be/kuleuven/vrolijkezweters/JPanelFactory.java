package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.controller.BeheerLopersController;
import be.kuleuven.vrolijkezweters.controller.BeheerMedewerkersController;
import be.kuleuven.vrolijkezweters.controller.BeheerWedstrijdenController;
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
            }catch (ParseException ignored){
            }
        }

        Object[] message = {"Naam: ", naam, "datum: ", picker, "Locatie: ", plaats, "Inschrijfprijs: ", inschrijvingsGeld, "Categorie", category};
        String[] buttons = {"Save", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, message, "Add Wedstrijd", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);
        if (option == JOptionPane.OK_OPTION) {
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
        return null;

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
            }catch (ParseException ignored){
            }
        }
        String[] buttons = {"Save", "Cancel"};
        int option = 0;
        if (operation.equals("add")) {
            wachtwoord = generatePassword();
            Object[] message = {"Geboortedatum: ", geboortedatum, "Voornaam: ", voornaam, "Naam: ", naam, "Geslacht: ", sex, "Datum Tewerkstelling: ", werkDatum, "Functie", functie, "Telefoon: ", telefoonnummer, "E-mail: ", eMail, "Gemeente: ", gemeente, "Straat + nr: ", straatEnNummer, "is Admin: ", isAdmin, "Wachtwoord: ", wachtwoord};
            option = JOptionPane.showOptionDialog(null, message, "Add Medewerker", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);

        } else if (operation.equals("modify")) {
            Object[] message = {"Geboortedatum: ", geboortedatum, "Voornaam: ", voornaam, "Naam: ", naam, "Geslacht: ", sex, "Functie", functie, "Telefoon: ", telefoonnummer, "E-mail: ", eMail, "Gemeente: ", gemeente, "Straat + nr: ", straatEnNummer, "is Admin: ", isAdmin};
            option = JOptionPane.showOptionDialog(null, message, "Bewerk Medewerker", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);
        }

        if (option == JOptionPane.OK_OPTION) {
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
        return null;

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
            }catch (ParseException ignored){
            }
        }
        String[] buttons = {"Save", "Cancel"};
        int option = 0;
        if (operation.equals("add")) {
            wachtwoord = generatePassword();
            Object[] message = {"Geboortedatum: ", geboortedatum, "Voornaam: ", voornaam, "Naam: ", naam, "Geslacht: ", sex, "Lengte: ", lengte, "Telefoon: ", telefoonnummer, "E-mail: ", eMail, "Gemeente: ", gemeente, "Straat + nr: ", straatEnNummer, "Generated Wachtwoord:", wachtwoord};
            option = JOptionPane.showOptionDialog(null, message, "Add Loper", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);

        } else if (operation.equals("modify")) {
            Object[] message = {"Geboortedatum: ", geboortedatum, "Voornaam: ", voornaam, "Naam: ", naam, "Geslacht: ", sex, "Lengte: ", lengte, "Telefoon: ", telefoonnummer, "E-mail: ", eMail, "Gemeente: ", gemeente, "Straat + nr: ", straatEnNummer};
            option = JOptionPane.showOptionDialog(null, message, "Add Loper", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);

        }

        if (option == JOptionPane.OK_OPTION) {
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
        return null;
        
    }

    public Etappe etappePanel(Wedstrijd w) {
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
        wedstrijd.setSelectedItem(w.getNaam());

        Object[] message = {"Naam: ", naam, "startlocatie: ", startPlaats, "eindlocatie: ", eindPlaats, "afstand in meter: ", afstandMeter, "deel van", wedstrijd};
        String[] buttons = {"Save", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, message, "Add Etappe", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);
        if (option == JOptionPane.OK_OPTION) {
            Etappe etappe = new Etappe();
            etappe.setNaam(naam.getText());
            etappe.setAfstandMeter(Integer.parseInt(afstandMeter.getText()));
            etappe.setStartPlaats(startPlaats.getText());
            etappe.setEindPlaats(eindPlaats.getText());
            etappe.setWedstrijdId(wedstrijdDao.getId(wedstrijdDao.getByNaam(Objects.requireNonNull(wedstrijd.getSelectedItem()).toString())));
            return etappe;
        }
        return null;

    }

    public Categorie categoriePanel() {
        JTextField naam = new JTextField();

        Object[] message = {"Naam: ", naam};
        String[] buttons = {"Save", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, message, "Create Categorie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);
        if (option == JOptionPane.OK_OPTION) {
            Categorie categorie = new Categorie();
            categorie.setCategorie(naam.getText());
            return categorie;
        }
        return null;
    }

    public Functie functiePanel() {
        JTextField naam = new JTextField();
        Object[] message = {"Naam: ", naam};
        String[] buttons = {"Save", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, message, "Create Categorie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);
        if (option == JOptionPane.OK_OPTION) {
            Functie functie = new Functie();
            functie.setFunctie(naam.getText());
            return functie;
        }
        return null;
    }

    public List<LoopNummer> loopNummerPanel(List<LoopNummer> loopNummers) {
        ArrayList<JTextField> uren = new ArrayList<>();
        ArrayList<JTextField> minuten = new ArrayList<>();
        ArrayList<JTextField> seconden = new ArrayList<>();
        for (LoopNummer loopNummer : loopNummers) {
            int uurT = loopNummer.getLooptijd() / 3600;
            int minuutT = (loopNummer.getLooptijd() % 3600) / 60;
            int secondeT = loopNummer.getLooptijd() % 60;
            uren.add(new JTextField(String.valueOf(uurT)));
            minuten.add(new JTextField(String.valueOf(minuutT)));
            seconden.add(new JTextField(String.valueOf(secondeT)));
        }
        Object[] message = new Object[3*uren.size() + 3*uren.size() + uren.size()];
        int j = 0;
        for (int i = 0; i < message.length; i = i + 7){
            message[i] = "Etappe " + ((i/7) +1);
            message[i+1] = "Uren ";
            message[i+2] = uren.get(j);
            message[i+3] = "Minuten ";
            message[i+4] = minuten.get(j);
            message[i+5] = "Seconden ";
            message[i+6] = seconden.get(j);
            j++;
        }
        String[] buttons = {"Save", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, message, "Edit Looptijd", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);
        if (option == JOptionPane.OK_OPTION) {
            for (int i = 0; i < loopNummers.size() ; i++){
                loopNummers.get(i).setLooptijd(3600*Integer.parseInt(uren.get(i).getText()) + 60*Integer.parseInt(minuten.get(i).getText()) + Integer.parseInt(seconden.get(i).getText()) );
            }
            return loopNummers;
        }
        return null;
    }

    public String generatePassword() {
        char[] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRST" .toCharArray();

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }

        return sb.toString();
    }
}
