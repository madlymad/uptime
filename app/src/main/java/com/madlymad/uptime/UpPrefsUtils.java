package com.madlymad.uptime;

import android.content.Context;

import com.madlymad.uptime.constants.TimeTextUtils;
import com.madlymad.uptime.models.objects.TimePickerValue;
import com.madlymad.uptime.notifications.CreateNotificationUtils;
import com.madlymad.util.PrefsUtils;

import java.util.Date;

import static com.madlymad.uptime.constants.MeasureUtils.DEFAULT_RESTART_MEASURE;
import static com.madlymad.uptime.constants.MeasureUtils.DEFAULT_RESTART_VALUE;

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

    public static TimePickerValue getTimePickerValue(Context context) {
        TimePickerValue value = new TimePickerValue();
        value.setMeasurement(UpPrefsUtils.getIntValue(context, UpPrefsUtils.NOTIFY_MEASURE, DEFAULT_RESTART_MEASURE));
        value.setNumericValue(UpPrefsUtils.getIntValue(context, UpPrefsUtils.NOTIFY_NUMBER, DEFAULT_RESTART_VALUE));
        return value;
    }

    public static void setTimePickerValue(Context context, TimePickerValue value) {
        UpPrefsUtils.getEditor(context)
                .putInt(UpPrefsUtils.NOTIFY_MEASURE, value.getMeasurement())
                .putInt(UpPrefsUtils.NOTIFY_NUMBER, value.getNumericValue())
                .apply();
    }
}
