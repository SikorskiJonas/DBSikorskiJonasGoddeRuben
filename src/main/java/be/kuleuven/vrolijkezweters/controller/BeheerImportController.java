package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.ImportExcel;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class BeheerImportController {
    private final ImportExcel importFacade = new ImportExcel();
    private String importChoise;
    private int numberOfColumns;

    @FXML
    private ChoiceBox<String> btnChooseModel;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnImportExcel;
    @FXML
    private Button btnSave;
    @FXML
    private TableView tblConfigs;

    public void initialize() {
        btnChooseModel.getItems().addAll("Wedstrijd", "Loper", "Medewerker", "Etappe", "LoopNummer");

        btnChooseModel.setOnAction(e -> chooseModel());
        btnImportExcel.setOnAction(e -> importExcel());
        btnSave.setOnAction(e -> saveImport());
        btnClose.setOnAction(e -> {
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
    }

    private void chooseModel() {
        importChoise = btnChooseModel.getSelectionModel().getSelectedItem();
        List<String> columns = new ArrayList<>();
        switch (importChoise) {
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
        numberOfColumns = columns.size();
        initTable(columns);
    }

    private void initTable(List<String> columns) {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblConfigs.getColumns().clear();
        tblConfigs.getItems().clear();

        int colIndex = 0;

        String[] columnss = columns.toArray(new String[0]);
        for (var colName : columnss) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }

    }

    public void importExcel() {
        if (importChoise == null) {
            JOptionPane.showMessageDialog(null, "Please select import model", "error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String fileName = "";

        JFileChooser file = new JFileChooser();
        file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        file.setFileHidingEnabled(false);

        if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            java.io.File f = file.getSelectedFile();
            System.err.println(f.getPath());
            fileName = f.getPath();
        }
        System.out.println(fileName);

        try {
            FileInputStream ins = new FileInputStream(fileName);
            XSSFWorkbook wb = new XSSFWorkbook(ins);
            XSSFSheet sheet = wb.getSheetAt(0);

            Iterator<Row> rows = sheet.rowIterator();

            StringBuilder format = new StringBuilder();

            try {
                if (rows.hasNext()) {
                    XSSFRow row = (XSSFRow) rows.next();
                    int columnIndex = 0;
                    while (columnIndex < numberOfColumns) {
                        XSSFCell cell = row.getCell(columnIndex);
                        if (cell.getStringCellValue() != null) {
                            format.append(cell.getStringCellValue());
                        }
                        columnIndex++;
                    }
                }
                if (format.toString().equals("blablalba")) {
                    ins.close();
                    throw new Exception("wrong or missing input columns");
                }
            } catch (Exception e) {
                throw new Exception("wrong or missing input columns");
            }
            while (rows.hasNext()) {

                List<String> strings = new ArrayList<>();

                XSSFRow row = (XSSFRow) rows.next();
                int columnIndex = 0;

                while (columnIndex < numberOfColumns) {
                    XSSFCell cell = row.getCell(columnIndex);
                    if (cell == null) {
                        columnIndex++;
                        continue;
                    }
                    try {
                        strings.add(cell.getStringCellValue());
                    } catch (Exception ignored) {
                    }
                    try {
                        strings.add(cell.getNumericCellValue() + "");

                    } catch (Exception ee) {
                        try {
                            Date date = cell.getDateCellValue();
                            DateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
                            String dateFormatted = formatDate.format(date);
                            strings.add(dateFormatted + "");
                        } catch (Exception ignored) {
                        }
                    }

                    columnIndex++;
                }
                tblConfigs.getItems().add(FXCollections.observableArrayList(strings));

                //todo check for correct input
                //if(blabla){}


            }
            ins.close();

        } catch (Exception ignored) {

        }
    }

    public void saveImport() {
        //TODO check if table is imported doesnt work
        if (tblConfigs.getItems() == null) {
            JOptionPane.showMessageDialog(null, "Please import excel file", "error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        tblConfigs.getItems().clear();
        importFacade.SaveToDb(importChoise, tblConfigs.getItems());
    }

}
