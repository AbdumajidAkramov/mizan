package dev.esbi.mizan.di

import dagger.Binds
import dagger.Module
import dev.esbi.mizan.domain.repository.DashboardRepository
import dev.esbi.mizan.feature.dashboard.data.repository.DashboardRepositoryImpl
import javax.inject.Singleton

@Module
interface DashboardModule {

    @Binds
    @Singleton
    fun bindDashboardRepository(impl: DashboardRepositoryImpl): DashboardRepository
}
