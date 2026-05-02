package project.controllers.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindowController {

    @FXML
    private ImageView viewPicture;

    public MainWindowController() {

    }

    @FXML
    private void handleOpenCompetitions() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/fxml/competitions-view.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Prikaz održanih takmičenja");
            stage.setScene(new Scene(root));

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showMethods() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/fishing-methods.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Metode pecanja");
            stage.setScene(new Scene(root));

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showFishSpecies() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/fish-species.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Vrste riba");
            stage.setScene(new Scene(root));

            stage.show();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showLakes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/lake.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Jezera");
            stage.setScene(new Scene(root));

            stage.show();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showTeams() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/team.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ekipe");
            stage.setScene(new Scene(root));

            stage.show();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showCompetitors() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/competitors.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Takmičari");
            stage.setScene(new Scene(root));

            stage.show();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showJudges() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/judge.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Sudije");
            stage.setScene(new Scene(root));

            stage.show();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showWindowToAddNewCompetition() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/add-competition.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Dodavanje novog takmičenja");
            stage.setScene(new Scene(root));

            stage.show();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
