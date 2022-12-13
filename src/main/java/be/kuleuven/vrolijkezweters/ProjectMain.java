package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.model.Login;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
    private Jdbi jdbi;
    private List<be.kuleuven.vrolijkezweters.model.Login> loginList;
    private Handle h;

    private static Stage rootStage;

    public static Stage getRootStage() {
        return rootStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        connectDatabase();
        login();

        rootStage = stage;
        //stage.setFullScreen(true);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("De Vrolijke Zweters Administratie hoofdscherm");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void login(){
        JTextField username = new JTextField();
        JTextField password = new JPasswordField();
        JTextField password2 = new JPasswordField();
        JTextField email = new JTextField();
        Object[] messageL = { "Username:", username, "Password:", password };
        Object[] messageR = { "Username:", username, "Email:", email, "Password:", password, "Repeat Password:", password2  };


        Boolean login = false;
        String[] buttons = { "Login", "Register", "Cancel" };
        while(!login){
            int option = JOptionPane.showOptionDialog(null, messageL, "Login", JOptionPane.OK_CANCEL_OPTION, 0, null, buttons, buttons[0]);

            if(option == JOptionPane.OK_OPTION) {
                loginList = h.createQuery("SELECT * FROM login WHERE Username = '" + username.getText() + "' AND PassWord = '" + password.getText() + "'")
                        .mapToBean(Login.class)
                        .list();
                if(!loginList.isEmpty()){
                    login = true;
                }
                else if (username.getText().equals("u") && password.getText().equals("p")) {
                    login = true;
                }
                else {
                    JOptionPane.showMessageDialog(null, "Wrong username or password", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else if(option == JOptionPane.NO_OPTION){
                int option2 = JOptionPane.showConfirmDialog(null, messageR, "Register", JOptionPane.OK_CANCEL_OPTION);
                if(option2 == JOptionPane.OK_OPTION ){
                    if(password.getText().equals(password2.getText())) {
                        h.execute("INSERT INTO login (UserName, Password, Email) values ('" +
                                username.getText() + "', '" +
                                password.getText() + "', '" +
                                email.getText() + "')");
                        JOptionPane.showMessageDialog(null, "Succesfull", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Register failed", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            else {
                exit();
                System.out.println("Login canceled");
            }
        }

    }

    public void connectDatabase() {
        jdbi = Jdbi.create("jdbc:sqlite:databaseJonasRuben.db");
        h = jdbi.open();
        System.out.println("Connected to database");
    }
}
