package com.madlymad;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.Set;

/**
 * Parent class with shared methods for all Prefs extensions.
 * <p>
 * Created by mando on 25/12/2016.
 */

@SuppressWarnings({"SameParameterValue", "WeakerAccess", "unused"})
public class Prefs {

    protected Prefs() {
        // empty
    }

    @NonNull
    public static String getKey(Context context, @StringRes int id) {
        return context.getString(id);
    }

    public static void setValue(Context context, @StringRes int id, boolean value) {
        setValue(context, getKey(context, id), value);
    }

    public static void setValue(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static String getStringValue(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, null);
    }

    public static Set<String> getStringSetValue(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getStringSet(key, null);
    }

    public static Object getValue(Context context, String key, boolean isArray) {
        if (isArray) {
            return getStringSetValue(context, key);
        } else {
            return getStringValue(context, key);
        }
    }

    public static Object getValue(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getAll().get(key);
    }

    public static boolean getBooleanValue(Context context, @StringRes int id, boolean defaultValue) {
        return getBooleanValue(context, getKey(context, id), defaultValue);
    }

    public static boolean getBooleanValue(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void setValue(Context context, String key, long value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void setValue(Context context, String key, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void remove(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static long getLongValue(Context context, String key, long defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(key, defaultValue);
    }

    public static long getLongValue(Context context, String key) {
        return getLongValue(context, key, 0);
    }

    public static int getIntValue(Context context, String key, int defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, defaultValue);
    }

    public static int getIntValue(Context context, String key) {
        return getIntValue(context, key, 0);
    }
}
