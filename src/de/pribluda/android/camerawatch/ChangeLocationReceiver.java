package de.pribluda.android.camerawatch;

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
        // .....

    }

    /**
     * initialize location change listener by sending pending intent on location manager
     *
     * @param context
     */
    public static void receiveLocationChanges(Context context) throws IllegalAccessException {
        final Configuration config = Configuration.getInstance(context);
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // determine best provider for this
        Criteria criteria = new Criteria();
        criteria.setSpeedRequired(false);
        criteria.setAltitudeRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(config.getPowerRequirement());
    }
}
