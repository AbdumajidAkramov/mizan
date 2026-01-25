package dev.esbi.mizan.feature.newtransaction.amountinput.store

import dev.esbi.mizan.mvikotlin.observer.StoreObserver
import javax.inject.Inject

internal class AmountInputObserver @Inject constructor() : StoreObserver<
        AmountInputStore.Intent,
        AmountInputStore.Action,
        AmountInputStore.Message,
        AmountInputStore.Label> {
}
