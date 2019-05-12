package com.madlymad.uptime

import android.util.Log
import androidx.test.runner.screenshot.Screenshot
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.IOException

class ScreenshotTakingRule : TestWatcher() {

    override fun failed(e: Throwable?, description: Description) {
        val parentFolderPath = "failures/${description.className}"
        takeScreenshot(parentFolderPath = parentFolderPath, screenShotName = description.methodName)
    }

    private fun takeScreenshot(parentFolderPath: String = "", screenShotName: String) {
        Log.d("Screenshots", "Taking screenshot of '$screenShotName'")
        val screenCapture = Screenshot.capture()
        val processors = setOf(MyScreenCaptureProcessor(parentFolderPath))
        try {
            screenCapture.apply {
                name = screenShotName
                process(processors)
            }
            Log.d("Screenshots", "Screenshot taken")
        } catch (ex: IOException) {
            Log.e("Screenshots", "Could not take the screenshot", ex)
        }
    }
}
