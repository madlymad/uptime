package com.madlymad.util;

/**
 * Created on 19/3/2018.
 *
 * @author mando
 */

public final class DeviceUtils {

    private DeviceUtils() {
    }

    /**
     * Returns the manufacturer of the device.
     */
    public static String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * @return {@link android.os.Build#MODEL}
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }

}
