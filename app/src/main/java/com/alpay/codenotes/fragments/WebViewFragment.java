package com.alpay.codenotes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.alpay.codenotes.R;
import com.alpay.codenotes.utils.NavigationManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WebViewFragment extends Fragment {

    private View view;
    private Unbinder unbinder;
    private String url;

    @BindView(R.id.error_layout)
    LinearLayout errorLayout;

    @BindView(R.id.webview_frame)
    WebView webView;

    @Nullable
    @BindView(R.id.webview_load_anim)
    LottieAnimationView loadAnim;

    public WebViewFragment() {

    }

    public WebViewFragment(String webURL) {
        this.url = webURL;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_web_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        setWebView();
        return view;
    }

    public void setWebView() {
        webView.setPadding(0, 0, 0, 0);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.addJavascriptInterface(new JavaScriptInterface(this.getContext()), "Android");
        WebSettings webSettings = webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                if (loadAnim != null)
                    loadAnim.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
                if (url.equals(failingUrl)) {
                    view.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                }
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
    }

    public class JavaScriptInterface {
        Context mContext;

        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void openHomePage() {
            NavigationManager.openFragment((AppCompatActivity) getActivity(), NavigationManager.CONTENT);
        }

    }


    @Override
    public void onStart() {
        webView.loadUrl(url);
        super.onStart();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }

}
