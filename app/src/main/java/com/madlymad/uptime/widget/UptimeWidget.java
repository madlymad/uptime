package com.madlymad.uptime.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.madlymad.debug.DebugConf;
import com.madlymad.debug.LtoF;
import com.madlymad.debug.Print;
import com.madlymad.uptime.R;
import com.madlymad.uptime.TimeTextUtils;

import java.util.Arrays;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link UptimeWidgetConfigureActivity UptimeWidgetConfigureActivity}
 */
public class UptimeWidget extends AppWidgetProvider {

    private static final String LOG_TAG = UptimeWidget.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if (DebugConf.DebugParts.WIDGET) {
            LtoF.logFile(context, Log.VERBOSE, "[" + LOG_TAG + "] onUpdate ids " + Arrays.toString(appWidgetIds));
        }
        // There may be multiple widgets active, so update all of them
        Update.updateAppWidgets(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName(context, UptimeWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] remainingIds = new int[0];
        if (manager != null) {
            remainingIds = manager.getAppWidgetIds(thisWidget);
            if (DebugConf.DebugParts.WIDGET) {
                LtoF.logFile(context, Log.VERBOSE, "[" + LOG_TAG + "] onDeleted ids "
                        + Arrays.toString(appWidgetIds) + " remain " + Arrays.toString(remainingIds));
            }
        }
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            PrefsWidget.deleteTitle(context, appWidgetId);
            PrefsWidget.deleteSmartTime(context, appWidgetId);
        }

        if (remainingIds.length == 0) {
            Update.shutdownUpdating(context);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        if (DebugConf.DebugParts.WIDGET) {
            LtoF.logFile(context, Log.VERBOSE, "[" + LOG_TAG + "] onEnabled");
        }
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        if (DebugConf.DebugParts.WIDGET) {
            LtoF.logFile(context, Log.VERBOSE, "[" + LOG_TAG + "] onDisabled");
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (DebugConf.DebugParts.WIDGET) {
            LtoF.logFile(context, Log.INFO, "[" + LOG_TAG + "] onReceive " + Print.intentToString(intent));
        }
        final String action = intent.getAction();
        final Bundle extras = intent.getExtras();
        int[] appWidgetIds = null;
        if (extras != null) {
            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            if (appWidgetIds == null && appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                appWidgetIds = new int[]{appWidgetId};
            }
        }
        // Special handling in case onDelete is not working
        if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
            if (appWidgetIds != null && appWidgetIds.length > 0) {
                this.onDeleted(context, appWidgetIds);
            }
        } else {
            super.onReceive(context, intent);
            if (appWidgetIds != null && appWidgetIds.length > 0) {
                for (int id : appWidgetIds) {
                    handleReceivedEvents(context, intent, id);
                }
            }
        }
    }

    private void handleReceivedEvents(Context context, Intent intent, int appWidgetId) {
        if (DebugConf.DebugParts.WIDGET) {
            LtoF.logFile(context, Log.VERBOSE, "[" + LOG_TAG + "] handleReceivedEvents " + appWidgetId);
        }
        if (Update.UPDATE_NOW.equals(intent.getAction())) {
            Update.updateWidget(context, appWidgetId);

            Toast.makeText(context,
                    context.getString(R.string.device_running_for, TimeTextUtils.getUptimePrettyString(context)),
                    Toast.LENGTH_LONG).show();
        }
    }

}
