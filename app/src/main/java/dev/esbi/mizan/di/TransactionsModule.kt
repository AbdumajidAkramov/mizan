package dev.esbi.mizan.di

import dagger.Binds
import dagger.Module
import dev.esbi.mizan.feature.addtransaction.data.repository.AccountRepositoryImpl
import dev.esbi.mizan.feature.addtransaction.data.repository.CategoryRepositoryImpl
import dev.esbi.mizan.feature.addtransaction.data.repository.TemplateRepositoryImpl
import dev.esbi.mizan.feature.addtransaction.data.repository.TransactionRepositoryImpl
import dev.esbi.mizan.feature.addtransaction.domain.repository.AccountRepository
import dev.esbi.mizan.feature.addtransaction.domain.repository.CategoryRepository
import dev.esbi.mizan.feature.addtransaction.domain.repository.TemplateRepository
import dev.esbi.mizan.feature.addtransaction.domain.repository.TransactionRepository
import dev.esbi.mizan.feature.transactions.data.repository.TransactionsRepositoryImpl
import dev.esbi.mizan.feature.transactions.domain.repository.TransactionsRepository

@Module
abstract class TransactionsModule {

    @Binds
    abstract fun bindTransactionsRepository(
        impl: TransactionsRepositoryImpl
    ): TransactionsRepository

    @Binds
    abstract fun bindTransactionRepository(
        impl: TransactionRepositoryImpl
    ): TransactionRepository
    
    @Binds
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository
    
    @Binds
    abstract fun bindAccountRepository(
        impl: AccountRepositoryImpl
    ): AccountRepository

    @Binds
    abstract fun bindTemplateRepository(
        impl: TemplateRepositoryImpl
    ): TemplateRepository
}
