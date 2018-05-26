package com.madlymad.uptime;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created on 28/4/2018.
 *
 * @author mando
 */

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class NotificationAutomatorTest {

    private static final String TEST_PACKAGE = BuildConfig.APPLICATION_ID;
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final long NOTIFICATION_TIMEOUT = 5000;
    private static final String NOTIFICATION_TITLE = "Restart Device";
    private static final String NOTIFICATION_TEXT = "Time has come to restart your device. Its important for its well-being!";
    public static final String ANDROID_WIDGET_BUTTON = "android.widget.Button";
    private static final String ANDROID_WIDGET_EDIT_TEXT = "android.widget.EditText";
    private UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(TEST_PACKAGE);
        // Clear out any previous instances
        assert intent != null;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(TEST_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void testNotificationFullFlow() throws UiObjectNotFoundException {
        UiObject okButton = mDevice.findObject(new UiSelector()
                .text("OK")
                .className(ANDROID_WIDGET_BUTTON));

        // Simulate a user-click on the OK button, if found.
        assert okButton.exists() && okButton.isEnabled();
        okButton.click();

        UiObject editText = mDevice.findObject(new UiSelector()
                .className(ANDROID_WIDGET_EDIT_TEXT));

        // Simulate a user-click on the OK button, if found.
        assert editText.exists() && editText.isEnabled();
        editText.click();
        editText.clearTextField();
        editText.setText("1");

        UiObject applyButton = mDevice.findObject(new UiSelector()
                .text("Apply")
                .className(ANDROID_WIDGET_BUTTON));

        // Simulate a user-click on the OK button, if found.
        assert applyButton.exists() && applyButton.isEnabled();
        applyButton.click();

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.openNotification();
        device.wait(Until.hasObject(By.text(NOTIFICATION_TITLE)), NOTIFICATION_TIMEOUT);
        UiObject2 title = device.findObject(By.text(NOTIFICATION_TITLE));
        UiObject2 text = device.findObject(By.text(NOTIFICATION_TEXT));
        assertEquals(NOTIFICATION_TITLE, title.getText());
        assertEquals(NOTIFICATION_TEXT, text.getText());
        title.click();
        device.wait(Until.hasObject((By.pkg(TEST_PACKAGE).depth(0))), NOTIFICATION_TIMEOUT);
    }

}
