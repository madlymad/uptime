package com.madlymad.debug;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.util.Log;

import com.madlymad.uptime.R;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created on 19/3/2018.
 *
 * @author mando
 */

public class LtoF {

    private static final String LOGFILE = "log.html";

    /**
     * @param context  the context to use
     * @param logLevel <ul>
     *                 <li>{@link Log#INFO} - for information about service life-circle</li>
     *                 <li>{@link Log#DEBUG} - for printing all transactions,
     *                 publish, subscribe, unsubscribe, connect, disconnect</li>
     *                 <li>{@link Log#ERROR} - for any Exception that is occurred</li>
     *                 <li>{@link Log#ASSERT} - for logging events from other things
     *                 happening in the device and may be important for </li>
     *                 <li>{@link Log#VERBOSE} - for logging events such as alarm
     *                 time set, reset, ...</li>
     *                 <li>{@link Log#WARN} - logging events such as connectivity
     *                 changes</li>
     *                 </ul>
     * @param message  message that will printed on file
     */
    public static void logFile(Context context, int logLevel, String message) {
        if (DebugConf.DebugParts.isDebug) {
            Log.println(logLevel, "UPTIME", message);
        }
        if (DebugConf.getLogToFile(context)) {
            LogFile.commitToFile(context, LOGFILE, message, logLevel, true);
        }
    }

    public static void deleteLogFile(Context context) {
        LogFile.commitToFile(context, LOGFILE, "File DELETED", Log.ERROR, false);
    }

    @SuppressWarnings("unused")
    public static String getLogFile(Context context) {
        return LogFile.readFromFile(context, LOGFILE);
    }

    public static void mailLogFile(Context context) {
        LogFile.asyncDumpLogToSDAttachToMail(context, LOGFILE);
    }

    public static String logFileDetails(Context context) {
        File file = new File(Uri.parse(getLogFilePath(context)).getPath());
        return context.getString(R.string.file_size, getStringSizeLengthFile(file.length()));
    }

    @NonNull
    public static String getLogFilePath(Context context) {
        return context.getFilesDir().getAbsolutePath() + File.separator + LOGFILE;
    }

    private static String getStringSizeLengthFile(long size) {

        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMo = sizeKb * sizeKb;
        float sizeGo = sizeMo * sizeKb;
        float sizeTerra = sizeGo * sizeKb;


        if (size < sizeMo)
            return df.format(size / sizeKb) + " KB";
        else if (size < sizeGo)
            return df.format(size / sizeMo) + " MB";
        else if (size < sizeTerra)
            return df.format(size / sizeGo) + " GB";

        return "";
    }
}
