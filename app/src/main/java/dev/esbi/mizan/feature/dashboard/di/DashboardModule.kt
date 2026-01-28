package dev.esbi.mizan.feature.dashboard.di

import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.dashboard.presentation.DashboardViewModel
import dev.esbi.mizan.feature.dashboard.presentation.store.DashboardStoreFactory
import kotlinx.coroutines.CoroutineDispatcher

@Module
object DashboardModule {

    @Provides
    @ScreenScope
    fun provideDashboardStoreFactory(
        storeFactory: com.arkivanov.mvikotlin.core.store.StoreFactory,
        observeDashboardSummaryUseCase: dev.esbi.mizan.feature.dashboard.domain.usecase.ObserveDashboardSummaryUseCase,
        refreshDashboardUseCase: dev.esbi.mizan.feature.dashboard.domain.usecase.RefreshDashboardUseCase,
        mockDataSeeder: dev.esbi.mizan.data.local.seeder.MockDataSeeder,
        @MainDispatcher mainDispatcher: CoroutineDispatcher
    ): DashboardStoreFactory {
        return DashboardStoreFactory(
            storeFactory,
            observeDashboardSummaryUseCase,
            refreshDashboardUseCase,
            mockDataSeeder,
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
