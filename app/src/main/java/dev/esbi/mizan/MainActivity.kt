package dev.esbi.mizan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import dev.esbi.mizan.data.settings.AppSettingsManager
import dev.esbi.mizan.feature.profile.domain.model.AppSettings
import dev.esbi.mizan.main.MainAppScreen
import dev.esbi.mizan.ui.theme.MizanTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsManager: AppSettingsManager

//    @Inject
//    lateinit var databaseSeedingManager: DatabaseSeedingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val appComponent = (application as MizanApplication).appComponent
        appComponent.inject(this)
        val settingState: MutableStateFlow<AppSettings> = MutableStateFlow(AppSettings())

        // Ensure database is seeded
//        databaseSeedingManager.ensureDatabaseSeeded(this)

        lifecycleScope.launch {
            settingsManager.settings.collect {
                settingState.value = it
            }
        }

        setContent {
            val state = settingState.collectAsState()
            MizanTheme(darkTheme = state.value.isDarkMode) {
                MainAppScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                )
            }
        }
    }
}
