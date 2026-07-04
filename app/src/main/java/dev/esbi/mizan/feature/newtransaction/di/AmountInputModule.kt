package dev.esbi.mizan.feature.newtransaction.di

import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.newtransaction.AmountInputViewModel
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStoreFactory

@Module
internal object AmountInputModule {


    @Provides
    @ScreenScope
    fun provideAmountInputViewModel(
        addNewTransactionStoreFactory: AddNewTransactionStoreFactory,
        transactionId: Long?
    ): AmountInputViewModel {
        return AmountInputViewModel(addNewTransactionStoreFactory, transactionId)
    }
}
