package com.madlymad.ui.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

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
        fragmentTransaction.commit();
    }
}
