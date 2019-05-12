package com.madlymad.uptime;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.madlymad.debug.DebugHelper;
import com.madlymad.integration.crashlytics.MadCrashlytics;
import com.madlymad.uptime.jobs.UptimeJobCreator;

/**
 * Created on 22/3/2018.
 *
 * @author mando
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (DebugHelper.DebugUtils.IS_DEBUG) {
            DebugHelper.setStrictMode();
        }

        MadCrashlytics.initOnPermission(this);

        JobManager.create(this).addJobCreator(new UptimeJobCreator());
    }
}
