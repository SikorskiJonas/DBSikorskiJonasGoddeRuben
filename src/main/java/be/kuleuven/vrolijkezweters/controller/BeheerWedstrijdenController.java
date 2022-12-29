package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.model.Categorie;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BeheerWedstrijdenController {
    private Jdbi jdbi;
    private List<Wedstrijd> wedstrijdList;
    private Handle h;

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
        connectDatabase();
        getWedstrijdList();
        initTable(wedstrijdList);
        //btnAdd.setOnAction(e -> showVoegToeScherm());
        btnAdd.setOnAction(e -> addNewRow());
        btnModify.setOnAction(e -> {
            verifyOneRowSelected();
            modifyCurrentRow();
        });
        btnDelete.setOnAction(e -> {
            verifyOneRowSelected();
            deleteCurrentRow();
        });
        
        btnClose.setOnAction(e -> {
            h.close();
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
    }

    public void connectDatabase() {
        jdbi = Jdbi.create("jdbc:sqlite:databaseJonasRuben.db");
        h = jdbi.open();
        System.out.println("Connected to database");
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
            tblConfigs.getItems().add(FXCollections.observableArrayList(wedstrijd.getNaam(), wedstrijd.getDatum(), wedstrijd.getPlaats(), wedstrijd.getInschrijvingsgeld(), wedstrijd.getCategorieId()));
        }
    }

    private void getWedstrijdList(){
        wedstrijdList = h.createQuery("SELECT * FROM Wedstrijd")
                .mapToBean(Wedstrijd.class)
                .list();
        //fetch list of categorieën
        List<Categorie> categorieList = h.createQuery("SELECT * FROM Categorie")
                .mapToBean(Categorie.class)
                .list();
        //convert categorieID's to their categories
        for (Wedstrijd wedstrijd : wedstrijdList) {
            String categorieId = wedstrijd.getCategorieId();
            int categorieIdInt = Integer.parseInt(categorieId);
            String categorie = categorieList.get(categorieIdInt - 1).getCategorie();
            wedstrijd.setCategorieId(categorie);
        }
    }

    private void addNewRow() {
        JTextField naam = new JTextField(5);
        JXDatePicker picker = new JXDatePicker();
        JTextField plaats = new JTextField(8);
        JTextField inschrijvingsGeld = new JTextField(5);
        JTextField categorie = new JTextField(5);

        picker.setDate(Calendar.getInstance().getTime());
        picker.setFormats(new SimpleDateFormat("dd/MM/yyyy"));

        List<Categorie> categorieList = h.createQuery("SELECT * FROM Categorie")
                .mapToBean(Categorie.class)
                .list();
        String[] choices = new String[categorieList.size()];
        for(int i = 0 ; i < categorieList.size(); i++){
            choices[i] = categorieList.get(i).toString().replace("Categorie{categorie'","").replace("}","");
        }
        final JComboBox<String> category = new JComboBox<String>(choices);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("naam:"));
        myPanel.add(naam);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("datum:"));
        myPanel.add(picker);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("plaats:"));
        myPanel.add(plaats);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("inschrijvingsgeld:"));
        myPanel.add(inschrijvingsGeld);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("categorie:"));
        myPanel.add(category);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Please Enter Values", JOptionPane.OK_CANCEL_OPTION);

        Date date = picker.getDate();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateFormatted = format.format(date);

        tblConfigs.getItems().add(FXCollections.observableArrayList(naam.getText(), dateFormatted, plaats.getText(), inschrijvingsGeld.getText(), category.getSelectedItem()));
        int cIndex = category.getSelectedIndex() + 1;
        h.execute("INSERT INTO wWdstrijd (naam, datum, plaats, inschrijvingsgeld, categorieid) values ('" +
                naam.getText() +"', '"+
                dateFormatted+"', '"+
                plaats.getText()+"', '"+
                inschrijvingsGeld.getText()+"', '"+
                cIndex+"')");
    }

    private void deleteCurrentRow() {
        List<Object> selectedItems = tblConfigs.getSelectionModel().getSelectedItems();
        System.out.println(selectedItems);
        for (int i = 0; i < selectedItems.size(); i++) {
            List<String> items = Arrays.asList(selectedItems.get(i).toString().split("\\s*,\\s*"));
            String naamI = items.get(0).substring(1);
            String datumI = items.get(1);
            String q = "DELETE FROM Wedstrijd WHERE datum = '" + datumI +"' AND naam = '"+ naamI +"'";
            System.out.println(q);
            h.execute(q);
            tblConfigs.getItems().clear();
            initialize();
        }
    }


    private void modifyCurrentRow() {
        JTextField naam = new JTextField(5);
        JXDatePicker picker = new JXDatePicker();
        JTextField plaats = new JTextField(8);
        JTextField inschrijvingsGeld = new JTextField(5);

        List<Object> selectedItems = tblConfigs.getSelectionModel().getSelectedItems();
        int index = tblConfigs.getSelectionModel().getFocusedIndex();
        List<String> items = new ArrayList<String>();
        for (int i = 0; i < selectedItems.size(); i++) {
            items = Arrays.asList(selectedItems.get(i).toString().replace("[", "").replace("]","").split("\\s*,\\s*"));
        }
        naam.setText(items.get(0));
        plaats.setText(items.get(2));
        inschrijvingsGeld.setText(items.get(3));

        picker.setDate(Calendar.getInstance().getTime());
        picker.setFormats(new SimpleDateFormat("dd/MM/yyyy"));

        List<Categorie> categorieList = h.createQuery("SELECT * FROM Categorie")
                .mapToBean(Categorie.class)
                .list();
        String[] choices = new String[categorieList.size()];
        for(int i = 0 ; i < categorieList.size(); i++){
            choices[i] = categorieList.get(i).toString().replace("Categorie{categorie'","").replace("}","");
        }
        final JComboBox<String> category = new JComboBox<String>(choices);
        category.setSelectedItem(items.get(4));

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("naam:"));
        myPanel.add(naam);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("datum:"));
        myPanel.add(picker);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("plaats:"));
        myPanel.add(plaats);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("inschrijvingsgeld:"));
        myPanel.add(inschrijvingsGeld);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("categorie:"));
        myPanel.add(category);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Please Enter Values", JOptionPane.OK_CANCEL_OPTION);

        Date date = picker.getDate();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateFormatted = format.format(date);


        tblConfigs.getItems().add(index, FXCollections.observableArrayList(naam.getText(), dateFormatted, plaats.getText(), inschrijvingsGeld.getText(), category.getSelectedItem()));
        tblConfigs.getItems().remove(index+1);
        int cIndex = category.getSelectedIndex() + 1;
        h.execute("UPDATE Wedstrijd SET " +
                "naam = '" + naam.getText() + "', " +
                "datum = '" + dateFormatted + "', " +
                "plaats = '" + plaats.getText() + "', " +
                "inschrijvingsgeld = '" + inschrijvingsGeld.getText() + "', " +
                "categorieId = '" + cIndex + "' " +
                "WHERE naam = '" + items.get(0) + "'");
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
}
