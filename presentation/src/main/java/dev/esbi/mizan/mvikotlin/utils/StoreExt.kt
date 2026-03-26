package dev.esbi.mizan.mvikotlin.utils

import com.arkivanov.mvikotlin.core.store.Bootstrapper
import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.mvikotlin.executor.CompositeExecutor
import dev.esbi.mizan.mvikotlin.executor.ExecutorsSet
import dev.esbi.mizan.mvikotlin.observer.ObservableExecutor
import dev.esbi.mizan.mvikotlin.observer.StoreObserversSet

/**
 * Creates an implementation of [Store].
 *
 * This function is used to create a Store instance with the given configuration.
 * It is particularly useful when you want to use a [ExecutorsSet] and [StoreObserversSet]
 * to observe of a [CompositeExecutor] callback functions.
 **/
fun <Intent : Any, Action : Any, Message : Any, State : Any, Label : Any> StoreFactory.create(
    name: String? = null,
    autoInit: Boolean = false,
    initialState: State,
    bootstrapper: Bootstrapper<Action>? = null,
    executors: ExecutorsSet<Intent, Action, State, Message, Label>,
    observers: StoreObserversSet<Intent, Action, Message, Label>,
    reducer: Reducer<State, Message>? = null,
): Store<Intent, State, Label> {
    val executor = { CompositeExecutor(executors) }
    return create(name, autoInit, initialState, bootstrapper, executor, observers, reducer)
}

/**
 * Creates an implementation of [Store].
 *
 * This function is used to create a Store instance with the given configuration.
 * It is particularly useful when you want to use a [StoreObserversSet]
 * to observe of a [ObservableExecutor] callback functions.
 **/
@Suppress("BracesOnIfStatements", "LongParameterList")
fun <Intent : Any, Action : Any, Message : Any, State : Any, Label : Any> StoreFactory.create(
    name: String? = null,
    autoInit: Boolean = false,
    initialState: State,
    bootstrapper: Bootstrapper<Action>? = null,
    executorFactory: () -> Executor<Intent, Action, State, Message, Label>,
    observers: StoreObserversSet<Intent, Action, Message, Label>,
    reducer: Reducer<State, Message>? = null,
): Store<Intent, State, Label> {
    val executor = { ObservableExecutor(executorFactory(), observers) }
    return if (reducer == null) create(
        name = name,
        autoInit = autoInit,
        initialState = initialState,
        bootstrapper = bootstrapper,
        executorFactory = executor
    ) else create(
        name = name,
        autoInit = autoInit,
        initialState = initialState,
        bootstrapper = bootstrapper,
        executorFactory = executor,
        reducer = reducer
    )
}
