package com.madlymad.uptime;

import android.content.Context;
import android.os.SystemClock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created on 17/3/2018.
 *
 * @author mando
 */

public class TextFormatUtils {

    public static final String GITLAB_PAGES = "https://madlymad.gitlab.io/uptime/";
    public static final int HOURS = 0;
    public static final int DAYS = 1;
    public static final int WEEKS = 2;
    public static final int MONTHS = 3;

    public static String getUptimeString() {
        long milis = SystemClock.elapsedRealtime();
        return TextFormatUtils.convertToHoursMinutesSecondsString(milis);
    }

    private static String convertToHoursMinutesSecondsString(long elapsedTime) {
        String format = String.format(Locale.US, "%%0%dd", 2);
        elapsedTime = elapsedTime / 1000;
        //String seconds = String.format(format, elapsedTime % 60);
        String minutes = String.format(format, (elapsedTime % 3600) / 60);
        String hours = String.format(Locale.US, "%d", elapsedTime / 3600);
        return hours + ":" + minutes;
    }

    public static long convertToMillis(int measurement, long time) {
        switch (measurement) {
            case HOURS:
                return TimeUnit.HOURS.toMillis(time);
            case DAYS:
                return TimeUnit.DAYS.toMillis(time);
            case WEEKS:
                return TimeUnit.DAYS.toMillis(time) * 7;
            case MONTHS:
                return TimeUnit.DAYS.toMillis(time) * 30;
        }
        return 0;
    }

    public static String getUptimePrettyString(Context context) {
        long[] times = calculateTimes(SystemClock.elapsedRealtime());
        long days = times[0];
        long hours = times[1];
        long minutes = times[2];
        long seconds = times[3];

        StringBuilder builder = new StringBuilder();
        if (days > 0) {
            builder.append(context.getResources().getQuantityString(R.plurals.days, (int) days, days));
        }
        if (hours > 0) {
            builder.append(" ")
                    .append(context.getResources().getQuantityString(R.plurals.hours, (int) hours, hours));
        }
        if (minutes > 0) {
            builder.append(" ")
                    .append(context.getResources().getQuantityString(R.plurals.minutes, (int) minutes, minutes));
        }
        if (seconds > 0) {
            builder.append(" ")
                    .append(context.getResources().getQuantityString(R.plurals.seconds, (int) seconds, seconds));
        }

        return builder.toString().trim();
    }

    public static long[] calculateTimes(long millis) {
        long[] times = new long[4];
        times[0] = TimeUnit.MILLISECONDS.toDays(millis);
        times[1] = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(times[0]);
        times[2] = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(times[1]) - TimeUnit.DAYS.toMinutes(times[0]);
        times[3] = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(times[2]) - TimeUnit.HOURS.toSeconds(times[1]) - TimeUnit.DAYS.toSeconds(times[0]);
        return times;
    }

    public static String getDateString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.US);
        return dateFormat.format(date);
    }

}
