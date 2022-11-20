package be.kuleuven.vrolijkezweters.controller;

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
    public Connection connection;
    private Jdbi jdbi;

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
        List<Wedstrijd> wedstrijdList = getWedstrijdList();
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

        for(var colName : new String[]{"Naam", "Datum", "Plaats", "Prijs", "Categorie"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }
<<<<<<< HEAD
        for(int i = 0; i < wedstrijdList.size(); i++) {
            tblConfigs.getItems().add(FXCollections.observableArrayList(wedstrijdList.get(i).getNaam(), wedstrijdList.get(i).getDatum(),wedstrijdList.get(i).getPlaats(), wedstrijdList.get(i).getInschrijvingsgeld(), wedstrijdList.get(i).getCategorie()));
        }
=======
<<<<<<< HEAD

        //data van wedstrijd
        for(int i = 0; i < 10; i++) {

            tblConfigs.getItems().add(FXCollections.observableArrayList("g", "Kleine wedstrijd " + i, "categorie 1", i*10 + "", i * 33 + "", "bb"));
        }
=======
>>>>>>> ebce92b5acd78d9d14ae86d8bb6ceb1eaf03dc18
>>>>>>> adc4e1d28cdd463e1fd238090ffaa56452908117
    }
    public List<Wedstrijd> getWedstrijdList(){
        System.out.println("fetching list of wedstrijden");

        return jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT * FROM wedstrijd WHERE Id = :Id")
                    .bind("Id", 1)
                    .mapToBean(Wedstrijd.class)
                    .list();
        });
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
