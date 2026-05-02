package project.dao;

import project.connection.DBConfig;
import project.models.Competition;
import project.models.TeamResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompetitionDAO {

    public void insertCompetition(Competition competition) {
        Connection conn = null;
        try {

            conn = DBConfig.getConnection();
            conn.setAutoCommit(false);

            // 1. Pronalaženje sljedećeg ID-a za takmičenje
            int nextId = 1;
            String idSql = "SELECT MAX(IdTakmicenja) FROM takmicenje";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(idSql)) {
                if (rs.next()) {
                    nextId = rs.getInt(1) + 1;
                }
            }

            // 2. SQL upit za unos (Nazivi kolona moraju biti isti kao na tvojoj slici)
            String sql = "INSERT INTO takmicenje (IdTakmicenja, Naziv, DatumOdrzavanja, MjestoOdrzavanja, IdMetodeRibolova, IdJezera) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, nextId);
                ps.setString(2, competition.getName());
                ps.setDate(3, competition.getDate());
                ps.setString(4, competition.getLocation());
                ps.setInt(5, competition.getFishingMethodId());
                ps.setInt(6, competition.getLakeId());

                ps.executeUpdate();
            }

            conn.commit(); // Potvrda unosa
            //System.out.println("Competition successfully added with ID: " + nextId);

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    //System.err.println("Transaction rolled back!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<TeamResult> getTeamResults(int competitionId) throws SQLException {

        List<TeamResult> list = new ArrayList<>();
        String query = "SELECT NazivEkipe, Plasman, UkupnaMasaKg FROM v_rezultati_ekipa WHERE IdTakmicenja = ? ORDER BY Plasman ASC";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, competitionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new TeamResult(rs.getString("NazivEkipe"), rs.getInt("Plasman"), rs.getDouble("UkupnaMasaKg")));
            }
        }
        return list;
    }

    public static boolean deleteCompetition(int competitionId) throws SQLException {

        String query = "DELETE FROM takmicenje WHERE IdTakmicenja = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, competitionId);
            return ps.executeUpdate() > 0;

        }
    }

    public static boolean isDrawFinished(int competitionId) throws SQLException {

        String query = "SELECT COUNT(*) FROM zrijeb_takmicara WHERE IdTakmicenja = ? AND IdSektora IS NOT NULL";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, competitionId);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt(1) > 0;
        }

        return false;
    }

    public static boolean hasRegisteredTeams(int competitionId) throws SQLException {

        String query = "SELECT COUNT(*) FROM zrijeb_takmicara WHERE IdTakmicenja = ? AND IdSektora IS NULL";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, competitionId);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt(1) > 0;

        }

        return false;
    }

    public static boolean areJudgesAssigned(int idTakmicenja, int idJezera) {

        String sectorCountSql = "SELECT COUNT(*) FROM sektor WHERE IdJezera = ?";

        String judgeCountSql = "SELECT COUNT(*) FROM sudija_na_sektoru sns " +
                "JOIN sektor s ON sns.IdSektora = s.IdSektora " +
                "WHERE sns.IdTakmicenja = ? AND s.IdJezera = ?";

        try (Connection conn = DBConfig.getConnection()) {
            int sectorCount = 0;
            try (PreparedStatement ps = conn.prepareStatement(sectorCountSql)) {
                ps.setInt(1, idJezera);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) sectorCount = rs.getInt(1);
            }

            int judgeCount = 0;
            try (PreparedStatement ps = conn.prepareStatement(judgeCountSql)) {
                ps.setInt(1, idTakmicenja);
                ps.setInt(2, idJezera);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) judgeCount = rs.getInt(1);
            }

            // Vraća true samo ako su svi sektori pokriveni
            return sectorCount > 0 && judgeCount >= sectorCount;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Competition> getAllCompetitions() throws SQLException {

        List<Competition> list = new ArrayList<>();

        try (Connection conn = DBConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM v_takmicenja")) {

            while (rs.next()) {
                list.add(new Competition(
                        rs.getInt("IdTakmicenja"),
                        rs.getString("Naziv"),
                        rs.getDate("DatumOdrzavanja"),
                        rs.getString("MjestoOdrzavanja"),
                        rs.getInt("IdMetodeRibolova"),
                        rs.getInt("IdJezera"),
                        rs.getString("NazivMetode"),
                        rs.getString("NazivJezera")
                ));
            }
        }

        return list;
    }
}