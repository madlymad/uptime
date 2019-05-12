package com.madlymad.uptime.constants;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TimeTextUtilsUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void check_date_print() {
        assertArrayEquals(new long[]{0, 0, 0, 1}, TimeTextUtils.calculateTimes(1000)); // 1 second
        assertArrayEquals(new long[]{0, 0, 1, 0}, TimeTextUtils.calculateTimes(60 * 1000)); // 1 minute
        assertArrayEquals(new long[]{0, 1, 0, 0}, TimeTextUtils.calculateTimes(60 * 60 * 1000)); // 1 hour
        assertArrayEquals(new long[]{1, 0, 0, 0}, TimeTextUtils.calculateTimes(24 * 60 * 60 * 1000)); // 1 day


        assertArrayEquals(new long[]{1, 1, 1, 1},
                TimeTextUtils.calculateTimes((24 * 60 * 60 * 1000) // 1 day
                        + (60 * 60 * 1000) // 1 hour
                        + (60 * 1000) // 1 minute
                        + (1000))); // 1 second
    }

    @Test
    public void checkTimeConversion() {
        assertEquals(60 * 60 * 1000, TimeTextUtils.convertToMillis(MeasureUtils.MEASURE_IN_HOURS, 1));
        assertEquals(24 * 60 * 60 * 1000, TimeTextUtils.convertToMillis(MeasureUtils.MEASURE_IN_DAYS, 1));
        assertEquals(30 * 24 * 60 * 60 * 1000L, TimeTextUtils.convertToMillis(MeasureUtils.MEASURE_IN_MONTHS, 1));
        assertEquals(12 * 30 * 24 * 60 * 60 * 1000L, TimeTextUtils.convertToMillis(MeasureUtils.MEASURE_IN_MONTHS, 12));
    }
}
