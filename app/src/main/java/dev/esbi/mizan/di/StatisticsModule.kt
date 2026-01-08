package dev.esbi.mizan.di

import dagger.Binds
import dagger.Module
import dev.esbi.mizan.feature.statistics.data.repository.StatisticsRepositoryImpl
import dev.esbi.mizan.feature.statistics.domain.repository.StatisticsRepository

@Module
abstract class StatisticsModule {
    
    @Binds
    abstract fun bindStatisticsRepository(
        impl: StatisticsRepositoryImpl
    ): StatisticsRepository
}
