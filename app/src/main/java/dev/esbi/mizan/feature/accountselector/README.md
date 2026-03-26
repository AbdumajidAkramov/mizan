# Account Selector Feature

Complete MVIKotlin + Dagger 2 implementation for account selection functionality.

## Architecture

This feature follows the project's standard architecture pattern:

- **MVI Store**: MVIKotlin store with State, Intent, Label pattern
- **Dependency Injection**: Dagger 2 with `@ScreenScope` subcomponent
- **ViewModel**: Lifecycle-aware ViewModel exposing reactive state
- **UI**: Jetpack Compose with Premium design system

## Components

### 1. Store Layer (`store/`)

#### `AccountSelectorStore.kt`
Main store interface defining:
- **State**: `isLoading`, `accounts`, `selectedAccountId`, `error`
- **Intent**: `LoadAccounts`, `SelectAccount`, `RetryLoad`
- **Label**: `AccountSelected`, `ShowError`

#### `AccountSelectorExecutor.kt`
Handles business logic:
- Fetches accounts from `AccountRepository`
- Dispatches state updates via Messages
- Publishes Labels for one-time events

#### `AccountSelectorReducer.kt`
Pure reducer function that updates state based on Messages.

#### `AccountSelectorStoreFactory.kt`
Factory for creating store instances with dependencies.

### 2. ViewModel Layer

#### `AccountSelectorViewModel.kt`
- Manages store lifecycle
- Exposes `state: Flow<State>` and `labels: Flow<Label>`
- Provides `onIntent(intent: Intent)` for user actions
- Automatically loads accounts on initialization

### 3. Dependency Injection (`di/`)

#### `AccountSelectorComponent.kt`
Dagger subcomponent with `@ScreenScope`:
```kotlin
@ScreenScope
@Subcomponent(modules = [AccountSelectorModule::class])
interface AccountSelectorComponent {
    val viewModel: AccountSelectorViewModel
    
    @Subcomponent.Factory
    interface Factory {
        fun create(): AccountSelectorComponent
    }
}
```

#### `AccountSelectorModule.kt`
Provides all dependencies:
- `AccountSelectorExecutor.Factory`
- `AccountSelectorStoreFactory`
- `AccountSelectorViewModel`

### 4. UI Layer

#### `AccountSelectionScreen.kt`
Full-screen composable that:
- Creates component from AppComponent
- Observes state and labels
- Handles navigation callbacks

#### `AccountSelectionContent.kt`
Main UI composable with:
- Loading/error states
- Grouped account list (Cash/Bank)
- Premium glassmorphism design
- Selection highlighting
- Add account button

#### `AccountSelectionContentSimple.kt`
Lightweight version for use in bottom sheets where parent manages state.

## Usage

### 1. Navigation Integration

Add to your navigation graph:

```kotlin
composable<NavRoute.AccountSelector> {
    val component = remember { appComponent.accountSelectorComponent().create() }
    val viewModel = component.viewModel

    AccountSelectionScreen(
        viewModel = viewModel,
        onClose = { navController.popBackStack() },
        onAccountSelected = { account ->
            // Handle account selection
            navController.popBackStack()
        },
        onAddAccountClick = {
            navController.navigate(NavRoute.AccountManagement)
        }
    )
}
```

### 2. Bottom Sheet Integration

For use in existing screens with their own state management:

```kotlin
ModalBottomSheet(
    onDismissRequest = { /* close */ }
) {
    AccountSelectionContentSimple(
        accounts = state.accounts,
        selectedAccount = state.selectedAccount,
        onAccountClick = { account ->
            // Handle selection
        },
        onAddAccountClick = {
            // Navigate to add account
        }
    )
}
```

### 3. Direct ViewModel Usage

If you need to integrate with custom UI:

```kotlin
val component = remember { appComponent.accountSelectorComponent().create() }
val viewModel = component.viewModel
val state by viewModel.state.collectAsState(initial = AccountSelectorStore.State())

// Send intents
viewModel.onIntent(AccountSelectorStore.Intent.LoadAccounts)
viewModel.onIntent(AccountSelectorStore.Intent.SelectAccount(account))

// Observe labels
LaunchedEffect(Unit) {
    viewModel.labels.collect { label ->
        when (label) {
            is AccountSelectorStore.Label.AccountSelected -> {
                // Handle selection
            }
            is AccountSelectorStore.Label.ShowError -> {
                // Show error
            }
        }
    }
}
```

## State Management

### State Properties
- `isLoading: Boolean` - Loading indicator
- `accounts: List<Account>` - All available accounts
- `selectedAccountId: Long?` - Currently selected account ID
- `error: String?` - Error message if any

### Intents (User Actions)
- `LoadAccounts` - Fetch accounts from repository
- `SelectAccount(account: Account)` - Select an account
- `RetryLoad` - Retry loading after error

### Labels (One-time Events)
- `AccountSelected(account: Account)` - Account was selected
- `ShowError(message: String)` - Error occurred

## Design System

The UI uses the Premium design system with:
- **Glassmorphism effects** for cards
- **Emerald accent** for selected state
- **Grouped layout** (Cash/Bank sections)
- **Smooth animations** for interactions
- **Error/loading states** with retry functionality

## Testing

### Unit Tests
Test the store logic:
```kotlin
@Test
fun `when LoadAccounts intent, should fetch accounts from repository`() {
    // Test executor logic
}
```

### Integration Tests
Test the complete flow:
```kotlin
@Test
fun `when account selected, should emit AccountSelected label`() {
    // Test store + reducer + executor
}
```

### UI Tests
Test composables:
```kotlin
@Test
fun `should display accounts in grouped sections`() {
    // Test UI rendering
}
```

## Dependencies

- `AccountRepository` - Data source for accounts
- `StoreFactory` - MVIKotlin store factory
- `CoroutineDispatcher` - For async operations (@MainDispatcher)

## Notes

- Component is scoped to screen lifecycle (`@ScreenScope`)
- Store is lazily initialized in ViewModel
- Accounts are automatically loaded on ViewModel init
- Uses project's existing `Account` and `Currency` domain models
