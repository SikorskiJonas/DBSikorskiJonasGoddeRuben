package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.jdbi.CategorieDao;
import be.kuleuven.vrolijkezweters.jdbi.EtappeDao;
import be.kuleuven.vrolijkezweters.jdbi.WedstrijdDao;
import be.kuleuven.vrolijkezweters.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static be.kuleuven.vrolijkezweters.controller.ProjectMainController.user;


public class BeheerMijnWedstrijdenController {

    final WedstrijdDao wedstrijdDao = new WedstrijdDao();
    final CategorieDao categorieDao = new CategorieDao();
    final EtappeDao etappeDao = new EtappeDao();
    List<Categorie> categorieList;
    @FXML
    private TableView tblConfigs;

    public void initialize() {
        List<Wedstrijd> ingeschrevenList = getIngeschrevenList(user);
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
            int id = wedstrijdDao.getIdByNameAndDate(wedstrijd.getNaam(), wedstrijd.getDatum());
            List<Etappe> etappeList = etappeDao.getByWedstrijdId(id);
            int totaleAfstand = 0;
            for (Etappe etappe : etappeList) {
                totaleAfstand = totaleAfstand + etappe.getAfstandMeter();
            }
            tblConfigs.getItems().add(FXCollections.observableArrayList(wedstrijd.getNaam(), wedstrijd.getDatum(), wedstrijd.getPlaats(), "\u20AC" + Double.valueOf(wedstrijd.getInschrijvingsgeld()).intValue(), wedstrijd.getCategorieID(), totaleAfstand + "m"));
        }
    }

    public List<Wedstrijd> getIngeschrevenList(Object user){
        String query = null;
        List<Wedstrijd> wedstrijdList = new ArrayList<Wedstrijd>();
        if (user.getClass() == Loper.class){
            wedstrijdList = wedstrijdDao.getWedstrijdenByLoperEmail(user);
        }
        if (user.getClass() == Medewerker.class){
            wedstrijdList = wedstrijdDao.getWedstrijdenByMedewerkerEmail(user);
        }
        return wedstrijdList;
    }

    public void openKlassement(String naam) {
        var stage = (Stage) tblConfigs.getScene().getWindow();
        stage.close();
        ProjectMainController projectMainController = new ProjectMainController();
        projectMainController.showBeheerScherm("klassement", projectMainController.btnKlassement);
        System.out.println("open juiste wedstrijd in klassement");

    }
}
