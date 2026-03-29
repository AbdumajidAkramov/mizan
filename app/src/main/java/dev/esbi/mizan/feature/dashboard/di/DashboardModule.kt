package dev.esbi.mizan.feature.dashboard.di

import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.dashboard.presentation.DashboardViewModel
import dev.esbi.mizan.presentation.di.MainDispatcher
import dev.esbi.mizan.presentation.feature.dashboard.domain.usecase.ObserveDashboardSummaryUseCase
import dev.esbi.mizan.presentation.feature.dashboard.domain.usecase.RefreshDashboardUseCase
import dev.esbi.mizan.presentation.feature.dashboard.presentation.store.DashboardStoreFactory
import kotlinx.coroutines.CoroutineDispatcher

@Module
object DashboardModule {

    @Provides
    @ScreenScope
    fun provideDashboardStoreFactory(
        storeFactory: com.arkivanov.mvikotlin.core.store.StoreFactory,
        observeDashboardSummaryUseCase: ObserveDashboardSummaryUseCase,
        refreshDashboardUseCase: RefreshDashboardUseCase,
        @MainDispatcher mainDispatcher: CoroutineDispatcher
    ): DashboardStoreFactory {
        return DashboardStoreFactory(
            storeFactory,
            observeDashboardSummaryUseCase,
            refreshDashboardUseCase,
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
