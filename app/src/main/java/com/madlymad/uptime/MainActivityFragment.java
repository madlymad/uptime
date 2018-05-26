package com.madlymad.uptime;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madlymad.debug.DebugConf;
import com.madlymad.debug.LtoF;
import com.madlymad.uptime.notifications.CreateNotification;
import com.madlymad.uptime.receivers.Receivers;
import com.madlymad.uptime.widget.Update;

import java.util.Calendar;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final String TAG = "app_config";
    private static final String LOG_TAG = "Main";
    private Chronometer chronometer;
    private EditText daysView;
    private TextView nextView;
    private Spinner measureSpinner;
    private Button unset;
    private TextView errorView;
    public MainActivityFragment() {
    }

    public static MainActivityFragment newInstance() {
        return new MainActivityFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chronometer = view.findViewById(R.id.chronometerUptime);
        chronometer.setBase(0);
        chronometer.start();

        unset = view.findViewById(R.id.buttonUnset);
        nextView = view.findViewById(R.id.textViewNotificationDetails);
        daysView = view.findViewById(R.id.editTextDays);
        measureSpinner = view.findViewById(R.id.spinnerMeasure);
        errorView = view.findViewById(R.id.textViewNotificationAlert);

        Button apply = view.findViewById(R.id.buttonApply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = Integer.parseInt(daysView.getText().toString());
                int position = measureSpinner.getSelectedItemPosition();
                long duration = TextFormatUtils.convertToMillis(position, number);
                if (duration > 0) {
                    com.madlymad.Prefs.setValue(getContext(), Prefs.NOTIFY_MEASURE, position);
                    com.madlymad.Prefs.setValue(getContext(), Prefs.NOTIFY_NUMBER, number);

                    CreateNotification.scheduleNotification(getContext(), duration);
                    Receivers.checkReceivers(getContext());
                } else {
                    Toast.makeText(getContext(), R.string.notify_error, Toast.LENGTH_LONG).show();
                }

                updateUI();
            }
        });

        unset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNotification.removeScheduledNotification(getContext());
                Receivers.checkReceivers(getContext());

                updateUI();
            }
        });

        Receivers.checkReceivers(getContext());
        updateUI();
        Update.updateAllWidgets(getContext());
    }

    private void updateUI() {
        boolean showRestart = false;
        long timestamp = com.madlymad.Prefs.getLongValue(getContext(), Prefs.NOTIFY_TIMESTAMP);
        long elapsedRestart = com.madlymad.Prefs.getLongValue(getContext(), Prefs.NOTIFY_MILLIS);
        Date date = new Date(timestamp);
        String myDate = TextFormatUtils.getDateString(date);
        boolean isScheduled = CreateNotification.isScheduled(getContext());

        if (DebugConf.DebugParts.NOTIFICATION) {
            LtoF.logFile(getContext(), Log.VERBOSE, "[" + LOG_TAG + "]" +
                    " Notify timestamp: " + timestamp
                    + " isScheduled: " + isScheduled);
        }

        if (!isScheduled && timestamp > 0) {
            // TODO improve scheduling
            CreateNotification.scheduleNotification(getContext(), elapsedRestart);
            isScheduled = CreateNotification.isScheduled(getContext());
        }

        if (isScheduled) {
            nextView.setText(getString(R.string.scheduled_notification, myDate));
            unset.setVisibility(View.VISIBLE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            if (timestamp < System.currentTimeMillis()) {
                showRestart = true;
            }
        } else {
            nextView.setText(R.string.no_scheduled_notifications);
            unset.setVisibility(View.GONE);
        }
        daysView.setText(String.valueOf(com.madlymad.Prefs.getIntValue(getContext(), Prefs.NOTIFY_NUMBER)));
        measureSpinner.setSelection(com.madlymad.Prefs.getIntValue(getContext(), Prefs.NOTIFY_MEASURE));

        errorView.setVisibility(showRestart ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Functionality of menu is at activity code
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (chronometer != null) {
            chronometer.setBase(0);
            chronometer.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (chronometer != null) {
            chronometer.stop();
        }
    }
}
