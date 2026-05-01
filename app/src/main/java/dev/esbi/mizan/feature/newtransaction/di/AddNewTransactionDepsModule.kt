package dev.esbi.mizan.feature.newtransaction.di

import com.arkivanov.mvikotlin.core.store.Executor
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStoreImpl
import dev.esbi.mizan.presentation.feature.addtransaction.store.executors.AddNewTransactionAccountSelectorExecutor
import dev.esbi.mizan.presentation.feature.addtransaction.store.executors.AddNewTransactionAmountInputExecutor
import dev.esbi.mizan.presentation.feature.addtransaction.store.executors.AddNewTransactionCalculatorExecutor
import dev.esbi.mizan.presentation.feature.addtransaction.store.executors.AddNewTransactionCategorySelectorExecutor
import dev.esbi.mizan.presentation.feature.addtransaction.store.executors.AddNewTransactionConfirmExecutor
import dev.esbi.mizan.presentation.feature.addtransaction.store.executors.AddNewTransactionExchangeRateExecutor
import dev.esbi.mizan.presentation.feature.addtransaction.store.executors.AddNewTransactionTemplatesExecutor

@Module
internal interface AddNewTransactionDepsModule {

    @Binds
    @ScreenScope
    fun bindsAddNewTransactionStore(impl: AddNewTransactionStoreImpl): AddNewTransactionStore

    @Binds
    @IntoSet
    fun bindsAddNewTransactionAccountSelectorExecutor(
        impl: AddNewTransactionAccountSelectorExecutor
    ): Executor<AddNewTransactionStore.Intent, AddNewTransactionStore.Action, AddNewTransactionStore.State, AddNewTransactionStore.Message, AddNewTransactionStore.Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionAmountInputExecutor(
        impl: AddNewTransactionAmountInputExecutor
    ): Executor<AddNewTransactionStore.Intent, AddNewTransactionStore.Action, AddNewTransactionStore.State, AddNewTransactionStore.Message, AddNewTransactionStore.Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionCategorySelectorExecutor(
        impl: AddNewTransactionCategorySelectorExecutor
    ): Executor<AddNewTransactionStore.Intent, AddNewTransactionStore.Action, AddNewTransactionStore.State, AddNewTransactionStore.Message, AddNewTransactionStore.Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionTemplatesExecutor(
        impl: AddNewTransactionTemplatesExecutor
    ): Executor<AddNewTransactionStore.Intent, AddNewTransactionStore.Action, AddNewTransactionStore.State, AddNewTransactionStore.Message, AddNewTransactionStore.Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionConfirmExecutor(
        impl: AddNewTransactionConfirmExecutor
    ): Executor<AddNewTransactionStore.Intent, AddNewTransactionStore.Action, AddNewTransactionStore.State, AddNewTransactionStore.Message, AddNewTransactionStore.Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionCalculatorExecutor(
        impl: AddNewTransactionCalculatorExecutor
    ): Executor<AddNewTransactionStore.Intent, AddNewTransactionStore.Action, AddNewTransactionStore.State, AddNewTransactionStore.Message, AddNewTransactionStore.Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionExchangeRateExecutor(
        impl: AddNewTransactionExchangeRateExecutor
    ): Executor<AddNewTransactionStore.Intent, AddNewTransactionStore.Action, AddNewTransactionStore.State, AddNewTransactionStore.Message, AddNewTransactionStore.Label>

}