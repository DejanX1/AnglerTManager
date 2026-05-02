package project.models;

public class Sector {

    private int sectorId;
    private int orderNumber;
    private int lakeId;
    private String lakeName;


    public Sector(int sectorId, int orderNumber, int lakeId, String lakeName) {
        this.sectorId = sectorId;
        this.orderNumber = orderNumber;
        this.lakeId = lakeId;
        this.lakeName = lakeName;
    }

    public int getSectorId() { return sectorId; }
    public int getOrderNumber() { return orderNumber; }
    public int getLakeId() { return lakeId; }
    public String getLakeName() { return lakeName; }

}