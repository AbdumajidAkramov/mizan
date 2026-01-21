# Android Translation Guide
## React → Jetpack Compose Migration Reference

This guide provides 1:1 mappings for translating this React/TypeScript expense manager to Jetpack Compose/Kotlin.

---

## 🎨 Design Tokens Translation

### CSS Variables → Compose Theme

#### React (theme.css)
```css
:root {
  --color-primary: #6750A4;
  --color-on-primary: #FFFFFF;
  --spacing-md: 16px;
  --elevation-1: 0px 1px 2px rgba(0, 0, 0, 0.3);
}
```

#### Kotlin (Theme.kt)
```kotlin
val md_theme_light_primary = Color(0xFF6750A4)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)

object Spacing {
    val md = 16.dp
}

val Elevation1 = 2.dp
```

---

## 📦 Type System Translation

### TypeScript → Kotlin

#### Transaction Model
**TypeScript (domain.ts)**
```typescript
interface Transaction {
  id: string;
  title: string;
  amount: number;
  category: TransactionCategory;
  timestamp: string;
  type: TransactionType;
  notes?: string;
}

type TransactionType = 'expense' | 'income';
```

**Kotlin (Domain.kt)**
```kotlin
data class Transaction(
    val id: String,
    val title: String,
    val amount: Double,
    val category: TransactionCategory,
    val timestamp: String, // ISO 8601
    val type: TransactionType,
    val notes: String? = null
)

enum class TransactionType {
    EXPENSE, INCOME
}
```

#### UiState Pattern
**TypeScript (domain.ts)**
```typescript
interface UiState<T> {
  status: 'idle' | 'loading' | 'success' | 'error' | 'empty';
  data?: T;
  error?: string;
}
```

**Kotlin (UiState.kt)**
```kotlin
sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    object Empty : UiState<Nothing>()
}
```

---

## 🧩 Component Translation

### Atoms Level

#### Button Component
**React (Button.tsx)**
```typescript
export function Button({
  children,
  variant = 'filled',
  size = 'medium',
  onClick,
}: ButtonProps) {
  return (
    <button
      onClick={onClick}
      className={`
        rounded-full
        ${variantStyles[variant]}
        ${sizeStyles[size]}
      `}
    >
      {children}
    </button>
  );
}
```

**Compose (Button.kt)**
```kotlin
@Composable
fun AppButton(
    text: String,
    variant: ButtonVariant = ButtonVariant.Filled,
    size: ButtonSize = ButtonSize.Medium,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = when (variant) {
        ButtonVariant.Filled -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
        ButtonVariant.Outlined -> ButtonDefaults.outlinedButtonColors()
        // ... other variants
    }
    
    Button(
        onClick = onClick,
        colors = colors,
        shape = RoundedCornerShape(100.dp),
        modifier = modifier.height(
            when (size) {
                ButtonSize.Small -> 32.dp
                ButtonSize.Medium -> 40.dp
                ButtonSize.Large -> 48.dp
            }
        )
    ) {
        Text(text)
    }
}
```

#### CategoryIcon Component
**React (CategoryIcon.tsx)**
```typescript
const CATEGORY_ICONS: Record<TransactionCategory, typeof Utensils> = {
  food: Utensils,
  transport: Car,
  // ...
};

export function CategoryIcon({ category, size = 24 }: CategoryIconProps) {
  const IconComponent = CATEGORY_ICONS[category];
  return <IconComponent size={size} />;
}
```

**Compose (CategoryIcon.kt)**
```kotlin
@Composable
fun CategoryIcon(
    category: TransactionCategory,
    size: Dp = 24.dp,
    modifier: Modifier = Modifier
) {
    val icon = when (category) {
        TransactionCategory.FOOD -> Icons.Outlined.Restaurant
        TransactionCategory.TRANSPORT -> Icons.Outlined.DirectionsCar
        // ...
    }
    
    Icon(
        imageVector = icon,
        contentDescription = category.name,
        modifier = modifier.size(size)
    )
}
```

#### LoadingSkeleton
**React (LoadingSkeleton.tsx)**
```typescript
export function LoadingSkeleton({
  width = '100%',
  height = 16,
  radius = 'md',
}: LoadingSkeletonProps) {
  return (
    <div
      className="bg-[var(--color-surface-variant)] animate-pulse"
      style={{ width, height: `${height}px` }}
    />
  );
}
```

**Compose (LoadingSkeleton.kt)**
```kotlin
@Composable
fun LoadingSkeleton(
    modifier: Modifier = Modifier,
    width: Dp = Dp.Unspecified,
    height: Dp = 16.dp,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    Box(
        modifier = modifier
            .then(if (width != Dp.Unspecified) Modifier.width(width) else Modifier.fillMaxWidth())
            .height(height)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = shape
            )
            .shimmerEffect() // Custom shimmer modifier
    )
}
```

---

### Molecules Level

#### TransactionListItem
**React (TransactionListItem.tsx)**
```typescript
export function TransactionListItem({ transaction, onClick }: TransactionListItemProps) {
  return (
    <button
      onClick={() => onClick?.(transaction)}
      className="bg-[var(--color-surface)] rounded-2xl p-[var(--spacing-md)]"
    >
      <CategoryIcon category={transaction.category} size={24} />
      <div>
        <h4>{transaction.title}</h4>
        <p>{formattedDate}</p>
      </div>
      <AmountText amount={transaction.amount} type={transaction.type} />
    </button>
  );
}
```

**Compose (TransactionListItem.kt)**
```kotlin
@Composable
fun TransactionListItem(
    transaction: Transaction,
    onClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onClick(transaction) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon
            Surface(
                shape = CircleShape,
                color = getCategoryColor(transaction.category).copy(alpha = 0.2f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CategoryIcon(category = transaction.category, size = 24.dp)
                }
            }
            
            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = formatDate(transaction.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Amount
            AmountText(
                amount = transaction.amount,
                type = transaction.type
            )
        }
    }
}
```

#### BalanceCard
**React (BalanceCard.tsx)**
```typescript
export function BalanceCard({
  totalBalance,
  monthlyExpenses,
  savingsAmount,
}: BalanceCardProps) {
  return (
    <div className="rounded-[28px] p-[var(--spacing-lg)] bg-gradient-to-br from-[var(--color-primary)] to-[var(--color-secondary)]">
      <Wallet size={20} />
      <p>Total Balance</p>
      <h2>${totalBalance.toFixed(2)}</h2>
      {/* Sub cards */}
    </div>
  );
}
```

**Compose (BalanceCard.kt)**
```kotlin
@Composable
fun BalanceCard(
    totalBalance: Double,
    monthlyExpenses: Double,
    savingsAmount: Double,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccountBalanceWallet,
                    contentDescription = "Balance",
                    tint = Color.White.copy(alpha = 0.9f)
                )
                Text(
                    text = "Total Balance",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
            
            Text(
                text = "$${"%.2f".format(totalBalance)}",
                style = MaterialTheme.typography.displayMedium,
                color = Color.White
            )
            
            // Sub cards row...
        }
    }
}
```

---

### Organisms Level

#### TransactionList with UiState
**React (TransactionList.tsx)**
```typescript
export function TransactionList({ transactionsState }: TransactionListProps) {
  if (transactionsState.status === 'loading') {
    return <LoadingSkeleton />;
  }
  
  if (transactionsState.status === 'error') {
    return <ErrorState message={transactionsState.error} />;
  }
  
  if (transactionsState.status === 'empty') {
    return <EmptyState />;
  }
  
  return (
    <div>
      {transactionsState.data.map(transaction => (
        <TransactionListItem key={transaction.id} transaction={transaction} />
      ))}
    </div>
  );
}
```

**Compose (TransactionList.kt)**
```kotlin
@Composable
fun TransactionList(
    transactionsState: UiState<List<Transaction>>,
    onTransactionClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    when (transactionsState) {
        is UiState.Loading -> {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(5) {
                    LoadingSkeleton(height = 80.dp)
                }
            }
        }
        
        is UiState.Error -> {
            ErrorState(message = transactionsState.message)
        }
        
        is UiState.Empty -> {
            EmptyState(
                icon = Icons.Outlined.Receipt,
                title = "No Transactions Yet",
                description = "Start tracking expenses"
            )
        }
        
        is UiState.Success -> {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
            ) {
                items(
                    items = transactionsState.data,
                    key = { it.id }
                ) { transaction ->
                    TransactionListItem(
                        transaction = transaction,
                        onClick = onTransactionClick
                    )
                }
            }
        }
        
        is UiState.Idle -> { /* No-op */ }
    }
}
```

---

## 📊 Charts Translation

### React (recharts) → Compose (Vico/MPAndroidChart)

**React - Line Chart**
```typescript
<LineChart data={weeklySpending}>
  <XAxis dataKey="dayLabel" />
  <YAxis />
  <Tooltip />
  <Line 
    type="monotone" 
    dataKey="totalAmount" 
    stroke="var(--color-primary)" 
    strokeWidth={3}
  />
</LineChart>
```

**Compose - Vico Chart**
```kotlin
val chartEntryModel = entryModelOf(
    weeklySpending.mapIndexed { index, day ->
        entryOf(index.toFloat(), day.totalAmount.toFloat())
    }
)

Chart(
    chart = lineChart(),
    model = chartEntryModel,
    startAxis = startAxis(),
    bottomAxis = bottomAxis(
        valueFormatter = { value, _ ->
            weeklySpending.getOrNull(value.toInt())?.dayLabel ?: ""
        }
    ),
    modifier = Modifier.height(200.dp)
)
```

---

## 🧭 Navigation Translation

### React Router → Compose Navigation

**React (App.tsx)**
```typescript
const [activeTab, setActiveTab] = useState<NavigationTab>('home');

const renderScreen = () => {
  switch (activeTab) {
    case 'home': return <DashboardScreen />;
    case 'transactions': return <TransactionsScreen />;
    // ...
  }
};
```

**Compose (Navigation.kt)**
```kotlin
@Composable
fun ExpenseManagerApp() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                onAddExpenseClick = { /* Show dialog */ }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { DashboardScreen() }
            composable("transactions") { TransactionsScreen() }
            composable("statistics") { StatisticsScreen() }
            composable("profile") { ProfileScreen() }
        }
    }
}
```

---

## 🎭 State Management Translation

### React useState → Compose State

**React**
```typescript
const [dashboardState, setDashboardState] = useState<UiState<DashboardSummary>>({
  status: 'loading'
});

useEffect(() => {
  loadDashboardData();
}, []);
```

**Compose (ViewModel)**
```kotlin
class DashboardViewModel : ViewModel() {
    private val _dashboardState = MutableStateFlow<UiState<DashboardSummary>>(UiState.Loading)
    val dashboardState: StateFlow<UiState<DashboardSummary>> = _dashboardState.asStateFlow()
    
    init {
        loadDashboardData()
    }
    
    private fun loadDashboardData() {
        viewModelScope.launch {
            _dashboardState.value = UiState.Loading
            try {
                val data = repository.getDashboardSummary()
                _dashboardState.value = UiState.Success(data)
            } catch (e: Exception) {
                _dashboardState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

// In Composable
val dashboardState by viewModel.dashboardState.collectAsState()
```

---

## 🎨 Styling Translation

### Tailwind/CSS → Compose Modifiers

**React**
```typescript
className="
  flex items-center gap-[var(--spacing-md)]
  bg-[var(--color-surface)]
  rounded-2xl
  p-[var(--spacing-lg)]
  shadow-[var(--elevation-1)]
"
```

**Compose**
```kotlin
modifier = Modifier
    .fillMaxWidth()
    .background(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp)
    )
    .padding(24.dp)
    .shadow(
        elevation = 2.dp,
        shape = RoundedCornerShape(16.dp)
    )
```

---

## 📱 Bottom Navigation Translation

**React (BottomNavigationBar.tsx)**
```typescript
<div className="fixed bottom-0">
  {navigationItems.map(item => (
    <button
      key={item.id}
      onClick={() => onTabChange(item.id)}
      className={isActive ? 'text-primary' : 'text-gray'}
    >
      <Icon />
      <span>{item.label}</span>
    </button>
  ))}
</div>
```

**Compose**
```kotlin
@Composable
fun BottomNavigationBar(
    navController: NavController,
    onAddExpenseClick: () -> Unit
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        
        navigationItems.forEach { item ->
            if (item.isSpecial) {
                // FAB
                FloatingActionButton(
                    onClick = onAddExpenseClick,
                    modifier = Modifier.offset(y = (-16).dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            } else {
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label) },
                    selected = currentDestination?.hierarchy?.any {
                        it.route == item.route
                    } == true,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}
```

---

## 🗂️ File Structure Mapping

### React → Android

```
React/TypeScript                    Kotlin/Compose
================                    ==============
src/types/domain.ts        →        data/model/Domain.kt
src/mocks/data.ts          →        data/repository/MockRepository.kt
src/app/components/atoms/  →        ui/components/atoms/
src/app/components/molecules/ →     ui/components/molecules/
src/app/components/organisms/ →     ui/components/organisms/
src/app/screens/           →        ui/screens/
src/app/App.tsx            →        MainActivity.kt + Navigation.kt
src/styles/theme.css       →        ui/theme/Theme.kt + Color.kt
```

---

## ✅ Migration Checklist

- [ ] Create domain models (data classes)
- [ ] Implement UiState sealed class
- [ ] Create color scheme from CSS variables
- [ ] Define spacing object (8dp grid)
- [ ] Create atom-level composables
- [ ] Create molecule-level composables
- [ ] Create organism-level composables
- [ ] Create screen composables
- [ ] Set up navigation graph
- [ ] Create ViewModels for state management
- [ ] Implement repository layer
- [ ] Add dependency injection (Hilt)
- [ ] Implement Material 3 theming
- [ ] Add animations and transitions
- [ ] Write unit tests
- [ ] Write UI tests

---

## 🔧 Recommended Libraries

```kotlin
// build.gradle.kts
dependencies {
    // Compose
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose")
    
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
    
    // Charts
    implementation("com.patrykandpatrick.vico:compose")
    // Or
    implementation("com.github.PhilJay:MPAndroidChart")
    
    // Dependency Injection
    implementation("com.google.dagger:hilt-android")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android")
}
```

---

**This guide provides direct 1:1 translations for all major patterns and components.**

**Follow the type system, state management, and component structure exactly as defined for seamless Android implementation.**
