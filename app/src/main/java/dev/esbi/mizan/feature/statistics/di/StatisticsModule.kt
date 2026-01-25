package dev.esbi.mizan.feature.statistics.di

import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.statistics.presentation.StatisticsViewModel
import dev.esbi.mizan.feature.statistics.presentation.store.StatisticsStoreFactory

@Module
object StatisticsModule {

    @Provides
    @ScreenScope
    fun provideStatisticsViewModel(
        storeFactory: StatisticsStoreFactory
    ): StatisticsViewModel {
        return StatisticsViewModel(storeFactory)
    }
}
