package dev.esbi.mizan.feature.transfer.di

import dagger.Module
import dagger.Provides
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.transfer.presentation.TransferViewModel
import dev.esbi.mizan.feature.transfer.presentation.store.TransferStoreFactory

@Module
object TransferModule {

    @Provides
    @ScreenScope
    fun provideTransferViewModel(
        storeFactory: TransferStoreFactory
    ): TransferViewModel {
        return TransferViewModel(storeFactory)
    }
}
