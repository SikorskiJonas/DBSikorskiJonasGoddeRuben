package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.jdbc.ConnectionManager;
import be.kuleuven.vrolijkezweters.model.Functie;
import be.kuleuven.vrolijkezweters.model.Medewerker;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jdbi.v3.core.result.ResultIterable;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class BeheerMedewerkersController {
    private List<Medewerker> medewerkerList;
    private List<Functie> functieList;

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

    public void initialize(){
        getMedewerkerList();
        initTable(medewerkerList);
        btnAdd.setOnAction(e -> addNewRow());
        btnModify.setOnAction(e -> {
            verifyOneRowSelected();
            modifyCurrentRow(tblConfigs.getSelectionModel().getSelectedItems());
        });
        btnDelete.setOnAction(e -> {
            verifyOneRowSelected();
            deleteCurrentRow(tblConfigs.getSelectionModel().getSelectedItems());
        });
        btnClose.setOnAction(e -> {
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
    }

    private void initTable(List<Medewerker> medewerkerList) {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblConfigs.getColumns().clear();
        int colIndex = 0;
        for(var colName : new String[]{"GeboorteDatum", "VoorNaam", "Naam", "Sex", "Datum tewerkstelling", "Functie", "Telefoonnummer", "E-mail", "Gemeente", "Straat + nr"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }
        for (Medewerker medewerker : medewerkerList) {
            String f = String.valueOf(medewerker.getFunctieId());
            tblConfigs.getItems().add(FXCollections.observableArrayList(medewerker.getGeboorteDatum(), medewerker.getVoornaam(), medewerker.getNaam(), medewerker.getSex(), medewerker.getDatumTewerkstelling(), String.valueOf(medewerker.getFunctieId()), medewerker.getTelefoonNummer(), medewerker.getEmail(), medewerker.getGemeente(), medewerker.getStraatEnNr()));
        }
    }

    public void getMedewerkerList(){
        System.out.println("fetching list of medewerkers");
        medewerkerList = ConnectionManager.handle.createQuery("SELECT * FROM Medewerker")
                .mapToBean(Medewerker.class)
                .list();
        //fetch list of functies
        functieList = ConnectionManager.handle.createQuery("SELECT * FROM Functie")
                .mapToBean(Functie.class)
                .list();
        //convert functieID's to their functies
        for (Medewerker medewerker : medewerkerList) {
            String functieID = medewerker.getFunctieId();
            int functieIDInt = Integer.parseInt(functieID);
            String functie = functieList.get(functieIDInt - 1).getFunctie();
            medewerker.setFunctieId(functie);
        }
    }

    private void addNewRow() {
        ArrayList<String> inputData = createJPanel(null);
        int gekozenFunctieID = 999;
        for (int i = 0; i <= functieList.size(); i++){
            if (functieList.get(i).getFunctie()==inputData.get(5)){
                gekozenFunctieID = i;
            }
        }
        String insertQuery = "INSERT INTO Medewerker (geboorteDatum, voornaam, naam, sex, datumTewerkstelling, functieID, telefoonnummer, 'eMail', gemeente, 'straatEnNr') values ('" +
                inputData.get(0) +"', '" + inputData.get(1) +"', '" + inputData.get(2) +"', '" + inputData.get(3) +"', '" + inputData.get(4) +"', '" + String.valueOf(gekozenFunctieID) +"', '" + inputData.get(6) +"', '" + inputData.get(7) +"', '" + inputData.get(8) +"', '" + inputData.get(9) + "')";
        if(checkInput(inputData)){
            ConnectionManager.handle.execute(insertQuery);
            tblConfigs.getItems().add(FXCollections.observableArrayList(inputData.get(0), inputData.get(1), inputData.get(2), inputData.get(3), inputData.get(4), inputData.get(6), inputData.get(7), inputData.get(8), inputData.get(9)));
        }
        else{
            showAlert("Input error", "De ingegeven data voldoet niet aan de constraints");
        }

    }

    private boolean checkInput(ArrayList<String> data){
        return data.get(1).length() <= 100 && data.get(1) != null &&
                data.get(2).length() <= 100 && data.get(2) != null &&
                (Objects.equals(data.get(3), "M") || Objects.equals(data.get(3), "F") || Objects.equals(data.get(3), "X")) && data.get(3) != null &&
                data.get(5).length() <= 100 && data.get(5) != null &&
                data.get(6).length() <= 100 && data.get(6) != null &&
                data.get(7).length() <= 100 && data.get(7) != null && data.get(7).matches("(.*)@(.*).(.*)") &&
                data.get(8).length() <= 100 && data.get(8) != null &&
                data.get(9).length() <= 100 && data.get(9) != null;
    }

    private void deleteCurrentRow(List<Object> selectedItems) {
        for (Object selectedItem : selectedItems) {
            List<String> items = Arrays.asList(selectedItem.toString().split("\\s*,\\s*"));
            String geboortedatumI = items.get(0).substring(1);
            String naamI = items.get(2);
            String voornaamI = items.get(1);
            String q = "DELETE FROM Medewerker WHERE geboortedatum = '" + geboortedatumI + "' AND voornaam = '" + voornaamI + "' AND naam = '" + naamI + "'";
            System.out.println(q);
            ConnectionManager.handle.execute(q);
            tblConfigs.getItems().clear();
            initialize();
        }
    }

    private void modifyCurrentRow(List<Object> selectedItems) {
        List<String> items = Arrays.asList(selectedItems.get(0).toString().split("\\s*,\\s*")); //only the first selected item is modified
        String geboortedatum = items.get(0).substring(1);
        String naam = items.get(2);
        String voornaam = items.get(1);
        ArrayList<String> inputData = createJPanel(items);
        int gekozenFunctieID = 999;
        for (int i = 0; i < functieList.size(); i++){
            if (functieList.get(i).getFunctie().equals(inputData.get(5))){
                gekozenFunctieID = i+1;
            }
        }
        String insertQuery = "UPDATE Medewerker SET " +
                " geboorteDatum ='" + inputData.get(0) +
                "' , voornaam='" + inputData.get(1) +
                "' , naam='" + inputData.get(2) +
                "' , sex='" + inputData.get(3) +
                "' , datumTewerkstelling='" + inputData.get(4) +
                "' , functieId='" + String.valueOf(gekozenFunctieID) +
                "' , telefoonnummer='" + inputData.get(6) +
                "' , eMail='" + inputData.get(7) +
                "' , gemeente='" + inputData.get(8) +
                "' , straatEnNr='" +inputData.get(9) +
                "' WHERE geboorteDatum= '" + geboortedatum + "' AND naam= '"+ naam + "' AND voornaam= '"+ voornaam +"'";
        if(checkInput(inputData)){
            ConnectionManager.handle.execute(insertQuery);
            tblConfigs.getItems().clear();
            getMedewerkerList();
            initTable(medewerkerList);
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
            showAlert("Hela!", "Eerst een record selecteren hee.");
        }
    }

    private ArrayList<String> createJPanel(List<String> items){
        JXDatePicker geboortedatum = new JXDatePicker();
        JTextField voornaam = new JTextField(5);
        JTextField naam = new JTextField(5);
        String[] geslactKeuzes = {"M", "F", "X"};
        JComboBox sex = new JComboBox<String>(geslactKeuzes);
        JXDatePicker werkDatum = new JXDatePicker();
        JTextField telefoonnummer = new JTextField(5);
        JTextField eMail = new JTextField(5);
        JTextField gemeente = new JTextField(5);
        JTextField straatEnNummer = new JTextField(5);

        geboortedatum.setDate(Calendar.getInstance().getTime());
        geboortedatum.setFormats(new SimpleDateFormat("dd/MM/yyyy"));
        werkDatum.setDate(Calendar.getInstance().getTime());
        werkDatum.setFormats(new SimpleDateFormat("dd/MM/yyyy"));

        List<Functie> functieList = ConnectionManager.handle.createQuery("SELECT * FROM Functie")
                .mapToBean(Functie.class)
                .list();
        String[] choices = new String[functieList.size()];
        for(int i = 0 ; i < functieList.size(); i++){
            choices[i] = functieList.get(i).toString().replace("Functie{functie'","").replace("}","");
        }
        final JComboBox<String> functie = new JComboBox<String>(choices);

        if (items != null){ // if an item is selected, automatically pre-fill boxes
            voornaam.setText(items.get(1));
            naam.setText(items.get(2));
            sex.setSelectedItem(items.get(3));
            functie.setSelectedItem(items.get(5));
            telefoonnummer.setText(items.get(6));
            eMail.setText(items.get(7));
            gemeente.setText(items.get(8));
            straatEnNummer.setText(items.get(9).substring(0, items.get(9).length() - 1));
        }
        Object[] message = { "Geboortedatum: ", geboortedatum,
                "Voornaam: ", voornaam,
                "Naam: ", naam,
                "Geslacht: ", sex,
                "Datum Tewerkstelling: ", werkDatum,
                "Functie", functie,
                "Telefoon: ", telefoonnummer,
                "E-mail: ", eMail,
                "Gemeente: ", gemeente,
                "Straat + nr: ", straatEnNummer};
        String[] buttons = { "Save", "Cancel" };

        int option = JOptionPane.showOptionDialog(null, message, "Add Medewerker", JOptionPane.OK_CANCEL_OPTION, 0, null, buttons, buttons[0]);

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String geboorteDatumFormatted = format.format(geboortedatum.getDate());
        String werkDatumFormatted = format.format(werkDatum.getDate());
        ArrayList<String> r = new ArrayList();

        r.add(geboorteDatumFormatted);
        r.add(voornaam.getText());
        r.add(naam.getText());
        r.add(sex.getSelectedItem().toString());
        r.add(werkDatumFormatted);
        r.add(functie.getSelectedItem().toString());
        r.add(telefoonnummer.getText());
        r.add(eMail.getText());
        r.add(gemeente.getText());
        r.add(straatEnNummer.getText());
        return r;
    }
}
