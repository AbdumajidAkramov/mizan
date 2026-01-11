package dev.esbi.mizan.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.esbi.mizan.feature.budget.presentation.BudgetViewModelFactory
import dev.esbi.mizan.feature.budget.presentation.ui.BudgetScreen
import dev.esbi.mizan.feature.dashboard.presentation.DashboardViewModelFactory
import dev.esbi.mizan.feature.dashboard.presentation.ui.DashboardScreen
import dev.esbi.mizan.feature.financialmirror.presentation.FinancialMirrorViewModelFactory
import dev.esbi.mizan.feature.financialmirror.presentation.ui.FinancialMirrorScreen
import dev.esbi.mizan.feature.profile.presentation.ProfileViewModelFactory
import dev.esbi.mizan.feature.profile.presentation.ui.ProfileScreen
import dev.esbi.mizan.feature.statistics.presentation.StatisticsViewModelFactory
import dev.esbi.mizan.feature.statistics.presentation.ui.PremiumStatisticsScreen
import dev.esbi.mizan.feature.statistics.presentation.ui.StatisticsScreen
import dev.esbi.mizan.feature.transactions.presentation.TransactionsViewModelFactory
import dev.esbi.mizan.feature.transactions.presentation.ui.TransactionsScreen

@Composable
fun MizanNavHost(
    navController: NavHostController,
    dashboardViewModelFactory: DashboardViewModelFactory,
    financialMirrorViewModelFactory: FinancialMirrorViewModelFactory,
    budgetViewModelFactory: BudgetViewModelFactory,
    transactionsViewModelFactory: TransactionsViewModelFactory,
    statisticsViewModelFactory: StatisticsViewModelFactory,
    profileViewModelFactory: ProfileViewModelFactory,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.Dashboard,
        modifier = modifier
    ) {
        composable<NavRoute.Dashboard> {
            DashboardScreen(
                viewModelFactory = dashboardViewModelFactory,
                onNavigateToCategory = { categoryId ->
                    navController.navigate(NavRoute.CategoryDetail(categoryId))
                }
            )
        }

        composable<NavRoute.FinancialMirror> {
            FinancialMirrorScreen(
                viewModelFactory = financialMirrorViewModelFactory
            )
        }

        composable<NavRoute.Budget> {
            BudgetScreen(
                viewModelFactory = budgetViewModelFactory
            )
        }

        composable<NavRoute.Transactions> {
            TransactionsScreen(
                viewModelFactory = transactionsViewModelFactory
            )
        }

        composable<NavRoute.Statistics> {
            PremiumStatisticsScreen()
//            StatisticsScreen(
//                viewModelFactory = statisticsViewModelFactory
//            )
        }

        composable<NavRoute.Profile> {
            ProfileScreen(
                viewModelFactory = profileViewModelFactory
            )
        }

        composable<NavRoute.CategoryDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoute.CategoryDetail>()
            // TODO: Implement CategoryDetailScreen when needed
        }
    }
}
