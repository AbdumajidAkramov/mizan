package dev.esbi.mizan.di

import dagger.Binds
import dagger.Module
import dev.esbi.mizan.feature.addtransaction.data.repository.CategoryRepositoryImpl
import dev.esbi.mizan.feature.addtransaction.data.repository.TemplateRepositoryImpl
import dev.esbi.mizan.presentation.feature.addtransaction.domain.repository.CategoryRepository
import dev.esbi.mizan.presentation.feature.addtransaction.domain.repository.TemplateRepository
import dev.esbi.mizan.data.repository.AccountRepositoryImpl as CoreAccountRepositoryImpl
import dev.esbi.mizan.data.repository.CurrencyRepositoryImpl as CoreCurrencyRepositoryImpl
import dev.esbi.mizan.data.repository.TransactionRepositoryImpl as CoreTransactionRepositoryImpl
import dev.esbi.mizan.domain.repository.AccountRepository as CoreAccountRepository
import dev.esbi.mizan.domain.repository.CurrencyRepository as CoreCurrencyRepository
import dev.esbi.mizan.domain.repository.TransactionRepository as CoreTransactionRepository

@Module
abstract class TransactionsModule {

    @Binds
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    abstract fun bindTemplateRepository(
        impl: TemplateRepositoryImpl
    ): TemplateRepository

    @Binds
    abstract fun bindCoreAccountRepository(
        impl: CoreAccountRepositoryImpl
    ): CoreAccountRepository

    @Binds
    abstract fun bindCoreCurrencyRepository(
        impl: CoreCurrencyRepositoryImpl
    ): CoreCurrencyRepository

    @Binds
    abstract fun bindCoreTransactionRepository(
        impl: CoreTransactionRepositoryImpl
    ): CoreTransactionRepository
}
