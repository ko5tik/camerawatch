package de.pribluda.android.camerawatch.location;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
    public static final int CANCEL_INTERVAL = 60000;
    Location cached;

    final Context context;
    private final Configuration configuration;
    private final LocationManager locationManager;

    // intent to use for location updates request
    private final Intent sendUpdatesIntent;
    // intent to use for canceling update request
    private final Intent stopUpdatesIntent;

    /**
     * initialize  legacy provider
     *
     * @param context
     */
    public LegacyLocationProcessor(Context context) {
        this.context = context;
        configuration = Configuration.getInstance(context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        sendUpdatesIntent = new Intent(LOCATION_CHANGE_INTENT);
        stopUpdatesIntent = new Intent(LOCATION_CHANGE_INTENT).putExtra(LOCATION_STOP_UPDATES, "xx");
    }


    /**
     * retrieve location taking caches instance into account, as well as  last locations of all available providers.
     *
     * @return
     */
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

    public Location processLocationUpdate(Location location) {
        Log.d(LOG_TAG, "updating location:" + location);

        cached = location;
        stopUpdates();
        return cached;
    }

    public void stopUpdates() {
        // disable location sending to save battery
        locationManager.removeUpdates(PendingIntent.getBroadcast(context, 0, sendUpdatesIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        Log.d(LOG_TAG, "canceled location updates");
    }

    /**
     * request one location update from all configured providers.
     */
    public void requestLocationUpdate() {


        boolean hasLocationUpdates = false;


        for (String provider : configuration.getActiveProvider().split("\\|")) {

            if (locationManager.isProviderEnabled(provider)) {
                Log.d(LOG_TAG, "requesting periodic location update from: " + provider);
                scheduleLocationUpdates(provider);

                hasLocationUpdates = true;
            }
        }
        if (hasLocationUpdates) {
            scheduleCancelationIntent();
        }


    }


    private void scheduleLocationUpdates(String provider) {
        locationManager.requestLocationUpdates(provider, 0, 0, PendingIntent.getBroadcast(context, 0, sendUpdatesIntent, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    private void scheduleCancelationIntent() {
        // schedule cancelation of location service
        final PendingIntent cancelIntent = PendingIntent.getBroadcast(context, 0, stopUpdatesIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager alarmService = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmService.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + CANCEL_INTERVAL, configuration.getWidgetUpdateInterval(), cancelIntent);
    }


}
