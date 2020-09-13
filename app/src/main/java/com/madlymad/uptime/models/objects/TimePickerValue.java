package com.madlymad.uptime.models.objects;

import com.madlymad.uptime.constants.MeasureUtils;

import static com.madlymad.uptime.constants.MeasureUtils.DEFAULT_RESTART_MEASURE;
import static com.madlymad.uptime.constants.MeasureUtils.DEFAULT_RESTART_VALUE;

public class TimePickerValue {
    @MeasureUtils.TimeMeasurement
    private int measurement;
    private int numericValue;

    public TimePickerValue() {
        measurement = DEFAULT_RESTART_MEASURE;
        numericValue = DEFAULT_RESTART_VALUE;
    }

    public TimePickerValue(@MeasureUtils.TimeMeasurement int measurement, int numericValue) {
        this.measurement = measurement;
        this.numericValue = numericValue;
    }

    public int getMeasurement() {
        return measurement;
    }

    public void setMeasurement(@MeasureUtils.TimeMeasurement int measurement) {
        this.measurement = measurement;
    }

    public int getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(int numericValue) {
        this.numericValue = numericValue;
    }
}
