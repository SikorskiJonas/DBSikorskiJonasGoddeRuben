package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.jdbc.ConnectionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ProjectMainController {

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

    public void initialize() {
        if (!ProjectMain.isAdmin){
            btnBeheerLopers.setVisible(false);
            btnBeheerMedewerkers.setVisible(false);
            btnConfigAttaches.setVisible(false);
            btnImport.setVisible(false);
        }
        btnWedstrijden.setOnAction(e -> showBeheerScherm("wedstrijden"));
        btnBeheerLopers.setOnAction(e -> showBeheerScherm("lopers"));
        btnBeheerMedewerkers.setOnAction(e -> showBeheerScherm("medewerkers"));
        btnConfigAttaches.setOnAction(e -> showBeheerScherm("attaches"));
        btnKlassement.setOnAction(e -> showBeheerScherm("klassement"));
        btnImport.setOnAction(e -> showBeheerScherm("import"));

    }

    private void showBeheerScherm(String id) {
        var resourceName = "beheer" + id + ".fxml";
        try {
            var stage = new Stage();
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource(resourceName));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Admin " + id);
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();


        } catch (Exception e) {
            throw new RuntimeException("Kan beheerscherm " + resourceName + " niet vinden", e);
        }
    }
}
