package project.dao;

import project.connection.DBConfig;
import project.models.Competitor;
import project.models.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {

    public boolean insertTeamWithMembers(String teamName, String association, List<Competitor> members, int idTakmicenja) {

        try (Connection conn = DBConfig.getConnection()) {

            int generatedTeamId = 1;
            String maxIdSql = "SELECT MAX(IdEkipe) FROM ekipa";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(maxIdSql)) {
                if (rs.next()) {
                    generatedTeamId = rs.getInt(1) + 1;
                }
            }

            String teamSql = "INSERT INTO ekipa (IdEkipe, Naziv, SRD) VALUES (?, ?, ?)";
            try (PreparedStatement psTeam = conn.prepareStatement(teamSql)) {
                psTeam.setInt(1, generatedTeamId);
                psTeam.setString(2, teamName);
                psTeam.setString(3, association);
                psTeam.executeUpdate();
            }

            int nextCompetitorId = 1;
            String maxCompIdSql = "SELECT MAX(IdTakmicara) FROM takmicar";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(maxCompIdSql)) {
                if (rs.next()) {
                    nextCompetitorId = rs.getInt(1) + 1;
                }
            }

            String competitorSql = "INSERT INTO takmicar (IdTakmicara, Ime, Prezime, DatumRodjenja, IdEkipe) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psComp = conn.prepareStatement(competitorSql)) {
                for (Competitor member : members) {
                    psComp.setInt(1, nextCompetitorId++);
                    psComp.setString(2, member.getFirstName());
                    psComp.setString(3, member.getLastName());
                    psComp.setString(4, member.getBirthDate());
                    psComp.setInt(5, generatedTeamId);
                    psComp.addBatch();
                }
                psComp.executeBatch();
            }

            String procedureSql = "{CALL sp_prijavi_ekipu(?, ?)}";
            try (CallableStatement cstmt = conn.prepareCall(procedureSql)) {
                cstmt.setInt(1, idTakmicenja);
                cstmt.setInt(2, generatedTeamId);
                cstmt.execute();
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTeam(int idEkipe) throws SQLException {

        String sql = "DELETE FROM ekipa WHERE IdEkipe = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEkipe);
            return ps.executeUpdate() > 0;
        }
    }

    public static List<Team> getTeams() throws SQLException {

        List<Team> list = new ArrayList<>();

        try (Connection conn = DBConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM v_ekipe")) {

            while (rs.next()) {
                list.add(new Team(rs.getInt("IdEkipe"), rs.getString("EkipaNaziv"), rs.getString("DrustvoNaziv")));
            }

        }
        return list;
    }

    public static List<Competitor> getAllCompetitors() throws SQLException {

        List<Competitor> list = new ArrayList<>();

        try (Connection conn = DBConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM v_takmicari")) {

            while (rs.next()) {
                list.add(new Competitor(
                        rs.getInt("IdTakmicara"),
                        rs.getString("Ime"),
                        rs.getString("Prezime"),
                        rs.getString("DatumRodjenja"),
                        rs.getInt("IdEkipe"),
                        rs.getString("EkipaNaziv")
                ));
            }
        }
        return list;
    }

}