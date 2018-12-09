package com.madlymad.debug;

import android.content.Context;
import android.os.SystemClock;
import androidx.annotation.StringRes;
import android.widget.Toast;

import com.madlymad.uptime.Prefs;
import com.madlymad.uptime.R;

/**
 * Created on 21/3/2018.
 *
 * @author mando
 */

public class BeDeveloper {
    private static final int TAPS_TO_BE_A_DEVELOPER = 7;
    private final long[] mHits = new long[3];
    private int mDevHitCountdown;
    private Toast mDevHitToast;
    private final int preference;

    /**
     * @param preference_key_id The preference value we want to setValue that the feature is enabled
     */
    public BeDeveloper(@StringRes int preference_key_id) {
        preference = preference_key_id;
    }

    public void onResume(Context context) {
        final boolean developerEnabled = Prefs.getBooleanValue(context, preference, false);
        mDevHitCountdown = developerEnabled ? -1 : TAPS_TO_BE_A_DEVELOPER;
        mDevHitToast = null;
    }

    /**
     * @param context For Toast and saving of preferences
     */
    public void onPreferenceTreeClick(Context context) {
        if (mDevHitCountdown < 0) {
            showToast(context, context.getString(R.string.show_dev_already));
        } else {
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                if (mDevHitCountdown > 0) {
                    mDevHitCountdown--;
                    if (mDevHitCountdown == 0) {
                        Prefs.setValue(context, preference, true);
                        showToast(context, context.getString(R.string.show_dev_on));
                    } else if (mDevHitCountdown > 0
                            && mDevHitCountdown < (TAPS_TO_BE_A_DEVELOPER - 2)) {
                        showToast(context, context.getResources().getQuantityString(R.plurals.show_dev_countdown,
                                mDevHitCountdown, mDevHitCountdown));
                    }
                }
            }
        }
    }

    private void showToast(Context context, String text) {
        if (mDevHitToast != null) {
            mDevHitToast.cancel();
        }
        mDevHitToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        mDevHitToast.show();
    }

}
