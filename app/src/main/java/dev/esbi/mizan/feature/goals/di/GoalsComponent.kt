package dev.esbi.mizan.feature.goals.di

import dagger.Subcomponent
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.goals.presentation.GoalsViewModel

@ScreenScope
@Subcomponent(modules = [GoalsModule::class])
interface GoalsComponent {

    val viewModel: GoalsViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): GoalsComponent
    }
}
