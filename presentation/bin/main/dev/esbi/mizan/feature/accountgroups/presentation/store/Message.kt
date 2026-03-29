package dev.esbi.mizan.feature.accountgroups.presentation.store

import dev.esbi.mizan.domain.model.AccountGroup

internal sealed interface Message {
    data class GroupsLoaded(val groups: List<AccountGroup>) : Message
    data class Loading(val isLoading: Boolean) : Message
}
