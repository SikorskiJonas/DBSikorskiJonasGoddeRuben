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
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class BeheerKlassementController {
    WedstrijdJdbi wedstrijdJdbi = new WedstrijdJdbi(ProjectMainController.connectionManager);
    LoperJdbi loperJdbi = new LoperJdbi(ProjectMainController.connectionManager);

    @FXML
    private Button btnClose;
    @FXML
    private ComboBox btnChoise;
    @FXML
    private TableView tblConfigs;

    public void initialize() {
        List<Wedstrijd> wedstrijdList = getWedstrijdList();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Collections.sort(wedstrijdList, new Comparator<Wedstrijd>() {
            public int compare(Wedstrijd w1, Wedstrijd w2) {
                try {
                    Date date1 = sdf.parse(w1.getDatum());
                    Date date2 = sdf.parse(w2.getDatum());
                    return date1.compareTo(date2);
                } catch (ParseException e) {e.printStackTrace();}
                return 0;
            }
        });
        Collections.reverse(wedstrijdList);

        Date now = new Date();
        wedstrijdList.removeIf(wedstrijd -> {
            try {return sdf.parse(wedstrijd.getDatum()).after(now);}
            catch (ParseException e) {e.printStackTrace();}
            return false;
        });

        for( Wedstrijd w : wedstrijdList){
            btnChoise.getItems().add(w.getDatum() + " | " + w.getNaam());
        }
        btnChoise.setVisibleRowCount(10);

        btnChoise.setOnAction(e -> chooseWedstrijd(wedstrijdList));
        btnClose.setOnAction(e -> {
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });

        if (wedstrijdList.size() > 0) {
            btnChoise.setValue(wedstrijdList.get(0).getDatum() + " | " + wedstrijdList.get(0).getNaam());
        }
        String selectedWedstrijd = wedstrijdList.get(0).getNaam();
        initTable(getLoopTijdenList(selectedWedstrijd));
    }

    private void initTable(List<KlassementObject> loopTijdenList) {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblConfigs.getColumns().clear();
        tblConfigs.getItems().clear();

        int colIndex = 0;

        for(var colName : new String[]{"Ranking", "Naam", "Tijd"}) {
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

        try{
            Node row = tblConfigs.lookup("TableRow[id=" + 3 + "]");
            row.setStyle("-fx-background-color: yellow;");
        } catch (Exception e){}


    }

    private List<Wedstrijd> getWedstrijdList(){
        return wedstrijdJdbi.getAll();
    }

    private void chooseWedstrijd(List<Wedstrijd> wedstrijdList){
        int selectedWedstrijdIndex = btnChoise.getSelectionModel().getSelectedIndex();
        String selectedWedstrijd = wedstrijdList.get(selectedWedstrijdIndex).getNaam();
        initTable(getLoopTijdenList(selectedWedstrijd));
    }

    private List<KlassementObject> getLoopTijdenList(String selectedWedstrijd){
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
