package dev.esbi.mizan.feature.accounts.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.util.CurrencyConverter
import dev.esbi.mizan.feature.accounts.presentation.AccountsViewModel
import dev.esbi.mizan.feature.accounts.presentation.store.AccountsStoreFactory
import kotlinx.coroutines.CoroutineDispatcher

@Module
internal object AccountsModule {

    @Provides
    @ScreenScope
    fun provideAccountsStoreFactory(
        storeFactory: StoreFactory,
        accountRepository: AccountRepository,
        currencyConverter: CurrencyConverter,
        @MainDispatcher mainDispatcher: CoroutineDispatcher
    ): AccountsStoreFactory {
        return AccountsStoreFactory(storeFactory, accountRepository, currencyConverter, mainDispatcher)
    }

    @Provides
    @ScreenScope
    fun provideAccountsViewModel(
        storeFactory: AccountsStoreFactory
    ): AccountsViewModel {
        return AccountsViewModel(storeFactory)
    }
}
