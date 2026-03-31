package dev.esbi.mizan.feature.accounts.di

import com.arkivanov.mvikotlin.core.store.Executor
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet
import dev.esbi.mizan.presentation.feature.accounts.store.AccountsExecutor
import dev.esbi.mizan.presentation.feature.accounts.store.AccountsStore.Action
import dev.esbi.mizan.presentation.feature.accounts.store.AccountsStore.Intent
import dev.esbi.mizan.presentation.feature.accounts.store.AccountsStore.Label
import dev.esbi.mizan.presentation.feature.accounts.store.AccountsStore.Message
import dev.esbi.mizan.presentation.feature.accounts.store.AccountsStore.State

@Module
internal interface AccountsModule {

    @Binds
    @IntoSet
    fun bindsAccountsExecutor(impl: AccountsExecutor): Executor<Intent, Action, State, Message, Label>

}
