package de.pribluda.android.camerawatch;

import android.location.Location;
import de.pribluda.android.camerawatch.data.Camera;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * test proper functionality of camera provider
 */
public class CameraProviderTest {

    /**
     * camera shall be accepted if distance between camera and location
     * is less than configured distance. computed distance will be
     * reduced by accuracy
     */
    @Test
    public void testCameraAcceptedIfDistanceIsLessAsConfigured(@Mocked final Location location,
                                                               @Mocked("computeCameraDistance") final CameraProvider c) {
        Configuration configuration = new Configuration();

        final Camera camera = new Camera();

        new Expectations() {
            {
                invoke(CameraProvider.class, "computeCameraDistance", camera, location);
                returns(200.0);

                location.getAccuracy();
                returns(51.0f);
            }
        };


        assertTrue(CameraProvider.cameraAcceptable(camera, location, 150.0f));

    }

    /**
     * camera shall be rejected in case distance is too far away
     */
    @Test
    public void testCameraIsRejectedIfDistanceIsMoreThanConfigured(@Mocked final Location location,
                                                                   @Mocked("computeCameraDistance") final CameraProvider c) {
        Configuration configuration = new Configuration();

        final Camera camera = new Camera();

        new Expectations() {
            {
                invoke(CameraProvider.class, "computeCameraDistance", camera, location);
                returns(200.0);

                location.getAccuracy();
                returns(51.0f);
            }
        };


        assertFalse(CameraProvider.cameraAcceptable(camera, location, 140.0f));

    }
}
