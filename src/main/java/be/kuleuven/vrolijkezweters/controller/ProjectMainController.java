package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.JPanelFactory;
import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.jdbc.ConnectionManager;
import be.kuleuven.vrolijkezweters.jdbc.LoperJdbi;
import be.kuleuven.vrolijkezweters.jdbc.MedewerkerJdbi;
import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Medewerker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;


public class ProjectMainController {
    public Object user;
    public static ConnectionManager connectionManager = new ConnectionManager();
    LoperJdbi loperJdbi = new LoperJdbi(ProjectMainController.connectionManager);
    MedewerkerJdbi medewerkerJdbi = new MedewerkerJdbi(ProjectMainController.connectionManager);

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

    public void setUser(Object user){
        this.user = user;
        if (user.getClass() == Loper.class){
            txtUser.setText("Logged in as [" + ((Loper) user).getEmail() + "].");
        }
        if (user.getClass() == Medewerker.class){
            txtUser.setText("Logged in as [" + ((Medewerker) user).getEmail() + "].");
        }
    }

    private void editAccount(){
        JPanelFactory jPanelFactory = new JPanelFactory();
        if(user.getClass()==Medewerker.class){
            user = jPanelFactory.createJPanel(user,"modify", BeheerMedewerkersController.class);

        }
        if(user.getClass()==Loper.class){
            user = jPanelFactory.createJPanel(user,"modify", BeheerLopersController.class);
        }
    }

    private void deleteAccount(){
        if(user.getClass()==Medewerker.class){
            medewerkerJdbi.delete((Medewerker) user);
        }
        if(user.getClass()==Loper.class){
            loperJdbi.delete((Loper) user);
        }
        showAlert("Succes", "Account succesfully deleted!");
        System.exit(0);
    }

    private void logOut(){
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
