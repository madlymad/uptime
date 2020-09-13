package com.madlymad.uptime.jobs;

import android.app.Notification;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.madlymad.debug.LtoF;
import com.madlymad.uptime.notifications.CreateNotificationUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created on 21/4/2018.
 *
 * @author mando
 */
public class RestartReminderJob extends Worker {

    public static final String TAG = "restart_reminder";
    private static final String LOG_TAG = "RestartJob";

    public RestartReminderJob(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public static void schedule(Context context, long inMs) {
        CreateNotificationUtils.removeNotification(context);

        LtoF.logFile(context, Log.DEBUG, "[" + LOG_TAG + "] scheduleIn " + inMs);
        // handle the restart settings here... one time notification that is rescheduled after boot is ok
        WorkManager.getInstance().enqueueUniqueWork(TAG,
                ExistingWorkPolicy.REPLACE,
                RestartReminderJob.buildRequest(inMs));
    }

    private static OneTimeWorkRequest buildRequest(long inMs) {
        Data data = new Data.Builder().putLong("inMs", inMs).build();
        OneTimeWorkRequest.Builder builder =
                new OneTimeWorkRequest.Builder(RestartReminderJob.class)
                        .setInputData(data)
                        .addTag(TAG);
        if (inMs > 0) {
            builder.setInitialDelay(inMs, TimeUnit.MILLISECONDS);
        }
        return builder.build();
    }

    public static void cancelJob(Context context) {
        WorkManager.getInstance().cancelAllWorkByTag(RestartReminderJob.TAG);
        LtoF.logFile(context, Log.DEBUG, "[" + LOG_TAG + "] cancelJob");
    }

    @NonNull
    @Override
    public Result doWork() {
        Notification notification = CreateNotificationUtils.getNotification(getApplicationContext());

        NotificationManagerCompat.from(getApplicationContext())
                .notify(CreateNotificationUtils.ID, notification);
        return Result.success();
    }

}
