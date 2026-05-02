package project.models;

public class Team {
    private int id;
    private String name;
    private String association;

    public Team(int id, String name, String association) {
        this.id = id;
        this.name = name;
        this.association = association;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getAssociation() { return association; }
}