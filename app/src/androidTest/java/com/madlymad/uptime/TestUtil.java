package com.madlymad.uptime;

import android.os.SystemClock;
import android.widget.NumberPicker;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.action.Tap;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

class TestUtil {
    public static final String TEST_PACKAGE = BuildConfig.APPLICATION_ID;
    public static final long DEFAULT_TIMEOUT = 5000;
    static final int LAUNCH_TIMEOUT = (int) DEFAULT_TIMEOUT;
    public static final String ANDROID_WIDGET_BUTTON = "android.widget.Button";
    static final String ANDROID_WIDGET_EDIT_TEXT = "android.widget.EditText";
    static final String ANDROID_WIDGET_TEXT_VIEW = "android.widget.TextView";
    static final String ANDROID_WIDGET_CHECKBOX = "android.widget.Checkbox";

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

    //This method returns child from scrollable parent using text
    public UiObject findChildFromScrollParentByText(String scrollParentClass, String childItemClass, String childDesc) throws UiObjectNotFoundException {
        UiScrollable parent = new UiScrollable(new UiSelector()
                .className(scrollParentClass));
        return parent.getChildByText(new UiSelector()
                .className(childItemClass), childDesc, true);
    }

    public static void selectNumberPickerValue(int pickerId, int targetValue, ActivityTestRule activityTestRule) {
        final int ROWS_PER_SWIPE = 5;
        NumberPicker numberPicker = activityTestRule.getActivity().findViewById(pickerId);
        ViewInteraction viewInteraction = onView(withId(pickerId));

        while (targetValue != numberPicker.getValue()) {
            int delta = Math.abs(targetValue - numberPicker.getValue());
            if (targetValue < numberPicker.getValue()) {
                if (delta >= ROWS_PER_SWIPE) {
                    viewInteraction.perform(new GeneralSwipeAction(Swipe.FAST, GeneralLocation.TOP_CENTER, GeneralLocation.BOTTOM_CENTER, Press.FINGER));
                } else {
                    viewInteraction.perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.TOP_CENTER, Press.FINGER, 0, 0));
                }
            } else {
                if (delta >= ROWS_PER_SWIPE) {
                    viewInteraction.perform(new GeneralSwipeAction(Swipe.FAST, GeneralLocation.BOTTOM_CENTER, GeneralLocation.TOP_CENTER, Press.FINGER));
                } else {
                    viewInteraction.perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.BOTTOM_CENTER, Press.FINGER, 0, 0));
                }
            }
            SystemClock.sleep(50);
        }
    }
}
