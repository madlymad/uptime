package com.madlymad.uptime;

import android.content.Context;
import android.os.SystemClock;

/**
 * Created on 18/3/2018.
 *
 * @author mando
 */

public class Prefs extends com.madlymad.Prefs {
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

    Prefs() {
        super();
    }

    public static void setTimestampFromMilliseconds(Context context, long milliseconds) {
        Prefs.setValue(context, Prefs.NOTIFY_TIMESTAMP,
                System.currentTimeMillis() + milliseconds - SystemClock.elapsedRealtime());
    }
}
