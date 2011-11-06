package de.pribluda.android.camerawatch.data;

/**
 * represents area where camera data is available.  area name corresponds to coarse location
 */
public class Area {

    String locality;
    String url;

    /**
     * locality of dataset
     * @return
     */
    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    /**
     * URL providing 
     * @return
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
