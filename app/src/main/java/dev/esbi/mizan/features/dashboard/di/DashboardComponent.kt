package dev.esbi.mizan.features.dashboard.di

import dagger.Subcomponent
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.features.dashboard.presentation.DashboardViewModel

@ScreenScope
@Subcomponent(modules = [DashboardModule::class])
interface DashboardComponent {

    val viewModel: DashboardViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): DashboardComponent
    }
}
