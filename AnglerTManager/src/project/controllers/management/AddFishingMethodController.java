package project.controllers.management;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import project.dao.FishingMethodDAO;

public class AddFishingMethodController {

    @FXML private TextField txtName;
    @FXML private TextArea txtDescription;

    private FishingMethodDAO dao = new FishingMethodDAO();

    @FXML
    private void handleSave() {
        String name = txtName.getText().trim();
        String description = txtDescription.getText().trim();

        if (name.isEmpty()) {
            showAlert("Greška", "Naziv metode mora biti unesen!");
            return;
        }

        boolean success = dao.insertMethod(name, description);

        if (success) {
            showAlert("Uspjeh", "Metoda ribolova uspješno dodana!");
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