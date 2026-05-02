package project.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project.connection.DBConfig;
import project.models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CatchDAO {

    public static boolean insertCatch(int idCompetitor, double weight, String time, int idFishSpecies, int idCompetition) {

        String query = "INSERT INTO ulov (IdTakmicara, IdUlova, MasaKg, VrijemeUlova, IdVrsteRibe, IdTakmicenja) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConfig.getConnection()) {

            // Rucno generisanje id-a jer nije podesen auto increment
            int nextIdUlova = 1;
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT MAX(IdUlova) FROM ulov")) {
                if (rs.next()) {
                    nextIdUlova = rs.getInt(1) + 1;
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, idCompetitor);
                pstmt.setInt(2, nextIdUlova);
                pstmt.setDouble(3, weight);
                pstmt.setString(4, time);
                pstmt.setInt(5, idFishSpecies);
                pstmt.setInt(6, idCompetition);

                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ObservableList<Competitor> getCompetitorsForCompetition(int competitionId) {

        ObservableList<Competitor> list = FXCollections.observableArrayList();

        String query = "SELECT t.IdTakmicara, t.Ime, t.Prezime, t.IdEkipe FROM takmicar t " +
                "JOIN zrijeb_takmicara z ON t.IdTakmicara = z.IdTakmicara " +
                "WHERE z.IdTakmicenja = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, competitionId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(new Competitor(
                        rs.getInt("IdTakmicara"),
                        rs.getString("Ime"),
                        rs.getString("Prezime"),
                        null,
                        rs.getInt("IdEkipe"),
                        null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Catch> getCatchesByCompetition(int competitionId) throws SQLException {

        List<Catch> list = new ArrayList<>();
        String query = "SELECT * FROM v_ulovi WHERE IdTakmicenja = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, competitionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Catch(
                            rs.getInt("IdUlova"),
                            rs.getInt("IdTakmicenja"),
                            rs.getString("Takmicar"),
                            rs.getString("VrstaRibe"),
                            rs.getDouble("MasaKg"),
                            rs.getString("VrijemeUlova")
                    ));
                }
            }
        }
        return list;
    }

    public static List<CompetitorsDraw> getCompetitorsDraw(int competitionId) throws SQLException {

        List<CompetitorsDraw> list = new ArrayList<>();
        String query = "SELECT * FROM v_pregled_zrijeba WHERE IdTakmicenja = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, competitionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new CompetitorsDraw(
                        rs.getInt("Pozicija"),
                        rs.getString("Takmičenje"),
                        rs.getString("Ime"),
                        rs.getString("Prezime"),
                        rs.getString("Ekipa"),
                        rs.getInt("Sektor")));
            }
        }

        return list;
    }

    public static List<TeamDraw> getTeamDraw(int competitionId) throws SQLException {

        List<TeamDraw> list = new ArrayList<>();
        String query = "SELECT NazivEkipe, Sektor_1, Sektor_2, Sektor_3 FROM v_zrijeb_ekipa WHERE IdTakmicenja = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, competitionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new TeamDraw(
                        rs.getString("NazivEkipe"),
                        rs.getString("Sektor_1"),
                        rs.getString("Sektor_2"),
                        rs.getString("Sektor_3")));
            }
        }

        return list;
    }

    public static List<CompetitorResult> getCompetitorResults(int competitionId) throws SQLException {

        List<CompetitorResult> list = new ArrayList<>();

        try (Connection conn = DBConfig.getConnection()) {

            try (CallableStatement cstmt = conn.prepareCall("{CALL sp_obracunaj_rezultate(?)}")) {
                cstmt.setInt(1, competitionId);
                cstmt.execute();
            }

            String query = "SELECT * FROM v_rezultati_takmicara WHERE IdTakmicenja = ? ORDER BY Plasman ASC";

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, competitionId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    list.add(new CompetitorResult(rs.getInt("IdTakmicara"), rs.getString("Ime") + " " + rs.getString("Prezime"), rs.getInt("IdTakmicenja"), rs.getDouble("UkupnaMasaKg"), rs.getInt("Plasman")));
                }
            }
        }

        return list;
    }

    public static List<TeamResult> getTeamResults(int competitionId) throws SQLException {

        List<TeamResult> list = new ArrayList<>();

        try (Connection conn = DBConfig.getConnection()) {
            try (CallableStatement cstmt = conn.prepareCall("{CALL sp_obracunaj_rezultate_ekipa(?)}")) {
                cstmt.setInt(1, competitionId);
                cstmt.execute();
            }

            String query = "SELECT e.Naziv AS NazivEkipe, re.Plasman, re.UkupnaMasaKg " +
                            "FROM rezultat_ekipe re " +
                            "JOIN ekipa e ON re.IdEkipe = e.IdEkipe " +
                            "WHERE re.IdTakmicenja = ? " +
                            "ORDER BY re.Plasman ASC";

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, competitionId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    list.add(new TeamResult(
                            rs.getString("NazivEkipe"),
                            rs.getInt("Plasman"),
                            rs.getDouble("UkupnaMasaKg")));
                }
            }
        }

        return list;
    }

}