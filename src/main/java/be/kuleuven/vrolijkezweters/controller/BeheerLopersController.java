package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.model.Categorie;
import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Wedstrijd;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BeheerLopersController {
    private Jdbi jdbi;
    private Handle handle;
    private List<Loper> loperList;

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
        connectDatabase();
        loperList = getLoperList();
        initTable(loperList);
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
            handle.close();
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
    }

    public void connectDatabase(){
        jdbi = Jdbi.create("jdbc:sqlite:databaseJonasRuben.db");
        handle = jdbi.open();
        System.out.println("Connected to database");
    }

    private void initTable(List<Loper> loperList) {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblConfigs.getColumns().clear();

        int colIndex = 0;

        for(var colName : new String[]{"GeboorteDatum", "VoorNaam", "Naam", "Sex", "Lengte", "telefoonNummer", "Email", "Gemeente", "Straat + nr"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }

        for(int i = 0; i < loperList.size(); i++) {
            tblConfigs.getItems().add(FXCollections.observableArrayList(loperList.get(i).getGeboorteDatum(), loperList.get(i).getVoornaam(), loperList.get(i).getNaam(), loperList.get(i).getSex(), loperList.get(i).getLengte(), loperList.get(i).getTelefoonNummer(), loperList.get(i).getEmail(), loperList.get(i).getGemeente(), loperList.get(i).getStraatplusnr()));
        }
    }
    public List<Loper> getLoperList(){
        System.out.println("fetching list of lopers");

        return jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT * FROM loper")
                    .mapToBean(Loper.class)
                    .list();
        });
    }

    private void addNewRow() {

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

        geboortedatum.setDate(Calendar.getInstance().getTime());
        geboortedatum.setFormats(new SimpleDateFormat("dd/MM/yyyy"));

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("geboorteDatum:"));
        myPanel.add(geboortedatum);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("voornaam:"));
        myPanel.add(voornaam);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("naam:"));
        myPanel.add(naam);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("geslacht:"));
        myPanel.add(sex);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("lengte:"));
        myPanel.add(lengte);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("telefoon:"));
        myPanel.add(telefoonnummer);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("E-mail:"));
        myPanel.add(eMail);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("gemeente:"));
        myPanel.add(gemeente);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("straat + nr:"));
        myPanel.add(straatEnNummer);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Please Enter Values", JOptionPane.OK_CANCEL_OPTION);

        Date date = geboortedatum.getDate();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateFormatted = format.format(date);

        tblConfigs.getItems().add(FXCollections.observableArrayList(dateFormatted, voornaam.getText(),  naam.getText(), sex.getSelectedItem(), lengte.getText(), telefoonnummer.getText(), eMail.getText(), gemeente.getText(), straatEnNummer.getText()));
        handle.execute("INSERT INTO loper (GeboorteDatum, Voornaam, Naam, Sex, Lengte, Telefoonnummer, 'E-mail', Gemeente, 'Straat + nr') values ('" +
                dateFormatted+"', '"+
                voornaam.getText() +"', '"+
                naam.getText() +"', '"+
                sex.getSelectedItem() +"', '"+
                lengte.getText()+"', '"+
                telefoonnummer.getText()+"', '"+
                eMail.getText()+"', '"+
                gemeente.getText()+"', '"+
                straatEnNummer.getText()+"')");
    }

    private void deleteCurrentRow() {
        List<Object> selectedItems = tblConfigs.getSelectionModel().getSelectedItems();
        for (int i = 0; i < selectedItems.size(); i++) {
            List<String> items = Arrays.asList(selectedItems.get(i).toString().split("\\s*,\\s*"));
            String geboortedatumI = items.get(0).substring(1);
            String naamI = items.get(2);
            String voornaamI = items.get(1);
            String q = "DELETE FROM loper WHERE GeboorteDatum = '" + geboortedatumI +"' AND Voornaam = '"+ voornaamI +"' AND Naam = '" + naamI +"'" ;
            System.out.println(q);
            handle.execute(q);
            tblConfigs.getItems().clear();
            initialize();
        }
    }

    private void modifyCurrentRow() {
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
