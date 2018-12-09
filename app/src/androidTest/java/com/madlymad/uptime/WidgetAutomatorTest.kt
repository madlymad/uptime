@file:Suppress("ConstantConditionIf")

package com.madlymad.uptime

import android.graphics.Point
import android.os.SystemClock
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import com.madlymad.uptime.NotificationAutomatorTest.ANDROID_WIDGET_BUTTON
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created on 28/4/2018.
 * @author mando
 */

const val TIMEOUT: Long = 5000
// WIDGET_SELECTION_AT_X: USE here true or depending
//  if you have to scroll at the X or Y axis when
//  navigating through widget selection screen.
const val WIDGET_SELECTION_AT_X: Boolean = true
const val WIDGET_NAME: String = "Uptime"

class WidgetAutomatorTest {

    private val mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Before
    fun setWidgetOnHome() {
        val screenSize = Point(mDevice.displayWidth, mDevice.displayHeight)
        val screenCenter = Point(screenSize.x / 2, screenSize.y / 2)
        // This a point on screen between the bottom icons
        // and the widgets, its a point that has no objects on a Galaxy S5
        // device with stock Launcher. Most probably you have to modify it in
        // your device or use an empty home screen and just long press at
        // the center of it.
        val showWidgets = Point(825, 1500)

        mDevice.pressHome()

        val launcherPackage = mDevice.launcherPackageName!!
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), TIMEOUT)

        // attempt long press
        mDevice.swipe(arrayOf(showWidgets, showWidgets), 150)
        //  pauseTest(2000)

        mDevice.findObject(By.text("Widgets")).click()
        // mDevice.waitForIdle(IDLE_TIMEOUT)

        var dimen = screenSize.y / 2
        if (WIDGET_SELECTION_AT_X) {
            dimen = screenSize.x / 2
        }

        var widget = findMyWidget(WIDGET_NAME)
        while (widget == null) {
            if (WIDGET_SELECTION_AT_X) {
                // Swipe left to right
                mDevice.swipe(dimen, screenCenter.y, 0, screenCenter.y, 150)
            } else {
                // Swipe top to bottom
                mDevice.swipe(screenCenter.x, dimen, screenCenter.x, 0, 150)
            }
            widget = findMyWidget(WIDGET_NAME)
        }
        val b = widget.visibleBounds
        val c = Point(b.left + 150, b.bottom + 150)
        val dest = Point(c.x + 250, c.y + 250)
        mDevice.swipe(arrayOf(c, c, dest), 150)
    }

    private fun findMyWidget(withName: String): UiObject2? {
        return mDevice.findObject(By.text(withName))
    }

    @Test
    fun addWidget() {
        // Press the button on the Widget Configuration Activity
        val okButton = mDevice.findObject(UiSelector()
                .text("Add widget")
                .className(ANDROID_WIDGET_BUTTON))
        okButton.click()

        // Find the just added widget
        val widget = mDevice.findObject(By.descContains(WIDGET_NAME))
        // Click outside the widget in order to added in the screen
        mDevice.click(widget.visibleBounds.left - 150, widget.visibleBounds.top - 150)

        // Assert that the timer is correct!
        val timerWidget = mDevice.findObject(By.res("com.madlymad.uptime", "appwidget_timer"))

        val inSec = SystemClock.elapsedRealtime() / 1000 / 60
        val min = inSec / 60
        val sec = inSec - min * 60
        val text = min.toString() + ":" + String.format("%02d", sec)

        Assert.assertEquals(text, timerWidget.text)
    }
}


