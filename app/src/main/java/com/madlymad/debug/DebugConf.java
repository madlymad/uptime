package com.madlymad.debug;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.madlymad.Prefs;
import com.madlymad.uptime.BuildConfig;
import com.madlymad.uptime.R;

final public class DebugConf {

    static final String RECIPIENT_EMAIL = "mando7stam@gmail.com";
    private final static String TAG = DebugConf.class.getSimpleName();

    private static Boolean LOG_TO_FILE = null;

    public static void setLogToFile(boolean value) {
        LOG_TO_FILE = value;
    }

    public static boolean getLogToFile(Context context) {
        if (LOG_TO_FILE == null) {
            LOG_TO_FILE = Prefs.getBooleanValue(context, R.string.key_logs_enable, false);
        }
        return LOG_TO_FILE;
    }

    @SuppressLint("NewApi")
    public static void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                //.detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                // or for all detectable problems
                //.detectAll()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects().penaltyLog()
                .penaltyDeath().build());
        Log.d(TAG, "StrictMode enabled");
    }

    /**
     * @return <code>true</code> when device is plunged into PC
     */
    @SuppressWarnings("unused")
    public static boolean isDeviceUSBPlunged(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent == null ? -1 : intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return // plugged == BatteryManager.BATTERY_PLUGGED_AC
                // ||
                plugged == BatteryManager.BATTERY_PLUGGED_USB;
    }

    @SuppressWarnings("unused")
    public static void sendHtmlEmail(Context context, String title, String text) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/html");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{RECIPIENT_EMAIL});
        i.putExtra(Intent.EXTRA_SUBJECT, title);
        i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(text));
        try {
            context.startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public interface DebugParts {

        boolean isDebug = BuildConfig.DEBUG;

        boolean WIDGET = true;
        boolean NOTIFICATION = true;
    }

}
