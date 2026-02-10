package dev.esbi.mizan.feature.transfer.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.domain.model.Account

interface TransferStore : Store<TransferStore.Intent, TransferStore.State, TransferStore.Label> {

    sealed interface Action {
        data object Init : Action
    }

    sealed interface Intent {
        data class SelectSource(val account: Account) : Intent
        data class SelectDestination(val account: Account) : Intent
        data class InputAmount(val text: String) : Intent
        data object SwapAccounts : Intent
        data object SubmitTransfer : Intent
    }

    data class State(
        val accounts: List<Account> = emptyList(),
        val sourceAccount: Account? = null,
        val destinationAccount: Account? = null,
        val amountText: String = "",
        val amount: Double = 0.0,
        val conversionRate: Double = 1.0,
        val isLoading: Boolean = false,
        val error: String? = null
    ) {
        val canSubmit: Boolean
            get() = sourceAccount != null
                    && destinationAccount != null
                    && amount > 0
                    && sourceAccount!!.id != destinationAccount!!.id
                    && !isLoading

        val availableDestinations: List<Account>
            get() = accounts.filter { it.id != sourceAccount?.id }
    }

    sealed interface Label {
        data class ShowError(val message: String) : Label
        data class TransferSuccess(val message: String) : Label
    }
}
