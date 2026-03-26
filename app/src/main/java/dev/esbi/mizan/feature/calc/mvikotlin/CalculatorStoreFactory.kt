package dev.esbi.mizan.feature.calc.mvikotlin

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.esbi.mizan.feature.calc.mvikotlin.CalculatorStore.Intent
import dev.esbi.mizan.feature.calc.mvikotlin.CalculatorStore.Label
import dev.esbi.mizan.feature.calc.mvikotlin.CalculatorStore.State
import javax.inject.Inject


class CalculatorStoreFactory @Inject constructor(private val storeFactory: StoreFactory) {

    fun create(): CalculatorStore =
        object : CalculatorStore, Store<Intent, State, Label> by storeFactory.create(
            name = "CalculatorStore",
            initialState = State(),
            executorFactory = ::CalculatorExecutor,
            reducer = CalculatorReducer
        ) {}
}