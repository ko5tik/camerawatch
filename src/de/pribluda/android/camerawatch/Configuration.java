package de.pribluda.android.camerawatch;

import android.content.Context;
import android.location.Criteria;
import android.util.Log;
import de.pribluda.android.andject.InjectPreference;
import de.pribluda.android.andject.PreferenceInjector;

/**
 * configuration object provides proper values,  values injected from shared preferences
 */
public class Configuration {
    public static final String LOG_TAG = "camerawatch.configuration";
    public static final String PREFERENCE_TAG = "preferences";
    public static final String HELP_URL = "http://ppwi.de/kameras/details_mobile.aspx";
    public static final String CAMERA_DETAIL_URL = "http://ppwi.de/kameras/details_mobile.aspx";
    private static Configuration instance;

    @InjectPreference
    private int powerRequirement = Criteria.POWER_MEDIUM;
    @InjectPreference
    private int accuracy = Criteria.ACCURACY_COARSE;

    @InjectPreference
    private String activeProvider = "";

    @InjectPreference
    private String passiveProvider = "";

    // periodic widget updates
    @InjectPreference
    private int widgetUpdateInterval = 300000;

    // whether periodic updates are active
    @InjectPreference
    private boolean widgetUpdatesActive = true;


    @InjectPreference
    private int locationUpdateInterval = 300000;

    @InjectPreference
    private float locationUpdateDistance = 300;

    @InjectPreference
    private float maxAcceptableDinstance = 1000;
    @InjectPreference
    private long maxAcceptableAge = 300000;


    /**
     * create and populate from shared preferences
     *
     * @param context
     */
    public Configuration(Context context) {
        // perform field injection from shared preferences via andject
        try {
            PreferenceInjector.inject(this, context.getSharedPreferences(PREFERENCE_TAG, Context.MODE_PRIVATE));
        } catch (IllegalAccessException e) {
            Log.e(LOG_TAG, "exception while reading configuration", e);
        }
    }

    /**
     * default constructor
     */
    public Configuration() {

    }


    /**
     * save preferences
     */
    public void save(Context context) {
        try {
            PreferenceInjector.eject(this, context.getSharedPreferences(PREFERENCE_TAG, Context.MODE_PRIVATE));
        } catch (IllegalAccessException e) {
            Log.e(LOG_TAG, "exception while saving configuration", e);
        }
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

    /**
     * location update interval in minutes,  0 means no updates
     *
     * @return
     */
    public int getLocationUpdateInterval() {
        return locationUpdateInterval;
    }

    public void setLocationUpdateInterval(int locationUpdateInterval) {
        this.locationUpdateInterval = locationUpdateInterval;
    }

    public float getLocationUpdateDistance() {
        return locationUpdateDistance;
    }

    public void setLocationUpdateDistance(float locationUpdateDistance) {
        this.locationUpdateDistance = locationUpdateDistance;
    }

    public float getMaxAcceptableDinstance() {
        return maxAcceptableDinstance;
    }

    public void setMaxAcceptableDinstance(float maxAcceptableDinstance) {
        this.maxAcceptableDinstance = maxAcceptableDinstance;
    }

    public long getMaxAcceptableAge() {
        return maxAcceptableAge;
    }

    public void setMaxAcceptableAge(long maxAcceptableAge) {
        this.maxAcceptableAge = maxAcceptableAge;
    }

    public static Configuration getInstance(final Context context) {
        if (instance == null) {
            instance = new Configuration(context);
        }
        return instance;
    }

    public int getWidgetUpdateInterval() {
        return widgetUpdateInterval;
    }

    public void setWidgetUpdateInterval(int widgetUpdateInterval) {
        this.widgetUpdateInterval = widgetUpdateInterval;
    }

    public boolean isWidgetUpdatesActive() {
        return widgetUpdatesActive;
    }

    public void setWidgetUpdatesActive(boolean widgetUpdatesActive) {
        this.widgetUpdatesActive = widgetUpdatesActive;
    }

    public String getActiveProvider() {
        return activeProvider;
    }

    public void setActiveProvider(String activeProvider) {
        this.activeProvider = activeProvider;
    }

    public String getPassiveProvider() {
        return passiveProvider;
    }

    public void setPassiveProvider(String passiveProvider) {
        this.passiveProvider = passiveProvider;
    }
}
