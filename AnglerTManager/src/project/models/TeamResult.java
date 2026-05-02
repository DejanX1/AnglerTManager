package project.models;

public class TeamResult {
    private String teamName;
    private int rank;
    private double totalCatch;


    public TeamResult(String teamName, int rank, double totalCatch) {
        this.teamName = teamName;
        this.rank = rank;
        this.totalCatch = totalCatch;
        //this.position = position;
    }

    public String getTeamName() { return teamName; }
    public int getRank() { return rank; }
    public double getTotalCatch() { return totalCatch; }
}