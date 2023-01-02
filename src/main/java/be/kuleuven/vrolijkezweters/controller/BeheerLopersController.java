package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.InputChecker;
import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.jdbc.ConnectionManager;
import be.kuleuven.vrolijkezweters.model.Loper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class BeheerLopersController {
    List<Loper> loperList;
    InputChecker inputChecker = new InputChecker();

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
        getLoperList();
        initTable(loperList);
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

    private void initTable(List<Loper> loperList) {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblConfigs.getColumns().clear();
        int colIndex = 0;
        for(var colName : new String[]{"GeboorteDatum", "VoorNaam", "Naam", "Sex", "Lengte", "telefoonNummer", "E-mail", "Gemeente", "Straat + nr"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }
        for (Loper loper : loperList) {
            tblConfigs.getItems().add(FXCollections.observableArrayList(loper.getGeboorteDatum(), loper.getVoornaam(), loper.getNaam(), loper.getSex(), loper.getLengte(), loper.getTelefoonNummer(), loper.getEmail(), loper.getGemeente(), loper.getStraatEnNr()));
        }
    }

    public void getLoperList(){
        System.out.println("fetching list of lopers");
            loperList = ConnectionManager.handle.createQuery("SELECT * FROM Loper")
                    .mapToBean(Loper.class)
                    .list();
    }

    private void addNewRow() {
        ArrayList<String> inputData = createJPanel(null, "add");
        String insertQuery = "INSERT INTO loper (geboorteDatum, voornaam, naam, sex, lengte, telefoonnummer, eMail, gemeente, straatEnNr, wachtwoord) values ('" +
                inputData.get(0) +"', '" + inputData.get(1) +"', '" + inputData.get(2) +"', '" + inputData.get(3) +"', '" + inputData.get(4) +"', '" + inputData.get(5) +"', '" + inputData.get(6) +"', '" + inputData.get(7) +"', '" + inputData.get(8) +"', '" + inputData.get(9) +"')";
        if(inputChecker.checkInput(inputData, "Loper")){
            ConnectionManager.handle.execute(insertQuery);
            tblConfigs.getItems().clear();
            getLoperList();
            initTable(loperList);
        }
        else{
            showAlert("Input error", "De ingegeven data voldoet niet aan de constraints");
        }

    }

    private void deleteCurrentRow(List<Object> selectedItems) {
        for (Object selectedItem : selectedItems) {
            List<String> items = Arrays.asList(selectedItem.toString().split("\\s*,\\s*"));
            String geboortedatumI = items.get(0).substring(1);
            String naamI = items.get(2);
            String voornaamI = items.get(1);
            String eMailI = items.get(6);
            String deleteLoper = "DELETE FROM Loper WHERE geboortedatum = '" + geboortedatumI + "' AND voornaam = '" + voornaamI + "' AND naam = '" + naamI + "'";
            ConnectionManager.handle.execute(deleteLoper);
            tblConfigs.getItems().clear();
            getLoperList();
            initTable(loperList);
        }
    }

    private void modifyCurrentRow(List<Object> selectedItems) {
        List<String> items = Arrays.asList(selectedItems.get(0).toString().split("\\s*,\\s*")); //only the first selected item is modified
        String geboortedatum = items.get(0).substring(1);
        String naam = items.get(2);
        String voornaam = items.get(1);
        ArrayList<String> inputData = createJPanel(items, "modify");
        String insertQuery = "UPDATE Loper SET " +
                " geboortedatum = '" + inputData.get(0) +
                "' , voornaam= '" + inputData.get(1) +
                "' , naam= '" + inputData.get(2) +
                "' , sex= '" + inputData.get(3) +
                "' , lengte= '" + inputData.get(4) +
                "' , telefoonnummer= '" + inputData.get(5) +
                "' , eMail= '" + inputData.get(6) +
                "' , gemeente= '" + inputData.get(7) +
                "' , straatEnNr= '" +inputData.get(8)   +
                "' WHERE geboorteDatum= '" + geboortedatum + "' AND naam= '"+ naam + "' AND voornaam= '"+ voornaam + "';";
        if(inputChecker.checkInput(inputData, "Loper")){
            ConnectionManager.handle.execute(insertQuery);
            tblConfigs.getItems().clear();
            getLoperList();
            initTable(loperList);
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

    private ArrayList<String> createJPanel(List<String> items, String operation){
        JXDatePicker geboortedatum = new JXDatePicker();
        JTextField voornaam = new JTextField(5);
        JTextField naam = new JTextField(5);
        String[] geslactKeuzes = {"M", "F", "X"};
        JComboBox sex = new JComboBox<String>(geslactKeuzes);
        JTextField lengte = new JTextField(3);
        JTextField telefoonnummer = new JTextField(5);
        JTextField eMail = new JTextField(5);
        JTextField gemeente = new JTextField(5);
        JTextField straatEnNummer = new JTextField(5);
        String wachtwoord = "";

        geboortedatum.setDate(Calendar.getInstance().getTime());
        geboortedatum.setFormats(new SimpleDateFormat("dd/MM/yyyy"));

        if (items != null){ // if an item is selected, automatically pre-fill boxes
            voornaam.setText(items.get(1));
            naam.setText(items.get(2));
            sex.setSelectedItem(items.get(3));
            lengte.setText(items.get(4));
            telefoonnummer.setText(items.get(5));
            eMail.setText(items.get(6));
            gemeente.setText(items.get(7));
            straatEnNummer.setText(items.get(8).substring(0, items.get(8).length() - 1));
        }
        String[] buttons = { "Save", "Cancel" };

        if(operation.equals("add")){
            wachtwoord = generatePassword();
            Object[] message = { "Geboortedatum: ", geboortedatum,
                    "Voornaam: ", voornaam,
                    "Naam: ", naam,
                    "Geslacht: ", sex,
                    "Lengte: ", lengte,
                    "Telefoon: ", telefoonnummer,
                    "E-mail: ", eMail,
                    "Gemeente: ", gemeente,
                    "Straat + nr: ", straatEnNummer,
                    "Generated Wachtwoord:", wachtwoord};
            int option = JOptionPane.showOptionDialog(null, message, "Add Loper", JOptionPane.OK_CANCEL_OPTION, 0, null, buttons, buttons[0]);

        }
        else if(operation.equals("modify")){
            Object[] message = { "Geboortedatum: ", geboortedatum,
                    "Voornaam: ", voornaam,
                    "Naam: ", naam,
                    "Geslacht: ", sex,
                    "Lengte: ", lengte,
                    "Telefoon: ", telefoonnummer,
                    "E-mail: ", eMail,
                    "Gemeente: ", gemeente,
                    "Straat + nr: ", straatEnNummer};
            int option = JOptionPane.showOptionDialog(null, message, "Add Loper", JOptionPane.OK_CANCEL_OPTION, 0, null, buttons, buttons[0]);

        }


        Date date = geboortedatum.getDate();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateFormatted = format.format(date);
        ArrayList<String> r = new ArrayList();
        r.add(dateFormatted);
        r.add(voornaam.getText());
        r.add(naam.getText());
        r.add(sex.getSelectedItem().toString());
        r.add(lengte.getText());
        r.add(telefoonnummer.getText());
        r.add(eMail.getText());
        r.add(gemeente.getText());
        r.add(straatEnNummer.getText());
        r.add(wachtwoord);
        return r;
    }

    public String generatePassword(){
        char[] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRST".toCharArray();

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String randomStr = sb.toString();

        return randomStr;
    }
}
