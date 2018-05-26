package com.madlymad.uptime.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * https://android.jlelse.eu/easy-job-scheduling-with-android-job-4a2c020b9742
 * <p>
 * Created on 21/4/2018.
 *
 * @author mando
 */
public class UptimeJobCreator implements JobCreator {

    @Override
    @Nullable
    public Job create(@NonNull String tag) {
        switch (tag) {
            case WidgetUpdateJob.TAG:
                return new WidgetUpdateJob();

            case RestartReminderJob.TAG:
                return new RestartReminderJob();

            default:
                return null;
        }
    }

}
