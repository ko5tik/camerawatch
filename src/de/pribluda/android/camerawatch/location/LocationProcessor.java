package de.pribluda.android.camerawatch.location;

import android.location.Location;

/**
 * interface to location service.  concrete implementation will be
 * version and api dependent.
 *
 * @author Konstantin Pribluda
 */
public interface LocationProcessor {
    public static final String LOCATION_CHANGE_INTENT = "de.pribluda.android.camerawatch.LOCATION_CHANGED";
    public static final String LOCATION_STOP_UPDATES = "de.pribluda.android.camerawatch.LOCATION_CHANGED";

    /**
     * retrieve actual location as good as possible
     *
     * @return
     */
    Location retrieveLocation();

    Location processLocationUpdate(Location location);

    /**
     * request location update
     */
    void requestLocationUpdate();

    /**
     * stop location updates
     */
    void stopUpdates();
}
