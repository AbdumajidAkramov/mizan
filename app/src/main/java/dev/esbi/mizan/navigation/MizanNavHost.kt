package dev.esbi.mizan.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.esbi.mizan.MizanApplication
import dev.esbi.mizan.feature.budget.presentation.ui.BudgetScreen
import dev.esbi.mizan.feature.dashboard.presentation.ui.DashboardScreen
import dev.esbi.mizan.feature.financialmirror.presentation.ui.FinancialMirrorScreen
import dev.esbi.mizan.feature.newtransaction.amountinput.AmountInputScreen
import dev.esbi.mizan.feature.newtransaction.categoryselect.CategorySelectScreen
import dev.esbi.mizan.feature.profile.presentation.ui.ProfileScreen
import dev.esbi.mizan.feature.statistics.presentation.ui.PremiumStatisticsScreen
import dev.esbi.mizan.feature.transactions.presentation.ui.TransactionsScreen

@Composable
internal fun MizanNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val appComponent = remember { (context.applicationContext as MizanApplication).appComponent }

    NavHost(
        navController = navController,
        startDestination = NavRoute.Dashboard,
        modifier = modifier
    ) {
        composable<NavRoute.Dashboard> {
            val component = remember { appComponent.dashboardComponent().create() }
            val viewModel = component.viewModel
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToCategory = { categoryId ->
                    navController.navigate(NavRoute.CategoryDetail(categoryId))
                }
            )
        }
        composable<NavRoute.FinancialMirror> {
            val component = remember { appComponent.financialMirrorComponent().create() }
            val viewModel = component.viewModel
            FinancialMirrorScreen(viewModel)
        }
        composable<NavRoute.Budget> {
            val component = remember { appComponent.budgetComponent().create() }
            val viewModel = component.viewModel
            BudgetScreen(viewModel)
        }
        composable<NavRoute.Transactions> {
            val component = remember { appComponent.transactionsComponent().create() }
            val viewModel = component.viewModel
            TransactionsScreen(viewModel)
        }
        composable<NavRoute.Statistics> {
            val component = remember { appComponent.statisticsComponent().create() }
            val viewModel = component.viewModel
            PremiumStatisticsScreen(viewModel)
        }
        composable<NavRoute.Profile> {
            val component = remember { appComponent.profileComponent().create() }
            val viewModel = component.viewModel
            ProfileScreen(viewModel)
        }
        composable<NavRoute.CategoryDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoute.CategoryDetail>()
            // TODO: Implement CategoryDetailScreen when needed
        }

        composable<NavRoute.AmountInput> {
            val component = remember { appComponent.amountInputComponent().create() }
            val viewModel = component.viewModel

            AmountInputScreen(
                viewModel = viewModel,
                onBackPressed = {
                    navController.popBackStack()
                },
                onSubmit = {
                    navController.navigate(
                        NavRoute.CategorySelect(
                            transactionType = "EXPENSE"
                        )
                    )
                }
            )
        }

        composable<NavRoute.CategorySelect> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoute.CategorySelect>()

            val component = remember { appComponent.categorySelectComponent().create() }
            val viewModel = component.viewModel

            CategorySelectScreen(
                viewModel = viewModel,
                onCategorySelected = {
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onManageCategories = {
                    navController.popBackStack()
                }
            )
        }

    }
}
