package de.pribluda.android.camerawatch;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import de.pribluda.android.camerawatch.location.LocationProcessor;
import de.pribluda.android.camerawatch.location.LocationProcessorProvider;
import mockit.Cascading;
import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrict;
import org.junit.Ignore;
import org.junit.Test;

/**
 * proper function of change location  receiver shall be assured
 */
public class ChangeLocationReceiverTest {

    /**
     * incoming location shall be passed to location processor
     */
    @Test
    public void testThatValidLocationIsDIspatchedProperly(@Cascading final Intent intent,
                                                          @Mocked final LocationProcessor locationProcessor,
                                                          @Mocked LocationProcessorProvider llp,
                                                          @Mocked final Context context,
                                                          @NonStrict Log l,
                                                          @Mocked final Location location,
                                                          @Mocked final CameraWidgetProvider cwp,
                                                          @Mocked(methods = {"onReceive"}, inverse = true) final ChangeLocationReceiver clr) {

        new Expectations() {
            {
                intent.toString();
                returns("foo");

                Log.d(ChangeLocationReceiver.LOG_TAG, "received intent: foo");

                LocationProcessorProvider.instance(context);
                returns(locationProcessor);


                intent.hasExtra(LocationManager.KEY_LOCATION_CHANGED);
                returns(true);

                intent.getExtras().get(LocationManager.KEY_LOCATION_CHANGED);
                returns(location);

                location.toString();
                returns("bar");

                Log.d(ChangeLocationReceiver.LOG_TAG, "received new location: bar");

                locationProcessor.processLocationUpdate(location);

                CameraWidgetProvider.displayCurrentState(context);
            }
        };


        clr.onReceive(context, intent);

    }

    /**
     * in case intent does  not contain valid location, updates shall be canceled on the spot
     */
    @Test
    public void testCancelationProcessing(@Cascading final Intent intent,
                                          @Mocked final LocationProcessor locationProcessor,
                                          @Mocked LocationProcessorProvider llp,
                                          @Mocked final Context context,
                                          @NonStrict Log l,
                                          @Mocked(methods = {"onReceive"}, inverse = true) final ChangeLocationReceiver clr) {
        new Expectations() {
            {
                intent.toString();
                returns("foo");

                Log.d(ChangeLocationReceiver.LOG_TAG, "received intent: foo");

                LocationProcessorProvider.instance(context);
                returns(locationProcessor);


                intent.hasExtra(LocationManager.KEY_LOCATION_CHANGED);
                returns(false);

                intent.hasExtra(LocationProcessor.LOCATION_STOP_UPDATES);
                returns(true);

                Log.d(ChangeLocationReceiver.LOG_TAG, "canceling location update");

                locationProcessor.stopUpdates();
            }


        };

        clr.onReceive(context, intent);
    }
}
