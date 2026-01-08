# Component Index - Quick Reference

## 📁 Project Structure

```
src/
├── types/domain.ts                          # All TypeScript interfaces & types
├── mocks/data.ts                            # Mock data (20 entries)
├── styles/theme.css                         # Material 3 design tokens
└── app/
    ├── components/
    │   ├── atoms/                          # Level 1: Atomic components
    │   │   ├── Button.tsx                  # Material 3 button
    │   │   ├── IconButton.tsx              # 48x48dp icon button
    │   │   ├── CategoryIcon.tsx            # Category icon renderer
    │   │   ├── LoadingSkeleton.tsx         # Shimmer loading
    │   │   └── AmountText.tsx              # Formatted currency
    │   ├── molecules/                      # Level 2: Composite components
    │   │   ├── TransactionListItem.tsx     # Transaction row
    │   │   ├── CategoryCard.tsx            # Category summary
    │   │   ├── BalanceCard.tsx             # Balance display
    │   │   ├── BudgetProgressCard.tsx      # Budget tracker
    │   │   ├── EmptyState.tsx              # Empty list state
    │   │   └── ErrorState.tsx              # Error message
    │   └── organisms/                      # Level 3: Complex sections
    │       ├── TransactionList.tsx         # Full transaction list
    │       ├── DashboardSummarySection.tsx # Dashboard content
    │       ├── AddExpenseForm.tsx          # Expense form modal
    │       └── BottomNavigationBar.tsx     # Bottom nav + FAB
    ├── screens/                            # Level 4: Full screens
    │   ├── DashboardScreen.tsx             # Home screen
    │   ├── TransactionsScreen.tsx          # History screen
    │   ├── StatisticsScreen.tsx            # Analytics screen
    │   └── ProfileScreen.tsx               # Settings screen
    └── App.tsx                             # Root component
```

---

## 🎯 Component Hierarchy

### Atoms (7 components)
Basic UI building blocks - cannot be broken down further.

| Component | Purpose | Props | Android Equivalent |
|-----------|---------|-------|-------------------|
| `Button` | Action button | `variant`, `size`, `onClick` | `Button()` |
| `IconButton` | Icon-only button | `icon`, `label`, `onClick` | `IconButton()` |
| `CategoryIcon` | Category icon | `category`, `size` | `Icon()` |
| `LoadingSkeleton` | Loading placeholder | `width`, `height`, `radius` | Shimmer Box |
| `AmountText` | Currency formatter | `amount`, `type`, `showSign` | Text with formatting |

### Molecules (7 components)
Combinations of atoms forming distinct UI elements.

| Component | Atoms Used | Purpose | Android Equivalent |
|-----------|------------|---------|-------------------|
| `TransactionListItem` | CategoryIcon, AmountText | Transaction row | `ListItem()` |
| `CategoryCard` | CategoryIcon, AmountText | Category summary | `Card()` + Content |
| `BalanceCard` | AmountText | Balance display | Gradient Card |
| `BudgetProgressCard` | - | Budget progress | Card with LinearProgressIndicator |
| `EmptyState` | Button (optional) | Empty list state | Empty view composable |
| `ErrorState` | Button | Error message | Error composable |

### Organisms (4 components)
Complex sections combining multiple molecules and atoms.

| Component | Children | State Management | Purpose |
|-----------|----------|------------------|---------|
| `TransactionList` | TransactionListItem, LoadingSkeleton, EmptyState, ErrorState | `UiState<Transaction[]>` | Transaction listing with filters |
| `DashboardSummarySection` | BalanceCard, BudgetProgressCard, Charts | `UiState<DashboardSummary>` | Complete dashboard view |
| `AddExpenseForm` | Button, IconButton, CategoryIcon | Form validation state | Expense creation modal |
| `BottomNavigationBar` | IconButton variants | Navigation state | Bottom nav with FAB |

### Screens (4 components)
Full-page views composing organisms.

| Screen | Organisms Used | Purpose |
|--------|----------------|---------|
| `DashboardScreen` | DashboardSummarySection | Main overview |
| `TransactionsScreen` | TransactionList | Transaction history |
| `StatisticsScreen` | CategoryCard, Charts | Analytics view |
| `ProfileScreen` | - | User settings |

---

## 🎨 Design Tokens Quick Reference

### Colors (Material 3)
```
Primary:     #6750A4
Surface:     #FEFBFF
Error:       #B3261E
Outline:     #79747E
```

### Category Colors
```
Food:         #FF6B6B
Transport:    #4ECDC4
Shopping:     #FFE66D
Bills:        #95E1D3
Entertainment:#C7CEEA
Health:       #FF8585
Travel:       #7B68EE
Tech:         #4CAF50
Income:       #2E7D32
```

### Spacing (8dp Grid)
```
xs:  4px   (0.5×)
sm:  8px   (1×)
md:  16px  (2×)
lg:  24px  (3×)
xl:  32px  (4×)
2xl: 40px  (5×)
3xl: 48px  (6×)
```

### Elevation Shadows
```
elevation-1: Subtle shadow
elevation-2: Medium shadow
elevation-3: Prominent shadow
elevation-4: High shadow
elevation-5: Maximum shadow
```

---

## 🔄 State Management Pattern

### UiState Wrapper
Every data-driven component uses:
```typescript
interface UiState<T> {
  status: 'idle' | 'loading' | 'success' | 'error' | 'empty';
  data?: T;
  error?: string;
}
```

### Visual States Handled

| Status | Visual Representation | Component Used |
|--------|----------------------|----------------|
| `loading` | Shimmer skeletons | `LoadingSkeleton` |
| `success` | Actual content | Data components |
| `error` | Error message + retry | `ErrorState` |
| `empty` | Empty state message | `EmptyState` |
| `idle` | Initial/no action | Nothing rendered |

---

## 📊 Domain Models

### Core Types
```typescript
Transaction          // Single expense/income entry
TransactionCategory  // 'food' | 'transport' | etc.
CategorySpending     // Aggregated category data
DashboardSummary     // Dashboard metrics
UserAccount          // User information
ExpenseFormData      // Form input data
```

### Helper Types
```typescript
UiState<T>          // MVI state wrapper
FormValidation      // Form error state
MonthlyComparison   // Month-to-month comparison
DailySpending       // Daily spending data point
```

---

## 🎯 Props Interface Patterns

### Standard Component Props
```typescript
// Every component has:
interface ComponentNameProps {
  // Required data
  data: DataType;
  
  // Optional handlers
  onClick?: (item: DataType) => void;
  onSubmit?: (formData: FormData) => void;
  
  // Optional UI customization
  className?: string;
  disabled?: boolean;
}
```

### State-Aware Component Props
```typescript
// Components handling async data:
interface ComponentProps {
  dataState: UiState<DataType>;
  onRetry?: () => void;
}
```

---

## 🔌 Data Flow

### Parent → Child (Props)
```
App.tsx
  ↓ dashboardState
DashboardScreen
  ↓ dashboardState
DashboardSummarySection
  ↓ individual props
Molecules & Atoms
```

### Child → Parent (Callbacks)
```
Button (onClick)
  ↑ event
AddExpenseForm (onSubmit)
  ↑ formData
App.tsx (handleAddExpense)
```

---

## 📱 Screen Navigation

```typescript
type NavigationTab = 'home' | 'statistics' | 'transactions' | 'profile';

// Navigation handled in App.tsx
const [activeTab, setActiveTab] = useState<NavigationTab>('home');
```

| Tab | Screen | Primary Organism |
|-----|--------|------------------|
| `home` | DashboardScreen | DashboardSummarySection |
| `statistics` | StatisticsScreen | CategoryCard + Charts |
| `transactions` | TransactionsScreen | TransactionList |
| `profile` | ProfileScreen | Menu sections |

---

## 🧪 Mock Data Reference

### Available Mock Data
```typescript
MOCK_USER_ACCOUNT          // User info
MOCK_TRANSACTIONS          // 20 transaction entries
MOCK_CATEGORY_SPENDING     // 5 category summaries
MOCK_WEEKLY_SPENDING       // 7 days of data
MOCK_DASHBOARD_SUMMARY     // Complete dashboard data
MOCK_MONTHLY_COMPARISON    // Month comparison
CATEGORY_METADATA          // 9 category configs
```

### Helper Functions
```typescript
getCategoryMetadata(id)              // Get category info
filterTransactionsByType(txns, type) // Filter by expense/income
calculateTotalAmount(txns)           // Sum transaction amounts
```

---

## 🎨 Component Styling Patterns

### Container Components
```typescript
className="
  bg-[var(--color-surface)]
  rounded-2xl
  p-[var(--spacing-lg)]
  shadow-[var(--elevation-1)]
  border border-[var(--color-outline-variant)]
"
```

### Interactive Elements
```typescript
className="
  transition-all duration-200
  hover:shadow-[var(--elevation-2)]
  active:scale-95
"
```

### Flexbox Layouts
```typescript
className="
  flex items-center gap-[var(--spacing-md)]
  justify-between
"
```

---

## ✅ Component Checklist

When creating new components:

- [ ] Define TypeScript interface with JSDoc
- [ ] Handle all UiState statuses (if data-driven)
- [ ] Use Material 3 design tokens (CSS variables)
- [ ] Follow 8dp spacing grid
- [ ] Add transition animations
- [ ] Include accessibility labels
- [ ] Export as named export
- [ ] Place in correct atomic level directory

---

## 🚀 Quick Start Commands

```bash
# View all components
ls src/app/components/**/*.tsx

# View all screens
ls src/app/screens/*.tsx

# View domain models
cat src/types/domain.ts

# View mock data
cat src/mocks/data.ts

# View theme tokens
cat src/styles/theme.css
```

---

## 📖 Usage Examples

### Using a Button
```typescript
import { Button } from '../atoms/Button';

<Button 
  variant="filled" 
  size="large" 
  onClick={handleClick}
>
  Add Expense
</Button>
```

### Using TransactionList
```typescript
import { TransactionList } from '../organisms/TransactionList';

<TransactionList
  transactionsState={{
    status: 'success',
    data: transactions
  }}
  onTransactionClick={handleClick}
/>
```

### Using UiState
```typescript
const [state, setState] = useState<UiState<Transaction[]>>({
  status: 'loading'
});

// Success
setState({ status: 'success', data: transactions });

// Error
setState({ status: 'error', error: 'Failed to load' });
```

---

**Total Components**: 22 (7 atoms + 7 molecules + 4 organisms + 4 screens)

**Lines of Code**: ~2,500 (excluding comments)

**Type Coverage**: 100%

**Design System**: Material 3

**Architecture**: MVI (Model-View-Intent)
