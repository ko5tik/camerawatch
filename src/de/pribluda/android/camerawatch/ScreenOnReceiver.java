package de.pribluda.android.camerawatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.util.Log;


/**
 * receives screen on event to poke service for update  location
 *
 * @author Konstantin Pribluda
 */
public class ScreenOnReceiver extends BroadcastReceiver {
    public static final String LOG_TAG = "camerawatch.screen_on";

    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "received screen on event");

        final Location location = ChangeLocationReceiver.lastBestLocation(context, 5000, 600);
        Log.d(LOG_TAG, "location: " + location);
    }


    /**
     * activate receiver
     *
     * @param context
     */
    public static void activate(Context context) {
        Log.d(LOG_TAG, "initialising receiver ");
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");

        context.registerReceiver(new ScreenOnReceiver(), filter);
    }
}
