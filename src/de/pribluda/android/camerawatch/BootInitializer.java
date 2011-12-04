package de.pribluda.android.camerawatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * broadcast receiver for boot complete event.  fires up location updates
 * @deprecated  do not need this
 */
public class BootInitializer extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("camerawatch.bootcomplete", "received boot complete broadcast");



    }
}
