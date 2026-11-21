package com.example.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.Bluetooth
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Contacts
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.WatchLater
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.model.WidgetCategory

fun categoryIcon(category: WidgetCategory): ImageVector = when (category) {
    WidgetCategory.CLOCK_DIGITAL -> Icons.Rounded.AccessTime
    WidgetCategory.CLOCK_ANALOG -> Icons.Rounded.Schedule
    WidgetCategory.CLOCK_HYBRID -> Icons.Rounded.WatchLater
    WidgetCategory.CALENDAR -> Icons.Rounded.CalendarMonth
    WidgetCategory.QUICK_SETTINGS -> Icons.Rounded.Tune
    WidgetCategory.GOOGLE -> Icons.Rounded.Search
    WidgetCategory.PHOTO -> Icons.Rounded.PhotoLibrary
    WidgetCategory.WEATHER -> Icons.Rounded.WbSunny
    WidgetCategory.DEVICE -> Icons.Rounded.Memory
    WidgetCategory.BATTERY -> Icons.Rounded.BatteryChargingFull
    WidgetCategory.CONTACTS -> Icons.Rounded.Contacts
    WidgetCategory.AI -> Icons.Rounded.AutoAwesome
    WidgetCategory.PRODUCTIVITY -> Icons.Rounded.Timer
    WidgetCategory.NOTES_TODO -> Icons.Rounded.CheckCircle
    WidgetCategory.APP_LAUNCHER -> Icons.Rounded.Apps
    WidgetCategory.FOLDER_APPS -> Icons.Rounded.Folder
    WidgetCategory.MUSIC -> Icons.Rounded.MusicNote
    WidgetCategory.HEALTH -> Icons.Rounded.Favorite
    WidgetCategory.FINANCE -> Icons.Rounded.TrendingUp
    WidgetCategory.BLUETOOTH -> Icons.Rounded.Bluetooth
    WidgetCategory.SOCIAL -> Icons.Rounded.Share
    WidgetCategory.GAMES -> Icons.Rounded.SportsEsports
    WidgetCategory.CAMERA -> Icons.Rounded.CameraAlt
    WidgetCategory.QUOTES -> Icons.Rounded.FormatQuote
    WidgetCategory.RELIGIOUS -> Icons.Rounded.NightsStay
    WidgetCategory.COMPASS -> Icons.Rounded.Explore
    WidgetCategory.CALCULATOR -> Icons.Rounded.Calculate
    WidgetCategory.PERSONALISE -> Icons.Rounded.Brush
}
