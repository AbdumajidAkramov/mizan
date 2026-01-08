# Material Design 3 (M3) Implementation Guide
## Complete Reference for Android Translation

This Expense Manager app **strictly follows Material Design 3 specifications** as defined by Google's official M3 documentation. Every component, color, shape, and typography scale maps 1:1 to Android Jetpack Compose.

---

## 🎨 M3 Color System

### Official M3 Color Roles

```css
/* Primary - Brand color */
--md-sys-color-primary: #6750A4
--md-sys-color-on-primary: #FFFFFF
--md-sys-color-primary-container: #EADDFF
--md-sys-color-on-primary-container: #21005D

/* Secondary - Complementary */
--md-sys-color-secondary: #625B71
--md-sys-color-on-secondary: #FFFFFF
--md-sys-color-secondary-container: #E8DEF8
--md-sys-color-on-secondary-container: #1D192B

/* Tertiary - Accent */
--md-sys-color-tertiary: #7D5260
--md-sys-color-on-tertiary: #FFFFFF
--md-sys-color-tertiary-container: #FFD8E4
--md-sys-color-on-tertiary-container: #31111D

/* Error - Destructive actions */
--md-sys-color-error: #B3261E
--md-sys-color-on-error: #FFFFFF
--md-sys-color-error-container: #F9DEDC
--md-sys-color-on-error-container: #410E0B

/* Surface - Backgrounds */
--md-sys-color-surface: #FFFBFE
--md-sys-color-on-surface: #1C1B1F
--md-sys-color-surface-variant: #E7E0EC
--md-sys-color-on-surface-variant: #49454F
```

### Kotlin Translation

```kotlin
// Theme.kt
val md_theme_light_primary = Color(0xFF6750A4)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFEADDFF)
val md_theme_light_onPrimaryContainer = Color(0xFF21005D)
// ... continue for all color roles

val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    // ...
)
```

---

## 🏗️ M3 Elevation System (Surface Tints)

### Material 3 uses surface tints, NOT box shadows

```css
/* Elevation Levels → Surface Container Colors */
--md-sys-elevation-level0: var(--md-sys-color-surface)
--md-sys-elevation-level1: var(--md-sys-color-surface-container-low)
--md-sys-elevation-level2: var(--md-sys-color-surface-container)
--md-sys-elevation-level3: var(--md-sys-color-surface-container-high)
--md-sys-elevation-level4: var(--md-sys-color-surface-container-high)
--md-sys-elevation-level5: var(--md-sys-color-surface-container-highest)
```

### React Example
```tsx
<M3Surface elevation={1} shape="medium">
  {/* Content uses surface-container-low color */}
</M3Surface>
```

### Kotlin Example
```kotlin
Card(
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
) {
    // Content
}
```

**Key Point**: M3 elevation is achieved through color tints, not shadows. Level 1 = slightly tinted surface.

---

## 📐 M3 Shape System

### Official M3 Shape Scale

```css
--md-sys-shape-corner-none: 0px
--md-sys-shape-corner-extra-small: 4px
--md-sys-shape-corner-small: 8px
--md-sys-shape-corner-medium: 12px
--md-sys-shape-corner-large: 16px
--md-sys-shape-corner-extra-large: 28px  /* Hero cards */
--md-sys-shape-corner-full: 9999px       /* Pills/Chips */
```

### Component → Shape Mapping

| Component | Shape | Corner Radius |
|-----------|-------|---------------|
| BalanceCard (Hero) | Extra Large | 28dp |
| CategoryCard | Medium | 12dp |
| TransactionListItem | Medium | 12dp |
| BudgetCard | Medium | 12dp |
| Button | Full | 9999dp (pill) |
| IconButton | Full | 9999dp (circle) |
| FAB | Large | 16dp |

### React Example
```tsx
<M3Surface shape="extra-large">
  {/* 28dp rounded corners */}
</M3Surface>
```

### Kotlin Example
```kotlin
Card(
    shape = MaterialTheme.shapes.extraLarge // 28dp
) {
    // Content
}
```

---

## 📝 M3 Typography Scale

### Complete M3 Type System

| Role | Size | Weight | Line Height | Usage |
|------|------|--------|-------------|-------|
| **Display Large** | 57px | 400 | 64px | Hero text |
| **Display Medium** | 45px | 400 | 52px | Large emphasis |
| **Display Small** | 36px | 400 | 44px | Balance amount |
| **Headline Large** | 32px | 400 | 40px | Section headers |
| **Headline Medium** | 28px | 400 | 36px | Screen titles |
| **Headline Small** | 24px | 400 | 32px | Card titles |
| **Title Large** | 22px | 400 | 28px | List headers |
| **Title Medium** | 16px | 500 | 24px | Card titles |
| **Title Small** | 14px | 500 | 20px | Dense titles |
| **Body Large** | 16px | 400 | 24px | Body text |
| **Body Medium** | 14px | 400 | 20px | Default body |
| **Body Small** | 12px | 400 | 16px | Captions |
| **Label Large** | 14px | 500 | 20px | Buttons |
| **Label Medium** | 12px | 500 | 16px | Chips |
| **Label Small** | 11px | 500 | 16px | Small labels |

### Usage in React

```tsx
<h1 className="display-small">$8,450.50</h1>
<h2 className="headline-medium">Dashboard</h2>
<p className="body-medium">Transaction description</p>
<button className="label-large">Add Expense</button>
```

### Usage in Compose

```kotlin
Text(
    text = "$8,450.50",
    style = MaterialTheme.typography.displaySmall
)
Text(
    text = "Dashboard",
    style = MaterialTheme.typography.headlineMedium
)
Text(
    text = "Transaction description",
    style = MaterialTheme.typography.bodyMedium
)
```

---

## 🧩 M3 Component Specifications

### M3 Button

**React Implementation:**
```tsx
<M3Button variant="filled" size="default">
  Add Expense
</M3Button>
```

**Variants:**
- `filled` - Primary action (bg: primary)
- `filled-tonal` - Secondary action (bg: secondary-container)
- `elevated` - Elevated surface
- `outlined` - Border only
- `text` - No background

**Kotlin Equivalent:**
```kotlin
Button(
    onClick = { },
    colors = ButtonDefaults.buttonColors()
) {
    Text("Add Expense")
}

FilledTonalButton(onClick = { }) {
    Text("Secondary Action")
}

OutlinedButton(onClick = { }) {
    Text("Outlined")
}

TextButton(onClick = { }) {
    Text("Text")
}
```

---

### M3 Card

**React Implementation:**
```tsx
<M3Card variant="elevated" onClick={handleClick}>
  <h3>Card Title</h3>
  <p>Card content</p>
</M3Card>
```

**Variants:**
- `elevated` - Level 1 surface tint
- `filled` - Surface variant color
- `outlined` - Border with outline color

**Kotlin Equivalent:**
```kotlin
Card(
    onClick = { },
    colors = CardDefaults.elevatedCardColors()
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Card Title", style = MaterialTheme.typography.titleLarge)
        Text("Card content", style = MaterialTheme.typography.bodyMedium)
    }
}
```

---

### M3 Surface

**React Implementation:**
```tsx
<M3Surface elevation={1} shape="medium">
  {children}
</M3Surface>
```

**Kotlin Equivalent:**
```kotlin
Surface(
    tonalElevation = 1.dp,
    shape = MaterialTheme.shapes.medium
) {
    // Content
}
```

---

### M3 List Item (Three-Line)

**React Implementation:**
```tsx
<M3TransactionListItem
  transaction={transaction}
  onClick={handleClick}
/>
```

**Structure:**
- Leading: 48×48dp icon container
- Center: Three-line text layout
- Trailing: Amount + metadata

**Kotlin Equivalent:**
```kotlin
ListItem(
    headlineContent = { Text("Transaction Title") },
    supportingContent = { Text("Date and time") },
    leadingContent = {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color, CircleShape)
        ) {
            Icon(...)
        }
    },
    trailingContent = {
        Column(horizontalAlignment = Alignment.End) {
            Text("$127.45")
            Text("Category")
        }
    }
)
```

---

### M3 Navigation Bar with FAB

**React Implementation:**
```tsx
<M3BottomNavigationBar
  activeTab="home"
  onTabChange={setActiveTab}
  onAddExpense={handleAdd}
/>
```

**Key Features:**
- NavigationBarItem for each tab
- FloatingActionButton in center
- State layer on interaction

**Kotlin Equivalent:**
```kotlin
Scaffold(
    bottomBar = {
        NavigationBar {
            items.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label) },
                    selected = currentRoute == item.route,
                    onClick = { navController.navigate(item.route) }
                )
            }
        }
    },
    floatingActionButton = {
        FloatingActionButton(
            onClick = { },
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
) { padding ->
    // Screen content
}
```

---

## 📏 8dp Grid System

### Spacing Scale

```css
--md-sys-spacing-xs: 4px   (0.5×)
--md-sys-spacing-sm: 8px   (1×)
--md-sys-spacing-md: 16px  (2×)
--md-sys-spacing-lg: 24px  (3×)
--md-sys-spacing-xl: 32px  (4×)
--md-sys-spacing-2xl: 40px (5×)
--md-sys-spacing-3xl: 48px (6×)
--md-sys-spacing-4xl: 56px (7×)
```

### Component Padding Standards

| Component | Padding |
|-----------|---------|
| Card | 16dp (md) |
| Large Card | 24dp (lg) |
| List Item | 16dp (md) |
| Button (horizontal) | 24dp (lg) |
| Screen margins | 16dp (md) |
| Component gaps | 8dp (sm) or 16dp (md) |

---

## 🎭 M3 State Layers

### Interaction States

```css
--md-sys-state-hover-opacity: 0.08
--md-sys-state-focus-opacity: 0.12
--md-sys-state-pressed-opacity: 0.12
--md-sys-state-dragged-opacity: 0.16
```

### React Example
```tsx
<button className="
  hover:bg-[var(--md-sys-color-on-surface)]/[0.08]
  active:bg-[var(--md-sys-color-on-surface)]/[0.12]
">
```

### Kotlin Example
```kotlin
Button(
    modifier = Modifier.clickable {
        // Ripple effect handled automatically by M3
    }
)
```

---

## 🔄 Component Mapping Table

### Complete React → Compose Translation

| React Component | Compose Equivalent |
|-----------------|-------------------|
| `<M3Button variant="filled">` | `Button()` |
| `<M3Button variant="filled-tonal">` | `FilledTonalButton()` |
| `<M3Button variant="outlined">` | `OutlinedButton()` |
| `<M3Button variant="text">` | `TextButton()` |
| `<M3IconButton variant="filled">` | `IconButton() + FilledIconButton` |
| `<M3Card variant="elevated">` | `Card(colors = elevatedCardColors())` |
| `<M3Card variant="outlined">` | `OutlinedCard()` |
| `<M3Surface elevation={1}>` | `Surface(tonalElevation = 1.dp)` |
| `<M3TransactionListItem>` | `ListItem(threeLine = true)` |
| `<M3BottomNavigationBar>` | `NavigationBar + NavigationBarItem` |
| `<M3CategoryCard>` | `Card + LinearProgressIndicator` |
| `<M3BalanceCard>` | `Card(shape = extraLarge)` |

---

## 📱 Screen Structure

### M3 Screen Anatomy

```
┌────────────────────────��────┐
│  Top App Bar (optional)     │
├─────────────────────────────┤
│                             │
│  Screen Content             │
│  - 16dp margin              │
│  - ScrollableColumn         │
│  - Spacing: 8dp/16dp        │
│                             │
│                             │
├─────────────────────────────┤
│  Bottom Navigation Bar      │
│  + FAB (floating)           │
└─────────────────────────────┘
```

### React Implementation
```tsx
<div className="max-w-lg mx-auto px-[var(--md-sys-spacing-md)]">
  <h1 className="headline-medium">Screen Title</h1>
  <div className="flex flex-col gap-[var(--md-sys-spacing-md)]">
    {/* Content */}
  </div>
</div>
```

### Kotlin Implementation
```kotlin
Scaffold(
    topBar = { TopAppBar(title = { Text("Screen Title") }) },
    bottomBar = { BottomNavigationBar() },
    floatingActionButton = { FAB() }
) { padding ->
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Content
    }
}
```

---

## ✅ M3 Compliance Checklist

- [x] Using M3 color system (primary, on-primary, containers)
- [x] Using surface tints for elevation (NO box shadows)
- [x] Using M3 shape scale (28dp for hero, 12dp for cards)
- [x] Using M3 typography scale (Display, Headline, Title, Body, Label)
- [x] Following 8dp grid system strictly
- [x] Using M3 state layer opacities
- [x] Implementing M3 components (Button, Card, Surface, ListItem)
- [x] Using M3 NavigationBar with FAB
- [x] Applying M3 interaction patterns
- [x] Using proper M3 color roles (on-surface, surface-variant, etc.)

---

## 🎯 Key M3 Principles

### 1. **Adaptive Color**
M3 uses dynamic color generation. In production, colors adapt to system theme and wallpaper (Android 12+).

### 2. **Surface Tinting** 
Elevation is shown through subtle color tints, not drop shadows.

### 3. **Expressive Typography**
Large, bold typography for emphasis (Display scale for hero elements).

### 4. **Rounded Shapes**
Generous corner radii (28dp for hero cards, 12dp for standard cards).

### 5. **Container Colors**
Use `-container` colors for filled buttons and chips (e.g., `primary-container`).

---

## 📚 Official M3 Resources

- **Design**: https://m3.material.io/
- **Components**: https://m3.material.io/components
- **Color System**: https://m3.material.io/styles/color/overview
- **Typography**: https://m3.material.io/styles/typography/overview
- **Elevation**: https://m3.material.io/styles/elevation/overview

---

## 🚀 Quick Start

### React (Current Implementation)
```bash
# All M3 tokens are in:
/src/styles/material3-theme.css

# M3 Components:
/src/app/components/atoms/M3*.tsx
/src/app/components/molecules/M3*.tsx
/src/app/components/organisms/M3*.tsx

# M3 Screens:
/src/app/screens/M3*Screen.tsx

# Main App:
/src/app/M3App.tsx
```

### Android (For Translation)
```kotlin
// Add M3 dependency
implementation("androidx.compose.material3:material3")

// Use M3 theme
MaterialTheme {
    // Your app
}
```

---

**This implementation is 100% Material Design 3 compliant and ready for 1:1 Android translation.**
