# Exchange Rate BottomSheet Implementation

## Overview

A specialized **Exchange Rate BottomSheet** component for manual exchange rate adjustment, matching the functional pattern of the MoneyManager app. This implementation provides a highly interactive modal bottom sheet with a custom numeric keypad for precise rate editing.

## Features

### ✅ Core Components

1. **ExchangeRateNumericKeypad** - Custom 4x3 grid numeric keypad
   - Keys: `0-9`, `.`, and `⌫` (Backspace)
   - Glassmorphic styling
   - Haptic feedback on each keypress
   - BigDecimal-safe input handling

2. **ExchangeRateBottomSheet** - Main modal bottom sheet
   - Header with conversion formula display
   - Editable exchange rate input field
   - CBU API sync button (refresh icon)
   - Real-time conversion preview
   - Custom numeric keypad integration
   - Smooth animations and glassmorphic design

3. **ExchangeRateDisplay** - Clickable rate display component
   - Shows current exchange rate
   - Opens bottom sheet on click
   - Glassmorphic card design

### 🎯 Key Features

- **BigDecimal Precision**: Uses scale 12 internally for accurate calculations
- **Haptic Feedback**: Provides tactile feedback on each keypress
- **Real-time Preview**: Shows equivalent amount in main currency as user types
- **CBU API Sync**: One-tap button to fetch latest rate from Central Bank
- **Glassmorphism Design**: Modern blur effects and semi-transparent backgrounds
- **Smooth Animations**: Elegant expand/collapse transitions

## Architecture

### MVI State Management

#### State Fields (AddNewTransactionStore.State)
```kotlin
val isExchangeRateBottomSheetVisible: Boolean = false
val manualExchangeRate: BigDecimal? = null
```

#### Intents
```kotlin
data object OpenExchangeRateBottomSheet : Intent
data object CloseExchangeRateBottomSheet : Intent
data class UpdateManualExchangeRate(val rate: BigDecimal) : Intent
data object SyncExchangeRateFromCBU : Intent
```

#### Messages
```kotlin
class UpdateExchangeRateBottomSheet(val isVisible: Boolean) : Message
class UpdateManualExchangeRate(val rate: BigDecimal?) : Message
```

### Executor

**AddNewTransactionExchangeRateExecutor** handles:
- Opening/closing the bottom sheet
- Updating manual exchange rate
- Syncing rate from CBU API via CurrencyRepository

## Usage

### 1. Basic Integration

```kotlin
@Composable
fun TransactionScreen(
    state: AddNewTransactionStore.State,
    accept: (AddNewTransactionStore.Intent) -> Unit
) {
    // Display exchange rate (clickable)
    if (state.selectedCurrency != null && !state.selectedCurrency.isMainCurrency) {
        ExchangeRateDisplay(
            currencyCode = state.selectedCurrency.code,
            mainCurrencyCode = state.currencies.firstOrNull { it.isMainCurrency }?.code ?: "UZS",
            exchangeRate = state.manualExchangeRate ?: state.selectedCurrency.exchangeRate,
            onClick = {
                accept(AddNewTransactionStore.Intent.OpenExchangeRateBottomSheet)
            }
        )
    }
    
    // Exchange Rate Bottom Sheet
    ExchangeRateBottomSheet(
        isVisible = state.isExchangeRateBottomSheetVisible,
        currencyCode = state.selectedCurrency?.code ?: "",
        mainCurrencyCode = state.currencies.firstOrNull { it.isMainCurrency }?.code ?: "UZS",
        currentRate = state.manualExchangeRate ?: state.selectedCurrency?.exchangeRate ?: BigDecimal.ONE,
        transactionAmount = state.amountDecimal,
        onRateChanged = { newRate ->
            accept(AddNewTransactionStore.Intent.UpdateManualExchangeRate(newRate))
        },
        onSyncRate = {
            accept(AddNewTransactionStore.Intent.SyncExchangeRateFromCBU)
        },
        onDismiss = {
            accept(AddNewTransactionStore.Intent.CloseExchangeRateBottomSheet)
        }
    )
}
```

### 2. Standalone Usage

```kotlin
@Composable
fun MyScreen() {
    var isVisible by remember { mutableStateOf(false) }
    var currentRate by remember { mutableStateOf(BigDecimal("12500.00")) }
    
    ExchangeRateBottomSheet(
        isVisible = isVisible,
        currencyCode = "USD",
        mainCurrencyCode = "UZS",
        currentRate = currentRate,
        transactionAmount = BigDecimal("100.00"),
        onRateChanged = { newRate ->
            currentRate = newRate
        },
        onSyncRate = {
            // Fetch from API
        },
        onDismiss = {
            isVisible = false
        }
    )
}
```

## Component Details

### ExchangeRateNumericKeypad

**Parameters:**
- `onNumberClick: (String) -> Unit` - Callback for number key presses (0-9)
- `onDecimalClick: () -> Unit` - Callback for decimal point press
- `onBackspaceClick: () -> Unit` - Callback for backspace press

**Layout:**
```
1  2  3
4  5  6
7  8  9
.  0  ⌫
```

### ExchangeRateBottomSheet

**Parameters:**
- `isVisible: Boolean` - Controls visibility
- `currencyCode: String` - Selected currency (e.g., "USD")
- `mainCurrencyCode: String` - Main currency (e.g., "UZS")
- `currentRate: BigDecimal` - Current exchange rate
- `transactionAmount: BigDecimal` - Transaction amount for preview
- `onRateChanged: (BigDecimal) -> Unit` - Rate update callback
- `onSyncRate: () -> Unit` - CBU sync callback
- `onDismiss: () -> Unit` - Dismiss callback

**UI Structure:**
```
┌─────────────────────────────┐
│     Exchange Rate           │ <- Header
│  1 USD = ? UZS              │ <- Formula
├─────────────────────────────┤
│ 1 USD = [12500.00] UZS  🔄  │ <- Rate Input + Sync
├─────────────────────────────┤
│ Conversion Preview          │ <- Real-time preview
│ 100.00 USD ≈ 1,250,000 UZS  │
├─────────────────────────────┤
│   1   2   3                 │
│   4   5   6                 │ <- Custom Keypad
│   7   8   9                 │
│   .   0   ⌫                 │
├─────────────────────────────┤
│        Apply                │ <- Action Button
└─────────────────────────────┘
```

### ExchangeRateDisplay

**Parameters:**
- `currencyCode: String` - Selected currency
- `mainCurrencyCode: String` - Main currency
- `exchangeRate: BigDecimal` - Current rate
- `onClick: () -> Unit` - Click callback to open bottom sheet

## Technical Specifications

### Precision
- **Internal Scale**: 12 decimal places for calculations
- **Display Scale**: 2-4 decimal places based on context
- **Type**: `BigDecimal` for all monetary values

### Styling
- **Theme**: Glassmorphism with blur effects
- **Colors**: Emerald accent color from MizanTheme
- **Animations**: Smooth expand/collapse transitions
- **Haptics**: `HapticFeedbackType.LongPress` on keypad

### Performance
- Lazy state initialization with `remember(currentRate)`
- Efficient recomposition with proper state management
- Minimal re-renders through targeted state updates

## Data Flow

```
User Action → Intent → Executor → Message → Reducer → State → UI
```

**Example Flow:**
1. User clicks "Edit" on ExchangeRateDisplay
2. `OpenExchangeRateBottomSheet` intent dispatched
3. Executor updates `isExchangeRateBottomSheetVisible = true`
4. Bottom sheet appears with current rate
5. User edits rate using custom keypad
6. Each keypress updates local state and calls `onRateChanged`
7. `UpdateManualExchangeRate` intent dispatched
8. State updated with new manual rate
9. User clicks "Apply" → bottom sheet dismisses
10. Transaction uses manual rate instead of default

## CBU API Integration

The sync button triggers `SyncExchangeRateFromCBU` intent:

```kotlin
private fun syncExchangeRateFromCBU() {
    scope.launch {
        try {
            val currencyCode = state().selectedCurrency?.code ?: return@launch
            val updatedCurrency = currencyRepository.getCurrencyByCode(currencyCode)
            
            if (updatedCurrency != null) {
                dispatch(Message.UpdateManualExchangeRate(updatedCurrency.exchangeRate))
                publish(Label.ShowToast("Exchange rate updated from CBU"))
            }
        } catch (e: Exception) {
            publish(Label.ShowToast("Error syncing: ${e.message}"))
        }
    }
}
```

## Files Created

### UI Components (`:ui-kit`)
- `ExchangeRateNumericKeypad.kt` - Custom numeric keypad
- `ExchangeRateBottomSheet.kt` - Main bottom sheet component
- `ExchangeRateIntegrationExample.kt` - Usage examples and helper components
- `CurrencyModel.kt` - Simple currency model for UI layer

### MVI Layer (`:presentation`)
- `AddNewTransactionExchangeRateExecutor.kt` - Intent handling and API sync

### Updated Files
- `AddNewTransactionStore.kt` - Added state fields and intents
- `AddNewTransactionReducer.kt` - Added message handlers

## Testing Checklist

- [ ] Open bottom sheet by clicking exchange rate display
- [ ] Enter rate using custom numeric keypad
- [ ] Verify haptic feedback on each keypress
- [ ] Test decimal point (only one allowed)
- [ ] Test backspace functionality
- [ ] Verify real-time conversion preview updates
- [ ] Click sync button to fetch CBU rate
- [ ] Verify toast messages appear
- [ ] Apply rate and verify it's used in transaction
- [ ] Dismiss bottom sheet without applying
- [ ] Test with different currencies
- [ ] Verify BigDecimal precision (12 decimals)

## Future Enhancements

- [ ] Add rate history graph
- [ ] Support for favorite/saved rates
- [ ] Offline rate caching
- [ ] Rate change notifications
- [ ] Multi-currency calculator mode
- [ ] Keyboard shortcuts support

## Dependencies

```kotlin
// ui-kit/build.gradle.kts
dependencies {
    implementation(project(":domain"))
    implementation(libs.androidx.compose.material3)
    // ... other dependencies
}
```

## License

Part of the Mizan Finance App - Internal Documentation
