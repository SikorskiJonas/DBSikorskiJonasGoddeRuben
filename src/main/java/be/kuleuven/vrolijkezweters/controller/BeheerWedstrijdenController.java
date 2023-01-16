package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.InputChecker;
import be.kuleuven.vrolijkezweters.JPanelFactory;
import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.jdbi.CategorieJdbi;
import be.kuleuven.vrolijkezweters.jdbi.EtappeJdbi;
import be.kuleuven.vrolijkezweters.jdbi.WedstrijdJdbi;
import be.kuleuven.vrolijkezweters.model.Categorie;
import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Arrays;
import java.util.List;

import static be.kuleuven.vrolijkezweters.controller.ProjectMainController.connectionManager;
import static be.kuleuven.vrolijkezweters.controller.ProjectMainController.user;

public class BeheerWedstrijdenController {
    final InputChecker inputChecker = new InputChecker();
    final JPanelFactory jPanelFactory = new JPanelFactory();
    final WedstrijdJdbi wedstrijdJdbi = new WedstrijdJdbi(ProjectMainController.connectionManager);
    final CategorieJdbi categorieJdbi = new CategorieJdbi(ProjectMainController.connectionManager);
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
    private TableView tblConfigs;

    public void initialize() {
        if (!ProjectMain.isAdmin) {
            btnAdd.setVisible(false);
            btnModify.setVisible(false);
            btnDelete.setVisible(false);
            btnAddEtappe.setVisible(false);
        }
        if (ProjectMain.isAdmin) {
            btnSchrijfIn.setVisible(false);
        }
        getWedstrijdList();
        initTable(wedstrijdList);
        btnAdd.setOnAction(e -> addNewRow());
        btnModify.setOnAction(e -> {
            verifyOneRowSelected();
            modifyCurrentRow(tblConfigs.getSelectionModel().getSelectedItems());
        });
        btnDelete.setOnAction(e -> {
            verifyOneRowSelected();
            deleteCurrentRow();
        });
        btnSchrijfIn.setOnAction(e -> {
            verifyOneRowSelected();
            schrijfIn();
        });
        btnAddEtappe.setOnAction(e -> voegEtappeToe());
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
            int afstand = wedstrijdJdbi.getTotaleAfstand(wedstrijd);
            tblConfigs.getItems().add(FXCollections.observableArrayList(wedstrijd.getNaam(), wedstrijd.getDatum(), wedstrijd.getPlaats(), "\u20AC" + Double.valueOf(wedstrijd.getInschrijvingsgeld()).intValue(), wedstrijd.getCategorieID(), afstand + "m"));
        }
    }

    private void getWedstrijdList() {
        wedstrijdList = wedstrijdJdbi.getAll();
        //fetch list of categorieën
        categorieList = categorieJdbi.getAll();
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
        if (inputChecker.checkInput(inputWedstrijd)) {
            wedstrijdJdbi.insert(inputWedstrijd);
            tblConfigs.getItems().clear();
            getWedstrijdList();
            initTable(wedstrijdList);
        } else {
            showAlert("Input error", "De ingegeven data voldoet niet aan de constraints");
        }
    }

    private void deleteCurrentRow() {
        ObservableList selectedItems = tblConfigs.getSelectionModel().getSelectedItems();
        System.out.println(selectedItems);
        for (Object selectedItem : selectedItems) {
            List<String> items = Arrays.asList(selectedItem.toString().split("\\s*,\\s*"));
            String naamI = items.get(0).substring(1);
            String datumI = items.get(1);
            wedstrijdJdbi.delete(wedstrijdJdbi.selectByNaamDatum(naamI, datumI));
            tblConfigs.getItems().clear();
            initialize();
        }
    }

    private void modifyCurrentRow(List<Object> selectedItems) {
        List<String> items = Arrays.asList(selectedItems.get(0).toString().split("\\s*,\\s*")); //only the first selected item is modified
        String naam = items.get(0).substring(1);
        String plaats = items.get(2);
        Wedstrijd selected = new Wedstrijd(naam, items.get(1), plaats, items.get(3), items.get(4));
        Wedstrijd inputWedstrijd = (Wedstrijd) jPanelFactory.createJPanel(selected, null, this.getClass());
        for (int i = 0; i < categorieList.size(); i++) {
            if (categorieList.get(i).getCategorie().equals(inputWedstrijd.getCategorieID())) {
                inputWedstrijd.setCategorieID(String.valueOf(i + 1));
            }
        }
        if (inputChecker.checkInput(inputWedstrijd)) {
            wedstrijdJdbi.update(inputWedstrijd, naam, plaats);
            tblConfigs.getItems().clear();
            getWedstrijdList();
            initTable(wedstrijdList);
        } else {
            showAlert("Input error", "De ingegeven data voldoet niet aan de constraints");
        }
    }

    private void schrijfIn() {
        List<String> items = Arrays.asList(tblConfigs.getSelectionModel().getSelectedItems().get(0).toString().split("\\s*,\\s*")); //only the first selected item is modified
        Wedstrijd selected = new Wedstrijd(items.get(0).substring(1), items.get(1), items.get(2), items.get(3), items.get(4));
        wedstrijdJdbi.schrijfIn((Loper) user, selected);
    }

    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
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
        EtappeJdbi etappeJdbi = new EtappeJdbi(connectionManager);
        etappeJdbi.insert(jPanelFactory.etappePanel());
    }
}