package dev.esbi.mizan.di

import dagger.Binds
import dagger.Module
import dev.esbi.mizan.feature.subscriptions.data.repository.SubscriptionRepositoryImpl
import dev.esbi.mizan.feature.subscriptions.domain.repository.SubscriptionRepository

@Module
abstract class SubscriptionsModule {

    @Binds
    abstract fun bindSubscriptionRepository(
        impl: SubscriptionRepositoryImpl
    ): SubscriptionRepository
}
