package com.madlymad.uptime;

import android.os.Bundle;
import android.view.MenuItem;

import com.madlymad.integration.crashlytics.MadCrashlytics;
import com.madlymad.ui.WebViewFragment;
import com.madlymad.ui.base.BaseActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class MainActivity extends BaseActivity implements
        PreferenceFragmentCompat.OnPreferenceStartScreenCallback,
        TimePickerDialogFragment.TimeDialogContainer, TimePickerDialogFragment.TimeDialogListener {

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

    @Override
    public TimePickerDialogFragment.TimeDialogListener onRequestListener() {
        return this;
    }

    @Override
    public void onDateSetDialog(int numericValue, int measurementValue) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(MainActivityFragment.TAG);
        if (fragment instanceof MainActivityFragment) {
            ((MainActivityFragment) fragment).onDateSetDialog(numericValue, measurementValue);
        }
    }
}
