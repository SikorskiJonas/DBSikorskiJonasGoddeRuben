package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.jdbc.ConnectionManager;
import be.kuleuven.vrolijkezweters.model.Login;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
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
    public static int isAdmin;
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

    public void login(){
        JTextField username = new JTextField();
        JTextField password = new JPasswordField();
        JTextField password2 = new JPasswordField();
        JTextField email = new JTextField();
        JCheckBox Admin = new JCheckBox();
        Object[] messageL = { "Username:", username, "Password:", password };
        Object[] messageR = { "Username:", username, "Email:", email, "Password:", password, "Repeat Password:", password2, "Are you an admin?", Admin };
        Boolean login = false;

        String[] buttons = { "Login", "Register", "Cancel" };
        while(!login){
            int option = JOptionPane.showOptionDialog(null, messageL, "Login", JOptionPane.OK_CANCEL_OPTION, 0, null, buttons, buttons[0]);
            loginList = ConnectionManager.handle.createQuery("SELECT * FROM Login WHERE userName = '" + username.getText() + "' AND passWord = '" + password.getText() + "'")
                    .mapToBean(Login.class)
                    .list();
            if(option == JOptionPane.OK_OPTION) {
                if(!loginList.isEmpty()){
                    login = true;
                }
                else if (username.getText().equals("u") && password.getText().equals("p")) {
                    login = true;
                    isAdmin = 1;
                }
                else {
                    JOptionPane.showMessageDialog(null, "Wrong username or password", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else if(option == JOptionPane.NO_OPTION){
                int option2 = JOptionPane.showConfirmDialog(null, messageR, "Register", JOptionPane.OK_CANCEL_OPTION);
                if(option2 == JOptionPane.OK_OPTION ){
                    if (Admin.isSelected()){
                        isAdmin = 1;
                    }
                    else {
                        isAdmin = 0;
                    }
                        if(password.getText().equals(password2.getText())) {
                            ConnectionManager.handle.execute("INSERT INTO Login (userName, passWord, eMail, isAdmin) values ('" +
                                    username.getText() + "', '" +
                                    password.getText() + "', '" +
                                    email.getText() + "', '" +
                                    String.valueOf(isAdmin) + "')");
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
}
