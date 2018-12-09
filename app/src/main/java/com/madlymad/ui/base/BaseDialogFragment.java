package com.madlymad.ui.base;

import android.app.Dialog;
import androidx.fragment.app.DialogFragment;

public class BaseDialogFragment extends DialogFragment {

    /**
     * Workaround the dismiss of the dialog although its marked as retain
     * https://stackoverflow.com/a/15444485/944070
     */
    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

}
