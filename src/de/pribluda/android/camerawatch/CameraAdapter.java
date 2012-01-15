package de.pribluda.android.camerawatch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.pribluda.android.camerawatch.data.Camera;

import java.util.Collections;
import java.util.List;

/**
 * provide list entries for camera list
 *
 * @author Konstantin Pribluda
 */
public class CameraAdapter extends BaseAdapter {
    public static final String LOG_TAG = "camerawatch.listadapter";
    private final Context context;
    private List<Camera> cameras;

    public CameraAdapter(Context context) {
        this.context = context;
        cameras = Collections.EMPTY_LIST;
    }

    public int getCount() {
        return cameras.size();
    }

    public Object getItem(int i) {
        return cameras.get(i);
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int index, View view, ViewGroup viewGroup) {
        View result = view;
        // inflate layout if not already created
        if (result == null) {
            result = LayoutInflater.from(context).inflate(R.layout.camera_list_entry_layout, viewGroup, false);
        }

        // set up data
        final Camera camera = cameras.get(index);

        final TextView cameraPosition = (TextView) result.findViewById(R.id.cameraPositionText);
        cameraPosition.setText(camera.getTitel());

        final TextView positionNumber = (TextView) result.findViewById(R.id.cameraPositionNumber);
        positionNumber.setText("" + (index + 1) + ".");
        return result;
    }

    /**
     * update data and notify view about change
     *
     * @param cameras
     */
    public void setCameraList(List<Camera> cameras) {
        this.cameras = cameras;
        notifyDataSetChanged();
    }
}
