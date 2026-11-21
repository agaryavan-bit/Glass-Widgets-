package com.example

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.glance.GlassWidget4x1Receiver
import com.example.glance.GlassWidget4x2Receiver
import com.example.glance.GlassWidget4x4Receiver
import com.example.model.WidgetSlotSize
import com.example.ui.GlassGalleryScreen
import com.example.ui.GlassGalleryViewModel
import com.example.ui.theme.GlassWidgetsTheme

/**
 * Shared reconfigurable Widget Configuration Activity pointed to by all 4 size-based
 * GlanceAppWidget slots (2x2, 4x1, 4x2, 4x4). Saves configuration to Jetpack DataStore
 * keyed by appWidgetId, triggers GlanceAppWidget update via getGlanceIdBy(appWidgetId),
 * and returns RESULT_OK.
 */
class WidgetConfigActivity : ComponentActivity() {

    private var appWidgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Default to CANCELED if user backs out before tapping Apply
        setResult(Activity.RESULT_CANCELED)

        appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        val suggestedSlot = detectSlotSizeForWidget(appWidgetId)

        setContent {
            GlassWidgetsTheme(darkTheme = true) {
                val galleryViewModel: GlassGalleryViewModel = viewModel()
                LaunchedEffect(appWidgetId) {
                    galleryViewModel.initializeForWidgetConfig(appWidgetId, suggestedSlot)
                }
                GlassGalleryScreen(
                    viewModel = galleryViewModel,
                    isWidgetConfigMode = true,
                    onApplyPlacedWidget = {
                        galleryViewModel.applyWidgetConfiguration(appWidgetId) {
                            val resultValue = Intent().putExtra(
                                AppWidgetManager.EXTRA_APPWIDGET_ID,
                                appWidgetId
                            )
                            setResult(Activity.RESULT_OK, resultValue)
                            finish()
                        }
                    }
                )
            }
        }
    }

    private fun detectSlotSizeForWidget(widgetId: Int): WidgetSlotSize {
        val info = AppWidgetManager.getInstance(this).getAppWidgetInfo(widgetId) ?: return WidgetSlotSize.SIZE_2X2
        val cls = info.provider?.className.orEmpty()
        return when {
            cls.endsWith(GlassWidget4x1Receiver::class.java.simpleName) -> WidgetSlotSize.SIZE_4X1
            cls.endsWith(GlassWidget4x2Receiver::class.java.simpleName) -> WidgetSlotSize.SIZE_4X2
            cls.endsWith(GlassWidget4x4Receiver::class.java.simpleName) -> WidgetSlotSize.SIZE_4X4
            else -> WidgetSlotSize.SIZE_2X2
        }
    }
}
