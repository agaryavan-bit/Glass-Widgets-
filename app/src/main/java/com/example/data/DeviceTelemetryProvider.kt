package com.example.data

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Environment
import android.os.StatFs
import android.os.SystemClock
import com.example.model.InstalledAppInfo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.max
import kotlin.math.roundToInt

data class WidgetTelemetrySnapshot(
    val hour24: String,
    val hour12: String,
    val minute: String,
    val second: String,
    val amPm: String,
    val hourInt: Int,
    val minuteInt: Int,
    val weekdayShort: String,
    val weekdayFull: String,
    val monthShort: String,
    val monthFull: String,
    val dayOfMonth: Int,
    val year: Int,
    val dayProgress: Float,
    val yearProgress: Float,
    val tokyoTime: String,
    val londonTime: String,
    val nycTime: String,
    val batteryPercent: Int,
    val isCharging: Boolean,
    val batteryTempC: Float,
    val batteryVoltageMv: Int,
    val ramUsedGb: Float,
    val ramTotalGb: Float,
    val ramPercent: Float,
    val storageUsedGb: Float,
    val storageTotalGb: Float,
    val storagePercent: Float,
    val uptimeFormatted: String,
    val cpuCores: Int,
    val networkLabel: String,
    val monthDaysCount: Int,
    val firstDayOfMonthOffset: Int
)

data class QuoteItem(val text: String, val author: String)
data class MusicTrack(val title: String, val artist: String, val duration: String)
data class PrayerSlot(val name: String, val time: String)

/**
 * 100% offline-safe telemetry and curated sample/cached data provider.
 * Zero blocking network calls — safe for Glance provideGlance() and Compose previews.
 */
object DeviceTelemetryProvider {

    val curatedQuotes = listOf(
        QuoteItem("Simplicity is the ultimate sophistication.", "Leonardo da Vinci"),
        QuoteItem("Clarity comes from subtracting the noise.", "Dieter Rams"),
        QuoteItem("Small daily craft compounds into mastery.", "Seneca"),
        QuoteItem("Light reveals what stillness holds.", "Kahlil Gibran"),
        QuoteItem("Design is how quietly something works.", "Jony Ive"),
        QuoteItem("Begin where you stand; refine as you move.", "Marcus Aurelius")
    )

    val curatedTracks = listOf(
        MusicTrack("Liquid Horizon", "Aurora Glass", "3:42"),
        MusicTrack("Midnight Refraction", "Lumen Drift", "4:15"),
        MusicTrack("Prism Rain (Lo-Fi)", "Kairo Beats", "2:58"),
        MusicTrack("Velvet Obsidian", "Solstice FM", "3:26")
    )

    val prayerSchedule = listOf(
        PrayerSlot("Fajr", "05:18"),
        PrayerSlot("Dhuhr", "12:24"),
        PrayerSlot("Asr", "15:46"),
        PrayerSlot("Maghrib", "18:12"),
        PrayerSlot("Isha", "19:34")
    )

    val defaultChecklistItems = listOf(
        "Review glass widgets",
        "Deep focus 25m sprint",
        "Hydrate 8 cups water",
        "Evening walk 6k steps",
        "Plan tomorrow priorities"
    )

    val reflectionVerses = listOf(
        "Verily, with hardship comes ease.",
        "Be still and find quiet strength within.",
        "Gratitude turns what we have into enough.",
        "Walk gently and bring light to every room."
    )

    fun captureSnapshot(context: Context): WidgetTelemetrySnapshot {
        val now = Date()
        val cal = Calendar.getInstance()
        cal.time = now

        val hour24Int = cal.get(Calendar.HOUR_OF_DAY)
        val hour12Int = cal.get(Calendar.HOUR).let { if (it == 0) 12 else it }
        val minuteInt = cal.get(Calendar.MINUTE)
        val secondInt = cal.get(Calendar.SECOND)

        val dayProgress = ((hour24Int * 60 + minuteInt) / 1440f).coerceIn(0f, 1f)
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
        val daysInYear = cal.getActualMaximum(Calendar.DAY_OF_YEAR).coerceAtLeast(365)
        val yearProgress = (dayOfYear.toFloat() / daysInYear.toFloat()).coerceIn(0f, 1f)

        val firstCal = (cal.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }
        val firstDayOffset = (firstCal.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY).coerceIn(0, 6)
        val monthDaysCount = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Real Battery telemetry (offline-safe sticky intent)
        var batteryPct = 84
        var isCharging = false
        var batteryTempC = 31.4f
        var batteryVoltageMv = 4180
        runCatching {
            val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            if (intent != null) {
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
                if (level >= 0 && scale > 0) {
                    batteryPct = ((level * 100f) / scale.toFloat()).roundToInt().coerceIn(0, 100)
                }
                val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL
                val tempTenths = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 314)
                batteryTempC = tempTenths / 10f
                batteryVoltageMv = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 4180)
            }
        }

        // Real RAM telemetry via ActivityManager
        var ramUsedGb = 4.2f
        var ramTotalGb = 8.0f
        runCatching {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            if (am != null) {
                val memInfo = ActivityManager.MemoryInfo()
                am.getMemoryInfo(memInfo)
                if (memInfo.totalMem > 0) {
                    ramTotalGb = memInfo.totalMem / (1024f * 1024f * 1024f)
                    val availGb = memInfo.availMem / (1024f * 1024f * 1024f)
                    ramUsedGb = max(0.1f, ramTotalGb - availGb)
                }
            }
        }

        // Real Internal Storage via StatFs
        var storageUsedGb = 64.5f
        var storageTotalGb = 128.0f
        runCatching {
            val stat = StatFs(Environment.getDataDirectory().path)
            val totalBytes = stat.blockCountLong * stat.blockSizeLong
            val availBytes = stat.availableBlocksLong * stat.blockSizeLong
            if (totalBytes > 0) {
                storageTotalGb = totalBytes / (1024f * 1024f * 1024f)
                storageUsedGb = max(0.5f, (totalBytes - availBytes) / (1024f * 1024f * 1024f))
            }
        }

        // Real Uptime
        val uptimeMs = SystemClock.elapsedRealtime()
        val uptimeHours = (uptimeMs / 3_600_000L).toInt()
        val uptimeMins = ((uptimeMs % 3_600_000L) / 60_000L).toInt()
        val uptimeFormatted = if (uptimeHours >= 24) {
            "${uptimeHours / 24}d ${uptimeHours % 24}h"
        } else {
            "${uptimeHours}h ${uptimeMins}m"
        }

        val cpuCores = Runtime.getRuntime().availableProcessors().coerceAtLeast(4)

        return WidgetTelemetrySnapshot(
            hour24 = hour24Int.toString().padStart(2, '0'),
            hour12 = hour12Int.toString().padStart(2, '0'),
            minute = minuteInt.toString().padStart(2, '0'),
            second = secondInt.toString().padStart(2, '0'),
            amPm = if (hour24Int < 12) "AM" else "PM",
            hourInt = hour24Int,
            minuteInt = minuteInt,
            weekdayShort = SimpleDateFormat("EEE", Locale.getDefault()).format(now).uppercase(),
            weekdayFull = SimpleDateFormat("EEEE", Locale.getDefault()).format(now),
            monthShort = SimpleDateFormat("MMM", Locale.getDefault()).format(now).uppercase(),
            monthFull = SimpleDateFormat("MMMM", Locale.getDefault()).format(now),
            dayOfMonth = cal.get(Calendar.DAY_OF_MONTH),
            year = cal.get(Calendar.YEAR),
            dayProgress = dayProgress,
            yearProgress = yearProgress,
            tokyoTime = formatZoneTime(now, "Asia/Tokyo"),
            londonTime = formatZoneTime(now, "Europe/London"),
            nycTime = formatZoneTime(now, "America/New_York"),
            batteryPercent = batteryPct,
            isCharging = isCharging,
            batteryTempC = batteryTempC,
            batteryVoltageMv = batteryVoltageMv,
            ramUsedGb = ramUsedGb,
            ramTotalGb = ramTotalGb,
            ramPercent = (ramUsedGb / ramTotalGb.coerceAtLeast(1f)).coerceIn(0f, 1f),
            storageUsedGb = storageUsedGb,
            storageTotalGb = storageTotalGb,
            storagePercent = (storageUsedGb / storageTotalGb.coerceAtLeast(1f)).coerceIn(0f, 1f),
            uptimeFormatted = uptimeFormatted,
            cpuCores = cpuCores,
            networkLabel = "Wi-Fi 6",
            monthDaysCount = monthDaysCount,
            firstDayOfMonthOffset = firstDayOffset
        )
    }

    private fun formatZoneTime(date: Date, zoneId: String): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone(zoneId)
        return sdf.format(date)
    }

    fun getDaysUntil(targetEpochMillis: Long): Int {
        val diff = targetEpochMillis - System.currentTimeMillis()
        return (diff / 86_400_000L).toInt().coerceAtLeast(0)
    }

    fun queryLaunchableApps(context: Context): List<InstalledAppInfo> {
        return runCatching {
            val pm = context.packageManager
            val intent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
            val resolved = pm.queryIntentActivities(intent, 0)
            resolved.mapNotNull { info ->
                val pkg = info.activityInfo?.packageName ?: return@mapNotNull null
                val label = info.loadLabel(pm)?.toString()?.trim().orEmpty().ifEmpty { pkg }
                InstalledAppInfo(packageName = pkg, label = label)
            }
                .distinctBy { it.packageName }
                .sortedBy { it.label.lowercase() }
        }.getOrElse { emptyList() }
    }
}
