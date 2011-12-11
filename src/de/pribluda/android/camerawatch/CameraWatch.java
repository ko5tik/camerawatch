package de.pribluda.android.camerawatch;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
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

    private LocationProcessor locationProcessor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        ViewInjector.startActivity(this);

        this.locationProcessor = LocationProcessorProvider.instance(this);

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
            final Geocoder geocoder = new Geocoder(this);
            final List<Address> addressList;
            try {

                double lat = location.getLatitude();
                double lon = location.getLongitude();

                addressList = geocoder.getFromLocation(lat, lon, 1);
                for (Address address : addressList) {
                    Log.d(LOG_TAG, "decoded address:" + address);
                }


                if (addressList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    final Formatter formatter = new Formatter(sb);

                    final Address address = addressList.get(0);
                    StringBuilder adr = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        adr.append(address.getAddressLine(i)).append(",");
                    }
                    formatter.format("%s %s (%3.4f : %3.4f) @ %s, auf %4.4f Meter genau", adr.toString(), location.getProvider(), lat, lon, DateFormat.getTimeInstance().format(new Date(location.getTime())), location.getAccuracy());

                    locationStatusText.setText(sb.toString());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "excepion  in geocoder", e);
            }

        } else {
            // no location available,  can not update, request location update
            // ASAP
            locationProcessor.requestLocationUpdate();
        }
    }

    /**
     * hibernate.  push data to widget and request fresh location
     */
    @Override
    protected void onPause() {
        super.onPause();

        locationProcessor.requestLocationUpdate();
        CameraWidgetProvider.displayCurrentState(this);


    }
}
