package com.madlymad.uptime.models;

import android.app.Application;

import com.madlymad.uptime.UpPrefsUtils;

import androidx.lifecycle.AndroidViewModel;

import static com.madlymad.uptime.constants.MeasureUtils.DEFAULT_RESTART_MEASURE;
import static com.madlymad.uptime.constants.MeasureUtils.DEFAULT_RESTART_VALUE;

@SuppressWarnings("PMD.DataClass")
public class TimePickerViewModel extends AndroidViewModel {

    private int measurement;
    private int numericValue;

    public TimePickerViewModel(Application application) {
        super(application);
        initData(application);
    }

    public int getMeasurement() {
        return measurement;
    }

    public void setMeasurement(int measurement) {
        this.measurement = measurement;
    }

    public int getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(int numericValue) {
        this.numericValue = numericValue;
    }

    public void storeData() {
        UpPrefsUtils.setValue(getApplication(), UpPrefsUtils.NOTIFY_MEASURE, this.measurement);
        UpPrefsUtils.setValue(getApplication(), UpPrefsUtils.NOTIFY_NUMBER, this.numericValue);
    }

    private void initData(Application application) {
        measurement = UpPrefsUtils.getIntValue(application, UpPrefsUtils.NOTIFY_MEASURE, DEFAULT_RESTART_MEASURE);
        numericValue = UpPrefsUtils.getIntValue(application, UpPrefsUtils.NOTIFY_NUMBER, DEFAULT_RESTART_VALUE);
    }

    public void initData() {
        initData(getApplication());
    }
}
