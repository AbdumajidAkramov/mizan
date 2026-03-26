package dev.esbi.mizan.feature.accountselector.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.feature.accountselector.AccountSelectorViewModel
import dev.esbi.mizan.feature.accountselector.store.AccountSelectorExecutor
import dev.esbi.mizan.feature.accountselector.store.AccountSelectorStoreFactory
import kotlinx.coroutines.CoroutineDispatcher

@Module
internal object AccountSelectorModule {

    @Provides
    @ScreenScope
    fun provideAccountSelectorExecutorFactory(
        @MainDispatcher mainDispatcher: CoroutineDispatcher,
        accountRepository: AccountRepository
    ): AccountSelectorExecutor.Factory {
        return object : AccountSelectorExecutor.Factory {
            override fun create(): AccountSelectorExecutor {
                return AccountSelectorExecutor(mainDispatcher, accountRepository)
            }
        }
    }

    @Provides
    @ScreenScope
    fun provideAccountSelectorStoreFactory(
        storeFactory: StoreFactory,
        @MainDispatcher mainDispatcher: CoroutineDispatcher,
        executorFactory: AccountSelectorExecutor.Factory
    ): AccountSelectorStoreFactory {
        return AccountSelectorStoreFactory(
            storeFactory,
            mainDispatcher,
            executorFactory
        )
    }

    @Provides
    @ScreenScope
    fun provideAccountSelectorViewModel(
        storeFactory: AccountSelectorStoreFactory
    ): AccountSelectorViewModel {
        return AccountSelectorViewModel(storeFactory)
    }
}
