package com.madlymad.uptime;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.madlymad.debug.BeDeveloper;
import com.madlymad.debug.DebugHelper;
import com.madlymad.debug.LtoF;
import com.madlymad.ui.WebViewFragment;
import com.madlymad.ui.base.BasePreferenceFragment;
import com.madlymad.uptime.constants.TimeTextUtils;
import com.madlymad.util.PrefsUtils;

public class MyPreferenceFragment extends BasePreferenceFragment {
    public static final String TAG = "settings";
    private final BeDeveloper developer;
    private Preference developerOptions;

    /**
     * Register the permissions callback, which handles the user's response to the
     * system permissions dialog. Save the return value, an instance of
     * ActivityResultLauncher, as an instance variable.
     */
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    Toast.makeText(getActivity(), R.string.permission_granted, Toast.LENGTH_SHORT).show();
                    LtoF.mailLogFile(getActivity());
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Toast.makeText(getActivity(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                }
            });

    public MyPreferenceFragment() {
        super();
        developer = new BeDeveloper(R.string.key_developer);
    }

    static MyPreferenceFragment newInstance() {
        return new MyPreferenceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onCreatePreferencesScreen(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        if (TextUtils.isEmpty(rootKey)) {
            setupDeveloper();
        }
    }

    @Override
    public void updateSummaries() {
        super.updateSummaries();

        setSummary(R.string.key_version, BuildConfig.VERSION_NAME);
        setSummary(R.string.key_logs_delete, LtoF.logFileDetails(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        developer.onResume(getActivity());

        updateSummaries();
        if (TextUtils.isEmpty(getRootKey())) {
            setupDeveloper();
            setupLicenses();
            setupCredits();
            setupLinks(R.string.key_privacy, TimeTextUtils.HTML_PRIVACY);
            setupLinks(R.string.key_terms, TimeTextUtils.HTML_TERMS);
        } else if (getRootKey().equals(getKey(R.string.key_developer_screen))) {
            setupLogEnabled();
            setupLogSendMail();
            setupLogShow();
            setupLogDelete();
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (is(preference, R.string.key_version)) {
            developer.onPreferenceTreeClick(getActivity());
            if (isAdded()) {
                setupDeveloper();
            }
        }
        return super.onPreferenceTreeClick(preference);
    }

    private void setupLicenses() {
        setOnClickPreference(R.string.key_license, this::displayLicensesFragment);
    }

    private void setupCredits() {
        setOnClickPreference(R.string.key_credits, this::displayCreditsFragment);
    }

    private void setupLogEnabled() {
        setOnChangePreference(R.string.key_logs_enable, newValue -> {
            if (newValue instanceof Boolean) {
                DebugHelper.setLogToFile((Boolean) newValue);
            }
            return true;
        });
    }

    private void setupLogSendMail() {
        setOnClickPreference(R.string.key_logs_email, this::mailLogs);
    }

    private void setupLogDelete() {
        setOnClickPreference(R.string.key_logs_delete, this::deleteLogs);
    }

    private void deleteLogs() {
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            if (which == AlertDialog.BUTTON_POSITIVE) {
                LtoF.deleteLogFile(getContext());
                updateSummaries();
            }
            dialog.dismiss();
        };

        Context context = getContext();
        if (context != null) {
            AlertDialog ad = new AlertDialog.Builder(context).create();
            ad.setCancelable(true);
            ad.setTitle(R.string.sure_delete_log);
            ad.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.no), listener);
            ad.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.yes), listener);
            ad.show();
        }
    }

    private void setupLogShow() {
        setOnClickPreference(R.string.key_logs_show, this::displayLogFile);
    }

    private void setupLinks(@StringRes int key, final String url) {
        setOnClickPreference(key, () -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }

    private void setupDeveloper() {
        Preference preference = getPreference(R.string.key_developer_screen);
        if (preference != null) {
            developerOptions = preference;
        }

        if (PrefsUtils.getBooleanValue(getActivity(), R.string.key_developer, false)) {
            addPreference(developerOptions);
        } else {
            removePreference(developerOptions);
        }
    }

    private void displayCreditsFragment() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).displayWebFragment(WebViewFragment.buildAsset("credits.html"));
        }
    }

    private void displayLogFile() {
        if (getActivity() instanceof MainActivity && getContext() != null) {
            ((MainActivity) getActivity()).displayWebFragment("file://" + LtoF.getLogFilePath(getContext()));
        }
    }

    private void displayLicensesFragment() {
        // When the user selects an option to see the licenses:
        startActivity(new Intent(getContext(), OssLicensesMenuActivity.class));
    }

    private void mailLogs() {
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}
