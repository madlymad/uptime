package com.madlymad.uptime;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.madlymad.debug.DebugConf;
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
        if (DebugConf.DebugParts.isDebug) {
            DebugConf.setStrictMode();
        }

        MadCrashlytics.initOnPermission(this);

        JobManager.create(this).addJobCreator(new UptimeJobCreator());
    }
}
