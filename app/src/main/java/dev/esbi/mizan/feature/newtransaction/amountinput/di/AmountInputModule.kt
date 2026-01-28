package dev.esbi.mizan.feature.newtransaction.amountinput.di

import android.content.Context
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.domain.repository.CurrencyRepository
import dev.esbi.mizan.domain.repository.TransactionRepository
import dev.esbi.mizan.feature.addtransaction.domain.repository.CategoryRepository
import dev.esbi.mizan.feature.newtransaction.amountinput.AmountInputViewModel
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.CameraScannerHandler
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.ManualInputHandler
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.NavigationHandler
import dev.esbi.mizan.feature.newtransaction.store.AmountInputObserver
import dev.esbi.mizan.feature.newtransaction.store.AmountInputStoreFactory
import dev.esbi.mizan.feature.newtransaction.store.executors.NewTransactionExecutor
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Provider

@Module
internal object AmountInputModule {

    @Provides
    @ScreenScope
    fun provideAmountInputExecutor(
        context: Context,
        @MainDispatcher mainDispatcher: CoroutineDispatcher,
        manualInputHandler: ManualInputHandler,
        navigationHandler: NavigationHandler,
        categoryRepository: CategoryRepository,
        currencyRepository: CurrencyRepository,
        transactionRepository: TransactionRepository
    ): NewTransactionExecutor {
        return NewTransactionExecutor(
            context = context,
            mainDispatcher = mainDispatcher,
            manualInputHandler = manualInputHandler,
            navigationHandler = navigationHandler,
            categoryRepository = categoryRepository,
            currencyRepository = currencyRepository,
            transactionRepository = transactionRepository
        )
    }

    @Provides
    @ScreenScope
    fun provideCameraScannerHandler(): CameraScannerHandler {
        return CameraScannerHandler()
    }

    @Provides
    @ScreenScope
    fun provideManualInputHandler(): ManualInputHandler {
        return ManualInputHandler()
    }

    @Provides
    @ScreenScope
    fun provideNavigationHandler(): NavigationHandler {
        return NavigationHandler()
    }

    @Provides
    @ScreenScope
    fun provideAmountInputObserver(): AmountInputObserver {
        return AmountInputObserver()
    }

    @Provides
    @ScreenScope
    fun provideAmountInputStoreFactory(
        storeFactory: Provider<StoreFactory>,
        executorProvider: Provider<NewTransactionExecutor>
    ): AmountInputStoreFactory {
        return AmountInputStoreFactory(storeFactory, executorProvider)
    }

    @Provides
    @ScreenScope
    fun provideAmountInputViewModel(
        storeFactory: AmountInputStoreFactory
    ): AmountInputViewModel {
        return AmountInputViewModel(storeFactory)
    }
}
