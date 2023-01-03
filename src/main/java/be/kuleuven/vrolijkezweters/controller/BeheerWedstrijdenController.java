package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.InputChecker;
import be.kuleuven.vrolijkezweters.JPanelFactory;
import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.jdbc.ConnectionManager;
import be.kuleuven.vrolijkezweters.model.Categorie;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.sun.javafx.application.PlatformImpl.exit;

public class BeheerWedstrijdenController {
    private List<Wedstrijd> wedstrijdList;
    List<Categorie> categorieList;
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

    public void initialize() {
        if(!ProjectMain.isAdmin){
            btnAdd.setVisible(false);
            btnModify.setVisible(false);
            btnDelete.setVisible(false);
        }
        getWedstrijdList();
        initTable(wedstrijdList);
        btnAdd.setOnAction(e -> addNewRow());
        btnModify.setOnAction(e -> {
            verifyOneRowSelected();
            modifyCurrentRow(tblConfigs.getSelectionModel().getSelectedItems());
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

    private void initTable(List<Wedstrijd> wedstrijdList) {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblConfigs.getColumns().clear();

        int colIndex = 0;

        for(var colName : new String[]{"Naam", "Datum", "Plaats", "Prijs", "Categorie"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }

        for (Wedstrijd wedstrijd : wedstrijdList) {
            tblConfigs.getItems().add(FXCollections.observableArrayList(wedstrijd.getNaam(), wedstrijd.getDatum(), wedstrijd.getPlaats(), wedstrijd.getInschrijvingsgeld(), wedstrijd.getCategorieId()));
        }
    }

    private void getWedstrijdList(){
        wedstrijdList = ConnectionManager.handle.createQuery("SELECT * FROM Wedstrijd")
                .mapToBean(Wedstrijd.class)
                .list();
        //fetch list of categorieën
        categorieList = ConnectionManager.handle.createQuery("SELECT * FROM Categorie")
                .mapToBean(Categorie.class)
                .list();
        //convert categorieID's to their categories
        for (Wedstrijd wedstrijd : wedstrijdList) {
            String categorieId = wedstrijd.getCategorieId();
            int categorieIdInt = Integer.parseInt(categorieId);
            String categorie = categorieList.get(categorieIdInt - 1).getCategorie();
            wedstrijd.setCategorieId(categorie);
        }
    }
    private void addNewRow(){
        ArrayList<String> inputData = jPanelFactory.createJPanel(null,null, this.getClass());
        int gekozenFunctieID = 999;
        for (int i = 0; i < categorieList.size(); i++){
            if (categorieList.get(i).getCategorie().equals(inputData.get(4))){
                gekozenFunctieID = i+1;
            }
        }
        String insertQuery = "INSERT INTO Wedstrijd (naam, datum, plaats, inschrijvingsgeld, categorieID) values ('" +
                inputData.get(0) +"', '" + inputData.get(1) +"', '" + inputData.get(2) +"', '" + inputData.get(3) +"', '" + String.valueOf(gekozenFunctieID)+"')";
        System.out.println(insertQuery);
        if(inputChecker.checkInput(inputData, "Wedstrijd")){
            ConnectionManager.handle.execute(insertQuery);
            tblConfigs.getItems().clear();
            getWedstrijdList();
            initTable(wedstrijdList);
        }
        else{
            showAlert("Input error", "De ingegeven data voldoet niet aan de constraints");
        }
    }

    private void deleteCurrentRow() {
        List<Object> selectedItems = tblConfigs.getSelectionModel().getSelectedItems();
        System.out.println(selectedItems);
        for (int i = 0; i < selectedItems.size(); i++) {
            List<String> items = Arrays.asList(selectedItems.get(i).toString().split("\\s*,\\s*"));
            String naamI = items.get(0).substring(1);
            String datumI = items.get(1);
            String q = "DELETE FROM Wedstrijd WHERE datum = '" + datumI +"' AND naam = '"+ naamI +"'";
            System.out.println(q);
            ConnectionManager.handle.execute(q);
            tblConfigs.getItems().clear();
            initialize();
        }
    }

    private void modifyCurrentRow(List<Object> selectedItems) {
        List<String> items = Arrays.asList(selectedItems.get(0).toString().split("\\s*,\\s*")); //only the first selected item is modified
        String naam = items.get(0).substring(1);
        String plaats = items.get(2);
        ArrayList<String> inputData = jPanelFactory.createJPanel(items,null, this.getClass());
        int gekozenCategorieID = 0;
        for (int i = 0; i < categorieList.size(); i++){
            if (categorieList.get(i).getCategorie().equals(inputData.get(4))){
                gekozenCategorieID = i+1;
            }
        }
        String updateQuery = "UPDATE Wedstrijd SET " +
                " naam ='" + inputData.get(0) +
                "' , datum='" + inputData.get(1) +
                "' , plaats='" + inputData.get(2) +
                "' , inschrijvingsgeld='" + inputData.get(3) +
                "' , categorieID='" + gekozenCategorieID +
                "' WHERE naam= '" + naam + "' AND plaats= '"+ plaats +"'";
        if(inputChecker.checkInput(inputData, "Wedstrijd")){
            ConnectionManager.handle.execute(updateQuery);
            tblConfigs.getItems().clear();
            getWedstrijdList();
            initTable(wedstrijdList);
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
            showAlert("Hela!", "Eerst een record selecteren hé.");
        }
    }
}
