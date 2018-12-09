package com.madlymad.uptime.models.data;

import androidx.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public abstract class PreferenceLiveData<T> extends LiveData<T> {
    final Context context;
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesObserver;

    PreferenceLiveData(Context context) {
        this.context = context;

        preferencesObserver = (sharedPreferences, key) -> {
            if (this.containsPreferenceKey(key)) {
                loadData();
            }
        };

        loadData();
    }

    protected abstract boolean containsPreferenceKey(String key);

    public abstract void backgroundDataLoad();


    @Override
    protected void onActive() {
        PreferenceManager.getDefaultSharedPreferences(context)
                .registerOnSharedPreferenceChangeListener(preferencesObserver);
    }

    @Override
    protected void onInactive() {
        PreferenceManager.getDefaultSharedPreferences(context)
                .unregisterOnSharedPreferenceChangeListener(preferencesObserver);
    }

    private void loadData() {
        new Thread(this::backgroundDataLoad).start();
    }

}
