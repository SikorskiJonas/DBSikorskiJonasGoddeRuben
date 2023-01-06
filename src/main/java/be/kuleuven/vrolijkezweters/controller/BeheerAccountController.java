package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.InputChecker;
import be.kuleuven.vrolijkezweters.JPanelFactory;
import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.jdbc.ConnectionManager;
import be.kuleuven.vrolijkezweters.jdbc.FunctieJdbi;
import be.kuleuven.vrolijkezweters.jdbc.LoperJdbi;
import be.kuleuven.vrolijkezweters.jdbc.MedewerkerJdbi;
import be.kuleuven.vrolijkezweters.model.Functie;
import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Medewerker;
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
    MedewerkerJdbi medewerkerJdbi = new MedewerkerJdbi(ProjectMainController.connectionManager);
    LoperJdbi loperJdbi = new LoperJdbi(ProjectMainController.connectionManager);
    FunctieJdbi functieJdbi= new FunctieJdbi(ProjectMainController.connectionManager);

    public void initialize() {

    }

    public Object modifyUserInfo(Object user) {
        JPanelFactory jPanelFactory = new JPanelFactory();
        if(user.getClass()== Medewerker.class){
            String naam = ((Medewerker)user).getNaam();
            String voornaam = ((Medewerker)user).getVoornaam();
            String geboortedatum = ((Medewerker)user).getGeboorteDatum();
            user = jPanelFactory.createJPanel(user,"modify", BeheerMedewerkersController.class);
            List<Functie> functieList = functieJdbi.getAll();
            for (int i = 0; i < functieList.size(); i++){
                if (functieList.get(i).getFunctie().equals(((Medewerker) user).getFunctieId())){
                    ((Medewerker) user).setFunctieId(String.valueOf(i+1));
                }
            }
            medewerkerJdbi.update((Medewerker) user, geboortedatum, naam, voornaam);
            return user;
        }
        if(user.getClass()==Loper.class){
            String naam = ((Loper)user).getNaam();
            String voornaam = ((Loper)user).getVoornaam();
            String geboortedatum = ((Loper)user).getGeboorteDatum();
            user = jPanelFactory.createJPanel(user,"modify", BeheerLopersController.class);
            loperJdbi.update((Loper) user, geboortedatum, naam, voornaam);
            return user;
        }
        return null;
    }

    public void deleteAccount(Object user){
        int option2 = JOptionPane.showConfirmDialog(null, "Are u sure u want to delete your account?", "Register", JOptionPane.OK_CANCEL_OPTION);
        if (option2 == JOptionPane.OK_OPTION) {
            if(user.getClass()==Medewerker.class){
                medewerkerJdbi.delete((Medewerker) user);
            }
            if(user.getClass()==Loper.class){
                loperJdbi.delete((Loper) user);
            }
            showAlert("Succes", "Account succesfully deleted!");
            System.exit(0);
        }

    }

    public void logOut() {
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
        ProjectMain projectMain = new ProjectMain();
        try{
            projectMain.start(projectMain.getRootStage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
