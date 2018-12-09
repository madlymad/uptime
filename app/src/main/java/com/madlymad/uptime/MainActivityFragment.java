package com.madlymad.uptime;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.madlymad.debug.DebugConf;
import com.madlymad.debug.LtoF;
import com.madlymad.uptime.models.NotificationViewModel;
import com.madlymad.uptime.models.TimePickerViewModel;
import com.madlymad.uptime.notifications.CreateNotification;
import com.madlymad.uptime.receivers.Receivers;
import com.madlymad.uptime.widget.Update;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final String TAG = "app_config";
    private static final String LOG_TAG = "Main";
    private Chronometer chronometer;
    private ImageButton unset;
    private TextView errorView;
    private TextView apply;

    private NotificationViewModel notificationViewModel;
    private TimePickerViewModel timePickerViewModel;

    public MainActivityFragment() {
    }

    static MainActivityFragment newInstance() {
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
        initializeModels();
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private void initializeModels() {
        if (getActivity() != null) {
            Prefs.updateNotificationDate(getActivity());
            // The ViewModelStore provides a new ViewModel or one previously created.
            notificationViewModel
                    = ViewModelProviders.of(getActivity()).get(NotificationViewModel.class);
            timePickerViewModel = ViewModelProviders.of(getActivity()).get(TimePickerViewModel.class);

            notificationViewModel.getData().observe(this, timers -> {
                if (timers != null && timers.timestamp != 0) {
                    updateNotifications(timers.elapsedTime);
                }
                updateUI();
            });
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chronometer = view.findViewById(R.id.chronometerUptime);
        chronometer.setBase(0);
        chronometer.start();

        unset = view.findViewById(R.id.buttonUnset);
        errorView = view.findViewById(R.id.textViewNotificationAlert);

        apply = view.findViewById(R.id.buttonApply);
        apply.setOnClickListener(view12 -> showSchedule());

        unset.setOnClickListener(view1 -> {
            CreateNotification.removeScheduledNotification(getContext());
            Receivers.checkReceivers(getContext());

            updateUI();
        });

        Receivers.checkReceivers(getContext());
        updateUI();
        Update.updateAllWidgets(getContext());
    }

    private void updateUI() {
        boolean showRestart = false;
        if (notificationViewModel.getData() == null
                || notificationViewModel.getData().getValue() == null) {
            return;
        }

        long timestamp = notificationViewModel.getData().getValue().timestamp;

        Date date = new Date(timestamp);
        String myDate = TimeTextUtils.getDateString(date);
        boolean isScheduled = CreateNotification.isScheduled(getContext());

        if (DebugConf.DebugParts.NOTIFICATION) {
            LtoF.logFile(getContext(), Log.VERBOSE, "[" + LOG_TAG + "]" +
                    " Notify timestamp: " + timestamp
                    + " isScheduled: " + isScheduled);
        }

        if (isScheduled) {
            apply.setText(myDate);
            unset.setVisibility(View.VISIBLE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            if (timestamp < System.currentTimeMillis()) {
                showRestart = true;
            }
        } else {
            apply.setText(R.string.schedule);
            unset.setVisibility(View.GONE);
        }

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

    private void showSchedule() {
        FragmentManager fm = getFragmentManager();
        if (fm != null) {
            TimePickerDialogFragment dialogFragment = TimePickerDialogFragment.newInstance();
            dialogFragment.show(fm, "fragment_edit_name");
        }
    }

    void onDateSetDialog(int numericValue, int measurementValue) {
        Log.d(LOG_TAG, "number [" + numericValue + "] measurement [" + measurementValue + "]");

        long duration = TimeTextUtils.convertToMillis(measurementValue, numericValue);
        if (duration > 0) {
            notificationViewModel.setData(duration);
            timePickerViewModel.storeData();
        } else {
            Toast.makeText(getContext(), R.string.notify_error, Toast.LENGTH_LONG).show();
        }

        updateUI();
    }

    private void updateNotifications(long duration) {
        Log.d(LOG_TAG, "updateNotifications [" + duration + "]");

        CreateNotification.scheduleNotification(getContext(), duration);
        Receivers.checkReceivers(getContext());
    }
}
