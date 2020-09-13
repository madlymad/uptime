package com.madlymad.uptime.models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.madlymad.uptime.UpPrefsUtils;
import com.madlymad.uptime.models.objects.TimePickerValue;

@SuppressWarnings("PMD.DataClass")
public class TimePickerViewModel extends AndroidViewModel {

    private TimePickerValue value = new TimePickerValue();

    public TimePickerViewModel(Application application) {
        super(application);
        initData(application);
    }

    public TimePickerValue getValue() {
        return value;
    }

    public void setValue(TimePickerValue timePickerValue) {
        value = timePickerValue;
    }

    public int getMeasurement() {
        return value.getMeasurement();
    }

    public void setMeasurement(int measurement) {
        value.setMeasurement(measurement);
    }

    public int getNumericValue() {
        return value.getNumericValue();
    }

    public void setNumericValue(int numericValue) {
        value.setNumericValue(numericValue);
    }

    public void storeData() {
        UpPrefsUtils.setTimePickerValue(getApplication(), value);
    }

    public void initData() {
        initData(getApplication());
    }

    private void initData(Application application) {
        value = UpPrefsUtils.getTimePickerValue(application);
    }
}
