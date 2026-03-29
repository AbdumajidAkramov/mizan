package dev.esbi.mizan.presentation.feature.premiumaddtransaction.store

import dev.esbi.mizan.presentation.mvikotlin.observer.StoreObserver
import javax.inject.Inject

class AddNewTransactionStoreObserver @Inject constructor(
) : StoreObserver<AddNewTransactionStore.Intent,
        AddNewTransactionStore.Action,
        AddNewTransactionStore.Message,
        AddNewTransactionStore.Label> {

    override fun onIntent(intent: AddNewTransactionStore.Intent) {

    }
}
