package com.madlymad.util;

import android.app.PendingIntent;
import android.os.Build;

public final class Util {
    private Util() {
    }

    public static int mutableFlags(int flags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return flags | PendingIntent.FLAG_MUTABLE;
        } else {
            return flags;
        }
    }
}
