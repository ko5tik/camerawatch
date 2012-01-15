package de.pribluda.android.camerawatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import de.pribluda.android.camerawatch.location.LocationProcessor;
import de.pribluda.android.camerawatch.location.LocationProcessorProvider;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * receives change location events and updates  status provider.  also fires up events to
 * update widget.
 *
 * @author Konstantin Pribluda
 */
public class ChangeLocationReceiver extends BroadcastReceiver {
    public static final String LOG_TAG = "camerawatch.location_change";



    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "received intent: " + intent);

        final LocationProcessor locationProcessor = LocationProcessorProvider.instance(context);


        // have we received new location?
        if (intent.hasExtra(LocationManager.KEY_LOCATION_CHANGED)) {
            Location location = (Location) intent.getExtras().get(LocationManager.KEY_LOCATION_CHANGED);

            Log.d(LOG_TAG, "received new location: " + location);
            location =  locationProcessor.processLocationUpdate(location);
            // update widget with location data
            try {
                CameraWidgetProvider.displayCurrentState(context);
            } catch (Exception e) {
                Log.d(LOG_TAG, "exception while updating widget", e);
            }
        }  else if(intent.hasExtra(LocationProcessor.LOCATION_STOP_UPDATES)) {
            Log.d(LOG_TAG, "canceling location update");
            locationProcessor.stopUpdates();
        }
    }


}
