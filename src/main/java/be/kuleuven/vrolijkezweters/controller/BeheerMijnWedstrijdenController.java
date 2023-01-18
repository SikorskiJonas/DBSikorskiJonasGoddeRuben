package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.JPanelFactory;
import be.kuleuven.vrolijkezweters.jdbi.CategorieDao;
import be.kuleuven.vrolijkezweters.jdbi.EtappeDao;
import be.kuleuven.vrolijkezweters.jdbi.LoopNummerDao;
import be.kuleuven.vrolijkezweters.jdbi.WedstrijdDao;
import be.kuleuven.vrolijkezweters.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static be.kuleuven.vrolijkezweters.controller.ProjectMainController.user;


public class BeheerMijnWedstrijdenController {

    final WedstrijdDao wedstrijdDao = new WedstrijdDao();
    final CategorieDao categorieDao = new CategorieDao();
    LoopNummerDao loopNummerDao = new LoopNummerDao();
    final EtappeDao etappeDao = new EtappeDao();
    List<Categorie> categorieList;
    @FXML
    private TableView tblConfigs;
    @FXML
    private Button btnLooptijdPerEtappe;

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

        btnLooptijdPerEtappe.setOnAction(e -> {
            verifyOneRowSelected();
            showLooptijdPerEtappe(selectedToLoopNummers(tblConfigs.getSelectionModel().getSelectedItems()));
        });

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
            if (user.getClass() == Loper.class){
                List<LoopNummer> loopNummers = loopNummerDao.selectByLoperWedstrijd(((Loper) user).getNaam(), wedstrijd.getNaam());
                LoopNummer k = new LoopNummer();
                for (LoopNummer l : loopNummers){
                    k.setLooptijd(k.getLooptijd()+l.getLooptijd());
                }
                int hours = k.getLooptijd() / 3600;
                int minutes = (k.getLooptijd() % 3600) / 60;
                int seconds = k.getLooptijd() % 60;
                String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                tblConfigs.getItems().add(FXCollections.observableArrayList(wedstrijd.getNaam(), wedstrijd.getDatum(), wedstrijd.getPlaats(), "\u20AC" + Double.valueOf(wedstrijd.getInschrijvingsgeld()).intValue(), wedstrijd.getCategorieID(), totaleAfstand + "m", time));
            }
            else{
                tblConfigs.getItems().add(FXCollections.observableArrayList(wedstrijd.getNaam(), wedstrijd.getDatum(), wedstrijd.getPlaats(), "\u20AC" + Double.valueOf(wedstrijd.getInschrijvingsgeld()).intValue(), wedstrijd.getCategorieID(), totaleAfstand + "m"));
            }
        }
    }

    public List<Wedstrijd> getIngeschrevenList(Object user){
        List<Wedstrijd> wedstrijdList = new ArrayList<Wedstrijd>();
        if (user.getClass() == Loper.class){
            wedstrijdList = wedstrijdDao.getWedstrijdenByLoperEmail(user);
        }
        if (user.getClass() == Medewerker.class){
            wedstrijdList = wedstrijdDao.getWedstrijdenByMedewerkerEmail(user);
        }
        return wedstrijdList;
    }

    private List<LoopNummer> selectedToLoopNummers(List<Object> selectedItems){
        LoopNummerDao loopNummerDao = new LoopNummerDao();
        List<String> items = Arrays.asList(selectedItems.get(0).toString().split("\\s*,\\s*")); //only the first selected item is modified
        return loopNummerDao.selectByLoperWedstrijd(((Loper)user).getNaam(), items.get(0).substring(1));}

    private void showLooptijdPerEtappe(List<LoopNummer> loopNummers){
        JPanelFactory jPanelFactory = new JPanelFactory();
        List<LoopNummer> nieuwLoopNummers = jPanelFactory.loopNummerPanel(loopNummers);
    }

    //TODO afmaken
    public void openKlassement(String naam) {
        System.out.println("open juiste wedstrijd in klassement");
        var stage = (Stage) tblConfigs.getScene().getWindow();
        stage.close();
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
}
