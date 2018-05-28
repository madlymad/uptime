package com.madlymad.uptime;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.madlymad.ui.base.BaseDialogFragment;
import com.madlymad.uptime.models.TimePickerViewModel;

import java.util.Objects;

import static com.madlymad.uptime.constants.Measure.MAX_DAYS;
import static com.madlymad.uptime.constants.Measure.MAX_HOURS;
import static com.madlymad.uptime.constants.Measure.MAX_MONTHS;
import static com.madlymad.uptime.constants.Measure.MEASURE_IN_DAYS;
import static com.madlymad.uptime.constants.Measure.MEASURE_IN_HOURS;
import static com.madlymad.uptime.constants.Measure.MEASURE_IN_MONTHS;
import static com.madlymad.uptime.constants.Measure.MIN_NUMERIC_TIME;

public class TimePickerDialogFragment extends BaseDialogFragment {

    private NumberPicker numberPicker;
    private NumberPicker valuePicker;
    private TimeDialogListener dialogListener;
    private TimePickerViewModel mViewModel;

    public TimePickerDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
        setRetainInstance(true);
    }

    public static TimePickerDialogFragment newInstance(TimeDialogListener listener) {
        TimePickerDialogFragment frag = new TimePickerDialogFragment();
        frag.dialogListener = listener;
        return frag;
    }

    @SuppressWarnings("SameParameterValue")
    private View onCreateDialogView(LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.selection_dialog, container);
    }

    private void initializeModels() {
        if (getActivity() != null) {
            mViewModel = ViewModelProviders.of(getActivity()).get(TimePickerViewModel.class);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = onCreateDialogView(Objects.requireNonNull(getActivity()).getLayoutInflater(), null);
        initializeModels();

        numberPicker = view.findViewById(R.id.pickNumber);
        valuePicker = view.findViewById(R.id.pickValue);

        initNumberValues();
        initMeasurementValues();
        initDefaultValues();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setView(view)
                .setTitle(R.string.picker_dialog_title)
                .setNegativeButton(R.string.cancel, (dialog, whichButton) -> {
                    mViewModel.initData();
                    dialog.dismiss();
                })
                .setPositiveButton(R.string.apply, ((dialog, which) -> {
                    dialog.dismiss();
                    if (dialogListener != null) {
                        dialogListener.onDateSetDialog(mViewModel.getNumericValue(), mViewModel.getMeasurement());
                    }
                }));
        return dialogBuilder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mViewModel.initData();
        super.onCancel(dialog);
    }

    private void initDefaultValues() {
        numberPicker.setValue(mViewModel.getNumericValue());
        valuePicker.setValue(mViewModel.getMeasurement());
    }

    private void initNumberValues() {
        numberPicker.setMinValue(MIN_NUMERIC_TIME);
        numberPicker.setMaxValue(MAX_DAYS);

        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> mViewModel.setNumericValue(newVal));
    }

    private void initMeasurementValues() {
        valuePicker.setMinValue(MEASURE_IN_HOURS);
        valuePicker.setMaxValue(MEASURE_IN_MONTHS);
        valuePicker.setDisplayedValues(getResources().getStringArray(R.array.time_measurements));
        valuePicker.setOnValueChangedListener((picker, oldVal, newVal) -> {

            switch (newVal) {
                case MEASURE_IN_HOURS: // hours
                    numberPicker.setMinValue(MIN_NUMERIC_TIME);
                    numberPicker.setMaxValue(MAX_HOURS);
                    break;
                case MEASURE_IN_DAYS: // days
                    numberPicker.setMinValue(MIN_NUMERIC_TIME);
                    numberPicker.setMaxValue(MAX_DAYS);
                    break;
                case MEASURE_IN_MONTHS: // months
                    numberPicker.setMinValue(MIN_NUMERIC_TIME);
                    numberPicker.setMaxValue(MAX_MONTHS);
                    break;
            }

            mViewModel.setMeasurement(newVal);
        });
    }

    public interface TimeDialogListener {
        void onDateSetDialog(int numericValue, int measurementValue);
    }
}