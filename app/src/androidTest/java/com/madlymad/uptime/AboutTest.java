package com.madlymad.uptime;


import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import com.madlymad.debug.BeDeveloper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class AboutTest {
    private static final String PRIVACY_POLICY_HTML = "https://madlymad.gitlab.io/uptime/policies/privacy_policy.html";
    private static final String TERMS_AND_CONDITIONS_HTML = "https://madlymad.gitlab.io/uptime/policies/terms_and_conditions.html";
    private static final long TIMEOUT = 30000;
    private UiDevice mDevice;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    @Rule
    public RuleChain ruleChain =
            RuleChain.outerRule(mActivityRule).around(new ScreenshotTakingRule());

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
    public void aboutTest() throws UiObjectNotFoundException {
        TestUtil.clickItemWithText(mDevice, "OK", TestUtil.ANDROID_WIDGET_BUTTON);

        // Click button More
        TestUtil.clickItemWithDescription(mDevice, "More options");

        // Click about
        TestUtil.clickItemWithText(mDevice, "About", TestUtil.ANDROID_WIDGET_TEXT_VIEW);

        UiObject crashData = TestUtil.findObjectWithText(mDevice, "Provide crash data", TestUtil.ANDROID_WIDGET_TEXT_VIEW);
        assert crashData.exists() && crashData.isEnabled();

        UiObject checkbox = crashData.getFromParent(new UiSelector().className(TestUtil.ANDROID_WIDGET_CHECKBOX));
        assert checkbox.exists() && checkbox.isEnabled() && checkbox.isChecked();

        // Click Open Source Licenses
        TestUtil.clickItemWithText(mDevice, "Open Source Licenses", TestUtil.ANDROID_WIDGET_TEXT_VIEW);
        // TODO verify scroll list

        TestUtil.clickItemWithDescription(mDevice, "Navigate up");

        // Click Privacy Policy
        TestUtil.clickItemWithText(mDevice, "Privacy Policy", TestUtil.ANDROID_WIDGET_TEXT_VIEW);
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.wait(Until.hasObject(By.text(PRIVACY_POLICY_HTML)), TIMEOUT);
        UiObject2 text = device.findObject(By.text(PRIVACY_POLICY_HTML));
        assert text.isEnabled();
        device.pressBack();

        // Click Terms & Conditions
        TestUtil.clickItemWithText(mDevice, "Terms & Conditions", TestUtil.ANDROID_WIDGET_TEXT_VIEW);
        device.wait(Until.hasObject(By.text(TERMS_AND_CONDITIONS_HTML)), TIMEOUT);
        text = device.findObject(By.text(TERMS_AND_CONDITIONS_HTML));
        assert text.isEnabled();
        device.pressBack();

        // Click Credits
        TestUtil.clickItemWithText(mDevice, "Credits", TestUtil.ANDROID_WIDGET_TEXT_VIEW);
        text = device.findObject(By.text("Credits"));
        assert text.isEnabled();
        device.pressBack();

        // Developer options
        UiObject developerOptions = TestUtil.findObjectWithText(mDevice, "Developer options", TestUtil.ANDROID_WIDGET_TEXT_VIEW);
        assert !developerOptions.exists();

        // Version
        UiObject version = TestUtil.findObjectWithText(mDevice, "Version", TestUtil.ANDROID_WIDGET_TEXT_VIEW);
        assert version.exists();
        UiObject versionNum = crashData.getFromParent(new UiSelector().className(TestUtil.ANDROID_WIDGET_TEXT_VIEW).text(BuildConfig.VERSION_NAME));
        assert versionNum.exists();

        for (int i = 0; i < BeDeveloper.TAPS_TO_BE_A_DEVELOPER * BeDeveloper.HITS_COUNT; i++) {
            version.click();
        }

        developerOptions = TestUtil.findObjectWithText(mDevice, "Developer options", TestUtil.ANDROID_WIDGET_TEXT_VIEW);
        assert developerOptions.exists();
    }
}
