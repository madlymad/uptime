package com.madlymad.uptime.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.madlymad.debug.LtoF;
import com.madlymad.uptime.Prefs;
import com.madlymad.uptime.notifications.CreateNotification;
import com.madlymad.uptime.widget.Update;

/**
 * A receiver that is responsible for updating widget and setting the alarms.
 */
public class UpdateBroadcastReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = UpdateBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.isEmpty(intent.getAction()) ||
                !Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // This shouldn't triggered.
            if (intent.getAction() != null) {
                LtoF.logFile(context, Log.INFO,
                        "[" + LOG_TAG + "] ignored action: '" + intent.getAction() + "'");
            }
            return;
        }
        LtoF.logFile(context, Log.DEBUG, "[" + LOG_TAG + "] action: " + intent.getAction());

        // Set the alarm
        long daysInMilliseconds = com.madlymad.Prefs.getLongValue(context, Prefs.NOTIFY_MILLIS);
        if (daysInMilliseconds > 0) {
            CreateNotification.scheduleNotification(context, daysInMilliseconds);
        }

        Update.updateAllWidgets(context.getApplicationContext());
    }

}