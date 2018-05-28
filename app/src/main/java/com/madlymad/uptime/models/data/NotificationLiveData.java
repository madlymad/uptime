package com.madlymad.uptime.models.data;

import android.content.Context;

import com.madlymad.uptime.Prefs;
import com.madlymad.uptime.models.objects.Times;
import com.madlymad.uptime.notifications.CreateNotification;

public class NotificationLiveData extends PreferenceLiveData<Times> {

    public NotificationLiveData(Context context) {
        super(context);
    }

    @Override
    protected boolean containsPreferenceKey(String key) {
        switch (key) {
            case Prefs.NOTIFY_TIMESTAMP:
            case Prefs.NOTIFY_MILLIS:
                return true;
        }
        return false;
    }

    @Override
    public void backgroundDataLoad() {
        Times data = new Times();
        data.timestamp = Prefs.getLongValue(context, Prefs.NOTIFY_TIMESTAMP);
        data.elapsedTime = Prefs.getLongValue(context, Prefs.NOTIFY_MILLIS);

        if (data.timestamp > 0) {
            CreateNotification.scheduleNotification(context, data.elapsedTime);
        }

        postValue(data);
    }
}