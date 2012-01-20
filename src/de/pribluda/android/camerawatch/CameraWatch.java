package de.pribluda.android.camerawatch;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import de.pribluda.android.andject.InjectView;
import de.pribluda.android.andject.ViewInjector;
import de.pribluda.android.camerawatch.location.LocationProcessor;
import de.pribluda.android.camerawatch.location.LocationProcessorProvider;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

/**
 * main activity
 */
public class CameraWatch extends Activity {
    public static final String LOG_TAG = "camerawatch.main";

    @InjectView(id = R.id.activityLocationStatus)
    private TextView locationStatusText;

    @InjectView(id = R.id.displayDistanceLabel)
    private TextView displayDistanceLabel;
    @InjectView(id = R.id.cameraList)
    private ListView cameraList;


    private LocationProcessor locationProcessor;

    private CameraProvider cameraProvider;
    private CameraAdapter adapter;
    private Configuration configuration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        ViewInjector.startActivity(this);

        this.locationProcessor = LocationProcessorProvider.instance(this);
        try {
            cameraProvider = CameraProvider.instance(this);
        } catch (Exception e) {
            Log.e(LOG_TAG, "unable to create camera provider", e);
        }

        configuration = Configuration.getInstance(this);

        adapter = new CameraAdapter(this);
        cameraList.setAdapter(adapter);
    }


    /**
     * perform necessary initialisation,  obtain best known location
     */
    @Override
    protected void onResume() {
        super.onResume();

        // retrieve location and display location status
        final Location location = locationProcessor.retrieveLocation();

        Log.d(LOG_TAG, "current location:" + location);

        if (location != null) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();

            final Geocoder geocoder = new Geocoder(this);
            final List<Address> addressList;

            StringBuilder sb = new StringBuilder();
            final Formatter formatter = new Formatter(sb);
            try {


                addressList = geocoder.getFromLocation(lat, lon, 1);
                for (Address address : addressList) {
                    Log.d(LOG_TAG, "decoded address:" + address);
                }


                if (addressList.size() > 0) {


                    final Address address = addressList.get(0);
                    StringBuilder adr = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        adr.append(address.getAddressLine(i)).append(",");
                    }
                    formatter.format("%s %s (%3.4f : %3.4f) @ %s, auf %d Meter genau", adr.toString(), location.getProvider(), lat, lon, DateFormat.getTimeInstance().format(new Date(location.getTime())), (int) location.getAccuracy());

                    locationStatusText.setText(sb.toString());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "excepion  in geocoder", e);

                formatter.format("Adresse unbekannt, %s (%3.4f : %3.4f) @ %s, auf %d Meter genau", location.getProvider(), lat, lon, DateFormat.getTimeInstance().format(new Date(location.getTime())), (int) location.getAccuracy());
                locationStatusText.setText(sb.toString());


            }

            updateCameraList(location);

        } else {
            // no location available,  can not update, request location update
            // ASAP
            locationProcessor.requestLocationUpdate();
        }

        displayDistanceLabel.setText("im " +  configuration.getMaxCameraDistance() + " meter Umkreis");
    }

    /**
     * update location
     *
     * @param location
     */
    private void updateCameraList(Location location) {
        Log.d(LOG_TAG, "setting camera list");
        adapter.setCameraList(cameraProvider.getCameraList(location, configuration.getMaxCameraDistance()));
    }

    /**
     * hibernate.  push data to widget and request fresh location
     */
    @Override
    protected void onPause() {
        super.onPause();

        locationProcessor.requestLocationUpdate();
        try {
            CameraWidgetProvider.displayCurrentState(this);
        } catch (Exception e) {
            Log.d(LOG_TAG, "exception while updating widget", e);
        }
        UpdateReceiver.activate(this);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.action.help == item.getItemId()) {
            // show help document,  we use online help
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Configuration.HELP_URL));
            startActivity(browserIntent);
            return true;
        } else if (R.action.settings == item.getItemId()) {
            startActivity(new Intent(this, Settings.class));
            return true;
        } else if (R.action.recommend == item.getItemId()) {
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.recommendation_subject));
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.recommendation_body));
            startActivity(emailIntent);
        }
        return false;
    }
}
