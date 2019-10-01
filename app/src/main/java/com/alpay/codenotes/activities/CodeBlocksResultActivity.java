package com.alpay.codenotes.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alpay.codenotes.R;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.WebChromeClient;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CodeBlocksResultActivity extends BaseActivity {

    private String[] p5Code = {};
    Bundle bundle;
    private String url = "";
    private boolean isFlappy = false;
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

    @BindView(R.id.error_image)
    ImageView imageView;

    @BindView(R.id.error_layout)
    LinearLayout errorLayout;

    @OnClick(R.id.back_code_button)
    public void backToProgramming(){
        super.onBackPressed();
    }

    @OnClick(R.id.save_code_button)
    public void saveProgram(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Programı Kaydet");
        alertDialog.setMessage("Program ismini gir:");

        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setNeutralButton("OK", (dialog, which) -> {
            String programName = input.getText().toString();
            String programCode = "";
            for (String code: p5Code){
                programCode += code;
            }
            NavigationManager.programListFragment.saveProgram(programName, programCode);
            NavigationManager.openFragment(this, NavigationManager.PROGRAM_LIST);
        });
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_blocks_result);
        ButterKnife.bind(this);
        bundle = getIntent().getExtras();
        setWebView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (bundle != null) {
            p5Code = bundle.getStringArray(NavigationManager.BUNDLE_CODE_KEY);
            isFlappy = bundle.getBoolean(NavigationManager.BUNDLE_FLAPPY_KEY);
            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                public void onPageFinished(WebView view, String url) {
                    mHandler.postDelayed(mLauncher, 500);
                }
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
                    if (url.equals(failingUrl)) {
                        view.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                        Glide.with(CodeBlocksResultActivity.this).load("file:///android_asset/lottie/empty.gif").into(imageView);
                    }
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }
            });
            if (isFlappy){
                url = "https://codenotesalpay.web.app/processing/flappy-game/index.html";
            } else{
                url = "https://codenotesalpay.web.app/processing/index.html";
            }
            webView.loadUrl(url);
        }
    }

    public void setWebView() {
        WebSettings webSettings = webView.getSettings();
        webView.setWebChromeClient(new WebChromeClient());
        webView.setPadding(0, 0, 0, 0);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    public void runCode() {
        for (String codeLine : p5Code) {
            codeLine = codeLine.replace("\n", "").replace("\r", "");
            evalCode("addCodeInput('" + codeLine + "')");
        }
        evalCode("runP5Code()");
    }

    public void evalCode(String code) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(code, null);
        } else {
            webView.loadUrl("javascript: (function () {" + code + "}());");
        }
    }
}
