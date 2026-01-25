package dev.esbi.mizan.feature.budget.di

import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.budget.presentation.BudgetViewModel
import dev.esbi.mizan.feature.budget.presentation.store.BudgetStoreFactory

@Module
object BudgetModule {

    @Provides
    @ScreenScope
    fun provideBudgetViewModel(
        storeFactory: BudgetStoreFactory
    ): BudgetViewModel {
        return BudgetViewModel(storeFactory)
    }
}
