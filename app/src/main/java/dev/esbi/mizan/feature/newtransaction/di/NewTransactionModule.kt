package dev.esbi.mizan.feature.newtransaction.di

import dagger.Binds
import dagger.Module
import dev.esbi.mizan.feature.newtransaction.data.repository.NewTransactionRepositoryImpl
import dev.esbi.mizan.feature.newtransaction.domain.repository.NewTransactionRepository
import javax.inject.Singleton

@Module
abstract class NewTransactionModule {
    
    @Binds
    @Singleton
    abstract fun bindNewTransactionRepository(
        newTransactionRepositoryImpl: NewTransactionRepositoryImpl
    ): NewTransactionRepository
}
