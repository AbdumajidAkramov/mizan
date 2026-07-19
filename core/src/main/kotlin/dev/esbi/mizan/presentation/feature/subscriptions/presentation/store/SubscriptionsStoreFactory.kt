package dev.esbi.mizan.presentation.feature.subscriptions.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.presentation.di.MainDispatcher
import dev.esbi.mizan.presentation.feature.subscriptions.domain.model.BillingCycle
import dev.esbi.mizan.presentation.feature.subscriptions.domain.model.Subscription
import dev.esbi.mizan.presentation.feature.subscriptions.domain.repository.SubscriptionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

class SubscriptionsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val subscriptionRepository: SubscriptionRepository,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) {

    fun create(): SubscriptionsStore =
        object : SubscriptionsStore, Store<SubscriptionsStore.Intent, SubscriptionsStore.State, SubscriptionsStore.Label> by storeFactory.create(
            name = "SubscriptionsStore",
            initialState = SubscriptionsStore.State(isLoading = true),
            bootstrapper = SimpleBootstrapper(SubscriptionsStore.Action.Init),
            executorFactory = { ExecutorImpl(subscriptionRepository, mainDispatcher) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data class SubscriptionsLoaded(val subscriptions: List<Subscription>) : Msg
        data class Error(val message: String) : Msg
        data object ShowAddDialog : Msg
        data object DismissDialog : Msg
    }

    private class ExecutorImpl(
        private val subscriptionRepository: SubscriptionRepository,
        @MainDispatcher mainDispatcher: CoroutineDispatcher
    ) : CoroutineExecutor<SubscriptionsStore.Intent, SubscriptionsStore.Action, SubscriptionsStore.State, Msg, SubscriptionsStore.Label>(
        mainContext = mainDispatcher
    ) {

        override fun executeAction(action: SubscriptionsStore.Action) {
            when (action) {
                SubscriptionsStore.Action.Init -> observeSubscriptions()
            }
        }

        override fun executeIntent(intent: SubscriptionsStore.Intent) {
            when (intent) {
                is SubscriptionsStore.Intent.AddSubscription -> addSubscription(intent)
                is SubscriptionsStore.Intent.DeleteSubscription -> deleteSubscription(intent.subscriptionId)
                is SubscriptionsStore.Intent.ShowAddDialog -> dispatch(Msg.ShowAddDialog)
                is SubscriptionsStore.Intent.DismissDialog -> dispatch(Msg.DismissDialog)
            }
        }

        private fun observeSubscriptions() {
            subscriptionRepository.observeAll()
                .onEach { subs -> dispatch(Msg.SubscriptionsLoaded(subs)) }
                .catch { e -> dispatch(Msg.Error(e.message ?: "Unknown error")) }
                .launchIn(scope)
        }

        private fun addSubscription(intent: SubscriptionsStore.Intent.AddSubscription) {
            scope.launch {
                try {
                    subscriptionRepository.insert(
                        Subscription(
                            name = intent.name,
                            amount = intent.amount,
                            billingCycle = try { BillingCycle.valueOf(intent.billingCycle) } catch (_: Exception) { BillingCycle.MONTHLY },
                            nextRenewalDate = intent.nextRenewalDate,
                            icon = intent.icon,
                            color = intent.color,
                            category = intent.category
                        )
                    )
                    dispatch(Msg.DismissDialog)
                    publish(SubscriptionsStore.Label.ShowSuccess("Subscription added"))
                } catch (e: Exception) {
                    publish(SubscriptionsStore.Label.ShowError(e.message ?: "Failed to add subscription"))
                }
            }
        }

        private fun deleteSubscription(id: Long) {
            scope.launch {
                try {
                    subscriptionRepository.delete(id)
                    publish(SubscriptionsStore.Label.ShowSuccess("Subscription removed"))
                } catch (e: Exception) {
                    publish(SubscriptionsStore.Label.ShowError(e.message ?: "Failed to remove subscription"))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<SubscriptionsStore.State, Msg> {
        override fun SubscriptionsStore.State.reduce(msg: Msg): SubscriptionsStore.State = when (msg) {
            is Msg.Loading -> copy(isLoading = true, error = null)
            is Msg.SubscriptionsLoaded -> {
                val monthlyCost = msg.subscriptions.fold(BigDecimal.ZERO) { acc, s -> acc.add(s.monthlyAmount) }
                copy(
                    isLoading = false,
                    subscriptions = msg.subscriptions,
                    totalMonthlyCost = monthlyCost,
                    error = null
                )
            }
            is Msg.Error -> copy(isLoading = false, error = msg.message)
            is Msg.ShowAddDialog -> copy(showAddDialog = true)
            is Msg.DismissDialog -> copy(showAddDialog = false)
        }
    }
}
