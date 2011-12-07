package de.pribluda.android.camerawatch.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import de.pribluda.android.camerawatch.Configuration;

/**
 * legacy location processor,  simulates single location updates
 *
 * @author Konstantin Pribluda
 */
public class LegacyLocationProcessor implements LocationProcessor {
    public static final String LOG_TAG = "camerawatch.legacyLocationProcesor";
    Location cached;

    final Context context;
    private final Configuration configuration;
    private final LocationManager locationManager;

    /**
     * initialiyze  legacy provided
     *
     * @param context
     */
    public LegacyLocationProcessor(Context context) {
        this.context = context;
        configuration = Configuration.getInstance(context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

    }


    public Location retrieveLocation() {

        long bestAfter = System.currentTimeMillis() - configuration.getMaxAcceptableAge();
        float bestAccuracy = Float.MAX_VALUE;
        long bestTime = Long.MIN_VALUE;

        // retrieve location manager

        Location candidate = cached;
        if (candidate != null) {
            bestAccuracy = candidate.getAccuracy();
            bestTime = candidate.getTime();
        }
        for (String serviceName : locationManager.getAllProviders()) {
            final android.location.Location lastKnownLocation = locationManager.getLastKnownLocation(serviceName);
            Log.d(LOG_TAG, "location candidate from :" + serviceName + " - " + lastKnownLocation);

            if (lastKnownLocation != null) {
                // first chedck whether time is better than before and acceptable
                final long lastKnownLocationTime = lastKnownLocation.getTime();
                final float lastKnownLocationAccuracy = lastKnownLocation.getAccuracy();

                // shall be better than before in location and accuracy,
                // otherwise fallback for best time and good enough accuracy
                if (bestTime < lastKnownLocationTime) {
                    // take this as candidate and update last known stuff
                    // also consider this as candidate as if accuracy and time are still in
                    // acceptable range
                    if ((bestAccuracy > lastKnownLocationAccuracy) || (configuration.getMaxAcceptableDinstance() > lastKnownLocationAccuracy && bestAfter < lastKnownLocationTime)) {
                        Log.d(LOG_TAG, "location candidate accepted:" + lastKnownLocation);
                        candidate = lastKnownLocation;
                        bestAccuracy = lastKnownLocationAccuracy;
                        bestTime = lastKnownLocationTime;
                    }
                }
            }
        }

        return candidate;

    }

    public void processLocationUpdate(Location location) {

    }

    public void requestLocationUpdate() {

    }
}
