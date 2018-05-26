package com.madlymad.uptime;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.madlymad.integration.crashlytics.MadCrashlytics;
import com.madlymad.ui.WebViewFragment;
import com.madlymad.ui.base.BaseActivity;

public class MainActivity extends BaseActivity implements
        PreferenceFragmentCompat.OnPreferenceStartScreenCallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            MadCrashlytics.initAskPermission(this);

            MainActivityFragment fragment = MainActivityFragment.newInstance();
            replaceFragment(fragment, MainActivityFragment.TAG);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_about:
                displayPreferences();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayPreferences() {
        MyPreferenceFragment fragment = MyPreferenceFragment.newInstance();
        addFragment(fragment, MyPreferenceFragment.TAG);
    }

    public void displayWebFragment(String url) {
        WebViewFragment fragment = WebViewFragment.newInstance(url);
        addFragment(fragment, WebViewFragment.TAG);
    }

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat preferenceFragmentCompat,
                                           PreferenceScreen preferenceScreen) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MyPreferenceFragment fragment = new MyPreferenceFragment();
        Bundle args = new Bundle();
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, preferenceScreen.getKey());
        fragment.setArguments(args);
        ft.replace(R.id.fragment, fragment, preferenceScreen.getKey());
        ft.addToBackStack(preferenceScreen.getKey());
        ft.commit();
        return true;
    }
}
