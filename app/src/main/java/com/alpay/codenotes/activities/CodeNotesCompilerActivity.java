package com.alpay.codenotes.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.adapter.CodeBlockViewAdapter;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.Utils;
import com.alpay.codenotes.view.AutofitVerticalRecyclerView;
import com.alpay.codenotes.view.utils.MarginDecoration;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.alpay.codenotes.models.GroupHelper.codeList;
import static com.alpay.codenotes.models.GroupHelper.groupId;
import static com.alpay.codenotes.utils.NavigationManager.BUNDLE_CODE_KEY;
import static com.alpay.codenotes.utils.NavigationManager.BUNDLE_FLAPPY_KEY;


public class CodeNotesCompilerActivity extends BaseActivity {

    @BindView(R.id.surface_view)
    SurfaceView cameraView;
    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.read_code_button)
    FloatingActionButton readCodeButton;
    @BindView(R.id.codeblocks_recycler_view)
    AutofitVerticalRecyclerView blocksRecyclerView;

    CameraSource cameraSource;
    TextRecognizer textRecognizer;
    CodeBlockViewAdapter codeBlockViewAdapter;
    private boolean isFlappy = false;

    final int RequestCameraPermissionID = 1001;
    String code;

    @OnClick(R.id.read_code_button)
    public void readCode() {
        if (code != null) {
            codeList.add(code);
            refreshCodeBlockRecyclerView(codeList.size() - 1);
        } else {
            Utils.showOKDialog(this, R.string.no_code_dialog_message);
        }
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
    public void backToHome(){
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
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
        textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        buildCameraSourceForText();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if (bundle.getStringArray(BUNDLE_CODE_KEY) != null){
                String[] p5code = bundle.getStringArray(BUNDLE_CODE_KEY);
                for (String code : p5code){
                    codeList.add(code);
                }
            }
            if (bundle.getString(NavigationManager.BUNDLE_KEY) != null) {
                openHintView(bundle.getString(NavigationManager.BUNDLE_KEY));
            }
            if (bundle.getBoolean(BUNDLE_FLAPPY_KEY)){
                isFlappy = true;
            }
        }
        recognizeText();
        setUpRecyclerView();
        refreshCodeBlockRecyclerView(codeList.size() -1);
    }

    private void openHintView(String instructions){
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


    private void buildCameraSourceForText() {
        cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(2.0f)
                .setAutoFocusEnabled(true)
                .build();
        startCamera();
    }

    private void startCamera() {
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CodeNotesCompilerActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                RequestCameraPermissionID);
                        return;
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });
    }

    private void recognizeText() {
        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {
                final SparseArray<TextBlock> items = detections.getDetectedItems();
                if (items.size() != 0) {
                    textView.post(() -> {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < items.size(); ++i) {
                            TextBlock item = items.valueAt(i);
                            stringBuilder.append(item.getValue());
                            stringBuilder.append("\n");
                        }
                        code = stringBuilder.toString().toLowerCase();
                        if (code.contains("group:")){
                            groupId = code.substring(6);
                        }
                        textView.setText(code);
                    });
                }
            }
        });
    }

}