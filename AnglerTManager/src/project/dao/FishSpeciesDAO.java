package project.dao;

import project.connection.DBConfig;
import project.models.FishSpecies;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.*;

public class FishSpeciesDAO {

    public static ObservableList<FishSpecies> getAllFishSpecies() {

        ObservableList<FishSpecies> list = FXCollections.observableArrayList();
        String query = "SELECT IdVrsteRibe, Naziv FROM vrsta_ribe";

        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(new FishSpecies(
                        rs.getInt("IdVrsteRibe"),
                        rs.getString("Naziv")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertSpecies(String name) {

        String maxIdSql = "SELECT MAX(IdVrsteRibe) FROM vrsta_ribe";
        String insertSql = "INSERT INTO vrsta_ribe (IdVrsteRibe, Naziv) VALUES (?, ?)";

        try (Connection conn = DBConfig.getConnection()) {
            int nextId = 1;
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(maxIdSql)) {
                if (rs.next()) nextId = rs.getInt(1) + 1;
            }

            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, nextId);
                ps.setString(2, name);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<FishSpecies> getSpecies() throws SQLException {
        List<FishSpecies> list = new ArrayList<>();
        try (Connection conn = DBConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM v_vrste_ribe")) {
            while (rs.next()) {
                list.add(new FishSpecies(rs.getInt("IdVrsteRibe"), rs.getString("VrstaRibeNaziv")));
            }
        }
        return list;
    }
}