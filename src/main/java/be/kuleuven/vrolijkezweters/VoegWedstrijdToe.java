package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.model.Wedstrijd;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

public class VoegWedstrijdToe {
    private Handle h;
    private Jdbi jdbi;
    /**
     * We moeten kijken of het mogelijk is om ipv hier ook met de database te connecten, om gwn de lijst van wedstrijden
     * en categorieen mee te geven als deze klasse gestart wordt
     */

    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField naamTxtField;
    @FXML
    private TextField datumTxtField;
    @FXML
    private TextField plaatsTxtField;
    @FXML
    private TextField inschrijfgeldTxtField;
    @FXML
    private ComboBox categorieComboBox;

    public void initialize(){
        connectDatabase();
        btnCancel.setOnAction(e -> {
            var stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        });
        btnSave.setOnAction(e -> {
            Wedstrijd w = readContent();
            saveContent(w);
        });

    }
    public void connectDatabase() {
        jdbi = Jdbi.create("jdbc:sqlite:databaseJonasRuben.db");
        h = jdbi.open();
        System.out.println("Connected to database");
    }

    private Wedstrijd readContent(){
        Wedstrijd w = new Wedstrijd();
        w.setNaam(naamTxtField.getText());
        w.setDatum(datumTxtField.getText());
        w.setPlaats(plaatsTxtField.getText());
        w.setInschrijvingsgeld(inschrijfgeldTxtField.getText());
        w.setCategorieId("2");
        return w;
    }

    private void saveContent(Wedstrijd w){
        h.execute("INSERT INTO wedstrijd (Naam, Datum, Plaats, Inschrijvingsgeld, CategorieId) values ('" +
                w.getNaam() +"', '"+
                w.getDatum()+"', '"+
                w.getPlaats()+"', '"+
                w.getInschrijvingsgeld()+"', '"+
                w.getCategorieId()+"')");
        var stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

}
