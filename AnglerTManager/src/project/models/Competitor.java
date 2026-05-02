package project.models;

public class Competitor {

    private int id;
    private String firstName;
    private String lastName;
    private String birthDate;
    private int teamId;
    private String teamName;

    public Competitor(int id, String firstName, String lastName, String birthDate, int teamId, String teamName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public Competitor(int id, String firstName, String lastName, String birthDate, int teamId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.teamId = teamId;
        //this.teamName = teamName;
    }

    public int getId() { return id; }
    public int getTeamId() { return teamId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getBirthDate() { return birthDate; }
    public String getTeamName() { return teamName; }
}