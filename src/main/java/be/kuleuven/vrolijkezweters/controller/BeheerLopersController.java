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
import javafx.scene.layout.Region;
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
            modifyCurrentRow(selectedToLoper(tblConfigs.getSelectionModel().getSelectedItems())); //Modify selected row
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
        if (inputChecker.checkInput(inputLoper).isEmpty()) {
            loperJdbi.insert(inputLoper);
            tblConfigs.getItems().clear();
            getLoperList();
            initTable(loperList);
        } else {
            String fouten = inputChecker.checkInput(inputLoper).toString();
            showAlert("Input error", fouten + " voldoen niet aan de eisen");
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

    private void modifyCurrentRow(Loper selected) {
        Loper inputLoper = (Loper) jPanelFactory.createJPanel(selected, "modify", this.getClass());
        inputLoper.setWachtwoord(selected.getWachtwoord());
        if (inputChecker.checkInput(inputLoper).isEmpty()) {
            loperJdbi.update(inputLoper, selected.getGeboorteDatum(), selected.getNaam(), selected.getVoornaam());
            tblConfigs.getItems().clear();
            getLoperList();
            initTable(loperList);
        } else {
            String fouten = inputChecker.checkInput(inputLoper).toString();
            showAlert("Input error", fouten + " Voldoet niet aan de criteria");
            modifyCurrentRow(selected);
        }

    }

    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void verifyOneRowSelected() {
        if (tblConfigs.getSelectionModel().getSelectedCells().size() == 0) {
            showAlert("Hela!", "Eerst een record selecteren hee.");
        }
    }

    private Loper selectedToLoper(List<Object> selectedItems){
        List<String> items = Arrays.asList(selectedItems.get(0).toString().split("\\s*,\\s*")); //only the first selected item is modified
        Loper l = loperJdbi.selectByVoornaamNaamGeboortedatum(items.get(1), items.get(2), items.get(0).substring(1));
        return l;
    }
}
