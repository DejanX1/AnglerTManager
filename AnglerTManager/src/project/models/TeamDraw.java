package project.models;

public class TeamDraw {

    private String teamName;
    private String sector1;
    private String sector2;
    private String sector3;

    public TeamDraw(String teamName, String sector1, String sector2, String sector3) {
        this.teamName = teamName;
        this.sector1 = sector1;
        this.sector2 = sector2;
        this.sector3 = sector3;
    }

    public String getTeamName() { return teamName; }
    public String getSector1() { return sector1; }
    public String getSector2() { return sector2; }
    public String getSector3() { return sector3; }
}