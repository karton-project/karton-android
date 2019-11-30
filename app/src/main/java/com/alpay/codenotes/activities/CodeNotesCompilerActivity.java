package com.alpay.codenotes.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.adapter.CodeBlockViewAdapter;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.Utils;
import com.alpay.codenotes.view.AutofitVerticalRecyclerView;
import com.alpay.codenotes.view.CameraPreview;
import com.alpay.codenotes.view.utils.MarginDecoration;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.alpay.codenotes.models.GroupHelper.codeList;
import static com.alpay.codenotes.utils.NavigationManager.BUNDLE_CODE_KEY;
import static com.alpay.codenotes.utils.NavigationManager.BUNDLE_FLAPPY_KEY;


public class CodeNotesCompilerActivity extends BaseActivity {

    @BindView(R.id.surface_view)
    FrameLayout cameraView;
    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.read_code_button)
    FloatingActionButton readCodeButton;
    @BindView(R.id.codeblocks_recycler_view)
    AutofitVerticalRecyclerView blocksRecyclerView;


    private Camera mCamera;
    private CameraPreview mPreview;

    CodeBlockViewAdapter codeBlockViewAdapter;
    private boolean isFlappy = false;

    final int RequestCameraPermissionID = 1001;
    String code;

    @OnClick(R.id.read_code_button)
    public void readCode() {
        mCamera.takePicture(null, null, picture);

    }

    @OnClick(R.id.send_code_button)
    public void sendCode() {
        String[] p5Code = codeList.toArray(new String[codeList.size()]);
        Intent intent = new Intent(this, CodeBlocksResultActivity.class);
        intent.putExtra(NavigationManager.BUNDLE_CODE_KEY, p5Code);
        intent.putExtra(NavigationManager.BUNDLE_FLAPPY_KEY, isFlappy);
        startActivity(intent);
    }

    @OnClick(R.id.back_code_button)
    public void backToHome() {
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    buildCameraSource();
                }
            }
            break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compiler);
        ButterKnife.bind(this);
        buildCameraSource();
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
        blocksRecyclerView.setAdapter(codeBlockViewAdapter);
        blocksRecyclerView.scrollToPosition(position);
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }


    private void buildCameraSource() {
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera);
        cameraView.addView(mPreview);
        Camera.Parameters params = mCamera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
        mCamera.setParameters(params);

    }

    private Camera.PictureCallback picture = (data, camera) -> {
        recognizeText(data);
        camera.startPreview();
    };


    private void recognizeText(byte[] bytes) {
        FirebaseVisionImage image = imageFromArray(bytes);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        detector.processImage(image)
                .addOnSuccessListener(firebaseVisionText -> {
                    code = firebaseVisionText.getText().toLowerCase();
                    if (code != null) {
                        checkAndCorrectCode();
                        codeList.add(code);
                        refreshCodeBlockRecyclerView(codeList.size() - 1);
                    } else {
                        Utils.showOKDialog(this, R.string.no_code_dialog_message);
                    }
                })
                .addOnFailureListener(e -> {
                    // Task failed with an exception
                    // ...
                });
    }

    private void checkAndCorrectCode() {
        code.replaceAll("o", "0");
        code.replaceAll("s", "5");
    }

    private FirebaseVisionImage imageFromArray(byte[] byteArray) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        return image;
    }


}