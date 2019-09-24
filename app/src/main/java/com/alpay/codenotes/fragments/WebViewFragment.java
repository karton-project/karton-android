package com.alpay.codenotes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alpay.codenotes.R;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.WebChromeClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WebViewFragment extends Fragment {

    private View view;
    private Unbinder unbinder;
    private String url;

    @BindView(R.id.webview_frame)
    WebView webView;

    @BindView(R.id.webview_progress_bar)
    ProgressBar progressBar;

    public WebViewFragment() {

    }

    public WebViewFragment(String webURL){
        this.url = webURL;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_web_view, container, false);
        unbinder =  ButterKnife.bind(this, view);
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
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Toast.makeText(view.getContext(), "Error", Toast.LENGTH_SHORT).show();
                super.onReceivedError(view, request, error);
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
