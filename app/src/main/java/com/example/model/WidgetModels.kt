package com.example.model

enum class WidgetCategory(
    val id: String,
    val displayName: String,
    val shortName: String,
    val defaultAccentArgb: Long,
    val tagline: String
) {
    CLOCK_DIGITAL("clock_digital", "Clock Digital", "Digital", 0xFF38BDF8, "Precision digital timepieces"),
    CLOCK_ANALOG("clock_analog", "Clock Analog", "Analog", 0xFFA78BFA, "Sculpted glass watch dials"),
    CLOCK_HYBRID("clock_hybrid", "Clock Hybrid", "Hybrid", 0xFF60A5FA, "Analog + digital desk docks"),
    CALENDAR("calendar", "Calendar", "Calendar", 0xFFFB7185, "Today, week strips & year rings"),
    QUICK_SETTINGS("quick_settings", "Quick Settings", "Controls", 0xFF2DD4BF, "Instant hardware toggles"),
    GOOGLE("google", "Google", "Google", 0xFF4285F4, "Search bars & Workspace tiles"),
    PHOTO("photo", "Photo", "Photos", 0xFFF472B6, "Frosted frames & collages"),
    WEATHER("weather", "Weather", "Weather", 0xFF38BDF8, "Forecast pills & atmospheric cards"),
    DEVICE("device", "Device", "Device", 0xFF34D399, "RAM, storage & uptime telemetry"),
    BATTERY("battery", "Battery", "Battery", 0xFF22C55E, "Power rings & voltage meters"),
    CONTACTS("contacts", "Contacts", "Contacts", 0xFFF97316, "1-tap glass speed dial avatars"),
    AI("ai", "AI", "AI Hub", 0xFF818CF8, "Prompt bars & AI launchers"),
    PRODUCTIVITY("productivity", "Productivity", "Focus", 0xFFFBBF24, "Pomodoro, streaks & countdowns"),
    NOTES_TODO("notes_todo", "Notes / To-Do", "Notes", 0xFFFDE047, "Sticky glass notes & checklists"),
    APP_LAUNCHER("app_launcher", "App Launcher", "Apps", 0xFF22D3EE, "Custom glass app docks"),
    FOLDER_APPS("folder_apps", "Folder Apps", "Folders", 0xFFA855F7, "Grouped 4 & 9-app glass folders"),
    MUSIC("music", "Music", "Music", 0xFFEC4899, "Now-playing glass decks"),
    HEALTH("health", "Health", "Health", 0xFF10B981, "Steps, water & mindful breathing"),
    FINANCE("finance", "Finance", "Finance", 0xFFF59E0B, "Crypto, FX, gold & market tickers"),
    BLUETOOTH("bluetooth", "Bluetooth", "Bluetooth", 0xFF3B82F6, "Audio gear & paired device pills"),
    SOCIAL("social", "Social", "Social", 0xFFE879F9, "Quick messaging & social hubs"),
    GAMES("games", "Games", "Games", 0xFFEF4444, "Dice, coin flip & mini-widgets"),
    CAMERA("camera", "Camera", "Camera", 0xFF06B6D4, "1-tap photo, video & QR modes"),
    QUOTES("quotes", "Quotes", "Quotes", 0xFFEAB308, "Editorial typography reflections"),
    RELIGIOUS("religious", "Religious", "Faith", 0xFF14B8A6, "Prayer times & reflection verses"),
    COMPASS("compass", "Compass", "Compass", 0xFFFB923C, "Glass heading & cardinal dials"),
    CALCULATOR("calculator", "Calculator", "Calc", 0xFF6366F1, "Interactive keypad & tip tools"),
    PERSONALISE("personalise", "Personalise", "Custom", 0xFFF43F5E, "Typography banners, QR & badges")
}

enum class WidgetSlotSize(
    val id: String,
    val label: String,
    val shortBadge: String,
    val cellCols: Int,
    val cellRows: Int,
    val previewAspectRatio: Float
) {
    SIZE_2X2("2x2", "Glass 2x2", "2×2", 2, 2, 1.0f),
    SIZE_4X1("4x1", "Glass 4x1", "4×1", 4, 1, 2.35f),
    SIZE_4X2("4x2", "Glass 4x2", "4×2", 4, 2, 1.80f),
    SIZE_4X4("4x4", "Glass 4x4", "4×4", 4, 4, 1.0f)
}

enum class GlassTheme(val id: String, val label: String) {
    LIGHT_FROSTED("light_frosted", "Light Frosted"),
    DARK_OBSIDIAN("dark_obsidian", "Dark Obsidian"),
    MATERIAL_YOU("material_you", "Material You"),
    CUSTOM("custom", "Custom")
}

enum class TypographyStyle(val id: String, val label: String) {
    GEOMETRIC_SANS("geometric_sans", "Geometric"),
    EDITORIAL_SERIF("editorial_serif", "Serif"),
    TECHNICAL_MONO("technical_mono", "Mono"),
    DISPLAY_BOLD("display_bold", "Bold Display"),
    MINIMAL_LIGHT("minimal_light", "Minimal")
}

enum class WidgetAlignment(val id: String) {
    START("start"),
    CENTER("center"),
    END("end"),
    SPACE_BETWEEN("space_between")
}

enum class WidgetDensity(val id: String) {
    AIRY("airy"),
    BALANCED("balanced"),
    COMPACT("compact")
}

enum class GlassFinish(val id: String, val label: String) {
    CLEAR_LIQUID("clear_liquid", "Clear Liquid"),
    FROSTED_MILK("frosted_milk", "Frosted Silk"),
    OBSIDIAN_SMOKE("obsidian_smoke", "Obsidian Smoke"),
    PRISM_TINT("prism_tint", "Prism Tint"),
    AURORA_GLOW("aurora_glow", "Aurora Glow")
}

enum class BorderStyle(val id: String) {
    HAIRLINE("hairline"),
    FROSTED_RIM("frosted_rim"),
    LUMINOUS_EDGE("luminous_edge"),
    DOUBLE_GLASS("double_glass"),
    ACCENT_GLOW("accent_glow")
}

enum class AccentMode(val id: String) {
    SUBTLE_TINT("subtle_tint"),
    VIVID_BADGE("vivid_badge"),
    GRADIENT_WASH("gradient_wash"),
    MONOCHROME("monochrome"),
    DUOTONE("duotone")
}

data class WidgetPresetSpec(
    val id: String,
    val category: WidgetCategory,
    val indexInCategory: Int,
    val name: String,
    val subtitle: String,
    val recommendedSize: WidgetSlotSize,
    val layoutVariant: Int,
    val typographyStyle: TypographyStyle,
    val alignment: WidgetAlignment,
    val density: WidgetDensity,
    val glassFinish: GlassFinish,
    val borderStyle: BorderStyle,
    val showIcon: Boolean,
    val showProgress: Boolean,
    val accentMode: AccentMode,
    val defaultAccentArgb: Long,
    val defaultTheme: GlassTheme,
    val tags: List<String> = emptyList()
)

data class WidgetInstanceConfig(
    val appWidgetId: Int = -1,
    val presetId: String = "clock_digital_01",
    val category: WidgetCategory = WidgetCategory.CLOCK_DIGITAL,
    val glassTheme: GlassTheme = GlassTheme.DARK_OBSIDIAN,
    val opacity: Float = 0.42f,
    val cornerRadiusDp: Int = 24,
    val accentColorArgb: Long = 0xFF38BDF8,
    val textColorArgb: Long = 0xFFFFFFFF,
    val customText: String = "",
    val targetDateEpochMillis: Long = System.currentTimeMillis() + 14L * 86_400_000L,
    val selectedApps: List<String> = emptyList(),
    val photoUri: String = ""
)

data class WidgetInteractiveState(
    val checklistMask: Int = 0b00101,
    val waterCount: Int = 5,
    val habitStreak: Int = 12,
    val quoteIndex: Int = 0,
    val diceValue: Int = 6,
    val coinHeads: Boolean = true,
    val tapCounter: Int = 24,
    val tttBoard: String = "X_O_X____",
    val calcDisplay: String = "0",
    val calcPendingOp: String = "",
    val calcAccumulator: Double = 0.0,
    val pomodoroRunning: Boolean = false,
    val pomodoroMinutes: Int = 25,
    val musicPlaying: Boolean = true,
    val musicTrackIndex: Int = 0
)

data class InstalledAppInfo(
    val packageName: String,
    val label: String
)
