package com.madlymad.debug;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created on 19/3/2018.
 *
 * @author mando
 */

public final class Print {

    private static Map<Class, Function<Object, String>> convertors = new HashMap<>();
    static {
        convertors.put(int[].class, value -> Arrays.toString((int[]) value));
        convertors.put(byte[].class, value -> Arrays.toString((byte[]) value));
        convertors.put(boolean[].class, value -> Arrays.toString((boolean[]) value));
        convertors.put(short[].class, value -> Arrays.toString((short[]) value));
        convertors.put(long[].class, value -> Arrays.toString((long[]) value));
        convertors.put(float[].class, value -> Arrays.toString((float[]) value));
        convertors.put(double[].class, value -> Arrays.toString((double[]) value));
        convertors.put(String[].class, value -> Arrays.toString((String[]) value));
        convertors.put(CharSequence[].class, value -> Arrays.toString((CharSequence[]) value));
        convertors.put(Parcelable[].class, value -> Arrays.toString((Parcelable[]) value));
        convertors.put(Bundle.class, value -> bundleToString((Bundle) value));
    }

    private Print() {
    }

    public static String intentToString(Intent intent) {
        if (intent == null) {
            return null;
        }

        return intent.toString() + " " + bundleToString(intent.getExtras());
    }

    private static String bundleToString(Bundle bundle) {
        StringBuilder out = new StringBuilder("Bundle[");

        if (bundle == null) {
            out.append("null");
        } else {
            boolean first = true;
            for (String key : bundle.keySet()) {
                if (!first) {
                    out.append(", ");
                }

                out.append(key).append('=');

                Object value = bundle.get(key);
                appendValue(out, value);

                first = false;
            }
        }

        out.append(']');
        return out.toString();
    }

    private static void appendValue(StringBuilder out, Object value) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            appendValueApiOld(out, value);
        } else {
            appendValueApi24(out, value);
        }
    }

    @SuppressWarnings({"PMD.NcssCount", "PMD.CyclomaticComplexity"})
    private static void appendValueApiOld(StringBuilder out, Object value) {
        if (value instanceof int[]) {
            out.append(Arrays.toString((int[]) value));
        } else if (value instanceof byte[]) {
            out.append(Arrays.toString((byte[]) value));
        } else if (value instanceof boolean[]) {
            out.append(Arrays.toString((boolean[]) value));
        } else if (value instanceof short[]) {
            out.append(Arrays.toString((short[]) value));
        } else if (value instanceof long[]) {
            out.append(Arrays.toString((long[]) value));
        } else if (value instanceof float[]) {
            out.append(Arrays.toString((float[]) value));
        } else if (value instanceof double[]) {
            out.append(Arrays.toString((double[]) value));
        } else if (value instanceof String[]) {
            out.append(Arrays.toString((String[]) value));
        } else if (value instanceof CharSequence[]) {
            out.append(Arrays.toString((CharSequence[]) value));
        } else if (value instanceof Parcelable[]) {
            out.append(Arrays.toString((Parcelable[]) value));
        } else if (value instanceof Bundle) {
            out.append(bundleToString((Bundle) value));
        } else {
            out.append(value);
        }
    }

    @TargetApi(24)
    private static void appendValueApi24(StringBuilder out, Object value) {
        Function<Object, String> convert = value == null ? null : convertors.get(value.getClass());
        if (convert == null) {
            out.append(value);
        } else {
            out.append(convert.apply(value));
        }
    }

}
