# Input to Confirmation Transition Flow

Complete implementation of validation and transition logic from the Input stage to the Confirmation stage in the NewTransaction feature.

## Overview

The confirmation flow validates all required fields (Amount, Account, Category) before allowing the user to proceed to the confirmation screen. If validation fails, appropriate error messages are displayed via toast notifications.

## Architecture

### Flow Diagram
```
User clicks "Next" button
    ↓
Intent.Next dispatched
    ↓
validateAndProceedToConfirmation() executed
    ↓
Validation checks:
    - Amount > 0?
    - Account selected?
    - Category selected?
    - (Transfer) Target account selected?
    ↓
[PASS] → Message.UpdateIsConfirm(true)
    ↓
State.isConfirm = true
    ↓
UI switches to ConfirmTransactionContent
    
[FAIL] → Label.ShowToast(error message)
    ↓
Toast displayed with specific error
    ↓
User remains on input screen
```

## Implementation Details

### 1. Store Layer

#### AddNewTransactionStore.kt

**State:**
```kotlin
data class State(
    val isConfirm: Boolean = false,  // Controls UI switching
    val amountDecimal: BigDecimal = BigDecimal.ZERO,
    val selectedAccount: Account? = null,
    val selectedCategory: Category? = null,
    val targetAccount: Account? = null,  // For transfers
    // ... other properties
)
```

**Intent:**
```kotlin
sealed interface Intent {
    data object Next : Intent  // Triggers validation and transition
    data object Back : Intent  // Goes back from confirmation to input
    // ... other intents
}
```

**Label:**
```kotlin
sealed interface Label {
    class ShowToast(val message: String) : Label  // For validation errors
    // ... other labels
}
```

**Message:**
```kotlin
sealed interface Message {
    class UpdateIsConfirm(val isConfirm: Boolean) : Message
    // ... other messages
}
```

### 2. Executor Layer

#### AddNewTransactionExecutor.kt

**Validation Function:**
```kotlin
private fun validateAndProceedToConfirmation() {
    val state = state()
    
    // Validate required fields
    when {
        state.amountDecimal.toDouble() <= 0.0 -> {
            publish(AddNewTransactionStore.Label.ShowToast("Please enter an amount"))
        }
        
        state.selectedAccount == null -> {
            publish(AddNewTransactionStore.Label.ShowToast("Please select an account"))
        }
        
        state.selectedCategory == null -> {
            publish(AddNewTransactionStore.Label.ShowToast("Please select a category"))
        }
        
        state.transactionType == Transaction.Type.TRANSFER && state.targetAccount == null -> {
            publish(AddNewTransactionStore.Label.ShowToast("Please select a target account for transfer"))
        }
        
        else -> {
            // All validations passed, proceed to confirmation
            dispatch(AddNewTransactionStore.Message.UpdateIsConfirm(true))
        }
    }
}
```

**Intent Handlers:**
```kotlin
override fun executeIntent(intent: AddNewTransactionStore.Intent) {
    when (intent) {
        is AddNewTransactionStore.Intent.Next -> {
            validateAndProceedToConfirmation()
        }

        is AddNewTransactionStore.Intent.Back -> {
            // If in confirmation, go back to input
            if (state().isConfirm) {
                dispatch(AddNewTransactionStore.Message.UpdateIsConfirm(false))
            } else {
                publish(AddNewTransactionStore.Label.BackTo)
            }
        }
        // ... other intents
    }
}
```

### 3. Reducer Layer

#### AddNewTransactionReducer.kt

```kotlin
override fun State.reduce(msg: Message) =
    when (msg) {
        is Message.UpdateIsConfirm -> copy(isConfirm = msg.isConfirm)
        // ... other messages
    }
```

### 4. UI Layer

#### NewTransactionScreen.kt

**Label Handling:**
```kotlin
var toastMessage by remember { mutableStateOf<String?>(null) }

LaunchedEffect(labels) {
    when (val currentLabel = labels) {
        is Label.ShowToast -> {
            toastMessage = currentLabel.message
        }
        // ... other labels
    }
}
```

**UI Switching:**
```kotlin
Box(modifier = Modifier.fillMaxSize()) {
    if (state.isConfirm) {
        // Confirmation screen
        ConfirmTransactionContent(
            state = ConfirmTransactionUiState(
                amount = state.amountDecimal.toString(),
                currencyCode = state.currency,
                transactionType = state.transactionType,
                categoryName = state.selectedCategory?.name,
                accountName = state.selectedAccount?.name.orEmpty(),
                toAccountName = state.targetAccount?.name,
                date = state.transactionDate,
                note = state.note,
                saveAsTemplate = state.saveAsTemplate,
                isLoading = state.isLoading,
            ),
            onNoteChange = { note -> accept(Intent.UpdateNote(note)) },
            onDateClick = { showDatePicker = true },
            onConfirmClick = { accept(Intent.ConfirmSave) },
            onBackClick = { accept(Intent.Back) },
            onSaveAsTemplateChange = { value ->
                accept(Intent.UpdateSaveAsTemplate(value))
            }
        )
    } else {
        // Input screen
        PremiumNewTransaction(
            state = state,
            accept = accept
        )
    }
    
    // Validation error toast
    toastMessage?.let { message ->
        MizanToast(
            message = message,
            status = MizanToastStatus.ATTENTION,
            isVisible = true,
            onDismiss = { toastMessage = null }
        )
    }
}
```

## Validation Rules

### Required Fields

1. **Amount** (`amountDecimal`)
   - Must be greater than 0
   - Error: "Please enter an amount"

2. **Account** (`selectedAccount`)
   - Must not be null
   - Error: "Please select an account"

3. **Category** (`selectedCategory`)
   - Must not be null
   - Error: "Please select a category"

4. **Target Account** (`targetAccount`) - *Only for TRANSFER type*
   - Must not be null when transaction type is TRANSFER
   - Error: "Please select a target account for transfer"

### Validation Order

Validations are checked in the following order:
1. Amount
2. Account
3. Category
4. Target Account (if transfer)

The first failed validation stops the process and shows its error message.

## Usage Examples

### Triggering Validation

From the UI (e.g., "Next" button):
```kotlin
Button(onClick = {
    viewModel.onNewTransactionStoreIntent(
        AddNewTransactionStore.Intent.Next
    )
}) {
    Text("Next to Confirm")
}
```

### Going Back from Confirmation

```kotlin
// In ConfirmTransactionContent
IconButton(onClick = {
    viewModel.onNewTransactionStoreIntent(
        AddNewTransactionStore.Intent.Back
    )
}) {
    Icon(Icons.Default.ArrowBack, "Back")
}
```

### Observing Confirmation State

```kotlin
val state by viewModel.addNewTransactionState.collectAsState()

if (state.isConfirm) {
    // Show confirmation UI
} else {
    // Show input UI
}
```

## User Experience Flow

### Happy Path
1. User enters amount
2. User selects account
3. User selects category
4. User clicks "Next"
5. ✅ Validation passes
6. UI smoothly transitions to confirmation screen
7. User reviews details
8. User clicks "Confirm & Save"
9. Transaction is saved

### Validation Error Path
1. User enters amount
2. User clicks "Next" (without selecting account/category)
3. ❌ Validation fails
4. Toast appears: "Please select an account"
5. User selects account
6. User clicks "Next" again
7. ❌ Validation fails
8. Toast appears: "Please select a category"
9. User selects category
10. User clicks "Next"
11. ✅ Validation passes
12. UI transitions to confirmation screen

## Testing

### Unit Tests

**Test Validation Logic:**
```kotlin
@Test
fun `when Next intent with invalid amount, should show error toast`() {
    // Given
    val state = State(amountDecimal = BigDecimal.ZERO)
    
    // When
    executor.executeIntent(Intent.Next)
    
    // Then
    verify { publish(Label.ShowToast("Please enter an amount")) }
    verify(exactly = 0) { dispatch(Message.UpdateIsConfirm(true)) }
}

@Test
fun `when Next intent with all valid fields, should transition to confirmation`() {
    // Given
    val state = State(
        amountDecimal = BigDecimal("100.00"),
        selectedAccount = mockAccount(),
        selectedCategory = mockCategory()
    )
    
    // When
    executor.executeIntent(Intent.Next)
    
    // Then
    verify { dispatch(Message.UpdateIsConfirm(true)) }
    verify(exactly = 0) { publish(any<Label.ShowToast>()) }
}
```

**Test Back Navigation:**
```kotlin
@Test
fun `when Back intent from confirmation, should return to input`() {
    // Given
    val state = State(isConfirm = true)
    
    // When
    executor.executeIntent(Intent.Back)
    
    // Then
    verify { dispatch(Message.UpdateIsConfirm(false)) }
}
```

### Integration Tests

```kotlin
@Test
fun `complete flow from input to confirmation and back`() {
    // 1. Start in input mode
    assertEquals(false, store.state.value.isConfirm)
    
    // 2. Try to proceed without data
    store.accept(Intent.Next)
    assertEquals(false, store.state.value.isConfirm)
    
    // 3. Fill in all required fields
    store.accept(Intent.OnAccountSelected(mockAccount()))
    store.accept(Intent.OnCategorySelected(mockCategory()))
    store.accept(Intent.OnNumberClick(Keypad.ONE))
    
    // 4. Proceed to confirmation
    store.accept(Intent.Next)
    assertEquals(true, store.state.value.isConfirm)
    
    // 5. Go back to input
    store.accept(Intent.Back)
    assertEquals(false, store.state.value.isConfirm)
}
```

## Key Features

1. **Comprehensive Validation**: All required fields checked before proceeding
2. **User-Friendly Errors**: Specific error messages for each validation failure
3. **Smooth Transitions**: Clean UI switching between input and confirmation
4. **Reversible Flow**: Users can go back from confirmation to edit
5. **Type Safety**: MVI architecture ensures type-safe state management
6. **Testable**: Clear separation of concerns enables easy testing

## Error Messages

| Validation | Error Message |
|-----------|---------------|
| Amount <= 0 | "Please enter an amount" |
| No account selected | "Please select an account" |
| No category selected | "Please select a category" |
| No target account (transfer) | "Please select a target account for transfer" |

## Build Status

✅ **BUILD SUCCESSFUL** - All changes compile without errors

## Notes

- The `isConfirm` boolean flag controls the entire UI switching logic
- Toast messages are displayed using the `MizanToast` component
- The validation order ensures users get the most relevant error first
- Back navigation from confirmation returns to input without losing data
- All state changes are reactive and immediately reflected in the UI
