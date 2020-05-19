package com.alpay.codenotes.activities;

import android.os.Bundle;
import android.os.Handler;

import com.alpay.codenotes.R;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.Utils;
import com.uxcam.UXCam;

import static com.alpay.codenotes.BaseApplication.auth;

public class SplashActivity extends BaseActivity {


    private static final int SPLASH_DELAY = 500;
    private final Handler mHandler = new Handler();
    private final Launcher mLauncher = new Launcher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        UXCam.startWithKey("x2f5tx2k2fpvspv");
        Utils.createTLModel(this);
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
