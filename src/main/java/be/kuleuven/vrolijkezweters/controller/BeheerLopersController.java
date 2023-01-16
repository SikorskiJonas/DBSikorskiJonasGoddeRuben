package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.InputChecker;
import be.kuleuven.vrolijkezweters.JPanelFactory;
import be.kuleuven.vrolijkezweters.jdbi.LoperJdbi;
import be.kuleuven.vrolijkezweters.model.Loper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class BeheerLopersController {
    final InputChecker inputChecker = new InputChecker();
    final JPanelFactory jPanelFactory = new JPanelFactory();
    final LoperJdbi loperJdbi = new LoperJdbi(ProjectMainController.connectionManager);
    List<Loper> loperList;
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

    /**
     * Runs when opening the Lopers controller.
     */
    public void initialize() {
        getLoperList();
        initTable(loperList);   //Load list of lopers into table
        btnAdd.setOnAction(e -> addNewRow());
        btnModify.setOnAction(e -> {
            verifyOneRowSelected();
            modifyCurrentRow(tblConfigs.getSelectionModel().getSelectedItems()); //Modify selected row
        });
        btnDelete.setOnAction(e -> {
            verifyOneRowSelected();
            deleteCurrentRow(tblConfigs.getSelectionModel().getSelectedItems());//Delete selected row
        });
        btnClose.setOnAction(e -> {
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
    }

    private void initTable(List<Loper> loperList) {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblConfigs.getColumns().clear();
        int colIndex = 0;
        for (var colName : new String[]{"GeboorteDatum", "VoorNaam", "Naam", "Sex", "Lengte", "telefoonNummer", "E-mail", "Gemeente", "Straat + nr"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }
        for (Loper loper : loperList) {
            tblConfigs.getItems().add(FXCollections.observableArrayList(loper.getGeboorteDatum(), loper.getVoornaam(), loper.getNaam(), loper.getSex(), loper.getLengte(), loper.getTelefoonNummer(), loper.getEmail(), loper.getGemeente(), loper.getStraatEnNr()));
        }
    }

    public void getLoperList() {
        System.out.println("fetching list of lopers");
        loperList = loperJdbi.getAll();
    }

    private void addNewRow() {
        Loper inputLoper = (Loper) jPanelFactory.createJPanel(null, "add", this.getClass());
        if (inputChecker.checkInput(inputLoper)) {
            loperJdbi.insert(inputLoper);
            tblConfigs.getItems().clear();
            getLoperList();
            initTable(loperList);
        } else {
            showAlert("Input error", "De ingegeven data voldoet niet aan de constraints");
        }

    }

    private void deleteCurrentRow(List<Object> selectedItems) {
        for (Object selectedItem : selectedItems) {
            List<String> items = Arrays.asList(selectedItem.toString().split("\\s*,\\s*"));
            String geboortedatumI = items.get(0).substring(1);
            String naamI = items.get(2);
            String voornaamI = items.get(1);
            loperJdbi.delete(loperJdbi.selectByVoornaamNaamGeboortedatum(voornaamI, naamI, geboortedatumI));
            tblConfigs.getItems().clear();
            getLoperList();
            initTable(loperList);
        }
    }

    private void modifyCurrentRow(List<Object> selectedItems) {
        List<String> items = Arrays.asList(selectedItems.get(0).toString().split("\\s*,\\s*")); //only the first selected item is modified
        String geboortedatum = items.get(0).substring(1);
        String naam = items.get(2);
        String voornaam = items.get(1);
        Loper selected = loperJdbi.selectByVoornaamNaamGeboortedatum(voornaam, naam, geboortedatum);
        Loper inputLoper = (Loper) jPanelFactory.createJPanel(selected, "modify", this.getClass());
        inputLoper.setWachtwoord(selected.getWachtwoord());
        if (inputChecker.checkInput(inputLoper)) {
            loperJdbi.update(inputLoper, geboortedatum, naam, voornaam);
            tblConfigs.getItems().clear();
            getLoperList();
            initTable(loperList);
        } else {
            showAlert("Input error", "De ingegeven data voldoet niet aan de constraints");
        }

    }

    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void verifyOneRowSelected() {
        if (tblConfigs.getSelectionModel().getSelectedCells().size() == 0) {
            showAlert("Hela!", "Eerst een record selecteren hee.");
        }
    }
}
