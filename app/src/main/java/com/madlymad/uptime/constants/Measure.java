package com.madlymad.uptime.constants;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class Measure {
    @Retention(SOURCE)
    @IntDef({MEASURE_IN_HOURS, MEASURE_IN_DAYS, MEASURE_IN_MONTHS})
    public @interface TimeMeasurement {
    }

    public static final int MEASURE_IN_HOURS = 0;
    public static final int MEASURE_IN_DAYS = 1;
    public static final int MEASURE_IN_MONTHS = 2;

    public static final int DEFAULT_RESTART_MEASURE = MEASURE_IN_DAYS;
    public static final int DEFAULT_RESTART_VALUE = 7;

    public static final int MIN_NUMERIC_TIME = 1;
    public static final int MAX_HOURS = 23;
    public static final int MAX_DAYS = 29;
    public static final int MAX_MONTHS = 3;
}
