package project.controllers.management;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import project.dao.FishSpeciesDAO;

public class AddFishSpeciesController {

    @FXML private TextField txtName;

    private FishSpeciesDAO dao = new FishSpeciesDAO();

    @FXML
    private void handleSave() {

        String name = txtName.getText().trim();

        if (name.isEmpty()) {
            showAlert("Greška", "Naziv vrste ribe mora biti unesen!");
            return;
        }

        boolean success = dao.insertSpecies(name);

        if (success) {
            showAlert("Uspjeh", "Vrsta ribe uspješno dodana!");
            ((Stage) txtName.getScene().getWindow()).close();
        } else {
            showAlert("Greška", "Greška pri upisu u bazu!");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}