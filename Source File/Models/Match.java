package models;

import java.sql.Timestamp;

public class Match {
    private int matchId;
    private int eventId;
    private int fighter1Id;
    private int fighter2Id;
    private String weightClass;
    private String matchType;  // 'main_card', 'preliminary', 'early_preliminary'
    private int scheduledRounds;
    private Integer winnerId;
    private String finishMethod;  // 'KO', 'TKO', 'Submission', 'Decision', 'Draw', 'No_Contest'
    private Integer finishRound;
    private Timestamp matchDate;
    private String status;  // 'scheduled', 'completed', 'cancelled'
    private int createdBy;

    // Constructors
    public Match() {}

    public Match(int eventId, int fighter1Id, int fighter2Id, String weightClass, 
                 String matchType, int scheduledRounds) {
        this.eventId = eventId;
        this.fighter1Id = fighter1Id;
        this.fighter2Id = fighter2Id;
        this.weightClass = weightClass;
        this.matchType = matchType;
        this.scheduledRounds = scheduledRounds;
        this.status = "scheduled";
    }

    // Getters and Setters
    public int getMatchId() { 
        return matchId; 
    }
    
    public void setMatchId(int matchId) { 
        this.matchId = matchId; 
    }

    public int getEventId() { 
        return eventId; 
    }
    
    public void setEventId(int eventId) { 
        this.eventId = eventId; 
    }

    public int getFighter1Id() { 
        return fighter1Id; 
    }
    
    public void setFighter1Id(int fighter1Id) { 
        this.fighter1Id = fighter1Id; 
    }

    public int getFighter2Id() { 
        return fighter2Id; 
    }
    
    public void setFighter2Id(int fighter2Id) { 
        this.fighter2Id = fighter2Id; 
    }

    public String getWeightClass() { 
        return weightClass; 
    }
    
    public void setWeightClass(String weightClass) { 
        this.weightClass = weightClass; 
    }

    public String getMatchType() { 
        return matchType; 
    }
    
    public void setMatchType(String matchType) { 
        this.matchType = matchType; 
    }

    public int getScheduledRounds() { 
        return scheduledRounds; 
    }
    
    public void setScheduledRounds(int scheduledRounds) { 
        this.scheduledRounds = scheduledRounds; 
    }

    public Integer getWinnerId() { 
        return winnerId; 
    }
    
    public void setWinnerId(Integer winnerId) { 
        this.winnerId = winnerId; 
    }

    public String getFinishMethod() { 
        return finishMethod; 
    }
    
    public void setFinishMethod(String finishMethod) { 
        this.finishMethod = finishMethod; 
    }

    public Integer getFinishRound() { 
        return finishRound; 
    }
    
    public void setFinishRound(Integer finishRound) { 
        this.finishRound = finishRound; 
    }

    public Timestamp getMatchDate() { 
        return matchDate; 
    }
    
    public void setMatchDate(Timestamp matchDate) { 
        this.matchDate = matchDate; 
    }

    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }

    public int getCreatedBy() { 
        return createdBy; 
    }
    
    public void setCreatedBy(int createdBy) { 
        this.createdBy = createdBy; 
    }

    // Utility Methods
    public boolean isScheduled() {
        return "scheduled".equalsIgnoreCase(status);
    }

    public boolean isCompleted() {
        return "completed".equalsIgnoreCase(status);
    }

    public boolean isCancelled() {
        return "cancelled".equalsIgnoreCase(status);
    }

    public boolean isDraw() {
        return winnerId == null && "Draw".equalsIgnoreCase(finishMethod);
    }

    @Override
    public String toString() {
        return "Match{" +
                "matchId=" + matchId +
                ", eventId=" + eventId +
                ", fighter1=" + fighter1Id +
                " vs fighter2=" + fighter2Id +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return matchId == match.matchId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(matchId);
    }
}