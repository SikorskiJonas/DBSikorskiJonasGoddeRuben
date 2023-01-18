package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.jdbi.MainDao;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static be.kuleuven.vrolijkezweters.controller.ProjectMainController.user;


public class BeheerMijnWedstrijdenController {

    final WedstrijdDao wedstrijdDao = new WedstrijdDao();
    final CategorieDao categorieDao = new CategorieDao();
    final LoopNummerDao loopNummerDao = new LoopNummerDao();
    final EtappeDao etappeDao = new EtappeDao();
    List<Categorie> categorieList;
    @FXML
    private TableView tblConfigs;
    @FXML
    private Button btnLooptijdPerEtappe;
    @FXML
    private Button btnSchrijfUit;


    public void initialize() {
        List<Wedstrijd> ingeschrevenList = getIngeschrevenList(user);
        categorieList = categorieDao.getAll();
        //convert categorieID's to their categories
        ingeschrevenList = convertCategorieIDs(ingeschrevenList);
        tblConfigs.getItems().clear();
        initTable(ingeschrevenList);

        btnLooptijdPerEtappe.setOnAction(e -> {
            verifyOneRowSelected();
            showLooptijdPerEtappe(selectedToLoopNummers(tblConfigs.getSelectionModel().getSelectedItems()));
        });
        btnSchrijfUit.setOnAction(e -> {
            verifyOneRowSelected();
            verwijderLoopNummers(selectedToLoopNummers(tblConfigs.getSelectionModel().getSelectedItems()));
        });

        tblConfigs.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && tblConfigs.getSelectionModel().getSelectedItem() != null) {
                var selectedRow = (List<String>) tblConfigs.getSelectionModel().getSelectedItem();
                openKlassement(selectedRow.get(0));
            }
        });

    }

    private void initTable(List<Wedstrijd> wedstrijdList) {
        MainDao mainDao = new MainDao();
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
                List<LoopNummer> loopNummers = mainDao.selectByLoperWedstrijd(((Loper) user).getNaam(), wedstrijd.getNaam());
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
        MainDao mainDao = new MainDao();
        List<Wedstrijd> wedstrijdList = new ArrayList<>();
        if (user.getClass() == Loper.class){
            wedstrijdList = mainDao.getWedstrijdenByLoperEmail(user);
        }
        if (user.getClass() == Medewerker.class){
            wedstrijdList = mainDao.getWedstrijdenByMedewerkerEmail(user);
        }
        return wedstrijdList;
    }


    private List<LoopNummer> selectedToLoopNummers(List<Object> selectedItems){
        MainDao mainDao = new MainDao();
        List<String> items = Arrays.asList(selectedItems.get(0).toString().split("\\s*,\\s*")); //only the first selected item is modified
        return mainDao.selectByLoperWedstrijd(((Loper)user).getNaam(), items.get(0).substring(1));}

    private void showLooptijdPerEtappe(List<LoopNummer> loopNummers){
        JPanelFactory jPanelFactory = new JPanelFactory();
        List<LoopNummer> nieuwLoopNummers = jPanelFactory.loopNummerPanel(loopNummers);
    }

    //TODO afmaken
    public void openKlassement(String naam) {
        var contentPane = (AnchorPane) tblConfigs.getParent();
        contentPane.getChildren().clear();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("beheerklassement.fxml"));
            contentPane.getChildren().add(loader.load());
            BeheerKlassementController beheerKlassementController = loader.getController();
            beheerKlassementController.setSelectedWedstrijd(naam);
        } catch (IOException e){
            System.out.println("kon fxml bestand beheerklassement.fxml niet vinden");
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

    private void verwijderLoopNummers(List<LoopNummer> loopNummers){
        for (LoopNummer l : loopNummers) {
            loopNummerDao.delete(l);
        }
        tblConfigs.getItems().clear();
        initTable(convertCategorieIDs(getIngeschrevenList(user)));
    }

    private List<Wedstrijd> convertCategorieIDs(List<Wedstrijd> wedstrijdList){
        for (Wedstrijd wedstrijd : wedstrijdList) {
            String categorieId = wedstrijd.getCategorieID();
            int categorieIdInt = Integer.parseInt(categorieId);
            String categorie = categorieList.get(categorieIdInt - 1).getCategorie();
            wedstrijd.setCategorieID(categorie);
        }
        return  wedstrijdList;
    }
}
