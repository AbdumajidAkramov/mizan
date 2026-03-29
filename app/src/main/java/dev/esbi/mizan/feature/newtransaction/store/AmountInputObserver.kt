package dev.esbi.mizan.feature.newtransaction.store

import dev.esbi.mizan.presentation.mvikotlin.observer.StoreObserver
import javax.inject.Inject

class AmountInputObserver @Inject constructor() : StoreObserver<
        NewTransactionStore.Intent,
        NewTransactionStore.Action,
        NewTransactionStore.Message,
        NewTransactionStore.Label> {
}
