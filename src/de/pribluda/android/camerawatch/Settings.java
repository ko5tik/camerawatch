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
    ViewGroup activeProviders;
    @InjectView(id = R.id.passiveContainer)
    ViewGroup passiveProviders;

    @InjectView(id = R.id.updateDistance)
    SeekBar updateDistance;
    @InjectView(id = R.id.updateDistanceLabel)
    TextView updateDistanceLabel;

    @InjectView(id = R.id.displayDistance)
    SeekBar displayDistance;
    @InjectView(id = R.id.displayDistanceLabel)
    TextView dsplayDistanceLabel;
    @InjectView(id = R.id.updateInterval)
    SeekBar updateInterval;
    @InjectView(id = R.id.updateIntervalLabel)
    TextView updateIntervalLabel;

    private final ArrayList<CheckBox> activeCheckboxes = new ArrayList<CheckBox>();
    private final ArrayList<CheckBox> passiveCheckboxes = new ArrayList<CheckBox>();

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
            activeProviders.addView(checkBox);
            activeCheckboxes.add(checkBox);

            checkBox.setChecked(configuration.getActiveProvider().contains(provider));


            checkBox = new CheckBox(this);
            checkBox.setText(provider);
            passiveProviders.addView(checkBox);
            passiveCheckboxes.add(checkBox);
            checkBox.setChecked(configuration.getActiveProvider().contains(provider));
        }

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
        for (CheckBox checkBox : activeCheckboxes) {
            if (checkBox.isChecked())
                stringBuilder.append(checkBox.getText()).append("|");
        }
        configuration.setActiveProvider(stringBuilder.toString());
        Log.d(LOG_TAG, "created active string:" + configuration.getActiveProvider());

        stringBuilder = new StringBuilder();
        for (CheckBox checkBox : passiveCheckboxes) {
            if (checkBox.isChecked())
                stringBuilder.append(checkBox.getText()).append("|");
        }
        configuration.setPassiveProvider(stringBuilder.toString());
        Log.d(LOG_TAG, "created passive string:" + configuration.getPassiveProvider());


        configuration.save(this);

        Log.d(LOG_TAG, "saved configuration");
        super.onPause();
    }
}