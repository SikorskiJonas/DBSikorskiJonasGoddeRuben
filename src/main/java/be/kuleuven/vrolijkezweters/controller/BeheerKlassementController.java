package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.InputChecker;
import be.kuleuven.vrolijkezweters.JPanelFactory;
import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.jdbi.LoopNummerDao;
import be.kuleuven.vrolijkezweters.jdbi.WedstrijdDao;
import be.kuleuven.vrolijkezweters.model.KlassementObject;
import be.kuleuven.vrolijkezweters.model.LoopNummer;
import be.kuleuven.vrolijkezweters.model.Medewerker;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BeheerKlassementController {
    final WedstrijdDao wedstrijdDao = new WedstrijdDao();
    final LoopNummerDao loopNummerDao = new LoopNummerDao();
    String selectedWedstrijd;

    @FXML
    private Button btnClose;
    @FXML
    private Button btnAddLooptijd;
    @FXML
    private ComboBox<String> btnChoise;
    @FXML
    private TableView tblConfigs;

    public void initialize() {
        List<Wedstrijd> wedstrijdList = getWedstrijdList();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        wedstrijdList.sort((w1, w2) -> {
            try {
                Date date1 = sdf.parse(w1.getDatum());
                Date date2 = sdf.parse(w2.getDatum());
                return date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        });
        Collections.reverse(wedstrijdList);

        Date now = new Date();
        wedstrijdList.removeIf(wedstrijd -> {
            try {
                return sdf.parse(wedstrijd.getDatum()).after(now);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        });

        for (Wedstrijd w : wedstrijdList) {
            btnChoise.getItems().add(w.getDatum() + " | " + w.getNaam());
        }
        btnChoise.setVisibleRowCount(10);
        if(!ProjectMain.isAdmin){
            btnAddLooptijd.setVisible(false);
        }
        btnChoise.setOnAction(e -> chooseWedstrijd(wedstrijdList));
        btnAddLooptijd.setOnAction(e -> {verifyOneRowSelected();
        editLooptijd(selectedToLoopnummers(tblConfigs.getSelectionModel().getSelectedItems()));});
        btnClose.setOnAction(e -> {
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });

        if (wedstrijdList.size() > 0) {
            btnChoise.setValue(wedstrijdList.get(0).getDatum() + " | " + wedstrijdList.get(0).getNaam());
        }
        selectedWedstrijd = wedstrijdList.get(0).getNaam();
        List<KlassementObject> loopTijden = loopNummerDao.getLoopTijdenList(selectedWedstrijd);
        initTable(loopTijden);
    }

    private void initTable(List<KlassementObject> loopTijdenList) {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblConfigs.getColumns().clear();
        tblConfigs.getItems().clear();

        int colIndex = 0;

        for (var colName : new String[]{"Ranking", "Naam", "Tijd"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }

        int i = 0;
        for (KlassementObject k : loopTijdenList) {
            i++;
            int hours = k.getLooptijd() / 3600;
            int minutes = (k.getLooptijd() % 3600) / 60;
            int seconds = k.getLooptijd() % 60;
            String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            tblConfigs.getItems().add(FXCollections.observableArrayList(i + "", k.getVoornaam() + " " + k.getNaam(), time));
        }

        try {
            Node row = tblConfigs.lookup("TableRow[id=" + 3 + "]");
            row.setStyle("-fx-background-color: yellow;");
        } catch (Exception ignored) {
        }


    }

    private List<Wedstrijd> getWedstrijdList() {
        return wedstrijdDao.getAll();
    }

    private void chooseWedstrijd(List<Wedstrijd> wedstrijdList) {
        int selectedWedstrijdIndex = btnChoise.getSelectionModel().getSelectedIndex();
        selectedWedstrijd = wedstrijdList.get(selectedWedstrijdIndex).getNaam();
        List<KlassementObject> loopTijden = loopNummerDao.getLoopTijdenList(selectedWedstrijd);
        initTable(loopTijden);
    }

    public void editLooptijd(List<LoopNummer> loopNummers) {
        JPanelFactory jPanelFactory = new JPanelFactory();
        List<LoopNummer> nieuwLoopNummers = jPanelFactory.loopNummerPanel(loopNummers);
        if (ProjectMain.isAdmin) {
            InputChecker inputChecker = new InputChecker();
            for (int i = 0; i < loopNummers.size(); i++) {
                loopNummerDao.update(nieuwLoopNummers.get(i), loopNummers.get(i));
            }
            List<KlassementObject> loopTijden = loopNummerDao.getLoopTijdenList(selectedWedstrijd);
            initTable(loopTijden);
        }
        if (!ProjectMain.isAdmin){
            showAlert("Heyhoi", "Je kan als niet-admin je looptijd niet aanpassen, alleen bekijken");
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

    private List<LoopNummer> selectedToLoopnummers(List<Object> selectedItems){
        List<String> items = Arrays.asList(selectedItems.get(0).toString().split("\\s*,\\s*")); //only the first selected item is modified
        return loopNummerDao.selectByLoperWedstrijd(items.get(1).split(" ")[1], this.selectedWedstrijd);}
    }
