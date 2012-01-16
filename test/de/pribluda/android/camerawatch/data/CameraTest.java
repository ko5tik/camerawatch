package de.pribluda.android.camerawatch.data;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertSame;

/**
 * test capabilities of camera object
 */
public class CameraTest {

    /**
     * comparator
     */
    @Test
    public void testCameraComaprison() {
        Camera one = new Camera();
        one.setDistance(100);
        Camera two = new Camera();
        two.setDistance(101);

        List<Camera> cameras = Arrays.asList(two, one);
        Collections.sort(cameras);


        assertSame(one, cameras.get(0));

        assertSame(two, cameras.get(1));
    }
}
