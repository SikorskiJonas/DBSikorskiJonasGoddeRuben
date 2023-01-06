package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.InputChecker;
import be.kuleuven.vrolijkezweters.JPanelFactory;
import be.kuleuven.vrolijkezweters.jdbc.ConnectionManager;
import be.kuleuven.vrolijkezweters.model.Functie;
import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Medewerker;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jdbi.v3.core.result.ResultIterable;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class BeheerMedewerkersController {
    private List<Medewerker> medewerkerList;
    private List<Functie> functieList;
    InputChecker inputChecker = new InputChecker();
    JPanelFactory jPanelFactory = new JPanelFactory();

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

    public void initialize(){
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
        for(var colName : new String[]{"GeboorteDatum", "VoorNaam", "Naam", "Sex", "Datum tewerkstelling", "Functie", "Telefoonnummer", "E-mail", "Gemeente", "Straat + nr", "Admin"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }
        for (Medewerker medewerker : medewerkerList) {
            String f = String.valueOf(medewerker.getFunctieId());
            tblConfigs.getItems().add(FXCollections.observableArrayList(medewerker.getGeboorteDatum(), medewerker.getVoornaam(), medewerker.getNaam(), medewerker.getSex(), medewerker.getDatumTewerkstelling(), String.valueOf(medewerker.getFunctieId()), medewerker.getTelefoonNummer(), medewerker.getEmail(), medewerker.getGemeente(), medewerker.getStraatEnNr(), medewerker.getIsAdmin()));
        }
    }

    public void getMedewerkerList(){
        System.out.println("fetching list of medewerkers");
        medewerkerList = ConnectionManager.handle.createQuery("SELECT * FROM Medewerker")
                .mapToBean(Medewerker.class)
                .list();
        //fetch list of functies
        functieList = ConnectionManager.handle.createQuery("SELECT * FROM Functie")
                .mapToBean(Functie.class)
                .list();
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
        for (int i = 0; i < functieList.size(); i++){
            if (functieList.get(i).getFunctie().equals(inputMedewerker.getFunctieId())){
                inputMedewerker.setFunctieId(String.valueOf(i+1));
            }
        }
        if(inputChecker.checkInput(inputMedewerker)){
            ConnectionManager.handle.execute("INSERT INTO Medewerker (geboortedatum, voornaam, naam, sex, datumTewerkstelling, functieId, telefoonnummer, eMail, gemeente, straatEnNr, wachtwoord, isAdmin) VALUES ('" +
                            inputMedewerker.getGeboorteDatum() +"' , '" +
                            inputMedewerker.getVoornaam() +"' , '" +
                            inputMedewerker.getNaam() +"' , '" +
                            inputMedewerker.getSex() +"' , '" +
                            inputMedewerker.getDatumTewerkstelling() +"' , '" +
                            inputMedewerker.getFunctieId() +"' , '" +
                            inputMedewerker.getTelefoonNummer() +"' , '" +
                            inputMedewerker.getEmail() +"' , '" +
                            inputMedewerker.getGemeente() +"' , '" +
                            inputMedewerker.getStraatEnNr() +"' , '" +
                            inputMedewerker.getWachtwoord() +"' , '" +
                            inputMedewerker.getIsAdmin() +" ') " );
            tblConfigs.getItems().clear();
            getMedewerkerList();
            initTable(medewerkerList);
        }
        else{
            showAlert("Input error", "De ingegeven data voldoet niet aan de constraints");
        }

    }

    private void deleteCurrentRow(List<Object> selectedItems) {
        for (Object selectedItem : selectedItems) {
            List<String> items = Arrays.asList(selectedItem.toString().split("\\s*,\\s*"));
            String geboortedatumI = items.get(0).substring(1);
            String naamI = items.get(2);
            String voornaamI = items.get(1);
            String eMailI = items.get(6);
            String deleteMedewerker = "DELETE FROM Medewerker WHERE geboortedatum = '" + geboortedatumI + "' AND voornaam = '" + voornaamI + "' AND naam = '" + naamI + "'";
            ConnectionManager.handle.execute(deleteMedewerker);
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
        List<Medewerker> selected = ConnectionManager.handle.createQuery("SELECT * FROM Medewerker WHERE geboortedatum = '" + geboortedatum + "' AND voornaam = '" + voornaam + "' AND naam = '" + naam + "'")
        .mapToBean(Medewerker.class).list();
        Medewerker inputMedewerker = (Medewerker) jPanelFactory.createJPanel(selected.get(0), "modify", this.getClass());
        for (int i = 0; i < functieList.size(); i++){
            if (functieList.get(i).getFunctie().equals(inputMedewerker.getFunctieId())){
                inputMedewerker.setFunctieId(String.valueOf(i+1));
            }
        }
        String updateQuery = "UPDATE Medewerker SET " +
                " geboorteDatum ='" + inputMedewerker.getGeboorteDatum() +
                "' , voornaam='" + inputMedewerker.getVoornaam() +
                "' , naam='" + inputMedewerker.getNaam() +
                "' , sex='" + inputMedewerker.getSex() +
                "' , datumTewerkstelling='" + inputMedewerker.getDatumTewerkstelling() +
                "' , functieId='" + inputMedewerker.getFunctieId() +
                "' , telefoonnummer='" + inputMedewerker.getTelefoonNummer() +
                "' , eMail='" + inputMedewerker.getEmail() +
                "' , gemeente='" + inputMedewerker.getGemeente() +
                "' , straatEnNr='" +inputMedewerker.getStraatEnNr() +
                "' WHERE geboorteDatum= '" + geboortedatum + "' AND naam= '"+ naam + "' AND voornaam= '"+ voornaam +"'";
        if(inputChecker.checkInput(inputMedewerker)){
            ConnectionManager.handle.execute(updateQuery);
            tblConfigs.getItems().clear();
            getMedewerkerList();
            initTable(medewerkerList);
        }
        else{
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
        if(tblConfigs.getSelectionModel().getSelectedCells().size() == 0) {
            showAlert("Hela!", "Eerst een record selecteren hee.");
        }
    }

    public String generatePassword(){
        char[] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRST".toCharArray();

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String randomStr = sb.toString();

        return randomStr;
    }
}
