package de.pribluda.android.camerawatch;

import android.content.Context;
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
    private String locationProvider = "";

    // periodic widget updates
    @InjectPreference
    private int widgetUpdateInterval = 10;

    @InjectPreference
    private int maxCameraDistance = 200;


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


    /**
     * maximal accepatble distance for location to be considered valid
     *
     * @return
     */
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


    public String getLocationProvider() {
        return locationProvider;
    }

    public void setLocationProvider(String locationProvider) {
        this.locationProvider = locationProvider;
    }

    /**
     * maximal distance for cameras to be included in list
     *
     * @return
     */
    public int getMaxCameraDistance() {
        return maxCameraDistance;
    }

    public void setMaxCameraDistance(int maxCameraDistance) {
        this.maxCameraDistance = maxCameraDistance;
    }
}
