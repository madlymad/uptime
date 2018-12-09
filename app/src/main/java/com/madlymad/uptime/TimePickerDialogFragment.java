package com.madlymad.uptime;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.madlymad.ui.base.BaseDialogFragment;
import com.madlymad.uptime.models.TimePickerViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

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
    }

    static TimePickerDialogFragment newInstance() {
        return new TimePickerDialogFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TimeDialogContainer) {
            // We are attaching to activity
            dialogListener = ((TimeDialogContainer) context).onRequestListener();
        }
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
                .setNegativeButton(R.string.cancel, (dialog, whichButton) -> mViewModel.initData())
                .setPositiveButton(R.string.apply, ((dialog, which) -> {
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
        updateNumericValue(mViewModel.getMeasurement());
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

            updateNumericValue(newVal);
            mViewModel.setMeasurement(newVal);
        });
    }

    private void updateNumericValue(int measurementValue) {
        switch (measurementValue) {
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
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        saveSelectionToModel();
        super.onSaveInstanceState(outState);
    }

    private void saveSelectionToModel() {
        mViewModel.setMeasurement(valuePicker.getValue());
        mViewModel.setNumericValue(numberPicker.getValue());
    }

    public interface TimeDialogContainer {
        TimeDialogListener onRequestListener();
    }

    public interface TimeDialogListener {
        void onDateSetDialog(int numericValue, int measurementValue);
    }
}