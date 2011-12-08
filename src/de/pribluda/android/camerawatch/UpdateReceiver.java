package de.pribluda.android.camerawatch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * activate periodic alarms
 *
 * @author Konstantin Pribluda
 */
public class UpdateReceiver extends BroadcastReceiver {
    public static final String LOG_TAG = "camerawatch.alarmactivator";
    public static final String ACTION = "de.pribluda.android.camerawatch.UPDATE_ALARM";

    public static void activate(Context context) {
        AlarmManager alarmService = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        Log.d(LOG_TAG, "pending intent: " + pendingIntent);
        //  if no intent there, schedule it   ASAP
        if (pendingIntent == null) {
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            // schedule new alarm in 15 minutes
            alarmService.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(),300000, pendingIntent);
            Log.d(LOG_TAG, "scheduled intent: " + pendingIntent);
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "received intent: " + intent);
        CameraWidgetProvider.displayCurrentState(context);
    }
}
