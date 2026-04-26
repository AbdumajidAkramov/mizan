# Selector Pattern Implementation for NewTransaction Flow

Complete implementation of the "Pick and Return" selector pattern for Account and Category selection in the NewTransaction flow.

## Overview

The selector pattern enables the NewTransaction flow to navigate to dedicated selector screens (AccountSelectionScreen and CategorySelectScreen), allow the user to pick a value, and return the selected value back to the NewTransactionStore state.

## Architecture

### Flow Diagram
```
NewTransactionScreen
    ↓ (NavigateToAccountSelector label)
AccountSelectionScreen
    ↓ (User selects account)
    ↓ (OnAccountSelected intent)
NewTransactionStore.State.selectedAccount updated
    ↓ (Navigate back)
NewTransactionScreen (shows selected account)
```

## Implementation Details

### 1. Store Layer Updates

#### AddNewTransactionStore.kt

**State Properties:**
```kotlin
data class State(
    val selectedAccount: Account? = null,
    val selectedCategory: Category? = null,
    // ... other properties
)
```

**New Intents:**
```kotlin
sealed interface Intent {
    // Navigation intents
    data object NavigateToAccountSelector : Intent
    data object NavigateToCategorySelector : Intent
    
    // Callback intents (called when user selects)
    data class OnAccountSelected(val account: Account) : Intent
    data class OnCategorySelected(val category: Category) : Intent
}
```

**New Labels:**
```kotlin
sealed interface Label {
    data object NavigateToAccountSelector : Label
    data object NavigateToCategorySelector : Label
}
```

**Messages:**
```kotlin
sealed interface Message {
    class UpdateSelectedAccount(val account: Account?) : Message
    class UpdateSelectedCategory(val category: Category?) : Message
}
```

### 2. Executor Updates

#### AddNewTransactionExecutor.kt

```kotlin
override fun executeIntent(intent: AddNewTransactionStore.Intent) {
    when (intent) {
        is AddNewTransactionStore.Intent.NavigateToAccountSelector -> {
            publish(AddNewTransactionStore.Label.NavigateToAccountSelector)
        }

        is AddNewTransactionStore.Intent.NavigateToCategorySelector -> {
            publish(AddNewTransactionStore.Label.NavigateToCategorySelector)
        }

        is AddNewTransactionStore.Intent.OnAccountSelected -> {
            dispatch(AddNewTransactionStore.Message.UpdateSelectedAccount(intent.account))
        }

        is AddNewTransactionStore.Intent.OnCategorySelected -> {
            dispatch(AddNewTransactionStore.Message.UpdateSelectedCategory(intent.category))
        }
    }
}
```

### 3. Screen Layer Updates

#### NewTransactionScreen.kt

**Label Handling:**
```kotlin
LaunchedEffect(labels) {
    when (labels) {
        AddNewTransactionStore.Label.NavigateToAccountSelector -> onNavigateToAccountSelector()
        AddNewTransactionStore.Label.NavigateToCategorySelector -> onNavigateToCategorySelector()
        // ... other labels
    }
}
```

### 4. Navigation Layer Updates

#### MizanNavHost.kt

**AccountSelector Navigation:**
```kotlin
composable<NavRoute.AccountSelector> {
    val component = remember { appComponent.accountSelectorComponent().create() }
    val viewModel = component.viewModel
    
    // Get previous ViewModel to pass callback
    val previousEntry = remember(navController.currentBackStackEntry) {
        navController.previousBackStackEntry
    }
    
    val amountInputViewModel = previousEntry?.let {
        remember { appComponent.amountInputComponent().create().viewModel }
    }

    AccountSelectionScreen(
        viewModel = viewModel,
        onClose = { navController.popBackStack() },
        onAccountSelected = { account ->
            // Pass selected account back to NewTransactionStore
            amountInputViewModel?.onNewTransactionStoreIntent(
                AddNewTransactionStore.Intent.OnAccountSelected(account)
            )
            navController.popBackStack()
        },
        onAddAccountClick = {
            navController.navigate(NavRoute.AccountManagement)
        }
    )
}
```

**CategorySelector Navigation:**
```kotlin
composable<NavRoute.CategorySelect> {
    val component = remember { appComponent.categorySelectComponent().create() }
    val viewModel = component.viewModel
    
    val previousEntry = remember(navController.currentBackStackEntry) {
        navController.previousBackStackEntry
    }
    
    val amountInputViewModel = previousEntry?.let {
        remember { appComponent.amountInputComponent().create().viewModel }
    }

    CategorySelectScreen(
        viewModel = viewModel,
        onCategorySelected = { category ->
            // Convert Category type (String-based to Long-based)
            val domainCategory = object : dev.esbi.mizan.domain.model.Category {
                override val id = category.id.toLongOrNull() ?: 0L
                override val name = category.name
                override val type = when (category.type.uppercase()) {
                    "EXPENSE" -> Transaction.Type.EXPENSE
                    "INCOME" -> Transaction.Type.INCOME
                    else -> Transaction.Type.EXPENSE
                }
                override val parentId = category.parentId?.toLongOrNull()
                override val iconName = category.iconName
                override val color = category.color
                override val budgetLimit: Double? = null
                override val isArchived = false
                override val orderIndex = 0
            }
            
            amountInputViewModel?.onNewTransactionStoreIntent(
                AddNewTransactionStore.Intent.OnCategorySelected(domainCategory)
            )
            navController.popBackStack()
        },
        onNavigateBack = { navController.popBackStack() },
        onManageCategories = {
            navController.navigate(NavRoute.ManageCategories)
        }
    )
}
```

## Usage Example

### Triggering Account Selection

From your UI (e.g., a button click):
```kotlin
Button(onClick = {
    viewModel.onNewTransactionStoreIntent(
        AddNewTransactionStore.Intent.NavigateToAccountSelector
    )
}) {
    Text(state.selectedAccount?.name ?: "Select Account")
}
```

### Triggering Category Selection

```kotlin
Button(onClick = {
    viewModel.onNewTransactionStoreIntent(
        AddNewTransactionStore.Intent.NavigateToCategorySelector
    )
}) {
    Text(state.selectedCategory?.name ?: "Select Category")
}
```

### Observing Selected Values

```kotlin
val state by viewModel.addNewTransactionState.collectAsState()

// Display selected account
state.selectedAccount?.let { account ->
    Text("Account: ${account.name}")
}

// Display selected category
state.selectedCategory?.let { category ->
    Text("Category: ${category.name}")
}
```

## Key Features

1. **Decoupled Navigation**: Selector screens are independent and reusable
2. **Type-Safe Callbacks**: Uses MVI intents for type-safe communication
3. **Reactive State Updates**: UI automatically updates when selection changes
4. **Cross-Component Communication**: Uses Dagger 2 components to access ViewModels
5. **Type Conversion**: Handles conversion between different Category implementations

## Important Notes

### Category Type Conversion

The project has two Category types:
- `dev.esbi.mizan.feature.addtransaction.domain.model.Category` (String-based IDs)
- `dev.esbi.mizan.domain.model.Category` (Long-based IDs, interface)

The navigation layer handles conversion between these types automatically.

### ViewModel Access Pattern

The pattern uses `previousBackStackEntry` to access the calling screen's ViewModel:
```kotlin
val previousEntry = remember(navController.currentBackStackEntry) {
    navController.previousBackStackEntry
}

val amountInputViewModel = previousEntry?.let {
    remember { appComponent.amountInputComponent().create().viewModel }
}
```

This ensures the callback can dispatch intents to the correct store instance.

## Testing

### Unit Tests

Test the store logic:
```kotlin
@Test
fun `when OnAccountSelected intent, should update selectedAccount in state`() {
    // Given
    val account = mockAccount()
    
    // When
    store.accept(AddNewTransactionStore.Intent.OnAccountSelected(account))
    
    // Then
    assertEquals(account, store.state.value.selectedAccount)
}
```

### Integration Tests

Test the complete flow:
```kotlin
@Test
fun `when navigating to account selector and selecting account, should update state`() {
    // 1. Trigger navigation
    store.accept(AddNewTransactionStore.Intent.NavigateToAccountSelector)
    
    // 2. Verify navigation label emitted
    assertEquals(
        AddNewTransactionStore.Label.NavigateToAccountSelector,
        store.labels.first()
    )
    
    // 3. Simulate account selection
    store.accept(AddNewTransactionStore.Intent.OnAccountSelected(mockAccount()))
    
    // 4. Verify state updated
    assertNotNull(store.state.value.selectedAccount)
}
```

## Benefits

1. **Clean Separation**: Selector screens are independent features
2. **Reusability**: Selector screens can be used from multiple flows
3. **Type Safety**: Compile-time safety with MVI intents
4. **Testability**: Easy to test with MVI architecture
5. **Maintainability**: Clear data flow and state management

## Build Status

✅ **BUILD SUCCESSFUL** - All changes compile without errors
