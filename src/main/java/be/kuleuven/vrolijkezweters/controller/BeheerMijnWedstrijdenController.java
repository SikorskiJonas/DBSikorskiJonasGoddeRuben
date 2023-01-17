package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.jdbi.CategorieDao;
import be.kuleuven.vrolijkezweters.jdbi.WedstrijdDao;
import be.kuleuven.vrolijkezweters.model.Categorie;
import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;

import static be.kuleuven.vrolijkezweters.controller.ProjectMainController.user;


public class BeheerMijnWedstrijdenController {

    final WedstrijdDao wedstrijdDao = new WedstrijdDao(ProjectMainController.connectionManager);
    final CategorieDao categorieDao = new CategorieDao(ProjectMainController.connectionManager);
    List<Categorie> categorieList;
    @FXML
    private TableView tblConfigs;

    public void initialize() {
        List<Wedstrijd> ingeschrevenList = wedstrijdDao.getInschreven(user);
        categorieList = categorieDao.getAll();
        //convert categorieID's to their categories
        for (Wedstrijd wedstrijd : ingeschrevenList) {
            String categorieId = wedstrijd.getCategorieID();
            int categorieIdInt = Integer.parseInt(categorieId);
            String categorie = categorieList.get(categorieIdInt - 1).getCategorie();
            wedstrijd.setCategorieID(categorie);
        }
        tblConfigs.getItems().clear();
        initTable(ingeschrevenList);

        tblConfigs.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && tblConfigs.getSelectionModel().getSelectedItem() != null) {
                var selectedRow = (List<String>) tblConfigs.getSelectionModel().getSelectedItem();
                openKlassement(selectedRow.get(0));
            }
        });

    }

    private void initTable(List<Wedstrijd> wedstrijdList) {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblConfigs.getColumns().clear();

        int colIndex = 0;
        for (var colName : new String[]{"Naam", "Datum", "Plaats", "Inschrijving", "Categorie", "Totale afstand"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }
        if (user.getClass()== Loper.class){
            tblConfigs.getColumns().add(new TableColumn<>("Looptijd totaal"));
        }

        for (Wedstrijd wedstrijd : wedstrijdList) {
            int afstand = wedstrijdDao.getTotaleAfstand(wedstrijd);
            tblConfigs.getItems().add(FXCollections.observableArrayList(wedstrijd.getNaam(), wedstrijd.getDatum(), wedstrijd.getPlaats(), "\u20AC" + Double.valueOf(wedstrijd.getInschrijvingsgeld()).intValue(), wedstrijd.getCategorieID(), afstand + "m"));
        }
    }

    //TODO afmaken
    public void openKlassement(String naam) {
        System.out.println("open juiste wedstrijd in klassement");
        var stage = (Stage) tblConfigs.getScene().getWindow();
        stage.close();
    }
}
