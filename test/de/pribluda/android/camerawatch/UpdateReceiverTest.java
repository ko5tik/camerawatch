package de.pribluda.android.camerawatch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import mockit.Expectations;
import mockit.Mock;
import mockit.Mocked;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * test proper function of update receiver
 *
 * @author Konstantin Pribluda
 */
public class UpdateReceiverTest {

    /**
     * upon activation recurring alarm shall be set with interval specified in
     * config class. old one shall be deactivated
     */
    @Test
    public void testReceiverActivation(@Mocked(methods = {"activate"}, inverse = true) final UpdateReceiver updateReceiver,
                                       @Mocked final Context context,
                                       @Mocked final Configuration configuration,
                                       @Mocked final AlarmManager alarmManager,
                                       @Mocked final PendingIntent pendingIntent,
                                       @Mocked System system,
                                       @Mocked Log log) {
        new Expectations() {
            {
                // shall retrieve configiration
                Configuration.getInstance(context);
                returns(configuration);


                // shall deactivate alarm just in case
                invoke(UpdateReceiver.class, "deactivate", context);

                // set to 3 minute activation
                configuration.getWidgetUpdateInterval();
                returns(3);


                context.getSystemService(Context.ALARM_SERVICE);
                returns(alarmManager);

                // create new pending intent
                PendingIntent.getBroadcast(context, 0, UpdateReceiver.INTENT, 0);
                returns(pendingIntent);

                // retrieve actual time
                System.currentTimeMillis();
                returns(239l);
                // activate alarm
                alarmManager.setInexactRepeating(AlarmManager.RTC, 239 + 180000, 180000, pendingIntent);

                Log.d(UpdateReceiver.LOG_TAG, "scheduled pending intent after: 3");
            }
        };

        UpdateReceiver.activate(context);
    }


    /**
     * U NO  UPDATE if interval is set to 0
     */
    @Test
    public void testNoActivationIfUpdateAufSetToZero(@Mocked(methods = {"activate"}, inverse = true) final UpdateReceiver updateReceiver,
                                                     @Mocked final Context context,
                                                     @Mocked final Configuration configuration,
                                                     @Mocked final AlarmManager alarmManager,
                                                     @Mocked final PendingIntent pendingIntent) {
        new Expectations() {
            {
                // shall retrieve configiration
                Configuration.getInstance(context);
                returns(configuration);


                // shall deactivate alarm just in case
                invoke(UpdateReceiver.class, "deactivate", context);

                // set to 3 minute activation
                configuration.getWidgetUpdateInterval();
                returns(0);


            }
        };


        UpdateReceiver.activate(context);
    }


    /**
     * shall cancel pending intent
     */
    @Test
    public void testProperDeactivation(@Mocked(methods = {"deactivate"}, inverse = true) final UpdateReceiver updateReceiver,
                                       @Mocked final Context context,
                                       @Mocked final PendingIntent pendingIntent,
                                       @Mocked Log l) {

        new Expectations() {
            {
                PendingIntent.getBroadcast(context, 0, UpdateReceiver.INTENT, PendingIntent.FLAG_NO_CREATE);
                returns(pendingIntent);

                Log.d(UpdateReceiver.LOG_TAG, "deactivated intent");

                pendingIntent.cancel();
            }
        };

        UpdateReceiver.deactivate(context);

    }


    /**
     * shall delegate  to widget provider
     */
    @Test
    public void testIntentReceiving(@Mocked final Context context,
                                    @Mocked final Intent intent,
                                    @Mocked Log l,
                                    @Mocked CameraWidgetProvider c,
                                    @Mocked(methods = {"onReceive"}, inverse = true) final UpdateReceiver updateReceiver) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        new Expectations() {
            {
                Log.d(UpdateReceiver.LOG_TAG, "received intent, update state");
                CameraWidgetProvider.displayCurrentState(context);
            }
        };

        updateReceiver.onReceive(context,intent);
    }
}


