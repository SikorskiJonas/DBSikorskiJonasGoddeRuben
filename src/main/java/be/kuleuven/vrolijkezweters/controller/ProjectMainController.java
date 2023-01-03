package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.jdbc.ConnectionManager;
import be.kuleuven.vrolijkezweters.model.Loper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;


public class ProjectMainController {
    public String user;
    public String isLoper;

    @FXML
    private Button btnWedstrijden;
    @FXML
    private Button btnBeheerLopers;
    @FXML
    private Button btnBeheerMedewerkers;
    @FXML
    private Button btnConfigAttaches;
    @FXML
    private Button btnKlassement;
    @FXML
    private Button btnImport;
    @FXML
    private MenuButton btnAccount;
    //@FXML
    //private MenuItem btnAccountGeneral;
    @FXML
    private AnchorPane contentPane;
    @FXML
    private Text txtUser;

    public void initialize() throws IOException {
        if (!ProjectMain.isAdmin){
            btnBeheerLopers.setVisible(false);
            btnBeheerMedewerkers.setVisible(false);
            btnConfigAttaches.setVisible(false);
            btnImport.setVisible(false);
        }

        btnWedstrijden.setOnAction(e -> showBeheerScherm("wedstrijden", btnWedstrijden));
        btnBeheerLopers.setOnAction(e -> showBeheerScherm("lopers", btnBeheerLopers));
        btnBeheerMedewerkers.setOnAction(e -> showBeheerScherm("medewerkers", btnBeheerMedewerkers));
        btnConfigAttaches.setOnAction(e -> showBeheerScherm("attaches", btnConfigAttaches));
        btnKlassement.setOnAction(e -> showBeheerScherm("klassement", btnKlassement));
        btnImport.setOnAction(e -> showBeheerScherm("import", btnImport));
        btnAccount.getItems().get(0).setOnAction(e -> editAccount());
        btnAccount.getItems().get(1).setOnAction(e -> deleteAccount());
        btnAccount.getItems().get(2).setOnAction(e -> logOut());
    }

    private void showBeheerScherm(String id, Button button) {
        var resourceName = "beheer" + id + ".fxml";
        try {
            contentPane.getChildren().clear();
            AnchorPane content;
            setButtonColors(button);
            content = FXMLLoader.load(getClass().getClassLoader().getResource("beheer" + id + ".fxml"));
            contentPane.getChildren().add(content);

        } catch (Exception e) {
            throw new RuntimeException("Kan beheerscherm " + resourceName + " niet vinden", e);
        }
    }

    private void setButtonColors(Button button){
        btnWedstrijden.setStyle("-fx-background-color:  #37beb0");
        btnBeheerLopers.setStyle("-fx-background-color:  #37beb0");
        btnBeheerMedewerkers.setStyle("-fx-background-color:  #37beb0");
        btnConfigAttaches.setStyle("-fx-background-color:  #37beb0");
        btnKlassement.setStyle("-fx-background-color:  #37beb0");
        btnImport.setStyle("-fx-background-color:  #37beb0");
        button.setStyle("-fx-background-color:  #298F84");
    }

    public void setUser(String user, String isLoper){
        this.user = user;
        this.isLoper = isLoper;
        txtUser.setText("Logged in as " + isLoper + " " + user);
    }

    private void editAccount(){

    }

    private void deleteAccount(){

    }

    private void logOut(){

    }
}
