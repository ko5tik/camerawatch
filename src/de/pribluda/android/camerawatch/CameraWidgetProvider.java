package de.pribluda.android.camerawatch;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * provides app widget displaying cameras in vicinity
 */
public class CameraWidgetProvider extends AppWidgetProvider {
    public static final String LOG_TAG = "camerawatch.widget";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "received intent:" + intent);
        super.onReceive(context, intent);
    }
}
