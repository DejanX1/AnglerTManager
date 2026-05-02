package project.models;

public class Catch {
    private int id;
    private int idCompetition;
    private String competitor;
    private String fishSpecies;
    private double weight;
    private String time;

    public Catch(int id, int idCompetition, String competitor, String fishSpecies, double weight, String time) {
        this.id = id;
        this.idCompetition = idCompetition;
        this.competitor = competitor;
        this.fishSpecies = fishSpecies;
        this.weight = weight;
        this.time = time;
    }

    public int getId() { return id; }
    public int getIdCompetition() { return idCompetition; }
    public String getCompetitor() { return competitor; }
    public String getFishSpecies() { return fishSpecies; }
    public double getWeight() { return weight; }
    public String getTime() { return time; }

}