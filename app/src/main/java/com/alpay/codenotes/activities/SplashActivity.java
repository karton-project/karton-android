package com.alpay.codenotes.activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.alpay.codenotes.BaseApplication;
import com.alpay.codenotes.R;
import com.alpay.codenotes.models.Content;
import com.alpay.codenotes.models.ContentHelper;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.alpay.codenotes.BaseApplication.auth;

public class SplashActivity extends BaseActivity {


    private static final int SPLASH_DELAY = 500;
    private final Handler mHandler = new Handler();
    private final Launcher mLauncher = new Launcher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Utils.isInternetAvailable(this)) {
            BaseApplication.ref.child("tr/version").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int version = ((Long) dataSnapshot.getValue()).intValue();
                    if (Utils.getIntegerFromSharedPreferences(SplashActivity.this, Utils.DB_VERSION_KEY) != version) {
                        generateContentListFromFirebase();
                        Utils.addIntegerToSharedPreferences(SplashActivity.this, Utils.DB_VERSION_KEY, version);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHandler.postDelayed(mLauncher, SPLASH_DELAY);
    }

    @Override
    protected void onStop() {
        mHandler.removeCallbacks(mLauncher);
        super.onStop();
    }

    public void checkIfUserAuthenticate() {
        if (!Utils.getBooleanFromSharedPreferences(this, Utils.IS_FIRST_OPEN_KEY)){
            Utils.addBooleanToSharedPreferences(this, Utils.IS_FIRST_OPEN_KEY, true);
            NavigationManager.openWelcomeActivity(this);
        }else{
            if (auth.getCurrentUser() == null && !Utils.getBooleanFromSharedPreferences(this, Utils.USER_LOGIN_KEY)) {
                NavigationManager.openAuthUIActivity(this);
            } else {
                NavigationManager.openHomeActiviy(this);
            }
        }
    }

    private void generateContentListFromFirebase() {
        ArrayList<Content> contentArrayList = new ArrayList<>();
        BaseApplication.ref.child("tr/contents").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Content content = dataSnapshot.getValue(Content.class);
                    contentArrayList.add(content);
                }
                ContentHelper.saveContentList(SplashActivity.this, contentArrayList);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });
    }

    private void launch() {
        if (!isFinishing()) {
            checkIfUserAuthenticate();
        }
    }

    private class Launcher implements Runnable {
        @Override
        public void run() {
            launch();
        }
    }
}
