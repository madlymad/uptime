package com.madlymad.uptime.constants;

import android.content.Context;
import android.os.SystemClock;

import com.madlymad.uptime.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created on 17/3/2018.
 *
 * @author mando
 */

public final class TimeTextUtils {

    private static final String GITLAB_PAGES = "https://madlymad.gitlab.io/uptime/";
    public static final String HTML_PRIVACY = TimeTextUtils.GITLAB_PAGES + "policies/privacy_policy.html";
    public static final String HTML_TERMS = TimeTextUtils.GITLAB_PAGES + "policies/terms_and_conditions.html";
    private static final int MILLIS = 1000;
    private static final int SEC_IN_H = 3600;
    private static final int MIN_IN_H = 60;
    private static final int DAYS = 30;
    private static final int DIGITS = 2;

    private static final int TIMES = 4;
    private static final int POS_H = 1;
    private static final int POS_D = 0;
    private static final int POS_MIN = 2;
    private static final int POS_SEC = 3;

    private TimeTextUtils() {
        //not called
    }

    public static String getUptimeString() {
        long millis = SystemClock.elapsedRealtime();
        return TimeTextUtils.convertToHoursMinutesSecondsString(millis);
    }

    private static String convertToHoursMinutesSecondsString(long elapsedTime) {
        String format = String.format(Locale.US, "%%0%dd", DIGITS);
        long elapsedTimeSec = elapsedTime / MILLIS;
        //String seconds = String.format(format, elapsedTimeSec % 60);
        String minutes = String.format(format, (elapsedTimeSec % SEC_IN_H) / MIN_IN_H);
        String hours = String.format(Locale.US, "%d", elapsedTimeSec / SEC_IN_H);
        return hours + ":" + minutes;
    }

    public static long convertToMillis(@MeasureUtils.TimeMeasurement int measurement, long time) {
        switch (measurement) {
            case MeasureUtils.MEASURE_IN_HOURS:
                return TimeUnit.HOURS.toMillis(time);
            case MeasureUtils.MEASURE_IN_DAYS:
                return TimeUnit.DAYS.toMillis(time);
            case MeasureUtils.MEASURE_IN_MONTHS:
                return TimeUnit.DAYS.toMillis(time) * DAYS;
            default:
                return POS_D;
        }
    }

    public static String getUptimePrettyString(Context context) {
        long[] times = calculateTimes(SystemClock.elapsedRealtime());
        long days = times[POS_D];
        long hours = times[POS_H];
        long minutes = times[POS_MIN];
        long seconds = times[POS_SEC];

        StringBuilder builder = new StringBuilder();
        if (days > 0) {
            builder.append(context.getResources().getQuantityString(R.plurals.days, (int) days, days));
        }
        if (hours > 0) {
            builder.append(' ')
                    .append(context.getResources().getQuantityString(R.plurals.hours, (int) hours, hours));
        }
        if (minutes > 0) {
            builder.append(' ')
                    .append(context.getResources().getQuantityString(R.plurals.minutes, (int) minutes, minutes));
        }
        if (seconds > 0) {
            builder.append(' ')
                    .append(context.getResources().getQuantityString(R.plurals.seconds, (int) seconds, seconds));
        }

        return builder.toString().trim();
    }

    public static String getUptimePrettyStringShort() {
        long[] times = calculateTimes(SystemClock.elapsedRealtime());
        long days = times[POS_D];
        long hours = times[POS_H];
        long minutes = times[POS_MIN];
        long seconds = times[POS_SEC];

        StringBuilder builder = new StringBuilder();
        if (days > 0) {
            builder.append(days).append("d");
        }
        if (hours > 0) {
            builder.append(' ').append(hours).append("h");
        }
        if (minutes > 0) {
            builder.append(' ').append(minutes).append("min");
        }
        if (seconds > 0) {
            builder.append(' ').append(seconds).append("sec");
        }

        return builder.toString().trim();
    }

    static long[] calculateTimes(long millis) {
        long[] times = new long[TIMES];
        times[POS_D] = TimeUnit.MILLISECONDS.toDays(millis);
        times[POS_H] = TimeUnit.MILLISECONDS.toHours(millis)
                - TimeUnit.DAYS.toHours(times[POS_D]);

        times[POS_MIN] = TimeUnit.MILLISECONDS.toMinutes(millis)
                - TimeUnit.HOURS.toMinutes(times[POS_H])
                - TimeUnit.DAYS.toMinutes(times[POS_D]);

        times[POS_SEC] = TimeUnit.MILLISECONDS.toSeconds(millis)
                - TimeUnit.MINUTES.toSeconds(times[POS_MIN])
                - TimeUnit.HOURS.toSeconds(times[POS_H])
                - TimeUnit.DAYS.toSeconds(times[POS_D]);
        return times;
    }

    public static String getDateString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy\nHH:mm", Locale.US);
        return dateFormat.format(date);
    }

    public static long futureDateForNowAfter(long milliseconds) {
        return System.currentTimeMillis() + milliseconds - SystemClock.elapsedRealtime();
    }
}
