package com.alpay.codenotes.fragments;

import static com.alpay.codenotes.utils.NavigationManager.BUNDLE_CODE_KEY;
import static com.alpay.codenotes.utils.NavigationManager.BUNDLE_KARTON;
import static com.alpay.codenotes.utils.NavigationManager.BUNDLE_TURTLE;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.activities.CodeBlocksResultActivity;
import com.alpay.codenotes.adapter.LevelBlockAdapter;
import com.alpay.codenotes.listener.RecyclerItemClickListener;
import com.alpay.codenotes.models.CodeLineHelper;
import com.alpay.codenotes.models.Level;
import com.alpay.codenotes.utils.Constants;
import com.alpay.codenotes.utils.Utils;
import com.alpay.codenotes.vision.BitmapUtils;
import com.alpay.codenotes.vision.CameraSource;
import com.alpay.codenotes.vision.CameraSourcePreview;
import com.alpay.codenotes.vision.GraphicOverlay;
import com.alpay.codenotes.vision.LevelBlockRecognitionProcessor;

import java.io.FileOutputStream;
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

    LevelBlockAdapter adapter;
    static int currentPos = -1;
    View currentView;
    boolean[] checkArray;
    String currentMod = BUNDLE_TURTLE;
    int imageRes, textRes;

    @BindView(R.id.level_blocks)
    RecyclerView recyclerView;

    @BindView(R.id.recognized_text)
    TextView recognizedTextContainer;

    @BindView(R.id.levels_spinner)
    Spinner levelsSpinner;


    @OnClick(R.id.level_ok)
    public void checkLevelCode() {
        String result = CodeLineHelper.clearCode((AppCompatActivity) getActivity(), Utils.levelCode.replaceAll("\\s+", ""));
        if (result.contentEquals(Utils.checkCode)) {
            adapter.addCurrentPicture((LevelBlockAdapter.LevelBlockViewHolder) recyclerView.findViewHolderForAdapterPosition(currentPos), currentPos);
            adapter.notifyDataSetChanged();
            checkArray[currentPos] = false;
            if (Level.isAllFalse(checkArray)) {
                openCompletedView(imageRes, textRes);
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
        setLevelSpinner(R.array.turtle_levels_array);
        setModSpinner();
        if (Utils.getStringFromSharedPreferences((AppCompatActivity) getActivity(), "CODE_LANG").contentEquals("UK")){
            openGameWebFragment(Constants.FLAPPY_EN);
        } else {
            openGameWebFragment(Constants.FLAPPY_TR);
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

    public void openGameWebFragment(String url){
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

    private void openInitView(int imageResource, int textResource) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.layout_level_result, null);
        ImageView imageView = dialogLayout.findViewById(R.id.dialog_imageview);
        TextView textView = dialogLayout.findViewById(R.id.explain_text);
        imageView.setImageResource(imageResource);
        textView.setText(textResource);
        new AlertDialog.Builder(getActivity())
                .setView(dialogLayout)
                .setNeutralButton(R.string.see_result, (dialog, which) -> {
                    Intent intent = new Intent(getActivity(), CodeBlocksResultActivity.class);
                    String[] p5CodeArr = (String[]) Level.getCodeArray();
                    intent.putExtra(currentMod, true);
                    intent.putExtra(BUNDLE_CODE_KEY, p5CodeArr);
                    getActivity().startActivity(intent);
                })
                .setPositiveButton(R.string.open_next_level, (dialog, which) -> {
                    Utils.currentLevel+=1;
                    openLevel(Utils.currentLevel);
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void openCompletedView(int imageResource, int textResource) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.layout_level_result, null);
        ImageView imageView = dialogLayout.findViewById(R.id.dialog_imageview);
        TextView textView = dialogLayout.findViewById(R.id.explain_text);
        imageView.setImageResource(imageResource);
        textView.setText(textResource);
        new AlertDialog.Builder(getActivity())
                .setView(dialogLayout)
                .setNeutralButton(R.string.see_result, (dialog, which) -> {
                    Intent intent = new Intent(getActivity(), CodeBlocksResultActivity.class);
                    String[] p5CodeArr = (String[]) Level.getCodeArray();
                    intent.putExtra(currentMod, true);
                    intent.putExtra(BUNDLE_CODE_KEY, p5CodeArr);
                    getActivity().startActivity(intent);
                })
                .setPositiveButton(R.string.open_next_level, (dialog, which) -> {
                    Utils.currentLevel+=1;
                    openLevel(Utils.currentLevel);
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public void setupRecyclerView() {
        openLevel(Utils.currentLevel);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (Level.levelBlockList.get(position).isContainCode()) {
                            if (view != currentView) {
                                view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
                                if (currentView != null)
                                    currentView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                            }
                            Utils.checkCode = Level.levelBlockList.get(position).getCode();
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

    private void setLevelSpinner(int levels_array) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                levels_array, R.layout.spinner_text);
        levelsSpinner.setAdapter(adapter);
        levelsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                openLevel(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setModSpinner() {
        Spinner spinner = (Spinner) view.findViewById(R.id.mode_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.mod_array, R.layout.spinner_text);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    currentMod = BUNDLE_TURTLE;
                    setLevelSpinner(R.array.turtle_levels_array);
                } else {
                    currentMod = BUNDLE_KARTON;
                    setLevelSpinner(R.array.karton_levels_array);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void openLevel(int level) {
        if (currentMod == BUNDLE_TURTLE) {
            switch (level) {
                case 0:
                    Level.levelBlockList = Level.turtleLevels1;
                    imageRes = R.drawable.result_square;
                    textRes = R.string.t_level1;
                    break;

                case 1:
                    Level.levelBlockList = Level.turtleLevels2;
                    imageRes = R.drawable.result_triangle;
                    textRes = R.string.t_level2;
                    break;

                case 2:
                    Level.levelBlockList = Level.turtleLevels3;
                    imageRes = R.drawable.result_square_loop;
                    textRes = R.string.t_level3;
                    break;

                case 3:
                    Level.levelBlockList = Level.turtleLevels4;
                    imageRes = R.drawable.result_star_loop;
                    textRes = R.string.t_level4;
                    break;

                case 4:
                    Level.levelBlockList = Level.turtleLevels5;
                    imageRes = R.drawable.result_penta_style;
                    textRes = R.string.t_level5;
                    break;

                default:
                    break;
            }
        } else {
            switch (level) {
                case 0:
                    Level.levelBlockList = Level.kartonLevels1;
                    imageRes = R.drawable.result_basic;
                    textRes = R.string.k_level1;
                    break;

                case 1:
                    Level.levelBlockList = Level.kartonLevels2;
                    imageRes = R.drawable.result_input;
                    textRes = R.string.k_level2;
                    break;

                case 2:
                    Level.levelBlockList = Level.kartonLevels3;
                    imageRes = R.drawable.result_conditionals;
                    textRes = R.string.k_level3;
                    break;

                case 3:
                    Level.levelBlockList = Level.kartonLevels4;
                    imageRes = R.drawable.result_forloop;
                    textRes = R.string.k_level4;
                    break;

                default:
                    break;

            }
        }
        Utils.currentLevel = level;
        levelsSpinner.post(() -> levelsSpinner.setSelection(level));
        checkArray = Level.returnCheckCodeArray(Level.levelBlockList);
        adapter = new LevelBlockAdapter((AppCompatActivity) getActivity());
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
            cameraSource.setMachineLearningFrameProcessor(new LevelBlockRecognitionProcessor());
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