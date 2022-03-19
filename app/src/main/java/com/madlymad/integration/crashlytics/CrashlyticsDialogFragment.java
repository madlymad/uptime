package com.madlymad.integration.crashlytics;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.madlymad.ui.base.BaseDialogFragment;
import com.madlymad.uptime.R;

public class CrashlyticsDialogFragment extends BaseDialogFragment {

    public static final String TAG = "agree_crashlytics";
    private AcceptDialogListener acceptDialogListener;

    static CrashlyticsDialogFragment newInstance(AcceptDialogListener listener) {
        CrashlyticsDialogFragment frag = new CrashlyticsDialogFragment();
        frag.acceptDialogListener = listener;
        frag.setCancelable(false);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireActivity())
                .setMessage(R.string.crashlytics_opt_in)
                .setCancelable(false)
                //.setTitle(R.string.title)
                .setPositiveButton(R.string.agree,
                        (dialog, whichButton) -> {
                            dialog.dismiss();
                            if (acceptDialogListener != null) {
                                acceptDialogListener.onFinishDialog(true);
                            }
                        }
                )
                .setNegativeButton(R.string.disagree,
                        (dialog, whichButton) -> {
                            dialog.dismiss();
                            if (acceptDialogListener != null) {
                                acceptDialogListener.onFinishDialog(false);
                            }
                        }
                );
        return dialogBuilder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (getDialog() != null) {
            getDialog().dismiss();
        }
        super.onSaveInstanceState(outState);
    }

    // Defines the listener interface with a method passing back data result.
    public interface AcceptDialogListener {
        void onFinishDialog(boolean accepted);
    }

}
