package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.jdbi.ConnectionManager;
import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Medewerker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.Objects;


public class ProjectMainController {
    public static final ConnectionManager connectionManager = new ConnectionManager();
    public static Object user;
    final BeheerAccountController accountController = new BeheerAccountController();
    public MenuItem btnAccountGeneral;
    public MenuItem btnAccountDelete;
    public MenuItem btnAccountLogout;

    @FXML
    private Button btnWedstrijden;
    @FXML
    private Button btnBeheerLopers;
    @FXML
    private Button btnBeheerMedewerkers;
    @FXML
    private Button btnKlassement;
    @FXML
    private Button btnImport;
    @FXML
    private Button btnMijnWedstrijden;
    @FXML
    private MenuButton btnAccount;
    @FXML
    private AnchorPane contentPane;
    @FXML
    private Text txtUser;

    public void initialize() {
        if (!ProjectMain.isAdmin) {
            btnBeheerLopers.setVisible(false);
            btnBeheerMedewerkers.setVisible(false);
            btnImport.setVisible(false);
        }
        btnWedstrijden.setOnAction(e -> showBeheerScherm("wedstrijden", btnWedstrijden));
        btnBeheerLopers.setOnAction(e -> showBeheerScherm("lopers", btnBeheerLopers));
        btnBeheerMedewerkers.setOnAction(e -> showBeheerScherm("medewerkers", btnBeheerMedewerkers));
        btnKlassement.setOnAction(e -> showBeheerScherm("klassement", btnKlassement));
        btnImport.setOnAction(e -> showBeheerScherm("import", btnImport));
        btnMijnWedstrijden.setOnAction(e -> showBeheerScherm("mijnwedstrijden", btnMijnWedstrijden));
        btnAccountGeneral.setOnAction(e -> user = accountController.modifyUserInfo(user));
        btnAccountDelete.setOnAction(e -> accountController.deleteAccount(user, btnAccount.getScene().getWindow()));
        btnAccountLogout.setOnAction(e -> accountController.logOut(btnAccount.getScene().getWindow()));
    }

    private void showBeheerScherm(String id, Button button) {
        var resourceName = "beheer" + id + ".fxml";
        try {
            contentPane.getChildren().clear();
            AnchorPane content;
            setButtonColors(button);
            content = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("beheer" + id + ".fxml")));
            contentPane.getChildren().add(content);

        } catch (Exception e) {
            throw new RuntimeException("Kan beheerscherm " + resourceName + " niet vinden", e);
        }
    }

    private void setButtonColors(Button button) {
        btnWedstrijden.setStyle("-fx-background-color:  #37beb0");
        btnBeheerLopers.setStyle("-fx-background-color:  #37beb0");
        btnBeheerMedewerkers.setStyle("-fx-background-color:  #37beb0");
        btnKlassement.setStyle("-fx-background-color:  #37beb0");
        btnImport.setStyle("-fx-background-color:  #37beb0");
        btnMijnWedstrijden.setStyle("-fx-background-color:  #37beb0");
        button.setStyle("-fx-background-color:  #298F84");
    }

    public void setUser(Object user) {
        ProjectMainController.user = user;
        if (user.getClass() == Loper.class) {
            txtUser.setText("Logged in as " + ((Loper) user).getVoornaam() + " " + ((Loper) user).getNaam());
        }
        if (user.getClass() == Medewerker.class) {
            txtUser.setText("Logged in as " + ((Medewerker) user).getVoornaam() + " " + ((Medewerker) user).getNaam());
        }
    }
}
