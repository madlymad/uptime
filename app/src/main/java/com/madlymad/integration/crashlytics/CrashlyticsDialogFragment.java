package com.madlymad.integration.crashlytics;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.madlymad.ui.base.BaseDialogFragment;
import com.madlymad.uptime.R;

public class CrashlyticsDialogFragment extends BaseDialogFragment {

    public static final String TAG = "agree_crashlytics";
    private static final String ACCEPTED = "accepted";
    private AcceptDialogListener acceptDialogListener;
    private CheckBox mCheckBox;

    static CrashlyticsDialogFragment newInstance(AcceptDialogListener listener) {
        CrashlyticsDialogFragment frag = new CrashlyticsDialogFragment();
        frag.acceptDialogListener = listener;
        frag.setCancelable(false);
        return frag;
    }

    @SuppressWarnings("SameParameterValue")
    private View onCreateDialogView(LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.fragment_crashlytics, container); // inflate here
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = onCreateDialogView(requireActivity().getLayoutInflater(), null);

        mCheckBox = view.findViewById(R.id.checkbox_agree);
        if (savedInstanceState != null) {
            mCheckBox.setChecked(savedInstanceState.getBoolean(ACCEPTED));
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireActivity())
                .setCancelable(false)
                .setView(view)
                //.setTitle(R.string.title)
                .setPositiveButton(R.string.ok,
                        (dialog, whichButton) -> {
                            dialog.dismiss();
                            if (acceptDialogListener != null) {
                                acceptDialogListener.onFinishDialog(mCheckBox.isChecked());
                            }
                        }
                );
        return dialogBuilder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(ACCEPTED, mCheckBox.isChecked());
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
