package com.madlymad.uptime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.madlymad.debug.DebugHelper;
import com.madlymad.debug.LtoF;
import com.madlymad.uptime.BuildConfig;
import com.madlymad.uptime.MainActivity;
import com.madlymad.uptime.R;
import com.madlymad.uptime.constants.TimeTextUtils;
import com.madlymad.uptime.jobs.WidgetUpdateJob;

import java.util.Arrays;

/**
 * Created on 22/4/2018.
 *
 * @author mando
 */
public final class UpdateHelper {
    static final String UPDATE_NOW = BuildConfig.APPLICATION_ID + ".UPDATE_NOW";
    private static final String LOG_TAG = UpdateHelper.class.getSimpleName();

    private UpdateHelper() {
    }

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {
        if (appWidgetManager == null) {
            // no manager to do anything
            return;
        }
        CharSequence widgetText = PrefsWidgetUtils.loadTitle(context, appWidgetId);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.uptime_widget);
        String timer = TimeTextUtils.getUptimeString();
        views.setTextViewText(R.id.appwidget_timer, timer);
        views.setOnClickPendingIntent(R.id.appwidget_timer, getPendingSelfIntent(context, UPDATE_NOW, appWidgetId));
        if (DebugHelper.DebugUtils.WIDGET) {
            LtoF.logFile(context, Log.VERBOSE, "[" + LOG_TAG + "] updateAppWidget [" + appWidgetId + "] timer " + timer);
        }

        views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setOnClickPendingIntent(R.id.appwidget_layout, getPendingMainIntent(context));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int... appWidgetIds) {
        if (DebugHelper.DebugUtils.WIDGET && appWidgetIds.length > 1) {
            LtoF.logFile(context, Log.VERBOSE, "[" + LOG_TAG + "] updateAppWidgets ids " + Arrays.toString(appWidgetIds));
        }
        startupUpdating(context);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static PendingIntent getPendingSelfIntent(Context context, String action, int appWidgetId) {
        Intent intent = new Intent(context, UptimeWidget.class);
        intent.setAction(action);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        return PendingIntent.getBroadcast(context, appWidgetId, intent, 0);
    }

    private static PendingIntent getPendingMainIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static void startupUpdating(Context context) {
        synchronized (UpdateHelper.class) {
            WidgetUpdateJob.schedulePeriodic(context);
        }
    }

    static void shutdownUpdating(Context context) {
        synchronized (UpdateHelper.class) {
            WidgetUpdateJob.cancelJobs(context);
        }
    }

    public static void updateAllWidgets(Context context) {
        ComponentName thisWidget = new ComponentName(context, UptimeWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        if (manager != null) {
            int[] appWidgetIds = manager.getAppWidgetIds(thisWidget);
            if (appWidgetIds != null && appWidgetIds.length > 0) {
                updateAppWidgets(context, manager, appWidgetIds);
            }
        }
    }

    static void updateWidget(Context context, int appWidgetId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (appWidgetManager != null) {
            UpdateHelper.updateAppWidgets(context, appWidgetManager, appWidgetId);
        }
    }
}
