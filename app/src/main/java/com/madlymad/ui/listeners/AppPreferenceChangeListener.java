package com.madlymad.ui.listeners;

import androidx.preference.MultiSelectListPreference;
import androidx.preference.SwitchPreference;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import android.text.TextUtils;

import com.madlymad.uptime.R;

import java.util.Set;

/**
 * A preference value change listener that updates the preference's summary
 * to reflect its new value.
 * <p/>
 * Created by mando on 27/9/2015.
 */
public class AppPreferenceChangeListener {

    public void onPreferenceChange(Preference preference, Object value) {
        StringBuilder stringBuffer = new StringBuilder();
        if (value != null) {
            stringBuffer.append(value.toString());
        }
        if (preference instanceof MultiSelectListPreference && value != null) {
            summaryForMultiSelectListPreference(preference, value);
        } else if (preference instanceof ListPreference) {
            summaryForListPreference(preference, stringBuffer);
        } else if (preference instanceof SwitchPreference) {
            summaryForSwitchPreference(preference, stringBuffer);
        } else if (!(preference instanceof CheckBoxPreference)) {
            // For all other preferences, set the summary to the value's
            // simple string representation.
            preference.setSummary(stringBuffer.toString());
        }
    }

    private void summaryForSwitchPreference(Preference preference, StringBuilder stringBuffer) {
        String title = (String) preference.getTitle();
        if (TextUtils.isEmpty(title)
                || title.equals(preference.getContext().getString(R.string.on))
                || title.equals(preference.getContext().getString(R.string.off))) {
            preference.setTitle(stringBuffer.toString());
            preference.setSummary(null);
        } else {
            preference.setSummary(stringBuffer.toString());
        }
    }

    private void summaryForListPreference(Preference preference, StringBuilder stringBuffer) {
        // For list preferences, look up the correct display value in
        // the preference's 'entries' list.
        ListPreference listPreference = (ListPreference) preference;
        int index = listPreference.findIndexOfValue(stringBuffer.toString());

        // Set the summary to reflect the new value.
        preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
    }

    private void summaryForMultiSelectListPreference(Preference preference, Object value) {
        StringBuilder stringBuffer;
        stringBuffer = new StringBuilder();
        //noinspection unchecked
        Set<String> selectedValues = (Set<String>) value;
        MultiSelectListPreference multiPreference = (MultiSelectListPreference) preference;
        CharSequence[] entries = multiPreference.getEntries();
        CharSequence[] values = multiPreference.getEntryValues();

        for (int i = 0; i < values.length; i++) {
            CharSequence entryValue = values[i];
            if (selectedValues.contains(entryValue.toString())) {
                stringBuffer.append(entries[i]).append(", ");
            }
        }
        if (stringBuffer.length() > 0) {
            int index = stringBuffer.lastIndexOf(", ");
            stringBuffer.delete(index, index + 2);
        }
        preference.setSummary(stringBuffer.toString());
    }
}
