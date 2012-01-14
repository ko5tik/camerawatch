package de.pribluda.android.camerawatch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * handles periodic location updates.  provides convenient static methods to activate and
 * deactivate. handles widget updates
 *
 * @author Konstantin Pribluda
 */
public class UpdateReceiver extends BroadcastReceiver {
    public static final String LOG_TAG = "camerawatch.alarmactivator";
    public static final String ACTION = "de.pribluda.android.camerawatch.UPDATE_ALARM";
    public static final Intent INTENT = new Intent(ACTION);

    public static void activate(Context context) {

        final Configuration configuration = Configuration.getInstance(context);



        deactivate(context);

        int widgetUpdateInterval = configuration.getWidgetUpdateInterval();
        if (widgetUpdateInterval > 0) {

            final AlarmManager alarmService = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, INTENT, 0);
            //  schedule it   ASAP


            // schedule new alarm
            alarmService.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + widgetUpdateInterval * 60000, widgetUpdateInterval * 60000, pendingIntent);
            Log.d(LOG_TAG, "scheduled pending intent after: " + widgetUpdateInterval);
        }

    }

    public static void deactivate(Context context) {

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, INTENT, PendingIntent.FLAG_NO_CREATE);


        // cancel if already there
        if (pendingIntent != null) {
            Log.d(LOG_TAG, "deactivated intent");
            pendingIntent.cancel();
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "received intent, update state");
        CameraWidgetProvider.displayCurrentState(context);
    }
}
