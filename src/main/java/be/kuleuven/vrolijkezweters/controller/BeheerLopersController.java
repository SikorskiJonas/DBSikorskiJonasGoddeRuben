package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.InputChecker;
import be.kuleuven.vrolijkezweters.JPanelFactory;
import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.jdbc.ConnectionManager;
import be.kuleuven.vrolijkezweters.model.Loper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class BeheerLopersController {
    List<Loper> loperList;
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
        getLoperList();
        initTable(loperList);
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

    private void initTable(List<Loper> loperList) {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblConfigs.getColumns().clear();
        int colIndex = 0;
        for(var colName : new String[]{"GeboorteDatum", "VoorNaam", "Naam", "Sex", "Lengte", "telefoonNummer", "E-mail", "Gemeente", "Straat + nr"}) {
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

    public void getLoperList(){
        System.out.println("fetching list of lopers");
            loperList = ConnectionManager.handle.createQuery("SELECT * FROM Loper")
                    .mapToBean(Loper.class)
                    .list();
    }

    private void addNewRow() {
        ArrayList<String> inputData = jPanelFactory.createJPanel(null, "add",this.getClass());
        String insertQuery = "INSERT INTO loper (geboorteDatum, voornaam, naam, sex, lengte, telefoonnummer, eMail, gemeente, straatEnNr, wachtwoord) values ('" +
                inputData.get(0) +"', '" + inputData.get(1) +"', '" + inputData.get(2) +"', '" + inputData.get(3) +"', '" + inputData.get(4) +"', '" + inputData.get(5) +"', '" + inputData.get(6) +"', '" + inputData.get(7) +"', '" + inputData.get(8) +"', '" + inputData.get(9) +"')";
        if(inputChecker.checkInput(inputData, "Loper")){
            ConnectionManager.handle.execute(insertQuery);
            tblConfigs.getItems().clear();
            getLoperList();
            initTable(loperList);
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
            String deleteLoper = "DELETE FROM Loper WHERE geboortedatum = '" + geboortedatumI + "' AND voornaam = '" + voornaamI + "' AND naam = '" + naamI + "'";
            ConnectionManager.handle.execute(deleteLoper);
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
        ArrayList<String> inputData = jPanelFactory.createJPanel(items, "modify",this.getClass());
        String insertQuery = "UPDATE Loper SET " +
                " geboortedatum = '" + inputData.get(0) +
                "' , voornaam= '" + inputData.get(1) +
                "' , naam= '" + inputData.get(2) +
                "' , sex= '" + inputData.get(3) +
                "' , lengte= '" + inputData.get(4) +
                "' , telefoonnummer= '" + inputData.get(5) +
                "' , eMail= '" + inputData.get(6) +
                "' , gemeente= '" + inputData.get(7) +
                "' , straatEnNr= '" +inputData.get(8)   +
                "' WHERE geboorteDatum= '" + geboortedatum + "' AND naam= '"+ naam + "' AND voornaam= '"+ voornaam + "';";
        if(inputChecker.checkInput(inputData, "Loper")){
            ConnectionManager.handle.execute(insertQuery);
            tblConfigs.getItems().clear();
            getLoperList();
            initTable(loperList);
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
}
