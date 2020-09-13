@file:Suppress("ConstantConditionIf")

package com.madlymad.uptime

import android.graphics.Point
import android.os.SystemClock
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.*
import com.madlymad.uptime.TestUtil.DEFAULT_TIMEOUT
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created on 28/4/2018.
 * @author mando
 */

// WIDGET_SELECTION_AT_X: USE here true or depending
//  if you have to scroll at the X or Y axis when
//  navigating through widget selection screen.
const val WIDGET_SELECTION_AT_X: Boolean = false
const val WIDGET_NAME: String = "Uptime"
const val APP_NAME: String = "Uptime"

class WidgetAutomatorTest {

    private val mDevice = UiDevice.getInstance(getInstrumentation())
    private val statusBarSize = 84

    /**
     * Works on simulator API 28
     */
    @Before
    fun setWidgetOnHome() {
        mDevice.pressHome()

        // Bring up the default launcher by searching for a UI component
        // that matches the content description for the launcher button.
        // Wait for launcher
        val launcherPackage: String = mDevice.launcherPackageName
        assertThat(launcherPackage, notNullValue())
        mDevice.wait(
                Until.hasObject(By.pkg(launcherPackage).depth(0)),
                DEFAULT_TIMEOUT
        )
        val x = 21
        val y = statusBarSize
        mDevice.swipe(x, y, x, y, 400)

        val screenSize = Point(mDevice.displayWidth, mDevice.displayHeight)
        val screenCenter = Point(screenSize.x / 2, screenSize.y / 2)

        val widgetsView = mDevice.findObject(UiSelector()
                .text("Widgets")
                .className(TestUtil.ANDROID_WIDGET_TEXT_VIEW))
        widgetsView.click()

        var dimen = screenSize.y / 2
        if (WIDGET_SELECTION_AT_X) {
            dimen = screenSize.x / 2
        }

        var widget = findMyWidget(APP_NAME)
        while (widget == null) {
            if (WIDGET_SELECTION_AT_X) {
                // Swipe left to right
                mDevice.swipe(dimen, screenCenter.y, 0, screenCenter.y, 150)
            } else {
                // Swipe top to bottom
                mDevice.swipe(screenCenter.x, dimen, screenCenter.x, 0, 150)
            }
            widget = findMyWidget(APP_NAME)
        }
        widget.longClick()

        val widgetItem = mDevice.findObject(UiSelector()
                .className("com.android.launcher3.widget.WidgetCell"))
                .getChild(UiSelector().className("android.widget.LinearLayout")
                        .childSelector(UiSelector().className("android.widget.TextView")
                                .text(APP_NAME)))

        widgetItem.dragTo(screenCenter.x, screenCenter.y, 40)

        mDevice.waitForWindowUpdate(BuildConfig.APPLICATION_ID, DEFAULT_TIMEOUT)
    }

    @After
    fun removeWidget() {
        val widgetItem = mDevice.findObject(By.text(APP_NAME))
        widgetItem.click(DEFAULT_TIMEOUT)
        val removeItem = mDevice.findObject(By.text("Remove"))
        widgetItem.drag(removeItem.visibleCenter)
    }


    @Suppress("SameParameterValue")
    private fun findMyWidget(withName: String): UiObject2? {
        return mDevice.findObject(By.text(withName))
    }

    @Test
    fun addWidget() {
        // Press the button on the Widget Configuration Activity
        val okButton = mDevice.findObject(UiSelector()
                .text("ADD WIDGET")
                .className(TestUtil.ANDROID_WIDGET_BUTTON))
        okButton.click()

        // Find the just added widget
        val widget = mDevice.findObject(By.descContains(WIDGET_NAME))
        // Click outside the widget in order to added in the screen
        mDevice.click(widget.visibleBounds.left - 150, widget.visibleBounds.top - 150)

        // Assert that the timer is correct!
        val timerWidget = mDevice.findObject(By.res(TestUtil.TEST_PACKAGE, "appwidget_timer"))

        val inSec = SystemClock.elapsedRealtime() / 1000 / 60
        val min = inSec / 60
        val sec = inSec - min * 60
        val text = min.toString() + ":" + String.format("%02d", sec)

        Assert.assertEquals(text, timerWidget.text)
    }
}
