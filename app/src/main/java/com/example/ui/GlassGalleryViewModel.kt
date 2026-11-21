package com.example.ui

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.GlassWidgetsApplication
import com.example.data.DeviceTelemetryProvider
import com.example.data.WidgetConfigStore
import com.example.data.WidgetTelemetrySnapshot
import com.example.glance.GlassWidgetSlots
import com.example.model.GlassTheme
import com.example.model.InstalledAppInfo
import com.example.model.WidgetCategory
import com.example.model.WidgetInstanceConfig
import com.example.model.WidgetPresetRegistry
import com.example.model.WidgetPresetSpec
import com.example.model.WidgetSlotSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class GlassGalleryUiState(
    val searchQuery: String = "",
    val selectedCategory: WidgetCategory? = null,
    val selectedSlotFilter: WidgetSlotSize? = null,
    val visiblePresets: List<WidgetPresetSpec> = WidgetPresetRegistry.allPresets,
    val totalPresetCount: Int = WidgetPresetRegistry.allPresets.size,
    val activeCustomizeSpec: WidgetPresetSpec? = null,
    val draftConfig: WidgetInstanceConfig = WidgetInstanceConfig(),
    val installedApps: List<InstalledAppInfo> = emptyList(),
    val showHowToGuide: Boolean = false,
    val isAmbientSoundEnabled: Boolean = true,
    val statusBannerMessage: String? = null,
    val configuringAppWidgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID
)

class GlassGalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as GlassWidgetsApplication

    private val _uiState = MutableStateFlow(GlassGalleryUiState())
    val uiState: StateFlow<GlassGalleryUiState> = _uiState.asStateFlow()

    private val _telemetry = MutableStateFlow(DeviceTelemetryProvider.captureSnapshot(application))
    val telemetry: StateFlow<WidgetTelemetrySnapshot> = _telemetry.asStateFlow()

    init {
        viewModelScope.launch {
            WidgetConfigStore.observeAmbientSoundEnabled(application).collect { enabled ->
                app.ambientAudioEngine.setSoundEnabled(enabled)
                _uiState.update { it.copy(isAmbientSoundEnabled = enabled) }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            val apps = DeviceTelemetryProvider.queryLaunchableApps(application)
            _uiState.update { it.copy(installedApps = apps) }
        }
    }

    fun initializeForWidgetConfig(appWidgetId: Int, suggestedSlot: WidgetSlotSize?) {
        _uiState.update {
            it.copy(
                configuringAppWidgetId = appWidgetId,
                selectedSlotFilter = suggestedSlot
            )
        }
        recomputeFilteredPresets()

        if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            viewModelScope.launch {
                val existing = WidgetConfigStore.getWidgetConfig(
                    getApplication(),
                    appWidgetId,
                    suggestedSlot ?: WidgetSlotSize.SIZE_2X2
                )
                val spec = WidgetPresetRegistry.getById(existing.presetId)
                _uiState.update {
                    it.copy(
                        selectedCategory = spec.category,
                        draftConfig = existing
                    )
                }
                recomputeFilteredPresets()
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        recomputeFilteredPresets()
    }

    fun onCategorySelected(category: WidgetCategory?) {
        _uiState.update { it.copy(selectedCategory = category) }
        recomputeFilteredPresets()
    }

    fun onSlotFilterSelected(slotSize: WidgetSlotSize?) {
        _uiState.update { it.copy(selectedSlotFilter = slotSize) }
        recomputeFilteredPresets()
    }

    private fun recomputeFilteredPresets() {
        val state = _uiState.value
        val results = WidgetPresetRegistry.search(
            query = state.searchQuery,
            category = state.selectedCategory,
            slotSize = state.selectedSlotFilter
        )
        _uiState.update { it.copy(visiblePresets = results) }
    }

    fun openCustomizerFor(spec: WidgetPresetSpec) {
        val currentId = _uiState.value.configuringAppWidgetId
        val defaultTextColor = if (spec.defaultTheme == GlassTheme.LIGHT_FROSTED) 0xFF0F172AL else 0xFFFFFFFFL
        _uiState.update {
            it.copy(
                activeCustomizeSpec = spec,
                draftConfig = WidgetInstanceConfig(
                    appWidgetId = currentId,
                    presetId = spec.id,
                    category = spec.category,
                    glassTheme = spec.defaultTheme,
                    opacity = 0.44f,
                    cornerRadiusDp = 24,
                    accentColorArgb = spec.defaultAccentArgb,
                    textColorArgb = defaultTextColor,
                    customText = "",
                    targetDateEpochMillis = System.currentTimeMillis() + 14L * 86_400_000L,
                    selectedApps = it.draftConfig.selectedApps,
                    photoUri = it.draftConfig.photoUri
                )
            )
        }
    }

    fun dismissCustomizer() {
        _uiState.update { it.copy(activeCustomizeSpec = null) }
    }

    fun updateDraft(transform: (WidgetInstanceConfig) -> WidgetInstanceConfig) {
        _uiState.update { it.copy(draftConfig = transform(it.draftConfig)) }
    }

    fun toggleSelectedApp(packageName: String) {
        updateDraft { cur ->
            val updated = if (cur.selectedApps.contains(packageName)) {
                cur.selectedApps - packageName
            } else {
                (cur.selectedApps + packageName).take(9)
            }
            cur.copy(selectedApps = updated)
        }
    }

    fun toggleAmbientSound() {
        val next = !_uiState.value.isAmbientSoundEnabled
        app.ambientAudioEngine.setSoundEnabled(next)
        _uiState.update { it.copy(isAmbientSoundEnabled = next) }
        viewModelScope.launch {
            WidgetConfigStore.setAmbientSoundEnabled(getApplication(), next)
        }
    }

    fun setHowToGuideVisible(visible: Boolean) {
        _uiState.update { it.copy(showHowToGuide = visible) }
    }

    fun clearStatusBanner() {
        _uiState.update { it.copy(statusBannerMessage = null) }
    }

    /**
     * Persists config keyed by appWidgetId and refreshes the GlanceAppWidget instance.
     */
    fun applyWidgetConfiguration(appWidgetId: Int, onCompleted: () -> Unit) {
        val spec = _uiState.value.activeCustomizeSpec ?: return
        val configToSave = _uiState.value.draftConfig.copy(
            appWidgetId = appWidgetId,
            presetId = spec.id,
            category = spec.category
        )
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                WidgetConfigStore.saveWidgetConfig(getApplication(), configToSave)
                GlassWidgetSlots.updateAppWidgetById(getApplication(), appWidgetId)
            }
            onCompleted()
        }
    }

    /**
     * Pins a configured preset directly from the in-app gallery to the home screen using its recommended slot.
     */
    fun requestPinSelectedWidget() {
        val spec = _uiState.value.activeCustomizeSpec ?: return
        val config = _uiState.value.draftConfig.copy(
            presetId = spec.id,
            category = spec.category
        )
        viewModelScope.launch {
            WidgetConfigStore.savePendingPinConfig(getApplication(), config)
            val context = getApplication<Application>()
            val awm = AppWidgetManager.getInstance(context)
            val provider = ComponentName(context, GlassWidgetSlots.receiverClassForSlot(spec.recommendedSize))
            val pinned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && awm.isRequestPinAppWidgetSupported) {
                runCatching { awm.requestPinAppWidget(provider, null, null) }.getOrDefault(false)
            } else {
                false
            }
            _uiState.update {
                it.copy(
                    activeCustomizeSpec = null,
                    statusBannerMessage = if (pinned) {
                        "Pinning ${spec.name} (${spec.recommendedSize.label}) to your home screen!"
                    } else {
                        "Saved style! Long-press your home screen → Widgets → ${spec.recommendedSize.label} to place it."
                    }
                )
            }
        }
    }
}
