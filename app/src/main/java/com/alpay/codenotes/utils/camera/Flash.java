package com.alpay.codenotes.utils.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

public class Flash {
    private static Camera cam;
    private static Camera.Parameters params;
    private  static CameraManager cameraManager;
    private static String cameraID;

    public static boolean openFlash(AppCompatActivity appCompatActivity) {
        if(!appCompatActivity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cameraManager = (CameraManager) appCompatActivity.getSystemService(Context.CAMERA_SERVICE);
            try {
                cameraID = cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(cameraID, true);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }else{
            cam = Camera.open();
            params = cam.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            cam.setParameters(params);
            cam.startPreview();
        }
        return true;
    }

    public static void closeFlash(AppCompatActivity appCompatActivity) {
        if(!appCompatActivity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                cameraManager.setTorchMode(cameraID, false);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }else{
            cam.stopPreview();
            cam.release();
        }
    }
}
