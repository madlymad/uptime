package com.madlymad.uptime;

import android.app.Application;

import com.madlymad.debug.DebugHelper;
import com.madlymad.integration.crashlytics.MadCrashlytics;

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
    }
}
