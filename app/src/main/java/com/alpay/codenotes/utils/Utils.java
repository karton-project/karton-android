package com.alpay.codenotes.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alpay.codenotes.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;

public class Utils {

    public static boolean noConnectionErrorDisplayed = false;
    public static final String USER_NAME_KEY = "user_name";
    public static final String USER_EMAIL_KEY = "user_email";
    public static final String USER_LOGIN_KEY = "is_login";
    public static final String DB_VERSION_KEY = "db_version";
    public static final String IS_FIRST_OPEN_KEY = "isfirst";
    public static final String CLOSE_FLAPPY = "closeflappy";
    public static String code = "";

    public static boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec(command).waitFor() == 0);
    }

    public static void showOKDialog(AppCompatActivity activity, int stringID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(stringID)
                .setCancelable(true)
                .setNeutralButton(android.R.string.ok, (dialog, id) -> {
                    //do things
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showOKDialogForIntent(AppCompatActivity activity, int stringID, final Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(stringID)
                .setCancelable(true)
                .setPositiveButton("OK", (dialog, id) -> {
                    if (intent != null) {
                        activity.startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static boolean isCameraAvailable(AppCompatActivity appCompatActivity) {
        PackageManager pm = appCompatActivity.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static void addStringToSharedPreferences(AppCompatActivity appCompatActivity, String key, String value) {
        SharedPreferences settings = appCompatActivity.getSharedPreferences("cndata", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void addIntegerToSharedPreferences(AppCompatActivity appCompatActivity, String key, int value) {
        SharedPreferences settings = appCompatActivity.getSharedPreferences("cndata", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void addBooleanToSharedPreferences(AppCompatActivity appCompatActivity, String key, boolean value) {
        SharedPreferences settings = appCompatActivity.getSharedPreferences("cndata", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String getStringFromSharedPreferences(AppCompatActivity appCompatActivity, String key) {
        SharedPreferences settings = appCompatActivity.getSharedPreferences("cndata", 0);
        return settings.getString(key, "");
    }

    public static int getIntegerFromSharedPreferences(AppCompatActivity appCompatActivity, String key) {
        SharedPreferences settings = appCompatActivity.getSharedPreferences("cndata", 0);
        return settings.getInt(key, 0);
    }

    public static boolean getBooleanFromSharedPreferences(AppCompatActivity appCompatActivity, String key) {
        SharedPreferences settings = appCompatActivity.getSharedPreferences("cndata", 0);
        return settings.getBoolean(key, false);
    }

    private static FirebaseAnalytics mFirebaseAnalytics;

    public static void initFirebaseAnalytics(AppCompatActivity appCompatActivity) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(appCompatActivity);
    }

    public static int convertToDip(Context ctx, float px) {
        return (int) (px * (ctx.getResources().getDisplayMetrics().density + 0.5f));
    }

    public static int convertToPx(Context ctx, float dp) {
        Resources r = ctx.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static int convertPxFromSp(Context ctx, float sp) {
        Resources r = ctx.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, r.getDisplayMetrics());
    }

    public static String getStringFromResource(Context context, int resourceId) {
        return context.getResources().getString(resourceId);
    }

    public static boolean isInternetAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public static String readInputStreamAsString(InputStream in)
            throws IOException {

        BufferedInputStream bis = new BufferedInputStream(in);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while (result != -1) {
            byte b = (byte) result;
            buf.write(b);
            result = bis.read();
        }
        return buf.toString();
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public static void sendAnalyticsData(String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        if (mFirebaseAnalytics != null)
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static void showErrorToast(AppCompatActivity activityCompat, int stringID, int duration) {
        LayoutInflater inflater = activityCompat.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_error, activityCompat.findViewById(R.id.error_toast_container));
        TextView text = layout.findViewById(R.id.error_toast_text);
        text.setText(activityCompat.getResources().getString(stringID));
        Toast toast = new Toast(activityCompat.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }

    public static void showWarningToast(AppCompatActivity activityCompat, int stringID, int duration) {
        LayoutInflater inflater = activityCompat.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_warning, activityCompat.findViewById(R.id.warning_toast_container));
        TextView text = layout.findViewById(R.id.warning_toast_text);
        text.setText(activityCompat.getResources().getString(stringID));
        Toast toast = new Toast(activityCompat.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }

    public static Drawable getDrawableWithName(AppCompatActivity appCompatActivity, String fileName) {
        Drawable drawable;
        try {
            String[] assetFileNames = appCompatActivity.getAssets().list("storytr");
            if (Arrays.asList(assetFileNames).contains(fileName)) {
                InputStream inputStream = appCompatActivity.getAssets().open("storytr/" + fileName);
                drawable = Drawable.createFromStream(inputStream, null);
                inputStream.close();
            } else {
                String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                if (!directoryPath.isEmpty()) {
                    directoryPath += "/CodeNotes/Drawable";
                } else {
                    directoryPath = "storage/self/primary/CodeNotes/Drawable";
                }
                File file = new File(directoryPath + "/" + fileName);
                drawable = Drawable.createFromPath(file.getAbsolutePath());
            }
            return drawable;
        } catch (IOException ex) {
            return null;
        }
    }

    public static Drawable encodeImageDrawableFromBase64(Context context, String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        return drawable;
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    public static Bitmap decodeSampledBitmapFromStream(InputStream inputStream, int reqWidth, int reqHeight) {
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return resizeBitmap(bitmap, reqWidth, reqHeight);
    }

    public static Bitmap resizeBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

}
