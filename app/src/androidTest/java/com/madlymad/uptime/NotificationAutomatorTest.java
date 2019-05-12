package com.madlymad.uptime;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;

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

    private static final long NOTIFICATION_TIMEOUT = 5000;
    private static final String NOTIFICATION_TITLE = "Restart Device";
    private static final String NOTIFICATION_TEXT = "Time has come to restart your device. It's important for its well-being!";
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
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), TestUtil.LAUNCH_TIMEOUT);

        // Launch the app
        Context context = ApplicationProvider.getApplicationContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(TestUtil.TEST_PACKAGE);
        // Clear out any previous instances
        assert intent != null;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(TestUtil.TEST_PACKAGE).depth(0)), TestUtil.LAUNCH_TIMEOUT);
    }

    @Test
    public void testNotificationFullFlow() throws UiObjectNotFoundException {
        // Simulate a user-click on the OK button, if found.
        TestUtil.clickItemWithText(mDevice, "OK", TestUtil.ANDROID_WIDGET_BUTTON);

        // Simulate a user-click on Schedule, if found.
        TestUtil.clickItemWithText(mDevice, "Schedule", TestUtil.ANDROID_WIDGET_TEXT_VIEW);

        // To set the value to 1
        for (int i = 6; i > 0; i = i - 2) {
            UiObject start = TestUtil.findObjectWithText(mDevice, String.valueOf(i), TestUtil.ANDROID_WIDGET_BUTTON);
            UiObject dest = TestUtil.findObjectWithText(mDevice, String.valueOf(i + 1), TestUtil.ANDROID_WIDGET_EDIT_TEXT);
            start.dragTo(dest, 1);
            SystemClock.sleep(50);
        }

        // To set the value to days
        UiObject start = TestUtil.findObjectWithText(mDevice, "hours", TestUtil.ANDROID_WIDGET_BUTTON);
        UiObject dest = TestUtil.findObjectWithText(mDevice, "days", TestUtil.ANDROID_WIDGET_EDIT_TEXT);
        start.dragTo(dest, 1);
        SystemClock.sleep(50);

        // Simulate a user-click on the OK button, if found.
        TestUtil.clickItemWithText(mDevice, "APPLY", TestUtil.ANDROID_WIDGET_BUTTON);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.openNotification();
        device.wait(Until.hasObject(By.text(NOTIFICATION_TITLE)), NOTIFICATION_TIMEOUT);
        UiObject2 title = device.findObject(By.text(NOTIFICATION_TITLE));
        UiObject2 text = device.findObject(By.text(NOTIFICATION_TEXT));
        // This requires the device to run for 1 hour
        assertEquals(NOTIFICATION_TITLE, title.getText());
        assertEquals(NOTIFICATION_TEXT, text.getText());
        title.click();
        device.wait(Until.hasObject((By.pkg(TestUtil.TEST_PACKAGE).depth(0))), NOTIFICATION_TIMEOUT);
    }

}
