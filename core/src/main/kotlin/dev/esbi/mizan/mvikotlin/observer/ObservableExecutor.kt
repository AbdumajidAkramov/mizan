package dev.esbi.mizan.mvikotlin.observer

import com.arkivanov.mvikotlin.core.store.Executor

/**
 * An ObservableExecutor that combines an Executor with a set of StoreObservers.
 *
 * This class is used to observe and intercept the execution of methods in the underlying
 * Executor by notifying the registered StoreObservers. It acts as a bridge between the Executor
 * and the StoreObservers, allowing for additional functionality and monitoring
 * of the execution process.
 * It can be extended in the future to add more custom behavior or functionality.
 */
class ObservableExecutor<Intent : Any, Action : Any, State : Any, Message : Any, Label : Any>(
    private val executor: Executor<Intent, Action, State, Message, Label>,
    private val observers: StoreObserversSet<Intent, Action, Message, Label>
) : Executor<Intent, Action, State, Message, Label> {

    override fun init(callbacks: Executor.Callbacks<State, Message, Action, Label>) {
        observers.forEach { it.onInit() }
        executor.init(
            object : Executor.Callbacks<State, Message, Action, Label> {
                override val state: State get() = callbacks.state

                override fun onAction(action: Action) {
                    callbacks.onAction(action)
                }

                override fun onMessage(message: Message) {
                    observers.forEach { it.onMessage(message) }
                    callbacks.onMessage(message)
                }

                override fun onLabel(label: Label) {
                    observers.forEach { it.onLabel(label) }
                    callbacks.onLabel(label)
                }
            }
        )
    }

    override fun executeAction(action: Action) {
        observers.forEach { it.onAction(action) }
        executor.executeAction(action)
    }

    override fun executeIntent(intent: Intent) {
        observers.forEach { it.onIntent(intent) }
        executor.executeIntent(intent)
    }

    override fun dispose() {
        observers.forEach { it.onDispose() }
        executor.dispose()
    }
}
