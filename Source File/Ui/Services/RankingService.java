package services;

import dao.RankingDAO;
import dao.FighterDAO;
import models.Ranking;
import models.Fighter;
import java.util.*;
import java.util.stream.Collectors;

public class RankingService {
    private RankingDAO rankingDAO;
    private FighterDAO fighterDAO;
    
    private static final String[] WEIGHT_CLASSES = {
        "Heavyweight", "Light Heavyweight", "Middleweight", "Welterweight",
        "Lightweight", "Featherweight", "Bantamweight", "Flyweight"
    };

    public RankingService() {
        this.rankingDAO = new RankingDAO();
        this.fighterDAO = new FighterDAO();
    }

    /**
     * Calculate P4P ranking points for a fighter
     * Formula: (Win Ratio * 100) + (Total Wins * 0.5) + (Total Fights * 0.1)
     */
    public double calculatePoints(Fighter fighter) {
        if (fighter.getTotalFights() == 0) {
            return 0.0;
        }
        
        double winRatioScore = fighter.getWinRatio();
        double winsBonus = fighter.getWins() * 0.5;
        double experienceBonus = fighter.getTotalFights() * 0.1;
        
        return winRatioScore + winsBonus + experienceBonus;
    }

    /**
     * Recalculate all rankings across all weight classes
     */
    public int recalculateAllRankings() {
        int totalUpdated = 0;
        
        for (String weightClass : WEIGHT_CLASSES) {
            totalUpdated += recalculateWeightClassRankings(weightClass);
        }
        
        return totalUpdated;
    }

    /**
     * Recalculate rankings for a specific weight class
     */
    public int recalculateWeightClassRankings(String weightClass) {
        // Get all active fighters in this weight class
        List<Fighter> fighters = fighterDAO.getFightersByWeightClass(weightClass);
        
        if (fighters == null || fighters.isEmpty()) {
            return 0;
        }
        
        // Calculate points for each fighter
        Map<Fighter, Double> fighterPoints = new HashMap<>();
        for (Fighter fighter : fighters) {
            double points = calculatePoints(fighter);
            fighterPoints.put(fighter, points);
        }
        
        // Sort fighters by points (descending)
        List<Map.Entry<Fighter, Double>> sortedFighters = fighterPoints.entrySet()
            .stream()
            .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
            .collect(Collectors.toList());
        
        // Update rankings in database
        int rank = 1;
        int updated = 0;
        
        for (Map.Entry<Fighter, Double> entry : sortedFighters) {
            Fighter fighter = entry.getKey();
            double points = entry.getValue();
            
            Ranking ranking = new Ranking(
                fighter.getFighterId(),
                weightClass,
                rank,
                points
            );
            
            if (rankingDAO.insertOrUpdateRanking(ranking)) {
                updated++;
            }
            
            rank++;
        }
        
        return updated;
    }

    /**
     * Get top N fighters in a weight class
     */
    public List<Ranking> getTopRankings(String weightClass, int limit) {
        List<Ranking> rankings = rankingDAO.getRankingsByWeightClass(weightClass);
        
        if (rankings == null) {
            return new ArrayList<>();
        }
        
        return rankings.stream()
            .limit(limit)
            .collect(Collectors.toList());
    }

    /**
     * Get fighter's rank in their weight class
     */
    public Integer getFighterRank(int fighterId) {
        Fighter fighter = fighterDAO.getFighterById(fighterId);
        
        if (fighter == null) {
            return null;
        }
        
        Ranking ranking = rankingDAO.getRankingByFighterAndWeightClass(
            fighterId, 
            fighter.getWeightClass()
        );
        
        return ranking != null ? ranking.getRankPosition() : null;
    }
}