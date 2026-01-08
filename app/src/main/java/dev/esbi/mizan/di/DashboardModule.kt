package dev.esbi.mizan.di

import dagger.Binds
import dagger.Module
import dev.esbi.mizan.feature.dashboard.data.repository.DashboardRepositoryImpl
import dev.esbi.mizan.feature.dashboard.domain.repository.DashboardRepository
import javax.inject.Singleton

@Module
interface DashboardModule {
    
    @Binds
    @Singleton
    fun bindDashboardRepository(impl: DashboardRepositoryImpl): DashboardRepository
}
