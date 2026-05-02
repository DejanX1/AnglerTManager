package project.models;

import java.sql.Date;

public class Competition {

    private int id;
    private String name;
    private Date date;
    private String location;
    private int fishingMethodId;
    private int lakeId;
    private String methodName;
    private String lakeName;

    public Competition() {}

    public Competition(int id, String name, Date date, String location, int fishingMethodId, int lakeId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.location = location;
        this.fishingMethodId = fishingMethodId;
        this.lakeId = lakeId;
    }

    public Competition(int id, String name, Date date, String location, int fishingMethodId, int lakeId, String methodName, String lakeName) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.location = location;
        this.fishingMethodId = fishingMethodId;
        this.lakeId = lakeId;
        this.methodName = methodName;
        this.lakeName = lakeName;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public Date getDate() { return date; }
    public String getLocation() { return location; }
    public int getFishingMethodId() { return fishingMethodId; }
    public int getLakeId() { return lakeId; }
    public String getMethodName() { return methodName; }
    public String getLakeName() { return lakeName; }

    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
}
