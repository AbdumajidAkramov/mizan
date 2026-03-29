package dev.esbi.mizan.feature.accountgroups.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.domain.model.AccountGroup
import dev.esbi.mizan.domain.model.AccountGroupType

interface AccountGroupStore : Store<AccountGroupStore.Intent, AccountGroupStore.State, AccountGroupStore.Label> {

    sealed interface Intent {
        data class AddOrUpdateGroup(
            val id: Long = 0L,
            val name: String,
            val iconName: String?,
            val type: AccountGroupType
        ) : Intent
        data class DeleteGroup(val id: Long) : Intent
    }

    data class State(
        val groups: List<AccountGroup> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed interface Label {
        data class Error(val message: String) : Label
        data object GroupSaved : Label
    }
}
