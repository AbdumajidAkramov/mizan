package dev.esbi.mizan.di

import dagger.Binds
import dagger.Module
import dev.esbi.mizan.feature.transfer.data.repository.TransferRepositoryImpl
import dev.esbi.mizan.feature.transfer.domain.repository.TransferRepository

@Module
abstract class TransferModule {

    @Binds
    abstract fun bindTransferRepository(
        impl: TransferRepositoryImpl
    ): TransferRepository
}
