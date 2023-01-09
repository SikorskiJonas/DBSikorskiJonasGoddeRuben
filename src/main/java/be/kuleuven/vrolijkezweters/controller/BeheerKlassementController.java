package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.jdbi.ConnectionManager;
import be.kuleuven.vrolijkezweters.jdbi.LoperJdbi;
import be.kuleuven.vrolijkezweters.jdbi.WedstrijdJdbi;
import be.kuleuven.vrolijkezweters.model.KlassementObject;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BeheerKlassementController {
    private String selectedWedstrijd;
    WedstrijdJdbi wedstrijdJdbi = new WedstrijdJdbi(ProjectMainController.connectionManager);
    LoperJdbi loperJdbi = new LoperJdbi(ProjectMainController.connectionManager);

    @FXML
    private Button btnClose;
    @FXML
    private ChoiceBox btnChoise;
    @FXML
    private TableView tblConfigs;

    public void initialize() {
        List<Wedstrijd> wedstrijdList = getWedstrijdList();
        for( Wedstrijd w : wedstrijdList){
            btnChoise.getItems().add(w.getDatum() + " " + w.getNaam());
        }

        btnChoise.setOnAction(e -> chooseWedstrijd(wedstrijdList));
        btnClose.setOnAction(e -> {
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
    }

    private void initTable(List<KlassementObject> loopTijdenList) {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblConfigs.getColumns().clear();
        tblConfigs.getItems().clear();

        int colIndex = 0;

        for(var colName : new String[]{"Ranking", "Naam", "loopTijd"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }

        int i = 0;
        for (KlassementObject k : loopTijdenList) {
            i++;
            tblConfigs.getItems().add(FXCollections.observableArrayList(i + "", k.getVoornaam() + " " + k.getNaam(), k.getLooptijd()/60 + ":" + String.format("%02d", k.getLooptijd()%60)));
        }

    }

    private List<Wedstrijd> getWedstrijdList(){
        return wedstrijdJdbi.getAll();
    }

    private void chooseWedstrijd(List<Wedstrijd> wedstrijdList){
        int selectedWedstrijdIndex = btnChoise.getSelectionModel().getSelectedIndex();
        selectedWedstrijd = wedstrijdList.get(selectedWedstrijdIndex).getNaam();
        System.out.println(selectedWedstrijd);
        initTable(getLoopTijdenList());
    }

    private List<KlassementObject> getLoopTijdenList(){
        List<KlassementObject> list = ConnectionManager.handle.createQuery("SELECT LoperId, Loper.voornaam, Loper.naam, Sum(looptijd) AS looptijd FROM loopNummer " +
                        "INNER JOIN Etappe on Etappe.id = loopNummer.etappeId " +
                        "INNER JOIN Loper on Loper.id = loopNummer.loperId " +
                        "INNER JOIN Wedstrijd on Wedstrijd.id = Etappe.wedstrijdId " +
                        "WHERE Wedstrijd.naam = '" + selectedWedstrijd + "' " +
                        "GROUP BY loperId " +
                        "ORDER BY looptijd ASC")
                .mapToBean(KlassementObject.class)
                .list();
        return list;
    }
}
