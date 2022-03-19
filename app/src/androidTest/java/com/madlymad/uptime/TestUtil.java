package com.madlymad.uptime;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

class TestUtil {
    public static final String TEST_PACKAGE = BuildConfig.APPLICATION_ID;
    public static final long DEFAULT_TIMEOUT = 5000;
    static final int LAUNCH_TIMEOUT = (int) DEFAULT_TIMEOUT;
    public static final String ANDROID_WIDGET_BUTTON = "android.widget.Button";
    static final String ANDROID_WIDGET_EDIT_TEXT = "android.widget.EditText";
    static final String ANDROID_WIDGET_TEXT_VIEW = "android.widget.TextView";
    //static final String ANDROID_WIDGET_CHECKBOX = "android.widget.Checkbox";

    static void clickItemWithText(UiDevice mDevice, String text, String className) throws UiObjectNotFoundException {
        // Simulate a user-click on the button or text, if found.
        UiObject uiObject = findObjectWithText(mDevice, text, className);
        assert uiObject.exists() && uiObject.isEnabled();
        uiObject.click();
    }

    static UiObject findObjectWithText(UiDevice mDevice, String text, String className) {
        UiObject element = mDevice.findObject(new UiSelector()
                .text(text)
                .className(className));
        element.waitForExists(DEFAULT_TIMEOUT);
        return element;
    }

    static void clickItemWithDescription(UiDevice mDevice, String text) throws UiObjectNotFoundException {
        // Simulate a user-click on the button or text, if found.
        UiObject uiObject = findObjectWithDescription(mDevice, text);
        assert uiObject.exists() && uiObject.isEnabled();
        uiObject.click();
    }

    private static UiObject findObjectWithDescription(UiDevice mDevice, String text) {
        return mDevice.findObject(new UiSelector()
                .descriptionContains(text));
    }
}
