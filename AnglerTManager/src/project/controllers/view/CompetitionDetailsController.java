package project.controllers.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import project.connection.DBConfig;
import project.controllers.management.AddCatchController;
import project.controllers.management.AddJudgesToCompetitionController;
import project.controllers.management.AddTeamWithMembersController;
import project.dao.CompetitionDAO;
import project.models.Competition;
import project.models.TeamResult;

import java.io.IOException;
import java.sql.*;

public class CompetitionDetailsController {

    @FXML private TableView<TeamResult> tabResults;
    @FXML private TableColumn<TeamResult, String> colTeamName;
    @FXML private TableColumn<TeamResult, Integer> colRank;
    @FXML private TableColumn<TeamResult, Double> colTotalCatch;

    private ObservableList<TeamResult> resultList = FXCollections.observableArrayList();
    private Competition currentCompetition;

    public void setData(Competition competition) {
        this.currentCompetition = competition;
        //loadTeamDraw(competition.getId());
        setupTable();
        loadResults(competition.getId());
    }

    private void setupTable() {

        colTeamName.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        colRank.setCellValueFactory(new PropertyValueFactory<>("rank"));
        colTotalCatch.setCellValueFactory(new PropertyValueFactory<>("totalCatch"));

        tabResults.setItems(resultList);
    }

    private void loadResults(int competitionId) {

        resultList.clear();

        try {

            resultList.addAll(CompetitionDAO.getTeamResults(competitionId));
            tabResults.setItems(resultList);

        } catch (SQLException e) {
            System.err.println("Greska pri ucitavanju podataka: " + e.getMessage());
        }

        tabResults.setItems(resultList);
    }

    @FXML
    private void showCatches() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/catch.fxml"));
            Parent root = loader.load();

            CatchViewController controller = loader.getController();
            controller.loadDataForCompetition(currentCompetition.getId());

            Stage stage = new Stage();
            stage.setTitle("Ulovi za takmičenje: " + currentCompetition.getName());
            stage.setScene(new Scene(root));

            stage.show();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showDraw() {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/fxml/team-draw.fxml"));
            Parent root = fxmlLoader.load();

            TeamDrawViewController controller = fxmlLoader.getController();
            controller.loadData(currentCompetition.getId());

            Stage stage = new Stage();
            stage.setTitle("Prikaz zrijeba za odabrano takmičenje");
            stage.setScene(new Scene(root));

            stage.show();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showCompetitorResults() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/fxml/competitors-results.fxml"));
            Parent root = fxmlLoader.load();

            CompetitorResultController controller = fxmlLoader.getController();
            controller.loadResults(currentCompetition.getId());

            Stage stage = new Stage();
            stage.setTitle("Prikaz rezlutata takmičara za odabrano takmičenje");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showTeamResults() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/team-results.fxml"));
            Parent root = loader.load();

            TeamResultController controller = loader.getController();
            controller.loadResults(currentCompetition.getId());

            Stage stage = new Stage();
            stage.setTitle("Rezultati ekipa: " + currentCompetition.getName());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showDrawCompetitors() {

        //System.out.println("Otvoram zrijeb za ID: " + currentCompetition.getId());

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/fxml/competitors-draw.fxml"));
            Parent root = fxmlLoader.load();

            CompetitorsDrawController controller = fxmlLoader.getController();
            int id = this.currentCompetition.getId();
            //System.out.println("Otvaram zrijeb za takmicenje ID: " + id);
            controller.loadData(this.currentCompetition.getId());

            Stage stage = new Stage();
            stage.setTitle("Prikaz žrijeba takmičara za odabrano takmičenje");
            stage.setScene(new Scene(root));

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openAddTeamWindow() {

        try {

            if (CompetitionDAO.isDrawFinished(currentCompetition.getId())) {
                showAlert("Akcija onemogućena", "Žrijeb je već izvršen. Više nije moguće dodavati takmičare.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/add-team-with-members.fxml"));
            Parent root = loader.load();

            AddTeamWithMembersController controller = loader.getController();
            controller.setIdTakmicenja(currentCompetition.getId());

            Stage stage = new Stage();

            stage.setTitle("Dodavanje nove ekipe i članova");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openAddCatchWindow() {

        try {

            if (!CompetitionDAO.isDrawFinished(currentCompetition.getId())) {
                showAlert("Pažnja", "Ne možete unositi ulove dok ne izvršite žrijeb takmičara!");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/add-catch.fxml"));
            Parent root = loader.load();

            AddCatchController controller = loader.getController();

            if (this.currentCompetition != null) {
                controller.setCompetitionId(this.currentCompetition.getId());
            } else {
                return;
            }

            controller.setOnSaveCallback(() -> {
                loadResults(this.currentCompetition.getId());
            });

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Greška", "Nije moguće otvoriti prozor za ulove.");
        }
    }

    @FXML
    private void handleOpenAddJudges() {
        try {

            if (CompetitionDAO.isDrawFinished(currentCompetition.getId())) {
                showAlert("Pažnja", "Ne možete mijenjati unos sudija nakon što je žrijeb obavljen!");
                return;
            }

            // 1. Učitaj FXML fajl novog prozora
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/add-judges.fxml"));
            Parent root = loader.load();

            // 2. Uzmi kontroler tog novog prozora (to je tvoj AddJudgesToCompetitionController)
            AddJudgesToCompetitionController controller = loader.getController();

            // 3. POZIV METODE: Ovdje prosljeđuješ ID-ove koje imaš u trenutnom prozoru
            // Pretpostavljam da imaš objekat 'currentCompetition' ili varijable idTakmicenja i idJezera
            controller.setData(this.currentCompetition.getId(), this.currentCompetition.getLakeId());

            // 4. Prikaži novi prozor
            Stage stage = new Stage();
            stage.setTitle("Dodjela sudija sektorima");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Greška", "Nije moguće otvoriti prozor za sudije.");
        }
    }

    @FXML
    private void handleExecuteDraw() {

        try {

            if (CompetitionDAO.isDrawFinished(currentCompetition.getId())) {
                showAlert("Greška", "Žrijeb je već obavljen!");
                return;
            }

            if (!CompetitionDAO.hasRegisteredTeams(currentCompetition.getId())) {
                showAlert("Greška", "Ne možete izvršiti žrijeb jer nema prijavljenih ekipa!");
                return;
            }

            if (!CompetitionDAO.areJudgesAssigned(currentCompetition.getId(), currentCompetition.getLakeId())) {
                showAlert("Pažnja", "Ne možete obaviti žrijeb dok ne dodijelite sudije na sve sektore!");
                return;
            }

            String procedure = "{CALL sp_napravi_zrijeb(?)}";

            try (Connection conn = DBConfig.getConnection();
                 CallableStatement cstmt = conn.prepareCall(procedure)) {

                cstmt.setInt(1, currentCompetition.getId());
                cstmt.execute();

                showAlert("Uspjeh", "Žrijeb je uspješno izvršen! Takmičenje je sada zaključano za nove prijave.");

            } catch (SQLException e) {
                showAlert("Greška", "Nije moguće izvršiti žrijeb: " + e.getMessage());
            }

        } catch(Exception e) {
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