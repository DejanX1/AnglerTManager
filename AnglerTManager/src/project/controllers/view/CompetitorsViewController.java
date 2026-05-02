package project.controllers.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import project.connection.DBConfig;
import project.dao.TeamDAO;
import project.models.Competition;
import project.models.Competitor;
import java.sql.*;

public class CompetitorsViewController {

    @FXML private TableView<Competitor> tableCompetitors;
    @FXML private TableColumn<Competitor, Integer> colId;
    @FXML private TableColumn<Competitor, String> colFirstName;
    @FXML private TableColumn<Competitor, String> colLastName;
    @FXML private TableColumn<Competitor, String> colBirthDate;
    @FXML private TableColumn<Competitor, String> colTeam;
    @FXML private TextField txtSearch;

    private ObservableList<Competitor> competitorList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        colTeam.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        loadCompetitors();
        setupSearchFilter();
    }

    private void loadCompetitors() {

        competitorList.clear();
        try {
            //tableCompetitors.setItems(competitorList);
            competitorList.addAll(TeamDAO.getAllCompetitors());
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void setupSearchFilter() {

        FilteredList<Competitor> filteredData = new FilteredList<>(competitorList, p -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(competitor -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (competitor.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (competitor.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Competitor> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableCompetitors.comparatorProperty());
        tableCompetitors.setItems(sortedData);
    }
}