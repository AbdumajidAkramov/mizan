package dev.esbi.mizan.feature.accounts.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.util.CurrencyConverter
import dev.esbi.mizan.feature.accounts.AccountsViewModel
import dev.esbi.mizan.feature.accounts.store.AccountsExecutor
import dev.esbi.mizan.feature.accounts.store.AccountsStoreFactory
import kotlinx.coroutines.CoroutineDispatcher

@Module
internal object AccountsModule {

    @Provides
    @ScreenScope
    fun provideAccountsExecutorFactory(
        @MainDispatcher mainDispatcher: CoroutineDispatcher,
        accountRepository: AccountRepository,
        currencyConverter: CurrencyConverter
    ): AccountsExecutor.Factory {
        return object : AccountsExecutor.Factory {
            override fun create(): AccountsExecutor {
                return AccountsExecutor(mainDispatcher, accountRepository, currencyConverter)
            }
        }
    }

    @Provides
    @ScreenScope
    fun provideAccountsStoreFactory(
        storeFactory: StoreFactory,
        executorFactory: AccountsExecutor.Factory
    ): AccountsStoreFactory {
        return AccountsStoreFactory(storeFactory, executorFactory)
    }

    @Provides
    @ScreenScope
    fun provideAccountsViewModel(
        storeFactory: AccountsStoreFactory
    ): AccountsViewModel {
        return AccountsViewModel(storeFactory)
    }
}
