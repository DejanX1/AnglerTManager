package project.controllers.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import project.connection.DBConfig;
import project.dao.CatchDAO;
import project.models.TeamDraw;

import java.sql.*;

public class TeamDrawViewController {

    @FXML private TableView<TeamDraw> tabDraw;
    @FXML private TableColumn<TeamDraw, String> colTeamName;
    @FXML private TableColumn<TeamDraw, String> colSector1;
    @FXML private TableColumn<TeamDraw, String> colSector2;
    @FXML private TableColumn<TeamDraw, String> colSector3;

    private ObservableList<TeamDraw> drawList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTable();
    }

    private void setupTable() {
        colTeamName.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        colSector1.setCellValueFactory(new PropertyValueFactory<>("sector1"));
        colSector2.setCellValueFactory(new PropertyValueFactory<>("sector2"));
        colSector3.setCellValueFactory(new PropertyValueFactory<>("sector3"));
        tabDraw.setItems(drawList);
    }

    public void loadData(int competitionId) {

        drawList.clear();

        try {
            drawList.setAll(CatchDAO.getTeamDraw(competitionId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}