package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.InputChecker;
import be.kuleuven.vrolijkezweters.JPanelFactory;
import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.jdbi.*;
import be.kuleuven.vrolijkezweters.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static be.kuleuven.vrolijkezweters.controller.ProjectMainController.user;

public class BeheerWedstrijdenController {

    final InputChecker inputChecker = new InputChecker();
    final JPanelFactory jPanelFactory = new JPanelFactory();
    final WedstrijdDao wedstrijdDao = new WedstrijdDao();
    final CategorieDao categorieDao = new CategorieDao();
    final EtappeDao etappeDao = new EtappeDao();
    final MedewerkerWedstrijdDao medewerkerWedstrijdDao = new MedewerkerWedstrijdDao();


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
        List<Wedstrijd> wedstrijdList = getWedstrijdList();
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
        btnAddEtappe.setOnAction(e -> voegEtappeToe(selectedToWedstrijd(tblConfigs.getSelectionModel().getSelectedItems())));
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
            int id = wedstrijdDao.getIdByNameAndDate(wedstrijd.getNaam(), wedstrijd.getDatum());
            List<Etappe> etappeList = etappeDao.getByWedstrijdId(id);
            int totaleAfstand = 0;
            for (Etappe etappe : etappeList) {
                totaleAfstand = totaleAfstand + etappe.getAfstandMeter();
            }
            tblConfigs.getItems().add(FXCollections.observableArrayList(wedstrijd.getNaam(), wedstrijd.getDatum(), wedstrijd.getPlaats(), "\u20AC" + Double.valueOf(wedstrijd.getInschrijvingsgeld()), wedstrijd.getCategorieID(), totaleAfstand + "m"));
        }
    }

    private List<Wedstrijd> getWedstrijdList() {
        List<Wedstrijd> wedstrijdList = wedstrijdDao.getAll();
        //fetch list of categorieën
        List<Categorie> categorieList = categorieDao.getAll();
        //convert categorieID's to their categories
        for (Wedstrijd wedstrijd : wedstrijdList) {
            String categorieId = wedstrijd.getCategorieID();
            int categorieIdInt = Integer.parseInt(categorieId);
            String categorie = categorieList.get(categorieIdInt - 1).getCategorie();
            wedstrijd.setCategorieID(categorie);
        }
        return wedstrijdList;
    }

    private void addNewRow() {
        Wedstrijd inputWedstrijd = (Wedstrijd) jPanelFactory.createJPanel(null, null, this.getClass());
        List<Categorie> categorieList = categorieDao.getAll();
        if (inputWedstrijd != null) {
            for (int i = 0; i < categorieList.size(); i++) {
                if (categorieList.get(i).getCategorie().equals(inputWedstrijd.getCategorieID())) {
                    inputWedstrijd.setCategorieID(String.valueOf(i + 1));
                }
            }
            if (inputChecker.checkInput(inputWedstrijd).isEmpty()) {
                wedstrijdDao.insert(inputWedstrijd);
                tblConfigs.getItems().clear();
                List<Wedstrijd> wedstrijdList = getWedstrijdList();
                initTable(wedstrijdList);
            } else {
                String fouten = inputChecker.checkInput(inputWedstrijd).toString();
                showAlert("Input error", fouten + " Voldoet niet aan de criteria");
            }
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
        List<Categorie> categorieList = categorieDao.getAll();
        Wedstrijd inputWedstrijd = (Wedstrijd) jPanelFactory.createJPanel(selected, null, this.getClass());
        if (inputWedstrijd != null) {
            for (int i = 0; i < categorieList.size(); i++) {
                if (categorieList.get(i).getCategorie().equals(inputWedstrijd.getCategorieID())) {
                    inputWedstrijd.setCategorieID(String.valueOf(i + 1));
                }
            }
            if (inputChecker.checkInput(inputWedstrijd).isEmpty()) {
                wedstrijdDao.update(inputWedstrijd, selected);
                tblConfigs.getItems().clear();
                getWedstrijdList();
                List<Wedstrijd> wedstrijdList = wedstrijdDao.getAll();
                initTable(wedstrijdList);
            } else {
                String fouten = inputChecker.checkInput(inputWedstrijd).toString();
                showAlert("Input error", fouten + " Voldoet niet aan de criteria");
                modifyCurrentRow(selected);
            }
        }
    }

    public void schrijfIn(Wedstrijd wedstrijd) {
        Date wedstrijdDatum;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            wedstrijdDatum = sdf.parse(wedstrijd.getDatum());
        } catch (ParseException e){
            showAlert("Error", "Er ging iets fout bij het controlleren van de datum, contacteer een admin");
            return;
        }
        if(wedstrijdDatum.after(new Date())){
            if (user.getClass() == Loper.class){
                LoopNummerDao loopNummerDao = new LoopNummerDao();
                LoperDao loperDao = new LoperDao();
                Loper l = (Loper) user;
                List<LoopNummer> bestaandeNummers = loopNummerDao.getAllSorted();
                int nieuwNummer = bestaandeNummers.get(0).getNummer() + 1;
                int loperID = loperDao.getId(l);
                int wedstrijdId = wedstrijdDao.getIdByNameAndDate(wedstrijd.getNaam(), wedstrijd.getDatum());
                List<Etappe> etappeList = etappeDao.getByWedstrijdId(wedstrijdId);
                for (Etappe etappe : etappeList) {
                    int etappeID = etappeDao.getIdByName(etappe.getNaam());
                    LoopNummer loopNummer = new LoopNummer(nieuwNummer, 0, loperID, etappeID);
                    loopNummerDao.insert(loopNummer);
                }
            }
            if (user.getClass() == Medewerker.class){
                MedewerkerDao medewerkerDao = new MedewerkerDao();
                WedstrijdDao wedstrijdDao = new WedstrijdDao();
                Medewerker m = (Medewerker) user;
                int medewerkerId = medewerkerDao.getId(m);
                int wedstrijdId = wedstrijdDao.getId(wedstrijd);
                medewerkerWedstrijdDao.insert(medewerkerId, wedstrijdId);
            }
        }
        if (wedstrijdDatum.before(new Date())){
            showAlert("Helaba", "Je kan je niet inschrijven voor een wedstrijd die al voorbij is");
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
            showAlert("Hela!", "Eerst een record selecteren hé.");
        }
    }

    private void voegEtappeToe(Wedstrijd wedstrijd) {
        EtappeDao etappeDao = new EtappeDao();
        etappeDao.insert(jPanelFactory.etappePanel(wedstrijd));
        List<Wedstrijd> wedstrijdList = getWedstrijdList();
        tblConfigs.getItems().clear();
        initTable(wedstrijdList);
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