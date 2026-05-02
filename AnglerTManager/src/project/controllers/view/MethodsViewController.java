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
import project.dao.FishingMethodDAO;
import project.models.FishingMethod;

import java.io.IOException;
import java.sql.*;

public class MethodsViewController {
    @FXML private TableView<FishingMethod> tableMethods;
    @FXML private TableColumn<FishingMethod, Integer> colId;
    @FXML private TableColumn<FishingMethod, String> colName;
    @FXML private TableColumn<FishingMethod, String> colDescription;

    private ObservableList<FishingMethod> methodList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        tableMethods.setItems(methodList);
        loadMethods();
    }

    private void loadMethods() {

        methodList.clear();


        try {
            //tableMethods.setItems(methodList);
            methodList.addAll(FishingMethodDAO.getMethods());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openAddMethodWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/add-fishing-method.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Dodavanje metode ribolova");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}