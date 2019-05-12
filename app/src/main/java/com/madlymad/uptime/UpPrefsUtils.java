package com.madlymad.uptime;

import android.content.Context;

import com.madlymad.uptime.constants.TimeTextUtils;
import com.madlymad.util.PrefsUtils;
import com.madlymad.uptime.notifications.CreateNotificationUtils;

import java.util.Date;

/**
 * Created on 18/3/2018.
 *
 * @author mando
 */

public class UpPrefsUtils extends PrefsUtils {
    /**
     * Calculated elapsed milliseconds for next notification.
     */
    public static final String NOTIFY_MILLIS = "NOTIFY_MILLIS";
    /**
     * Timestamp when apply is pressed, needed to calculate future/past date for
     * next restart notification
     */
    public static final String NOTIFY_TIMESTAMP = "NOTIFY_TIMESTAMP";
    /**
     * User's selection for measurement value, used to setup UI
     */
    public static final String NOTIFY_MEASURE = "NOTIFY_MEASURE";
    /**
     * User's typed value in the number field, used to setup UI
     */
    public static final String NOTIFY_NUMBER = "NOTIFY_NUMBER";

    UpPrefsUtils() {
        super();
    }

    public static void setTimestampFromMilliseconds(Context context, long milliseconds) {
        UpPrefsUtils.setValue(context, UpPrefsUtils.NOTIFY_TIMESTAMP,
                TimeTextUtils.futureDateForNowAfter(milliseconds));
    }

    public static void updateNotificationDate(Context context) {
        long timestamp = UpPrefsUtils.getLongValue(context, UpPrefsUtils.NOTIFY_TIMESTAMP);
        if (timestamp > 0) { // User has setup a notification
            long elapsed = UpPrefsUtils.getLongValue(context, UpPrefsUtils.NOTIFY_MILLIS);
            Date notify = new Date(timestamp);
            Date now = new Date();
            if (now.compareTo(notify) > 0) {
                // update timestamp
                setTimestampFromMilliseconds(context, elapsed);
            }
            CreateNotificationUtils.scheduleNotification(context, elapsed);
        }
    }
}
