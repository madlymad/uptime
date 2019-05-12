package com.madlymad.integration.crashlytics;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.madlymad.util.PrefsUtils;
import com.madlymad.ui.base.BaseActivity;
import com.madlymad.uptime.BuildConfig;
import com.madlymad.uptime.R;

import androidx.fragment.app.FragmentManager;
import io.fabric.sdk.android.Fabric;

/**
 * Created on 22/3/2018.
 *
 * @author mando
 */

public final class MadCrashlytics {
    /**
     * "debug" or "release" application
     */
    private static final String BUILD_TYPE = "BUILD_TYPE";

    private MadCrashlytics() {
    }

    /**
     * Ask for permission and initialize the crash reporting
     *
     * @param context The context to use
     */
    public static void initAskPermission(BaseActivity context) {
        boolean userOptInFlag = checkOptInValue(context);
        if (!userOptInFlag) {
            askPermission(context);
        }
    }

    /**
     * In case we have the permission initialize the crash reporting
     *
     * @param context The context to use
     */
    public static void initOnPermission(Context context) {
        boolean userOptInFlag = checkOptInValue(context);
        if (userOptInFlag) {
            // Only initialize Fabric is user opt-in is true
            start(context);
        }
    }

    private static void askPermission(final BaseActivity context) {
        FragmentManager fm = context.getSupportFragmentManager();

        CrashlyticsDialogFragment crashlyticsDialogFragment =
                (CrashlyticsDialogFragment) fm.findFragmentByTag(CrashlyticsDialogFragment.TAG);
        if (crashlyticsDialogFragment == null) {
            crashlyticsDialogFragment = CrashlyticsDialogFragment.newInstance(
                    accepted -> {
                        PrefsUtils.setValue(context, R.string.key_crashlytics_on, accepted);
                        if (accepted) {
                            start(context);
                        }
                    });
            crashlyticsDialogFragment.show(fm, CrashlyticsDialogFragment.TAG);
        }
    }

    private static boolean checkOptInValue(Context context) {
        return PrefsUtils.getBooleanValue(context, R.string.key_crashlytics_on, false);
    }

    private static void start(Context context) {
        Fabric.with(context, new Crashlytics());
        initKeys();
    }

    private static void initKeys() {
        Crashlytics.setString(MadCrashlytics.BUILD_TYPE, BuildConfig.BUILD_TYPE);
    }
}
