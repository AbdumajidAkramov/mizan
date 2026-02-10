package dev.esbi.mizan.feature.subscriptions.di

import dagger.Subcomponent
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.subscriptions.presentation.SubscriptionsViewModel

@ScreenScope
@Subcomponent(modules = [SubscriptionsModule::class])
interface SubscriptionsComponent {

    val viewModel: SubscriptionsViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): SubscriptionsComponent
    }
}
