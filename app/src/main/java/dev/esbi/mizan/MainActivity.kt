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
import dev.esbi.mizan.domain.model.profile.AppSettings
import dev.esbi.mizan.main.MainAppScreen
import dev.esbi.mizan.design.theme.MizanTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsManager: AppSettingsManager

    @Inject
    lateinit var mockDataSeeder: dev.esbi.mizan.data.local.seeder.MockDataSeeder

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val appComponent = (application as MizanApplication).appComponent
        appComponent.inject(this)
        val settingState: MutableStateFlow<AppSettings> = MutableStateFlow(AppSettings())

        lifecycleScope.launch {
//            mockDataSeeder.seedData() // Ensure basic entities explicitly exist
            settingsManager.settings.collect {
                settingState.value = it
            }
        }

        setContent {
            val state = settingState.collectAsState()
            _root_ide_package_.dev.esbi.mizan.design.theme.MizanTheme(darkTheme = state.value.isDarkMode) {
                MainAppScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                )
            }
        }
    }
}
