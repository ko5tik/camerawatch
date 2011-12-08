package de.pribluda.android.camerawatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;


/**
 * receives screen on event to poke service for update  location
 *
 * @author Konstantin Pribluda
 * @deprecated no way to register this from receiver.
 */
public class ScreenOnReceiver extends BroadcastReceiver {
    public static final String LOG_TAG = "camerawatch.screen_on";

    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "received screen on event");



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
