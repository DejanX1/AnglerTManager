package project.controllers.management;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import project.dao.CatchDAO;
import project.dao.FishSpeciesDAO;
import project.models.Competitor;
import project.models.FishSpecies;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddCatchController {

    @FXML private ComboBox<Competitor> comboCompetitor;
    @FXML private TextField txtWeight;
    @FXML private ComboBox<FishSpecies> comboFishSpecies;

    @FXML
    private TextField txtSearch;

    private int currentCompetitionId;
    private ObservableList<Competitor> allCompetitors = FXCollections.observableArrayList();
    private Runnable onSaveCallback;

    @FXML
    public void initialize() {

        comboFishSpecies.setItems(FishSpeciesDAO.getAllFishSpecies());
        setupCompetitorCombo();

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterCompetitors(newValue);
        });
    }

    private void filterCompetitors(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            comboCompetitor.setItems(allCompetitors);
        } else {
            String lower = searchText.toLowerCase();
            ObservableList<Competitor> filtered = FXCollections.observableArrayList();
            for (Competitor c : allCompetitors) {
                if (c.getFirstName().toLowerCase().contains(lower) ||
                        c.getLastName().toLowerCase().contains(lower)) {
                    filtered.add(c);
                }
            }
            comboCompetitor.setItems(filtered);

            if (!filtered.isEmpty()) {
                comboCompetitor.show();
            }
        }
    }

    public void setCompetitionId(int id) {
        this.currentCompetitionId = id;
        loadCompetitors();
    }

    private void loadCompetitors() {
        allCompetitors = CatchDAO.getCompetitorsForCompetition(currentCompetitionId);
        comboCompetitor.setItems(allCompetitors);
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    @FXML
    private void handleSave() {
        try {
            Competitor selected = comboCompetitor.getValue();
            if (selected == null) {
                showAlert("Greška", "Morate izabrati takmičara!");
                return;
            }

            if (txtWeight.getText().isEmpty()) {
                showAlert("Greška", "Unesite masu ulova!");
                return;
            }

            FishSpecies selectedSpecies = comboFishSpecies.getValue();
            if (selectedSpecies == null) {
                showAlert("Greška", "Morate izabrati vrstu ribe!");
                return;
            }

            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            int fishId = selectedSpecies.getId();
            double weight = Double.parseDouble(txtWeight.getText());

            boolean success = CatchDAO.insertCatch(selected.getId(), weight, currentTime, fishId, currentCompetitionId);

            if (success) {

                if (onSaveCallback != null) {
                    onSaveCallback.run();
                }

                showAlert("Uspjeh", "Ulov sačuvan!");
                ((Stage) txtWeight.getScene().getWindow()).close();
            }

        } catch (NumberFormatException e) {
            showAlert("Greška", "Masa mora biti broj (npr. 1.5)!");
        }

    }

    private void setupCompetitorCombo() {
        comboCompetitor.setConverter(new StringConverter<Competitor>() {
            @Override
            public String toString(Competitor c) {
                return (c == null) ? "" : c.getFirstName() + " " + c.getLastName();
            }
            @Override
            public Competitor fromString(String string) { return null; }
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