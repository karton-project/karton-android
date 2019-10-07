package com.alpay.codenotes.fragments;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alpay.codenotes.R;
import com.alpay.codenotes.view.InkView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import top.defaults.colorpicker.ColorPickerPopup;


public class SketchFragment extends Fragment {

    public static InkView inkView;
    private static int frameID;
    private String fileName;
    private String directoryPath;
    Unbinder unbinder;
    View view;

    @OnClick(R.id.sketch_clean)
    public void cleanSketch() {
        SketchFragment.inkView.clear();
    }

    @OnClick(R.id.sketch_eraser)
    public void eraseSketch() {
        SketchFragment.inkView.setColor(Color.WHITE);
    }

    @OnClick(R.id.sketch_draw)
    public void drawSketch() {
        SketchFragment.inkView.setColor(Color.BLACK);
    }

    @OnClick(R.id.sketch_color_picker)
    public void changeColor() {
        new ColorPickerPopup.Builder(getActivity())
                .initialColor(Color.BLUE) // Set initial color
                .enableBrightness(true) // Enable brightness slider or not
                .enableAlpha(true) // Enable alpha slider or not
                .okTitle(getResources().getString(R.string.ok))
                .cancelTitle(getResources().getString(R.string.cancel))
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        SketchFragment.inkView.setColor(color);
                    }

                    @Override
                    public void onColor(int color, boolean fromUser) {

                    }
                });
    }

    public SketchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sketch, container, false);
        unbinder = ButterKnife.bind(this, view);
        prepareInkView();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    private void prepareInkView() {
        inkView = view.findViewById(R.id.ink);
        inkView.hasFlag(InkView.FLAG_INTERPOLATION);
        inkView.addFlag(InkView.FLAG_RESPONSIVE_WIDTH);
        inkView.setBackgroundColor(Color.WHITE);
    }

    public static void setFrameID(int frameID) {
        SketchFragment.frameID = frameID;
    }

    public void saveBitmap() {
        Bitmap bitmap = inkView.getBitmap();
        Bitmap bmp = inkView.getBitmap();
        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp, 0, 0, null);
        directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (!directoryPath.isEmpty()) {
            directoryPath += "/CodeNotes/Drawable";
        } else {
            directoryPath = "storage/self/primary/CodeNotes/Drawable";
        }
        fileName = "sketch" + frameID + ".png";
        File file = new File(directoryPath + "/" + fileName);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.flush();
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inkView.clear();
    }

    @OnClick(R.id.sketch_save_button)
    public void saveSketch() {
        saveBitmap();
    }


}