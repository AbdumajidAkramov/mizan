# UI Kit Structure - dev.esbi.mizan.ui.kit

Complete design system ported from React/Tailwind to Jetpack Compose.

## Package Structure

```
ui.kit/
├── atoms/                  # Basic building blocks
│   ├── AmountText.kt      ✅
│   ├── Button.kt          ✅
│   ├── CategoryIcon.kt    ✅
│   ├── IconButton.kt      ✅
│   ├── LoadingSkeleton.kt ✅
│   ├── M3Button.kt        ✅
│   ├── M3Card.kt          ✅
│   ├── M3IconButton.kt    ✅
│   └── M3Surface.kt       ✅
│
├── premium/               # Premium design components
│   ├── PremiumButton.kt   ✅
│   ├── PremiumCard.kt     ✅
│   ├── PremiumAIInsights.kt      (in /ui/components)
│   ├── PremiumBalanceCard.kt     (in /ui/components)
│   ├── PremiumBottomNav.kt       ⏳
│   ├── PremiumCalendar.kt        (in /ui/components)
│   ├── PremiumCashFlowCard.kt    (in /ui/components)
│   ├── PremiumCategoryPicker.kt  ⏳
│   ├── PremiumEmergencyFund.kt   (in /ui/components)
│   ├── PremiumHealthScore.kt     (in /ui/components)
│   ├── PremiumNetWorthCard.kt    (in /ui/components)
│   ├── PremiumThemeToggle.kt     (in /ui/components)
│   └── PremiumTransactionItem.kt ⏳
│
├── molecules/             # Composite components
│   ├── BalanceCard.kt     ⏳
│   ├── BudgetProgressCard.kt ⏳
│   ├── CategoryCard.kt    ⏳
│   ├── EmptyState.kt      (in /ui/components)
│   ├── ErrorState.kt      (in /ui/components)
│   ├── M3BalanceCard.kt   ⏳
│   ├── M3BudgetProgressCard.kt ⏳
│   ├── M3CategoryCard.kt  ⏳
│   ├── M3TransactionListItem.kt ⏳
│   └── TransactionListItem.kt ⏳
│
├── organisms/             # Complex feature components
│   ├── AddExpenseForm.kt  ⏳
│   ├── BottomNavigationBar.kt ⏳
│   ├── DashboardSummarySection.kt ⏳
│   ├── M3BottomNavigationBar.kt ⏳
│   └── TransactionList.kt ⏳
│
└── ui/                    # Base UI primitives (Shadcn-style)
    ├── Badge.kt           ⏳
    ├── Card.kt            ⏳
    ├── Checkbox.kt        ⏳
    ├── Dialog.kt          ⏳
    ├── Input.kt           ⏳
    ├── Label.kt           ⏳
    ├── Progress.kt        ⏳
    ├── Select.kt          ⏳
    ├── Separator.kt       ⏳
    ├── Slider.kt          ⏳
    ├── Switch.kt          ⏳
    ├── Textarea.kt        ⏳
    └── ... (30+ components)
```

## Completed Components (11/67)

### ✅ Atoms (9/9) - 100%
All basic building blocks complete with Material3 variants.

### ✅ Premium (2/13) - 15%
Core premium components (Button, Card) in new structure.
Remaining components exist in `/ui/components` and need migration.

### ⏳ Molecules (0/10) - 0%
Awaiting implementation.

### ⏳ Organisms (0/5) - 0%
Awaiting implementation.

### ⏳ Base UI (0/30+) - 0%
Shadcn-style primitives awaiting implementation.

## Design System Mappings

### Tailwind → Compose
- `rounded-2xl` → `RoundedCornerShape(24.dp)`
- `bg-white/10` → `Color.White.copy(alpha = 0.1f)`
- `backdrop-blur-[20px]` → `blur(20.dp)` modifier
- `shadow-xl` → `elevation = 8.dp`
- `hover:scale-95` → `pressScale()` animation

### Color System
- Using `PremiumColors` object
- Material3 `MaterialTheme.colorScheme`
- Gradient brushes for premium effects

### Typography
- Material3 typography system
- Custom font sizes with `.sp` units
- FontWeight for emphasis

## Next Steps
1. Migrate existing Premium components from `/ui/components` to `/ui/kit/premium`
2. Implement Molecule components on demand
3. Implement Organism components as features require
4. Implement Base UI primitives for form/dialog needs

## Usage Example
```kotlin
import dev.esbi.mizan.ui.kit.atoms.*
import dev.esbi.mizan.ui.kit.premium.*

// Atoms
AmountText(amount = 1234.56, type = TransactionType.INCOME, showSign = true)
KitButton(text = "Submit", onClick = {}, variant = ButtonVariant.FILLED)
CategoryIcon(category = CategoryType.FOOD, size = IconSize.LARGE)

// Premium
PremiumButton(onClick = {}, variant = PremiumButtonVariant.GRADIENT_PRIMARY) {
    Text("Premium Action")
}
PremiumCard(variant = PremiumCardVariant.Glass) {
    // Card content
}
```
