package com.madlymad.uptime.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.madlymad.debug.LtoF;
import com.madlymad.uptime.UpPrefsUtils;
import com.madlymad.uptime.notifications.CreateNotificationUtils;
import com.madlymad.uptime.widget.UpdateHelper;

/**
 * A receiver that is responsible for updating widget and setting the alarms.
 */
public class UpdateBroadcastReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = UpdateBroadcastReceiver.class.getSimpleName();
    private static final String ACTION_QUICKBOOT_POWERON = "android.intent.action.QUICKBOOT_POWERON";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.isEmpty(intent.getAction())
                || !Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())
                || !ACTION_QUICKBOOT_POWERON.equals(intent.getAction())) {
            // This shouldn't triggered.
            if (intent.getAction() != null) {
                LtoF.logFile(context, Log.INFO,
                        "[" + LOG_TAG + "] ignored action: '" + intent.getAction() + "'");
            }
            return;
        }
        LtoF.logFile(context, Log.DEBUG, "[" + LOG_TAG + "] action: " + intent.getAction());

        // Set the alarm
        long daysInMilliseconds = UpPrefsUtils.getLongValue(context, UpPrefsUtils.NOTIFY_MILLIS);
        if (daysInMilliseconds > 0) {
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())
                    || ACTION_QUICKBOOT_POWERON.equals(intent.getAction())) {
                // We just rebooted so calculate next restart timestamp!
                UpPrefsUtils.setTimestampFromMilliseconds(context, daysInMilliseconds);
            }

            CreateNotificationUtils.scheduleNotification(context, daysInMilliseconds);
        }

        UpdateHelper.updateAllWidgets(context.getApplicationContext());
    }
}
