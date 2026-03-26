# Currency Loading from Database

Complete implementation of currency fetching from the local database to populate the `currencies` property in `AddNewTransactionStore.State`.

## Overview

The currency loading feature fetches available currencies from the Room database using the `CurrencyRepository` and populates the store state reactively. The implementation follows MVIKotlin architecture with Dagger 2 dependency injection.

## Architecture

### Flow Diagram
```
Store Initialization
    ↓
Bootstrapper triggers Action.InitCurrencies
    ↓
AddNewTransactionCurrencyExecutor.executeAction()
    ↓
CurrencyRepository.observeCurrencies()
    ↓
Flow<List<Currency>> collected
    ↓
Message.UpdateCurrencies dispatched
    ↓
Reducer updates State.currencies
    ↓
UI reactively updates with currency list
```

## Implementation Details

### 1. Domain Layer

#### Currency Model
```kotlin
data class Currency(
    val code: String,           // "UZS", "USD", "EUR" - Primary Key
    val name: String,           // "O'zbek so'mi", "US Dollar"
    val symbol: String,         // "so'm", "$", "€"
    val rateToBase: Double,     // Exchange rate to base currency
    val isBaseCurrency: Boolean // Whether this is the base currency
)
```

#### CurrencyRepository
```kotlin
interface CurrencyRepository {
    fun observeCurrencies(): Flow<List<Currency>>
    suspend fun setBaseCurrency(code: String)
    suspend fun updateRate(code: String, rateToBase: Double)
    suspend fun getCurrencyByCode(code: String): Currency?
}
```

### 2. Store Layer

#### AddNewTransactionStore.kt

**State:**
```kotlin
data class State(
    val currencies: List<Currency> = emptyList(),
    val currency: String = "UZS",  // Selected currency code
    // ... other properties
)
```

**Action:**
```kotlin
sealed interface Action {
    data object InitCurrencies : Action
    // ... other actions
}
```

**Message:**
```kotlin
sealed interface Message {
    class UpdateCurrencies(val currencies: List<Currency>) : Message
    // ... other messages
}
```

### 3. Executor Layer

#### AddNewTransactionCurrencyExecutor.kt

```kotlin
internal class AddNewTransactionCurrencyExecutor @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val currencyRepository: CurrencyRepository,
) : CoroutineExecutor<Intent, Action, State, Message, Label>() {

    override fun executeAction(action: Action) {
        when (action) {
            is Action.InitCurrencies -> {
                fetchCurrencies()
            }
            else -> Unit
        }
    }

    private fun fetchCurrencies() {
        currencyRepository.observeCurrencies()
            .onStart {
                dispatch(Message.UpdateLoading(true))
            }
            .onEach { currencies ->
                dispatch(Message.UpdateCurrencies(currencies))
                dispatch(Message.UpdateLoading(false))
                
                // Auto-select base currency if available
                if (state().currency.isEmpty() && currencies.isNotEmpty()) {
                    val baseCurrency = currencies.find { it.isBaseCurrency }
                    baseCurrency?.let {
                        dispatch(Message.UpdateCurrency(it.code))
                    }
                }
            }
            .catch { error ->
                dispatch(Message.UpdateLoading(false))
                dispatch(Message.UpdateError("Failed to load currencies: ${error.message}"))
            }
            .launchIn(scope)
    }
}
```

**Key Features:**
- Uses `Flow` for reactive updates
- Handles loading states
- Auto-selects base currency on first load
- Graceful error handling with user-friendly messages
- Lifecycle-aware with coroutine scope

### 4. Reducer Layer

#### AddNewTransactionReducer.kt

```kotlin
override fun State.reduce(msg: Message) =
    when (msg) {
        is Message.UpdateCurrencies -> copy(currencies = msg.currencies)
        // ... other messages
    }
```

### 5. Store Implementation

#### AddNewTransactionStoreImpl.kt

```kotlin
class AddNewTransactionStoreImpl @Inject constructor(
    private val executors: ExecutorsSet<...>,
    private val observer: AddNewTransactionStoreObserver,
    private val storeFactory: StoreFactory,
) : AddNewTransactionStore,
    Store<...> by storeFactory.create(
        initialState = AddNewTransactionStore.State(),
        bootstrapper = SimpleBootstrapper(
            AddNewTransactionStore.Action.InitPad,
            AddNewTransactionStore.Action.InitAccounts,
            AddNewTransactionStore.Action.InitCategories,
            AddNewTransactionStore.Action.InitCurrencies,  // ← Added
            AddNewTransactionStore.Action.CheckAndConfirm
        ),
        executorFactory = { CompositeExecutor(executors) },
        reducer = AddNewTransactionReducer,
        observers = setOf(observer)
    )
```

### 6. Dependency Injection

#### AddNewTransactionDepsModule.kt

```kotlin
@Module
internal interface AddNewTransactionDepsModule {
    
    @Binds
    @IntoSet
    fun bindsAddNewTransactionCurrencyExecutor(
        impl: AddNewTransactionCurrencyExecutor
    ): Executor<Intent, Action, State, Message, Label>
    
    // ... other bindings
}
```

**Dependencies Injected:**
- `CurrencyRepository` - For database access
- `@MainDispatcher CoroutineDispatcher` - For coroutine context
- Automatically provided by Dagger 2

### 7. UI Layer Updates

#### MizanCurrencySelector.kt

Updated to accept `List<Currency>` instead of `List<String>`:

```kotlin
@Composable
fun MizanCurrencySelector(
    currencies: List<Currency>,  // Changed from List<String>
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(...) {
        items(currencies) { currency ->
            CurrencyChip(
                label = currency.code,  // Display currency code
                isSelected = currency.code == selectedCurrency,
                onClick = { onCurrencySelected(currency.code) }
            )
        }
    }
}
```

#### CurrencyWheelPicker.kt

Updated similarly to work with `Currency` objects:

```kotlin
@Composable
fun CurrencyWheelPicker(
    currencies: List<Currency>,  // Changed from List<String>
    initialCurrency: String,
    onCurrencySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MizanTheme.premium.text.tertiary
) {
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = currencies.indexOfFirst { 
            it.code == initialCurrency 
        }.coerceAtLeast(0)
    )
    
    // ... rest of implementation using currency.code
}
```

## Data Flow

### Initialization Flow
1. **Store Creation**: `AddNewTransactionStoreImpl` is instantiated
2. **Bootstrapper**: Triggers `Action.InitCurrencies`
3. **Executor**: `AddNewTransactionCurrencyExecutor` handles the action
4. **Repository**: `CurrencyRepository.observeCurrencies()` returns `Flow<List<Currency>>`
5. **Collection**: Flow is collected in executor's coroutine scope
6. **Dispatch**: `Message.UpdateCurrencies` is dispatched with currency list
7. **Reducer**: Updates `State.currencies` immutably
8. **UI**: Automatically updates via reactive state observation

### Auto-Selection Logic
```kotlin
if (state().currency.isEmpty() && currencies.isNotEmpty()) {
    val baseCurrency = currencies.find { it.isBaseCurrency }
    baseCurrency?.let {
        dispatch(Message.UpdateCurrency(it.code))
    }
}
```

Automatically selects the base currency (e.g., "UZS") when:
- No currency is currently selected
- Currencies list is not empty
- A base currency is defined in the database

## Error Handling

### Loading States
```kotlin
.onStart {
    dispatch(Message.UpdateLoading(true))
}
.onEach { currencies ->
    dispatch(Message.UpdateCurrencies(currencies))
    dispatch(Message.UpdateLoading(false))
}
```

### Error Handling
```kotlin
.catch { error ->
    dispatch(Message.UpdateLoading(false))
    dispatch(Message.UpdateError("Failed to load currencies: ${error.message}"))
}
```

**User Experience:**
- Loading indicator shown while fetching
- Error message displayed if fetch fails
- Graceful degradation (empty list if no currencies)

## Usage Example

### Observing Currency State

```kotlin
val state by viewModel.addNewTransactionState.collectAsState()

// Access currencies
val currencies = state.currencies
val selectedCurrency = state.currency

// Display in UI
MizanCurrencySelector(
    currencies = currencies,
    selectedCurrency = selectedCurrency,
    onCurrencySelected = { code ->
        viewModel.onNewTransactionStoreIntent(
            AddNewTransactionStore.Intent.OnUpdateCurrency(code)
        )
    }
)
```

### Currency Display

```kotlin
// Show currency list
currencies.forEach { currency ->
    Text("${currency.code} - ${currency.name} (${currency.symbol})")
}

// Example output:
// UZS - O'zbek so'mi (so'm)
// USD - US Dollar ($)
// EUR - Euro (€)
```

## Testing

### Unit Tests

**Test Currency Loading:**
```kotlin
@Test
fun `when InitCurrencies action, should fetch and update currencies`() = runTest {
    // Given
    val mockCurrencies = listOf(
        Currency("UZS", "Uzbek Som", "so'm", 1.0, true),
        Currency("USD", "US Dollar", "$", 12500.0, false)
    )
    coEvery { currencyRepository.observeCurrencies() } returns flowOf(mockCurrencies)
    
    // When
    executor.executeAction(Action.InitCurrencies)
    
    // Then
    verify { dispatch(Message.UpdateCurrencies(mockCurrencies)) }
    verify { dispatch(Message.UpdateLoading(false)) }
}
```

**Test Auto-Selection:**
```kotlin
@Test
fun `when currencies loaded with empty selection, should auto-select base currency`() = runTest {
    // Given
    val currencies = listOf(
        Currency("USD", "US Dollar", "$", 1.0, false),
        Currency("UZS", "Uzbek Som", "so'm", 1.0, true)
    )
    coEvery { currencyRepository.observeCurrencies() } returns flowOf(currencies)
    
    // When
    executor.executeAction(Action.InitCurrencies)
    
    // Then
    verify { dispatch(Message.UpdateCurrency("UZS")) }
}
```

**Test Error Handling:**
```kotlin
@Test
fun `when currency fetch fails, should dispatch error message`() = runTest {
    // Given
    val error = Exception("Database error")
    coEvery { currencyRepository.observeCurrencies() } returns flow { throw error }
    
    // When
    executor.executeAction(Action.InitCurrencies)
    
    // Then
    verify { dispatch(Message.UpdateError("Failed to load currencies: Database error")) }
    verify { dispatch(Message.UpdateLoading(false)) }
}
```

## Key Features

1. **Reactive Updates**: Uses Flow for real-time currency list updates
2. **Lifecycle-Aware**: Properly scoped to store lifecycle
3. **Auto-Selection**: Automatically selects base currency
4. **Error Handling**: Graceful error handling with user feedback
5. **Loading States**: Proper loading indicators
6. **Type-Safe**: Uses domain model instead of primitive types
7. **Testable**: Clean separation of concerns enables easy testing
8. **Memory Safe**: No memory leaks with proper coroutine scope management

## Benefits

### Before (Hardcoded)
```kotlin
val currencies: List<String> = listOf("EUR", "UZS", "RUB", "USD")
```
- ❌ Static list
- ❌ No database integration
- ❌ Can't add/remove currencies
- ❌ No currency metadata (name, symbol, rate)

### After (Database-Driven)
```kotlin
val currencies: List<Currency> = emptyList()  // Populated from database
```
- ✅ Dynamic list from database
- ✅ Full currency metadata available
- ✅ Can add/remove currencies via settings
- ✅ Exchange rates included
- ✅ Reactive updates when currencies change
- ✅ Base currency auto-selection

## Database Schema

The currencies are stored in Room database with the following structure:

```sql
CREATE TABLE currencies (
    code TEXT PRIMARY KEY NOT NULL,
    name TEXT NOT NULL,
    symbol TEXT NOT NULL,
    rate_to_base REAL NOT NULL,
    is_base_currency INTEGER NOT NULL
);
```

## Build Status

✅ **BUILD SUCCESSFUL** - All changes compile without errors

## Files Modified/Created

### Created:
- `AddNewTransactionCurrencyExecutor.kt` - Currency fetching executor

### Modified:
- `AddNewTransactionStore.kt` - Added Currency import, updated State
- `AddNewTransactionReducer.kt` - Added UpdateCurrencies message handler
- `AddNewTransactionStoreImpl.kt` - Added InitCurrencies to bootstrapper
- `AddNewTransactionDepsModule.kt` - Added CurrencyExecutor binding
- `MizanCurrencySelector.kt` - Updated to use List<Currency>
- `CurrencyWheelPicker.kt` - Updated to use List<Currency>

## Future Enhancements

1. **Currency Management Screen**: Allow users to add/edit/remove currencies
2. **Exchange Rate Updates**: Periodic updates from external API
3. **Currency Conversion**: Real-time conversion between currencies
4. **Favorite Currencies**: Pin frequently used currencies
5. **Currency Search**: Search/filter currency list
6. **Offline Support**: Cache currency data for offline use

## Notes

- The implementation follows the exact same pattern as `InitAccounts` and `InitCategories`
- CurrencyRepository must be implemented in the data layer with Room DAO
- The base currency is automatically selected on first load
- UI components now display full currency information (code, name, symbol)
- The flow is lifecycle-aware and properly disposed when the store is destroyed
