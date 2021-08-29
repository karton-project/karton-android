package com.alpay.codenotes.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.alpay.codenotes.R;
import com.alpay.codenotes.activities.FBVisionActivity;
import com.alpay.codenotes.adapter.LevelBlockAdapter;
import com.alpay.codenotes.models.Level;
import com.alpay.codenotes.utils.Utils;
import com.alpay.codenotes.vision.CameraSource;
import com.alpay.codenotes.vision.CameraSourcePreview;
import com.alpay.codenotes.vision.GraphicOverlay;
import com.alpay.codenotes.vision.TextRecognitionProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LevelFragment extends Fragment {


    private static final String TAG = LevelFragment.class.getSimpleName();
    private static final int PERMISSION_REQUESTS = 1;

    private View view;
    private Unbinder unbinder;

    private CameraSource cameraSource;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;

    LevelBlockAdapter adapter;

    @BindView(R.id.level_blocks)
    RecyclerView recyclerView;

    @OnClick(R.id.level_ok)
    public void checkLevelCode(){
        if (Utils.checkCode.contentEquals(Utils.levelCode)){
            Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_level, container, false);
        unbinder = ButterKnife.bind(this, view);
        setSpinner();
        Level.populateTurtleLevels();
        setupRecyclerView();

        preview = view.findViewById(R.id.firePreview);
        if (preview == null) {
            Log.d(TAG, "Preview is null");
        }
        graphicOverlay = view.findViewById(R.id.fireFaceOverlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }

        if (allPermissionsGranted()) {
            createCameraSource();
        } else {
            getRuntimePermissions();
        }

        return view;
    }

    @Override
    public void onDetach() {
        unbinder.unbind();
        super.onDetach();
    }


    public void setupRecyclerView(){
        adapter = new LevelBlockAdapter((AppCompatActivity) getActivity(), Level.turtleLevels1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setSpinner(){
        Spinner spinner = (Spinner) view.findViewById(R.id.levels_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.levels_array, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                openLevel(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void openLevel(int level){
        switch (level){
            case 1:
                adapter = new LevelBlockAdapter((AppCompatActivity) getActivity(), Level.turtleLevels1);
                break;

            case 2:
                adapter = new LevelBlockAdapter((AppCompatActivity) getActivity(), Level.turtleLevels2);
                break;

            case 3:
                adapter = new LevelBlockAdapter((AppCompatActivity) getActivity(), Level.turtleLevels3);
                break;

            case 4:
                adapter = new LevelBlockAdapter((AppCompatActivity) getActivity(), Level.turtleLevels4);
                break;

            case 5:
                adapter = new LevelBlockAdapter((AppCompatActivity) getActivity(), Level.turtleLevels5);
            default:
                break;
        }
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void createCameraSource() {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(getActivity(), graphicOverlay);
        }

        try {
            Log.i(TAG, "Using Text Detector Processor");
            cameraSource.setMachineLearningFrameProcessor(new TextRecognitionProcessor());
        } catch (Exception e) {
            Toast.makeText(
                    getActivity().getApplicationContext(),
                    "Can not create image processor: " + e.getMessage(),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    public void onPause() {
        super.onPause();
        preview.stop();
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    getActivity().getPackageManager()
                            .getPackageInfo(getActivity().getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(getActivity(), permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(getActivity(), permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    getActivity(), allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "Permission granted!");
        if (allPermissionsGranted()) {
            createCameraSource();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }
}