package com.alpay.codenotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.alpay.codenotes.R;
import com.alpay.codenotes.utils.Utils;

import static com.alpay.codenotes.BaseApplication.auth;

public class SplashActivity extends BaseActivity {


    private static final int SPLASH_DELAY = 500;
    private final Handler mHandler = new Handler();
    private final Launcher mLauncher = new Launcher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
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
        if (auth.getCurrentUser() == null && Utils.getBooleanFromSharedPreferences(this, Utils.USER_LOGIN_KEY)) {
            Intent intent = new Intent(this, AuthUiActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
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
