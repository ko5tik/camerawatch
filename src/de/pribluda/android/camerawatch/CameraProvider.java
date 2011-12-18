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
 * TODO: this is just dumb provider. needs real implementation
 *
 * @author Konstantin Pribluda
 */
public class CameraProvider {
    public static final String LOG_TAG = "camerawatch.dataprovider";

    private static CameraProvider instance;
    private final List<Camera> cameras;

    public CameraProvider(InputStream inputStream) throws InvocationTargetException, IOException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        cameras = new ArrayList<Camera>(JSONUnmarshaller.unmarshallArray(new JsonReader(new InputStreamReader(inputStream)), Camera.class));
        Log.d(LOG_TAG, "readc camera list from backend storage");
    }

    public static CameraProvider instance(Context context) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        if (instance == null) {
            instance = new CameraProvider(context.getResources().openRawResource(R.raw.cameras));
        }
        return instance;
    }

    public List<Camera> getCameraList(Location location) {
        return cameras;
    }
}
