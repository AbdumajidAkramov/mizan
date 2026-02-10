package dev.esbi.mizan.feature.transfer.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.feature.transfer.domain.repository.TransferRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class TransferStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val transferRepository: TransferRepository,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) {

    fun create(): TransferStore =
        object : TransferStore, Store<TransferStore.Intent, TransferStore.State, TransferStore.Label> by storeFactory.create(
            name = "TransferStore",
            initialState = TransferStore.State(isLoading = true),
            bootstrapper = SimpleBootstrapper(TransferStore.Action.Init),
            executorFactory = { ExecutorImpl(transferRepository, mainDispatcher) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data class AccountsLoaded(val accounts: List<Account>) : Msg
        data class SourceSelected(val account: Account) : Msg
        data class DestinationSelected(val account: Account) : Msg
        data class AmountChanged(val text: String, val amount: Double) : Msg
        data class Swapped(val source: Account?, val destination: Account?) : Msg
        data object TransferStarted : Msg
        data object TransferCompleted : Msg
        data class Error(val message: String) : Msg
    }

    private class ExecutorImpl(
        private val transferRepository: TransferRepository,
        @MainDispatcher mainDispatcher: CoroutineDispatcher
    ) : CoroutineExecutor<TransferStore.Intent, TransferStore.Action, TransferStore.State, Msg, TransferStore.Label>(
        mainContext = mainDispatcher
    ) {

        override fun executeAction(action: TransferStore.Action) {
            when (action) {
                TransferStore.Action.Init -> observeAccounts()
            }
        }

        override fun executeIntent(intent: TransferStore.Intent) {
            when (intent) {
                is TransferStore.Intent.SelectSource -> dispatch(Msg.SourceSelected(intent.account))
                is TransferStore.Intent.SelectDestination -> dispatch(Msg.DestinationSelected(intent.account))
                is TransferStore.Intent.InputAmount -> {
                    val parsed = intent.text.toDoubleOrNull() ?: 0.0
                    dispatch(Msg.AmountChanged(intent.text, parsed))
                }
                is TransferStore.Intent.SwapAccounts -> {
                    val currentState = state()
                    dispatch(Msg.Swapped(currentState.destinationAccount, currentState.sourceAccount))
                }
                is TransferStore.Intent.SubmitTransfer -> submitTransfer()
            }
        }

        private fun observeAccounts() {
            transferRepository.observeAccounts()
                .onEach { accounts -> dispatch(Msg.AccountsLoaded(accounts)) }
                .catch { e -> dispatch(Msg.Error(e.message ?: "Failed to load accounts")) }
                .launchIn(scope)
        }

        private fun submitTransfer() {
            val s = state()
            val src = s.sourceAccount ?: return
            val dst = s.destinationAccount ?: return
            if (s.amount <= 0) return

            dispatch(Msg.TransferStarted)
            scope.launch {
                try {
                    val targetAmount = if (src.currency.code != dst.currency.code) {
                        s.amount * s.conversionRate
                    } else null

                    transferRepository.executeTransfer(
                        sourceAccountId = src.id,
                        destinationAccountId = dst.id,
                        amount = s.amount,
                        targetAmount = targetAmount,
                        note = "Transfer from ${src.name} to ${dst.name}"
                    )
                    dispatch(Msg.TransferCompleted)
                    publish(TransferStore.Label.TransferSuccess(
                        "Transferred ${formatAmount(s.amount)} from ${src.name} to ${dst.name}"
                    ))
                } catch (e: Exception) {
                    dispatch(Msg.Error(e.message ?: "Transfer failed"))
                    publish(TransferStore.Label.ShowError(e.message ?: "Transfer failed"))
                }
            }
        }

        private fun formatAmount(amount: Double): String =
            String.format("%,.0f", amount).replace(',', ' ')
    }

    private object ReducerImpl : Reducer<TransferStore.State, Msg> {
        override fun TransferStore.State.reduce(msg: Msg): TransferStore.State = when (msg) {
            is Msg.Loading -> copy(isLoading = true, error = null)
            is Msg.AccountsLoaded -> copy(isLoading = false, accounts = msg.accounts, error = null)
            is Msg.SourceSelected -> {
                val newDest = if (destinationAccount?.id == msg.account.id) null else destinationAccount
                copy(sourceAccount = msg.account, destinationAccount = newDest)
            }
            is Msg.DestinationSelected -> copy(destinationAccount = msg.account)
            is Msg.AmountChanged -> copy(amountText = msg.text, amount = msg.amount)
            is Msg.Swapped -> copy(sourceAccount = msg.source, destinationAccount = msg.destination)
            is Msg.TransferStarted -> copy(isLoading = true, error = null)
            is Msg.TransferCompleted -> copy(isLoading = false, amountText = "", amount = 0.0)
            is Msg.Error -> copy(isLoading = false, error = msg.message)
        }
    }
}
