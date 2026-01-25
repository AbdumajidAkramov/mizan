package dev.esbi.mizan.feature.transactions.di

import dagger.Subcomponent
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.transactions.presentation.TransactionsViewModel

@ScreenScope
@Subcomponent(modules = [TransactionsModule::class])
interface TransactionsComponent {

    val viewModel: TransactionsViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): TransactionsComponent
    }
}
