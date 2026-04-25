package dev.esbi.mizan.feature.dashboard.di

import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.repository.CurrencyRepository
import dev.esbi.mizan.domain.usecase.GetDashboardSummaryUseCase
import dev.esbi.mizan.domain.usecase.dashboard.ObserveDashboardSummaryUseCase
import dev.esbi.mizan.domain.usecase.dashboard.RefreshDashboardUseCase
import dev.esbi.mizan.domain.util.CurrencyConverter
import dev.esbi.mizan.feature.dashboard.presentation.DashboardViewModel
import dev.esbi.mizan.presentation.di.MainDispatcher
import dev.esbi.mizan.presentation.feature.dashboard.store.DashboardStoreFactory
import kotlinx.coroutines.CoroutineDispatcher

@Module
object DashboardModule {

    @Provides
    @ScreenScope
    fun provideGetDashboardSummaryUseCase(
        accountRepository: AccountRepository,
        currencyRepository: CurrencyRepository,
        currencyConverter: CurrencyConverter
    ): GetDashboardSummaryUseCase {
        return GetDashboardSummaryUseCase(
            accountRepository = accountRepository,
            currencyRepository = currencyRepository,
            currencyConverter = currencyConverter
        )
    }

    @Provides
    @ScreenScope
    fun provideDashboardStoreFactory(
        storeFactory: com.arkivanov.mvikotlin.core.store.StoreFactory,
        observeDashboardSummaryUseCase: ObserveDashboardSummaryUseCase,
        refreshDashboardUseCase: RefreshDashboardUseCase,
        getDashboardSummaryUseCase: GetDashboardSummaryUseCase,
        @MainDispatcher mainDispatcher: CoroutineDispatcher
    ): DashboardStoreFactory {
        return DashboardStoreFactory(
            storeFactory,
            observeDashboardSummaryUseCase,
            refreshDashboardUseCase,
            getDashboardSummaryUseCase,
            mainDispatcher
        )
    }

    @Provides
    @ScreenScope
    fun provideDashboardViewModel(
        storeFactory: DashboardStoreFactory
    ): DashboardViewModel {
        return DashboardViewModel(storeFactory)
    }
}
