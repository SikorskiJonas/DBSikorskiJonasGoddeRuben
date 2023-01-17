package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.controller.ProjectMainController;
import be.kuleuven.vrolijkezweters.jdbi.ConnectionManager;
import be.kuleuven.vrolijkezweters.jdbi.JdbiManager;
import be.kuleuven.vrolijkezweters.jdbi.LoperDao;
import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Medewerker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static com.sun.javafx.application.PlatformImpl.exit;

/**
 * DB Taak 2022-2023: De Vrolijke Zweters
 * Zie https://kuleuven-diepenbeek.github.io/db-course/extra/project/ voor opgave details
 */

public class ProjectMain extends Application {
    public static boolean isAdmin;
    private static Stage rootStage;
    private final InputChecker inputChecker = new InputChecker();
    private Object user;

    public static void main(String[] args) {
        launch();
    }

    public Stage getRootStage() {
        return rootStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        ConnectionManager.connectDatabase();
        JdbiManager.init("jdbc:sqlite:databaseJonasRuben.db");
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

    public void login() {
        JTextField password = new JPasswordField();
        JTextField password2 = new JPasswordField();
        JTextField email = new JTextField();
        Object[] loginMessage = {"E-mail:", email, "Password:", password};
        Object[] registerMessage = {"E-mail:", email, "Password:", password, "Repeat Password:", password2};
        boolean login = false;

        String[] buttons = {"Login", "Register", "Cancel"};
        while (!login) {
            int option = JOptionPane.showOptionDialog(null, loginMessage, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);

            ////////////////////////
            ///////////////////////
            /////////////////////////
            //TODO verplaats in jdbi klasse
            /////////////////////////
            /////////////////////////
            /////////////////////////
            Jdbi jdbi = JdbiManager.getJdbi();
            List<Loper> loperLoginList = jdbi.withHandle(handle -> {
                return handle.createQuery("SELECT * FROM Loper WHERE eMail = :eMail AND wachtwoord = :password")
                        .bind("eMail", email.getText())
                        .bind("password", password.getText())
                        .mapToBean(Loper.class)
                        .list();
            });
            List<Medewerker> medewerkerLoginList = jdbi.withHandle(handle -> {
                return handle.createQuery("SELECT * FROM Medewerker WHERE eMail = :eMail AND wachtwoord = :password")
                        .bind("eMail", email.getText())
                        .bind("password", password.getText())
                        .mapToBean(Medewerker.class)
                        .list();
            });


            //List<Loper> loperLoginList = ConnectionManager.handle.createQuery("SELECT * FROM Loper WHERE eMail = '" + email.getText() + "' AND wachtwoord = '" + password.getText() + "'").mapToBean(Loper.class).list();
            //List<Medewerker> medewerkerLoginList = ConnectionManager.handle.createQuery("SELECT * FROM Medewerker WHERE eMail = '" + email.getText() + "' AND wachtwoord = '" + password.getText() + "'").mapToBean(Medewerker.class).list();
            if (option == JOptionPane.OK_OPTION) {
                if (!loperLoginList.isEmpty()) {
                    login = true;
                    isAdmin = false;
                    user = loperLoginList.get(0);
                } else if (!medewerkerLoginList.isEmpty()) {
                    login = true;
                    isAdmin = medewerkerLoginList.get(0).getIsAdmin().equals("true");
                    user = medewerkerLoginList.get(0);
                } else if (email.getText().equals("u") && password.getText().equals("p")) {
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
                        enterCredentials(email.getText(), password.getText());
                    } else {
                        JOptionPane.showMessageDialog(null, "Register failed", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else {
                exit();
                System.out.println("Login canceled");
            }
        }

    }

    private void enterCredentials(String eMail, String password) {
        JXDatePicker geboortedatum = new JXDatePicker();
        JTextField voornaam = new JTextField(5);
        JTextField naam = new JTextField(5);
        JTextField lengte = new JTextField(5);
        String[] geslactKeuzes = {"M", "F", "X"};
        JComboBox<String> sex = new JComboBox<>(geslactKeuzes);
        JTextField telefoonnummer = new JTextField(5);
        JTextField gemeente = new JTextField(5);
        JTextField straatEnNummer = new JTextField(5);

        String[] options = {"Register", "Cancel"};
        Object[] message = {"Voornaam:", voornaam, "Naam:", naam, "Geboortedatum:", geboortedatum, "Geslacht:", sex, "Lengte:", lengte, "Telefoon:", telefoonnummer, "Gemeente:", gemeente, "Straat en nummer:", straatEnNummer};

        int enterCreds = JOptionPane.showOptionDialog(null, message, "Geef gegevens in", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);

        geboortedatum.setDate(Calendar.getInstance().getTime());
        geboortedatum.setFormats(new SimpleDateFormat("dd/MM/yyyy"));
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateFormatted = format.format(geboortedatum.getDate());

        Loper loper = new Loper();
        loper.setGeboortedatum(dateFormatted);
        loper.setVoornaam(voornaam.getText());
        loper.setNaam(naam.getText());
        loper.setSex(Objects.requireNonNull(sex.getSelectedItem()).toString());
        loper.setLengte(lengte.getText());
        loper.setTelefoonnummer(telefoonnummer.getText());
        loper.seteMail(eMail);
        loper.setGemeente(gemeente.getText());
        loper.setStraatEnNr(straatEnNummer.getText());
        loper.setWachtwoord(password);
        if (inputChecker.checkInput(loper).isEmpty()) {
            LoperDao loperDao = new LoperDao();
            loperDao.insert(loper);
            JOptionPane.showMessageDialog(null, "Register succesfull", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
        }
        if (!inputChecker.checkInput(loper).isEmpty()) {
            String fouten = inputChecker.checkInput(loper).toString();
            JOptionPane.showMessageDialog(null, "wrong input for: " + fouten, "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
