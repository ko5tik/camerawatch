package de.pribluda.android.camerawatch.data;

/**
 * Bean describing camera
 */
public class Camera implements Comparable<Camera> {
    String repotrer;
    String titel;
    String Typ;
    String owner;
    double longitude;
    double latitude;
    String description;
    String designator;
    String ownerType;
    String ID;
    double distance;

    /**
     * camera identifier. used to construct detail URL
     *
     * @return
     */
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDesignator() {
        return designator;
    }

    public void setDesignator(String designator) {
        this.designator = designator;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getRepotrer() {
        return repotrer;
    }

    public void setRepotrer(String repotrer) {
        this.repotrer = repotrer;
    }

    public String getTyp() {
        return Typ;
    }

    public void setTyp(String typ) {
        this.Typ = typ;
    }

    /**
     * distance to current point of view. will be set while filtering camera list
     * @return
     */
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * compare based on distance set
     * @param camera
     * @return
     */
    public int compareTo(Camera camera) {

        if(camera == null) {
            return 1;
        }
        return (int) (getDistance() - camera.getDistance());
    }
}
