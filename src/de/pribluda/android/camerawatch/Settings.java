package de.pribluda.android.camerawatch;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import de.pribluda.android.andject.InjectView;
import de.pribluda.android.andject.ViewInjector;

import java.util.ArrayList;

/**
 * configure settings
 *
 * @author Konstantin Pribluda
 */
public class Settings extends Activity {

    public static final String LOG_TAG = "camerawatch.settings";
    @InjectView(id = R.id.activeContainer)
    ViewGroup locationProviders;



    @InjectView(id = R.id.displayDistance)
    SeekBar displayDistance;
    @InjectView(id = R.id.displayDistanceLabel)
    TextView displayDistanceLabel;

    @InjectView(id = R.id.updateInterval)
    SeekBar updateInterval;
    @InjectView(id = R.id.updateIntervalLabel)
    TextView updateIntervalLabel;

    private final ArrayList<CheckBox> locationCheckboxes = new ArrayList<CheckBox>();


    private final Configuration configuration = Configuration.getInstance(this);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        ViewInjector.startActivity(this);

        // populate provider lists

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        for (String provider : locationManager.getAllProviders()) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(provider);
            locationProviders.addView(checkBox);
            locationCheckboxes.add(checkBox);

            checkBox.setChecked(configuration.getLocationProvider().contains(provider));


            checkBox = new CheckBox(this);
            checkBox.setText(provider);
            checkBox.setChecked(configuration.getLocationProvider().contains(provider));
        }


        // initialize seekbar listeners
        updateInterval.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int value, boolean byUser) {
                // only if user did this

                configuration.setWidgetUpdateInterval(value);
                if (0 == value) {
                    // deaktivated
                    updateIntervalLabel.setText("Abgeschaltet");
                } else {
                    updateIntervalLabel.setText(value + " Min");
                }

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // do nothing
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                // do nothing
            }
        });

        updateInterval.setProgress(configuration.getWidgetUpdateInterval());




        displayDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
                  configuration.setMaxCameraDistance(value);
                  displayDistanceLabel.setText(value + "Meter");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        displayDistance.setProgress(configuration.getMaxCameraDistance());
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * settigna activity is terminated. save configuration ASAP
     */
    @Override
    protected void onPause() {

        StringBuilder stringBuilder = new StringBuilder();
        for (CheckBox checkBox : locationCheckboxes) {
            if (checkBox.isChecked())
                stringBuilder.append(checkBox.getText()).append("|");
        }
        configuration.setLocationProvider(stringBuilder.toString());
        Log.d(LOG_TAG, "created active string:" + configuration.getLocationProvider());



        configuration.save(this);

        Log.d(LOG_TAG, "saved configuration");
        super.onPause();
    }
}