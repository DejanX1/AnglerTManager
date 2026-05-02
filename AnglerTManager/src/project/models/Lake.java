package project.models;

public class Lake {
    private int id;
    private String name;
    private String location;
    private double surfaceHa;

    public Lake(int id, String name, String location, double area) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.surfaceHa = area;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public double getArea() { return surfaceHa; }
}