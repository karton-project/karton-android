package com.alpay.codenotes;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.alpay.codenotes.models.User;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;
import io.fabric.sdk.android.Fabric;

public class BaseApplication extends Application {

    public static FirebaseAuth auth;
    public static User user = new User();
    public static String userID;
    public static int version;

    public static DatabaseReference ref;


    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Fabric.with(this, new Crashlytics());
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        if (auth.getCurrentUser() != null) {
            userID = auth.getUid();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}