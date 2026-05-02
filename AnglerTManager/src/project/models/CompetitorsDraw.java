package project.models;

public class CompetitorsDraw {
    private int position;
    private String competition;
    private String firstName;
    private String lastName;
    private String team;
    private int sector;

    public CompetitorsDraw(int position, String competition, String firstName, String lastName, String team, int sector) {
        this.position = position;
        this.competition = competition;
        this.firstName = firstName;
        this.lastName = lastName;
        this.team = team;
        this.sector = sector;
    }

    public int getPosition() { return position; }
    public String getCompetition() { return competition; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getTeam() { return team; }
    public int getSector() { return sector; }
}