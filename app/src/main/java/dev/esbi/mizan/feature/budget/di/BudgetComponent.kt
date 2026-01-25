package dev.esbi.mizan.feature.budget.di

import dagger.Subcomponent
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.budget.presentation.BudgetViewModel

@ScreenScope
@Subcomponent(modules = [BudgetModule::class])
interface BudgetComponent {

    val viewModel: BudgetViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): BudgetComponent
    }
}
