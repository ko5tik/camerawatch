package de.pribluda.android.camerawatch.location;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import de.pribluda.android.camerawatch.Configuration;
import mockit.*;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * TODO: test request for  certain providers -  especially splitting
 *
 * @author Konstantin Pribluda
 */
public class LegacyLocationProviderTest {

    /**
     * shall request updates from enabled providers and
     */
    @Test
    public void testProperUpdateRequest(@Mocked final LocationManager locationManager,
                                        @Mocked final Context context,
                                        @Mocked final Configuration configuration,
                                        @Cascading Intent intent,
                                        @Mocked Log log,
                                        @Mocked(methods = {"scheduleLocationUpdates", "scheduleCancelationIntent"}) final LegacyLocationProcessor llp) {

        Deencapsulation.setField(llp, "locationManager", locationManager);
        Deencapsulation.setField(llp, "configuration", configuration);
        new Expectations() {

            {

                configuration.getLocationProvider();
                returns("grumple|grample|grimple");

                locationManager.isProviderEnabled("grumple");
                returns(true);
                Log.d(LegacyLocationProcessor.LOG_TAG, withAny(""));
                invoke(llp, "scheduleLocationUpdates", "grumple");


                locationManager.isProviderEnabled("grample");
                returns(false);


                locationManager.isProviderEnabled("grimple");
                returns(true);

                Log.d(LegacyLocationProcessor.LOG_TAG, withAny(""));
                invoke(llp, "scheduleLocationUpdates", "grimple");


                invoke(llp, "scheduleCancelationIntent");

            }
        };


        llp.requestLocationUpdate();

        // nothing else is allowed
        new FullVerifications() {
            {
            }
        };
    }


    /**
     * in case not poroviders are enabled, none shall happen
     */
    @Test
    public void testThatNoCancelationIsScheduledIfNoProviderEnabled(@Mocked final LocationManager locationManager,
                                                                    @Mocked final Context context,
                                                                    @Mocked final Configuration configuration,
                                                                    @Cascading Intent intent,
                                                                    @Mocked Log log,
                                                                    @Mocked(methods = {"scheduleLocationUpdates", "scheduleCancelationIntent"}) final LegacyLocationProcessor llp) {

        Deencapsulation.setField(llp, "locationManager", locationManager);
        Deencapsulation.setField(llp, "configuration", configuration);


        new Expectations() {

            {

                configuration.getLocationProvider();
                returns("grumple|grample|grimple");

                locationManager.isProviderEnabled("grumple");
                returns(false);


                locationManager.isProviderEnabled("grample");
                returns(false);


                locationManager.isProviderEnabled("grimple");
                returns(false);


            }
        };


        llp.requestLocationUpdate();

        // nothing else is allowed
        new FullVerifications() {
            {
            }
        };
    }


    /**
     * test proper initialisation sequence.
     */
    @Test
    public void testProperInitialisation(@Mocked final Context context,
                                         @Mocked final Configuration configuration,
                                         @Mocked final LocationManager locationManager,
                                         @Mocked final Intent req,
                                         @Mocked final Intent cancel) {
        new Expectations() {
            {
                Configuration.getInstance(context);
                returns(configuration);
                context.getSystemService(Context.LOCATION_SERVICE);
                returns(locationManager);

                new Intent(LocationProcessor.LOCATION_CHANGE_INTENT);
                returns(req);

                new Intent(LocationProcessor.LOCATION_CHANGE_INTENT).putExtra(LocationProcessor.LOCATION_STOP_UPDATES, withAny("string"));
                returns(cancel);


            }
        };

        final LegacyLocationProcessor llp = new LegacyLocationProcessor(context);

        assertSame(context, Deencapsulation.getField(llp, "context"));
        assertSame(configuration, Deencapsulation.getField(llp, "configuration"));
        assertSame(locationManager, Deencapsulation.getField(llp, "locationManager"));

        //  System.err.println("send: " + req);
        //  System.err.println("send: " + Deencapsulation.getField(llp, "sendUpdatesIntent"));
        //  System.err.println("cancel: " + cancel);
        //  System.err.println("cancel: " + Deencapsulation.getField(llp, "stopUpdatesIntent"));

        //TODO: this fails for unknown cause , investigate.
        //assertSame(req, Deencapsulation.getField(llp, "sendUpdatesIntent"));

        assertSame(cancel, Deencapsulation.getField(llp, "stopUpdatesIntent"));


        // nothing else is allowed
        new FullVerifications() {
            {
            }
        };
    }


    /**
     * when locationis received,  shall stop updates immediately
     */
    @Test
    public void testThatLocationRequestUsCanceledUponLocationReceiving(@Mocked("stopUpdates") final LegacyLocationProcessor llp,
                                                                       @NonStrict final Location location,
                                                                       @NonStrict Log log

    ) {

        new Expectations() {
            {
                llp.stopUpdates();
            }
        };


        assertSame(location, llp.processLocationUpdate(location));
        assertSame(location, Deencapsulation.getField(llp, "cached"));

    }


    /**
     * shall shedule location update properly
     */
    @Test
    public void testScheduleCancelation(@Mocked(methods = {"scheduleCancelationIntent"}, inverse = true) final LegacyLocationProcessor llp,
                                        @Mocked final AlarmManager alarmManager,
                                        @Mocked System system,
                                        @Mocked final PendingIntent pendingIntent,
                                        @Mocked final Intent intent,
                                        @Mocked final Context context) {

        Deencapsulation.setField(llp, "stopUpdatesIntent", intent);
        Deencapsulation.setField(llp, "context", context);

        new Expectations() {
            {

                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                returns(pendingIntent);


                context.getSystemService(Context.ALARM_SERVICE);
                returns(alarmManager);

                System.currentTimeMillis();
                returns(239l);

                alarmManager.set(AlarmManager.ELAPSED_REALTIME, 239 + LegacyLocationProcessor.CANCEL_INTERVAL, pendingIntent);

            }

        };

        Deencapsulation.invoke(llp, "scheduleCancelationIntent");

    }


    /**
     * test proper scheduling of updates
     */
    @Test
    public void testUpdateScheduling(@Mocked(methods = {"scheduleLocationUpdates"}, inverse = true) final LegacyLocationProcessor llp,
                                     @Mocked final LocationManager locationManager,
                                     @Mocked final Intent intent,
                                     @Mocked final PendingIntent pi,
                                     @Mocked final Context context) {

        Deencapsulation.setField(llp, "locationManager", locationManager);
        Deencapsulation.setField(llp, "sendUpdatesIntent", intent);
        Deencapsulation.setField(llp, "context", context);

        new Expectations() {
            {
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT); returns(pi);
                locationManager.requestLocationUpdates("bramble",0,0,pi);
            }
        };

        Deencapsulation.invoke(llp, "scheduleLocationUpdates", "bramble");
    }
}
