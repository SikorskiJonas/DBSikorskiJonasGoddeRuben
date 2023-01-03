package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.InputChecker;
import be.kuleuven.vrolijkezweters.JPanelFactory;
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
    JPanelFactory jPanelFactory = new JPanelFactory();

    public void initialize() {

    }

    private void modifyUserInfo() {
        Loper loper = ConnectionManager.handle.createQuery("SELECT * FROM Loper WHERE id = 4").mapToBean(Loper.class).list().get(0);
        List<String> items = Arrays.asList(loper.toString().split("\\s*,\\s*"));
        String geboortedatum = items.get(0).substring(1);
        String naam = items.get(2);
        String voornaam = items.get(1);
        Loper inputLoper = (Loper) jPanelFactory.createJPanel(items, "modify", Loper.class);
        String insertQuery = "UPDATE Loper SET " +
                " geboortedatum = '" + inputLoper.getGeboorteDatum() +
                "' , voornaam= '" + inputLoper.getVoornaam() +
                "' , naam= '" + inputLoper.getNaam() +
                "' , sex= '" + inputLoper.getSex() +
                "' , lengte= '" + inputLoper.getLengte()+
                "' , telefoonnummer= '" + inputLoper.getTelefoonNummer() +
                "' , eMail= '" + inputLoper.getEmail() +
                "' , gemeente= '" + inputLoper.getGemeente() +
                "' , straatEnNr= '" +inputLoper.getStraatEnNr()   +
                "' WHERE geboorteDatum= '" + geboortedatum + "' AND naam= '"+ naam + "' AND voornaam= '"+ voornaam + "';";

        if(inputChecker.checkInput(inputLoper)){
            ConnectionManager.handle.execute(insertQuery);
        }
        else{
            showAlert("Input error", "De ingegeven data voldoet niet aan de constraints");
        }

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
