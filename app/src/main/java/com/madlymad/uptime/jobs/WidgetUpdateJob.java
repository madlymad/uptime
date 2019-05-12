package com.madlymad.uptime.jobs;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.madlymad.debug.LtoF;
import com.madlymad.uptime.UpPrefsUtils;
import com.madlymad.uptime.widget.UpdateHelper;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created on 21/4/2018.
 *
 * @author mando
 */
public class WidgetUpdateJob extends Job {
    public static final String TAG = "widget_update_job";
    private static final String LOG_TAG = WidgetUpdateJob.class.getSimpleName();
    private static final int DURATION_FROM = 15;
    private static final int DURATION_TO = 10;

    public static void schedulePeriodic(Context context) {
        if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
            // job already scheduled, nothing to do
            LtoF.logFile(context, Log.VERBOSE, "[" + LOG_TAG + "] already scheduled");
            return;
        }

        LtoF.logFile(context, Log.DEBUG, "[" + LOG_TAG + "] schedulePeriodic");
        new JobRequest.Builder(WidgetUpdateJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(DURATION_FROM), TimeUnit.MINUTES.toMillis(DURATION_TO))
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }

    public static void cancelJobs(Context context) {
        Set<JobRequest> jobs = JobManager.instance().getAllJobRequestsForTag(TAG);
        LtoF.logFile(context, Log.DEBUG, "[" + LOG_TAG + "] cancelJobs " + jobs.size());
        for (JobRequest job : jobs) {
            JobManager.instance().cancel(job.getJobId());
        }
    }

    @Override
    @NonNull
    protected Result onRunJob(@NonNull Params params) {

        LtoF.logFile(getContext(), Log.DEBUG, "[" + LOG_TAG + "] onJobRun");
        UpdateHelper.updateAllWidgets(getContext());
        UpPrefsUtils.updateNotificationDate(getContext());

        return Result.SUCCESS;
    }
}
