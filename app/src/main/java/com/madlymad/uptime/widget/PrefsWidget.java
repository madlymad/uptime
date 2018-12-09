package com.madlymad.uptime.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.madlymad.uptime.BuildConfig;
import com.madlymad.uptime.R;

/**
 * Created on 18/3/2018.
 *
 * @author mando
 */

class PrefsWidget {
    private static final String PREFS_NAME = BuildConfig.APPLICATION_ID + ".UptimeWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String KEY_TITLE = "title";
    private static final String KEY_SMART_TIMER = "timer_seconds";

    private static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences(PREFS_NAME, 0);
    }

    private static String getKey(int appWidgetId, String key) {
        return PREF_PREFIX_KEY + appWidgetId + key;
    }

    @SuppressWarnings("SameParameterValue")
    private static void putString(Context context, int appWidgetId, String key, String value) {
        SharedPreferences.Editor prefs = getPreference(context).edit();
        prefs.putString(getKey(appWidgetId, key), value);
        prefs.apply();
    }

    @SuppressWarnings("SameParameterValue")
    private static String getString(Context context, int appWidgetId, String key, String defaultValue) {
        SharedPreferences prefs = getPreference(context);
        String value = prefs.getString(getKey(appWidgetId, key), null);
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        } else {
            return value;
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static void putBoolean(Context context, int appWidgetId, String key, boolean value) {
        SharedPreferences.Editor prefs = getPreference(context).edit();
        prefs.putBoolean(getKey(appWidgetId, key), value);
        prefs.apply();
    }

    @SuppressWarnings("SameParameterValue")
    private static boolean getBoolean(Context context, int appWidgetId, String key, boolean defaultValue) {
        SharedPreferences prefs = getPreference(context);
        return prefs.getBoolean(getKey(appWidgetId, key), defaultValue);
    }

    private static void deletePreference(Context context, int appWidgetId, String key) {
        SharedPreferences.Editor prefs = getPreference(context).edit();
        prefs.remove(getKey(appWidgetId, key));
        prefs.apply();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitle(Context context, int appWidgetId, String text) {
        putString(context, appWidgetId, KEY_TITLE, text);
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitle(Context context, int appWidgetId) {
        return getString(context, appWidgetId, KEY_TITLE, context.getString(R.string.uptime));
    }

    static void deleteTitle(Context context, int appWidgetId) {
        deletePreference(context, appWidgetId, KEY_TITLE);
    }

    static boolean loadSmartTime(Context context, int appWidgetId) {
        return getBoolean(context, appWidgetId, KEY_SMART_TIMER, true);
    }

    static void saveSmartTime(Context context, int appWidgetId, boolean isChecked) {
        putBoolean(context, appWidgetId, KEY_SMART_TIMER, isChecked);
    }

    static void deleteSmartTime(Context context, int appWidgetId) {
        deletePreference(context, appWidgetId, KEY_SMART_TIMER);
    }

    static void deletePrefs(Context context, int appWidgetId) {
        deletePreference(context, appWidgetId, KEY_TITLE);
        deletePreference(context, appWidgetId, KEY_SMART_TIMER);
    }
}
