package com.madlymad.uptime.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.madlymad.debug.DebugHelper;
import com.madlymad.debug.LtoF;
import com.madlymad.uptime.R;
import com.madlymad.uptime.receivers.Receivers;

import androidx.appcompat.widget.AppCompatCheckBox;

/**
 * The configuration screen for the {@link UptimeWidget UptimeWidget} AppWidget.
 */
public class UptimeWidgetConfigureActivity extends Activity {

    private static final String LOG_TAG = UptimeWidgetConfigureActivity.class.getSimpleName();

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private TextInputEditText mAppWidgetText;
    private AppCompatCheckBox mAppWidgetSmartTime;
    private TextView textViewCheckDetails;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = UptimeWidgetConfigureActivity.this;

            // before setValue make sure to cleanup previous widgets
            PrefsWidgetUtils.deletePrefs(context, mAppWidgetId);

            // When the button is clicked, store the string locally
            Editable text = mAppWidgetText.getText();
            String widgetText = (text == null) ? "" : text.toString();
            PrefsWidgetUtils.saveTitle(context, mAppWidgetId, widgetText);

            boolean showSeconds = mAppWidgetSmartTime.isChecked();
            PrefsWidgetUtils.saveSmartTime(context, mAppWidgetId, showSeconds);

            // It is the responsibility of the configuration activity to update the app widget
            UpdateHelper.updateWidget(context, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            if (DebugHelper.DebugUtils.WIDGET) {
                LtoF.logFile(UptimeWidgetConfigureActivity.this, Log.DEBUG,
                        "[" + LOG_TAG + "] added new widget with [" + mAppWidgetId + "]"
                                + " title '" + widgetText + "' seconds [" + showSeconds + "]");
            }

            Receivers.checkReceivers(context);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.uptime_widget_configure);
        mAppWidgetText = findViewById(R.id.appwidget_text);
        textViewCheckDetails = findViewById(R.id.textCheckDetails);
        mAppWidgetSmartTime = findViewById(R.id.checkboxSmartDisplay);
        mAppWidgetSmartTime.setOnCheckedChangeListener((compoundButton, isChecked) -> updateTextViewDetails());

        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mAppWidgetText.setText(PrefsWidgetUtils.loadTitle(this, mAppWidgetId));
        mAppWidgetSmartTime.setChecked(PrefsWidgetUtils.loadSmartTime(this, mAppWidgetId));
        updateTextViewDetails();
    }

    private void updateTextViewDetails() {
        if (mAppWidgetSmartTime == null) {
            return;
        }
        if (mAppWidgetSmartTime.isChecked()) {
            textViewCheckDetails.setText(R.string.smart_time_details);
        } else {
            textViewCheckDetails.setText(R.string.not_smart_time_details);
        }
    }
}
