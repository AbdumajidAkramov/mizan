package dev.esbi.mizan.feature.premiumaddtransaction.di

import com.arkivanov.mvikotlin.core.store.Executor
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.premiumaddtransaction.store.executors.AddNewTransactionCurrencyExecutor
import dev.esbi.mizan.feature.premiumaddtransaction.store.executors.AddNewTransactionExecutor
import dev.esbi.mizan.feature.premiumaddtransaction.store.executors.AddNewTransactionLoadExecutor
import dev.esbi.mizan.feature.premiumaddtransaction.store.executors.AddNewTransactionPadExecutor
import dev.esbi.mizan.presentation.feature.premiumaddtransaction.store.AddNewTransactionStore
import dev.esbi.mizan.presentation.feature.premiumaddtransaction.store.AddNewTransactionStore.Action
import dev.esbi.mizan.presentation.feature.premiumaddtransaction.store.AddNewTransactionStore.Intent
import dev.esbi.mizan.presentation.feature.premiumaddtransaction.store.AddNewTransactionStore.Label
import dev.esbi.mizan.presentation.feature.premiumaddtransaction.store.AddNewTransactionStore.Message
import dev.esbi.mizan.presentation.feature.premiumaddtransaction.store.AddNewTransactionStore.State
import dev.esbi.mizan.presentation.feature.premiumaddtransaction.store.AddNewTransactionStoreImpl
import dev.esbi.mizan.presentation.feature.premiumaddtransaction.store.executors.AddNewTransactionAccountSelectorExecutor
import dev.esbi.mizan.presentation.feature.premiumaddtransaction.store.executors.AddNewTransactionAmountInputExecutor
import dev.esbi.mizan.presentation.feature.premiumaddtransaction.store.executors.AddNewTransactionCalculatorExecutor
import dev.esbi.mizan.presentation.feature.premiumaddtransaction.store.executors.AddNewTransactionCategorySelectorExecutor
import dev.esbi.mizan.presentation.feature.premiumaddtransaction.store.executors.AddNewTransactionConfirmExecutor
import dev.esbi.mizan.presentation.feature.premiumaddtransaction.store.executors.AddNewTransactionTemplatesExecutor

@Module
internal interface AddNewTransactionDepsModule {

    @Binds
    @ScreenScope
    fun bindsAddNewTransactionStore(impl: AddNewTransactionStoreImpl): AddNewTransactionStore

    @Binds
    @IntoSet
    fun bindsAddNewTransactionAccountSelectorExecutor(
        impl: AddNewTransactionAccountSelectorExecutor
    ): Executor<Intent, Action, State, Message, Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionAmountInputExecutor(
        impl: AddNewTransactionAmountInputExecutor
    ): Executor<Intent, Action, State, Message, Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionCategorySelectorExecutor(
        impl: AddNewTransactionCategorySelectorExecutor
    ): Executor<Intent, Action, State, Message, Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionExecutor(
        impl: AddNewTransactionExecutor
    ): Executor<Intent, Action, State, Message, Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionPadExecutor(
        impl: AddNewTransactionPadExecutor
    ): Executor<Intent, Action, State, Message, Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionTemplatesExecutor(
        impl: AddNewTransactionTemplatesExecutor
    ): Executor<Intent, Action, State, Message, Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionConfirmExecutor(
        impl: AddNewTransactionConfirmExecutor
    ): Executor<Intent, Action, State, Message, Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionCalculatorExecutor(
        impl: AddNewTransactionCalculatorExecutor
    ): Executor<Intent, Action, State, Message, Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionCurrencyExecutor(
        impl: AddNewTransactionCurrencyExecutor
    ): Executor<Intent, Action, State, Message, Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionLoadExecutor(
        impl: AddNewTransactionLoadExecutor
    ): Executor<Intent, Action, State, Message, Label>

}
