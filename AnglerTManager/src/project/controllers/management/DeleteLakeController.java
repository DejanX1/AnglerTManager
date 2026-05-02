package project.controllers.management;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.dao.LakeDAO;

import java.sql.SQLException;

public class DeleteLakeController {

    @FXML
    private TextField lakeIdField;

    @FXML
    void confirmDelete() {
        try {
            int id = Integer.parseInt(lakeIdField.getText());
            LakeDAO dao = new LakeDAO();

            boolean success = dao.deleteLakeById(id);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Lake deleted successfully.");
                closeWindow();
            } else {
                showAlert(Alert.AlertType.WARNING, "Not Found", "No lake found with ID: " + id);
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid number.");
        } catch (SQLException e) {

            if (e.getSQLState().startsWith("23")) {
                showAlert(Alert.AlertType.ERROR, "Deletion Failed",
                        "Nije moguće obrisati jezero jer postoje takmičenja ili žrebovi vezani za njega.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Došlo je do greške u bazi: " + e.getMessage());
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) lakeIdField.getScene().getWindow();
        stage.close();
    }
}