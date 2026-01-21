package dev.esbi.mizan.feature.newtransaction.root

import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.di.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewTransactionStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val getTransactionMetadataUseCase: dev.esbi.mizan.feature.newtransaction.domain.usecase.GetTransactionMetadataUseCase,
    private val saveNewTransactionUseCase: dev.esbi.mizan.feature.newtransaction.domain.usecase.SaveNewTransactionUseCase
) {
    fun create(): NewTransactionStore =
        object : NewTransactionStore, com.arkivanov.mvikotlin.core.store.Store<NewTransactionIntent, NewTransactionState, NewTransactionAction> by storeFactory.create(
            name = "NewTransactionStore",
            initialState = NewTransactionState(),
            executorFactory = { 
                SimpleExecutor(getTransactionMetadataUseCase, saveNewTransactionUseCase, mainDispatcher) 
            },
            reducer = SimpleReducer()
        ) {}
}

class SimpleExecutor(
    private val getTransactionMetadataUseCase: dev.esbi.mizan.feature.newtransaction.domain.usecase.GetTransactionMetadataUseCase,
    private val saveNewTransactionUseCase: dev.esbi.mizan.feature.newtransaction.domain.usecase.SaveNewTransactionUseCase,
    private val mainDispatcher: CoroutineDispatcher
) : CoroutineExecutor<NewTransactionIntent, NewTransactionAction, NewTransactionState, NewTransactionMessage, NewTransactionAction>(
    mainContext = mainDispatcher
) {
    
    override fun executeIntent(intent: NewTransactionIntent) {
        super.executeIntent(intent)
        when (intent) {
            is NewTransactionAction.LoadInitialData -> {
                scope.launch {
                    try {
                        val transactionType = state().transactionType ?: dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType.Expense
                        val metadataResult = getTransactionMetadataUseCase(transactionType)
                        
                        if (metadataResult.isSuccess) {
                            val metadata = metadataResult.getOrThrow()
                            dispatch(NewTransactionMessage.UpdateAvailableCategories(metadata.categories))
                            dispatch(NewTransactionMessage.UpdateAvailableAccounts(metadata.accounts))
                        }
                    } catch (e: Exception) {
                        // Handle error
                    }
                }
            }
            else -> {
                // Handle other intents as needed
            }
        }
    }
}

class SimpleReducer : com.arkivanov.mvikotlin.core.store.Reducer<NewTransactionState, NewTransactionMessage> {
    override fun NewTransactionState.reduce(msg: NewTransactionMessage): NewTransactionState {
        return when (msg) {
            is NewTransactionMessage.UpdateAvailableCategories -> copy(availableCategories = msg.categories)
            is NewTransactionMessage.UpdateAvailableAccounts -> copy(availableAccounts = msg.accounts)
            else -> this
        }
    }
}
