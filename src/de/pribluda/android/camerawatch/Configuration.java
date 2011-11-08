package de.pribluda.android.camerawatch;

import android.content.Context;
import android.location.Criteria;
import de.pribluda.android.andject.InjectPreference;
import de.pribluda.android.andject.PreferenceInjector;

/**
 * configuration object provides proper values,  values injected from shared preferences
 */
public class Configuration {
    public static final String PREFERENCE_TAG = "preferences";
    private static Configuration instance;

    @InjectPreference
    private int powerRequirement = Criteria.POWER_MEDIUM;
    @InjectPreference
    private int accuracy = Criteria.ACCURACY_COARSE;

    public Configuration(Context context) throws IllegalAccessException {
        // perform field injection from shared preferences via andject
        PreferenceInjector.inject(this, context.getSharedPreferences(PREFERENCE_TAG, Context.MODE_PRIVATE));
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getPowerRequirement() {
        return powerRequirement;
    }

    public void setPowerRequirement(int powerRequirement) {
        this.powerRequirement = powerRequirement;
    }

    public static Configuration getInstance(final Context context) throws IllegalAccessException {
        if (instance == null) {
            instance = new Configuration(context);
        }
        return instance;
    }
}
