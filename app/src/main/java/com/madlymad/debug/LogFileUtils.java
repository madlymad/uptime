package com.madlymad.debug;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.madlymad.uptime.BuildConfig;
import com.madlymad.util.DeviceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.collection.SparseArrayCompat;
import androidx.core.content.FileProvider;

// Requires API Level 26
// https://pmd.github.io/pmd-6.13.0/pmd_rules_java_performance.html#avoidfilestream
@SuppressWarnings({"PMD.AvoidFileStream"})
final class LogFileUtils {
    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss.SSS";
    private static final String TAG = "LogFileUtils";
    static final int KB = 1024;
    private static final String DEFAULT_COLOR = "#808080"; // grey

    private static final SparseArrayCompat<String> COLORS = new SparseArrayCompat<>();

    static {
        COLORS.append(Log.ERROR, "#990000"); // red
        COLORS.append(Log.WARN, "#FF6600"); // orange
        COLORS.append(Log.DEBUG, "#3300CC");  // blue
        COLORS.append(Log.VERBOSE, "#000000"); // black
        COLORS.append(Log.INFO, "#336600"); // green
        COLORS.append(Log.ASSERT, "#990099");  // magenta
    }

    private LogFileUtils() {
    }

    /**
     * Timestamp appended automatically to log file
     *
     * @param context  The context to use
     * @param filename the name of the file that will used for logging
     * @param text     the text that will printed.
     * @param logLevel used for added color to logs
     * @param append   <code>true</code> to continue writing existing file
     */
    @SuppressWarnings("SameParameterValue")
    static void commitToFile(Context context, String filename, String text, int logLevel, boolean append) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        String timestamp = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        String color = getLogColorString(logLevel);
        String entryString = "<b>" + timestamp + ":</b> " + "<font color=\"" + color + "\">" + text + "</font><br/>";

        try (FileOutputStream fOut = context.openFileOutput(filename, append ? Context.MODE_APPEND : Context.MODE_PRIVATE);
             OutputStreamWriter osw = new OutputStreamWriter(fOut, StandardCharsets.UTF_8)) {
            osw.write(entryString);
        } catch (IOException e) {
            if (DebugHelper.DebugUtils.IS_DEBUG) {
                Log.d(TAG, e.getMessage());
            }
        }
    }

    /**
     * May block UI if not in async
     */
    @SuppressWarnings("SameParameterValue")
    static String readFromFile(Context context, String filename) {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileInputStream fIn = context.openFileInput(filename);
             InputStreamReader isr = new InputStreamReader(fIn, StandardCharsets.UTF_8);
             BufferedReader bufferReader = new BufferedReader(isr)
        ) {
            String readString;
            do {
                readString = bufferReader.readLine();
                if (readString != null) {
                    stringBuilder.append(readString).append('\n');
                }
            } while (readString == null);
        } catch (FileNotFoundException e) {
            if (DebugHelper.DebugUtils.IS_DEBUG) {
                Log.e(TAG, "File not found: ", e);
            }
        } catch (IOException e) {
            if (DebugHelper.DebugUtils.IS_DEBUG) {
                Log.e(TAG, "Can not read file: ", e);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * <ul>
     * <li>Green: #336600</li>
     * <li>Orange: #FF6600</li>
     * <li>Blue: #3300CC</li>
     * <li>Red: #990000</li>
     * <li>Grey: #808080</li>
     * <li>Magenta: #990099</li>
     * </ul>
     *
     * @param logLevel gets static values from {@link Log}
     */
    private static String getLogColorString(int logLevel) {
        return COLORS.get(logLevel, DEFAULT_COLOR);
    }

    /**
     * @param logFilename if it is an external path just attached to email
     */
    @SuppressWarnings("SameParameterValue")
    @SuppressLint("StaticFieldLeak")
    static void asyncDumpLogToSDAttachToMail(final Context context, final String logFilename) {

        new AsyncTask<Void, Void, Boolean>() {

            private String path;
            private File outputFile;

            @Override
            protected Boolean doInBackground(Void... params) {
                if (logFilename.startsWith(Environment.getExternalStorageDirectory().getPath())) {
                    path = logFilename;
                    return true;
                }
                File dataFile = context.getFileStreamPath(logFilename);
                // Environment.getExternalStorageDirectory().getPath():
                // /sdcard/
                path = Environment.getExternalStorageDirectory().getPath() + "/debug_" + logFilename;
                outputFile = new File(path);
                if (outputFile.exists()) {
                    Log.e(TAG, "Overriding file '" + path + "'");
                }
                try (FileInputStream fis = new FileInputStream(dataFile);
                     FileOutputStream fos = new FileOutputStream(path)) {

                    byte[] buffer = new byte[KB];
                    int read;
                    do {
                        read = fis.read(buffer);
                        if (read != -1) {
                            fos.write(buffer, 0, read);
                        }
                    } while (read == -1);
                    fos.flush();
                    return true;
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "asyncDumpLogToSDAttachToMail file issue", e);
                } catch (IOException e) {
                    Log.e(TAG, "asyncDumpLogToSDAttachToMail io issue", e);
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    Toast.makeText(context, "Log dumped to SD", Toast.LENGTH_SHORT).show();

                    if (outputFile == null) {
                        // should never occur but just in case...
                        Toast.makeText(context, "Cannot find Log path", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "File name : '" + outputFile.getAbsolutePath() + "'");
                        String str = "ApplicationId: " + BuildConfig.APPLICATION_ID
                                + "\n"
                                + "AppVersion: " + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")"
                                + "\n"
                                + "Device Info: " + DeviceUtils.getManufacturer()
                                + " (" + DeviceUtils.getModel() + ")\n";

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setType("text/html");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{
                                DebugHelper.RECIPIENT_EMAIL
                        });
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Debug Log: " + BuildConfig.APPLICATION_ID);
                        intent.putExtra(Intent.EXTRA_TEXT, str);
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", outputFile);
                        intent.putExtra(Intent.EXTRA_STREAM, contentUri);
                        context.startActivity(Intent.createChooser(intent, "Send mail via "));


                        if (!BuildConfig.DEBUG) {
                            File dataFile = context.getFileStreamPath(logFilename);
                            Toast.makeText(context, "Log file " + (dataFile.delete() ? "deleted." : "cannot deleted. Please clear data instead."),
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                } else {
                    Toast.makeText(context, "Dumping failed. Check Log", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
