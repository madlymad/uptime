package com.madlymad.uptime.jobs;

import android.app.Notification;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.madlymad.debug.LtoF;
import com.madlymad.uptime.notifications.CreateNotification;

import java.util.Set;

/**
 * Created on 21/4/2018.
 *
 * @author mando
 */
public class RestartReminderJob extends Job {

    public static final String TAG = "restart_reminder";
    private static final String LOG_TAG = "RestartJob";

    public static void schedule(Context context, long inMs) {
        if (isScheduled(context, inMs)) {
            return;
        }

        LtoF.logFile(context, Log.DEBUG, "[" + LOG_TAG + "] scheduleIn " + inMs);

        // handle the restart settings here... one time notification that is rescheduled after boot is ok
        /*int jobId = */
        JobRequest.Builder builder = new JobRequest.Builder(RestartReminderJob.TAG)
                .setUpdateCurrent(true);
        if (inMs == 0) {
            builder = builder.startNow();
        } else {
            builder = builder.setExact(inMs);
        }
        builder.build().schedule();
    }

    public static boolean isScheduled(Context context) {
        if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {

            // job already scheduled, nothing to do
            LtoF.logFile(context, Log.VERBOSE, "[" + LOG_TAG + "] already scheduled");
            return true;
        }
        return false;
    }

    private static boolean isScheduled(Context context, long inMs) {
        Set<JobRequest> jobRequests = JobManager.instance().getAllJobRequestsForTag(TAG);
        if (!jobRequests.isEmpty()) {
            for (JobRequest request : jobRequests) {
                if (request.getIntervalMs() != inMs) {
                    LtoF.logFile(context, Log.VERBOSE,
                            "[" + LOG_TAG + "] current scheduled [" + request.getIntervalMs() + "] is outdated");
                    return false;
                }
            }

            // job already scheduled, nothing to do
            LtoF.logFile(context, Log.VERBOSE, "[" + LOG_TAG + "] already scheduled");
            return true;
        }
        return false;
    }

    public static void cancelJob(Context context) {
        Set<JobRequest> jobs = JobManager.instance().getAllJobRequestsForTag(TAG);
        LtoF.logFile(context, Log.DEBUG, "[" + LOG_TAG + "] cancelJobs " + jobs.size());
        for (JobRequest job : jobs) {
            JobManager.instance().cancel(job.getJobId());
        }
    }

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {

        Notification notification = CreateNotification.getNotification(getContext());

        NotificationManagerCompat.from(getContext())
                .notify(CreateNotification.ID, notification);


        return Result.SUCCESS;
    }

}
