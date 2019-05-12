package com.madlymad.debug;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created on 19/3/2018.
 *
 * @author mando
 */

public final class Print {

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

    @SuppressWarnings({"PMD.NcssCount", "PMD.CyclomaticComplexity"})
    private static void appendValue(StringBuilder out, Object value) {
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
}
