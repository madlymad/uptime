package com.madlymad.uptime.models.data;

import android.content.Context;

import com.madlymad.uptime.UpPrefsUtils;
import com.madlymad.uptime.models.objects.Times;
import com.madlymad.uptime.notifications.CreateNotificationUtils;

public class NotificationLiveData extends PreferenceLiveData<Times> {

    public NotificationLiveData(Context context) {
        super(context);
    }

    @Override
    protected boolean containsPreferenceKey(String key) {
        switch (key) {
            case UpPrefsUtils.NOTIFY_TIMESTAMP:
            case UpPrefsUtils.NOTIFY_MILLIS:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void backgroundDataLoad() {
        Times data = new Times(getContext());
        if (data.getTimestamp() > 0) {
            CreateNotificationUtils.scheduleNotification(getContext(), data.getElapsedTime());
        }

        postValue(data);
    }
}
