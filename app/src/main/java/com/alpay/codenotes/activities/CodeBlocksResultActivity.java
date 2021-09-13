package com.alpay.codenotes.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.alpay.codenotes.R;
import com.alpay.codenotes.utils.Constants;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.Utils;
import com.alpay.codenotes.view.SaveProgramDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CodeBlocksResultActivity extends BaseActivity {

    private String[] p5Code = {};
    Bundle bundle;
    private String url = "";
    private boolean turtleMode = false;
    private final Handler mHandler = new Handler();
    private final Launcher mLauncher = new Launcher();

    private class Launcher implements Runnable {
        @Override
        public void run() {
            runCode();
        }
    }

    @BindView(R.id.result_webview)
    WebView webView;

    @BindView(R.id.error_layout)
    LinearLayout errorLayout;

    @OnClick(R.id.back_code_button)
    public void backToProgramming() {
        super.onBackPressed();
    }

    @OnClick(R.id.save_code_button)
    public void saveProgram() {
        SaveProgramDialog saveProgramDialog = new SaveProgramDialog(this);
        saveProgramDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_blocks_result);
        ButterKnife.bind(this);
        bundle = getIntent().getExtras();
        setWebView();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onStart() {
        super.onStart();
        if (bundle != null) {
            p5Code = bundle.getStringArray(NavigationManager.BUNDLE_CODE_KEY);
            turtleMode = bundle.getBoolean(NavigationManager.BUNDLE_TURTLE);
            webView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    mHandler.postDelayed(mLauncher, 500);
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    if (url.equals(failingUrl)) {
                        view.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                    }
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }
            });
            if (turtleMode) {
                if (Utils.getStringFromSharedPreferences(this, "CODE_LANG").contentEquals("UK")) {
                    url = Constants.TURTLE_EN_CODE;
                } else {
                    url = Constants.TURTLE_TR_CODE;
                }
            } else {
                if (Utils.getStringFromSharedPreferences(this, "CODE_LANG").contentEquals("UK")) {
                    url = Constants.EN_CODE;
                } else {
                    url = Constants.TR_CODE;
                }
            }
            webView.loadUrl(url);
        }
    }

    public void setWebView() {
        WebSettings webSettings = webView.getSettings();
        webView.setPadding(0, 0, 0, 0);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    public void runCode() {
        for (String codeLine : p5Code) {
            codeLine = codeLine.trim().replace("\n", "#");
            evalCode("addCodeInput('" + codeLine + "');");
        }
        evalCode("runP5Code();");
    }

    public void evalCode(String code) {
        webView.loadUrl("javascript: (function () {" + code + "}());");
    }
}
