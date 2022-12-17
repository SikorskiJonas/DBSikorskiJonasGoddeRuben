package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.model.Wedstrijd;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BeheerImportController {
    private Jdbi jdbi;
    private Handle h;
    private String importChoise;

    @FXML
    private ChoiceBox btnImport;
    @FXML
    private Button btnClose;
    @FXML
    private TableView tblConfigs;

    public void initialize(){
        connectDatabase();

        btnImport.getItems().addAll("Wedstrijd", "Loper", "Medewerker", "Etappe", "LoopNummer");

        btnImport.setOnAction(e -> chooseModel());
        btnClose.setOnAction(e -> {
            h.close();
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
    }

    public void connectDatabase(){
        jdbi = Jdbi.create("jdbc:sqlite:databaseJonasRuben.db");
        h = jdbi.open();
        System.out.println("Connected to database");
    }

    private void chooseModel(){
        importChoise = btnImport.getSelectionModel().getSelectedItem().toString();
        List<String> columns = new ArrayList<String>();
        switch(importChoise){
            case "Wedstrijd":
                Collections.addAll(columns, "naam", "datum", "plaats", "inschrijvingsgeld", "categorieId");
                break;
            case "Loper":
                Collections.addAll(columns, "geboorteDatum", "voornaam", "naam", "sex", "lengte", "telefoonNummer", "email", "gemeente", "straatplusnr");
                break;
            case "Medewerker":
                Collections.addAll(columns, "geboorteDatum", "voornaam", "naam", "sex", "datumTewerkstelling", "functieId", "telefoonNummer", "email", "gemeente", "straatplusnr");
                break;
            case "Etappe":
                Collections.addAll(columns, "afstandMeter", "startPlaats", "eindPlaats", "wedstrijdId", "naam");
                break;
            case "LoopNummer":
                Collections.addAll(columns, "nummer", "loopTijd", "loperId", "etappeId");
                break;
        }
        initTable(columns, columns.size());
    }

    private void initTable(List<String> columns, int lenght) {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblConfigs.getColumns().clear();

        int colIndex = 0;

        String[] columnss = columns.toArray(new String[0]);
        for(var colName : columnss) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }

        /*for (int i = 0; i<10; i++) {
            tblConfigs.getItems().add(FXCollections.observableArrayList("mm", "dd", "ff", "zz"));
        }*/
    }

}
