package project.controllers.management;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.dao.LakeDAO;
import project.models.Lake;

import java.sql.*;

public class AddLakeController {

    @FXML private TextField txtName;
    @FXML private TextField txtLocation;
    @FXML private TextField txtSurface;

    @FXML
    void addLake() {
        try {

            String name = txtName.getText();
            String location = txtLocation.getText();
            String surfaceRaw = txtSurface.getText();

            if (name.trim().isEmpty() || location.trim().isEmpty() || surfaceRaw.trim().isEmpty()) {
                showAlert("Greška", "Sva polja moraju biti popunjena!", Alert.AlertType.ERROR);
                return;
            }

            double surface = Double.parseDouble(surfaceRaw);

            Lake newLake = new Lake(0, name, location, surface);

            LakeDAO lakeDAO = new LakeDAO();
            lakeDAO.insertWithSectors(newLake);

            Stage stage = (Stage) txtName.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showAlert("Greška", "Površina mora biti broj (npr. 10.5)!", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}