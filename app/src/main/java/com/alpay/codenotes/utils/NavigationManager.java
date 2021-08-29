package com.alpay.codenotes.utils;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.alpay.codenotes.R;
import com.alpay.codenotes.activities.AuthUiActivity;
import com.alpay.codenotes.activities.FBVisionActivity;
import com.alpay.codenotes.activities.HomeActivity;
import com.alpay.codenotes.fragments.LevelFragment;
import com.alpay.codenotes.activities.TransferLearningActivity;
import com.alpay.codenotes.activities.WelcomeActivity;
import com.alpay.codenotes.fragments.AccountFragment;
import com.alpay.codenotes.fragments.ContentListFragment;
import com.alpay.codenotes.fragments.ProgramListFragment;
import com.alpay.codenotes.fragments.SketchFragment;
import com.alpay.codenotes.fragments.StudyNotesFragment;
import com.alpay.codenotes.fragments.WebViewFragment;

public class NavigationManager {

    public static final String BUNDLE_KEY = "bundlekey";
    public static final String BUNDLE_CODE_KEY = "codekey";
    public static final String BUNDLE_TURTLE = "turtle";
    public static final String LEVEL = "level";
    public static final String CONTENT = "content";
    public static final String SKETCH = "sketch";
    public static final String NOTES = "notes";
    public static final String ACCOUNT = "account";
    public static final String PROGRAM_LIST = "programlist";

    public static void openFragment(AppCompatActivity appCompatActivity, String fragmentID) {
        if (!appCompatActivity.getClass().getSimpleName().contentEquals("HomeActivity")) {
            Intent intent = new Intent(appCompatActivity, HomeActivity.class);
            intent.putExtra(BUNDLE_KEY, fragmentID);
            appCompatActivity.startActivity(intent);
            return;
        }
        FragmentTransaction ft = appCompatActivity.getSupportFragmentManager().beginTransaction();
        if (fragmentID.contentEquals(CONTENT)) {
            ContentListFragment contentListFragment = new ContentListFragment();
            ft.replace(R.id.fragment_container, contentListFragment);
        } else if (fragmentID.contentEquals(LEVEL)) {
            LevelFragment levelFragment = new LevelFragment();
            ft.replace(R.id.fragment_container, levelFragment);
        } else if (fragmentID.contentEquals(SKETCH)) {
            SketchFragment sketchFragment = new SketchFragment();
            ft.replace(R.id.fragment_container, sketchFragment);
        } else if (fragmentID.contentEquals(NOTES)) {
            StudyNotesFragment studyNotesFragment = new StudyNotesFragment();
            ft.replace(R.id.fragment_container, studyNotesFragment);
        } else if (fragmentID.contentEquals(PROGRAM_LIST)) {
            ProgramListFragment programListFragment = new ProgramListFragment();
            ft.replace(R.id.fragment_container, programListFragment);
        } else if (fragmentID.contentEquals(ACCOUNT)) {
            AccountFragment accountFragment = new AccountFragment();
            ft.replace(R.id.fragment_container, accountFragment);
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public static void openWebViewFragment(AppCompatActivity appCompatActivity, String url) {
        FragmentTransaction ft = appCompatActivity.getSupportFragmentManager().beginTransaction();
        WebViewFragment webViewFragment = new WebViewFragment(url);
        ft.replace(R.id.fragment_container, webViewFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public static void openPracticeWithInstructions(AppCompatActivity appCompatActivity, String instructions) {
        Intent intent = new Intent(appCompatActivity, FBVisionActivity.class);
        intent.putExtra(NavigationManager.BUNDLE_KEY, instructions);
        appCompatActivity.startActivity(intent);
    }

    public static void openLevelActivity(AppCompatActivity appCompatActivity) {
        Intent intent = new Intent(appCompatActivity, LevelFragment.class);
        appCompatActivity.startActivity(intent);
    }

    public static void openWelcomeActivity(AppCompatActivity appCompatActivity) {
        Intent intent = new Intent(appCompatActivity, WelcomeActivity.class);
        appCompatActivity.startActivity(intent);
        appCompatActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        appCompatActivity.finish();
    }

    public static void openAuthUIActivity(AppCompatActivity appCompatActivity) {
        Intent intent = new Intent(appCompatActivity, AuthUiActivity.class);
        appCompatActivity.startActivity(intent);
        appCompatActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        appCompatActivity.finish();
    }

    public static void openHomeActiviy(AppCompatActivity appCompatActivity) {
        Intent intent = new Intent(appCompatActivity, HomeActivity.class);
        appCompatActivity.startActivity(intent);
        appCompatActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        appCompatActivity.finish();
    }

    public static void openTransferLearning(AppCompatActivity appCompatActivity) {
        Intent intent = new Intent(appCompatActivity, TransferLearningActivity.class);
        appCompatActivity.startActivity(intent);
    }
}
