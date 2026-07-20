package dev.esbi.mizan.di

import dagger.Module
import dev.esbi.mizan.feature.accounts.di.AccountsComponent
import dev.esbi.mizan.feature.accountselector.di.AccountSelectorComponent
import dev.esbi.mizan.feature.budget.di.BudgetComponent
import dev.esbi.mizan.feature.calc.di.MizanCalculatorComponent
import dev.esbi.mizan.feature.financialmirror.di.FinancialMirrorComponent
import dev.esbi.mizan.feature.goals.di.GoalsComponent
import dev.esbi.mizan.feature.managecategories.di.ManageCategoriesComponent
import dev.esbi.mizan.feature.profile.di.ProfileComponent
import dev.esbi.mizan.feature.statistics.di.StatisticsComponent
import dev.esbi.mizan.feature.subscriptions.di.SubscriptionsComponent
import dev.esbi.mizan.feature.transactionshub.di.TransactionsHubComponent
import dev.esbi.mizan.features.addtransaction.di.AmountInputComponent
import dev.esbi.mizan.features.dashboard.di.DashboardComponent

/**
 * Module that declares all screen-level subcomponents.
 * This module must be included in AppComponent to make subcomponents available.
 */
@Module(
    subcomponents = [
        AmountInputComponent::class,
        AccountsComponent::class,
        AccountSelectorComponent::class,
        BudgetComponent::class,
        DashboardComponent::class,
        FinancialMirrorComponent::class,
        GoalsComponent::class,
        SubscriptionsComponent::class,
        ManageCategoriesComponent::class,
        ProfileComponent::class,
        StatisticsComponent::class,
        TransactionsHubComponent::class,
        MizanCalculatorComponent::class
    ]
)
object SubcomponentsModule
