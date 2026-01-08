package dev.esbi.mizan.di

import dagger.Binds
import dagger.Module
import dev.esbi.mizan.feature.transactions.data.repository.TransactionsRepositoryImpl
import dev.esbi.mizan.feature.transactions.domain.repository.TransactionsRepository

@Module
abstract class TransactionsModule {
    
    @Binds
    abstract fun bindTransactionsRepository(
        impl: TransactionsRepositoryImpl
    ): TransactionsRepository
}
