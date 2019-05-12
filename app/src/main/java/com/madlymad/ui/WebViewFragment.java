package com.madlymad.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.madlymad.ui.base.BaseFragment;
import com.madlymad.uptime.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * https://www.bignerdranch.com/blog/open-source-licenses-and-android/
 */
public class WebViewFragment extends BaseFragment {

    public static final String TAG = "WebView";
    private static final String WEB_URL = "url";
    private String url;

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
        if (savedInstanceState != null) {
            url = savedInstanceState.getString(WEB_URL, url);
        }
        return inflater.inflate(R.layout.webview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebView webView = view.findViewById(R.id.webView);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                setTitle(view.getTitle());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(WEB_URL, url);
        super.onSaveInstanceState(outState);
    }
}
