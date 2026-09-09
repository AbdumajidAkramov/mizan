package dev.esbi.mizan.presentation.feature.addtransaction.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.repository.CurrencyRepository
import dev.esbi.mizan.presentation.di.MainDispatcher
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Action
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Intent
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Label
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.Message
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Executor for handling exchange rate bottom sheet and manual rate updates.
 */
class AddNewTransactionExchangeRateExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val currencyRepository: CurrencyRepository
) : CoroutineExecutor<Intent, Action, State, Message, Label>() {

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OpenExchangeRateBottomSheet -> {
                dispatch(Message.UpdateExchangeRateBottomSheet(true))
            }

            is Intent.CloseExchangeRateBottomSheet -> {
                dispatch(Message.UpdateExchangeRateBottomSheet(false))
            }

            is Intent.UpdateManualExchangeRate -> {
                dispatch(Message.UpdateManualExchangeRate(intent.rate))
            }

            is Intent.SyncExchangeRateFromCBU -> {
                syncExchangeRateFromCBU()
            }

            is Intent.OnAddSubCategory -> {
                publish(Label.NavigateToSubCurrency)
            }

            else -> Unit
        }
    }

    /**
     * Sync exchange rate from CBU API for the selected currency.
     */
    private fun syncExchangeRateFromCBU() {
        scope.launch(mainDispatcher) {
            try {
                val currentState = state()
                val currencyCode = currentState.selectedCurrency?.code ?: return@launch

                // Fetch latest rate from repository
                val updatedCurrency = currencyRepository.getCurrencyByCode(currencyCode)

                if (updatedCurrency != null) {
                    // Update manual rate with the synced rate
                    dispatch(Message.UpdateManualExchangeRate(updatedCurrency.exchangeRate))

                    // Show success toast
                    publish(Label.ShowToast("Exchange rate updated from CBU"))
                } else {
                    publish(Label.ShowToast("Failed to sync exchange rate"))
                }
            } catch (e: Exception) {
                publish(Label.ShowToast("Error syncing exchange rate: ${e.message}"))
            }
        }
    }
}
