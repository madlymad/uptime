package com.madlymad.uptime.models.objects;

import android.content.Context;

import com.madlymad.uptime.UpPrefsUtils;

public class Times {

    private final long timestamp;
    private final long elapsedTime;

    public Times(Context context) {
        timestamp = UpPrefsUtils.getLongValue(context, UpPrefsUtils.NOTIFY_TIMESTAMP);
        elapsedTime = UpPrefsUtils.getLongValue(context, UpPrefsUtils.NOTIFY_MILLIS);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

}
