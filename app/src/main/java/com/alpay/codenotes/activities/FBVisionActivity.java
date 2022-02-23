package com.alpay.codenotes.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.alpay.codenotes.models.CodeLine;
import com.alpay.codenotes.models.CodeLineHelper;
import com.alpay.codenotes.utils.CodePool;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.Utils;
import com.alpay.codenotes.view.utils.MarginDecoration;
import com.alpay.codenotes.vision.BitmapUtils;
import com.alpay.codenotes.vision.CameraSource;
import com.alpay.codenotes.vision.CameraSourcePreview;
import com.alpay.codenotes.vision.GraphicOverlay;
import com.alpay.codenotes.vision.TextRecognitionProcessor;
import com.squareup.seismic.ShakeDetector;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.alpay.codenotes.models.CodeLineHelper.codeList;
import static com.alpay.codenotes.utils.NavigationManager.BUNDLE_CODE_KEY;
import static com.alpay.codenotes.utils.NavigationManager.BUNDLE_TURTLE;


public class FBVisionActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback, ShakeDetector.Listener {

    private static final String TAG = FBVisionActivity.class.getSimpleName();
    private static final int PERMISSION_REQUESTS = 1;
    private boolean turtleMode = false;

    private CameraSource cameraSource;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private CodePool codePool = new CodePool();

    @BindView(R.id.codeblocks_recycler_view)
    RecyclerView blocksRecyclerView;

    @BindView(R.id.codeblocks_clear_all)
    ImageView clearCodeButton;

    CodeBlockViewAdapter codeBlockViewAdapter = new CodeBlockViewAdapter(this);

    @OnClick(R.id.codeblocks_clear_all)
    public void clearCodeBlocks() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.clear_code_title)
                .setMessage(R.string.clear_code_exp)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    codeList = new ArrayList();
                    refreshCodeBlockRecyclerView(0);
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    // do nothing
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @OnClick(R.id.read_code_button)
    public void readCode() {
        saveImage();
        addCodeToCodeList(checkAndCorrectCode(Utils.code));
    }

    private String checkAndCorrectCode(String code) {
        ArrayList<CodeLine> codeLines = new ArrayList<>();
        String[] lines = code.split("\n");
        for (String line : lines) {
            codeLines.add(CodeLineHelper.codeToCodeLine(this, line + "\n"));
        }
        return CodeLineHelper.programToCodeText(codeLines);
    }

    @Override
    public void hearShake() {
        addCodeToCodeList(codePool.drawRandomCodeFromPool());
        Toast.makeText(this, "Added a new random code!", Toast.LENGTH_SHORT).show();
    }

    public void addCodeToCodeList(String code) {
        if (code != null) {
            if (code.contains("\n")) {
                for (String line : code.split("\n")) {
                    if (line.length() > 2)
                        codeList.add(CodeLineHelper.codeToCodeLine(this, line.trim()));
                }
            } else {
                codeList.add(CodeLineHelper.codeToCodeLine(this, code));
            }
            refreshCodeBlockRecyclerView(codeList.size() - 1);
        } else {
            Utils.showOKDialog(this, R.string.no_code_dialog_message);
        }
    }

    protected void saveImage(){
        Bitmap cameraImage = Bitmap.createBitmap(480, 360, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(cameraImage);
        graphicOverlay.draw(c);
        try (FileOutputStream out = new FileOutputStream(BitmapUtils.getOutputMediaFile("kartonblocks"))) {
            cameraImage.compress(Bitmap.CompressFormat.PNG, 100, out);
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.send_code_button)
    public void sendCode() {
        String[] p5Code = CodeLineHelper.programToCodeTextArray(codeList);
        Intent intent = new Intent(this, CodeBlocksResultActivity.class);
        intent.putExtra(BUNDLE_CODE_KEY, p5Code);
        intent.putExtra(BUNDLE_TURTLE, turtleMode);
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
                    codeList.add(CodeLineHelper.codeToCodeLine(this, code));
                }
            }
            if (bundle.getString(NavigationManager.BUNDLE_KEY) != null) {
                openHintView(bundle.getString(NavigationManager.BUNDLE_KEY));
            }
            if (bundle.getBoolean(BUNDLE_TURTLE)) {
                turtleMode = true;
            }
        }

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);
        clearCodeButton.setVisibility(View.GONE);
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpRecyclerView();
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
        blocksRecyclerView.setHasFixedSize(true);
        blocksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        blocksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        blocksRecyclerView.addItemDecoration(new MarginDecoration(this));

        ItemTouchHelper.Callback callback = new ItemMoveCallback(codeBlockViewAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(blocksRecyclerView);

        blocksRecyclerView.setAdapter(codeBlockViewAdapter);
    }

    public void refreshCodeBlockRecyclerView(int position) {
        codeBlockViewAdapter.notifyDataSetChanged();
        blocksRecyclerView.scrollToPosition(position);
        if (codeList.size() > 0) {
            clearCodeButton.setVisibility(View.VISIBLE);
        } else {
            clearCodeButton.setVisibility(View.GONE);
        }
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
        /*if (cameraSource != null) {
            cameraSource.release();
        }*/
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