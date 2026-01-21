# Transactions Hub Implementation Guide

## Overview
The Transactions Hub is a comprehensive transaction management feature that integrates 5 distinct view modes into a unified interface, providing users with multiple perspectives for analyzing their financial data.

## Architecture

### Main Components

#### 1. **TransactionsHubScreen** (`/src/app/screens/TransactionsHubScreen.tsx`)
The main container screen that orchestrates the entire Transactions Hub experience.

**Features:**
- Top App Bar with Back, Search, Filter, and Favorite actions
- Dynamic date selector with month navigation
- Horizontal scrollable tab navigation
- Full-screen overlay implementation
- Floating Action Button for adding transactions

**Navigation Flow:**
```
Dashboard → TransactionsHub → Add Transaction
                            ↓
                    5 Different View Modes
```

#### 2. **View Components** (`/src/app/components/transactions/`)

##### A. TransactionsHubDailyView
Groups transactions by date with daily totals.

**Key Features:**
- Date-based grouping
- Daily income/expense/total calculations
- Month summary header
- Chronological transaction list
- Tappable transaction items

**MVI Intent Mapping:**
```kotlin
sealed class DailyViewIntent {
    object LoadMonth : DailyViewIntent()
    data class SelectTransaction(val id: String) : DailyViewIntent()
}

data class DailyViewState(
    val monthSummary: MonthSummary,
    val dailyGroups: List<DailyGroup>,
    val status: LoadStatus
)
```

##### B. TransactionsHubCalendarView
Full-month calendar with visual transaction indicators.

**Key Features:**
- 42-day calendar grid (6 weeks)
- Transaction count indicators
- Income/expense dots
- Date selection
- Today highlighting
- Selected date transaction list

**Android Implementation Notes:**
- Use `CalendarView` or custom compose calendar
- LazyVerticalGrid for calendar days
- State management for selected date
- Color coding for transaction types

##### C. TransactionsHubMonthlyView
Weekly grouped transactions with aggregated totals.

**Key Features:**
- Week-based grouping
- Week range calculation
- Progress bars for income ratio
- Collapsible week sections
- Month overview card

**Week Calculation Logic:**
```kotlin
fun getWeekNumber(date: LocalDate): Int {
    val firstDayOfMonth = date.withDayOfMonth(1)
    val dayOfMonth = date.dayOfMonth
    return ((dayOfMonth + firstDayOfMonth.dayOfWeek.value - 1) / 7) + 1
}
```

##### D. TransactionsHubSummaryView
Comprehensive financial overview with multiple data dimensions.

**Key Features:**
- Account breakdown cards
- Budget progress with color-coded status
- Top 5 spending categories
- Export to Excel functionality
- Visual progress indicators

**State Structure:**
```kotlin
data class SummaryViewState(
    val monthTotals: MonthTotals,
    val accounts: List<AccountSummary>,
    val budgetStatus: BudgetStatus,
    val topCategories: List<CategoryBreakdown>,
    val status: LoadStatus
)
```

##### E. TransactionsHubDescriptionView
AI-powered smart grouping by descriptions and patterns.

**Key Features:**
- AI-generated insight groups
- User description grouping
- Pattern detection
- Smart categorization badges
- Collapsible group sections

**AI Insights Implementation:**
```kotlin
interface TransactionInsightsAnalyzer {
    fun detectPatterns(transactions: List<Transaction>): List<InsightGroup>
    fun groupBySimilarity(transactions: List<Transaction>): List<DescriptionGroup>
}
```

## Design System Integration

### Colors
All views use the Mizan premium theme:
- **Primary Accent**: Emerald Green (`#10b981`)
- **Income**: Success gradient (`#4facfe` → `#00f2fe`)
- **Expense**: Error red (`#ff6b6b`)
- **Glass Background**: `rgba(255, 255, 255, 0.7)` (light) / `rgba(255, 255, 255, 0.05)` (dark)

### Typography
Follows 8dp grid system:
- **heading-xl**: 28px, weight 600
- **heading-lg**: 24px, weight 600
- **heading-md**: 20px, weight 600
- **heading-sm**: 18px, weight 600
- **body-md**: 14px, weight 400
- **body-sm**: 12px, weight 400
- **body-xs**: 11px, weight 400

### Spacing
Strict 8dp grid:
- xs: 4dp
- sm: 8dp
- md: 16dp
- lg: 24dp
- xl: 32dp
- 2xl: 40dp

### Border Radius
- xs: 8dp
- sm: 12dp
- md: 16dp
- lg: 20dp
- xl: 24dp
- full: 9999dp

## State Management (MVI Pattern)

### Global State
```kotlin
data class TransactionsHubState(
    val activeView: ViewMode,
    val currentDate: LocalDate,
    val transactions: List<Transaction>,
    val filters: TransactionFilters,
    val isFavorite: Boolean,
    val loadStatus: LoadStatus
)

sealed class ViewMode {
    object Daily : ViewMode()
    object Calendar : ViewMode()
    object Monthly : ViewMode()
    object Summary : ViewMode()
    object Description : ViewMode()
}
```

### Intents
```kotlin
sealed class TransactionsHubIntent {
    data class ChangeView(val mode: ViewMode) : TransactionsHubIntent()
    object NavigateBack : TransactionsHubIntent()
    object OpenSearch : TransactionsHubIntent()
    object OpenFilter : TransactionsHubIntent()
    object ToggleFavorite : TransactionsHubIntent()
    data class NavigateMonth(val direction: Direction) : TransactionsHubIntent()
    data class SelectTransaction(val transaction: Transaction) : TransactionsHubIntent()
    object AddTransaction : TransactionsHubIntent()
}
```

### Effects
```kotlin
sealed class TransactionsHubEffect {
    object NavigateToAddTransaction : TransactionsHubEffect()
    object NavigateBack : TransactionsHubEffect()
    data class ShowTransactionDetails(val id: String) : TransactionsHubEffect()
    object ShowSearchDialog : TransactionsHubEffect()
    object ShowFilterDialog : TransactionsHubEffect()
}
```

## Animations

### View Transitions
- **Tab Switch**: 200ms fade + scale
- **Month Navigation**: 300ms slide horizontal
- **Transaction Reveal**: 150ms fade-up stagger (50ms delay per item)
- **FAB**: Scale on press (0.95), glow on idle

### Calendar Interactions
- **Date Selection**: 200ms scale + background color
- **Today Indicator**: 2s pulse animation
- **Transaction Indicators**: 150ms fade-in

## Performance Optimizations

### Lazy Loading
```kotlin
// Daily View - Load transactions on scroll
LazyColumn {
    items(
        items = dailyGroups,
        key = { it.date }
    ) { group ->
        DailyGroupCard(group)
    }
}
```

### Memoization
```kotlin
val monthlyGroups = remember(transactions, currentMonth) {
    transactions.groupByWeek(currentMonth)
}

val calendarDays = remember(currentMonth, transactions) {
    generateCalendarDays(currentMonth, transactions)
}
```

### State Hoisting
Keep filtering/grouping logic in ViewModel to prevent recomposition.

## Accessibility

### Content Descriptions
- All icons: Meaningful labels
- Transaction items: "Amount type category on date"
- FAB: "Add new transaction"
- Calendar days: "Date with X transactions"

### Semantic Roles
- Transaction items: `role = Role.Button`
- Calendar grid: `role = Role.Grid`
- Tab bar: `role = Role.TabList`

### Touch Targets
Minimum 48dp for all interactive elements.

## Testing Strategy

### Unit Tests
```kotlin
@Test
fun `groupTransactionsByDate should return correct daily groups`() {
    val transactions = listOf(/* mock data */)
    val result = transactionsGrouper.groupByDate(transactions)
    
    assertEquals(3, result.size)
    assertEquals(150.50, result[0].totalExpense, 0.01)
}

@Test
fun `calculateWeekNumber should return correct week`() {
    val date = LocalDate.of(2026, 1, 15)
    val week = dateUtils.getWeekNumber(date)
    
    assertEquals(3, week)
}
```

### UI Tests
```kotlin
@Test
fun `tapping transaction should navigate to details`() = runComposeTest {
    setContent { TransactionsHubScreen(/* ... */) }
    
    onNodeWithText("Starbucks Coffee").performClick()
    
    verify(navController).navigate("transaction_details/txn_004")
}
```

### Integration Tests
```kotlin
@Test
fun `switching views should preserve date selection`() = runTest {
    val viewModel = TransactionsHubViewModel(repository)
    viewModel.processIntent(ChangeView(ViewMode.Calendar))
    viewModel.processIntent(ChangeView(ViewMode.Daily))
    
    assertEquals(currentMonth, viewModel.state.value.currentDate.month)
}
```

## Data Flow

```
User Action → Intent → ViewModel → Repository → UseCase → DataSource
                                         ↓
                                     State Update
                                         ↓
                                    UI Recompose
```

### Repository Methods
```kotlin
interface TransactionsRepository {
    suspend fun getTransactionsByMonth(month: YearMonth): List<Transaction>
    suspend fun getTransactionById(id: String): Transaction
    suspend fun getAccountSummaries(): List<AccountSummary>
    suspend fun getBudgetStatus(month: YearMonth): BudgetStatus
    suspend fun exportToExcel(month: YearMonth): File
}
```

## Migration from React to Compose

### Component Mapping
| React Component | Compose Equivalent |
|----------------|-------------------|
| `<button>` | `Button` / `IconButton` |
| `<div>` with flexbox | `Column` / `Row` |
| `useState` | `remember { mutableStateOf }` |
| `useEffect` | `LaunchedEffect` |
| CSS transitions | `animateContentSize` / `AnimatedVisibility` |

### Style Translation
```css
/* React/Tailwind */
className="px-[var(--premium-space-lg)] py-[var(--premium-space-md)]"

// Compose
Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
```

## Export Functionality

The Summary View includes an "Export to Excel" feature:

```kotlin
class ExcelExporter(private val context: Context) {
    suspend fun exportTransactions(
        transactions: List<Transaction>,
        month: YearMonth
    ): Result<Uri> = withContext(Dispatchers.IO) {
        try {
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Transactions")
            
            // Headers
            val headerRow = sheet.createRow(0)
            headerRow.createCell(0).setCellValue("Date")
            headerRow.createCell(1).setCellValue("Description")
            headerRow.createCell(2).setCellValue("Category")
            headerRow.createCell(3).setCellValue("Amount")
            headerRow.createCell(4).setCellValue("Type")
            
            // Data rows
            transactions.forEachIndexed { index, txn ->
                val row = sheet.createRow(index + 1)
                row.createCell(0).setCellValue(txn.timestamp.toString())
                row.createCell(1).setCellValue(txn.title)
                row.createCell(2).setCellValue(txn.category)
                row.createCell(3).setCellValue(txn.amount)
                row.createCell(4).setCellValue(txn.type.name)
            }
            
            // Save file
            val file = File(context.cacheDir, "transactions_${month}.xlsx")
            FileOutputStream(file).use { workbook.write(it) }
            
            Result.success(FileProvider.getUriForFile(context, file))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

## Key Implementation Notes for Android

1. **Date Handling**: Use `java.time.LocalDate` for consistency
2. **Calendar Grid**: Use `LazyVerticalGrid(columns = GridCells.Fixed(7))`
3. **Horizontal Tabs**: Use `ScrollableTabRow` from Material 3
4. **FAB Position**: Use `Scaffold` with `floatingActionButton`
5. **Glass Effect**: Use `Modifier.blur()` with semi-transparent backgrounds
6. **Animations**: Prefer `AnimatedContent` for view switching
7. **State Preservation**: Use `rememberSaveable` for configuration changes
8. **Back Handler**: Implement `BackHandler` for proper navigation

## File Structure for Android

```
app/
├── presentation/
│   ├── transactions/
│   │   ├── TransactionsHubScreen.kt
│   │   ├── TransactionsHubViewModel.kt
│   │   ├── TransactionsHubState.kt
│   │   ├── TransactionsHubIntent.kt
│   │   ├── views/
│   │   │   ├── DailyView.kt
│   │   │   ├── CalendarView.kt
│   │   │   ├── MonthlyView.kt
│   │   │   ├── SummaryView.kt
│   │   │   └── DescriptionView.kt
│   │   └── components/
│   │       ├── TransactionCard.kt
│   │       ├── DailyGroupHeader.kt
│   │       ├── CalendarDay.kt
│   │       ├── WeekCard.kt
│   │       └── AccountSummaryCard.kt
├── domain/
│   ├── usecase/
│   │   ├── GetTransactionsByMonthUseCase.kt
│   │   ├── GroupTransactionsByDateUseCase.kt
│   │   ├── GroupTransactionsByWeekUseCase.kt
│   │   ├── GenerateCalendarDaysUseCase.kt
│   │   ├── AnalyzeTransactionPatternsUseCase.kt
│   │   └── ExportTransactionsUseCase.kt
│   └── model/
│       ├── Transaction.kt
│       ├── DailyGroup.kt
│       ├── WeekGroup.kt
│       ├── CalendarDay.kt
│       └── InsightGroup.kt
└── data/
    └── repository/
        └── TransactionsRepositoryImpl.kt
```

## Summary

The Transactions Hub provides a comprehensive, multi-perspective view of financial data through 5 distinct view modes, each optimized for different use cases. The implementation follows MVI pattern, Material 3 design principles, and maintains consistency with Mizan's premium fintech aesthetic. All components are designed to be easily portable to Jetpack Compose with MVIKotlin.
