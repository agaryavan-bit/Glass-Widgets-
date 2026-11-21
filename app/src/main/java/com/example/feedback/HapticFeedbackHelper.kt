package com.example.feedback

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView

/**
 * Tactile haptic feedback helper delivering crisp, subtle liquid-glass haptic cues.
 */
class GlassHaptics(private val view: View) {

    fun tick() {
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            HapticFeedbackConstants.TEXT_HANDLE_MOVE
        } else {
            HapticFeedbackConstants.CLOCK_TICK
        }
        view.performHapticFeedback(flag)
    }

    fun click() {
        view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
    }

    fun confirm() {
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            HapticFeedbackConstants.CONFIRM
        } else {
            HapticFeedbackConstants.LONG_PRESS
        }
        view.performHapticFeedback(flag)
    }
}

@Composable
fun rememberGlassHaptics(): GlassHaptics {
    val view = LocalView.current
    return remember(view) { GlassHaptics(view) }
}
