package de.pribluda.android.camerawatch.location;

import android.location.Location;

/**
 * interface to location service.  concrete implementation will be
 * version and api dependent.
 *
 * @author Konstantin Pribluda
 */
public interface LocationProcessor {

    /**
     * retrieve actual location as good as possible
     *
     * @return
     */
    Location retrieveLocation();

    void processLocationUpdate(Location location);

    /**
     * request location update
     */
    void requestLocationUpdate();

}
