package project.controllers.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import project.connection.DBConfig;
import project.dao.CatchDAO;
import project.models.TeamResult;

import java.sql.*;

public class TeamResultController {

    @FXML private TableView<TeamResult> tableTeamResults;
    @FXML private TableColumn<TeamResult, String> colTeamName;
    @FXML private TableColumn<TeamResult, Integer> colRank;
    @FXML private TableColumn<TeamResult, Double> colTotalCatch;

    private ObservableList<TeamResult> resultList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colTeamName.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        colRank.setCellValueFactory(new PropertyValueFactory<>("rank"));
        colTotalCatch.setCellValueFactory(new PropertyValueFactory<>("totalCatch"));
        tableTeamResults.setItems(resultList);
    }

    public void loadResults(int competitionId) {

        resultList.clear();

        try {
            resultList.setAll(CatchDAO.getTeamResults(competitionId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}