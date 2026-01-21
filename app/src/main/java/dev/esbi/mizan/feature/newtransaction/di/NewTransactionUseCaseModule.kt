package dev.esbi.mizan.feature.newtransaction.di

import dagger.Module
import dagger.Provides
import dev.esbi.mizan.feature.newtransaction.domain.repository.NewTransactionRepository
import dev.esbi.mizan.feature.newtransaction.domain.usecase.GetTransactionMetadataUseCase
import dev.esbi.mizan.feature.newtransaction.domain.usecase.SaveNewTransactionUseCase
import javax.inject.Singleton

@Module
class NewTransactionUseCaseModule {
    
    @Provides
    @Singleton
    fun provideGetTransactionMetadataUseCase(
        repository: NewTransactionRepository
    ): GetTransactionMetadataUseCase {
        return GetTransactionMetadataUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideSaveNewTransactionUseCase(
        repository: NewTransactionRepository
    ): SaveNewTransactionUseCase {
        return SaveNewTransactionUseCase(repository)
    }
}
