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
import project.connection.DBConfig;
import project.dao.FishSpeciesDAO;
import project.models.FishSpecies;

import java.io.IOException;
import java.sql.*;
import javafx.stage.Stage;

public class FishViewController {
    @FXML private TableView<FishSpecies> tableFish;
    @FXML private TableColumn<FishSpecies, Integer> colId;
    @FXML private TableColumn<FishSpecies, String> colName;

    private ObservableList<FishSpecies> fishList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        tableFish.setItems(fishList);
        loadFish();
    }

    private void loadFish() {
        fishList.clear();

        try {
            //tableFish.setItems(fishList);
            fishList.addAll(FishSpeciesDAO.getSpecies());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openAddSpeciesWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/add-fish-species.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Dodavanje vrste ribe");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}