package dev.esbi.mizan.feature.premiumaddtransaction.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.repository.CurrencyRepository
import dev.esbi.mizan.domain.repository.TransactionRepository
import dev.esbi.mizan.feature.addtransaction.domain.repository.CategoryRepository
import dev.esbi.mizan.feature.addtransaction.domain.repository.TemplateRepository
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.ManualInputHandler
import dev.esbi.mizan.feature.newtransaction.amountinput.executor.NavigationHandler
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class AddNewTransactionTemplatesExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val manualInputHandler: ManualInputHandler,
    private val navigationHandler: NavigationHandler,
    private val categoryRepository: CategoryRepository,
    private val currencyRepository: CurrencyRepository,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val templateRepository: TemplateRepository
) : CoroutineExecutor<AddNewTransactionStore.Intent,
        AddNewTransactionStore.Action,
        AddNewTransactionStore.State,
        AddNewTransactionStore.Message,
        AddNewTransactionStore.Label>() {

    override fun executeIntent(intent: AddNewTransactionStore.Intent) {
        when (intent) {
            else -> Unit
        }
    }
}
