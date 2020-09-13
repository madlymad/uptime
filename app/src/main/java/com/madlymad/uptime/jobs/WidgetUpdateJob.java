package com.madlymad.uptime.jobs;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.Operation;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.madlymad.debug.LtoF;
import com.madlymad.uptime.UpPrefsUtils;
import com.madlymad.uptime.widget.UpdateHelper;

import java.util.concurrent.TimeUnit;

/**
 * Created on 21/4/2018.
 *
 * @author mando
 */
public class WidgetUpdateJob extends Worker {
    public static final String TAG = "widget_update_job";
    private static final String LOG_TAG = WidgetUpdateJob.class.getSimpleName();
    private static final int DURATION_FROM = 15;
    private static final int DURATION_TO = 10;

    public WidgetUpdateJob(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public static void schedulePeriodic(Context context) {
        LtoF.logFile(context, Log.DEBUG, "[" + LOG_TAG + "] schedulePeriodic");
        WorkManager.getInstance().enqueueUniquePeriodicWork(TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                WidgetUpdateJob.buildRequest());
    }

    @NonNull
    private static PeriodicWorkRequest buildRequest() {
        return new PeriodicWorkRequest.Builder(
                WidgetUpdateJob.class,
                DURATION_FROM, TimeUnit.MINUTES,
                DURATION_TO, TimeUnit.MINUTES)
                .addTag(TAG)
                .build();
    }

    public static void cancelJobs(Context context) {
        Operation.State operation = WorkManager.getInstance().cancelAllWorkByTag(TAG)
                .getState().getValue();
        LtoF.logFile(context, Log.DEBUG, "[" + LOG_TAG + "] cancelJobs " + operation);
    }

    @NonNull
    @Override
    public Result doWork() {
        LtoF.logFile(getApplicationContext(), Log.DEBUG, "[" + LOG_TAG + "] onJobRun");
        UpdateHelper.updateAllWidgets(getApplicationContext());
        UpPrefsUtils.updateNotificationDate(getApplicationContext());
        return Result.success();
    }
}
