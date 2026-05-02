package project.dao;

import project.connection.DBConfig;
import project.models.Judge;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JudgeDAO {

    public boolean insertJudge(String firstName, String lastName, String license) {

        String insertSql = "INSERT INTO sudija (IdSudije, Ime, Prezime, Licenca) VALUES (?, ?, ?, ?)";
        String maxIdSql = "SELECT MAX(IdSudije) FROM sudija";

        try (Connection conn = DBConfig.getConnection()) {

            int nextId = 1;
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(maxIdSql)) {
                if (rs.next()) {
                    nextId = rs.getInt(1) + 1;
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, nextId);
                ps.setString(2, firstName);
                ps.setString(3, lastName);
                ps.setString(4, license);
                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteJudge(int idSudije) throws SQLException {

        String sql = "DELETE FROM sudija WHERE IdSudije = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSudije);
            return ps.executeUpdate() > 0;

        }
    }

    public List<Judge> getAllJudges() {

        List<Judge> judges = new ArrayList<>();

        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM sudija")) {

            while (rs.next()) {
                judges.add(new Judge(
                        rs.getInt("IdSudije"),
                        rs.getString("Ime"),
                        rs.getString("Prezime"),
                        rs.getString("Licenca")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return judges;
    }

    public boolean assignJudgesToSectors(int idTakmicenja, List<int[]> dodjelaList) {

        String deleteSql = "DELETE FROM sudija_na_sektoru WHERE IdTakmicenja = ?";
        String insertSql = "INSERT INTO sudija_na_sektoru (IdSudije, IdTakmicenja, IdSektora) VALUES (?, ?, ?)";

        try (Connection conn = DBConfig.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psDelete = conn.prepareStatement(deleteSql)) {
                psDelete.setInt(1, idTakmicenja);
                psDelete.executeUpdate();
            }

            try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                for (int[] dodjela : dodjelaList) {

                    // dodjela[0] = IdSudije, dodjela[1] = IdSektora
                    psInsert.setInt(1, dodjela[0]);
                    psInsert.setInt(2, idTakmicenja);
                    psInsert.setInt(3, dodjela[1]);
                    psInsert.addBatch();
                }
                psInsert.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Judge> getJudges() throws SQLException {

        List<Judge> list = new ArrayList<>();

        try (Connection conn = DBConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM v_sudija")) {

            while (rs.next()) {
                list.add(new Judge(rs.getInt("IdSudije"), rs.getString("Ime"), rs.getString("Prezime"), rs.getString("Licenca")));
            }

        }
        return list;
    }
}