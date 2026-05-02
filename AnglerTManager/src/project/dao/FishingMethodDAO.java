package project.dao;

import project.connection.DBConfig;
import project.models.FishingMethod;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FishingMethodDAO {

    public List<FishingMethod> getAllMethods() {

        List<FishingMethod> methods = new ArrayList<>();

        String sql = "SELECT * FROM metoda_ribolova";

        try (Connection conn = DBConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                methods.add(new FishingMethod(
                        rs.getInt("IdMetodeRibolova"),
                        rs.getString("Naziv"),
                        rs.getString("Opis")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return methods;
    }

    public boolean insertMethod(String name, String description) {

        String maxIdSql = "SELECT MAX(IdMetodeRibolova) FROM metoda_ribolova";
        String insertSql = "INSERT INTO metoda_ribolova (IdMetodeRibolova, Naziv, Opis) VALUES (?, ?, ?)";

        try (Connection conn = DBConfig.getConnection()) {
            int nextId = 1;
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(maxIdSql)) {
                if (rs.next()) nextId = rs.getInt(1) + 1;
            }

            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, nextId);
                ps.setString(2, name);
                ps.setString(3, description);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<FishingMethod> getMethods() throws SQLException {
        List<FishingMethod> list = new ArrayList<>();
        try (Connection conn = DBConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM v_metode_ribolova")) {
            while (rs.next()) {
                list.add(new FishingMethod(rs.getInt("IdMetodeRibolova"), rs.getString("MetodaNaziv"), rs.getString("Opis")));
            }
        }
        return list;
    }

}