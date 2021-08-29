package com.alpay.codenotes.activities;

import android.os.Bundle;

import com.alpay.codenotes.R;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.Utils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    final String TAG = HomeActivity.class.getSimpleName();
    String bundleKey = "";
    Bundle bundle;

    @OnClick(R.id.nav_practice)
    public void openProgrammingArea() {
        NavigationManager.openFragment(this, NavigationManager.PROGRAM_LIST);
    }

    @OnClick(R.id.nav_learn)
    public void openLearningActivities() {
        NavigationManager.openFragment(this, NavigationManager.CONTENT);
    }

    @OnClick(R.id.nav_game)
    public void openHourOfCodeGame() {
        NavigationManager.openFragment(this, NavigationManager.LEVEL);
    }

    @OnClick(R.id.nav_user)
    public void openSignedInAccount(){
        NavigationManager.openFragment(this, NavigationManager.ACCOUNT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Utils.initFirebaseAnalytics(this);
        Utils.sendAnalyticsData(TAG, "App starts.");
        bundle = getIntent().getExtras();
        if (bundle != null) {
            bundleKey = bundle.getString(NavigationManager.BUNDLE_KEY);
            NavigationManager.openFragment(this, bundleKey);
        } else {
            NavigationManager.openFragment(this, NavigationManager.CONTENT);
        }
    }

}
