package dev.esbi.mizan.feature.newtransaction.categoryselect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.esbi.mizan.feature.newtransaction.categorychooser.ErrorView
import dev.esbi.mizan.feature.newtransaction.categorychooser.LoadingView
import dev.esbi.mizan.feature.newtransaction.store.state.CategorySelectBottomSheetState
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectorBottomSheet(
    state: CategorySelectBottomSheetState,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {},
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = when {
                                state.selectedParentId != null -> {
                                    state.parentCategory?.name ?: "Subcategories"
                                }

                                else -> "Select Category"
                            },
                            style = MizanTheme.premium.typography.headingSm,
                            color = MizanTheme.premium.text.primary
                        )
                    },
                    actions = {
                        IconButton(onClick = onDismiss) {
                            MizanIcon(
                                icon = IconValue(dev.esbi.mizan.ui.utils.Icons.ic_close),
                                contentDescription = "Manage Categories",
                                tint = MizanTheme.premium.text.secondary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MizanTheme.premium.background.primary
                    )
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MizanTheme.premium.background.primary)
            ) {
                when {
                    state.isLoading -> {
                        LoadingView()
                    }

                    state.error != null -> {
                        ErrorView(
                            error = state.error,
                            onRetry = {
//                                accept(CategorySelectStore.Intent.RetryLoad)
                            }
                        )
                    }

                    else -> {
/*
                        CategoryGrid(
                            state = state,
                            onCategoryClick = { category ->
                                if (state.selectedParentId != null) {
                                    accept(
                                        CategorySelectStore.Intent.SelectSubCategory(
                                            category
                                        )
                                    )
                                } else {
                                    accept(
                                        CategorySelectStore.Intent.SelectParentCategory(
                                            category
                                        )
                                    )
                                }
                            }
                        )
*/
                    }
                }
            }
        }
    }

}
