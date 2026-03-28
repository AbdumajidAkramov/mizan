package dev.esbi.mizan.data.settings

import androidx.datastore.core.DataStore
import dev.esbi.mizan.domain.model.profile.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

class AppSettingsManager(
    private val dataStore: DataStore<AppSettings>
) {
    // Sozlamalarni kuzatib borish (Flow)
    val settings: Flow<AppSettings> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(AppSettings())
            } else {
                throw exception
            }
        }

    // Faqat Dark Mode-ni o'zgartirish
    suspend fun toggleDarkMode() {
        dataStore.updateData { currentSettings ->
            currentSettings.copy(isDarkMode = !currentSettings.isDarkMode)
        }
    }

    // Tilni yangilash
    suspend fun updateLanguage(newLanguage: String) {
        dataStore.updateData { it.copy(language = newLanguage) }
    }

    // Barcha sozlamalarni yangilash
    suspend fun updateSettings(newSettings: AppSettings) {
        dataStore.updateData { newSettings }
    }
}