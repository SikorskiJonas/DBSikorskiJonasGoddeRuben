package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.model.Categorie;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class BeheerWedstrijdenController {
    private Jdbi jdbi;
    private List<Wedstrijd> wedstrijdList;

    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnModify;
    @FXML
    private Button btnClose;
    @FXML
    private TableView tblConfigs;

    public void initialize() throws SQLException {
        connectDatabase();
        getWedstrijdList();
        initTable(wedstrijdList);
        btnAdd.setOnAction(e -> addNewRow());
        btnModify.setOnAction(e -> {
            verifyOneRowSelected();
            modifyCurrentRow();
        });
        btnDelete.setOnAction(e -> {
            verifyOneRowSelected();
            deleteCurrentRow();
        });
        
        btnClose.setOnAction(e -> {
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
    }

    public void connectDatabase() throws SQLException {
        jdbi = Jdbi.create("jdbc:sqlite:databaseJonasRuben.db");
        //connection = DriverManager.getConnection("jdbc:sqlite:databaseJonasRuben.db");
        //var s = connection.createStatement();
        System.out.println("Connected to database");
    }

    private void initTable(List<Wedstrijd> wedstrijdList) {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblConfigs.getColumns().clear();

        int colIndex = 0;

        for(var colName : new String[]{"Naam", "Datum", "Plaats", "Prijs", "CategorieId"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }
<<<<<<< HEAD
=======

>>>>>>> f2899b8deee524b664ec2712e45f832b90b383af
        for(int i = 0; i < wedstrijdList.size(); i++) {
            tblConfigs.getItems().add(FXCollections.observableArrayList(wedstrijdList.get(i).getNaam(), wedstrijdList.get(i).getDatum(),wedstrijdList.get(i).getPlaats(), wedstrijdList.get(i).getInschrijvingsgeld(), wedstrijdList.get(i).getCategorieId()));
        }
    }
    private void getWedstrijdList(){
        System.out.println("fetching list of wedstrijden");
        wedstrijdList = jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM wedstrijd")
                .mapToBean(Wedstrijd.class)
                .list());
        List<Categorie> categorieList = jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM categorie")
                .mapToBean(Categorie.class)
                .list());
        for (int i = 0; i<wedstrijdList.size(); i++){
            String cId = wedstrijdList.get(i).getCategorieId();
            int cIdInt = Integer.parseInt(cId);
            wedstrijdList.get(i).setCategorieId(categorieList.get(cIdInt-1).getCategorie());
        }
    }

    private void addNewRow() {
    }

    private void deleteCurrentRow() {
    }

    private void modifyCurrentRow() {
    }

    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void verifyOneRowSelected() {
        if(tblConfigs.getSelectionModel().getSelectedCells().size() == 0) {
            showAlert("Hela!", "Eerst een record selecteren hé.");
        }
    }
}
