package dev.esbi.mizan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dev.esbi.mizan.feature.budget.presentation.BudgetViewModelFactory
import dev.esbi.mizan.feature.dashboard.presentation.DashboardViewModelFactory
import dev.esbi.mizan.feature.financialmirror.presentation.FinancialMirrorViewModelFactory
import dev.esbi.mizan.feature.profile.presentation.ProfileViewModelFactory
import dev.esbi.mizan.feature.statistics.presentation.StatisticsViewModelFactory
import dev.esbi.mizan.feature.transactions.presentation.TransactionsViewModelFactory
import dev.esbi.mizan.navigation.MizanBottomNavigation
import dev.esbi.mizan.navigation.MizanNavHost
import dev.esbi.mizan.ui.theme.MizanTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var dashboardViewModelFactory: DashboardViewModelFactory
    
    @Inject
    lateinit var financialMirrorViewModelFactory: FinancialMirrorViewModelFactory
    
    @Inject
    lateinit var budgetViewModelFactory: BudgetViewModelFactory
    
    @Inject
    lateinit var transactionsViewModelFactory: TransactionsViewModelFactory
    
    @Inject
    lateinit var statisticsViewModelFactory: StatisticsViewModelFactory
    
    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        (application as MizanApplication).appComponent.inject(this)
        
        enableEdgeToEdge()
        setContent {
            MizanTheme {
                val navController = rememberNavController()
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        MizanBottomNavigation(navController = navController)
                    }
                ) { innerPadding ->
                    MizanNavHost(
                        navController = navController,
                        dashboardViewModelFactory = dashboardViewModelFactory,
                        financialMirrorViewModelFactory = financialMirrorViewModelFactory,
                        budgetViewModelFactory = budgetViewModelFactory,
                        transactionsViewModelFactory = transactionsViewModelFactory,
                        statisticsViewModelFactory = statisticsViewModelFactory,
                        profileViewModelFactory = profileViewModelFactory,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}