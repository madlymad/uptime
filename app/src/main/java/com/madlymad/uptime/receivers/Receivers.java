package com.madlymad.uptime.receivers;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import com.madlymad.uptime.notifications.CreateNotificationUtils;
import com.madlymad.uptime.widget.UptimeWidget;

/**
 * Created on 22/4/2018.
 *
 * @author mando
 */
public final class Receivers {

    private Receivers() {
    }

    private static void changeComponentStateBootReceiver(Context context, boolean enable) {
        changeComponentStateReceiver(context, enable, UpdateBroadcastReceiver.class);
    }

    private static <T> void changeComponentStateReceiver(Context context, boolean enable, @SuppressWarnings("SameParameterValue") Class<T> cls) {
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static void checkReceivers(Context context) {
        changeComponentStateBootReceiver(context, CreateNotificationUtils.isScheduled(context)
                || widgetsActive(context) > 0);
    }

    private static int widgetsActive(Context context) {
        AppWidgetManager wm = AppWidgetManager.getInstance(context);
        if (wm != null) {
            return wm.getAppWidgetIds(new ComponentName(context, UptimeWidget.class)).length;
        }
        return 0;
    }
}
