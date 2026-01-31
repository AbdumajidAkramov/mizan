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
import dev.esbi.mizan.feature.accountmanagement.ui.AccountManagementScreen
import dev.esbi.mizan.feature.managecategories.ui.ManageCategoriesContent
import dev.esbi.mizan.feature.newtransaction.NewTransactionScreen
import dev.esbi.mizan.feature.newtransaction.categoryselect.CategorySelectScreen
import dev.esbi.mizan.feature.profile.presentation.ui.ProfileScreen
import dev.esbi.mizan.feature.statistics.presentation.ui.PremiumStatisticsScreen
import dev.esbi.mizan.feature.transactionshub.TransactionsHubScreen

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
                },
                onNavigateToNewTransaction = {
                    navController.navigate(NavRoute.AmountInput)
                },
                onNavigateToTransactionsHub = {
                    navController.navigate(NavRoute.Transactions)
                },
                onNavigateToProfile = {
                    // TODO: Navigate to Profile screen when implemented
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
            val component = remember { appComponent.transactionsHubComponent().create() }
            val viewModel = component.viewModel
            TransactionsHubScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onAddTransactionClick = {
                    navController.navigate(NavRoute.AmountInput)
                },
                onEditTransactionClick = { transactionId ->
                    // TODO: Navigate to edit transaction screen
                }
            )
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

            NewTransactionScreen(
                viewModel = viewModel,
                onBackPressed = {
                    navController.popBackStack()
                },
                onNavigateToManageCategories = {
                    navController.navigate(NavRoute.ManageCategories)
                },
                onSubmit = {
                    navController.navigate(NavRoute.Transactions) {
                        popUpTo(NavRoute.Transactions) {
                            inclusive = true
                        }
                    }
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
                    navController.navigate(NavRoute.ManageCategories)
                }
            )
        }

        composable<NavRoute.ManageCategories> {
            val component = remember { appComponent.manageCategoriesComponent().create() }
            val viewModel = component.viewModel

            ManageCategoriesContent(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToEditCategory = { categoryId ->
                    // TODO: Navigate to edit category screen if needed
                }
            )
        }

        composable<NavRoute.AccountManagement> {
            val component = remember { appComponent.accountManagementComponent().create() }
            val viewModel = component.viewModel

            AccountManagementScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToEditAccount = { accountId ->
                    // TODO: Navigate to edit account screen if needed
                }
            )
        }

    }
}
