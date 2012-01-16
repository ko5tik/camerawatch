package de.pribluda.android.camerawatch;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.RemoteViews;
import de.pribluda.android.camerawatch.data.Camera;
import de.pribluda.android.camerawatch.location.LocationProcessor;
import de.pribluda.android.camerawatch.location.LocationProcessorProvider;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

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

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "was enabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.d(LOG_TAG, "was updated, activate location changes");

        UpdateReceiver.activate(context);

        try {
            displayCurrentState(context);
        } catch (Exception e) {
            Log.d(LOG_TAG, "exception while updating widget", e);
        }
    }

    /**
     * set update notificatin that no suitable location provider is available
     *
     * @param context
     */
    public static void notifyNoProvider(Context context) {
        updateWidgetState(context, context.getResources().getText(R.string.noLocationProvider), "", "");
    }


    private static void updateWidgetState(Context context, CharSequence amountCameras, String locationCity, String locationStatus) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.camera_widget_layout);
        views.setOnClickPendingIntent(R.id.camerawidget, PendingIntent.getActivity(context, 0, new Intent(context, CameraWatch.class), PendingIntent.FLAG_UPDATE_CURRENT));
        views.setTextViewText(R.id.widget_amount_cameras, amountCameras);
        views.setTextViewText(R.id.widget_location, locationCity);
        //  views.setTextViewText(R.id.widget_security, securityStatus);
        views.setTextViewText(R.id.locationStatus, locationStatus);

        final int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(CameraWidgetProvider.class.getPackage().getName(), CameraWidgetProvider.class.getName()));

        manager.updateAppWidget(appWidgetIds, views);

        Log.d(LOG_TAG, "widget state changed");
    }

    /**
     * display current widtget state
     *
     * @param context
     */
    public static void displayCurrentState(Context context) throws InvocationTargetException, IOException, NoSuchMethodException, IllegalAccessException, InstantiationException {


        final LocationProcessor locationProcessor = LocationProcessorProvider.instance(context);
        final Location lastKnownLocation = locationProcessor.retrieveLocation();

        if (lastKnownLocation == null) {
            // request single update
            Log.d(LOG_TAG, "no last state found. request single update");
            locationProcessor.requestLocationUpdate();

            notifyNoProvider(context);

            return;
        }
        final double lat = lastKnownLocation.getLatitude();
        final double lon = lastKnownLocation.getLongitude();

        Log.d(LOG_TAG, "current location:" + lon + ":" + lat);

        //  create address string
        final Geocoder geocoder = new Geocoder(context);

        List<Address> addressList = null;

        try {
            addressList = geocoder.getFromLocation(lat, lon, 1);
            for (Address address : addressList) {
                Log.d(LOG_TAG, "decoded address:" + address);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "excepion  in geocoder", e);
        }

        // create location status string

        StringBuilder sb = new StringBuilder();
        final Formatter formatter = new Formatter(sb);

        formatter.format(" %s (%3.4f : %3.4f) @ %s, in %d m", lastKnownLocation.getProvider(), lat, lon, DateFormat.getTimeInstance().format(new Date(lastKnownLocation.getTime())), (int)Configuration.getInstance(context).getMaxCameraDistance());

        String addressString = "";
        if (addressList != null && addressList.size() > 0) {

            addressString = addressList.get(0).getLocality();

        }

        // retrieve amount of cameras
        int size = CameraProvider.instance(context).getCameraList(lastKnownLocation, Configuration.getInstance(context).getMaxCameraDistance()).size();

        updateWidgetState(context, "Anzahl Kameras: " + size, addressString,
                sb.toString());
    }
}
