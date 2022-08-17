package com.alpay.codenotes.fragments;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.adapter.GameLevelAdapter;
import com.alpay.codenotes.listener.RecyclerItemClickListener;
import com.alpay.codenotes.models.CodeLineHelper;
import com.alpay.codenotes.models.Game;
import com.alpay.codenotes.utils.Constants;
import com.alpay.codenotes.utils.Utils;
import com.alpay.codenotes.vision.CameraSource;
import com.alpay.codenotes.vision.CameraSourcePreview;
import com.alpay.codenotes.vision.GraphicOverlay;
import com.alpay.codenotes.vision.GameLevelBlockRecognitionProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GameFragment extends Fragment {

    private static final String TAG = GameFragment.class.getSimpleName();
    private static final int PERMISSION_REQUESTS = 1;

    private View view;
    private Unbinder unbinder;

    private CameraSource cameraSource;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;

    GameLevelAdapter adapter;
    static int currentPos = 1;
    View currentView;
    boolean[] checkArray;

    @BindView(R.id.game_blocks)
    RecyclerView recyclerView;

    @BindView(R.id.recognized_text)
    TextView recognizedTextContainer;

    @BindView(R.id.game_level_spinner)
    Spinner gameLevelsSpinner;


    @OnClick(R.id.game_ok)
    public void checkLevelCode() {
        String result = CodeLineHelper.codeToGameCommandLine((AppCompatActivity) getActivity(), Utils.gameLevelCode.replaceAll("\\s+", ""));
        if (result.contentEquals(Utils.checkCode)) {
            adapter.addCurrentPicture((GameLevelAdapter.GameLevelBlockViewHolder) recyclerView.findViewHolderForAdapterPosition(currentPos), currentPos);
            adapter.notifyDataSetChanged();
            checkArray[currentPos] = false;
            if (Utils.isAllFalse(checkArray) && Utils.currentGameLevel < Utils.gameLevelSize) {
                openGameLevel(Utils.currentGameLevel += 1);
            }
        } else {
            recognizedTextContainer.setText(result);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_game, container, false);
        unbinder = ButterKnife.bind(this, view);
        setGameLevelSpinner(R.array.game_levels_array);
        if (Utils.getStringFromSharedPreferences((AppCompatActivity) getActivity(), "CODE_LANG").contentEquals("UK")) {
            openGameWebFragment(Constants.FLAPPY_EN);
            Game.populateFlappyLevelsTR(0);
        } else {
            openGameWebFragment(Constants.FLAPPY_TR);
            Game.populateFlappyLevelsEN(0);
        }
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

    public void openGameWebFragment(String url) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        WebViewFragment webViewFragment = new WebViewFragment(url);
        ft.replace(R.id.gameWebView, webViewFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void onDetach() {
        unbinder.unbind();
        super.onDetach();
    }

    public void setupRecyclerView() {
        openGameLevel(Utils.currentGameLevel);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (Game.levelBlockList.get(position).isContainCode()) {
                            if (view != currentView) {
                                view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
                                if (currentView != null)
                                    currentView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                            }
                            Utils.checkCode = Game.levelBlockList.get(position).getCode();
                            currentPos = position;
                            currentView = view;
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    private void setGameLevelSpinner(int levels_array) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                levels_array, R.layout.spinner_text);
        gameLevelsSpinner.setAdapter(adapter);
        gameLevelsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                openGameLevel(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void openGameLevel(int level) {
        if (Utils.isTR(getActivity())) {
            Game.populateFlappyLevelsTR(level);
        } else {
            Game.populateFlappyLevelsEN(level);
        }
        Utils.currentGameLevel = level;
        gameLevelsSpinner.post(() -> gameLevelsSpinner.setSelection(level));
        checkArray = Game.returnCheckCodeArray(Game.levelBlockList);
        adapter = new GameLevelAdapter((AppCompatActivity) getActivity());
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
            cameraSource.setMachineLearningFrameProcessor(new GameLevelBlockRecognitionProcessor());
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
            if (!isPermissionGranted((AppCompatActivity) getActivity(), permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted((AppCompatActivity) getActivity(), permission)) {
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

    private static boolean isPermissionGranted(AppCompatActivity appCompatActivity, String permission) {
        if (ContextCompat.checkSelfPermission(appCompatActivity, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }
}