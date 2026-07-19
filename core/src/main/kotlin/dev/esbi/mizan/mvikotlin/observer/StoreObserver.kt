package dev.esbi.mizan.mvikotlin.observer

/**
 * An interface for observing the input parameters of a Store.
 *
 * This component is used to observe the input parameters of a Store. It provides callback
 * methods that can be implemented to handle specific events related to the input parameters,
 * such as initialization, actions, intents, messages, labels, and disposal.
 * It is useful for various purposes, including analytics and logging,
 * as it allows for building logic based on the input parameters of functions.
 */
interface StoreObserver<in Intent : Any, in Action : Any, in Message : Any, in Label : Any> {

    fun onInit() {
    }

    fun onAction(action: Action) {
    }

    fun onIntent(intent: Intent) {
    }

    fun onMessage(message: Message) {
    }

    fun onLabel(label: Label) {
    }

    fun onDispose() {
    }
}
