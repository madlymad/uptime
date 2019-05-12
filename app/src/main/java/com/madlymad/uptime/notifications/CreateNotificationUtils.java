package com.madlymad.uptime.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import com.madlymad.uptime.MainActivity;
import com.madlymad.uptime.R;
import com.madlymad.uptime.UpPrefsUtils;
import com.madlymad.uptime.jobs.RestartReminderJob;

import androidx.core.app.NotificationCompat;

/**
 * Created on 22/4/2018.
 *
 * @author mando
 */
public final class CreateNotificationUtils {
    public static final int ID = 1000;
    private static final String NOTIFICATION_CHANNEL_ID = "default";
    //private static final String LOG_TAG = CreateNotificationUtils.class.getSimpleName();

    private CreateNotificationUtils() {
    }

    public static void removeScheduledNotification(Context context) {
        UpPrefsUtils.remove(context, UpPrefsUtils.NOTIFY_MILLIS);
        UpPrefsUtils.remove(context, UpPrefsUtils.NOTIFY_TIMESTAMP);
        UpPrefsUtils.remove(context, UpPrefsUtils.NOTIFY_MEASURE);
        UpPrefsUtils.remove(context, UpPrefsUtils.NOTIFY_NUMBER);

        removeNotification(context);
        RestartReminderJob.cancelJob(context);
    }

    private static void removeNotification(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static Notification getNotification(Context context) {
        String content = context.getString(R.string.restart_device);
        initChannels(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(context.getString(R.string.restart));
        builder.setContentText(content);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(content));
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentIntent(getPendingIntent(context));

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        return notification;
    }

    private static void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.uptime),
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(context.getString(R.string.restart));

            notificationManager.createNotificationChannel(channel);
        }
    }

    public static boolean isScheduled(Context context) {
        return RestartReminderJob.isScheduled(context);
    }

    public static void scheduleNotification(Context context, long elapsedTime) {
        long inMs = elapsedTime - SystemClock.elapsedRealtime();
        if (inMs <= 0) {
            inMs = 0;
        }
        RestartReminderJob.schedule(context, inMs);
    }
}
