package dev.esbi.mizan.presentation.feature.accountgroups.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.domain.model.AccountGroup
import dev.esbi.mizan.presentation.feature.accountgroups.model.AccountGroupBottomSheetModel

interface AccountGroupStore :
    Store<AccountGroupStore.Intent, AccountGroupStore.State, AccountGroupStore.Label> {

    sealed interface Intent {
        data class AddOrUpdateGroup(val accountGroup: AccountGroup?) : Intent
        data class DeleteGroup(val id: Long) : Intent
        class DismissAddEditAccountGroupSheet : Intent
        class ShowAddEditAccountGroupSheet(val accountGroup: AccountGroup? = null) : Intent
    }

    data class State(
        val groups: List<AccountGroup> = emptyList(),
        val isLoading: Boolean = false,
        val accountGroupEditBottomSheet: AccountGroupBottomSheetModel? = null
    )

    sealed interface Label {
        data class Error(val message: String) : Label
        data object GroupSaved : Label
    }
}
