package project.controllers.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import project.connection.DBConfig;
import project.dao.CatchDAO;
import project.models.CompetitorResult;

import java.sql.*;

public class CompetitorResultController {

    @FXML
    private TableView<CompetitorResult> resultsTable;

    //@FXML
    //private TableColumn<CompetitorResult, Integer> colCompetitionId;

    @FXML
    private TableColumn<CompetitorResult, String> colCompetitionId;

    @FXML
    private TableColumn<CompetitorResult, String> colCompetitorFullName;

    @FXML
    private TableColumn<CompetitorResult, Double> colTotalWeight;

    @FXML
    private TableColumn<CompetitorResult, Integer> colRank;

    private ObservableList<CompetitorResult> resultList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colCompetitionId.setCellValueFactory(new PropertyValueFactory<>("competitionId"));
        colCompetitorFullName.setCellValueFactory(new PropertyValueFactory<>("fullNameOfCompetitor"));
        colTotalWeight.setCellValueFactory(new PropertyValueFactory<>("totalWeight"));
        colRank.setCellValueFactory(new PropertyValueFactory<>("rank"));

        //loadResults(1);
        resultsTable.setItems(resultList);
    }

    public void loadResults(int competitionId) {
        resultList.clear();

        try {
            resultList.setAll(CatchDAO.getCompetitorResults(competitionId));
            //resultsTable.setItems(resultList);
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}