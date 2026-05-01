package dev.esbi.mizan.feature.newtransaction.di

import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.newtransaction.AmountInputViewModel
import dev.esbi.mizan.feature.newtransaction2.store.AmountInputObserver
import dev.esbi.mizan.feature.newtransaction2.store.executors.ManualInputHandler
import dev.esbi.mizan.feature.newtransaction2.store.executors.NavigationHandler
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStoreFactory

@Module
internal object AmountInputModule {

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
    fun provideAmountInputViewModel(
        addNewTransactionStoreFactory: AddNewTransactionStoreFactory
    ): AmountInputViewModel {
        return AmountInputViewModel(addNewTransactionStoreFactory)
    }
}
