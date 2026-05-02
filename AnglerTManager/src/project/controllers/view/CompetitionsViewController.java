package project.controllers.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import project.connection.DBConfig;
import project.dao.CompetitionDAO;
import project.models.Competition;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class CompetitionsViewController {

    private ObservableList<Competition> masterData = FXCollections.observableArrayList();

    @FXML
    private TableView<Competition> tabCompetitions;

    @FXML
    private Button btnViewAll;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colIdCompetition;

    @FXML
    private TableColumn<?, ?> colIdFishingMethod;

    @FXML
    private TableColumn<?, ?> colIdLake;

    @FXML
    private TableColumn<?, ?> colLocation;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TextField txtSearch;

    public CompetitionsViewController() {

    }

    @FXML
    public void initialize() {

        setupTableColumns();
        loadDataFromDatabase();
        setupSearchFilter();

        tabCompetitions.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tabCompetitions.getSelectionModel().getSelectedItem() != null) {
                Competition selected = tabCompetitions.getSelectionModel().getSelectedItem();
                openDetails(selected);
            }
        });

    }

    private void openDetails(Competition selected) {
        if (selected == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/competition-details.fxml"));
            Parent root = loader.load();

            CompetitionDetailsController controller = loader.getController();
            controller.setData(selected);

            Stage stage = new Stage();
            stage.setTitle("Prikaz detalja odabranog takmicenja: " + selected.getName());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupTableColumns() {
        colIdCompetition.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        //colIdFishingMethod.setCellValueFactory(new PropertyValueFactory<>("fishingMethodId"));
        colIdFishingMethod.setCellValueFactory(new PropertyValueFactory<>("methodName"));
        //colIdLake.setCellValueFactory(new PropertyValueFactory<>("lakeId"));
        colIdLake.setCellValueFactory(new PropertyValueFactory<>("lakeName"));
    }

    private void loadDataFromDatabase() {

        masterData.clear();

        try {
            masterData.setAll(CompetitionDAO.getAllCompetitions());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupSearchFilter() {

        FilteredList<Competition> filteredData = new FilteredList<>(masterData, p -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(competition -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (competition.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (competition.getLocation().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Competition> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tabCompetitions.comparatorProperty());
        tabCompetitions.setItems(sortedData);
    }

    @FXML
    private void handleDeleteAction() {
        Competition selected = tabCompetitions.getSelectionModel().getSelectedItem();

        if (selected != null) {
            // Kreiranje potvrdnog prozora (Confirmation Alert)
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda brisanja");
            alert.setHeaderText("Brisanje takmičenja: " + selected.getName());
            alert.setContentText("Da li ste sigurni da želite obrisati ovo takmičenje?\n" +
                    "Ova akcija će obrisati sve povezane žrijebove i rezultate.");

            // Čekanje na odgovor korisnika
            Optional<ButtonType> result = alert.showAndWait();

            // Ako je korisnik kliknuo OK, pozovi brisanje
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteCompetition(selected);
            } //else {
                //System.out.println("Brisanje otkazano.");
            //}

        } else {
            // Opciono: Možeš izbaciti Warning alert ako ništa nije selektovano
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("Greška pri brisanju");
            warning.setHeaderText(null);
            warning.setContentText("Molimo odaberite takmičenje iz tabele koje želite obrisati.");
            warning.showAndWait();
        }
    }

    private void deleteCompetition(Competition selected) {
        try {

            boolean success = CompetitionDAO.deleteCompetition(selected.getId());

            if (success) {
                masterData.remove(selected);
                showAlert("Uspjeh", "Takmičenje je uspješno obrisano.");
            } else {
                showAlert("Greška", "Takmičenje nije pronađeno u bazi.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
