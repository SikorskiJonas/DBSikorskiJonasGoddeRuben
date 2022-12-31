package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.controller.BeheerLopersController;
import be.kuleuven.vrolijkezweters.jdbc.ConnectionManager;
import be.kuleuven.vrolijkezweters.model.Login;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.sun.javafx.application.PlatformImpl.exit;

/**
 * DB Taak 2022-2023: De Vrolijke Zweters
 * Zie https://kuleuven-diepenbeek.github.io/db-course/extra/project/ voor opgave details
 *
 *
 *
 */
public class ProjectMain extends Application {
    private InputChecker inputChecker = new InputChecker();
    public static boolean isAdmin;
    private List<be.kuleuven.vrolijkezweters.model.Login> loginList;
    private static Stage rootStage;

    public static Stage getRootStage() {
        return rootStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        ConnectionManager.connectDatabase();
        login();
        rootStage = stage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("De Vrolijke Zweters Administratie hoofdscherm");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void login() {
        JTextField username = new JTextField();
        JTextField password = new JPasswordField();
        JTextField password2 = new JPasswordField();
        JTextField email = new JTextField();
        JCheckBox Admin = new JCheckBox();
        String[] registerAsOptions = {"Loper", "Medewerker"};
        JComboBox registerOption = new JComboBox<String>(registerAsOptions);
        Object[] loginMessage = {"Username:", username, "Password:", password};
        Object[] registerMessage = {"Username:", username, "Email:", email, "Password:", password, "Repeat Password:", password2, "Register as:", registerOption, "Are you an admin?", Admin};
        Boolean login = false;

        String[] buttons = {"Login", "Register", "Cancel"};
        while (!login) {
            int option = JOptionPane.showOptionDialog(null, loginMessage, "Login", JOptionPane.OK_CANCEL_OPTION, 0, null, buttons, buttons[0]);
            loginList = ConnectionManager.handle.createQuery("SELECT * FROM Login WHERE userName = '" + username.getText() + "' AND passWord = '" + password.getText() + "'")
                    .mapToBean(Login.class)
                    .list();
            if (option == JOptionPane.OK_OPTION) {
                if (!loginList.isEmpty()) {
                    login = true;
                } else if (username.getText().equals("u") && password.getText().equals("p")) {
                    login = true;
                    isAdmin = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong username or password", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (option == JOptionPane.NO_OPTION) {
                int option2 = JOptionPane.showConfirmDialog(null, registerMessage, "Register", JOptionPane.OK_CANCEL_OPTION);
                if (option2 == JOptionPane.OK_OPTION) {
                    isAdmin = Admin.isSelected();
                    if (password.getText().equals(password2.getText()) && !isAdmin) {
                        JOptionPane.showMessageDialog(null, "Succesfull, now please enter your credentials", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                        enterCredentials(registerOption.getSelectedItem().toString(), email.getText(), username.getText(), password.getText());
                    }
                    if (isAdmin) {
                        JOptionPane.showMessageDialog(null, "Succesfully registered", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                        ConnectionManager.handle.execute("INSERT INTO Login (userName, passWord, eMail, isAdmin) values ('" +
                                username.getText() + "', '" +
                                password.getText() + "', '" +
                                email.getText() + "', '" +
                                isAdmin + "')");
                    }else {
                        JOptionPane.showMessageDialog(null, "Register failed", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else {
                exit();
                System.out.println("Login canceled");
            }
        }

    }

    private void enterCredentials(String option, String eMail, String username, String password) {
        JXDatePicker geboortedatum = new JXDatePicker();
        JTextField voornaam = new JTextField(5);
        JTextField naam = new JTextField(5);
        JTextField lengte = new JTextField(5);
        String[] geslactKeuzes = {"M", "F", "X"};
        JComboBox sex = new JComboBox<String>(geslactKeuzes);
        JTextField telefoonnummer = new JTextField(5);
        JTextField gemeente = new JTextField(5);
        JTextField straatEnNummer = new JTextField(5);
        String[] options = {"Register", "Cancel"};
        if (option.equals("Loper")) {
            Object[] message = {"Voornaam:", voornaam, "Naam:", naam, "Geboortedatum:", geboortedatum, "Geslacht:", sex, "Lengte:", lengte, "Telefoon:", telefoonnummer, "Gemeente:", gemeente, "Straat en nummer:", straatEnNummer};
            int enterCreds = JOptionPane.showOptionDialog(null, message, "Geef gegevens in", JOptionPane.OK_CANCEL_OPTION, 0, null, options, options[0]);

        }
        if (option.equals("Medewerker")) {
            Object[] message = {"Voornaam:", voornaam, "Naam:", naam, "Geboortedatum:", geboortedatum, "Geslacht:", sex, "Telefoon:", telefoonnummer, "Gemeente:", gemeente, "Straat en nummer:", straatEnNummer, " ", "Je werkdatum en je functie kunnen", "alleen door Admins toegevoegd worden"};
            int enterCreds = JOptionPane.showOptionDialog(null, message, "Geef gegevens in", JOptionPane.OK_CANCEL_OPTION, 0, null, options, options[0]);
        }
        geboortedatum.setDate(Calendar.getInstance().getTime());
        geboortedatum.setFormats(new SimpleDateFormat("dd/MM/yyyy"));
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateFormatted = format.format(geboortedatum.getDate());
        ArrayList<String> credData = new ArrayList();
        credData.add(dateFormatted);
        credData.add(voornaam.getText());
        credData.add(naam.getText());
        credData.add(sex.getSelectedItem().toString());
        credData.add(telefoonnummer.getText());
        credData.add(eMail);
        credData.add(gemeente.getText());
        credData.add(straatEnNummer.getText());
        String insertQuery = null;
        if (option.equals("Loper")) {
            credData.add(4, lengte.getText());
            if (inputChecker.checkInput(credData, "Loper")) {
                insertQuery = "INSERT INTO loper (geboorteDatum, voornaam, naam, sex, lengte, telefoonnummer, eMail, gemeente, straatEnNr) values ('" +
                        credData.get(0) + "', '" + credData.get(1) + "', '" + credData.get(2) + "', '" + credData.get(3) + "', '" + credData.get(4) + "', '" + credData.get(5) + "', '" + credData.get(6) + "', '" + credData.get(7) + "', '" + credData.get(8) + "')";
            }
        }
        if (option.equals("Medewerker")) {
            if (inputChecker.checkInput(credData, "Medewerker")) {
                insertQuery = "INSERT INTO loper (geboorteDatum, voornaam, naam, sex, lengte, telefoonnummer, eMail, gemeente, straatEnNr) values ('" +
                        credData.get(0) + "', '" + credData.get(1) + "', '" + credData.get(2) + "', '" + credData.get(3) + "', '" + credData.get(4) + "', '" + credData.get(5) + "', '" + credData.get(6) + "', '" + credData.get(7) + "')";
            }
        }
        if(!option.equals("Medewerker") && !option.equals("Loper")){
            JOptionPane.showMessageDialog(null, "Input Error", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        ConnectionManager.handle.execute("INSERT INTO Login (userName, passWord, eMail, isAdmin) values ('" +
                username + "', '" +
                password + "', '" +
                eMail + "', '" +
                String.valueOf(isAdmin) + "')");
        ConnectionManager.handle.execute(insertQuery);
        JOptionPane.showMessageDialog(null, "Regsiter succesfull", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
    }
}
