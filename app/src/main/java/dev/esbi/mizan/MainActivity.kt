package dev.esbi.mizan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dev.esbi.mizan.data.settings.AppSettingsManager
import dev.esbi.mizan.data.local.DatabaseSeedingManager
import dev.esbi.mizan.feature.profile.domain.model.AppSettings
import dev.esbi.mizan.main.MainAppScreen
import dev.esbi.mizan.ui.theme.MizanTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import dev.esbi.mizan.feature.newtransaction.di.NewTransactionStoreProvider

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsManager: AppSettingsManager

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory // MizanViewModelFactory keladi

    @Inject
    lateinit var databaseSeedingManager: DatabaseSeedingManager

    @Inject
    lateinit var newTransactionStoreProvider: NewTransactionStoreProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (application as MizanApplication).appComponent
        appComponent.inject(this)
        val settingState: MutableStateFlow<AppSettings> = MutableStateFlow(AppSettings())
        
        // Ensure database is seeded
        databaseSeedingManager.ensureDatabaseSeeded(this)
        
//        val viewModelFactoryProvider = appComponent.viewModelFactoryProvider
        lifecycleScope.launch {
            settingsManager.settings.collect {
                settingState.value = it
            }
        }

        enableEdgeToEdge()
        setContent {
            val state = settingState.collectAsState()
            MizanTheme(darkTheme = state.value.isDarkMode) {
                MainAppScreen(
                viewModelFactory = viewModelFactory,
                newTransactionStoreProvider = newTransactionStoreProvider
            )

                /*               val navController = rememberNavController()
                               Scaffold(
                                   modifier = Modifier.fillMaxSize(),
                               ) { innerPadding ->
                                   Box(modifier = Modifier.padding(innerPadding)) {
                                       MizanNavHost(
                                           modifier = Modifier.fillMaxSize(),
                                           navController = navController,
                                           viewModelFactoryProvider = viewModelFactoryProvider
                                       )

                                       PremiumBottomNavigation(
                                           navController = navController,
                                           modifier = Modifier
                                               .align(Alignment.BottomCenter)
                                               .fillMaxWidth()
                                               .padding(bottom = MizanTheme.premium.spacing.md) // pb-[var(--premium-space-md)]
                                               .padding(horizontal = MizanTheme.premium.spacing.md), // px-[var(--premium-space-md)]

                                       )
                                   }
                               }
                */
            }
        }
    }
}