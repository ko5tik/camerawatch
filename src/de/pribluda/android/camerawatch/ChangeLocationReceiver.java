package de.pribluda.android.camerawatch;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.util.Log;

/**
 * receives change location events and updates  status provider.  also fires up events to
 * update widget
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
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

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
            manager.requestLocationUpdates(config.getProvider(), config.getMinTime(), config.getMinDistance(), PendingIntent.getBroadcast(context, 0, new Intent(LOCATION_CHANGE_INTENT), PendingIntent.FLAG_UPDATE_CURRENT ));
            CameraWidgetProvider.displayCurrentState(context);
        }
    }
}
