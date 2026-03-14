package dao;

import utils.DatabaseConnection;
import models.Ranking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RankingDAO {
    private Connection connection;

    public RankingDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Ranking> getRankingsByWeightClass(String weightClass) {
        List<Ranking> rankings = new ArrayList<>();
        String query = "SELECT * FROM rankings WHERE weight_class = ? ORDER BY rank_position";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, weightClass);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Ranking ranking = extractRankingFromResultSet(rs);
                rankings.add(ranking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rankings;
    }

    public List<Ranking> getAllRankings() {
        List<Ranking> rankings = new ArrayList<>();
        String query = "SELECT * FROM rankings ORDER BY weight_class, rank_position";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Ranking ranking = extractRankingFromResultSet(rs);
                rankings.add(ranking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rankings;
    }

    public Ranking getRankingByFighterAndWeightClass(int fighterId, String weightClass) {
        String query = "SELECT * FROM rankings WHERE fighter_id = ? AND weight_class = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, fighterId);
            pstmt.setString(2, weightClass);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractRankingFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public boolean insertOrUpdateRanking(Ranking ranking) {
        Ranking existing = getRankingByFighterAndWeightClass(ranking.getFighterId(), ranking.getWeightClass());
        
        if (existing != null) {
            return updateRanking(ranking);
        } else {
            return insertRanking(ranking);
        }
    }

    public boolean insertRanking(Ranking ranking) {
        String query = "INSERT INTO rankings (ranking_id, fighter_id, weight_class, rank_position, points, last_updated) " +
                      "VALUES (rankings_seq.NEXTVAL, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, ranking.getFighterId());
            pstmt.setString(2, ranking.getWeightClass());
            pstmt.setInt(3, ranking.getRankPosition());
            pstmt.setDouble(4, ranking.getPoints());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRanking(Ranking ranking) {
        String query = "UPDATE rankings SET rank_position = ?, points = ?, last_updated = CURRENT_TIMESTAMP " +
                      "WHERE fighter_id = ? AND weight_class = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, ranking.getRankPosition());
            pstmt.setDouble(2, ranking.getPoints());
            pstmt.setInt(3, ranking.getFighterId());
            pstmt.setString(4, ranking.getWeightClass());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRanking(int rankingId) {
        String query = "DELETE FROM rankings WHERE ranking_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, rankingId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Ranking extractRankingFromResultSet(ResultSet rs) throws SQLException {
        Ranking ranking = new Ranking();
        ranking.setRankingId(rs.getInt("ranking_id"));
        ranking.setFighterId(rs.getInt("fighter_id"));
        ranking.setWeightClass(rs.getString("weight_class"));
        ranking.setRankPosition(rs.getInt("rank_position"));
        ranking.setPoints(rs.getDouble("points"));
        ranking.setLastUpdated(rs.getTimestamp("last_updated"));
        return ranking;
    }
}