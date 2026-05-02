package project.models;

public class CompetitorResult {
    private int competitorId;
    private int competitionId;
    private double totalWeight;
    private int rank;
    private String fullNameOfCompetitor;

    public CompetitorResult(int competitorId, String fullNameOfCompetitor, int competitionId, double totalWeight, int rank) {
        this.competitorId = competitorId;
        this.fullNameOfCompetitor = fullNameOfCompetitor;
        this.competitionId = competitionId;
        this.totalWeight = totalWeight;
        this.rank = rank;
    }

    public int getCompetitorId() { return competitorId; }
    public String getFullNameOfCompetitor() {return fullNameOfCompetitor;};
    public int getCompetitionId() { return competitionId; }
    public double getTotalWeight() { return totalWeight; }
    public int getRank() { return rank; }
}