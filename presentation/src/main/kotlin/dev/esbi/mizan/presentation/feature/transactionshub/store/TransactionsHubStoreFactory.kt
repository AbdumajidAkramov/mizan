package dev.esbi.mizan.presentation.feature.transactionshub.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import dev.esbi.mizan.presentation.di.MainDispatcher
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.repository.TransactionRepository
import dev.esbi.mizan.presentation.feature.addtransaction.domain.repository.CategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Factory for creating TransactionsHubStore
 */
class TransactionsHubStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository
) {

    fun create(): TransactionsHubStore =
        object : TransactionsHubStore,
            Store<TransactionsHubStore.Intent, TransactionsHubStore.State, TransactionsHubStore.Label> by storeFactory.create(
                name = "TransactionsHubStore",
                initialState = TransactionsHubStore.State(),
                bootstrapper = CoroutineBootstrapperImpl(),
                executorFactory = {
                    TransactionsHubExecutor(
                        mainDispatcher = mainDispatcher,
                        transactionRepository = transactionRepository,
                        categoryRepository = categoryRepository,
                        accountRepository = accountRepository
                    )
                },
                reducer = TransactionsHubReducer()
            ) {}

    private class CoroutineBootstrapperImpl : CoroutineBootstrapper<Unit>() {
        override fun invoke() {
            dispatch(Unit)
        }
    }
}
