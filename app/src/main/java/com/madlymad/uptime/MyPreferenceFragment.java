package com.madlymad.uptime;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.madlymad.Prefs;
import com.madlymad.debug.BeDeveloper;
import com.madlymad.debug.DebugConf;
import com.madlymad.debug.LtoF;
import com.madlymad.ui.WebViewFragment;
import com.madlymad.ui.base.BasePreferenceFragment;

import java.util.ArrayList;

public class MyPreferenceFragment extends BasePreferenceFragment {
    public static final String TAG = "settings";
    private final BeDeveloper developer;
    private Preference developerOptions;

    public MyPreferenceFragment() {
        developer = new BeDeveloper(R.string.key_developer);
    }

    public static MyPreferenceFragment newInstance() {
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
            setupLinks(R.string.key_privacy, TimeTextUtils.GITLAB_PAGES + "policies/privacy_policy.html");
            setupLinks(R.string.key_terms, TimeTextUtils.GITLAB_PAGES + "policies/terms_and_conditions.html");
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
                DebugConf.setLogToFile((Boolean) newValue);
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

        if (Prefs.getBooleanValue(getActivity(), R.string.key_developer, false)) {
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
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getActivity(), R.string.permission_granted, Toast.LENGTH_SHORT).show();
                LtoF.mailLogFile(getActivity());
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getActivity(), getString(R.string.permission_denied, deniedPermissions.toString()), Toast.LENGTH_SHORT).show();
            }
        };
        if (getContext() != null) {
            TedPermission.with(getContext())
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage(R.string.deny_permission_message)
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .check();
        }
    }

}