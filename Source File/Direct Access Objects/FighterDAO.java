
package dao;

import utils.DatabaseConnection;
import models.Fighter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FighterDAO {
    private Connection connection;

    public FighterDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Fighter> getAllFighters() {
        List<Fighter> fighters = new ArrayList<>();
        String query = "SELECT * FROM fighters ORDER BY fighter_name";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Fighter fighter = extractFighterFromResultSet(rs);
                fighters.add(fighter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return fighters;
    }

    public List<Fighter> getAllActiveFighters() {
        List<Fighter> fighters = new ArrayList<>();
        String query = "SELECT * FROM fighters WHERE status = 'active' ORDER BY fighter_name";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Fighter fighter = extractFighterFromResultSet(rs);
                fighters.add(fighter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return fighters;
    }

    public List<Fighter> getFightersByWeightClass(String weightClass) {
        List<Fighter> fighters = new ArrayList<>();
        String query = "SELECT * FROM fighters WHERE weight_class = ? AND status = 'active' ORDER BY fighter_name";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, weightClass);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Fighter fighter = extractFighterFromResultSet(rs);
                fighters.add(fighter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return fighters;
    }

    public List<Fighter> getRecentlyRetiredFighters() {
        List<Fighter> fighters = new ArrayList<>();
        String query = "SELECT * FROM fighters WHERE status = 'retired' ORDER BY fighter_id DESC FETCH FIRST 10 ROWS ONLY";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Fighter fighter = extractFighterFromResultSet(rs);
                fighters.add(fighter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return fighters;
    }

    public Fighter getFighterById(int fighterId) {
        String query = "SELECT * FROM fighters WHERE fighter_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, fighterId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractFighterFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public List<Fighter> searchFighters(String searchTerm) {
        List<Fighter> fighters = new ArrayList<>();
        String query = "SELECT * FROM fighters WHERE LOWER(fighter_name) LIKE ? OR LOWER(nickname) LIKE ? " +
                      "ORDER BY fighter_name";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            String search = "%" + searchTerm.toLowerCase() + "%";
            pstmt.setString(1, search);
            pstmt.setString(2, search);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Fighter fighter = extractFighterFromResultSet(rs);
                fighters.add(fighter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return fighters;
    }

    public boolean insertFighter(Fighter fighter) {
        String query = "INSERT INTO fighters (fighter_id, fighter_name, nickname, weight_class, country, " +
                      "age, height, weight, reach, total_fights, wins, losses, draws, win_ratio, status, created_by) " +
                      "VALUES (fighters_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, 0, 0, 0, 0, 0.00, 'active', ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, fighter.getFighterName());
            pstmt.setString(2, fighter.getNickname());
            pstmt.setString(3, fighter.getWeightClass());
            pstmt.setString(4, fighter.getCountry());
            pstmt.setInt(5, fighter.getAge());
            pstmt.setDouble(6, fighter.getHeight());
            pstmt.setDouble(7, fighter.getWeight());
            pstmt.setDouble(8, fighter.getReach());
            pstmt.setInt(9, fighter.getCreatedBy());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateFighter(Fighter fighter) {
        String query = "UPDATE fighters SET fighter_name = ?, nickname = ?, weight_class = ?, country = ?, " +
                      "age = ?, height = ?, weight = ?, reach = ?, status = ? WHERE fighter_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, fighter.getFighterName());
            pstmt.setString(2, fighter.getNickname());
            pstmt.setString(3, fighter.getWeightClass());
            pstmt.setString(4, fighter.getCountry());
            pstmt.setInt(5, fighter.getAge());
            pstmt.setDouble(6, fighter.getHeight());
            pstmt.setDouble(7, fighter.getWeight());
            pstmt.setDouble(8, fighter.getReach());
            pstmt.setString(9, fighter.getStatus());
            pstmt.setInt(10, fighter.getFighterId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateFighterRecord(int fighterId, int wins, int losses, int draws) {
        String query = "UPDATE fighters SET wins = ?, losses = ?, draws = ?, " +
                      "total_fights = wins + losses + draws, " +
                      "win_ratio = CASE WHEN (wins + losses + draws) > 0 " +
                      "THEN (wins / (wins + losses + draws)) * 100 ELSE 0 END " +
                      "WHERE fighter_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, wins);
            pstmt.setInt(2, losses);
            pstmt.setInt(3, draws);
            pstmt.setInt(4, fighterId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean retireFighter(int fighterId) {
        String query = "UPDATE fighters SET status = 'retired' WHERE fighter_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, fighterId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFighter(int fighterId) {
        String query = "DELETE FROM fighters WHERE fighter_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, fighterId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Fighter extractFighterFromResultSet(ResultSet rs) throws SQLException {
        Fighter fighter = new Fighter();
        fighter.setFighterId(rs.getInt("fighter_id"));
        fighter.setFighterName(rs.getString("fighter_name"));
        fighter.setNickname(rs.getString("nickname"));
        fighter.setWeightClass(rs.getString("weight_class"));
        fighter.setCountry(rs.getString("country"));
        fighter.setAge(rs.getInt("age"));
        fighter.setHeight(rs.getDouble("height"));
        fighter.setWeight(rs.getDouble("weight"));
        fighter.setReach(rs.getDouble("reach"));
        fighter.setTotalFights(rs.getInt("total_fights"));
        fighter.setWins(rs.getInt("wins"));
        fighter.setLosses(rs.getInt("losses"));
        fighter.setDraws(rs.getInt("draws"));
        fighter.setWinRatio(rs.getDouble("win_ratio"));
        fighter.setStatus(rs.getString("status"));
        fighter.setCreatedBy(rs.getInt("created_by"));
        
        return fighter;
    }
}