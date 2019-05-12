package com.madlymad.integration;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.StringDef;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created on 22/3/2018.
 * Usage example
 *
 * https://firebase.google.com/docs/analytics/android/start/
 *
 * @author mando
 */
@SuppressWarnings("unused")
public class MadAnalytics {

    private FirebaseAnalytics mFirebaseAnalytics;

    @StringDef({
            WidgetTypes.SIMPLE,
            WidgetTypes.DETAILED,
            WidgetTypes.SPECIAL
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface WidgetTypes {
        String SIMPLE = "simple";
        String DETAILED = "detailed";
        String SPECIAL = "special";
    }

    private void init(Context context) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    private void logEvent(@WidgetTypes String type, String title) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, type);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, title);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "widget");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
