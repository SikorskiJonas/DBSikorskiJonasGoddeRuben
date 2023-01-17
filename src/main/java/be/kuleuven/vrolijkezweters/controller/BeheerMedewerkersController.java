package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.InputChecker;
import be.kuleuven.vrolijkezweters.JPanelFactory;
import be.kuleuven.vrolijkezweters.jdbi.FunctieDao;
import be.kuleuven.vrolijkezweters.jdbi.MedewerkerDao;
import be.kuleuven.vrolijkezweters.model.Functie;
import be.kuleuven.vrolijkezweters.model.Medewerker;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class BeheerMedewerkersController {
    final InputChecker inputChecker = new InputChecker();
    final JPanelFactory jPanelFactory = new JPanelFactory();
    final MedewerkerDao medewerkerDao = new MedewerkerDao();
    final FunctieDao functieDao = new FunctieDao();
    private List<Medewerker> medewerkerList;
    private List<Functie> functieList;
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

    public void initialize() {
        getMedewerkerList();
        initTable(medewerkerList);
        btnAdd.setOnAction(e -> addNewRow());
        btnModify.setOnAction(e -> {
            verifyOneRowSelected();
            modifyCurrentRow(tblConfigs.getSelectionModel().getSelectedItems());
        });
        btnDelete.setOnAction(e -> {
            verifyOneRowSelected();
            deleteCurrentRow(tblConfigs.getSelectionModel().getSelectedItems());
        });
        btnClose.setOnAction(e -> {
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
    }

    private void initTable(List<Medewerker> medewerkerList) {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblConfigs.getColumns().clear();
        int colIndex = 0;
        for (var colName : new String[]{"GeboorteDatum", "VoorNaam", "Naam", "Sex", "Datum tewerkstelling", "Functie", "Telefoonnummer", "E-mail", "Gemeente", "Straat + nr", "Admin"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }
        for (Medewerker medewerker : medewerkerList) {
            tblConfigs.getItems().add(FXCollections.observableArrayList(medewerker.getGeboorteDatum(), medewerker.getVoornaam(), medewerker.getNaam(), medewerker.getSex(), medewerker.getDatumTewerkstelling(), String.valueOf(medewerker.getFunctieId()), medewerker.getTelefoonNummer(), medewerker.getEmail(), medewerker.getGemeente(), medewerker.getStraatEnNr(), medewerker.getIsAdmin()));
        }
    }

    public void getMedewerkerList() {
        System.out.println("fetching list of medewerkers");
        medewerkerList = medewerkerDao.getAll();
        //fetch list of functies
        functieList = functieDao.getAll();
        //convert functieID's to their functies
        for (Medewerker medewerker : medewerkerList) {
            String functieID = medewerker.getFunctieId();
            int functieIDInt = Integer.parseInt(functieID);
            String functie = functieList.get(functieIDInt - 1).getFunctie();
            medewerker.setFunctieId(functie);
        }
    }

    private void addNewRow() {
        Medewerker inputMedewerker = (Medewerker) jPanelFactory.createJPanel(null, "add", this.getClass());
        for (int i = 0; i < functieList.size(); i++) {
            if (functieList.get(i).getFunctie().equals(inputMedewerker.getFunctieId())) {
                inputMedewerker.setFunctieId(String.valueOf(i + 1));
            }
        }
        if (inputChecker.checkInput(inputMedewerker).isEmpty()) {
            medewerkerDao.insert(inputMedewerker);
            tblConfigs.getItems().clear();
            getMedewerkerList();
            initTable(medewerkerList);
        } else {
            String fouten = inputChecker.checkInput(inputMedewerker).toString();
            showAlert("Input error", fouten + "Voldoet niet aan de criteria");
        }
    }

    private void deleteCurrentRow(List<Object> selectedItems) {
        for (Object selectedItem : selectedItems) {
            List<String> items = Arrays.asList(selectedItem.toString().split("\\s*,\\s*"));
            String geboortedatumI = items.get(0).substring(1);
            String naamI = items.get(2);
            String voornaamI = items.get(1);
            medewerkerDao.delete(medewerkerDao.selectByVoornaamNaamGeboortedatum(voornaamI, naamI, geboortedatumI));
            tblConfigs.getItems().clear();
            tblConfigs.getItems().clear();
            getMedewerkerList();
            initTable(medewerkerList);
        }
    }

    private void modifyCurrentRow(List<Object> selectedItems) {
        List<String> items = Arrays.asList(selectedItems.get(0).toString().split("\\s*,\\s*")); //only the first selected item is modified
        String geboortedatum = items.get(0).substring(1);
        String naam = items.get(2);
        String voornaam = items.get(1);
        Medewerker selected = medewerkerDao.selectByVoornaamNaamGeboortedatum(voornaam, naam, geboortedatum);
        Medewerker inputMedewerker = (Medewerker) jPanelFactory.createJPanel(selected, "modify", this.getClass());
        inputMedewerker.setWachtwoord(selected.getWachtwoord());
        for (int i = 0; i < functieList.size(); i++) {
            if (functieList.get(i).getFunctie().equals(inputMedewerker.getFunctieId())) {
                inputMedewerker.setFunctieId(String.valueOf(i + 1));
            }
        }
        if (inputChecker.checkInput(inputMedewerker).isEmpty()) {
            medewerkerDao.update(inputMedewerker, geboortedatum, naam, voornaam);
            tblConfigs.getItems().clear();
            getMedewerkerList();
            initTable(medewerkerList);
        } else {
            String fouten = inputChecker.checkInput(inputMedewerker).toString();
            showAlert("Input error", fouten + "Voldoet niet aan de criteria");
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

}
