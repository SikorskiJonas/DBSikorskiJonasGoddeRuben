package be.kuleuven.vrolijkezweters;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DB Taak 2022-2023: De Vrolijke Zweters
 * Zie https://kuleuven-diepenbeek.github.io/db-course/extra/project/ voor opgave details
 *
 * Deze code is slechts een quick-start om je op weg te helpen met de integratie van JavaFX tabellen en data!
 * Zie README.md voor meer informatie.
 */
public class ProjectMain extends Application {

    private static Stage rootStage;
    public Connection connection;
    public static Stage getRootStage() {
        return rootStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        connectDatabase();
        rootStage = stage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("De Vrolijke Zweters Administratie hoofdscherm");
        stage.setScene(scene);
        stage.show();
    }
    public void connectDatabase() throws SQLException {
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:databaseJonasRuben.db");
            var s = connection.createStatement();
            System.out.println("Connected to database");
        }catch(SQLException throwables){
            throw new SQLException("Kan niet verbinden met database");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
