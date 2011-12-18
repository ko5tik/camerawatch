package de.pribluda.android.camerawatch;

import android.content.Context;
import android.util.Log;
import de.pribluda.android.camerawatch.location.LocationProcessor;
import de.pribluda.android.camerawatch.location.LocationProcessorProvider;
import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Mocked;
import org.junit.Test;

/**
 * assure proper function of camera widget provider
 *
 * @author Konstantin Pribluda
 */
public class CameraWidgetProviderTest {

    /**
     * in case no actual location is set, widget shall bne updated properly
     */
    @Test
    public void testNoLocationProducesProperMessage(@Mocked final Context context,
                                                    @Mocked final LocationProcessorProvider locationProcessorProvider,
                                                    @Mocked final LocationProcessor locationProcessor,
                                                    @Mocked final Log log,
                                                    @Mocked({"notifyNoProvider"}) final CameraWidgetProvider cameraWidgetProvider
    ) {

        new Expectations() {
            {
                LocationProcessorProvider.instance(context);
                returns(locationProcessor);

                locationProcessor.retrieveLocation();
                returns(null);

                Log.d(CameraWidgetProvider.LOG_TAG, "no last state found. request single update");

                locationProcessor.requestLocationUpdate();

                CameraWidgetProvider.notifyNoProvider(context);
            }
        };


        CameraWidgetProvider.displayCurrentState(context);

        new FullVerifications() {
            {
            }
        };
    }
}
