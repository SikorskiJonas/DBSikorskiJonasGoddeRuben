package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.jdbc.ConnectionManager;
import be.kuleuven.vrolijkezweters.model.KlassementObject;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.List;

public class BeheerKlassementController {
    private String selectedWedstrijd;

    @FXML
    private Button btnClose;
    @FXML
    private ChoiceBox btnChoise;
    @FXML
    private TableView tblConfigs;

    public void initialize() {
        List<Wedstrijd> wedstrijdList = getWedstrijdList();
        for( Wedstrijd w : wedstrijdList){
            btnChoise.getItems().add(w.getNaam());
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
            tblConfigs.getItems().add(FXCollections.observableArrayList(i + "", k.getVoornaam() + " " + k.getNaam(), k.getLooptijd() + ""));
            System.out.println(k.getVoornaam());
        }

    }

    private List<Wedstrijd> getWedstrijdList(){
        return ConnectionManager.handle.createQuery("SELECT * FROM Wedstrijd")
                .mapToBean(Wedstrijd.class)
                .list();
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
                        "GROUP BY loperId")
                .mapToBean(KlassementObject.class)
                .list();
        Collections.sort(list, (p1, p2) -> p1.getLooptijd() - (p2.getLooptijd()));
        return list;
    }
}
