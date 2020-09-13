package com.madlymad.uptime;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.madlymad.ui.base.BaseDialogFragment;
import com.madlymad.uptime.constants.MeasureUtils;
import com.madlymad.uptime.models.TimePickerViewModel;
import com.madlymad.uptime.models.objects.TimePickerValue;

public class TimePickerDialogFragment extends BaseDialogFragment {

    private NumberPicker numberPicker;
    private NumberPicker valuePicker;
    private TimeDialogListener dialogListener;
    private TimePickerViewModel mViewModel;

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
            mViewModel = new ViewModelProvider(getActivity()).get(TimePickerViewModel.class);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = onCreateDialogView(requireActivity().getLayoutInflater(), null);
        initializeModels();

        numberPicker = view.findViewById(R.id.pickNumber);
        valuePicker = view.findViewById(R.id.pickValue);

        initNumberValues();
        initMeasurementValues();
        initDefaultValues();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireActivity())
                .setCancelable(false)
                .setView(view)
                .setTitle(R.string.picker_dialog_title)
                .setNegativeButton(R.string.cancel, (dialog, whichButton) -> mViewModel.initData())
                .setPositiveButton(R.string.apply, (dialog, which) -> {
                    if (dialogListener != null) {
                        dialogListener.onDateSetDialog(mViewModel.getValue());
                    }
                });
        return dialogBuilder.create();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        mViewModel.initData();
        super.onCancel(dialog);
    }

    private void initDefaultValues() {
        numberPicker.setValue(mViewModel.getNumericValue());
        valuePicker.setValue(mViewModel.getMeasurement());
        updateNumericValue(mViewModel.getMeasurement());
    }

    private void initNumberValues() {
        updateNumericValue(MeasureUtils.MEASURE_IN_DAYS);

        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> mViewModel.setNumericValue(newVal));
    }

    private void initMeasurementValues() {
        valuePicker.setMinValue(MeasureUtils.MEASURE_IN_HOURS);
        valuePicker.setMaxValue(MeasureUtils.MEASURE_IN_MONTHS);
        valuePicker.setDisplayedValues(getResources().getStringArray(R.array.time_measurements));
        valuePicker.setOnValueChangedListener((picker, oldVal, newVal) -> {

            updateNumericValue(newVal);
            mViewModel.setMeasurement(newVal);
        });
    }

    private void updateNumericValue(int measurementValue) {
        int[] values = MeasureUtils.measurementValues(measurementValue);
        numberPicker.setMinValue(values[MeasureUtils.POS_MIN]);
        numberPicker.setMaxValue(values[MeasureUtils.POS_MAX]);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        saveSelectionToModel();
        super.onSaveInstanceState(outState);
    }

    private void saveSelectionToModel() {
        mViewModel.setValue(new TimePickerValue(valuePicker.getValue(), numberPicker.getValue()));
    }

    public interface TimeDialogContainer {
        TimeDialogListener onRequestListener();
    }

    public interface TimeDialogListener {
        void onDateSetDialog(TimePickerValue value);
    }
}
