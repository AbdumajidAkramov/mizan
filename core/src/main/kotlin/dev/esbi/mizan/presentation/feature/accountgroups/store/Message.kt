package dev.esbi.mizan.presentation.feature.accountgroups.store

import dev.esbi.mizan.domain.model.AccountGroup
import dev.esbi.mizan.presentation.feature.accountgroups.model.AccountGroupBottomSheetModel

internal sealed interface Message {
    data class GroupsLoaded(val groups: List<AccountGroup>) : Message
    data class Loading(val isLoading: Boolean) : Message
    data class UpdateEditGroupBottomSheetState(val value: AccountGroupBottomSheetModel? = null) : Message
}
