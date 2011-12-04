package de.pribluda.android.camerawatch;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * receives change location events and updates  status provider.  also fires up events to
 * update widget
 *
 * @author Konstantin Pribluda
 */
public class ChangeLocationReceiver extends BroadcastReceiver {
    public static final String LOG_TAG = "camerawatch.location_change";
    public static final String LOCATION_CHANGE_INTENT = "de.pribluda.android.camerawatch.LOCATION_CHANGED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "received location change:" + intent);
        // update widget with location data
        CameraWidgetProvider.displayCurrentState(context);
    }

    /**
     * initialize location change listener by sending pending intent on location manager
     *
     * @param context
     */

    public static void requestLocationChanges(Context context) {
        final Configuration config = Configuration.getInstance(context);
        LocationManager manager = getLocationService(context);

        // determine best provider for this
        Criteria criteria = new Criteria();
        criteria.setSpeedRequired(false);
        criteria.setAltitudeRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(config.getPowerRequirement());
        criteria.setAccuracy(config.getAccuracy());


        if (!manager.isProviderEnabled(config.getProvider())) {
            Log.d(LOG_TAG, "provider not availlable");
            // update widget to tell about lack of available providers
            CameraWidgetProvider.notifyNoProvider(context);
        } else {
            Log.d(LOG_TAG, "requesting periodic location updates");
            manager.requestLocationUpdates(config.getProvider(), config.getMinTime(), config.getMinDistance(), PendingIntent.getBroadcast(context, 0, new Intent(LOCATION_CHANGE_INTENT), PendingIntent.FLAG_UPDATE_CURRENT));
            CameraWidgetProvider.displayCurrentState(context);
        }
    }


    /**
     * retrieve actual location as good as possible
     *
     * @param maxDistance      maximal distance tolerance for provider to be considered
     * @param maxAcceptableAge maximal delay for the provider to be considered
     * @return
     */
    public static Location lastBestLocation(final Context context, final float maxDistance, final long maxAcceptableAge) {

        long bestAfter = System.currentTimeMillis() - maxAcceptableAge;
        float bestAccuracy = Float.MAX_VALUE;
        long bestTime = Long.MIN_VALUE;

        // retrieve location manager
        final LocationManager locationService = getLocationService(context);
        Location candidate = null;

        for (String serviceName : locationService.getAllProviders()) {
            final android.location.Location lastKnownLocation = locationService.getLastKnownLocation(serviceName);
            Log.d(LOG_TAG, "location candidate from :" +serviceName + " - " +  lastKnownLocation);

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
                    if ((bestAccuracy > lastKnownLocationAccuracy) || (maxDistance > lastKnownLocationAccuracy && bestAfter < lastKnownLocationTime)) {
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


    private static LocationManager getLocationService(Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

}
