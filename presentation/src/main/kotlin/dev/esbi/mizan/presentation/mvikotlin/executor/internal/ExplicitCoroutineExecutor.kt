package dev.esbi.mizan.presentation.mvikotlin.executor.internal

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers

/**
 * A [ExplicitCoroutineExecutor] to be able to override the init function.
 */
@Suppress("MaximumLineLength", "MaxLineLength") // internal usage
open class ExplicitCoroutineExecutor<in Intent : Any, Action : Any, State : Any, Message : Any, Label : Any> private constructor(
    private val executor: InternalCoroutineExecutor<Intent, Action, State, Message, Label>,
) : Executor<Intent, Action, State, Message, Label> by executor {
    protected val scope get() = executor.coroutineScope

    constructor(mainContext: CoroutineContext = Dispatchers.Main) : this(
        executor = InternalCoroutineExecutor(mainContext)
    )

    override fun init(callbacks: Executor.Callbacks<State, Message, Action, Label>) {
        executor.init(callbacks)
    }

    protected fun state(): State {
        return executor.state
    }

    protected fun forward(action: Action) {
        executor.forwardAction(action)
    }

    protected fun dispatch(message: Message) {
        executor.dispatchMessage(message)
    }

    protected fun publish(label: Label) {
        executor.publishLabel(label)
    }
}

private class InternalCoroutineExecutor<in Intent : Any, Action : Any, State : Any, Message : Any, Label : Any>(
    mainContext: CoroutineContext,
) : CoroutineExecutor<Intent, Action, State, Message, Label>(mainContext) {
    val coroutineScope get() = scope
    val state get() = state()
    fun forwardAction(action: Action) = forward(action)
    fun dispatchMessage(message: Message) = dispatch(message)
    fun publishLabel(label: Label) = publish(label)
}
