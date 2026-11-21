package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.GlassGalleryScreen
import com.example.ui.GlassGalleryViewModel
import com.example.ui.theme.GlassWidgetsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GlassWidgetsTheme(darkTheme = true) {
                val galleryViewModel: GlassGalleryViewModel = viewModel()
                GlassGalleryScreen(
                    viewModel = galleryViewModel,
                    isWidgetConfigMode = false
                )
            }
        }
    }
}
