package dev.esbi.mizan.di

import dagger.Module
import dev.esbi.mizan.feature.budget.di.BudgetComponent
import dev.esbi.mizan.feature.dashboard.di.DashboardComponent
import dev.esbi.mizan.feature.financialmirror.di.FinancialMirrorComponent
import dev.esbi.mizan.feature.newtransaction.amountinput.di.AmountInputComponent
import dev.esbi.mizan.feature.newtransaction.categoryselect.di.CategorySelectComponent
import dev.esbi.mizan.feature.profile.di.ProfileComponent
import dev.esbi.mizan.feature.statistics.di.StatisticsComponent
import dev.esbi.mizan.feature.transactions.di.TransactionsComponent

/**
 * Module that declares all screen-level subcomponents.
 * This module must be included in AppComponent to make subcomponents available.
 */
@Module(
    subcomponents = [
        // New Transaction Flow
        AmountInputComponent::class,
        CategorySelectComponent::class,
        // Main Screens
        BudgetComponent::class,
        DashboardComponent::class,
        FinancialMirrorComponent::class,
        ProfileComponent::class,
        StatisticsComponent::class,
        TransactionsComponent::class
    ]
)
object SubcomponentsModule
