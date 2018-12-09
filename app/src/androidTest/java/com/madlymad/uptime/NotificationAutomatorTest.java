package com.madlymad.uptime;

import android.content.Context;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created on 28/4/2018.
 *
 * @author mando
 */

@SdkSuppress(minSdkVersion = 18)
public class NotificationAutomatorTest {

    private static final String TEST_PACKAGE = BuildConfig.APPLICATION_ID;
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final long NOTIFICATION_TIMEOUT = 5000;
    private static final String NOTIFICATION_TITLE = "Restart Device";
    private static final String NOTIFICATION_TEXT = "Time has come to restart your device. It's important for its well-being!";
    public static final String ANDROID_WIDGET_BUTTON = "android.widget.Button";
    private static final String ANDROID_WIDGET_EDIT_TEXT = "android.widget.EditText";
    private static final String ANDROID_WIDGET_TEXT_VIEW = "android.widget.TextView";
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
        Context context = ApplicationProvider.getApplicationContext();
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

        UiObject schedule = mDevice.findObject(new UiSelector()
                .text("Schedule")
                .className(ANDROID_WIDGET_TEXT_VIEW));

        // Simulate a user-click on the OK button, if found.
        assert schedule.exists() && schedule.isEnabled();
        schedule.click();

        UiObject numberPicker = mDevice.findObject(new UiSelector()
                .className(ANDROID_WIDGET_EDIT_TEXT)
                .text("7"));
        numberPicker.setText("1");
        // To set the value we have to click it!
        numberPicker = mDevice.findObject(new UiSelector()
                .className(ANDROID_WIDGET_EDIT_TEXT)
                .text("1"));
        numberPicker.click();

        UiObject measurePicker = mDevice.findObject(new UiSelector()
                .className(ANDROID_WIDGET_EDIT_TEXT)
                .text("days"));
        measurePicker.setText("hours");
        // To set the value we have to click it!
        measurePicker = mDevice.findObject(new UiSelector()
                .className(ANDROID_WIDGET_EDIT_TEXT)
                .text("hours"));
        measurePicker.click();
        numberPicker.click();

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
        // This requires the device to run for 1 hour
        assertEquals(NOTIFICATION_TITLE, title.getText());
        assertEquals(NOTIFICATION_TEXT, text.getText());
        title.click();
        device.wait(Until.hasObject((By.pkg(TEST_PACKAGE).depth(0))), NOTIFICATION_TIMEOUT);
    }

}
