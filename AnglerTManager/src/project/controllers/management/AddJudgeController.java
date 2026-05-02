package project.controllers.management;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import project.dao.JudgeDAO;

public class AddJudgeController {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtLicense;

    private JudgeDAO judgeDAO = new JudgeDAO();

    @FXML
    private void handleSave() {
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String license = txtLicense.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || license.isEmpty()) {
            showAlert("Greška", "Sva polja moraju biti popunjena!");
            return;
        }

        boolean success = judgeDAO.insertJudge(firstName, lastName, license);

        if (success) {
            showAlert("Uspjeh", "Sudija uspješno dodan!");
            ((Stage) txtFirstName.getScene().getWindow()).close();
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