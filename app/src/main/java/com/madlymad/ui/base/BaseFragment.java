package com.madlymad.ui.base;

import android.text.TextUtils;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

/**
 * Created on 23/3/2018.
 *
 * @author mando
 */

public class BaseFragment extends Fragment {

    private String previousTitle;
    private String currentTitle;

    public void setTitle(@StringRes int title) {
        if (isAdded() && getActivity() != null) {
            getActivity().setTitle(title);
        }
        currentTitle = getString(title);
    }

    protected void setTitle(String title) {
        if (isAdded() && getActivity() != null) {
            getActivity().setTitle(title);
        }
        currentTitle = title;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getActivity() != null) {
            previousTitle = (String) getActivity().getTitle();

            if (!TextUtils.isEmpty(currentTitle)) {
                getActivity().setTitle(currentTitle);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (getActivity() != null && !TextUtils.isEmpty(previousTitle)) {
            getActivity().setTitle(previousTitle);
        }
    }

}
