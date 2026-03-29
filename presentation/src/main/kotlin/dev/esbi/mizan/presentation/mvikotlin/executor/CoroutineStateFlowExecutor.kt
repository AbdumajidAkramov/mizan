package dev.esbi.mizan.presentation.mvikotlin.executor

import com.arkivanov.mvikotlin.core.store.Executor
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import dev.esbi.mizan.presentation.mvikotlin.executor.internal.ExplicitCoroutineExecutor

/**
 * A [CoroutineStateFlowExecutor] for executing intents using a shared flow.
 *
 * This class provides a convenient way to handle data flow using the [Flow] in Kotlin Coroutines.
 * It encapsulates a [MutableStateFlow] to which intents can be emitted and provides a mechanism to
 * execute those intents.
 */
@Suppress("MaxLineLength", "unused") // kept it for readability
abstract class CoroutineStateFlowExecutor<in Intent : Any, Action : Any, State : Any, Message : Any, Label : Any, Value : Any?>(
    initialValue: Value,
    mainContext: CoroutineContext = Dispatchers.Main,
) : ExplicitCoroutineExecutor<Intent, Action, State, Message, Label>(mainContext) {
    private val flow = MutableStateFlow(initialValue)

    final override fun init(callbacks: Executor.Callbacks<State, Message, Action, Label>) {
        super.init(callbacks)
        flow.executeIntent().launchIn(scope)
    }

    @Deprecated(message = "hidden for use", level = DeprecationLevel.HIDDEN)
    final override fun executeIntent(intent: Intent) {
        flow.tryEmit(updateValue(intent))
    }

    /**
     * Returns the current value of the [MutableStateFlow]. Is supposed to be used in
     * [updateValue] when nothing needs to be returned.
     */
    protected fun currentValue(): Value {
        return flow.value
    }

    /**
     * Executes the intents by transforming the [MutableStateFlow] to
     * a flow of any - [Message] or [Label].
     */
    protected open fun Flow<Value>.executeIntent(): Flow<Any> {
        return emptyFlow()
    }

    /**
     * Updates the state based on the given [intent].
     *
     * Override this method to define how the state should be updated based on different intents.
     * If it's necessary not to return new value, then use [currentValue] function to
     * return current value.
     */
    protected abstract fun updateValue(intent: Intent): Value
}
