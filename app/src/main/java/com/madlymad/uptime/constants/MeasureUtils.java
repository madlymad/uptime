package com.madlymad.uptime.constants;

import java.lang.annotation.Retention;

import androidx.annotation.IntDef;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public final class MeasureUtils {

    public static final int MEASURE_IN_HOURS = 0;
    public static final int MEASURE_IN_DAYS = 1;
    public static final int MEASURE_IN_MONTHS = 2;

    public static final int DEFAULT_RESTART_MEASURE = MEASURE_IN_DAYS;
    public static final int DEFAULT_RESTART_VALUE = 7;

    private static final int MIN_NUMERIC_TIME = 1;
    private static final int MAX_HOURS = 23;
    private static final int MAX_DAYS = 29;
    private static final int MAX_MONTHS = 3;

    public static final int POS_MIN = 0;
    public static final int POS_MAX = 1;

    private MeasureUtils() {
    }

    public static int[] measurementValues(int measurementValue) {
        int[] values = new int[2];
        switch (measurementValue) {
            case MEASURE_IN_HOURS: // hours
                values[POS_MIN] = MIN_NUMERIC_TIME;
                values[POS_MAX] = MAX_HOURS;

                break;
            case MEASURE_IN_DAYS: // days
                values[POS_MIN] = MIN_NUMERIC_TIME;
                values[POS_MAX] = MAX_DAYS;

                break;
            case MEASURE_IN_MONTHS: // months
                values[POS_MIN] = MIN_NUMERIC_TIME;
                values[POS_MAX] = MAX_MONTHS;

                break;

            default: // error
                // noting to do

                break;
        }
        return values;
    }

    @Retention(SOURCE)
    @IntDef({MEASURE_IN_HOURS, MEASURE_IN_DAYS, MEASURE_IN_MONTHS})
    public @interface TimeMeasurement {
    }

}
