package project.dao;

import project.connection.DBConfig;
import project.models.Lake;
import project.models.Sector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LakeDAO {

    public static void insertWithSectors(Lake lake) {

        try (Connection conn = DBConfig.getConnection()) {

            // 1. Get next ID for Lake
            int nextLakeId = 1;
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT MAX(IdJezera) FROM jezero")) {
                if (rs.next()) {
                    nextLakeId = rs.getInt(1) + 1;
                }
            }

            // 2. Insert Lake
            String lakeSql = "INSERT INTO jezero (IdJezera, Naziv, Lokacija, PovrsinaHa) VALUES (?, ?, ?, ?)";
            try (PreparedStatement psLake = conn.prepareStatement(lakeSql)) {
                psLake.setInt(1, nextLakeId);
                psLake.setString(2, lake.getName());
                psLake.setString(3, lake.getLocation());
                psLake.setDouble(4, lake.getArea());
                psLake.executeUpdate();
            }

            // 3. Get starting ID for Sectors
            int nextSectorId = 1;
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT MAX(IdSektora) FROM sektor")) {
                if (rs.next()) {
                    nextSectorId = rs.getInt(1) + 1;
                }
            }

            // 4. Insert 3 Sectors in a loop
            String sectorSql = "INSERT INTO sektor (IdSektora, RedniBroj, IdJezera) VALUES (?, ?, ?)";
            try (PreparedStatement psSector = conn.prepareStatement(sectorSql)) {
                for (int i = 1; i <= 3; i++) {
                    psSector.setInt(1, nextSectorId);
                    psSector.setInt(2, i);
                    psSector.setInt(3, nextLakeId);
                    psSector.executeUpdate();

                    nextSectorId++;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Sector> getSectorsByLake(int idJezera) {

        List<Sector> sectors = new ArrayList<>();

        String query = "SELECT * FROM sektor WHERE IdJezera = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idJezera);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                sectors.add(new Sector(
                        rs.getInt("IdSektora"),
                        rs.getInt("RedniBroj"),
                        rs.getInt("IdJezera"),
                        ""
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sectors;
    }

    public boolean deleteLakeById(int lakeId) throws SQLException {

        String deleteSectorsSql = "DELETE FROM sektor WHERE IdJezera = ?";
        String deleteLakeSql = "DELETE FROM jezero WHERE IdJezera = ?";

        try (Connection conn = DBConfig.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psSectors = conn.prepareStatement(deleteSectorsSql);
                 PreparedStatement psLake = conn.prepareStatement(deleteLakeSql)) {

                psSectors.setInt(1, lakeId);
                psSectors.executeUpdate();

                psLake.setInt(1, lakeId);
                int affectedRows = psLake.executeUpdate();

                conn.commit();
                return affectedRows > 0;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public static List<Lake> getAllLakes() {

        List<Lake> lakes = new ArrayList<>();
        String sql = "SELECT * FROM jezero";

        try (Connection conn = DBConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lakes.add(new Lake(
                        rs.getInt("IdJezera"),
                        rs.getString("Naziv"),
                        rs.getString("Lokacija"),
                        rs.getDouble("PovrsinaHa")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lakes;
    }
}