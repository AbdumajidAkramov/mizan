package dev.esbi.mizan.mvikotlin.observer

/**
 * A typealias representing a set of StoreObservers.
 *
 * This typealias is useful for Dagger Multibinding into a set.
 */
typealias StoreObserversSet<Intent, Action, Message, Label> =
        Set<@JvmSuppressWildcards StoreObserver<Intent, Action, Message, Label>>
