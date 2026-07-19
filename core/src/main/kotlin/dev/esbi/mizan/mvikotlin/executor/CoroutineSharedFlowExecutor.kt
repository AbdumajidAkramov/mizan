package dev.esbi.mizan.mvikotlin.executor

import com.arkivanov.mvikotlin.core.store.Executor
import dev.esbi.mizan.mvikotlin.executor.internal.ExplicitCoroutineExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlin.coroutines.CoroutineContext

/**
 * A [CoroutineSharedFlowExecutor] for executing intents using a shared flow.
 *
 * This class provides a convenient way to handle data flow using the [Flow] in Kotlin Coroutines.
 * It encapsulates a [MutableSharedFlow] to which intents can be emitted and provides a mechanism to
 * execute those intents.
 */
@Suppress("unused")
open class CoroutineSharedFlowExecutor<in Intent : Any, Action : Any, State : Any, Message : Any, Label : Any>(
    replay: Int = 1,
    extraBufferCapacity: Int = 0,
    onBufferOverflow: BufferOverflow = BufferOverflow.DROP_OLDEST,
    mainContext: CoroutineContext = Dispatchers.Main,
) : ExplicitCoroutineExecutor<Intent, Action, State, Message, Label>(mainContext) {
    private val flow = MutableSharedFlow<Intent>(replay, extraBufferCapacity, onBufferOverflow)

    final override fun init(callbacks: Executor.Callbacks<State, Message, Action, Label>) {
        super.init(callbacks)
        flow.executeIntent().launchIn(scope)
    }

    @Deprecated(message = "hidden for use", level = DeprecationLevel.HIDDEN)
    final override fun executeIntent(intent: Intent) {
        flow.tryEmit(intent)
    }

    /**
     * Executes the intents by transforming the [MutableSharedFlow] to
     * a flow of any - [Message] or [Label].
     *
     * @return A flow of any - [Message] or [Label].
     */
    protected open fun Flow<Intent>.executeIntent(): Flow<Any> {
        return emptyFlow()
    }
}
