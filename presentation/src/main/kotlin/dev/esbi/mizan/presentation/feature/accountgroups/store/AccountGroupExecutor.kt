package dev.esbi.mizan.presentation.feature.accountgroups.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.model.AccountGroup
import dev.esbi.mizan.domain.model.AccountGroupType
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.presentation.feature.accountgroups.store.AccountGroupStore.Intent
import dev.esbi.mizan.presentation.feature.accountgroups.store.AccountGroupStore.Label
import dev.esbi.mizan.presentation.feature.accountgroups.store.AccountGroupStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class AccountGroupExecutor(
    mainDispatcher: CoroutineDispatcher,
    private val accountRepository: AccountRepository
) : CoroutineExecutor<Intent, Unit, State, Message, Label>(mainContext = mainDispatcher) {

    override fun executeAction(action: Unit) {
        dispatch(Message.Loading(true))
        scope.launch {
            try {
                accountRepository.observeAccountGroups().collect { groups ->
                    dispatch(Message.GroupsLoaded(groups))
                }
            } catch (e: Exception) {
                dispatch(Message.Loading(false))
                publish(Label.Error(e.message ?: "Failed to load account groups"))
            }
        }
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.AddOrUpdateGroup -> saveGroup(intent)
            is Intent.DeleteGroup -> deleteGroup(intent.id)
        }
    }

    private fun saveGroup(intent: Intent.AddOrUpdateGroup) {
        scope.launch {
            try {
                val group = object : AccountGroup {
                    override val id: Long = intent.id
                    override val name: String = intent.name
                    override val iconName: String? = intent.iconName
                    override val orderIndex: Int? = 0 // simplified or fetch max
                    override val type: AccountGroupType = intent.type
                }
                accountRepository.saveAccountGroup(group)
                publish(Label.GroupSaved)
            } catch (e: Exception) {
                publish(Label.Error(e.message ?: "Failed to save account group"))
            }
        }
    }

    private fun deleteGroup(id: Long) {
        scope.launch {
            try {
                accountRepository.deleteAccountGroup(id)
            } catch (e: Exception) {
                publish(Label.Error(e.message ?: "Failed to delete account group"))
            }
        }
    }
}
