
package models;

public class Fighter {
    private int fighterId;
    private String fighterName;
    private String nickname;
    private String weightClass;
    private String country;
    private int age;
    private double height;
    private double weight;
    private double reach;
    private int totalFights;
    private int wins;
    private int losses;
    private int draws;
    private double winRatio;
    private String status;  // 'active' or 'retired'
    private int createdBy;

    // Constructors
    public Fighter() {}

    public Fighter(String fighterName, String nickname, String weightClass, 
                   String country, int age, double height, double weight, double reach) {
        this.fighterName = fighterName;
        this.nickname = nickname;
        this.weightClass = weightClass;
        this.country = country;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.reach = reach;
        this.status = "active";
        this.totalFights = 0;
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
        this.winRatio = 0.0;
    }

    // Getters and Setters
    public int getFighterId() { 
        return fighterId; 
    }
    
    public void setFighterId(int fighterId) { 
        this.fighterId = fighterId; 
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

    public String getWeightClass() { 
        return weightClass; 
    }
    
    public void setWeightClass(String weightClass) { 
        this.weightClass = weightClass; 
    }

    public String getCountry() { 
        return country; 
    }
    
    public void setCountry(String country) { 
        this.country = country; 
    }

    public int getAge() { 
        return age; 
    }
    
    public void setAge(int age) { 
        this.age = age; 
    }

    public double getHeight() { 
        return height; 
    }
    
    public void setHeight(double height) { 
        this.height = height; 
    }

    public double getWeight() { 
        return weight; 
    }
    
    public void setWeight(double weight) { 
        this.weight = weight; 
    }

    public double getReach() { 
        return reach; 
    }
    
    public void setReach(double reach) { 
        this.reach = reach; 
    }

    public int getTotalFights() { 
        return totalFights; 
    }
    
    public void setTotalFights(int totalFights) { 
        this.totalFights = totalFights; 
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

    public double getWinRatio() { 
        return winRatio; 
    }
    
    public void setWinRatio(double winRatio) { 
        this.winRatio = winRatio; 
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
    public void calculateWinRatio() {
        if (totalFights > 0) {
            this.winRatio = ((double) wins / totalFights) * 100;
        } else {
            this.winRatio = 0.0;
        }
    }

    public void updateTotalFights() {
        this.totalFights = wins + losses + draws;
    }

    public void addWin() {
        this.wins++;
        updateTotalFights();
        calculateWinRatio();
    }

    public void addLoss() {
        this.losses++;
        updateTotalFights();
        calculateWinRatio();
    }

    public void addDraw() {
        this.draws++;
        updateTotalFights();
        calculateWinRatio();
    }

    public boolean isActive() {
        return "active".equalsIgnoreCase(status);
    }

    public boolean isRetired() {
        return "retired".equalsIgnoreCase(status);
    }

    public String getRecord() {
        return wins + "-" + losses + "-" + draws;
    }

    @Override
    public String toString() {
        return fighterName + (nickname != null && !nickname.isEmpty() ? " \"" + nickname + "\"" : "") + 
               " - " + weightClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fighter fighter = (Fighter) o;
        return fighterId == fighter.fighterId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(fighterId);
    }
}