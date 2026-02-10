package dev.esbi.mizan.feature.subscriptions.di

import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.subscriptions.presentation.SubscriptionsViewModel
import dev.esbi.mizan.feature.subscriptions.presentation.store.SubscriptionsStoreFactory

@Module
object SubscriptionsModule {

    @Provides
    @ScreenScope
    fun provideSubscriptionsViewModel(
        storeFactory: SubscriptionsStoreFactory
    ): SubscriptionsViewModel {
        return SubscriptionsViewModel(storeFactory)
    }
}
