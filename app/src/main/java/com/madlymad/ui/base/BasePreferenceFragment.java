package com.madlymad.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceScreen;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.madlymad.Prefs;
import com.madlymad.ui.listeners.AppPreferenceChangeListener;
import com.madlymad.ui.listeners.OnActionListener;
import com.madlymad.ui.listeners.OnChangeListener;
import com.madlymad.uptime.R;

import java.util.Objects;


/**
 * Base reusable class that contains all basic functionality for preferences.
 * <p>
 * Created by mando on 11/3/2017.
 */

public abstract class BasePreferenceFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener {

    private static final String LOG_TAG = BasePreferenceFragment.class.getSimpleName();
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static final AppPreferenceChangeListener sBindPreferenceSummaryToValueListener =
            new AppPreferenceChangeListener();
    private String rootKey;

    private String previousTitle;
    private String currentTitle;

    private void setTitle(String title) {
        if (isAdded() && getActivity() != null) {
            getActivity().setTitle(title);
        }
        currentTitle = title;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getActivity() != null) {
            previousTitle = (String) getActivity().getTitle();

            if (!TextUtils.isEmpty(currentTitle)) {
                getActivity().setTitle(currentTitle);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (getActivity() != null) {
            if (!TextUtils.isEmpty(previousTitle)) {
                getActivity().setTitle(previousTitle);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Hide keypad when a Preference Fragment is created
        if (getActivity() != null) {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && getView() != null) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

    protected Preference getPreference(@StringRes int key) {
        return getPreference(getKey(key));
    }

    private Preference getPreference(String key) {
        return getPreferenceScreen().findPreference(key);
    }

    protected String getKey(@StringRes int key) {
        return Prefs.getKey(Objects.requireNonNull(getContext()), key);
    }

    protected void addPreference(Preference preference) {
        if (preference != null) {
            getPreferenceScreen().addPreference(preference);
        }
    }

    protected void removePreference(@Nullable Preference preference) {
        if (preference != null) {
            getPreferenceScreen().removePreference(preference);
        }
    }

    protected void setOnClickPreference(@StringRes int key, @NonNull final OnActionListener onActionListener) {
        Preference preference = getPreference(key);
        if (preference != null) {
            preference.setOnPreferenceClickListener(preference1 -> {
                onActionListener.onAction();
                return true;
            });
        }
    }

    @SuppressWarnings("SameParameterValue")
    protected void setOnChangePreference(@StringRes int key, @NonNull final OnChangeListener onChangeListener) {
        Preference preference = getPreference(key);
        if (preference != null) {
            preference.setOnPreferenceChangeListener((preference1, newValue) -> onChangeListener.onChange(newValue));
        }
    }

    @Override
    public final void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        this.rootKey = rootKey;
        this.onCreatePreferencesScreen(savedInstanceState, rootKey);
        setTitle((String) getPreference(rootKey).getTitle());
    }

    protected String getRootKey() {
        return rootKey;
    }

    protected abstract void onCreatePreferencesScreen(Bundle savedInstanceState, String rootKey);

    /**
     * @param preference the preference to compare
     * @param key        the key to compare if refer to this preference
     * @return <code>true</code> in case the screen is added and that two keys are equal
     */
    protected boolean is(Preference preference, @StringRes int key) {
        return getContext() != null && Prefs.getKey(getContext(), key).equals(preference.getKey());
    }

    public void updateSummaries() {
        // Bind the summaries of EditText/List/Dialog/Ringtone preferences
        // to their values. When their values change, their summaries are
        // updated to reflect the new value, per the Android Design
        // guidelines.
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        setupSummaries(preferenceScreen);
    }

    protected void setSummary(@StringRes int key, String summary) {
        Preference preference = getPreference(key);
        if (preference != null) {
            preference.setSummary(summary);
        }
    }

    private void setupSummaries(PreferenceGroup preferenceScreen) {
        int count = preferenceScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference preference = preferenceScreen.getPreference(i);
            if (preference instanceof PreferenceGroup) {
                PreferenceGroup group = (PreferenceGroup) preference;
                setupSummaries(group);
            } else if (preference.getClass() != Preference.class) {
                // Show summary for all other preferences
                bindPreferenceSummaryToValue(preference);
            }
        }
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        if (preference == null) {
            Log.e(LOG_TAG, "Preference is null cannot bind summary");
            return;
        }
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        bindPreferenceValue(preference, null);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        bindPreferenceValue(preference, newValue);
        return true;
    }

    private void bindPreferenceValue(Preference preference, Object newValue) {
        if (preference instanceof SwitchPreference) {
            if (newValue == null) {
                newValue = Prefs.getBooleanValue(preference.getContext(), preference.getKey(), false);
            }
            newValue = (Boolean) newValue ?
                    preference.getContext().getString(R.string.on) :
                    preference.getContext().getString(R.string.off);
        } else if (!(preference instanceof CheckBoxPreference)) {
            if (newValue == null) {
                newValue = Prefs.getValue(preference.getContext(), preference.getKey(),
                        preference instanceof MultiSelectListPreference);
            }
        }

        // Trigger the listener immediately with the preference's current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, newValue);
    }
}
