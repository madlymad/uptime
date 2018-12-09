package com.madlymad.ui.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import com.madlymad.uptime.R;

/**
 * A parent activity that each activity should override.
 * <p>
 * Created by mando on 2/1/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @SuppressWarnings("SameParameterValue")
    protected void replaceFragment(Fragment fragment, String tag) {
        replaceFragment(fragment, tag, false);
    }

    protected void addFragment(Fragment fragment, String tag) {
        replaceFragment(fragment, tag, true);
    }

    private void replaceFragment(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment, tag);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }
}
