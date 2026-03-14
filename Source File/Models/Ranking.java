package models;

import java.sql.Timestamp;

public class Ranking {
    private int rankingId;
    private int fighterId;
    private String weightClass;
    private int rankPosition;
    private double points;
    private Timestamp lastUpdated;
    
    // For display purposes
    private String fighterName;
    private String nickname;
    private int wins;
    private int losses;
    private int draws;

    // Constructors
    public Ranking() {}

    public Ranking(int fighterId, String weightClass, int rankPosition, double points) {
        this.fighterId = fighterId;
        this.weightClass = weightClass;
        this.rankPosition = rankPosition;
        this.points = points;
    }

    // Getters and Setters
    public int getRankingId() { 
        return rankingId; 
    }
    
    public void setRankingId(int rankingId) { 
        this.rankingId = rankingId; 
    }

    public int getFighterId() { 
        return fighterId; 
    }
    
    public void setFighterId(int fighterId) { 
        this.fighterId = fighterId; 
    }

    public String getWeightClass() { 
        return weightClass; 
    }
    
    public void setWeightClass(String weightClass) { 
        this.weightClass = weightClass; 
    }

    public int getRankPosition() { 
        return rankPosition; 
    }
    
    public void setRankPosition(int rankPosition) { 
        this.rankPosition = rankPosition; 
    }

    public double getPoints() { 
        return points; 
    }
    
    public void setPoints(double points) { 
        this.points = points; 
    }

    public Timestamp getLastUpdated() { 
        return lastUpdated; 
    }
    
    public void setLastUpdated(Timestamp lastUpdated) { 
        this.lastUpdated = lastUpdated; 
    }

    public String getFighterName() { 
        return fighterName; 
    }
    
    public void setFighterName(String fighterName) { 
        this.fighterName = fighterName; 
    }

    public String getNickname() { 
        return nickname; 
    }
    
    public void setNickname(String nickname) { 
        this.nickname = nickname; 
    }

    public int getWins() { 
        return wins; 
    }
    
    public void setWins(int wins) { 
        this.wins = wins; 
    }

    public int getLosses() { 
        return losses; 
    }
    
    public void setLosses(int losses) { 
        this.losses = losses; 
    }

    public int getDraws() { 
        return draws; 
    }
    
    public void setDraws(int draws) { 
        this.draws = draws; 
    }

    // Utility Methods
    public String getRecord() {
        return wins + "-" + losses + "-" + draws;
    }

    @Override
    public String toString() {
        return "#" + rankPosition + " - " + fighterName + 
               (nickname != null && !nickname.isEmpty() ? " \"" + nickname + "\"" : "") +
               " (" + weightClass + ") - " + String.format("%.2f", points) + " pts";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ranking ranking = (Ranking) o;
        return rankingId == ranking.rankingId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(rankingId);
    }
}