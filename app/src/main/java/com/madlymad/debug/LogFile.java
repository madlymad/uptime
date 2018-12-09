package com.madlymad.debug;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import androidx.core.content.FileProvider;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class LogFile {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS", Locale.US);
    private static final String TAG = "LogFile";

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
        String timestamp = dateFormat.format(new Date(System.currentTimeMillis()));
        String color = getLogColorString(logLevel);
        String entryString = "<b>" + timestamp + ":</b> " + "<font color=\"" + color + "\">" + text + "</font><br/>";

        OutputStreamWriter osw = null;
        try {
            FileOutputStream fOut = context.openFileOutput(filename, (append) ? Context.MODE_APPEND : Context.MODE_PRIVATE);
            osw = new OutputStreamWriter(fOut, "UTF-8");

            osw.write(entryString);

        } catch (IOException e) {
            if (DebugConf.DebugParts.isDebug) {
                Log.d(TAG, e.getMessage());
            }
        } finally {
            if (osw != null) {
                try {
                    osw.flush();
                    osw.close();
                } catch (Exception e) {
                    if (DebugConf.DebugParts.isDebug) {
                        Log.d(TAG, "Couldn't close stream");
                    }
                }
            }
        }
    }

    /**
     * May block UI if not in async
     */
    @SuppressWarnings("SameParameterValue")
    static String readFromFile(Context context, String filename) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fIn = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fIn, "UTF-8");

            BufferedReader bufferReader = new BufferedReader(isr);

            String readString;
            while ((readString = bufferReader.readLine()) != null) {
                stringBuilder.append(readString).append("\n");
            }

            isr.close();

        } catch (FileNotFoundException e) {
            if (DebugConf.DebugParts.isDebug) {
                Log.e(TAG, "File not found: " + e.getMessage());
            }
        } catch (IOException e) {
            if (DebugConf.DebugParts.isDebug) {
                Log.e(TAG, "Can not read file: " + e.getMessage());
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
        // grey
        String color = "#808080";
        switch (logLevel) {
            case Log.ERROR:
                // red
                color = "#990000";
                break;
            case Log.WARN:
                // orange
                color = "#FF6600";
                break;
            case Log.DEBUG:
                // blue
                color = "#3300CC";
                break;
            case Log.VERBOSE:
                // black
                color = "#000000";
                break;
            case Log.INFO:
                // green
                color = "#336600";
                break;
            case Log.ASSERT:
                // magenta
                color = "#990099";
                break;
        }
        return color;
    }

    /**
     * @param logFilename if it is an external path just attached to email
     */
    @SuppressWarnings("SameParameterValue")
    @SuppressLint("StaticFieldLeak")
    static void asyncDumpLogToSDAttachToMail(final Context context, final String logFilename) {

        new AsyncTask<Void, Void, Boolean>() {

            String path;
            File outputFile;

            @Override
            protected Boolean doInBackground(Void... params) {
                if (logFilename.startsWith(Environment.getExternalStorageDirectory().getPath())) {
                    path = logFilename;
                    return true;
                }
                File dataFile = context.getFileStreamPath(logFilename);

                FileInputStream fis = null;
                FileOutputStream fos = null;
                path = null;

                try {
                    fis = new FileInputStream(dataFile);
                    // Environment.getExternalStorageDirectory().getPath():
                    // /sdcard/
                    path = Environment.getExternalStorageDirectory().getPath() + "/debug_" + logFilename;
                    outputFile = new File(path);
                    if (outputFile.exists()) {
                        Log.e(TAG, "Overriding file '" + path + "'");
                    }

                    fos = new FileOutputStream(path);

                    byte[] buffer = new byte[1024];

                    int read;

                    while ((read = fis.read(buffer)) != -1) {
                        fos.write(buffer, 0, read);
                    }

                    fos.flush();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            Log.e(TAG, "failed to close input stream", e);
                        }
                    }

                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            Log.e(TAG, "failed to close output stream", e);
                        }
                    }
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    Toast.makeText(context, "Log dumped to SD", Toast.LENGTH_SHORT).show();

                    if (outputFile != null) {
                        Log.d(TAG, "File name : '" + outputFile.getAbsolutePath() + "'");
                        String str = "" + "ApplicationId: " + BuildConfig.APPLICATION_ID +
                                "\n" +
                                "AppVersion: " + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")" +
                                "\n" +
                                "Device Info: " + DeviceUtils.getManufacturer() + " (" + DeviceUtils.getModel() + ")" +
                                "\n";

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setType("text/html");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{
                                DebugConf.RECIPIENT_EMAIL
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

                    } else {
                        // should never occur but just in case...
                        Toast.makeText(context, "Cannot find Log path", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "Dumping failed. Check Log", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();

    }

}
