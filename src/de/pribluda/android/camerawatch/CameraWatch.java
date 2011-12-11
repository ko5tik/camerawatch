package de.pribluda.android.camerawatch;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * main activity
 */
public class CameraWatch extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

    }
}
