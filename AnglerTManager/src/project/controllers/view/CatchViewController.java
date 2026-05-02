package project.controllers.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import project.dao.CatchDAO;
import project.models.Catch;

import java.sql.*;

public class CatchViewController {

    @FXML private TableView<Catch> tableCatches;
    @FXML private TableColumn<Catch, String> colId;
    @FXML private TableColumn<Catch, String> colIdCompetition;
    @FXML private TableColumn<Catch, String> colCompetitor;
    @FXML private TableColumn<Catch, String> colFish;
    @FXML private TableColumn<Catch, Double> colWeight;
    @FXML private TableColumn<Catch, String> colTime;

    private ObservableList<Catch> catchList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIdCompetition.setCellValueFactory(new PropertyValueFactory<>("idCompetition"));
        colCompetitor.setCellValueFactory(new PropertyValueFactory<>("competitor"));
        colFish.setCellValueFactory(new PropertyValueFactory<>("fishSpecies"));
        colWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));

    }

    public void loadDataForCompetition(int competitionId) {
        try {
            catchList.setAll(CatchDAO.getCatchesByCompetition(competitionId));
            tableCatches.setItems(catchList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}