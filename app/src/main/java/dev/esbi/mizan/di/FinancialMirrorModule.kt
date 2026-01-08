package dev.esbi.mizan.di

import dagger.Binds
import dagger.Module
import dev.esbi.mizan.feature.financialmirror.data.repository.FinancialMirrorRepositoryImpl
import dev.esbi.mizan.feature.financialmirror.domain.repository.FinancialMirrorRepository

@Module
abstract class FinancialMirrorModule {
    
    @Binds
    abstract fun bindFinancialMirrorRepository(
        impl: FinancialMirrorRepositoryImpl
    ): FinancialMirrorRepository
}
