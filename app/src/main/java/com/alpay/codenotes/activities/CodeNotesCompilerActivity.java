package com.alpay.codenotes.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.adapter.CodeBlockViewAdapter;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.Utils;
import com.alpay.codenotes.view.AutofitVerticalRecyclerView;
import com.alpay.codenotes.view.CameraPreview;
import com.alpay.codenotes.view.utils.MarginDecoration;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.ArrayList;
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


public class CodeNotesCompilerActivity extends BaseActivity{

    @BindView(R.id.surface_view)
    FrameLayout cameraView;
    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.read_code_button)
    FloatingActionButton readCodeButton;
    @BindView(R.id.codeblocks_recycler_view)
    AutofitVerticalRecyclerView blocksRecyclerView;

    private static  final int FOCUS_AREA_SIZE= 300;
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

    private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
        int result;
        if (Math.abs(touchCoordinateInCameraReper)+focusAreaSize/2>1000){
            if (touchCoordinateInCameraReper>0){
                result = 1000 - focusAreaSize/2;
            } else {
                result = -1000 + focusAreaSize/2;
            }
        } else{
            result = touchCoordinateInCameraReper - focusAreaSize/2;
        }
        return result;
    }


    private Rect calculateFocusArea(float x, float y) {
        int left = clamp(Float.valueOf((x / cameraView.getWidth()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        int top = clamp(Float.valueOf((y / cameraView.getHeight()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);

        return new Rect(left, top, left + FOCUS_AREA_SIZE, top + FOCUS_AREA_SIZE);
    }

    private void setCameraFocus(){
        cameraView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mCamera != null) {
                    mCamera.cancelAutoFocus();

                    Rect focusRect = calculateFocusArea(event.getX(), event.getY());

                    Camera.Parameters parameters = mCamera.getParameters();
                    if (parameters.getFocusMode().equals(Camera.Parameters.FOCUS_MODE_AUTO)) {
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    }

                    if (parameters.getMaxNumFocusAreas() > 0) {
                        List<Camera.Area> mylist = new ArrayList<Camera.Area>();
                        mylist.add(new Camera.Area(focusRect, 1000));
                        parameters.setFocusAreas(mylist);
                    }

                    try {
                        mCamera.cancelAutoFocus();
                        mCamera.setParameters(parameters);
                        mCamera.startPreview();
                        mCamera.autoFocus(new Camera.AutoFocusCallback() {
                            @Override
                            public void onAutoFocus(boolean success, Camera camera) {
                                if (!camera.getParameters().getFocusMode().equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                                    Camera.Parameters parameters = camera.getParameters();
                                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                                    if (parameters.getMaxNumFocusAreas() > 0) {
                                        parameters.setFocusAreas(null);
                                    }
                                    camera.setParameters(parameters);
                                    camera.startPreview();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }

        });
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
        setCameraFocus();
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

    }

    private Camera.PictureCallback picture = (data, camera) -> {
        recognizeText(data);
        camera.startPreview();
    };

    private void addRectangle(int left, int top, int width, int height){
        ShapeDrawable sd = new ShapeDrawable(new RectShape());
        sd.getPaint().setColor(0xFFFFFFFF);
        sd.getPaint().setStyle(Paint.Style.STROKE);
        sd.getPaint().setStrokeWidth(2);
        View shapeView = new View(this);
        shapeView.setBackground(sd);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        params.setMargins(left, top, 0, 0);
        cameraView.addView(shapeView, params);
    }


    private void recognizeText(byte[] bytes) {
        FirebaseVisionImage image = imageFromArray(bytes);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        detector.processImage(image)
                .addOnSuccessListener(firebaseVisionText -> {
                    /*for (FirebaseVisionText.TextBlock textBlock : firebaseVisionText.getTextBlocks()){
                        Rect rect = textBlock.getBoundingBox();
                        addRectangle(rect.left, rect.top, rect.width(), rect.height());
                    }*/
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
        code = code.replaceAll("\\so\\s", "0");
        code = code.replaceAll("\\ss\\s", "5");
    }

    private FirebaseVisionImage imageFromArray(byte[] byteArray) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        return image;
    }


}