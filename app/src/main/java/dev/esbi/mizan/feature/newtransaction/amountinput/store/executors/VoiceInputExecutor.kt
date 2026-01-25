package dev.esbi.mizan.feature.newtransaction.amountinput.store.executors

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.di.MainDispatcher
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputState
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputStore
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class VoiceInputExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : CoroutineExecutor<
        AmountInputStore.Intent,
        AmountInputStore.Action,
        AmountInputState,
        AmountInputStore.Message,
        AmountInputStore.Label>(
    mainContext = mainDispatcher
) {

}
