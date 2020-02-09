package aplicacion.android.kvn.touruta.OBJECTS;

public class Checkpoint {
    private int checkpointTourId;
    private double lat;
    private double lon;
    private String name;

    public Checkpoint() {
    }

    public Checkpoint(int checkpointTourId, double lat, double lon, String name) {
        this.checkpointTourId = checkpointTourId;
        this.lat = lat;
        this.lon = lon;
        this.name = name;
    }

    public int getCheckpointTourId() {
        return checkpointTourId;
    }

    public void setCheckpointTourId(int checkpointTourId) {
        this.checkpointTourId = checkpointTourId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
