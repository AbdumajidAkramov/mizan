package dev.esbi.mizan.presentation.feature.subscriptions.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.presentation.feature.subscriptions.domain.model.Subscription

import java.math.BigDecimal

interface SubscriptionsStore : Store<SubscriptionsStore.Intent, SubscriptionsStore.State, SubscriptionsStore.Label> {

    sealed interface Action {
        data object Init : Action
    }

    sealed interface Intent {
        data class AddSubscription(
            val name: String,
            val amount: BigDecimal,
            val billingCycle: String,
            val nextRenewalDate: Long,
            val icon: String,
            val color: String,
            val category: String
        ) : Intent

        data class DeleteSubscription(val subscriptionId: Long) : Intent
        data object ShowAddDialog : Intent
        data object DismissDialog : Intent
    }

    data class State(
        val subscriptions: List<Subscription> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val totalMonthlyCost: BigDecimal = BigDecimal.ZERO,
        val showAddDialog: Boolean = false
    )

    sealed interface Label {
        data class ShowError(val message: String) : Label
        data class ShowSuccess(val message: String) : Label
    }
}
