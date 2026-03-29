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
import dev.esbi.mizan.feature.accountgroups.AccountGroupManagementScreen
import dev.esbi.mizan.feature.accountmanagement.AccountManagementScreen
import dev.esbi.mizan.feature.accountselector.AccountSelectionScreen
import dev.esbi.mizan.feature.addaccount.AddNewAccountScreen
import dev.esbi.mizan.feature.budget.presentation.ui.BudgetScreen
import dev.esbi.mizan.feature.dashboard.presentation.ui.DashboardScreen
import dev.esbi.mizan.feature.financialmirror.presentation.ui.FinancialMirrorScreen
import dev.esbi.mizan.feature.goals.presentation.ui.FinancialGoalsScreen
import dev.esbi.mizan.feature.managecategories.ui.ManageCategoriesContent
import dev.esbi.mizan.feature.newtransaction.categoryselect.CategorySelectScreen
import dev.esbi.mizan.feature.premiumaddtransaction.NewTransactionScreen
import dev.esbi.mizan.feature.profile.presentation.ui.ProfileScreen
import dev.esbi.mizan.feature.statistics.presentation.ui.PremiumStatisticsScreen
import dev.esbi.mizan.feature.subscriptions.presentation.ui.SubscriptionTrackerScreen
import dev.esbi.mizan.feature.transactionshub.TransactionsHubScreen
import dev.esbi.mizan.presentation.feature.accountgroups.store.AccountGroupStoreFactory
import dev.esbi.mizan.presentation.feature.addaccount.store.AddAccountStoreFactory
import dev.esbi.mizan.presentation.feature.premiumaddtransaction.store.AddNewTransactionStore

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
                    navController.navigate(NavRoute.AmountInput) {
                        popUpTo(NavRoute.AmountInput) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToTransactionsHub = {
                    navController.navigate(NavRoute.Transactions)
                },
                onNavigateToProfile = {
                },
                onNavigateToGoals = {
                    navController.navigate(NavRoute.FinancialGoals)
                },
                onNavigateToSubscriptions = {
                    navController.navigate(NavRoute.Subscriptions)
                },
                onNavigateToTransfer = {
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
            ProfileScreen(
                viewModel,
                onNavigateToBudgetManagementScreen = {
                    navController.navigate(NavRoute.Budget)
                },
                onNavigateToFinancialGoalsScreen = {
                    navController.navigate(NavRoute.FinancialGoals)
                },
                onNavigateToAccountsScreen = {
                    navController.navigate(NavRoute.AccountManagement)
                },
                onNavigateToAccountGroupsScreen = {
                    navController.navigate(NavRoute.AccountGroupManagement)
                }
            )
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
                onNavigateToAccountManage = {
                    navController.navigate(NavRoute.AccountManagement)
                },
                onNavigateToAccountSelector = {
                    navController.navigate(NavRoute.AccountSelector)
                },
                onNavigateToCategorySelector = {
                    navController.navigate(NavRoute.CategorySelect(transactionType = "EXPENSE"))
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

            // Get the previous back stack entry to access the NewTransaction ViewModel
            val previousEntry = remember(navController.currentBackStackEntry) {
                navController.previousBackStackEntry
            }

            // Get the AmountInput component from previous entry if it exists
            val amountInputViewModel = previousEntry?.let {
                remember { appComponent.amountInputComponent().create().viewModel }
            }

            CategorySelectScreen(
                viewModel = viewModel,
                onCategorySelected = { category ->
                    // Convert Category type and pass back to NewTransactionStore
                    val domainCategory = object : dev.esbi.mizan.domain.model.Category {
                        override val id = category.id
                        override val name = category.name
                        override val type = category.type
                        override val parentId = category.parentId
                        override val iconName = category.iconName
                        override val color = category.color
                        override val budgetLimit: Double? = null
                        override val isArchived = false
                        override val orderIndex = 0
                    }
                    amountInputViewModel?.onNewTransactionStoreIntent(
                        AddNewTransactionStore.Intent.OnCategorySelected(
                            domainCategory
                        )
                    )
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
        composable<NavRoute.FinancialGoals> {
            val component = remember { appComponent.goalsComponent().create() }
            val viewModel = component.viewModel
            FinancialGoalsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable<NavRoute.Subscriptions> {
            val component = remember { appComponent.subscriptionsComponent().create() }
            val viewModel = component.viewModel
            SubscriptionTrackerScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
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
                    if (accountId == null) {
                        navController.navigate(NavRoute.AddNewAccount)
                    } else {
                        // TODO: Navigate to edit account screen
                    }
                }
            )
        }
        composable<NavRoute.AccountSelector> {
            val component = remember { appComponent.accountSelectorComponent().create() }
            val viewModel = component.viewModel

            // Get the previous back stack entry to access the NewTransaction ViewModel
            val previousEntry = remember(navController.currentBackStackEntry) {
                navController.previousBackStackEntry
            }

            // Get the AmountInput component from previous entry if it exists
            val amountInputViewModel = previousEntry?.let {
                remember { appComponent.amountInputComponent().create().viewModel }
            }

            AccountSelectionScreen(
                viewModel = viewModel,
                onClose = {
                    navController.popBackStack()
                },
                onAccountSelected = { account ->
                    // Pass the selected account back to NewTransactionStore
                    amountInputViewModel?.onNewTransactionStoreIntent(
                        AddNewTransactionStore.Intent.OnAccountSelected(
                            account
                        )
                    )
                    navController.popBackStack()
                },
                onAddAccountClick = {
                    navController.navigate(NavRoute.AccountManagement)
                }
            )
        }

        composable<NavRoute.AccountGroupManagement> {
            val store = remember {
                AccountGroupStoreFactory(
                    storeFactory = appComponent.storeFactory,
                    accountRepository = appComponent.accountRepository
                ).create()
            }
            AccountGroupManagementScreen(
                store = store,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable<NavRoute.AddNewAccount> {
            val store = remember {
                AddAccountStoreFactory(
                    storeFactory = appComponent.storeFactory,
                    accountRepository = appComponent.accountRepository
                ).create()
            }
            AddNewAccountScreen(
                store = store,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
