package dev.esbi.mizan.presentation.feature.accounts.store

import dev.esbi.mizan.presentation.mvikotlin.observer.StoreObserver
import javax.inject.Inject

class AccountsStoreObserver @Inject constructor(
) : StoreObserver<
        AccountsStore.Intent,
        AccountsStore.Action,
        AccountsStore.Message,
        AccountsStore.Label> {

    override fun onIntent(intent: AccountsStore.Intent) {

    }
}
