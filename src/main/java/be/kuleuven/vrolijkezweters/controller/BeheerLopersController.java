package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;

import java.sql.SQLException;
import java.util.List;

public class BeheerLopersController {
    private Jdbi jdbi;
    private List<Loper> loperList;

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
        loperList = getLoperList();
        initTable(loperList);
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

    private void initTable(List<Loper> loperList) {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblConfigs.getColumns().clear();

        int colIndex = 0;

        for(var colName : new String[]{"GeboorteDatum", "VoorNaam", "Naam", "Sex", "Lengte", "telefoonNummer", "Email", "Gemeente", "Straat + nr"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }

        for(int i = 0; i < loperList.size(); i++) {
            tblConfigs.getItems().add(FXCollections.observableArrayList(loperList.get(i).getGeboorteDatum(), loperList.get(i).getVoornaam(), loperList.get(i).getNaam(), loperList.get(i).getSex(), loperList.get(i).getLengte(), loperList.get(i).getTelefoonNummer(), loperList.get(i).getEmail(), loperList.get(i).getGemeente(), loperList.get(i).getStraatplusnr()));
        }
    }
    public List<Loper> getLoperList(){
        System.out.println("fetching list of lopers");

        return jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT * FROM loper")
                    .mapToBean(Loper.class)
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
