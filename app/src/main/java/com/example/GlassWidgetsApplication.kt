package com.example

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.feedback.AmbientGlassAudioEngine

/**
 * Application entry point coordinating foreground/background lifecycle for the
 * light ambient background audio so playback automatically stops whenever the app closes
 * or leaves the foreground.
 */
class GlassWidgetsApplication : Application(), Application.ActivityLifecycleCallbacks {

    val ambientAudioEngine: AmbientGlassAudioEngine = AmbientGlassAudioEngine()

    private var startedActivityCount: Int = 0
    private var isChangingConfig: Boolean = false

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }

    override fun onTerminate() {
        ambientAudioEngine.onAppBackgrounded()
        super.onTerminate()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

    override fun onActivityStarted(activity: Activity) {
        if (++startedActivityCount == 1 && !isChangingConfig) {
            ambientAudioEngine.onAppForegrounded()
        }
    }

    override fun onActivityResumed(activity: Activity) = Unit

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStopped(activity: Activity) {
        isChangingConfig = activity.isChangingConfigurations
        startedActivityCount = (startedActivityCount - 1).coerceAtLeast(0)
        if (startedActivityCount == 0 && !isChangingConfig) {
            ambientAudioEngine.onAppBackgrounded()
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    override fun onActivityDestroyed(activity: Activity) {
        if (startedActivityCount == 0 && !activity.isChangingConfigurations) {
            ambientAudioEngine.onAppBackgrounded()
        }
    }
}
