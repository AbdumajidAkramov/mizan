package dev.esbi.mizan.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import dev.esbi.mizan.feature.newtransaction.root.NewTransactionScreen
import dev.esbi.mizan.feature.newtransaction.di.NewTransactionStoreProvider
import dev.esbi.mizan.feature.newtransaction.domain.usecase.GetTransactionMetadataUseCase
import dev.esbi.mizan.feature.newtransaction.domain.usecase.SaveNewTransactionUseCase
import dev.esbi.mizan.feature.addtransaction.presentation.AddTransactionViewModel
import dev.esbi.mizan.feature.addtransaction.presentation.PremiumAddTransactionScreen
import dev.esbi.mizan.feature.budget.presentation.BudgetViewModel
import dev.esbi.mizan.feature.budget.presentation.ui.BudgetScreen
import dev.esbi.mizan.feature.dashboard.presentation.DashboardViewModel
import dev.esbi.mizan.feature.dashboard.presentation.ui.DashboardScreen
import dev.esbi.mizan.feature.financialmirror.presentation.FinancialMirrorViewModel
import dev.esbi.mizan.feature.financialmirror.presentation.ui.FinancialMirrorScreen
import dev.esbi.mizan.feature.profile.presentation.ProfileViewModel
import dev.esbi.mizan.feature.profile.presentation.ui.ProfileScreen
import dev.esbi.mizan.feature.statistics.presentation.StatisticsViewModel
import dev.esbi.mizan.feature.statistics.presentation.ui.PremiumStatisticsScreen
import dev.esbi.mizan.feature.transactions.presentation.TransactionsViewModel
import dev.esbi.mizan.feature.transactions.presentation.ui.TransactionsScreen

@Composable
internal fun MizanNavHost(
    navController: NavHostController,
    viewModelFactory: ViewModelProvider.Factory,
    newTransactionStoreProvider: NewTransactionStoreProvider,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.Dashboard,
        modifier = modifier
    ) {
        composable<NavRoute.Dashboard>() {
            val viewModel: DashboardViewModel = viewModel(factory = viewModelFactory)
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToCategory = { categoryId ->
                    navController.navigate(NavRoute.CategoryDetail(categoryId))
                }
            )
        }

        composable<NavRoute.FinancialMirror> {
            val viewModel: FinancialMirrorViewModel = viewModel(factory = viewModelFactory)
            FinancialMirrorScreen(viewModel)
        }
        composable<NavRoute.Budget> {
            val viewModel: BudgetViewModel = viewModel(factory = viewModelFactory)
            BudgetScreen(viewModel)
        }
        composable<NavRoute.Transactions> {
            val viewModel: TransactionsViewModel = viewModel(factory = viewModelFactory)
            TransactionsScreen(viewModel)
        }
        composable<NavRoute.Statistics> {
            val viewModel: StatisticsViewModel = viewModel(factory = viewModelFactory)
            PremiumStatisticsScreen(viewModel)
        }
        composable<NavRoute.Profile> {
            val viewModel: ProfileViewModel = viewModel(factory = viewModelFactory)
            ProfileScreen(viewModel)
        }
        composable<NavRoute.CategoryDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoute.CategoryDetail>()
            // TODO: Implement CategoryDetailScreen when needed
        }

        composable<NavRoute.AddTransaction> { backStackEntry ->
            val viewModel: AddTransactionViewModel = viewModel(factory = viewModelFactory)
            PremiumAddTransactionScreen(
                viewModel = viewModel,
                onClose = {
                    navController.popBackStack()
                },
                onSave = {
                    navController.popBackStack()
                }
            )
        }

        composable<NavRoute.NewAddTransaction> {
            val store = newTransactionStoreProvider.create().create()
            NewTransactionScreen(
                store = store,
                onClose = {
                    navController.popBackStack()
                },
                onSave = { transaction ->
                    // TODO: Handle saving the transaction
                    navController.popBackStack()
                },
                onManageCategories = {
                    // TODO: Navigate to category management if needed
                }
            )
        }
    }
}
