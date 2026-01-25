package dev.esbi.mizan.mvikotlin.executor

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Store

/**
 * A [CompositeExecutor] that combines multiple Executors to work with a single [Store].
 *
 * This executor is necessary to implement the functionality of multiple Executors for one [Store].
 */
class CompositeExecutor<in Intent : Any, Action : Any, in State : Any, out Message : Any, out Label : Any>(
    private val executors: ExecutorsSet<Intent, Action, State, Message, Label>
) : Executor<Intent, Action, State, Message, Label> {

    override fun init(callbacks: Executor.Callbacks<State, Message, Action, Label>) {
        executors.forEach { it.init(callbacks) }
    }

    override fun executeIntent(intent: Intent) {
        executors.forEach { it.executeIntent(intent) }
    }

    override fun executeAction(action: Action) {
        executors.forEach { it.executeAction(action) }
    }

    override fun dispose() {
        executors.forEach { it.dispose() }
    }
}
