package de.pribluda.android.camerawatch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * handles periodic widget updates.  provides convenient static methods to activate and
 * deactivate periodic widget updates
 *
 * @author Konstantin Pribluda
 */
public class UpdateReceiver extends BroadcastReceiver {
    public static final String LOG_TAG = "camerawatch.alarmactivator";
    public static final String ACTION = "de.pribluda.android.camerawatch.UPDATE_ALARM";

    public static void activate(Context context) {

        final Configuration configuration = Configuration.getInstance(context);

        final AlarmManager alarmService = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        deactivate(context);

        Intent intent = new Intent(ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        //  schedule it   ASAP


        // schedule new alarm
        alarmService.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + configuration.getWidgetUpdateInterval(), configuration.getWidgetUpdateInterval(), pendingIntent);
        Log.d(LOG_TAG, "scheduled pending intent vor:" + configuration.getWidgetUpdateInterval());


    }

    private static void deactivate(Context context) {
        Intent intent = new Intent(ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE);

        Log.d(LOG_TAG, "deactivating updates? ...");
        // cancel if already there
        if (pendingIntent != null) {
            Log.d(LOG_TAG, "deactivated intent");
            pendingIntent.cancel();
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "received intent: " + intent);
        CameraWidgetProvider.displayCurrentState(context);
    }
}
