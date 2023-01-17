package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.InputChecker;
import be.kuleuven.vrolijkezweters.JPanelFactory;
import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.jdbi.CategorieDao;
import be.kuleuven.vrolijkezweters.jdbi.ConnectionManager;
import be.kuleuven.vrolijkezweters.jdbi.EtappeDao;
import be.kuleuven.vrolijkezweters.jdbi.WedstrijdDao;
import be.kuleuven.vrolijkezweters.model.Categorie;
import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;

import java.util.Arrays;
import java.util.List;

import static be.kuleuven.vrolijkezweters.controller.ProjectMainController.connectionManager;
import static be.kuleuven.vrolijkezweters.controller.ProjectMainController.user;

public class BeheerWedstrijdenController {
    final InputChecker inputChecker = new InputChecker();
    final JPanelFactory jPanelFactory = new JPanelFactory();
    final WedstrijdDao wedstrijdDao = new WedstrijdDao();
    final CategorieDao categorieDao = new CategorieDao();
    List<Categorie> categorieList;
    private List<Wedstrijd> wedstrijdList;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnModify;
    @FXML
    private Button btnSchrijfIn;
    @FXML
    private Button btnAddEtappe;
    @FXML
    private Button btnAddCategorie;
    @FXML
    private TableView tblConfigs;

    public void initialize() {
        if (!ProjectMain.isAdmin) {
            btnAdd.setVisible(false);
            btnModify.setVisible(false);
            btnDelete.setVisible(false);
            btnAddEtappe.setVisible(false);
            btnAddCategorie.setVisible(false);
        }
        if (ProjectMain.isAdmin) {
            btnSchrijfIn.setVisible(false);
        }
        getWedstrijdList();
        initTable(wedstrijdList);
        btnAdd.setOnAction(e -> addNewRow());
        btnModify.setOnAction(e -> {
            verifyOneRowSelected();
            modifyCurrentRow(selectedToWedstrijd(tblConfigs.getSelectionModel().getSelectedItems()));
        });
        btnDelete.setOnAction(e -> {
            verifyOneRowSelected();
            deleteCurrentRow();
        });
        btnSchrijfIn.setOnAction(e -> {
            verifyOneRowSelected();
            schrijfIn(selectedToWedstrijd(tblConfigs.getSelectionModel().getSelectedItems()));
        });
        btnAddEtappe.setOnAction(e -> voegEtappeToe());
        btnAddCategorie.setOnAction(e -> voegCategorieToe());
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

        for (Wedstrijd wedstrijd : wedstrijdList) {
            int afstand = wedstrijdDao.getTotaleAfstand(wedstrijd);
            tblConfigs.getItems().add(FXCollections.observableArrayList(wedstrijd.getNaam(), wedstrijd.getDatum(), wedstrijd.getPlaats(), "\u20AC" + Double.valueOf(wedstrijd.getInschrijvingsgeld()), wedstrijd.getCategorieID(), afstand + "m"));
        }
    }

    private void getWedstrijdList() {
        wedstrijdList = wedstrijdDao.getAll();
        //fetch list of categorieën
        categorieList = categorieDao.getAll();
        //convert categorieID's to their categories
        for (Wedstrijd wedstrijd : wedstrijdList) {
            String categorieId = wedstrijd.getCategorieID();
            int categorieIdInt = Integer.parseInt(categorieId);
            String categorie = categorieList.get(categorieIdInt - 1).getCategorie();
            wedstrijd.setCategorieID(categorie);
        }
    }

    private void addNewRow() {
        Wedstrijd inputWedstrijd = (Wedstrijd) jPanelFactory.createJPanel(null, null, this.getClass());
        for (int i = 0; i < categorieList.size(); i++) {
            if (categorieList.get(i).getCategorie().equals(inputWedstrijd.getCategorieID())) {
                inputWedstrijd.setCategorieID(String.valueOf(i + 1));
            }
        }
        if (inputChecker.checkInput(inputWedstrijd).isEmpty()) {
            wedstrijdDao.insert(inputWedstrijd);
            tblConfigs.getItems().clear();
            getWedstrijdList();
            initTable(wedstrijdList);
        } else {
            String fouten = inputChecker.checkInput(inputWedstrijd).toString();
            showAlert("Input error", fouten + " Voldoet niet aan de criteria");
        }
    }

    private void deleteCurrentRow() {
        ObservableList selectedItems = tblConfigs.getSelectionModel().getSelectedItems();
        System.out.println(selectedItems);
        for (Object selectedItem : selectedItems) {
            List<String> items = Arrays.asList(selectedItem.toString().split("\\s*,\\s*"));
            String naamI = items.get(0).substring(1);
            String datumI = items.get(1);
            wedstrijdDao.delete(wedstrijdDao.selectByNaamDatum(naamI, datumI));
            tblConfigs.getItems().clear();
            initialize();
        }
    }

    private void modifyCurrentRow(Wedstrijd selected) {
        Wedstrijd inputWedstrijd = (Wedstrijd) jPanelFactory.createJPanel(selected, null, this.getClass());
        for (int i = 0; i < categorieList.size(); i++) {
            if (categorieList.get(i).getCategorie().equals(inputWedstrijd.getCategorieID())) {
                inputWedstrijd.setCategorieID(String.valueOf(i + 1));
            }
        }
        if (inputChecker.checkInput(inputWedstrijd).isEmpty()) {
            wedstrijdDao.update(inputWedstrijd, selected);
            tblConfigs.getItems().clear();
            getWedstrijdList();
            initTable(wedstrijdList);
        } else {
            String fouten = inputChecker.checkInput(inputWedstrijd).toString();
            showAlert("Input error", fouten + " Voldoet niet aan de criteria");
            modifyCurrentRow(selected);
        }
    }

    private void schrijfIn(Wedstrijd selected) {
        wedstrijdDao.schrijfIn(user, selected);
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
            showAlert("Hela!", "Eerst een record selecteren hé.");
        }
    }

    private void voegEtappeToe() {
        EtappeDao etappeDao = new EtappeDao();
        etappeDao.insert(jPanelFactory.etappePanel());
    }

    private Wedstrijd selectedToWedstrijd(List<Object> selectedItems){
        List<String> items = Arrays.asList(selectedItems.get(0).toString().split("\\s*,\\s*")); //only the first selected item is modified
        return new Wedstrijd(items.get(0).substring(1), items.get(1), items.get(2), items.get(3).replace("\u20AC", ""), items.get(4));
    }

    private void voegCategorieToe(){
        categorieDao.insert(jPanelFactory.categoriePanel());
        showAlert("Toppie!", "Goed gedaan, je hebt een categorie aangemaakt. \n Ik ben heel trots op je!");
    }
}