package com.alpay.codenotes;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.alpay.codenotes.models.User;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;
import io.fabric.sdk.android.Fabric;

public class BaseApplication extends Application {

    public static FirebaseAuth auth;
    public static User user = new User();
    public static String userID;
    public static int version;

    private static final String TAG = BaseApplication.class.getSimpleName();
    private int mVisibleCount;
    private boolean mInBackground;
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
            ref.child("users/" + userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                mVisibleCount++;
                if (mInBackground && mVisibleCount > 0) {
                    mInBackground = false;
                    Log.i(TAG, "App in foreground");
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                mVisibleCount--;
                if (mVisibleCount == 0) {
                    if (activity.isFinishing()) {
                        saveUserData();
                        Log.i(TAG, "App is finishing");
                    } else {
                        mInBackground = true;
                        Log.i(TAG, "App in background");
                    }
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    public boolean isAppInBackground() {
        return mInBackground;
    }

    public boolean isAppVisible() {
        return mVisibleCount > 0;
    }

    public int getVisibleCount() {
        return mVisibleCount;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static boolean checkIfUserLogin(){
        return !userID.contentEquals("");
    }

    private void saveUserData(){
        if (checkIfUserLogin()){

            if (user.getGroupList() != null){
                ref.child("users").child(userID).child("groupList").setValue(user.getGroupList());
            }

            if (user.getNoteList() != null){
                ref.child("users").child(userID).child("noteList").setValue(user.getNoteList());
            }
        }

    }
}