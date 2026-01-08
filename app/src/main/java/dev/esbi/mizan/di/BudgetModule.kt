package dev.esbi.mizan.di

import dagger.Binds
import dagger.Module
import dev.esbi.mizan.feature.budget.data.repository.BudgetRepositoryImpl
import dev.esbi.mizan.feature.budget.domain.repository.BudgetRepository

@Module
abstract class BudgetModule {
    
    @Binds
    abstract fun bindBudgetRepository(
        impl: BudgetRepositoryImpl
    ): BudgetRepository
}
