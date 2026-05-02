package project.controllers.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import project.connection.DBConfig;
import project.models.Sector;

import java.sql.*;

public class SectorsViewController {

    @FXML private TableView<Sector> sectorTable;
    @FXML private TableColumn<Sector, Integer> colSectorId;
    @FXML private TableColumn<Sector, Integer> colOrderNumber;
    @FXML private TableColumn<Sector, Integer> colLakeId;
    @FXML private TableColumn<Sector, String> colLakeName;

    private ObservableList<Sector> sectorList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colSectorId.setCellValueFactory(new PropertyValueFactory<>("sectorId"));
        colOrderNumber.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
        colLakeId.setCellValueFactory(new PropertyValueFactory<>("lakeId"));
        colLakeName.setCellValueFactory(new PropertyValueFactory<>("lakeName"));
    }

    public void loadAllSectors() {
        sectorList.clear();
        String query = "SELECT * FROM v_sektori";

        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                sectorList.add(new Sector(
                        rs.getInt("IdSektora"),
                        rs.getInt("RedniBrojSektora"),
                        rs.getInt("IdJezera"),
                        rs.getString("NazivJezera")
                ));
            }
            sectorTable.setItems(sectorList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
