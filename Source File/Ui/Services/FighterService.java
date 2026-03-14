package services;

import dao.FighterDAO;
import dao.MatchDAO;
import models.Fighter;
import models.Match;
import java.util.List;

public class FighterService {
    private FighterDAO fighterDAO;
    private MatchDAO matchDAO;

    public FighterService() {
        this.fighterDAO = new FighterDAO();
        this.matchDAO = new MatchDAO();
    }

    /**
     * Update fighter record after match completion
     */
    public boolean updateFighterRecordAfterMatch(int matchId) {
        Match match = matchDAO.getMatchById(matchId);
        
        if (match == null || !match.isCompleted()) {
            return false;
        }
        
        Fighter fighter1 = fighterDAO.getFighterById(match.getFighter1Id());
        Fighter fighter2 = fighterDAO.getFighterById(match.getFighter2Id());
        
        if (fighter1 == null || fighter2 == null) {
            return false;
        }
        
        // Update records based on match result
        if (match.isDraw()) {
            // Both fighters get a draw
            fighter1.addDraw();
            fighter2.addDraw();
        } else if (match.getWinnerId() != null) {
            if (match.getWinnerId() == fighter1.getFighterId()) {
                fighter1.addWin();
                fighter2.addLoss();
            } else {
                fighter2.addWin();
                fighter1.addLoss();
            }
        }
        
        // Update database
        boolean result1 = fighterDAO.updateFighterRecord(fighter1.getFighterId(), 
            fighter1.getWins(), fighter1.getLosses(), fighter1.getDraws());
        boolean result2 = fighterDAO.updateFighterRecord(fighter2.getFighterId(), 
            fighter2.getWins(), fighter2.getLosses(), fighter2.getDraws());
        
        return result1 && result2;
    }

    /**
     * Get fighter statistics
     */
    public String getFighterStats(int fighterId) {
        Fighter fighter = fighterDAO.getFighterById(fighterId);
        
        if (fighter == null) {
            return "Fighter not found";
        }
        
        StringBuilder stats = new StringBuilder();
        stats.append("Name: ").append(fighter.getFighterName()).append("\n");
        stats.append("Nickname: ").append(fighter.getNickname()).append("\n");
        stats.append("Weight Class: ").append(fighter.getWeightClass()).append("\n");
        stats.append("Country: ").append(fighter.getCountry()).append("\n");
        stats.append("Record: ").append(fighter.getRecord()).append("\n");
        stats.append("Win Ratio: ").append(String.format("%.2f%%", fighter.getWinRatio())).append("\n");
        stats.append("Status: ").append(fighter.getStatus()).append("\n");
        
        return stats.toString();
    }

    /**
     * Get top fighters by win ratio
     */
    public List<Fighter> getTopFighters(int limit) {
        List<Fighter> allFighters = fighterDAO.getAllActiveFighters();
        
        return allFighters.stream()
            .sorted((f1, f2) -> Double.compare(f2.getWinRatio(), f1.getWinRatio()))
            .limit(limit)
            .toList();
    }

    /**
     * Validate fighter data
     */
    public boolean validateFighterData(Fighter fighter) {
        if (fighter.getFighterName() == null || fighter.getFighterName().trim().isEmpty()) {
            return false;
        }
        
        if (fighter.getWeightClass() == null || fighter.getWeightClass().trim().isEmpty()) {
            return false;
        }
        
        if (fighter.getAge() < 18 || fighter.getAge() > 50) {
            return false;
        }
        
        if (fighter.getHeight() <= 0 || fighter.getWeight() <= 0 || fighter.getReach() <= 0) {
            return false;
        }
        
        return true;
    }
}