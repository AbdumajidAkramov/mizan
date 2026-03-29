package dev.esbi.mizan.feature.accountgroups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.domain.model.AccountGroupType
import dev.esbi.mizan.feature.accountgroups.components.AddEditAccountGroupSheet
import dev.esbi.mizan.presentation.feature.accountgroups.store.AccountGroupStore
import dev.esbi.mizan.ui.components.account.AccountGroupListItem
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons as MizanIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountGroupManagementScreen(
    store: AccountGroupStore,
    onBackClick: () -> Unit
) {
    val state by store.states.collectAsState(initial = AccountGroupStore.State())
    var showAddSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account Groups", color = MizanTheme.premium.text.primary) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        MizanIcon(
                            icon = IconValue(MizanIcons.ic_arrow_back),
                            tint = MizanTheme.premium.text.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MizanTheme.premium.background.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddSheet = true },
                containerColor = MizanTheme.premium.colors.emerald
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Group", tint = Color.White)
            }
        },
        containerColor = MizanTheme.premium.background.primary
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.groups, key = { it.id }) { group ->
                        AccountGroupListItem(
                            name = group.name,
                            typeLabel = when (group.type) {
                                AccountGroupType.DEFAULT -> "General"
                                AccountGroupType.CREDIT_CARD -> "Credit Card"
                                AccountGroupType.DEBIT_CARD -> "Debit Card"
                            },
                            onClick = { /* Handle Edit / Delete */ },
                            iconContent = {
                                val iconName = when (group.type) {
                                    AccountGroupType.CREDIT_CARD -> "ic_credit_card"
                                    AccountGroupType.DEBIT_CARD -> "ic_card"
                                    else -> "ic_accounts"
                                }
                                MizanIcon(
                                    icon = IconValue(iconName),
                                    tint = MizanTheme.premium.text.primary
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAddSheet) {
        AddEditAccountGroupSheet(
            onDismiss = { showAddSheet = false },
            onSave = { name, type ->
                store.accept(
                    AccountGroupStore.Intent.AddOrUpdateGroup(
                        name = name,
                        iconName = null, // simplified logic
                        type = type
                    )
                )
                showAddSheet = false
            }
        )
    }
}
