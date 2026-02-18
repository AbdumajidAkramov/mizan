package dev.esbi.mizan.feature.premiumaddtransaction.di

import com.arkivanov.mvikotlin.core.store.Executor
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet
import dev.esbi.mizan.di.ScreenScope
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStoreImpl
import dev.esbi.mizan.feature.premiumaddtransaction.store.executors.AddNewTransactionAccountSelectorExecutor
import dev.esbi.mizan.feature.premiumaddtransaction.store.executors.AddNewTransactionAmountInputExecutor
import dev.esbi.mizan.feature.premiumaddtransaction.store.executors.AddNewTransactionCategorySelectorExecutor
import dev.esbi.mizan.feature.premiumaddtransaction.store.executors.AddNewTransactionExecutor
import dev.esbi.mizan.feature.premiumaddtransaction.store.executors.AddNewTransactionPadExecutor
import dev.esbi.mizan.feature.premiumaddtransaction.store.executors.AddNewTransactionTemplatesExecutor

@Module
internal interface AddNewTransactionDepsModule {

    @Binds
    @ScreenScope
    fun bindsAddNewTransactionStore(impl: AddNewTransactionStoreImpl): AddNewTransactionStore

    @Binds
    @IntoSet
    fun bindsAddNewTransactionAccountSelectorExecutor(
        impl: AddNewTransactionAccountSelectorExecutor
    ): Executor<AddNewTransactionStore.Intent,
            AddNewTransactionStore.Action,
            AddNewTransactionStore.State,
            AddNewTransactionStore.Message,
            AddNewTransactionStore.Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionAmountInputExecutor(
        impl: AddNewTransactionAmountInputExecutor
    ): Executor<AddNewTransactionStore.Intent,
            AddNewTransactionStore.Action,
            AddNewTransactionStore.State,
            AddNewTransactionStore.Message,
            AddNewTransactionStore.Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionCategorySelectorExecutor(
        impl: AddNewTransactionCategorySelectorExecutor
    ): Executor<AddNewTransactionStore.Intent,
            AddNewTransactionStore.Action,
            AddNewTransactionStore.State,
            AddNewTransactionStore.Message,
            AddNewTransactionStore.Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionExecutor(
        impl: AddNewTransactionExecutor
    ): Executor<AddNewTransactionStore.Intent,
            AddNewTransactionStore.Action,
            AddNewTransactionStore.State,
            AddNewTransactionStore.Message,
            AddNewTransactionStore.Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionPadExecutor(
        impl: AddNewTransactionPadExecutor
    ): Executor<AddNewTransactionStore.Intent,
            AddNewTransactionStore.Action,
            AddNewTransactionStore.State,
            AddNewTransactionStore.Message,
            AddNewTransactionStore.Label>

    @Binds
    @IntoSet
    fun bindsAddNewTransactionTemplatesExecutor(
        impl: AddNewTransactionTemplatesExecutor
    ): Executor<AddNewTransactionStore.Intent,
            AddNewTransactionStore.Action,
            AddNewTransactionStore.State,
            AddNewTransactionStore.Message,
            AddNewTransactionStore.Label>

}
