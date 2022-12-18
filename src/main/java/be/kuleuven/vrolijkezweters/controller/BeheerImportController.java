package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.ImportFacade;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import javax.swing.*;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JFileChooser;


public class BeheerImportController {
    private Jdbi jdbi;
    private Handle h;
    private String importChoise;
    private ImportFacade importFacade;
    private int numberOfColumns;
    private List<String> columns;

    @FXML
    private ChoiceBox btnChooseModel;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnImportExcel;
    @FXML
    private TableView tblConfigs;

    public void initialize(){
        connectDatabase();

        btnChooseModel.getItems().addAll("Wedstrijd", "Loper", "Medewerker", "Etappe", "LoopNummer");

        btnChooseModel.setOnAction(e -> chooseModel());
        btnImportExcel.setOnAction(e -> importExcel());
        btnClose.setOnAction(e -> {
            h.close();
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
    }

    public void connectDatabase(){
        jdbi = Jdbi.create("jdbc:sqlite:databaseJonasRuben.db");
        h = jdbi.open();
        System.out.println("Connected to database");
    }

    private void chooseModel(){
        importChoise = btnChooseModel.getSelectionModel().getSelectedItem().toString();
        List<String> columns = new ArrayList<String>();
        switch(importChoise){
            case "Wedstrijd":
                Collections.addAll(columns, "naam", "datum", "plaats", "inschrijvingsgeld", "categorieId");
                break;
            case "Loper":
                Collections.addAll(columns, "geboorteDatum", "voornaam", "naam", "sex", "lengte", "telefoonNummer", "email", "gemeente", "straatplusnr");
                break;
            case "Medewerker":
                Collections.addAll(columns, "geboorteDatum", "voornaam", "naam", "sex", "datumTewerkstelling", "functieId", "telefoonNummer", "email", "gemeente", "straatplusnr");
                break;
            case "Etappe":
                Collections.addAll(columns, "afstandMeter", "startPlaats", "eindPlaats", "wedstrijdId", "naam");
                break;
            case "LoopNummer":
                Collections.addAll(columns, "nummer", "loopTijd", "loperId", "etappeId");
                break;
        }
        //TODO dit nog goed implementeren niet globaal!!!!!!!!!!!!!!!!!!!!!
        this.columns = columns;
        numberOfColumns = columns.size();
        initTable(columns, columns.size());
    }

    private void initTable(List<String> columns, int lenght) {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblConfigs.getColumns().clear();

        int colIndex = 0;

        String[] columnss = columns.toArray(new String[0]);
        for(var colName : columnss) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }

    }

    public void importExcel(){
        if(importChoise == null){
            JOptionPane.showMessageDialog(null, "Please select import model", "error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String fileName = "";
        List<Object> objects = new ArrayList<Object>();

        JFileChooser file = new JFileChooser();
        file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        file.setFileHidingEnabled(false);

        if(file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            java.io.File f = file.getSelectedFile();
            System.err.println(f.getPath());
            fileName = f.getPath();
        }
        System.out.println(fileName);

        if(fileName!=null){
            try{
                FileInputStream ins = new FileInputStream(fileName);
                XSSFWorkbook wb = new XSSFWorkbook(ins);
                XSSFSheet sheet = wb.getSheetAt(0);

                Iterator rows = sheet.rowIterator();

                List<String> models = new ArrayList<String>();

                int columns = 9;
                String format = "";

                try{
                    if(rows.hasNext()){
                        XSSFRow row = (XSSFRow) rows.next();
                        int columnIndex = 0;
                        while(columnIndex < columns){
                            XSSFCell cell = row.getCell(columnIndex);
                            if(cell.getStringCellValue() != null) { format += cell.getStringCellValue();}
                            columnIndex++;
                        }
                    }
                    if(format.equals("blablalba")){
                        ins.close();
                        throw new Exception("wrong or missing input columns");
                    }
                }catch ( Exception e){
                    throw new Exception("wrong or missing input columns");
                }
                while(rows.hasNext()){
                    String defaultObject = "";
                    XSSFRow row = (XSSFRow) rows.next();
                    Iterator cells = row.cellIterator();
                    int columnIndex =0;
                    while(columnIndex< columns){
                        XSSFCell cell = row.getCell(columnIndex);
                        if(cell == null){
                            columnIndex++;
                            continue;
                        }
                        try{ defaultObject += cell.getStringCellValue() + ",,,";} catch ( Exception e){}
                        try{
                            Date date = cell.getDateCellValue();
                            DateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
                            String dateFormatted = formatDate.format(date);
                            defaultObject += dateFormatted + ",,,";
                        }
                        catch ( Exception e){
                            try{ defaultObject += cell.getNumericCellValue() + ",,,";} catch ( Exception ee){}
                        }

                        columnIndex++;
                    }
                    System.out.println(defaultObject);

                    //todo check for correct input
                    //if(blabla){}


                }
                ins.close();

                addToTable(models);
                importFacade.SaveToDb(objects);

            }
            catch( Exception e){

            }
        }
    }

    public void addToTable(List<String> models){
        for(String m : models){
            //TODO
        }
    }

}
