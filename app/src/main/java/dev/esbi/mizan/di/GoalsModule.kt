package dev.esbi.mizan.di

import dagger.Binds
import dagger.Module
import dev.esbi.mizan.feature.goals.data.repository.GoalRepositoryImpl
import dev.esbi.mizan.presentation.feature.goals.domain.repository.GoalRepository

@Module
abstract class GoalsModule {

    @Binds
    abstract fun bindGoalRepository(
        impl: GoalRepositoryImpl
    ): GoalRepository
}
