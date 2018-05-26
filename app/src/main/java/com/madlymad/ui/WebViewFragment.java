package com.madlymad.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.madlymad.ui.base.BaseFragment;
import com.madlymad.uptime.R;

/**
 * https://www.bignerdranch.com/blog/open-source-licenses-and-android/
 */
public class WebViewFragment extends BaseFragment {

    public static final String TAG = "WebView";
    private String url;

    public WebViewFragment() {
        setRetainInstance(true);
    }

    public static String buildAsset(@SuppressWarnings("SameParameterValue") String filename) {
        return "file:///android_asset/" + filename;
    }

    public static WebViewFragment newInstance(String url) {
        WebViewFragment fragment = new WebViewFragment();
        fragment.url = url;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.webview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebView webView = (WebView) view;
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                setTitle(view.getTitle());
            }
        });
    }

}