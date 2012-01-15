package de.pribluda.android.camerawatch;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import com.google.gson.stream.JsonReader;
import de.pribluda.android.camerawatch.data.Camera;
import de.pribluda.android.jsonmarshaller.JSONUnmarshaller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * provides list of cameras based on location and settings
 *
 * @author Konstantin Pribluda
 */
public class CameraProvider {
    public static final String LOG_TAG = "camerawatch.dataprovider";

    private static CameraProvider instance;
    private final List<Camera> cameras;


    public CameraProvider(InputStream inputStream) throws InvocationTargetException, IOException, NoSuchMethodException, IllegalAccessException, InstantiationException {

        cameras = new ArrayList<Camera>(JSONUnmarshaller.unmarshallArray(new JsonReader(new InputStreamReader(inputStream)), Camera.class));
        Log.d(LOG_TAG, "read camera list from backend storage");
    }

    public static CameraProvider instance(Context context) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        if (instance == null) {
            instance = new CameraProvider(context.getResources().openRawResource(R.raw.cameras));
        }
        return instance;
    }

    /**
     * retrieve list of cameras for supplied location based on supplied location and settings
     *
     * @param location POI location
     * @return list of cameras in distance
     */
    public List<Camera> getCameraList(Location location, double distance) {
        ArrayList<Camera> result = new ArrayList<Camera>();

        for (Camera camera : cameras) {
            if (cameraAcceptable(camera, location, distance)) {
                result.add(camera);
            }
        }

        return result;
    }


    /**
     * whether given camera has  to be included in list
     *
     * @param camera   camera in question
     * @param location
     * @param distance
     * @return
     */
    public static boolean cameraAcceptable(Camera camera, Location location, double distance) {

        double result = computeCameraDistance(camera, location);
        //Log.d(LOG_TAG, "camera distance:" + result);

        return (result - (double) location.getAccuracy()) < distance;


    }

    private static double computeCameraDistance(Camera camera, Location location) {
        float[] result = new float[1];

        Location.distanceBetween(camera.getLatitude(), camera.getLongitude(), location.getLatitude(), location.getLongitude(), result);
        return result[0];
    }


}
