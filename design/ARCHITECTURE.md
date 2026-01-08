# Expense Manager - Architecture Documentation

## Overview
This is a production-ready Expense Manager application built with **React**, **TypeScript**, and **Tailwind CSS**, following **Material 3 Design** principles and **MVI (Model-View-Intent)** architecture patterns optimized for Android translation.

---

## 🏗️ Architecture Pattern: MVI (Model-View-Intent)

### Key Principles
1. **Unidirectional Data Flow**: Data flows in one direction through the app
2. **Explicit State Management**: All UI states are explicitly defined
3. **Immutability**: State objects are immutable and replaced, not mutated
4. **Separation of Concerns**: Business logic separated from UI rendering

### State Structure
```typescript
interface UiState<T> {
  status: 'idle' | 'loading' | 'success' | 'error' | 'empty';
  data?: T;
  error?: string;
}
```

Every data-driven component receives a `UiState<T>` wrapper that explicitly handles:
- **Loading**: Skeleton placeholders (shimmer effect)
- **Success**: Actual data rendering
- **Error**: Error message with retry option
- **Empty**: Empty state with helpful message

---

## 📁 File Structure

```
src/
├── types/
│   └── domain.ts                    # Domain models & TypeScript interfaces
├── mocks/
│   └── data.ts                      # Mock data (20 realistic entries)
├── app/
│   ├── components/
│   │   ├── atoms/                   # Smallest UI elements
│   │   │   ├── Button.tsx
│   │   │   ├── IconButton.tsx
│   │   │   ├── CategoryIcon.tsx
│   │   │   ├── LoadingSkeleton.tsx
│   │   │   └── AmountText.tsx
│   │   ├── molecules/               # Combinations of atoms
│   │   │   ├── TransactionListItem.tsx
│   │   │   ├── CategoryCard.tsx
│   │   │   ├── BalanceCard.tsx
│   │   │   ├── BudgetProgressCard.tsx
│   │   │   ├── EmptyState.tsx
│   │   │   └── ErrorState.tsx
│   │   └── organisms/               # Complex component compositions
│   │       ├── TransactionList.tsx
│   │       ├── DashboardSummarySection.tsx
│   │       ├── AddExpenseForm.tsx
│   │       └── BottomNavigationBar.tsx
│   ├── screens/                     # Full screen components
│   │   ├── DashboardScreen.tsx
│   │   ├── TransactionsScreen.tsx
│   │   ├── StatisticsScreen.tsx
│   │   └── ProfileScreen.tsx
│   └── App.tsx                      # Root component
└── styles/
    └── theme.css                    # Material 3 design tokens
```

---

## 🎨 Design System: Material 3

### Color Tokens (1:1 Android Mapping)
```css
/* Primary Colors */
--color-primary: #6750A4
--color-on-primary: #FFFFFF
--color-primary-container: #EADDFF
--color-on-primary-container: #21005D

/* Surface Colors */
--color-surface: #FEFBFF
--color-on-surface: #1C1B1F
--color-surface-variant: #E7E0EC
--color-on-surface-variant: #49454F

/* Category Colors */
--color-category-food: #FF6B6B
--color-category-transport: #4ECDC4
--color-category-shopping: #FFE66D
--color-category-bills: #95E1D3
--color-category-entertainment: #C7CEEA
--color-category-health: #FF8585
--color-category-travel: #7B68EE
--color-category-tech: #4CAF50
--color-category-income: #2E7D32
```

### 8dp Grid System (Android Standard)
```css
--spacing-xs: 4px    /* 0.5 * 8dp */
--spacing-sm: 8px    /* 1 * 8dp */
--spacing-md: 16px   /* 2 * 8dp */
--spacing-lg: 24px   /* 3 * 8dp */
--spacing-xl: 32px   /* 4 * 8dp */
--spacing-2xl: 40px  /* 5 * 8dp */
--spacing-3xl: 48px  /* 6 * 8dp */
```

### Elevation System (Material 3)
```css
--elevation-1: 0px 1px 2px rgba(0, 0, 0, 0.3), 0px 1px 3px 1px rgba(0, 0, 0, 0.15)
--elevation-2: 0px 1px 2px rgba(0, 0, 0, 0.3), 0px 2px 6px 2px rgba(0, 0, 0, 0.15)
--elevation-3: 0px 4px 8px 3px rgba(0, 0, 0, 0.15), 0px 1px 3px rgba(0, 0, 0, 0.3)
```

---

## 🧩 Atomic Design Pattern

### Atoms
**Purpose**: Basic building blocks that cannot be broken down further

Examples:
- `Button.tsx` - Material 3 button with variants (filled, outlined, text, elevated)
- `IconButton.tsx` - 48x48dp touch target icon button
- `CategoryIcon.tsx` - Category-specific icon rendering
- `AmountText.tsx` - Formatted currency display
- `LoadingSkeleton.tsx` - Shimmer loading placeholder

### Molecules
**Purpose**: Simple combinations of atoms that form distinct UI elements

Examples:
- `TransactionListItem.tsx` - Single transaction row (icon + text + amount)
- `BalanceCard.tsx` - Balance display with gradient background
- `CategoryCard.tsx` - Category spending summary with progress bar
- `EmptyState.tsx` - Empty list placeholder
- `ErrorState.tsx` - Error message with retry button

### Organisms
**Purpose**: Complex components that combine molecules and atoms

Examples:
- `TransactionList.tsx` - Full transaction list with filters and state management
- `DashboardSummarySection.tsx` - Complete dashboard with charts and cards
- `AddExpenseForm.tsx` - Multi-step expense creation form
- `BottomNavigationBar.tsx` - Bottom navigation with FAB

### Screens
**Purpose**: Full-page compositions of organisms

Examples:
- `DashboardScreen.tsx` - Main financial overview
- `TransactionsScreen.tsx` - Transaction history
- `StatisticsScreen.tsx` - Analytics and insights
- `ProfileScreen.tsx` - User settings

---

## 📊 Domain Models

### Core Entities

#### Transaction
```typescript
interface Transaction {
  id: string;
  title: string;
  amount: number;
  category: TransactionCategory;
  timestamp: string;      // ISO 8601
  type: 'expense' | 'income';
  notes?: string;
}
```

#### CategorySpending
```typescript
interface CategorySpending {
  category: TransactionCategory;
  categoryLabel: string;
  totalAmount: number;
  percentage: number;
  transactionCount: number;
  colorToken: string;
}
```

#### DashboardSummary
```typescript
interface DashboardSummary {
  totalBalance: number;
  monthlyExpenses: number;
  monthlySavings: number;
  budgetLimit: number;
  budgetPercentageUsed: number;
  topCategories: CategorySpending[];
  weeklySpending: DailySpending[];
}
```

---

## 🔄 State Management Flow

### Example: Loading Dashboard Data

```typescript
// 1. Initial State (Idle)
const [dashboardState, setDashboardState] = useState<UiState<DashboardSummary>>({
  status: 'idle'
});

// 2. Loading State
setDashboardState({ status: 'loading' });
// UI renders: <LoadingSkeleton />

// 3. Success State
setDashboardState({
  status: 'success',
  data: dashboardData
});
// UI renders: Actual dashboard content

// 4. Error State (if fetch fails)
setDashboardState({
  status: 'error',
  error: 'Failed to load dashboard'
});
// UI renders: <ErrorState /> with retry button
```

---

## 🎯 Android Translation Guide

### Component Mapping: React → Jetpack Compose

| React Component | Jetpack Compose Equivalent |
|----------------|---------------------------|
| `Button` | `Button()` / `FilledTonalButton()` |
| `IconButton` | `IconButton()` |
| `TransactionListItem` | `ListItem()` with `threeLine` |
| `BalanceCard` | `Card()` with gradient modifier |
| `BottomNavigationBar` | `NavigationBar()` with `FloatingActionButton()` |
| `LoadingSkeleton` | `Box()` with shimmer modifier |

### CSS Variable → Compose Theme

```kotlin
// theme.css
--color-primary: #6750A4

// MaterialTheme.kt
val md_theme_light_primary = Color(0xFF6750A4)
```

### Spacing System

```kotlin
// theme.css
--spacing-md: 16px

// Dimens.kt
val spacing_md = 16.dp
```

---

## 📦 Mock Data

Location: `/src/mocks/data.ts`

**Contains**:
- 20 realistic transaction entries
- 9 category metadata definitions
- Dashboard summary with calculated metrics
- User account information
- Helper functions for data manipulation

**Categories Available**:
1. Food & Dining
2. Transport
3. Shopping
4. Bills & Utilities
5. Entertainment
6. Health & Fitness
7. Travel
8. Technology
9. Income

---

## 🧪 Testing Strategy (Recommended)

### Unit Tests
- Test individual atoms (Button, AmountText)
- Test state transformations
- Test utility functions in `mocks/data.ts`

### Integration Tests
- Test molecules with mock props
- Test organisms with UiState variations
- Test form validation in AddExpenseForm

### E2E Tests
- Test navigation flow
- Test expense creation flow
- Test filtering and sorting

---

## 🚀 Performance Optimizations

1. **Lazy Loading**: Screens can be code-split
2. **Memoization**: Use `React.memo()` for expensive organisms
3. **Virtual Scrolling**: For large transaction lists (use `react-window`)
4. **Debouncing**: For search/filter inputs
5. **Image Optimization**: Icons are SVG-based (lucide-react)

---

## 📝 Naming Conventions

### Files
- **PascalCase** for components: `TransactionListItem.tsx`
- **camelCase** for utilities: `data.ts`
- **kebab-case** for CSS: `theme.css`

### Variables
- **camelCase** for variables: `dashboardState`
- **PascalCase** for types: `Transaction`
- **SCREAMING_SNAKE_CASE** for constants: `MOCK_TRANSACTIONS`

### CSS Classes
- **Tailwind utilities**: `bg-[var(--color-primary)]`
- **Semantic spacing**: `p-[var(--spacing-md)]`

---

## 🔐 Type Safety

All components are fully typed with:
- **Props interfaces**: Explicit prop types with JSDoc
- **Domain models**: Strongly typed business entities
- **State types**: Generic `UiState<T>` wrapper
- **Event handlers**: Typed callbacks

Example:
```typescript
/**
 * TransactionListItem Molecule Component
 * Single transaction row in list view
 */
export interface TransactionListItemProps {
  /** Transaction data */
  transaction: Transaction;
  /** Click handler for item */
  onClick?: (transaction: Transaction) => void;
}
```

---

## 📚 Dependencies

### Core
- `react` 18.3.1
- `typescript` (via Vite)
- `tailwindcss` 4.1.12

### UI Libraries
- `lucide-react` - Icon system
- `recharts` - Charts and graphs

### Already Installed
All required dependencies are pre-installed in `package.json`

---

## 🎓 Best Practices Implemented

1. ✅ **Separation of Concerns**: Logic separated from UI
2. ✅ **Single Responsibility**: Each component has one job
3. ✅ **DRY Principle**: Reusable atoms and molecules
4. ✅ **Explicit State**: No implicit state transitions
5. ✅ **Type Safety**: Full TypeScript coverage
6. ✅ **Accessibility**: ARIA labels on interactive elements
7. ✅ **Responsive Design**: Mobile-first approach
8. ✅ **Design Consistency**: Material 3 design tokens
9. ✅ **Performance**: Optimized re-renders
10. ✅ **Documentation**: JSDoc on all public interfaces

---

## 🔄 Future Enhancements

1. **State Management**: Add Redux or Zustand for global state
2. **API Integration**: Connect to real backend
3. **Offline Support**: Add service workers
4. **Data Persistence**: LocalStorage or IndexedDB
5. **Authentication**: User login/signup
6. **Budget Alerts**: Push notifications
7. **Export**: CSV/PDF export functionality
8. **Dark Mode**: Theme switching
9. **Multi-currency**: Support multiple currencies
10. **Analytics**: Advanced spending insights

---

## 📞 Component Communication

```
App.tsx (Root)
    ↓
Screens (Dashboard, Transactions, etc.)
    ↓
Organisms (TransactionList, DashboardSummarySection)
    ↓
Molecules (TransactionListItem, CategoryCard)
    ↓
Atoms (Button, CategoryIcon, AmountText)
```

**Data flows down** via props
**Events flow up** via callbacks

---

## ✅ Android Architect Checklist

- [x] MVI architecture pattern
- [x] Explicit UI states (loading/error/empty/success)
- [x] Material 3 design system
- [x] 8dp grid spacing system
- [x] Domain-driven naming
- [x] TypeScript interfaces with JSDoc
- [x] Atomic design pattern
- [x] Separation of concerns
- [x] 20+ mock data entries
- [x] Organized file structure
- [x] 1:1 Android component mapping
- [x] CSS variables for theming
- [x] Production-ready code quality

---

**Built for seamless Android translation with Jetpack Compose.**
