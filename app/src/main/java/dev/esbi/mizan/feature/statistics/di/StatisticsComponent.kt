package dev.esbi.mizan.feature.statistics.di

import dagger.Subcomponent
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.statistics.presentation.StatisticsViewModel

@ScreenScope
@Subcomponent(modules = [StatisticsModule::class])
interface StatisticsComponent {

    val viewModel: StatisticsViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): StatisticsComponent
    }
}
