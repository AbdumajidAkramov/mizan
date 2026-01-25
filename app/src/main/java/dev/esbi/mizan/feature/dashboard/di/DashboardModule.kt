package dev.esbi.mizan.feature.dashboard.di

import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.dashboard.presentation.DashboardViewModel
import dev.esbi.mizan.feature.dashboard.presentation.store.DashboardStoreFactory

@Module
object DashboardModule {

    @Provides
    @ScreenScope
    fun provideDashboardViewModel(
        storeFactory: DashboardStoreFactory
    ): DashboardViewModel {
        return DashboardViewModel(storeFactory)
    }
}
