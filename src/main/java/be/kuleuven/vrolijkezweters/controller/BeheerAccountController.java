package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.InputChecker;
import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.jdbc.ConnectionManager;
import be.kuleuven.vrolijkezweters.model.Loper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class BeheerAccountController {

    InputChecker inputChecker = new InputChecker();

    public void initialize() {

    }

    private void modifyUserInfo() {
        Loper loper = ConnectionManager.handle.createQuery("SELECT * FROM Loper WHERE id = 4").mapToBean(Loper.class).list().get(0);
        List<String> items = Arrays.asList(loper.toString().split("\\s*,\\s*"));
        String geboortedatum = items.get(0).substring(1);
        String naam = items.get(2);
        String voornaam = items.get(1);
        ArrayList<String> inputData = createJPanel(items);
        String insertQuery = "UPDATE Loper SET " +
                " geboortedatum = '" + inputData.get(0) +
                "' , voornaam= '" + inputData.get(1) +
                "' , naam= '" + inputData.get(2) +
                "' , sex= '" + inputData.get(3) +
                "' , lengte= '" + inputData.get(4) +
                "' , telefoonnummer= '" + inputData.get(5) +
                "' , eMail= '" + inputData.get(6) +
                "' , gemeente= '" + inputData.get(7) +
                "' , straatEnNr= '" +inputData.get(8)   +
                "' WHERE geboorteDatum= '" + geboortedatum + "' AND naam= '"+ naam + "' AND voornaam= '"+ voornaam + "';";

        if(inputChecker.checkInput(inputData, "Loper")){
            ConnectionManager.handle.execute(insertQuery);
        }
        else{
            showAlert("Input error", "De ingegeven data voldoet niet aan de constraints");
        }

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

    public void deleteAccount(){
        int option2 = JOptionPane.showConfirmDialog(null, "Are u sure u want to delete your account?", "Register", JOptionPane.OK_CANCEL_OPTION);
        if (option2 == JOptionPane.OK_OPTION) {
            //String deleteLoper = "DELETE FROM Loper WHERE geboortedatum = '" + geboortedatumI + "' AND voornaam = '" + voornaamI + "' AND naam = '" + naamI + "'";
            //ConnectionManager.handle.execute(deleteLoper);
            System.out.println("gelukt!");
        }

    }

    public void logOut() throws Exception {
        /*final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File currentJar = new File(DBSikorskiJonasGoddeRuben.class.getProtectionDomain().getCodeSource().getLocation().toURI());

        /* is it a jar file? */
        //if(!currentJar.getName().endsWith(".jar"))
          //  return;

        /* Build command: java -jar application.jar */
        /*final ArrayList<String> command = new ArrayList<String>();
        command.add(javaBin);
        command.add("-jar");
        command.add(currentJar.getPath());

        final ProcessBuilder builder = new ProcessBuilder(command);
        builder.start();
        System.exit(0);*/
    }

    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
