package com.example

import com.example.model.WidgetCategory
import com.example.model.WidgetPresetRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WidgetPresetRegistryTest {

    @Test
    fun registryContainsExactly28CategoriesAnd840UniquePresets() {
        assertEquals(28, WidgetCategory.entries.size)
        assertEquals(840, WidgetPresetRegistry.allPresets.size)

        val uniqueIds = WidgetPresetRegistry.allPresets.map { it.id }.toSet()
        assertEquals(840, uniqueIds.size)

        val uniqueNames = WidgetPresetRegistry.allPresets.map { "${it.category.id}:${it.name}" }.toSet()
        assertEquals(840, uniqueNames.size)

        WidgetCategory.entries.forEach { category ->
            val presets = WidgetPresetRegistry.getForCategory(category)
            assertEquals("Category ${category.displayName} must have 30 presets", 30, presets.size)
        }
    }

    @Test
    fun searchFiltersAcrossAll840Presets() {
        val qrResults = WidgetPresetRegistry.search("qr")
        assertTrue(qrResults.isNotEmpty())
        val aiResults = WidgetPresetRegistry.search("gemini")
        assertTrue(aiResults.isNotEmpty())
    }
}
