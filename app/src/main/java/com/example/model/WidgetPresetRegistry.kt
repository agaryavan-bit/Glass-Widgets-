package com.example.model

/**
 * Central Data-Driven 840-Preset Engine.
 * Procedurally registers EXACTLY 30 distinct WidgetPresetSpec entries for EACH of the
 * 28 categories (28 x 30 = 840 total presets) without duplicating composable boilerplate.
 */
object WidgetPresetRegistry {

    const val PRESETS_PER_CATEGORY = 30
    const val TOTAL_EXPECTED_PRESETS = 28 * PRESETS_PER_CATEGORY // 840

    private data class ArchetypeBlueprint(
        val baseName: String,
        val subtitle: String,
        val preferredSize: WidgetSlotSize,
        val defaultShowIcon: Boolean,
        val defaultShowProgress: Boolean,
        val keywords: List<String>
    )

    private data class EditionProfile(
        val suffix: String,
        val theme: GlassTheme,
        val finish: GlassFinish,
        val border: BorderStyle,
        val accentMode: AccentMode,
        val density: WidgetDensity
    )

    private val editions = listOf(
        EditionProfile(
            suffix = "Frosted",
            theme = GlassTheme.LIGHT_FROSTED,
            finish = GlassFinish.FROSTED_MILK,
            border = BorderStyle.FROSTED_RIM,
            accentMode = AccentMode.SUBTLE_TINT,
            density = WidgetDensity.AIRY
        ),
        EditionProfile(
            suffix = "Obsidian",
            theme = GlassTheme.DARK_OBSIDIAN,
            finish = GlassFinish.OBSIDIAN_SMOKE,
            border = BorderStyle.LUMINOUS_EDGE,
            accentMode = AccentMode.VIVID_BADGE,
            density = WidgetDensity.BALANCED
        ),
        EditionProfile(
            suffix = "Prism",
            theme = GlassTheme.MATERIAL_YOU,
            finish = GlassFinish.AURORA_GLOW,
            border = BorderStyle.ACCENT_GLOW,
            accentMode = AccentMode.GRADIENT_WASH,
            density = WidgetDensity.COMPACT
        )
    )

    private val categoryBlueprints: Map<WidgetCategory, List<ArchetypeBlueprint>> = mapOf(
        WidgetCategory.CLOCK_DIGITAL to listOf(
            ArchetypeBlueprint("Horizon Minimal", "Clean floating digital time", WidgetSlotSize.SIZE_4X1, false, false, listOf("minimal", "clean", "time")),
            ArchetypeBlueprint("Bold Editorial", "Oversized geometric numerals", WidgetSlotSize.SIZE_2X2, false, false, listOf("bold", "large", "clock")),
            ArchetypeBlueprint("Stacked Vertical", "Hours over minutes column", WidgetSlotSize.SIZE_2X2, true, false, listOf("stacked", "vertical")),
            ArchetypeBlueprint("Split-Flip Deck", "Retro split-flap glass tiles", WidgetSlotSize.SIZE_4X2, false, false, listOf("flip", "split", "retro")),
            ArchetypeBlueprint("Seconds Pill", "Live seconds bar & colon", WidgetSlotSize.SIZE_4X1, true, true, listOf("seconds", "pill", "live")),
            ArchetypeBlueprint("World Dual-Zone", "Local + Tokyo & London clocks", WidgetSlotSize.SIZE_4X2, true, false, listOf("world", "timezone", "dual")),
            ArchetypeBlueprint("Date & Day Strip", "Digital clock with full date", WidgetSlotSize.SIZE_4X2, true, true, listOf("date", "combo", "day")),
            ArchetypeBlueprint("Mono Chrono", "Technical monospace readout", WidgetSlotSize.SIZE_2X2, false, true, listOf("mono", "tech", "chrono")),
            ArchetypeBlueprint("Desk Stand Wide", "Bedside landscape dock clock", WidgetSlotSize.SIZE_4X2, true, true, listOf("desk", "dock", "bedside")),
            ArchetypeBlueprint("Aurora Studio 4x4", "Full digital clock & daylight arc", WidgetSlotSize.SIZE_4X4, true, true, listOf("studio", "dashboard", "daylight"))
        ),
        WidgetCategory.CLOCK_ANALOG to listOf(
            ArchetypeBlueprint("Minimal Glass Dial", "Pure hands on frosted disc", WidgetSlotSize.SIZE_2X2, false, false, listOf("minimal", "dial", "analog")),
            ArchetypeBlueprint("Liquid Ring Halo", "Luminous double bezel watch", WidgetSlotSize.SIZE_2X2, false, true, listOf("ring", "halo", "glass")),
            ArchetypeBlueprint("Precision Tick Marks", "60-tick Swiss chronometer", WidgetSlotSize.SIZE_2X2, false, false, listOf("ticks", "swiss", "markers")),
            ArchetypeBlueprint("Roman Heritage", "Classic Roman numeral dial", WidgetSlotSize.SIZE_2X2, false, false, listOf("roman", "classic", "serif")),
            ArchetypeBlueprint("Dual-Zone Twin", "Side-by-side home & GMT dials", WidgetSlotSize.SIZE_4X2, true, false, listOf("dual", "world", "twin")),
            ArchetypeBlueprint("Skeleton Tourbillon", "Open-frame glass movement", WidgetSlotSize.SIZE_2X2, true, true, listOf("skeleton", "open", "tourbillon")),
            ArchetypeBlueprint("Polaris Minimal", "North-star 12 index marker", WidgetSlotSize.SIZE_2X2, true, false, listOf("polaris", "dot", "minimal")),
            ArchetypeBlueprint("Chronograph Sub-Dial", "Analog face with day gauge", WidgetSlotSize.SIZE_4X2, true, true, listOf("chrono", "subdial")),
            ArchetypeBlueprint("Floating Pill Dial", "Compact dial + weekday badge", WidgetSlotSize.SIZE_4X1, true, false, listOf("pill", "bar", "dial")),
            ArchetypeBlueprint("Grand Atelier 4x4", "Large architectural wall clock", WidgetSlotSize.SIZE_4X4, true, true, listOf("grand", "wall", "large"))
        ),
        WidgetCategory.CLOCK_HYBRID to listOf(
            ArchetypeBlueprint("Analog + Digital Bar", "Dial beside crisp time readout", WidgetSlotSize.SIZE_4X2, true, false, listOf("hybrid", "analog", "digital")),
            ArchetypeBlueprint("Clock + Battery Arc", "Timepiece with live power %", WidgetSlotSize.SIZE_4X2, true, true, listOf("battery", "power", "hybrid")),
            ArchetypeBlueprint("Clock + Date Ribbon", "Unified time and calendar pill", WidgetSlotSize.SIZE_4X1, true, false, listOf("date", "strip", "ribbon")),
            ArchetypeBlueprint("Desk Dock Pro", "Nightstand hybrid status hub", WidgetSlotSize.SIZE_4X2, true, true, listOf("desk", "dock", "stand")),
            ArchetypeBlueprint("Compact Hybrid Cube", "Mini dial with digital badge", WidgetSlotSize.SIZE_2X2, true, true, listOf("cube", "compact")),
            ArchetypeBlueprint("Dual Time + Weather", "Time, dial & local sky glance", WidgetSlotSize.SIZE_4X2, true, false, listOf("weather", "dual")),
            ArchetypeBlueprint("Focus Clock Bar", "Hybrid time with day progress", WidgetSlotSize.SIZE_4X1, false, true, listOf("progress", "day")),
            ArchetypeBlueprint("Executive Folio", "Dial, digital time & schedule", WidgetSlotSize.SIZE_4X4, true, true, listOf("executive", "dashboard")),
            ArchetypeBlueprint("Split Horizon Glass", "Top digital + bottom analog ring", WidgetSlotSize.SIZE_2X2, false, true, listOf("split", "horizon")),
            ArchetypeBlueprint("Command Center 4x4", "Clock, battery, date & quick info", WidgetSlotSize.SIZE_4X4, true, true, listOf("command", "center"))
        ),
        WidgetCategory.CALENDAR to listOf(
            ArchetypeBlueprint("Today Glass Card", "Bold day number & weekday", WidgetSlotSize.SIZE_2X2, true, false, listOf("today", "day", "date")),
            ArchetypeBlueprint("Week Horizon Strip", "7-day glass pill ribbon", WidgetSlotSize.SIZE_4X1, false, true, listOf("week", "strip", "7day")),
            ArchetypeBlueprint("Mini Month Grid", "Full translucent month view", WidgetSlotSize.SIZE_4X4, true, false, listOf("month", "grid", "calendar")),
            ArchetypeBlueprint("Upcoming Agenda", "Next meetings & time badges", WidgetSlotSize.SIZE_4X2, true, false, listOf("event", "agenda", "upcoming")),
            ArchetypeBlueprint("Day Progress Ring", "Hours elapsed & remaining", WidgetSlotSize.SIZE_2X2, true, true, listOf("day", "progress", "ring")),
            ArchetypeBlueprint("Year Progress Bar", "Percentage of year completed", WidgetSlotSize.SIZE_4X1, true, true, listOf("year", "progress", "bar")),
            ArchetypeBlueprint("Split Date & Events", "Today tile beside event list", WidgetSlotSize.SIZE_4X2, true, true, listOf("split", "events")),
            ArchetypeBlueprint("Compact Month Square", "Tight glass calendar matrix", WidgetSlotSize.SIZE_2X2, false, false, listOf("compact", "month")),
            ArchetypeBlueprint("Milestone Countdown", "Days left to your target date", WidgetSlotSize.SIZE_4X2, true, true, listOf("countdown", "milestone")),
            ArchetypeBlueprint("Planner Studio 4x4", "Month grid + agenda + year bar", WidgetSlotSize.SIZE_4X4, true, true, listOf("planner", "studio"))
        ),
        WidgetCategory.QUICK_SETTINGS to listOf(
            ArchetypeBlueprint("Control Pill Quad", "Wi-Fi, BT, Sound & Brightness", WidgetSlotSize.SIZE_4X1, true, false, listOf("wifi", "bluetooth", "controls")),
            ArchetypeBlueprint("2x2 Tile Matrix", "Four frosted hardware tiles", WidgetSlotSize.SIZE_2X2, true, false, listOf("matrix", "tiles", "toggles")),
            ArchetypeBlueprint("Connectivity Bar", "Wi-Fi, Mobile, Airplane & Hotspot", WidgetSlotSize.SIZE_4X1, true, false, listOf("network", "airplane", "wifi")),
            ArchetypeBlueprint("Torch & Sound Dock", "Flashlight, Ringer & DND", WidgetSlotSize.SIZE_4X2, true, true, listOf("flashlight", "sound", "dnd")),
            ArchetypeBlueprint("Display & Rotate", "Brightness, Auto-rotate & Theme", WidgetSlotSize.SIZE_4X2, true, true, listOf("brightness", "rotate", "display")),
            ArchetypeBlueprint("Control Center 4x4", "Full 8-tile glass control deck", WidgetSlotSize.SIZE_4X4, true, true, listOf("control center", "full")),
            ArchetypeBlueprint("Wi-Fi Focus Tile", "Dedicated wireless quick launch", WidgetSlotSize.SIZE_2X2, true, false, listOf("wifi", "shortcut")),
            ArchetypeBlueprint("Night Mode Bar", "DND, Brightness & Sound pills", WidgetSlotSize.SIZE_4X1, true, false, listOf("night", "quiet")),
            ArchetypeBlueprint("Power & Radio Deck", "Airplane, BT, Battery & Settings", WidgetSlotSize.SIZE_4X2, true, true, listOf("radio", "settings")),
            ArchetypeBlueprint("Minimal Toggle Trio", "Three clean icon capsules", WidgetSlotSize.SIZE_4X1, true, false, listOf("trio", "minimal"))
        ),
        WidgetCategory.GOOGLE to listOf(
            ArchetypeBlueprint("Liquid Search Bar", "Frosted Google Search + Mic", WidgetSlotSize.SIZE_4X1, true, false, listOf("search", "google", "bar")),
            ArchetypeBlueprint("Workspace Quad", "Gmail, Drive, Calendar & Docs", WidgetSlotSize.SIZE_2X2, true, false, listOf("gmail", "drive", "workspace")),
            ArchetypeBlueprint("Explore Strip", "Search, Maps, YouTube & Photos", WidgetSlotSize.SIZE_4X1, true, false, listOf("maps", "youtube", "photos")),
            ArchetypeBlueprint("Gemini + Search Dock", "Quick Google & Gemini launcher", WidgetSlotSize.SIZE_4X2, true, false, listOf("gemini", "search", "ai")),
            ArchetypeBlueprint("Maps Commute Tile", "1-tap Google Maps navigation", WidgetSlotSize.SIZE_2X2, true, false, listOf("maps", "commute")),
            ArchetypeBlueprint("Gmail & Calendar Duo", "Inbox + schedule quick pair", WidgetSlotSize.SIZE_4X2, true, false, listOf("gmail", "calendar")),
            ArchetypeBlueprint("YouTube & Photos Card", "Media & memories glass dock", WidgetSlotSize.SIZE_4X2, true, false, listOf("youtube", "photos")),
            ArchetypeBlueprint("Google Hub 4x4", "Search bar + 8 Google app tiles", WidgetSlotSize.SIZE_4X4, true, false, listOf("hub", "all", "suite")),
            ArchetypeBlueprint("Voice & Lens Pill", "Search with Voice & Lens shortcuts", WidgetSlotSize.SIZE_4X1, true, false, listOf("voice", "lens", "mic")),
            ArchetypeBlueprint("Drive Cloud Tile", "Quick access to Drive files", WidgetSlotSize.SIZE_2X2, true, true, listOf("drive", "cloud"))
        ),
        WidgetCategory.PHOTO to listOf(
            ArchetypeBlueprint("Full-Bleed Glass Frame", "Edge-to-edge photo with rim", WidgetSlotSize.SIZE_2X2, false, false, listOf("frame", "photo", "clean")),
            ArchetypeBlueprint("Polaroid Memory", "Classic white-lip glass caption", WidgetSlotSize.SIZE_2X2, false, false, listOf("polaroid", "caption", "memory")),
            ArchetypeBlueprint("Cinema Pill Strip", "Wide panoramic photo banner", WidgetSlotSize.SIZE_4X1, false, false, listOf("pill", "panoramic", "wide")),
            ArchetypeBlueprint("Dual Collage Deck", "Two framed memories side-by-side", WidgetSlotSize.SIZE_4X2, false, false, listOf("collage", "dual", "grid")),
            ArchetypeBlueprint("Captioned Journal", "Photo with date & glass note", WidgetSlotSize.SIZE_4X2, true, false, listOf("journal", "caption", "date")),
            ArchetypeBlueprint("Gallery Mosaic 4x4", "Editorial 3-frame photo collage", WidgetSlotSize.SIZE_4X4, false, false, listOf("mosaic", "collage", "4x4")),
            ArchetypeBlueprint("Frosted Vignette", "Soft translucent glass border", WidgetSlotSize.SIZE_2X2, false, false, listOf("vignette", "border")),
            ArchetypeBlueprint("Memory Postcard", "Landscape card with title tag", WidgetSlotSize.SIZE_4X2, true, false, listOf("postcard", "travel")),
            ArchetypeBlueprint("Circle Medallion", "Circular glass portrait frame", WidgetSlotSize.SIZE_2X2, false, false, listOf("circle", "portrait")),
            ArchetypeBlueprint("Film Strip Trio", "Three rounded frames in a bar", WidgetSlotSize.SIZE_4X1, false, false, listOf("film", "strip", "trio"))
        ),
        WidgetCategory.WEATHER to listOf(
            ArchetypeBlueprint("Current Temp Pill", "Live sky icon & temperature", WidgetSlotSize.SIZE_4X1, true, false, listOf("temp", "pill", "current")),
            ArchetypeBlueprint("Atmosphere Square", "Condition, feels-like & H/L", WidgetSlotSize.SIZE_2X2, true, false, listOf("condition", "square")),
            ArchetypeBlueprint("High / Low Horizon", "Temp range bar with sun icon", WidgetSlotSize.SIZE_4X1, true, true, listOf("high", "low", "strip")),
            ArchetypeBlueprint("Hourly Forecast Deck", "Next 5 hours sky timeline", WidgetSlotSize.SIZE_4X2, true, false, listOf("hourly", "forecast")),
            ArchetypeBlueprint("5-Day Outlook", "Five-day frosted weather row", WidgetSlotSize.SIZE_4X2, true, false, listOf("5-day", "weekly")),
            ArchetypeBlueprint("UV & Humidity Gauge", "Atmospheric comfort telemetry", WidgetSlotSize.SIZE_2X2, true, true, listOf("uv", "humidity", "air")),
            ArchetypeBlueprint("Wind & Dew Card", "Wind speed, gusts & pressure", WidgetSlotSize.SIZE_4X2, true, true, listOf("wind", "pressure")),
            ArchetypeBlueprint("Sky Dashboard 4x4", "Current + hourly + 5-day + UV", WidgetSlotSize.SIZE_4X4, true, true, listOf("dashboard", "full", "weather")),
            ArchetypeBlueprint("Minimal Degree", "Giant frosted temperature only", WidgetSlotSize.SIZE_2X2, false, false, listOf("minimal", "giant")),
            ArchetypeBlueprint("Sunrise & Sunset Arc", "Golden hour daylight tracker", WidgetSlotSize.SIZE_4X2, true, true, listOf("sunrise", "sunset", "sun"))
        ),
        WidgetCategory.DEVICE to listOf(
            ArchetypeBlueprint("RAM Usage Gauge", "Live memory ring & GB readout", WidgetSlotSize.SIZE_2X2, true, true, listOf("ram", "memory", "live")),
            ArchetypeBlueprint("Storage StatFs Card", "Internal storage bar via StatFs", WidgetSlotSize.SIZE_4X2, true, true, listOf("storage", "disk", "statfs")),
            ArchetypeBlueprint("System Pill Bar", "RAM, Storage & Network at glance", WidgetSlotSize.SIZE_4X1, true, true, listOf("pill", "system", "bar")),
            ArchetypeBlueprint("CPU & Uptime Meter", "Cores, uptime & thermal glance", WidgetSlotSize.SIZE_2X2, true, true, listOf("cpu", "uptime")),
            ArchetypeBlueprint("Network Telemetry", "Active Wi-Fi/Cellular & link", WidgetSlotSize.SIZE_4X1, true, false, listOf("network", "wifi", "5g")),
            ArchetypeBlueprint("Dual Core Monitor", "RAM + Storage split meters", WidgetSlotSize.SIZE_4X2, true, true, listOf("dual", "ram", "storage")),
            ArchetypeBlueprint("Hardware Matrix 4x4", "Complete RAM, Disk, CPU, Net", WidgetSlotSize.SIZE_4X4, true, true, listOf("dashboard", "telemetry")),
            ArchetypeBlueprint("Uptime Chrono Tile", "Time since last device boot", WidgetSlotSize.SIZE_2X2, true, false, listOf("uptime", "boot")),
            ArchetypeBlueprint("Storage Donut", "Circular free-space indicator", WidgetSlotSize.SIZE_2X2, false, true, listOf("donut", "storage")),
            ArchetypeBlueprint("Diagnostics Strip", "4-metric glass status bar", WidgetSlotSize.SIZE_4X2, true, true, listOf("diagnostics", "metrics"))
        ),
        WidgetCategory.BATTERY to listOf(
            ArchetypeBlueprint("Power Ring Halo", "Luminous percentage ring", WidgetSlotSize.SIZE_2X2, true, true, listOf("ring", "percentage", "battery")),
            ArchetypeBlueprint("Liquid Charge Bar", "Horizontal sleek charge meter", WidgetSlotSize.SIZE_4X1, true, true, listOf("bar", "horizontal")),
            ArchetypeBlueprint("Charging Bolt Pill", "Live bolt & remaining estimate", WidgetSlotSize.SIZE_4X1, true, true, listOf("charging", "bolt", "pill")),
            ArchetypeBlueprint("Thermal & Voltage", "Battery °C, mV & health stats", WidgetSlotSize.SIZE_4X2, true, true, listOf("temp", "voltage", "health")),
            ArchetypeBlueprint("Giant Bold Percent", "Ultra-clear oversized battery %", WidgetSlotSize.SIZE_2X2, false, false, listOf("bold", "large", "readout")),
            ArchetypeBlueprint("Power Dock Card", "Ring + voltage + temperature", WidgetSlotSize.SIZE_4X2, true, true, listOf("dock", "card")),
            ArchetypeBlueprint("Segmented Cell Bar", "10-cell neon glass battery", WidgetSlotSize.SIZE_4X1, false, true, listOf("segmented", "cells")),
            ArchetypeBlueprint("Vertical Tank Gauge", "Liquid fill battery bottle", WidgetSlotSize.SIZE_2X2, true, true, listOf("tank", "liquid", "vertical")),
            ArchetypeBlueprint("Eco Battery Hub 4x4", "Full power analytics & estimate", WidgetSlotSize.SIZE_4X4, true, true, listOf("hub", "analytics")),
            ArchetypeBlueprint("Minimal Bolt Badge", "Compact icon + percent tile", WidgetSlotSize.SIZE_2X2, true, false, listOf("minimal", "badge"))
        ),
        WidgetCategory.CONTACTS to listOf(
            ArchetypeBlueprint("2x2 Speed Dial Grid", "Four frosted avatar shortcuts", WidgetSlotSize.SIZE_2X2, true, false, listOf("speed dial", "2x2", "avatars")),
            ArchetypeBlueprint("Favorites Strip 4x1", "Quick row of top contacts", WidgetSlotSize.SIZE_4X1, true, false, listOf("favorites", "strip", "row")),
            ArchetypeBlueprint("1-Tap Call Card", "Featured contact Call + SMS", WidgetSlotSize.SIZE_4X2, true, false, listOf("call", "message", "1-tap")),
            ArchetypeBlueprint("Emergency ICE Card", "High-contrast emergency contact", WidgetSlotSize.SIZE_4X2, true, false, listOf("emergency", "ice", "sos")),
            ArchetypeBlueprint("VIP Avatar Tile", "Single large contact medallion", WidgetSlotSize.SIZE_2X2, true, false, listOf("vip", "avatar")),
            ArchetypeBlueprint("Family Circle 4x4", "6-contact call & message hub", WidgetSlotSize.SIZE_4X4, true, false, listOf("family", "circle", "hub")),
            ArchetypeBlueprint("Quick Message Bar", "Direct SMS pills for favorites", WidgetSlotSize.SIZE_4X1, true, false, listOf("sms", "message", "bar")),
            ArchetypeBlueprint("Dual Direct Dial", "Two split call/text glass cards", WidgetSlotSize.SIZE_4X2, true, false, listOf("dual", "dial")),
            ArchetypeBlueprint("Work Team Strip", "Colleague quick-reach bar", WidgetSlotSize.SIZE_4X1, true, false, listOf("work", "team")),
            ArchetypeBlueprint("Contact Folio Card", "Avatar, role & action buttons", WidgetSlotSize.SIZE_4X2, true, false, listOf("folio", "buttons"))
        ),
        WidgetCategory.AI to listOf(
            ArchetypeBlueprint("Gemini Prompt Bar", "1-tap Gemini glass search pill", WidgetSlotSize.SIZE_4X1, true, false, listOf("gemini", "prompt", "ai")),
            ArchetypeBlueprint("AI Quartet Grid", "Gemini, ChatGPT, Claude & Grok", WidgetSlotSize.SIZE_2X2, true, false, listOf("chatgpt", "claude", "grok")),
            ArchetypeBlueprint("All-6 AI Dock", "Gemini, GPT, Claude, Grok, DeepSeek, Perplexity", WidgetSlotSize.SIZE_4X2, true, false, listOf("deepseek", "perplexity", "all")),
            ArchetypeBlueprint("Voice AI Capsule", "Instant voice assistant bar", WidgetSlotSize.SIZE_4X1, true, false, listOf("voice", "assistant")),
            ArchetypeBlueprint("ChatGPT Quick Tile", "Direct ChatGPT launcher square", WidgetSlotSize.SIZE_2X2, true, false, listOf("chatgpt", "tile")),
            ArchetypeBlueprint("Claude Focus Card", "Minimal Claude prompt starter", WidgetSlotSize.SIZE_4X2, true, false, listOf("claude", "writing")),
            ArchetypeBlueprint("Perplexity Answer Bar", "Research & web answer pill", WidgetSlotSize.SIZE_4X1, true, false, listOf("perplexity", "search")),
            ArchetypeBlueprint("DeepSeek & Grok Pair", "Side-by-side reasoning launch", WidgetSlotSize.SIZE_4X2, true, false, listOf("deepseek", "grok")),
            ArchetypeBlueprint("AI Command Studio 4x4", "Prompt bar + 6 AI models + tips", WidgetSlotSize.SIZE_4X4, true, true, listOf("studio", "command", "hub")),
            ArchetypeBlueprint("Minimal Spark Tile", "Clean single-tap AI orb", WidgetSlotSize.SIZE_2X2, true, false, listOf("spark", "minimal"))
        ),
        WidgetCategory.PRODUCTIVITY to listOf(
            ArchetypeBlueprint("Pomodoro Mini-Timer", "25:00 tap-to-toggle focus clock", WidgetSlotSize.SIZE_2X2, true, true, listOf("pomodoro", "timer", "focus")),
            ArchetypeBlueprint("Habit Streak Counter", "Tap +1 daily streak flame", WidgetSlotSize.SIZE_2X2, true, true, listOf("habit", "streak", "counter")),
            ArchetypeBlueprint("Event Countdown Card", "Days remaining to exam/launch", WidgetSlotSize.SIZE_4X2, true, true, listOf("countdown", "exam", "event")),
            ArchetypeBlueprint("Year Progress Pill", "Sleek annual milestone bar", WidgetSlotSize.SIZE_4X1, true, true, listOf("year", "progress", "bar")),
            ArchetypeBlueprint("Quick Bookmarks Bar", "4 frosted productivity links", WidgetSlotSize.SIZE_4X1, true, false, listOf("bookmarks", "links", "clipboard")),
            ArchetypeBlueprint("Deep Work Deck", "Pomodoro + streak combined", WidgetSlotSize.SIZE_4X2, true, true, listOf("deep work", "focus")),
            ArchetypeBlueprint("Focus Sprint Pill", "Compact horizontal timer bar", WidgetSlotSize.SIZE_4X1, true, true, listOf("sprint", "pill")),
            ArchetypeBlueprint("Goal Meter Square", "Quarterly target ring %", WidgetSlotSize.SIZE_2X2, true, true, listOf("goal", "ring", "target")),
            ArchetypeBlueprint("Productivity Hub 4x4", "Timer, streak, countdown & year", WidgetSlotSize.SIZE_4X4, true, true, listOf("hub", "dashboard")),
            ArchetypeBlueprint("Daily Anchor Card", "Single priority highlight card", WidgetSlotSize.SIZE_4X2, true, false, listOf("priority", "anchor"))
        ),
        WidgetCategory.NOTES_TODO to listOf(
            ArchetypeBlueprint("Sticky Glass Note", "Translucent memo square", WidgetSlotSize.SIZE_2X2, true, false, listOf("sticky", "note", "memo")),
            ArchetypeBlueprint("Interactive Checklist", "Tap-to-toggle 4 task items", WidgetSlotSize.SIZE_4X2, true, true, listOf("checklist", "todo", "toggle")),
            ArchetypeBlueprint("Quick-Add Note Bar", "1-tap capture & memo pill", WidgetSlotSize.SIZE_4X1, true, false, listOf("quick add", "bar", "capture")),
            ArchetypeBlueprint("Pinned Goal Card", "Highlighted north-star goal", WidgetSlotSize.SIZE_4X2, true, true, listOf("pinned", "goal")),
            ArchetypeBlueprint("5-Item Task Board 4x4", "Full interactive checklist card", WidgetSlotSize.SIZE_4X4, true, true, listOf("5-item", "board", "tasks")),
            ArchetypeBlueprint("Mini 3-Task Cube", "Compact 3-item check square", WidgetSlotSize.SIZE_2X2, false, true, listOf("3-item", "compact")),
            ArchetypeBlueprint("Glass Scratchpad", "Clean lined note canvas", WidgetSlotSize.SIZE_4X2, false, false, listOf("scratchpad", "lined")),
            ArchetypeBlueprint("Task Progress Pill", "Done count + quick tap bar", WidgetSlotSize.SIZE_4X1, true, true, listOf("progress", "count")),
            ArchetypeBlueprint("Focus Mantra Note", "Custom pinned reminder note", WidgetSlotSize.SIZE_2X2, true, false, listOf("mantra", "reminder")),
            ArchetypeBlueprint("Split Note & To-Do", "Memo beside interactive ticks", WidgetSlotSize.SIZE_4X4, true, true, listOf("split", "note", "todo"))
        ),
        WidgetCategory.APP_LAUNCHER to listOf(
            ArchetypeBlueprint("2x2 App Quad Dock", "Four chosen app shortcuts", WidgetSlotSize.SIZE_2X2, true, false, listOf("2x2", "quad", "apps")),
            ArchetypeBlueprint("4x1 Floating Dock", "Sleek 4-app glass capsule", WidgetSlotSize.SIZE_4X1, true, false, listOf("4x1", "dock", "capsule")),
            ArchetypeBlueprint("4x2 Eight-App Deck", "Two rows of favorite apps", WidgetSlotSize.SIZE_4X2, true, false, listOf("4x2", "eight", "deck")),
            ArchetypeBlueprint("1x1 Spotlight Tile", "Hero single app glass button", WidgetSlotSize.SIZE_2X2, true, false, listOf("1x1", "hero", "single")),
            ArchetypeBlueprint("Labeled App Strip", "4 apps with crisp labels", WidgetSlotSize.SIZE_4X1, true, false, listOf("labeled", "strip")),
            ArchetypeBlueprint("Minimalist Text Dock", "Typography-only app launcher", WidgetSlotSize.SIZE_4X2, false, false, listOf("text", "minimalist")),
            ArchetypeBlueprint("App Drawer Mini 4x4", "12-app translucent grid", WidgetSlotSize.SIZE_4X4, true, false, listOf("drawer", "grid", "12")),
            ArchetypeBlueprint("Split Essentials", "Primary app + 3 quick tiles", WidgetSlotSize.SIZE_4X2, true, false, listOf("essentials", "split")),
            ArchetypeBlueprint("Pill Pair Launcher", "Two wide labeled app pills", WidgetSlotSize.SIZE_4X1, true, false, listOf("pair", "pill")),
            ArchetypeBlueprint("Glass Pad 2x2", "Rounded icon pods with sheen", WidgetSlotSize.SIZE_2X2, true, false, listOf("pods", "sheen"))
        ),
        WidgetCategory.FOLDER_APPS to listOf(
            ArchetypeBlueprint("Study 4-App Folder", "Grouped learning & notes apps", WidgetSlotSize.SIZE_2X2, true, false, listOf("study", "4-app", "folder")),
            ArchetypeBlueprint("Work 9-App Folder", "Expandable 3x3 work suite", WidgetSlotSize.SIZE_4X4, true, false, listOf("work", "9-app", "3x3")),
            ArchetypeBlueprint("Media 4-App Folder", "Music, video & photo cluster", WidgetSlotSize.SIZE_2X2, true, false, listOf("media", "4-app", "folder")),
            ArchetypeBlueprint("Utilities 9-App Folder", "Tools & system 3x3 glass box", WidgetSlotSize.SIZE_4X4, true, false, listOf("utilities", "tools", "9-app")),
            ArchetypeBlueprint("Study Ribbon Bar", "Wide 4-app study capsule", WidgetSlotSize.SIZE_4X1, true, false, listOf("study", "ribbon")),
            ArchetypeBlueprint("Work Dual Folder", "Two mini folders side-by-side", WidgetSlotSize.SIZE_4X2, true, false, listOf("work", "dual")),
            ArchetypeBlueprint("Media Shelf Card", "Titled 4-app entertainment deck", WidgetSlotSize.SIZE_4X2, true, false, listOf("media", "shelf")),
            ArchetypeBlueprint("Utilities Drawer Bar", "Quick 4-tool horizontal bar", WidgetSlotSize.SIZE_4X1, true, false, listOf("utilities", "bar")),
            ArchetypeBlueprint("Smart Folder Hub 4x4", "Study + Work + Media + Tools", WidgetSlotSize.SIZE_4X4, true, false, listOf("hub", "grouped")),
            ArchetypeBlueprint("Compact Stack Folder", "Minimal 2x2 titled app cube", WidgetSlotSize.SIZE_2X2, true, false, listOf("stack", "compact"))
        ),
        WidgetCategory.MUSIC to listOf(
            ArchetypeBlueprint("Now Playing Deck", "Album art, title & transport", WidgetSlotSize.SIZE_4X2, true, true, listOf("now playing", "controls", "music")),
            ArchetypeBlueprint("Minimalist Pill", "Compact play/pause & track bar", WidgetSlotSize.SIZE_4X1, true, false, listOf("pill", "minimal", "bar")),
            ArchetypeBlueprint("Album Art Frame", "Full square cover + glass controls", WidgetSlotSize.SIZE_2X2, true, false, listOf("album", "art", "frame")),
            ArchetypeBlueprint("Vinyl Glass Studio", "Circular disc art + waveform", WidgetSlotSize.SIZE_4X2, true, true, listOf("vinyl", "disc", "wave")),
            ArchetypeBlueprint("Transport Cube", "Play, skip & track badge", WidgetSlotSize.SIZE_2X2, true, true, listOf("transport", "cube")),
            ArchetypeBlueprint("Hi-Fi Dashboard 4x4", "Large cover, progress & queue", WidgetSlotSize.SIZE_4X4, true, true, listOf("hifi", "queue", "4x4")),
            ArchetypeBlueprint("Lyrics & Track Card", "Ambient lyric line + controls", WidgetSlotSize.SIZE_4X2, true, true, listOf("lyrics", "ambient")),
            ArchetypeBlueprint("Glass Equalizer Bar", "Audio visualizer pill + skip", WidgetSlotSize.SIZE_4X1, true, true, listOf("equalizer", "visualizer")),
            ArchetypeBlueprint("Cover Overlay Card", "Frosted transport over artwork", WidgetSlotSize.SIZE_4X2, true, false, listOf("overlay", "cover")),
            ArchetypeBlueprint("Mini Play Button", "Bold play/pause square tile", WidgetSlotSize.SIZE_2X2, true, false, listOf("play", "mini"))
        ),
        WidgetCategory.HEALTH to listOf(
            ArchetypeBlueprint("Step Goal Ring", "Daily steps arc & % target", WidgetSlotSize.SIZE_2X2, true, true, listOf("steps", "ring", "goal")),
            ArchetypeBlueprint("Water Intake Tapper", "Interactive +1 glass cup counter", WidgetSlotSize.SIZE_2X2, true, true, listOf("water", "hydration", "tap")),
            ArchetypeBlueprint("Wellness Strip 4x1", "Steps, Water & Sleep summary", WidgetSlotSize.SIZE_4X1, true, true, listOf("wellness", "strip")),
            ArchetypeBlueprint("Mindful Breath Card", "4-7-8 calm breathing cue", WidgetSlotSize.SIZE_4X2, true, true, listOf("breathing", "mindful", "calm")),
            ArchetypeBlueprint("Sleep Recovery Card", "Hours slept & sleep quality", WidgetSlotSize.SIZE_4X2, true, true, listOf("sleep", "recovery")),
            ArchetypeBlueprint("Hydration Bar 4x1", "Tap-to-add 8-cup water tracker", WidgetSlotSize.SIZE_4X1, true, true, listOf("hydration", "bar", "water")),
            ArchetypeBlueprint("Vitality Hub 4x4", "Steps, water, sleep & breath", WidgetSlotSize.SIZE_4X4, true, true, listOf("vitality", "dashboard")),
            ArchetypeBlueprint("Active Calories Tile", "Daily burn ring & streak", WidgetSlotSize.SIZE_2X2, true, true, listOf("calories", "active")),
            ArchetypeBlueprint("Steps + Water Duo", "Side-by-side step & cup cards", WidgetSlotSize.SIZE_4X2, true, true, listOf("duo", "steps", "water")),
            ArchetypeBlueprint("Posture & Move Cue", "Gentle hourly stand reminder", WidgetSlotSize.SIZE_4X1, true, false, listOf("move", "reminder"))
        ),
        WidgetCategory.FINANCE to listOf(
            ArchetypeBlueprint("Crypto Ticker Card", "BTC & ETH frosted price card", WidgetSlotSize.SIZE_4X2, true, true, listOf("crypto", "btc", "eth")),
            ArchetypeBlueprint("Currency FX Pill", "USD / EUR / GBP / JPY strip", WidgetSlotSize.SIZE_4X1, true, false, listOf("currency", "fx", "exchange")),
            ArchetypeBlueprint("Gold & Silver Tile", "Precious metals spot prices", WidgetSlotSize.SIZE_2X2, true, true, listOf("gold", "silver", "metals")),
            ArchetypeBlueprint("Market Index Deck", "S&P 500, NASDAQ & NIFTY", WidgetSlotSize.SIZE_4X2, true, true, listOf("market", "index", "stocks")),
            ArchetypeBlueprint("Bitcoin Focus Cube", "BTC live-style readout & 24h %", WidgetSlotSize.SIZE_2X2, true, true, listOf("bitcoin", "btc")),
            ArchetypeBlueprint("Portfolio Pulse 4x4", "Crypto, FX, Gold & Indices", WidgetSlotSize.SIZE_4X4, true, true, listOf("portfolio", "dashboard")),
            ArchetypeBlueprint("Mini Ticker Pill", "Compact 3-asset market bar", WidgetSlotSize.SIZE_4X1, false, false, listOf("ticker", "bar")),
            ArchetypeBlueprint("FX Converter Glance", "Quick 100 USD conversion list", WidgetSlotSize.SIZE_4X2, true, false, listOf("converter", "usd")),
            ArchetypeBlueprint("Bull / Bear Meter", "Market sentiment glass gauge", WidgetSlotSize.SIZE_2X2, true, true, listOf("sentiment", "gauge")),
            ArchetypeBlueprint("Watchlist Trio Card", "Top 3 assets with % badges", WidgetSlotSize.SIZE_4X2, true, true, listOf("watchlist", "trio"))
        ),
        WidgetCategory.BLUETOOTH to listOf(
            ArchetypeBlueprint("Earbuds Battery Card", "L/R buds & case charge bars", WidgetSlotSize.SIZE_4X2, true, true, listOf("earbuds", "battery", "audio")),
            ArchetypeBlueprint("Headphones Square", "Connected headset & battery %", WidgetSlotSize.SIZE_2X2, true, true, listOf("headphones", "bluetooth")),
            ArchetypeBlueprint("Paired Device Pill", "Active BT gear + settings tap", WidgetSlotSize.SIZE_4X1, true, true, listOf("paired", "pill")),
            ArchetypeBlueprint("Quick BT Settings", "1-tap Bluetooth panel tile", WidgetSlotSize.SIZE_2X2, true, false, listOf("settings", "shortcut")),
            ArchetypeBlueprint("Audio Gear Deck", "Buds, Watch & Speaker status", WidgetSlotSize.SIZE_4X2, true, true, listOf("watch", "speaker", "multi")),
            ArchetypeBlueprint("Bluetooth Hub 4x4", "All paired devices & controls", WidgetSlotSize.SIZE_4X4, true, true, listOf("hub", "devices")),
            ArchetypeBlueprint("Codec & Link Bar", "LDAC/AAC badge + battery %", WidgetSlotSize.SIZE_4X1, true, false, listOf("codec", "audio")),
            ArchetypeBlueprint("Dual Pod Meter", "Left & Right earbud rings", WidgetSlotSize.SIZE_2X2, true, true, listOf("left", "right", "rings")),
            ArchetypeBlueprint("Quick Pair Capsule", "Instant BT toggle & status", WidgetSlotSize.SIZE_4X1, true, false, listOf("quick", "capsule")),
            ArchetypeBlueprint("Studio Headphones", "Large glass headphone card", WidgetSlotSize.SIZE_4X2, true, true, listOf("studio", "headset"))
        ),
        WidgetCategory.SOCIAL to listOf(
            ArchetypeBlueprint("Social Quad 2x2", "WhatsApp, IG, X & Telegram", WidgetSlotSize.SIZE_2X2, true, false, listOf("whatsapp", "instagram", "x")),
            ArchetypeBlueprint("Social Pill Strip", "6 quick social launch icons", WidgetSlotSize.SIZE_4X1, true, false, listOf("strip", "social", "pill")),
            ArchetypeBlueprint("Messaging Trio", "WhatsApp, Telegram & Discord", WidgetSlotSize.SIZE_4X1, true, false, listOf("telegram", "discord", "chat")),
            ArchetypeBlueprint("Creator Deck 4x2", "Instagram, X, Reddit & Discord", WidgetSlotSize.SIZE_4X2, true, false, listOf("reddit", "creator", "deck")),
            ArchetypeBlueprint("WhatsApp Hero Tile", "1-tap glass chat launcher", WidgetSlotSize.SIZE_2X2, true, false, listOf("whatsapp", "hero")),
            ArchetypeBlueprint("Community Hub 4x4", "All 6 networks with badges", WidgetSlotSize.SIZE_4X4, true, false, listOf("hub", "all", "community")),
            ArchetypeBlueprint("Reddit & X News Bar", "Quick feeds glass capsule", WidgetSlotSize.SIZE_4X1, true, false, listOf("reddit", "news")),
            ArchetypeBlueprint("Discord & Gaming Chat", "Voice servers & DM shortcuts", WidgetSlotSize.SIZE_4X2, true, false, listOf("discord", "voice")),
            ArchetypeBlueprint("Minimal Social Labels", "Clean typographic social list", WidgetSlotSize.SIZE_4X2, false, false, listOf("minimal", "text")),
            ArchetypeBlueprint("Instagram Focus Tile", "Frosted IG quick launch", WidgetSlotSize.SIZE_2X2, true, false, listOf("instagram", "tile"))
        ),
        WidgetCategory.GAMES to listOf(
            ArchetypeBlueprint("Lucky Dice Roller", "Tap to roll 1–6 glass die", WidgetSlotSize.SIZE_2X2, true, false, listOf("dice", "roll", "random")),
            ArchetypeBlueprint("Liquid Coin Flip", "Tap for Heads or Tails", WidgetSlotSize.SIZE_2X2, true, false, listOf("coin", "flip", "heads")),
            ArchetypeBlueprint("Tap Counter Clicker", "Interactive score tapper", WidgetSlotSize.SIZE_2X2, true, true, listOf("counter", "tap", "score")),
            ArchetypeBlueprint("Tic-Tac-Toe Mini", "Playable 3x3 glass grid", WidgetSlotSize.SIZE_4X4, true, false, listOf("tic-tac-toe", "ttt", "playable")),
            ArchetypeBlueprint("Game Launcher Bar", "4-slot quick game dock", WidgetSlotSize.SIZE_4X1, true, false, listOf("launcher", "dock", "games")),
            ArchetypeBlueprint("Dice + Coin Arcade", "Dual randomizer glass deck", WidgetSlotSize.SIZE_4X2, true, false, listOf("arcade", "dual")),
            ArchetypeBlueprint("Scoreboard Dual", "Tap P1 / P2 match counter", WidgetSlotSize.SIZE_4X2, true, true, listOf("scoreboard", "match")),
            ArchetypeBlueprint("Quick Play Pill", "Instant tap counter bar", WidgetSlotSize.SIZE_4X1, true, true, listOf("quick", "pill")),
            ArchetypeBlueprint("Arcade Suite 4x4", "Dice, Coin, Counter & Games", WidgetSlotSize.SIZE_4X4, true, true, listOf("suite", "hub")),
            ArchetypeBlueprint("D20 RPG Roller", "Tabletop glass dice square", WidgetSlotSize.SIZE_2X2, true, false, listOf("rpg", "d20", "dice"))
        ),
        WidgetCategory.CAMERA to listOf(
            ArchetypeBlueprint("Camera Mode Strip", "Photo, Video, Selfie & QR", WidgetSlotSize.SIZE_4X1, true, false, listOf("photo", "video", "selfie", "qr")),
            ArchetypeBlueprint("2x2 Capture Grid", "Four 1-tap lens shortcuts", WidgetSlotSize.SIZE_2X2, true, false, listOf("capture", "2x2", "grid")),
            ArchetypeBlueprint("1-Tap Photo Shutter", "Instant camera launch tile", WidgetSlotSize.SIZE_2X2, true, false, listOf("shutter", "photo")),
            ArchetypeBlueprint("QR Scanner Pill", "Fast frosted QR & barcode", WidgetSlotSize.SIZE_4X1, true, false, listOf("qr", "scanner", "fast")),
            ArchetypeBlueprint("Creator Camera Deck", "Photo, Video, Portrait & Selfie", WidgetSlotSize.SIZE_4X2, true, false, listOf("portrait", "creator")),
            ArchetypeBlueprint("Quick Video Rec Tile", "Direct camcorder intent tile", WidgetSlotSize.SIZE_2X2, true, false, listOf("video", "record")),
            ArchetypeBlueprint("Selfie & Portrait Duo", "Front-camera quick pair", WidgetSlotSize.SIZE_4X2, true, false, listOf("selfie", "portrait")),
            ArchetypeBlueprint("Pro Lens Studio 4x4", "All 5 camera modes + gallery", WidgetSlotSize.SIZE_4X4, true, false, listOf("studio", "pro", "lens")),
            ArchetypeBlueprint("Minimal Lens Bar", "Clean 3-icon capture capsule", WidgetSlotSize.SIZE_4X1, true, false, listOf("minimal", "capsule")),
            ArchetypeBlueprint("Viewfinder Frame", "Crosshair glass camera trigger", WidgetSlotSize.SIZE_2X2, true, false, listOf("viewfinder", "frame"))
        ),
        WidgetCategory.QUOTES to listOf(
            ArchetypeBlueprint("Editorial Serif Quote", "Tap-to-cycle literary card", WidgetSlotSize.SIZE_4X2, false, false, listOf("serif", "editorial", "quote")),
            ArchetypeBlueprint("Minimal Zen Pill", "Single-line daily reflection", WidgetSlotSize.SIZE_4X1, false, false, listOf("zen", "minimal", "pill")),
            ArchetypeBlueprint("Bold Manifesto Cube", "High-impact display quote", WidgetSlotSize.SIZE_2X2, false, false, listOf("bold", "manifesto")),
            ArchetypeBlueprint("Author Spotlight", "Quote with frosted author tag", WidgetSlotSize.SIZE_4X2, true, false, listOf("author", "tag")),
            ArchetypeBlueprint("Mono Philosophy", "Typewriter monospace wisdom", WidgetSlotSize.SIZE_4X2, false, false, listOf("mono", "typewriter")),
            ArchetypeBlueprint("Daily Affirmation", "Soft centered morning mantra", WidgetSlotSize.SIZE_2X2, true, false, listOf("affirmation", "morning")),
            ArchetypeBlueprint("Stoic Reflection 4x4", "Large gallery-framed wisdom", WidgetSlotSize.SIZE_4X4, true, false, listOf("stoic", "large", "gallery")),
            ArchetypeBlueprint("Quoted Ribbon Bar", "Compact quote + next tap", WidgetSlotSize.SIZE_4X1, true, false, listOf("ribbon", "cycle")),
            ArchetypeBlueprint("Glass Bookplate", "Framed quotation marks accent", WidgetSlotSize.SIZE_2X2, true, false, listOf("bookplate", "marks")),
            ArchetypeBlueprint("Curated Muse Card", "Editorial quote & day index", WidgetSlotSize.SIZE_4X2, true, true, listOf("muse", "daily"))
        ),
        WidgetCategory.RELIGIOUS to listOf(
            ArchetypeBlueprint("Next Prayer Countdown", "Time remaining to next prayer", WidgetSlotSize.SIZE_2X2, true, true, listOf("prayer", "countdown", "next")),
            ArchetypeBlueprint("Daily Prayer Schedule", "Fajr, Dhuhr, Asr, Maghrib, Isha", WidgetSlotSize.SIZE_4X2, true, true, listOf("schedule", "times", "daily")),
            ArchetypeBlueprint("Prayer Pill Strip", "Compact next-prayer glass bar", WidgetSlotSize.SIZE_4X1, true, false, listOf("pill", "bar", "prayer")),
            ArchetypeBlueprint("Reflection Verse Card", "Serene daily sacred verse", WidgetSlotSize.SIZE_4X2, true, false, listOf("verse", "reflection")),
            ArchetypeBlueprint("Sanctuary Hub 4x4", "Full prayer table + reflection", WidgetSlotSize.SIZE_4X4, true, true, listOf("sanctuary", "full")),
            ArchetypeBlueprint("Dawn & Dusk Times", "Fajr & Maghrib glass pair", WidgetSlotSize.SIZE_4X1, true, false, listOf("fajr", "maghrib")),
            ArchetypeBlueprint("Peace Mantra Square", "Centered gratitude verse", WidgetSlotSize.SIZE_2X2, false, false, listOf("peace", "gratitude")),
            ArchetypeBlueprint("Prayer Progress Ring", "Circular countdown to next call", WidgetSlotSize.SIZE_2X2, true, true, listOf("ring", "progress")),
            ArchetypeBlueprint("Split Prayer & Verse", "Next time beside daily verse", WidgetSlotSize.SIZE_4X2, true, true, listOf("split", "verse")),
            ArchetypeBlueprint("Minimal Faith Ribbon", "Quiet serif reflection bar", WidgetSlotSize.SIZE_4X1, false, false, listOf("minimal", "serif"))
        ),
        WidgetCategory.COMPASS to listOf(
            ArchetypeBlueprint("Cardinal Glass Dial", "N/E/S/W ring with luminous needle", WidgetSlotSize.SIZE_2X2, true, false, listOf("cardinal", "dial", "compass")),
            ArchetypeBlueprint("Degree Readout Card", "Large azimuth ° & direction", WidgetSlotSize.SIZE_2X2, false, true, listOf("degree", "azimuth")),
            ArchetypeBlueprint("Heading Pill Strip", "Compact horizontal bearing tape", WidgetSlotSize.SIZE_4X1, true, false, listOf("heading", "tape", "pill")),
            ArchetypeBlueprint("Navigator Dual Deck", "Compass dial + degree & coords", WidgetSlotSize.SIZE_4X2, true, true, listOf("navigator", "dual")),
            ArchetypeBlueprint("Expedition Studio 4x4", "Full precision compass rose", WidgetSlotSize.SIZE_4X4, true, true, listOf("expedition", "rose")),
            ArchetypeBlueprint("True North Minimal", "Bold N arrow & degree badge", WidgetSlotSize.SIZE_2X2, true, false, listOf("north", "minimal")),
            ArchetypeBlueprint("Aviation Bearing Bar", "Cockpit-style heading ribbon", WidgetSlotSize.SIZE_4X1, false, true, listOf("aviation", "bearing")),
            ArchetypeBlueprint("Marine Compass Card", "360° graduated frosted dial", WidgetSlotSize.SIZE_4X2, true, false, listOf("marine", "360")),
            ArchetypeBlueprint("Trail Heading Tile", "Outdoor bearing + elevation", WidgetSlotSize.SIZE_2X2, true, true, listOf("trail", "outdoor")),
            ArchetypeBlueprint("Horizon Azimuth Bar", "Sleek cardinal direction pill", WidgetSlotSize.SIZE_4X1, true, false, listOf("horizon", "azimuth"))
        ),
        WidgetCategory.CALCULATOR to listOf(
            ArchetypeBlueprint("Interactive Keypad 4x4", "Full tap-to-calculate glass pad", WidgetSlotSize.SIZE_4X4, false, false, listOf("keypad", "interactive", "calc")),
            ArchetypeBlueprint("Compact Calc Deck", "Quick display + +/-/×/÷ keys", WidgetSlotSize.SIZE_4X2, true, false, listOf("compact", "keys")),
            ArchetypeBlueprint("Tip & Split Helper", "Instant 15% / 18% / 20% card", WidgetSlotSize.SIZE_4X2, true, false, listOf("tip", "percentage", "split")),
            ArchetypeBlueprint("Launch Calc Tile", "1-tap frosted calculator icon", WidgetSlotSize.SIZE_2X2, true, false, listOf("launch", "shortcut")),
            ArchetypeBlueprint("Percent Pill Bar", "Quick 10% / 15% / 20% / 25% bar", WidgetSlotSize.SIZE_4X1, true, false, listOf("percent", "bar")),
            ArchetypeBlueprint("Mini NumPad Cube", "Essential numbers & = tile", WidgetSlotSize.SIZE_2X2, false, false, listOf("numpad", "mini")),
            ArchetypeBlueprint("Quick Math Ribbon", "Last result + launch button", WidgetSlotSize.SIZE_4X1, true, false, listOf("ribbon", "result")),
            ArchetypeBlueprint("Tax & Discount Card", "Quick shopping % breakdown", WidgetSlotSize.SIZE_4X2, true, true, listOf("discount", "tax")),
            ArchetypeBlueprint("Finance Calc Cube", "Bold symbol grid glass launcher", WidgetSlotSize.SIZE_2X2, true, false, listOf("symbols", "cube")),
            ArchetypeBlueprint("Scientific Launch Bar", "Calc + converter quick strip", WidgetSlotSize.SIZE_4X1, true, false, listOf("scientific", "strip"))
        ),
        WidgetCategory.PERSONALISE to listOf(
            ArchetypeBlueprint("Typography Banner", "Custom editorial statement bar", WidgetSlotSize.SIZE_4X1, false, false, listOf("banner", "typography", "custom")),
            ArchetypeBlueprint("Custom Text-to-QR", "Instant scannable glass QR tile", WidgetSlotSize.SIZE_2X2, false, false, listOf("qr", "code", "custom")),
            ArchetypeBlueprint("Aesthetic Glass Spacer", "Pure translucent frosted tile", WidgetSlotSize.SIZE_2X2, false, false, listOf("spacer", "minimal", "clean")),
            ArchetypeBlueprint("Signature Monogram", "Framed initials glass badge", WidgetSlotSize.SIZE_2X2, true, false, listOf("monogram", "badge", "initials")),
            ArchetypeBlueprint("QR Business Card", "Custom QR + title & handle", WidgetSlotSize.SIZE_4X2, true, false, listOf("qr", "card", "share")),
            ArchetypeBlueprint("Mood Board Plaque", "Custom headline + subtitle", WidgetSlotSize.SIZE_4X2, true, false, listOf("plaque", "headline")),
            ArchetypeBlueprint("Glass Divider Pill", "Wide minimalist status badge", WidgetSlotSize.SIZE_4X1, true, false, listOf("divider", "pill", "badge")),
            ArchetypeBlueprint("Studio Poster 4x4", "Large typographic art frame", WidgetSlotSize.SIZE_4X4, true, false, listOf("poster", "art", "4x4")),
            ArchetypeBlueprint("Neon Nameplate", "Luminous accent custom tag", WidgetSlotSize.SIZE_4X1, true, false, listOf("nameplate", "neon")),
            ArchetypeBlueprint("Pure Frosted Canvas", "Ultra-clean 4x2 glass spacer", WidgetSlotSize.SIZE_4X2, false, false, listOf("canvas", "spacer"))
        )
    )

    private val accentVariants = listOf(
        0xFF38BDF8L, // Liquid Cyan
        0xFFA78BFA, // Aurora Violet
        0xFFFB7185, // Sunset Coral
        0xFF34D399, // Emerald Mint
        0xFFFBBF24, // Solar Amber
        0xFF60A5FA, // Glacier Blue
        0xFFF472B6, // Rose Quartz
        0xFF2DD4BF, // Teal Prism
        0xFF818CF8, // Indigo Glow
        0xFFF97316  // Tangerine
    )

    val allPresets: List<WidgetPresetSpec> by lazy {
        buildList(TOTAL_EXPECTED_PRESETS) {
            WidgetCategory.entries.forEachIndexed { catIdx, category ->
                val blueprints = categoryBlueprints.getValue(category)
                check(blueprints.size == 10) {
                    "Category ${category.id} must define exactly 10 archetypes"
                }
                for (indexInCat in 0 until PRESETS_PER_CATEGORY) {
                    val archetypeIdx = indexInCat % 10
                    val editionIdx = indexInCat / 10 // 0 = Frosted, 1 = Obsidian, 2 = Prism
                    val bp = blueprints[archetypeIdx]
                    val ed = editions[editionIdx]

                    // Ensure distinct typography, alignment, finish, border, and size variety across all 30
                    val typography = TypographyStyle.entries[(archetypeIdx + editionIdx * 2 + catIdx) % TypographyStyle.entries.size]
                    val alignment = WidgetAlignment.entries[(archetypeIdx + editionIdx) % WidgetAlignment.entries.size]
                    val border = BorderStyle.entries[(archetypeIdx + editionIdx * 2) % BorderStyle.entries.size]
                    val finish = GlassFinish.entries[(editionIdx + archetypeIdx) % GlassFinish.entries.size]
                    val accentMode = AccentMode.entries[(editionIdx * 2 + archetypeIdx) % AccentMode.entries.size]

                    // Rotate slot size gracefully on edition 2 for broader form-factor coverage
                    val slotSize = if (editionIdx == 2 && archetypeIdx % 3 == 0) {
                        when (bp.preferredSize) {
                            WidgetSlotSize.SIZE_2X2 -> WidgetSlotSize.SIZE_4X2
                            WidgetSlotSize.SIZE_4X1 -> WidgetSlotSize.SIZE_4X2
                            WidgetSlotSize.SIZE_4X2 -> WidgetSlotSize.SIZE_2X2
                            WidgetSlotSize.SIZE_4X4 -> WidgetSlotSize.SIZE_4X2
                        }
                    } else {
                        bp.preferredSize
                    }

                    val accentColor = if (editionIdx == 0) {
                        category.defaultAccentArgb
                    } else {
                        accentVariants[(catIdx + archetypeIdx + editionIdx * 3) % accentVariants.size]
                    }

                    val serial = (indexInCat + 1).toString().padStart(2, '0')
                    val presetId = "${category.id}_$serial"
                    val presetName = "${bp.baseName} · ${ed.suffix}"

                    add(
                        WidgetPresetSpec(
                            id = presetId,
                            category = category,
                            indexInCategory = indexInCat,
                            name = presetName,
                            subtitle = bp.subtitle,
                            recommendedSize = slotSize,
                            layoutVariant = archetypeIdx,
                            typographyStyle = typography,
                            alignment = alignment,
                            density = ed.density,
                            glassFinish = finish,
                            borderStyle = border,
                            showIcon = if (editionIdx == 2 && archetypeIdx % 4 == 1) !bp.defaultShowIcon else bp.defaultShowIcon,
                            showProgress = if (editionIdx == 1 && archetypeIdx % 3 == 2) true else bp.defaultShowProgress,
                            accentMode = accentMode,
                            defaultAccentArgb = accentColor,
                            defaultTheme = ed.theme,
                            tags = bp.keywords + listOf(
                                category.displayName.lowercase(),
                                ed.suffix.lowercase(),
                                slotSize.id,
                                typography.label.lowercase()
                            )
                        )
                    )
                }
            }
        }
    }

    val presetsById: Map<String, WidgetPresetSpec> by lazy {
        allPresets.associateBy { it.id }
    }

    val presetsByCategory: Map<WidgetCategory, List<WidgetPresetSpec>> by lazy {
        allPresets.groupBy { it.category }
    }

    fun getById(id: String): WidgetPresetSpec = presetsById[id] ?: allPresets.first()

    fun getForCategory(category: WidgetCategory): List<WidgetPresetSpec> =
        presetsByCategory[category].orEmpty()

    fun defaultPresetForSlot(slotSize: WidgetSlotSize): WidgetPresetSpec =
        allPresets.firstOrNull { it.recommendedSize == slotSize } ?: allPresets.first()

    fun search(
        query: String,
        category: WidgetCategory? = null,
        slotSize: WidgetSlotSize? = null
    ): List<WidgetPresetSpec> {
        val trimmed = query.trim().lowercase()
        val base = if (category != null) getForCategory(category) else allPresets
        return base.filter { spec ->
            val sizeMatches = slotSize == null || spec.recommendedSize == slotSize
            if (!sizeMatches) return@filter false
            if (trimmed.isEmpty()) return@filter true
            spec.name.lowercase().contains(trimmed) ||
                spec.subtitle.lowercase().contains(trimmed) ||
                spec.category.displayName.lowercase().contains(trimmed) ||
                spec.tags.any { it.contains(trimmed) }
        }
    }
}
