package dev.esbi.mizan.feature.newtransaction.amountinput

import androidx.lifecycle.ViewModel
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputState
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputStore
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputStore.Intent
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputStore.Label
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AmountInputViewModel @Inject constructor(
    private val store: AmountInputStore
) : ViewModel() {

    val state: Flow<AmountInputState> = store.states
    val labels: Flow<Label> = store.labels

    fun onIntent(intent: Intent) {
        store.accept(intent)
    }

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }
}
