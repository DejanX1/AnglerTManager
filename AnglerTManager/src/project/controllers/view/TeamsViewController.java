package project.controllers.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import project.connection.DBConfig;
import project.dao.TeamDAO;
import project.models.Team;
import java.sql.*;

public class TeamsViewController {
    @FXML private TableView<Team> tableTeams;
    @FXML private TableColumn<Team, Integer> colId;
    @FXML private TableColumn<Team, String> colName;
    @FXML private TableColumn<Team, String> colAssociation;

    private ObservableList<Team> teamList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAssociation.setCellValueFactory(new PropertyValueFactory<>("association"));

        tableTeams.setItems(teamList);
        loadTeams();
    }

    private void loadTeams() {

        teamList.clear();

        try {
            //tableTeams.setItems(teamList);
            teamList.addAll(TeamDAO.getTeams());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteTeam() {

        Team selected = tableTeams.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("Greška");
            warning.setHeaderText(null);
            warning.setContentText("Molimo odaberite ekipu koju želite obrisati.");
            warning.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Potvrda brisanja");
        confirm.setHeaderText(null);
        confirm.setContentText("Da li ste sigurni da želite obrisati ekipu: " +
                selected.getName() + "?\nOvo će obrisati i sve takmičare te ekipe!");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    TeamDAO teamDAO = new TeamDAO();
                    boolean success = teamDAO.deleteTeam(selected.getId());

                    if (success) {
                        teamList.remove(selected);
                    } else {
                        showAlert("Greška", "Ekipa nije pronađena u bazi.");
                    }
                } catch (SQLException e) {
                    if (e.getSQLState().startsWith("23")) {
                        showAlert("Greška", "Nije moguće obrisati ekipu jer je vezana za postojeće takmičenje.");
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