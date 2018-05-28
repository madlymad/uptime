package com.madlymad.uptime;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.madlymad.uptime.constants.Measure;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Created on 28/4/2018.
 *
 * @author mando
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void acceptTerms() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        onView(withText(appContext.getString(R.string.ok))).perform(click());
    }

    @Test
    public void checkInitialNoScheduledNotifications() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        onView(withText(appContext.getString(R.string.schedule)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void createNotificationToFireNow() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        // Check that notification set views are not shown
        notificationViewsNotShown(appContext);

        // Show notification dialog
        onView(withId(R.id.buttonApply)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonApply)).perform(click());

        // Set the notification
        ViewInteraction buttonApply = onView(
                allOf(withId(android.R.id.button1), withText(appContext.getString(R.string.apply))));
        buttonApply.perform(scrollTo(), click());

        onView(withId(R.id.buttonApply)).check(matches(not(withText(appContext.getString(R.string.schedule)))));

        // Check that notification views appeared
        onView(withId(R.id.buttonUnset)).check(matches(isDisplayed()));
        //onView(withId(R.id.textViewNotificationAlert)).check(matches(isDisplayed()));

        // Unset the notification
        onView(withId(R.id.buttonUnset)).perform(click());

        // Check that notification views disappeared
        notificationViewsNotShown(appContext);
    }

    private void notificationViewsNotShown(Context appContext) {
        onView(withText(appContext.getString(R.string.schedule)))
                .check(matches(isDisplayed()));
        onView(withId(R.id.textViewNotificationAlert)).check(matches(not(isDisplayed())));
        onView(withId(R.id.buttonUnset)).check(matches(not(isDisplayed())));
    }

}
