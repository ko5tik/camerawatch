package de.pribluda.android.camerawatch.location;

import android.content.Context;
import android.location.Location;

/**
 * factory fro lcoation processirs. will instantiate proper processor based on
 *
 * @author e078262 ( Konstantin Pribluda )
 */
public class LocationProcessorProvider {

    static LocationProcessor instance;

    public static synchronized LocationProcessor instance(Context context) {
        if (instance == null) {
            instance = new LegacyLocationProcessor(context);
        }
        return instance;
    }
}
