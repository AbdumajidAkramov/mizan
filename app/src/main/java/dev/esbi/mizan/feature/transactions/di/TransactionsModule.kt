package dev.esbi.mizan.feature.transactions.di

import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.transactions.presentation.TransactionsViewModel
import dev.esbi.mizan.feature.transactions.presentation.store.TransactionsStoreFactory

@Module
object TransactionsModule {

    @Provides
    @ScreenScope
    fun provideTransactionsViewModel(
        storeFactory: TransactionsStoreFactory
    ): TransactionsViewModel {
        return TransactionsViewModel(storeFactory)
    }
}
