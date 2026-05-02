package project.controllers.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import project.connection.DBConfig;
import project.dao.JudgeDAO;
import project.models.Judge;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.*;

public class JudgesViewController {

    @FXML private TableView<Judge> tableJudges;

    @FXML private TableColumn<Judge, Integer> colId;
    @FXML private TableColumn<Judge, String> colFirstName;
    @FXML private TableColumn<Judge, String> colLastName;
    @FXML private TableColumn<Judge, String> colLicense;

    private ObservableList<Judge> judgeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colLicense.setCellValueFactory(new PropertyValueFactory<>("license"));

        tableJudges.setItems(judgeList);
        loadJudges();
    }

    private void loadJudges() {

        judgeList.clear();

        try {
            //tableJudges.setItems(judgeList);
            judgeList.addAll(JudgeDAO.getJudges());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void openAddJudgeWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/add-new-judge.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Dodavanje novog sudije");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteJudge() {

        Judge selected = tableJudges.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("Greška");
            warning.setHeaderText(null);
            warning.setContentText("Molimo odaberite sudiju kojeg želite obrisati.");
            warning.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Potvrda brisanja");
        confirm.setHeaderText(null);
        confirm.setContentText("Da li ste sigurni da želite obrisati sudiju: " + selected.getFirstName() + " " + selected.getLastName() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    JudgeDAO judgeDAO = new JudgeDAO();
                    boolean success = judgeDAO.deleteJudge(selected.getId());

                    if (success) {
                        judgeList.remove(selected);
                    } else {
                        showAlert("Greška", "Sudija nije pronađen u bazi.");
                    }
                } catch (SQLException e) {
                    if (e.getSQLState().startsWith("23")) {
                        showAlert("Greška", "Nije moguće obrisati sudiju jer je dodijeljen na takmičenje.");
                    } else {
                        showAlert("Greška", "Greška pri brisanju: " + e.getMessage());
                    }
                }
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}