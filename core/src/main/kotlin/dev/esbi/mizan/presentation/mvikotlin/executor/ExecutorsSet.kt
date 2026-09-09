package dev.esbi.mizan.presentation.mvikotlin.executor

import com.arkivanov.mvikotlin.core.store.Executor

/**
 * A typealias representing a set of Executors.
 *
 * This typealias is useful for Dagger Multibinding into a set.
 */
typealias ExecutorsSet<Intent, Action, State, Message, Label> =
        Set<@JvmSuppressWildcards Executor<Intent, Action, State, Message, Label>>
