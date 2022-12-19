package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BeheerKlassementController {
    private Jdbi jdbi;
    private String selectedWedstrijd;
    private Handle h;

    @FXML
    private Button btnClose;
    @FXML
    private ChoiceBox btnChoise;
    @FXML
    private TableView tblConfigs;

    public void initialize() {
        connectDatabase();
        List<Wedstrijd> wedstrijdList = getWedstrijdList();
        for( Wedstrijd w : wedstrijdList){
            btnChoise.getItems().add(w.getNaam());
        }

        btnChoise.setOnAction(e -> chooseWedstrijd(wedstrijdList));
        btnClose.setOnAction(e -> {
            h.close();
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
    }

    public void connectDatabase() {
        jdbi = Jdbi.create("jdbc:sqlite:databaseJonasRuben.db");
        h = jdbi.open();
        System.out.println("Connected to database");
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
        return h.createQuery("SELECT * FROM wedstrijd")
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
        List<KlassementObject> list = h.createQuery("SELECT LoperId, loper.Voornaam, loper.Naam, Sum(Looptijd) AS Looptijd FROM loop_nummer " +
                        "INNER JOIN etappe on etappe.Id = loop_nummer.EtappeId " +
                        "INNER JOIN loper on loper.Id = loop_nummer.LoperId " +
                        "INNER JOIN wedstrijd on wedstrijd.Id = etappe.WedstrijdId " +
                        "WHERE wedstrijd.naam = '" + selectedWedstrijd + "' " +
                        "GROUP BY LoperId")
                .mapToBean(KlassementObject.class)
                .list();
        Collections.sort(list, (p1, p2) -> p1.getLooptijd() - (p2.getLooptijd()));
        return list;
    }
}
