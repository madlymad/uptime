package com.madlymad.uptime.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.madlymad.uptime.Prefs;

import static com.madlymad.uptime.constants.Measure.DEFAULT_RESTART_MEASURE;
import static com.madlymad.uptime.constants.Measure.DEFAULT_RESTART_VALUE;

public class TimePickerViewModel extends AndroidViewModel {

    private int measurement;
    private int numericValue;

    public TimePickerViewModel(Application application) {
        super(application);

        initData();
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
        Prefs.setValue(getApplication(), Prefs.NOTIFY_MEASURE, this.measurement);
        Prefs.setValue(getApplication(), Prefs.NOTIFY_NUMBER, this.numericValue);
    }

    public void initData() {
        measurement = Prefs.getIntValue(getApplication(), Prefs.NOTIFY_MEASURE, DEFAULT_RESTART_MEASURE);
        numericValue = Prefs.getIntValue(getApplication(), Prefs.NOTIFY_NUMBER, DEFAULT_RESTART_VALUE);
    }
}
