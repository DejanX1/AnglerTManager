package project.controllers.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import project.connection.DBConfig;
import project.dao.CatchDAO;
import project.models.CompetitorsDraw;

import java.sql.*;

public class CompetitorsDrawController {

    @FXML private TableView<CompetitorsDraw> tableDraw;
    @FXML private TableColumn<CompetitorsDraw, Integer> colPosition;
    @FXML private TableColumn<CompetitorsDraw, String> colCompetition;
    @FXML private TableColumn<CompetitorsDraw, String> colFirstName;
    @FXML private TableColumn<CompetitorsDraw, String> colLastName;
    @FXML private TableColumn<CompetitorsDraw, String> colTeam;
    @FXML private TableColumn<CompetitorsDraw, Integer> colSector;

    private ObservableList<CompetitorsDraw> drawData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        colCompetition.setCellValueFactory(new PropertyValueFactory<>("competition"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colTeam.setCellValueFactory(new PropertyValueFactory<>("team"));
        colSector.setCellValueFactory(new PropertyValueFactory<>("sector"));

        tableDraw.setItems(drawData);
    }

    public void loadData(int competitionId) {

        drawData.clear();

        try {
            //tableDraw.setItems(drawData);
            drawData.addAll(CatchDAO.getCompetitorsDraw(competitionId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}