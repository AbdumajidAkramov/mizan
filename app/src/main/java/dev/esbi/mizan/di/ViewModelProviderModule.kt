package dev.esbi.mizan.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import dev.esbi.mizan.feature.dashboard.presentation.DashboardViewModel
import dev.esbi.mizan.feature.dashboard.presentation.DashboardViewModelFactory
import javax.inject.Provider
import javax.inject.Singleton

@Module
internal interface ViewModelProviderModule {


    companion object {


        /* @Provides
         @Singleton
         fun provideViewModelFactoryProvider(
             dashboardViewModelFactory: DashboardViewModelFactory,
             financialMirrorViewModelFactory: FinancialMirrorViewModelFactory,
             budgetViewModelFactory: BudgetViewModelFactory,
             transactionsViewModelFactory: TransactionsViewModelFactory,
             statisticsViewModelFactory: StatisticsViewModelFactory,
             profileViewModelFactory: ProfileViewModelFactory,
             addTransactionViewModelFactory: AddTransactionViewModelFactory,
         ): ViewModelFactoryProvider {
             return ViewModelFactoryProvider(
                 dashboardViewModelFactory,
                 financialMirrorViewModelFactory,
                 budgetViewModelFactory,
                 transactionsViewModelFactory,
                 statisticsViewModelFactory,
                 profileViewModelFactory,
                 addTransactionViewModelFactory
             )
         }*/
    }
}
