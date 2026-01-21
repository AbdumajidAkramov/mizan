package dev.esbi.mizan.feature.addtransaction.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.feature.addtransaction.domain.repository.AccountRepository
import dev.esbi.mizan.feature.addtransaction.domain.repository.CategoryRepository
import dev.esbi.mizan.feature.addtransaction.domain.usecase.AddTransactionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class AddTransactionStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository
) {

    fun create(): AddTransactionStore =
        object : AddTransactionStore,
            Store<AddTransactionStore.Intent, AddTransactionStore.State, AddTransactionStore.Label> by storeFactory.create(
                name = "AddTransactionStore",
                initialState = AddTransactionStore.State(),
                bootstrapper = SimpleBootstrapper(AddTransactionStore.Action.Init),
                executorFactory = {
                    AddTransactionExecutor(mainDispatcher, addTransactionUseCase, categoryRepository, accountRepository)
                },
                reducer = AddTransactionReducer
            ) {}

}
