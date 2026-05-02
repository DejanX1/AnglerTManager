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
import project.dao.LakeDAO;
import project.models.Lake;

import java.io.IOException;
import java.sql.*;

public class LakesViewController {

    @FXML private TableView<Lake> tableLakes;
    @FXML private TableColumn<Lake, Integer> colId;
    @FXML private TableColumn<Lake, String> colName;
    @FXML private TableColumn<Lake, String> colLocation;
    @FXML private TableColumn<Lake, Double> colSurfaceHa;

    private ObservableList<Lake> lakeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colSurfaceHa.setCellValueFactory(new PropertyValueFactory<>("area"));

        tableLakes.setItems(lakeList);
        loadLakes();
    }

    private void loadLakes() {

        lakeList.clear();

        try {
            lakeList.addAll(LakeDAO.getAllLakes());
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void openAddLakeWindow() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/add-lake.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Dodavanje novog jezera");
            stage.setScene(new Scene(root));

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openDeleteLakeWindow() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/delete-lake.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Brisanje postojećeg jezera");
            stage.setScene(new Scene(root));

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showAllSectors() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/fxml/sectors-view.fxml"));
            Parent root = fxmlLoader.load();

            SectorsViewController controller = fxmlLoader.getController();

            controller.loadAllSectors();

            Stage stage = new Stage();
            stage.setTitle("Prikaz svih sektora");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}