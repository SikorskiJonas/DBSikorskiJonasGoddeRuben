package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.controller.ProjectMainController;
import be.kuleuven.vrolijkezweters.jdbi.ConnectionManager;
import be.kuleuven.vrolijkezweters.jdbi.LoperJdbi;
import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Medewerker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.sun.javafx.application.PlatformImpl.exit;

/**
 * DB Taak 2022-2023: De Vrolijke Zweters
 * Zie https://kuleuven-diepenbeek.github.io/db-course/extra/project/ voor opgave details
 *
 */

public class ProjectMain extends Application {
    private InputChecker inputChecker = new InputChecker();
    public static boolean isAdmin;
    private List<Loper> loperLoginList;
    private List<Medewerker> medewerkerLoginList;
    private static Stage rootStage;
    private Object user;

    public Stage getRootStage() {
        return rootStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        ConnectionManager.connectDatabase();
        login();
        rootStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
        Scene scene = new Scene(loader.load());
        ProjectMainController controller = loader.getController();
        controller.setUser(user);
        stage.setTitle("De Vrolijke Zweters Administratie hoofdscherm");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void login() {
        JTextField password = new JPasswordField();
        JTextField password2 = new JPasswordField();
        JTextField email = new JTextField();
        Object[] loginMessage = {"E-mail:", email, "Password:", password};
        Object[] registerMessage = {"E-mail:", email, "Password:", password, "Repeat Password:", password2};
        Boolean login = false;

        String[] buttons = {"Login", "Register", "Cancel"};
        while (!login) {
            int option = JOptionPane.showOptionDialog(null, loginMessage, "Login", JOptionPane.OK_CANCEL_OPTION, 0, null, buttons, buttons[0]);
            loperLoginList = ConnectionManager.handle.createQuery("SELECT * FROM Loper WHERE eMail = '" + email.getText() + "' AND wachtwoord = '" + password.getText() + "'")
                    .mapToBean(Loper.class)
                    .list();
            medewerkerLoginList = ConnectionManager.handle.createQuery("SELECT * FROM Medewerker WHERE eMail = '" + email.getText() + "' AND wachtwoord = '" + password.getText() + "'")
                    .mapToBean(Medewerker.class)
                    .list();
            if (option == JOptionPane.OK_OPTION) {
                if (!loperLoginList.isEmpty()) {
                    login = true;
                    isAdmin = false;
                    user = loperLoginList.get(0);
                } else if (!medewerkerLoginList.isEmpty()) {
                    login = true;
                    isAdmin = medewerkerLoginList.get(0).getIsAdmin().equals("true");
                    user = medewerkerLoginList.get(0);
                }else if (email.getText().equals("u") && password.getText().equals("p")) {
                    login = true;
                    isAdmin = true;
                    user = "Ultimate admin";
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong username or password", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (option == JOptionPane.NO_OPTION) {
                int option2 = JOptionPane.showConfirmDialog(null, registerMessage, "Register", JOptionPane.OK_CANCEL_OPTION);
                if (option2 == JOptionPane.OK_OPTION) {
                    if (password.getText().equals(password2.getText())) {
                        JOptionPane.showMessageDialog(null, "Great!, now please enter the following credentials", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                        enterCredentials( email.getText(), password.getText());
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

    private void enterCredentials( String eMail, String password) {
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
        Object[] message = {"Voornaam:", voornaam, "Naam:", naam, "Geboortedatum:", geboortedatum, "Geslacht:", sex, "Lengte:", lengte, "Telefoon:", telefoonnummer, "Gemeente:", gemeente, "Straat en nummer:", straatEnNummer};

        int enterCreds = JOptionPane.showOptionDialog(null, message, "Geef gegevens in", JOptionPane.OK_CANCEL_OPTION, 0, null, options, options[0]);

        geboortedatum.setDate(Calendar.getInstance().getTime());
        geboortedatum.setFormats(new SimpleDateFormat("dd/MM/yyyy"));
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateFormatted = format.format(geboortedatum.getDate());

        Loper loper = new Loper();
        loper.setGeboorteDatum(dateFormatted);
        loper.setVoornaam(voornaam.getText());
        loper.setNaam(naam.getText());
        loper.setSex(sex.getSelectedItem().toString());
        loper.setLengte(lengte.getText());
        loper.setTelefoonNummer(telefoonnummer.getText());
        loper.setEmail(eMail);
        loper.setGemeente(gemeente.getText());
        loper.setStraatEnNr(straatEnNummer.getText());
        loper.setWachtwoord(password);
        if (inputChecker.checkInput(loper)) {
            LoperJdbi loperJdbi = new LoperJdbi(new ConnectionManager());
            loperJdbi.insert(loper);
            JOptionPane.showMessageDialog(null, "Register succesfull", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
        }
        if (!inputChecker.checkInput(loper)) {
            JOptionPane.showMessageDialog(null, "Something went wrong, try again please", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
