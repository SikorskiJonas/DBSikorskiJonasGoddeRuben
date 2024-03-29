package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.JPanelFactory;
import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.jdbi.FunctieDao;
import be.kuleuven.vrolijkezweters.jdbi.LoperDao;
import be.kuleuven.vrolijkezweters.jdbi.MedewerkerDao;
import be.kuleuven.vrolijkezweters.model.Functie;
import be.kuleuven.vrolijkezweters.model.Loper;
import be.kuleuven.vrolijkezweters.model.Medewerker;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.swing.*;
import java.util.List;

public class BeheerAccountController {

    final MedewerkerDao medewerkerDao = new MedewerkerDao();
    final LoperDao loperDao = new LoperDao();

    public Object modifyUserInfo(Object user) {
        FunctieDao functieDao = new FunctieDao();
        JPanelFactory jPanelFactory = new JPanelFactory();
        if (user.getClass() == Medewerker.class) {
            Medewerker medewerkerOud = (Medewerker) user;
            user = jPanelFactory.createJPanel(user, "modify", BeheerMedewerkersController.class);
            List<Functie> functieList = functieDao.getAll();
            for (int i = 0; i < functieList.size(); i++) {
                if (functieList.get(i).getFunctie().equals(((Medewerker) user).getFunctieId())) {
                    ((Medewerker) user).setFunctieId(String.valueOf(i + 1));
                }
            }
            medewerkerDao.update((Medewerker) user, medewerkerOud);
            return user;
        }
        if (user.getClass() == Loper.class) {
            Loper loperOld = (Loper) user;
            user = jPanelFactory.createJPanel(user, "modify", BeheerLopersController.class);
            loperDao.update((Loper) user, loperOld);
            return user;
        }
        return null;
    }

    public void deleteAccount(Object user, Window w) {
        int option2 = JOptionPane.showConfirmDialog(null, "Are u sure u want to delete your account?", "Register", JOptionPane.OK_CANCEL_OPTION);
        if (option2 == JOptionPane.OK_OPTION) {
            if (user.getClass() == Medewerker.class) {
                medewerkerDao.delete((Medewerker) user);
            }
            if (user.getClass() == Loper.class) {
                loperDao.delete((Loper) user);
            }
            showAlert("Succes", "Account succesfully deleted!");
            ProjectMain projectMain = new ProjectMain();
            try {
                ((Stage) w).close();
                projectMain.start(projectMain.getRootStage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void logOut(Window w) {
        ProjectMain projectMain = new ProjectMain();
        try {
            ((Stage) w).close();
            projectMain.start(projectMain.getRootStage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
