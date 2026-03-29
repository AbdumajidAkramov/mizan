package dev.esbi.mizan.feature.goals.di

import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.goals.presentation.GoalsViewModel
import dev.esbi.mizan.presentation.feature.goals.presentation.store.GoalsStoreFactory

@Module
object GoalsModule {

    @Provides
    @ScreenScope
    fun provideGoalsViewModel(
        storeFactory: GoalsStoreFactory
    ): GoalsViewModel {
        return GoalsViewModel(storeFactory)
    }
}
