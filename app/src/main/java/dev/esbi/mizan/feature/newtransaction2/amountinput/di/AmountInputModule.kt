package dev.esbi.mizan.feature.newtransaction2.amountinput.di

import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.newtransaction.AmountInputViewModel
import dev.esbi.mizan.feature.newtransaction2.amountinput.executor.CameraScannerHandler
import dev.esbi.mizan.feature.newtransaction2.store.AmountInputObserver
import dev.esbi.mizan.feature.newtransaction2.store.executors.ManualInputHandler
import dev.esbi.mizan.feature.newtransaction2.store.executors.NavigationHandler
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStoreFactory

@Module
internal object AmountInputModule {

    /* @Provides
     @ScreenScope
     fun provideAmountInputExecutor(
         context: Context,
         @MainDispatcher mainDispatcher: CoroutineDispatcher,
         manualInputHandler: ManualInputHandler,
         navigationHandler: NavigationHandler,
         categoryRepository: CategoryRepository,
         currencyRepository: CurrencyRepository,
         transactionRepository: TransactionRepository,
         accountRepository: AccountRepository,
         templateRepository: TemplateRepository
     ): NewTransactionExecutor {
         return NewTransactionExecutor(
             context = context,
             mainDispatcher = mainDispatcher,
             manualInputHandler = manualInputHandler,
             navigationHandler = navigationHandler,
             categoryRepository = categoryRepository,
             currencyRepository = currencyRepository,
             transactionRepository = transactionRepository,
             accountRepository = accountRepository,
             templateRepository = templateRepository
         )
     }*/

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

    /*
        @Provides
        @ScreenScope
        fun provideAmountInputStoreFactory(
            storeFactory: Provider<StoreFactory>,
            executorProvider: Provider<NewTransactionExecutor>
        ): AmountInputStoreFactory {
            return AmountInputStoreFactory(storeFactory, executorProvider)
        }
    */

    @Provides
    @ScreenScope
    fun provideAmountInputViewModel(
        addNewTransactionStoreFactory: AddNewTransactionStoreFactory
    ): AmountInputViewModel {
        return AmountInputViewModel(addNewTransactionStoreFactory)
    }
}
