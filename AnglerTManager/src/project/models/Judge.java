package project.models;

public class Judge {
    private int id;
    private String firstName;
    private String lastName;
    private String license;

    public Judge(int id, String firstName, String lastName, String license) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.license = license;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getLicense() { return license; }
}