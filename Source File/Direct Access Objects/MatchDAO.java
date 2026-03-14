package dao;

import utils.DatabaseConnection;
import models.Match;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchDAO {
    private Connection connection;

    public MatchDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Match> getAllMatches() {
        List<Match> matches = new ArrayList<>();
        String query = "SELECT * FROM matches ORDER BY match_date DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Match match = extractMatchFromResultSet(rs);
                matches.add(match);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return matches;
    }

    public List<Match> getUpcomingMatches() {
        List<Match> matches = new ArrayList<>();
        String query = "SELECT * FROM matches WHERE status = 'scheduled' ORDER BY match_date";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Match match = extractMatchFromResultSet(rs);
                matches.add(match);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return matches;
    }

    public List<Match> getScheduledMatches() {
        List<Match> matches = new ArrayList<>();
        String query = "SELECT * FROM matches WHERE status = 'scheduled' ORDER BY match_date ASC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Match match = extractMatchFromResultSet(rs);
                matches.add(match);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return matches;
    }

    public List<Match> getCompletedMatches() {
        List<Match> matches = new ArrayList<>();
        String query = "SELECT * FROM matches WHERE status = 'completed' ORDER BY match_date DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Match match = extractMatchFromResultSet(rs);
                matches.add(match);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return matches;
    }

    public List<Match> getCancelledMatches() {
        List<Match> matches = new ArrayList<>();
        String query = "SELECT * FROM matches WHERE status = 'cancelled' ORDER BY match_date DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Match match = extractMatchFromResultSet(rs);
                matches.add(match);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return matches;
    }

    public List<Match> getMatchesByEvent(int eventId) {
        List<Match> matches = new ArrayList<>();
        String query = "SELECT * FROM matches WHERE event_id = ? ORDER BY match_date";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, eventId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Match match = extractMatchFromResultSet(rs);
                matches.add(match);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return matches;
    }

    public Match getMatchById(int matchId) {
        String query = "SELECT * FROM matches WHERE match_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, matchId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractMatchFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public boolean insertMatch(Match match) {
        String query = "INSERT INTO matches (match_id, event_id, fighter1_id, fighter2_id, weight_class, " +
                      "match_type, scheduled_rounds, match_date, status, created_by) " +
                      "VALUES (matches_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, 'scheduled', ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, match.getEventId());
            pstmt.setInt(2, match.getFighter1Id());
            pstmt.setInt(3, match.getFighter2Id());
            pstmt.setString(4, match.getWeightClass());
            pstmt.setString(5, match.getMatchType());
            pstmt.setInt(6, match.getScheduledRounds());
            pstmt.setInt(7, match.getCreatedBy());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMatchStatus(int matchId, String status) {
        String query = "UPDATE matches SET status = ? WHERE match_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, matchId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMatchDate(int matchId, Timestamp newDate) {
        String query = "UPDATE matches SET match_date = ? WHERE match_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setTimestamp(1, newDate);
            pstmt.setInt(2, matchId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMatchResult(int matchId, Integer winnerId, String finishMethod, int finishRound) {
        String query = "UPDATE matches SET winner_id = ?, finish_method = ?, finish_round = ?, status = 'completed' " +
                      "WHERE match_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            if (winnerId != null) {
                pstmt.setInt(1, winnerId);
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            pstmt.setString(2, finishMethod);
            pstmt.setInt(3, finishRound);
            pstmt.setInt(4, matchId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMatch(int matchId) {
        String query = "DELETE FROM matches WHERE match_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, matchId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Match extractMatchFromResultSet(ResultSet rs) throws SQLException {
        Match match = new Match();
        match.setMatchId(rs.getInt("match_id"));
        match.setEventId(rs.getInt("event_id"));
        match.setFighter1Id(rs.getInt("fighter1_id"));
        match.setFighter2Id(rs.getInt("fighter2_id"));
        match.setWeightClass(rs.getString("weight_class"));
        match.setMatchType(rs.getString("match_type"));
        match.setScheduledRounds(rs.getInt("scheduled_rounds"));
        
        int winnerId = rs.getInt("winner_id");
        if (!rs.wasNull()) {
            match.setWinnerId(winnerId);
        }
        
        match.setFinishMethod(rs.getString("finish_method"));
        
        int finishRound = rs.getInt("finish_round");
        if (!rs.wasNull()) {
            match.setFinishRound(finishRound);
        }
        
        match.setMatchDate(rs.getTimestamp("match_date"));
        match.setStatus(rs.getString("status"));
        match.setCreatedBy(rs.getInt("created_by"));
        
        return match;
    }
}