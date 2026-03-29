package dev.esbi.mizan.feature.accountgroups.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import dev.esbi.mizan.feature.accountgroups.presentation.store.AccountGroupStore.State

internal class AccountGroupReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State =
        when (msg) {
            is Message.GroupsLoaded -> copy(groups = msg.groups, isLoading = false)
            is Message.Loading -> copy(isLoading = msg.isLoading)
        }
}
