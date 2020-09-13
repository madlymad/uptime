package com.madlymad.uptime.models.objects;

import android.content.Context;

import androidx.annotation.NonNull;

import com.madlymad.uptime.UpPrefsUtils;

public class Times {

    private final long timestamp;
    private final long elapsedTime;

    public Times(Context context) {
        timestamp = UpPrefsUtils.getLongValue(context, UpPrefsUtils.NOTIFY_TIMESTAMP);
        elapsedTime = UpPrefsUtils.getLongValue(context, UpPrefsUtils.NOTIFY_MILLIS);
    }

    public static boolean scheduled(Context context) {
        return (new Times(context)).nonZero();
    }

    private boolean nonZero() {
        return timestamp != 0 && elapsedTime != 0;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    @NonNull
    @Override
    public String toString() {
        return "Times{" +
                "timestamp=" + timestamp +
                ", elapsedTime=" + elapsedTime +
                '}';
    }
}
