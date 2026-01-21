package dev.esbi.mizan.feature.newtransaction.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import dev.esbi.mizan.feature.newtransaction.domain.usecase.GetTransactionMetadataUseCase
import dev.esbi.mizan.feature.newtransaction.domain.usecase.SaveNewTransactionUseCase
import dev.esbi.mizan.feature.newtransaction.root.NewTransactionStoreFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewTransactionStoreProvider @Inject constructor(
    private val getTransactionMetadataUseCase: GetTransactionMetadataUseCase,
    private val saveNewTransactionUseCase: SaveNewTransactionUseCase
) {
    fun create(): NewTransactionStoreFactory {
        return NewTransactionStoreFactory(
            storeFactory = DefaultStoreFactory(),
            mainDispatcher = kotlinx.coroutines.Dispatchers.Main,
            getTransactionMetadataUseCase = getTransactionMetadataUseCase,
            saveNewTransactionUseCase = saveNewTransactionUseCase
        )
    }
}
