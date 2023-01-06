package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.InputChecker;
import be.kuleuven.vrolijkezweters.JPanelFactory;
import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.jdbc.CategorieJdbi;
import be.kuleuven.vrolijkezweters.jdbc.ConnectionManager;
import be.kuleuven.vrolijkezweters.jdbc.WedstrijdJdbi;
import be.kuleuven.vrolijkezweters.model.Categorie;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class BeheerWedstrijdenController {
    private List<Wedstrijd> wedstrijdList;
    List<Categorie> categorieList;
    InputChecker inputChecker = new InputChecker();
    JPanelFactory jPanelFactory = new JPanelFactory();
    WedstrijdJdbi wedstrijdJdbi = new WedstrijdJdbi(ProjectMainController.connectionManager);
    CategorieJdbi categorieJdbi = new CategorieJdbi(ProjectMainController.connectionManager);

    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnModify;
    @FXML
    private Button btnClose;
    @FXML
    private TableView tblConfigs;

    public void initialize() {
        if(!ProjectMain.isAdmin){
            btnAdd.setVisible(false);
            btnModify.setVisible(false);
            btnDelete.setVisible(false);
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
        
        btnClose.setOnAction(e -> {
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
    }

    private void initTable(List<Wedstrijd> wedstrijdList) {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblConfigs.getColumns().clear();

        int colIndex = 0;

        for(var colName : new String[]{"Naam", "Datum", "Plaats", "Prijs", "Categorie"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }

        for (Wedstrijd wedstrijd : wedstrijdList) {
            tblConfigs.getItems().add(FXCollections.observableArrayList(wedstrijd.getNaam(), wedstrijd.getDatum(), wedstrijd.getPlaats(), wedstrijd.getInschrijvingsgeld(), wedstrijd.getCategorieID()));
        }
    }

    private void getWedstrijdList(){
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
    private void addNewRow(){
        Wedstrijd inputWedstrijd = (Wedstrijd) jPanelFactory.createJPanel(null,null, this.getClass());
        for (int i = 0; i < categorieList.size(); i++){
            if (categorieList.get(i).getCategorie().equals(inputWedstrijd.getCategorieID())){
                inputWedstrijd.setCategorieID(String.valueOf(i+1));
            }
        }
        if(inputChecker.checkInput(inputWedstrijd)){
            wedstrijdJdbi.insert(inputWedstrijd);
            tblConfigs.getItems().clear();
            getWedstrijdList();
            initTable(wedstrijdList);
        }
        else{
            showAlert("Input error", "De ingegeven data voldoet niet aan de constraints");
        }
    }

    private void deleteCurrentRow() {
        List<Object> selectedItems = tblConfigs.getSelectionModel().getSelectedItems();
        System.out.println(selectedItems);
        for (int i = 0; i < selectedItems.size(); i++) {
            List<String> items = Arrays.asList(selectedItems.get(i).toString().split("\\s*,\\s*"));
            String naamI = items.get(0).substring(1);
            String datumI = items.get(1);
            wedstrijdJdbi.delete(wedstrijdJdbi.selectByNaamDatum(naamI,datumI));
            tblConfigs.getItems().clear();
            initialize();
        }
    }

    private void modifyCurrentRow(List<Object> selectedItems) {
        List<String> items = Arrays.asList(selectedItems.get(0).toString().split("\\s*,\\s*")); //only the first selected item is modified
        String naam = items.get(0).substring(1);
        String plaats = items.get(2);
        Wedstrijd selected = new Wedstrijd(naam, items.get(1), plaats, items.get(3), items.get(4));
        Wedstrijd inputWedstrijd = (Wedstrijd) jPanelFactory.createJPanel(selected,null, this.getClass());
        for (int i = 0; i < categorieList.size(); i++){
            if (categorieList.get(i).getCategorie().equals(inputWedstrijd.getCategorieID())){
                inputWedstrijd.setCategorieID(String.valueOf(i+1));
            }
        }
        if(inputChecker.checkInput(inputWedstrijd)){
            wedstrijdJdbi.update(inputWedstrijd,naam,plaats);
            tblConfigs.getItems().clear();
            getWedstrijdList();
            initTable(wedstrijdList);
        }
        else{
            showAlert("Input error", "De ingegeven data voldoet niet aan de constraints");
        }
    }

    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void verifyOneRowSelected() {
        if(tblConfigs.getSelectionModel().getSelectedCells().size() == 0) {
            showAlert("Hela!", "Eerst een record selecteren hé.");
        }
    }
<<<<<<< HEAD

    private ArrayList<String> createJPanel(List<String> items){
        JTextField naam = new JTextField();
        JXDatePicker picker = new JXDatePicker();
        JTextField plaats = new JTextField();
        JTextField inschrijvingsGeld = new JTextField();
        List<Categorie> categorieList = ConnectionManager.handle.createQuery("SELECT * FROM Categorie")
                .mapToBean(Categorie.class)
                .list();
        String[] choices = new String[categorieList.size()];
        for(int i = 0 ; i < categorieList.size(); i++){
            choices[i] = categorieList.get(i).toString().replace("Categorie{categorie'","").replace("}","");
        }

        final JComboBox<String> category = new JComboBox<String>(choices);

        picker.setDate((Date) choices[0]);
        picker.setFormats(new SimpleDateFormat("dd/MM/yyyy"));

        if (items != null){ // if an item is selected, automatically pre-fill boxes
            naam.setText(items.get(0).substring(1));
            plaats.setText(items.get(2));
            inschrijvingsGeld.setText(items.get(3).substring(0, items.get(3).length() - 1));
            category.setSelectedItem(items.get(4));
        }

        Object[] message = { "Naam: ", naam,
                "datum: ", picker,
                "Locatie: ", plaats,
                "Inschrijfprijs: ", inschrijvingsGeld,
                "Categorie", category};
        String[] buttons = { "Save", "Cancel" };
        int option = JOptionPane.showOptionDialog(null, message, "Add Loper", JOptionPane.OK_CANCEL_OPTION, 0, null, buttons, buttons[0]);

        Date date = picker.getDate();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateFormatted = format.format(date);
        ArrayList<String> r = new ArrayList();
        r.add(naam.getText());
        r.add(dateFormatted);
        r.add(plaats.getText());
        r.add(inschrijvingsGeld.getText());
        r.add(category.getSelectedItem().toString());
        return r;
    }
=======
>>>>>>> bf0aee5457815d384b97f3d16cf4c69986ffa554
}
