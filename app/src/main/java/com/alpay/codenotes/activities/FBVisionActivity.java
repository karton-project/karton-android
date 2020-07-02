package com.alpay.codenotes.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.adapter.CodeBlockViewAdapter;
import com.alpay.codenotes.adapter.ItemMoveCallback;
import com.alpay.codenotes.utils.CodePool;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.Utils;
import com.alpay.codenotes.view.utils.MarginDecoration;
import com.alpay.codenotes.vision.CameraSource;
import com.alpay.codenotes.vision.CameraSourcePreview;
import com.alpay.codenotes.vision.GraphicOverlay;
import com.alpay.codenotes.vision.TextRecognitionProcessor;
import com.squareup.seismic.ShakeDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import static com.alpay.codenotes.models.GroupHelper.codeList;
import static com.alpay.codenotes.utils.NavigationManager.BUNDLE_CODE_KEY;
import static com.alpay.codenotes.utils.NavigationManager.BUNDLE_FLAPPY_KEY;


public class FBVisionActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback, ShakeDetector.Listener {

    private static final String TAG = FBVisionActivity.class.getSimpleName();
    private static final int PERMISSION_REQUESTS = 1;
    private boolean isFlappy = false;

    private com.alpay.codenotes.vision.CameraSource cameraSource = null;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private CodePool codePool = new CodePool();

    @BindView(R.id.codeblocks_recycler_view)
    RecyclerView blocksRecyclerView;

    CodeBlockViewAdapter codeBlockViewAdapter;

    @OnClick(R.id.read_code_button)
    public void readCode() {
        addCodeToCodeList(checkAndCorrectCode(Utils.code));
    }

    private String checkAndCorrectCode(String code) {
        String result = "";
        String searchResult = "";
        if (code.length() > 3){
            code = code.toLowerCase();
            String[] parsedCode = code.split(" ");
            if(Utils.isENCoding(this))
                searchResult = FuzzySearch.extractOne(parsedCode[0] + " " + parsedCode[1] + " " + parsedCode[2], Arrays.asList(Utils.command_array_en)).getString();
            else
                searchResult = FuzzySearch.extractOne(parsedCode[0] + " " + parsedCode[1] + " " + parsedCode[2], Arrays.asList(Utils.command_array_tr)).getString();
            String[] parsedResult = searchResult.split(" ");
            for (int i =0; i< parsedResult.length; i++){
                parsedCode[i] = parsedResult[i];
            }
            for (int i =0; i< parsedCode.length; i++){
                result = result + parsedCode[i] + " ";
            }
        }
        return result.trim();
    }

    @Override
    public void hearShake() {
        addCodeToCodeList(codePool.drawRandomCodeFromPool());
        Toast.makeText(this, "Added a new random code!", Toast.LENGTH_SHORT).show();
    }

    public void addCodeToCodeList(String code){
        if (code != null) {
            if (code.contains("\n")){
                for (String line : code.split("\\r?\\n")){
                    codeList.add(line);
                }
            }else {
                codeList.add(code);
            }
            refreshCodeBlockRecyclerView(codeList.size() - 1);
        } else {
            Utils.showOKDialog(this, R.string.no_code_dialog_message);
        }
    }

    @OnClick(R.id.send_code_button)
    public void sendCode() {
        String[] p5Code = codeList.toArray(new String[codeList.size()]);
        Intent intent = new Intent(this, CodeBlocksResultActivity.class);
        intent.putExtra(BUNDLE_CODE_KEY, p5Code);
        intent.putExtra(BUNDLE_FLAPPY_KEY, isFlappy);
        startActivity(intent);
    }

    @OnClick(R.id.back_code_button)
    public void backToHome() {
        super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_vision);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getStringArray(BUNDLE_CODE_KEY) != null) {
                String[] p5code = bundle.getStringArray(BUNDLE_CODE_KEY);
                for (String code : p5code) {
                    codeList.add(code);
                }
            }
            if (bundle.getString(NavigationManager.BUNDLE_KEY) != null) {
                openHintView(bundle.getString(NavigationManager.BUNDLE_KEY));
            }
            if (bundle.getBoolean(BUNDLE_FLAPPY_KEY)) {
                isFlappy = true;
            }
        }

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);

        preview = findViewById(R.id.firePreview);
        if (preview == null) {
            Log.d(TAG, "Preview is null");
        }
        graphicOverlay = findViewById(R.id.fireFaceOverlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }

        if (allPermissionsGranted()) {
            createCameraSource();
        } else {
            getRuntimePermissions();
        }
        setUpRecyclerView();
        refreshCodeBlockRecyclerView(codeList.size() - 1);

    }

    private void openHintView(String instructions) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.task_title)
                .setMessage(instructions)
                .setNeutralButton(android.R.string.ok, (dialog, which) -> {
                    // Continue with operation
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void setUpRecyclerView() {
        blocksRecyclerView.addItemDecoration(new MarginDecoration(this));
        blocksRecyclerView.setHasFixedSize(true);
    }

    public void refreshCodeBlockRecyclerView(int position) {
        codeBlockViewAdapter = new CodeBlockViewAdapter(this, codeList);
        blocksRecyclerView.setHasFixedSize(true);
        blocksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        blocksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ItemTouchHelper.Callback callback =
                new ItemMoveCallback(codeBlockViewAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(blocksRecyclerView);
        blocksRecyclerView.setAdapter(codeBlockViewAdapter);
        blocksRecyclerView.scrollToPosition(position);
    }


    private void createCameraSource() {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
        }

        try {
            Log.i(TAG, "Using Text Detector Processor");
            cameraSource.setMachineLearningFrameProcessor(new TextRecognitionProcessor());
        } catch (Exception e) {
            Toast.makeText(
                    getApplicationContext(),
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
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
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
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
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