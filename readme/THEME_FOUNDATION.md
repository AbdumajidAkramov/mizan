# Theme Foundation - Mizan Design System

Complete theme implementation for Android app based on web design system CSS analysis.

## 📁 Files Created/Updated

### 1. **Color.kt** (277 lines)
Complete color palette with Premium and Material3 colors for light/dark themes.

### 2. **Theme.kt** (312 lines)
Material3 ColorSchemes with Premium and Material3 variants, plus MizanTheme composable.

### 3. **Type.kt** (157 lines)
Complete Material3 typography system with all display, headline, title, body, and label variants.

---

## 🎨 Color System

### **Premium Brand Colors** (Theme-Independent)
```kotlin
PremiumPrimary = Color(0xFF667EEA)          // Purple-Blue
PremiumSecondary = Color(0xFFF5576C)        // Pink-Red
PremiumSuccess = Color(0xFF00F2FE)          // Cyan
PremiumWarning = Color(0xFFFEE140)          // Yellow
PremiumError = Color(0xFFFF6B6B)            // Red
```

### **Light Theme Colors**
```kotlin
Background: #F8F9FA (Light Gray)
Surface: #FFFFFF (White)
Text Primary: #1A1A2E (Dark Blue-Gray)
Text Secondary: #4A5568 (Medium Gray)
```

### **Dark Theme Colors**
```kotlin
Background: #0F0F23 (Deep Dark Blue)
Surface: #1A1A2E (Dark Blue)
Text Primary: #FFFFFF (White)
Text Secondary: #A8B2D1 (Light Blue-Gray)
```

### **Category Colors** (9 categories)
```kotlin
CategoryFood = Color(0xFFFF6B9D)            // Pink
CategoryTransport = Color(0xFF4FACFE)       // Blue
CategoryShopping = Color(0xFFFFA34D)        // Orange
CategoryBills = Color(0xFF00D2FF)           // Cyan
CategoryEntertainment = Color(0xFFC471F5)   // Purple
CategoryHealth = Color(0xFFFF6B6B)          // Red
CategoryTravel = Color(0xFF667EEA)          // Indigo
CategoryTech = Color(0xFF00F2A0)            // Green
CategoryIncome = Color(0xFF00F2FE)          // Cyan-Blue
```

### **Premium Gradients**
```kotlin
GradientPrimary: #667EEA → #764BA2 (Purple-Blue)
GradientSecondary: #F093FB → #F5576C (Pink)
GradientSuccess: #4FACFE → #00F2FE (Blue-Cyan)
GradientWarm: #FA709A → #FEE140 (Pink-Yellow)
GradientCool: #30CFD0 → #330867 (Cyan-Purple)
```

### **PremiumColors Object**
Convenient access to all premium design tokens:
```kotlin
PremiumColors.Primary
PremiumColors.BgPrimary
PremiumColors.TextPrimary
PremiumColors.Surface2
PremiumColors.CategoryFood
PremiumColors.GradientPrimary
```

---

## 🎭 Theme Schemes

### **Premium Dark Color Scheme**
- Primary: Purple-Blue (#667EEA)
- Background: Deep Dark (#0F0F23)
- Surface: Dark Blue (#1A1A2E)
- Text: White with gray variants
- Complete Material3 mapping with surface containers

### **Premium Light Color Scheme**
- Primary: Purple-Blue (#667EEA)
- Background: Light Gray (#F8F9FA)
- Surface: White (#FFFFFF)
- Text: Dark with gray variants
- Complete Material3 mapping with surface containers

### **Material3 Dark/Light Schemes**
Official Material3 color system with proper M3 tokens:
- Primary: #D0BCFF (Dark) / #6750A4 (Light)
- Full M3 color roles (primary, secondary, tertiary, error)
- Surface containers (lowest, low, default, high, highest)
- Outline variants

---

## 📝 Typography System

### **Display Styles** (Large Headlines)
```kotlin
displayLarge:  57sp / 64sp line / -0.25sp letter / Normal
displayMedium: 45sp / 52sp line / 0sp letter / Normal
displaySmall:  36sp / 44sp line / 0sp letter / Normal
```

### **Headline Styles** (Section Headers)
```kotlin
headlineLarge:  32sp / 40sp line / 0sp letter / Normal
headlineMedium: 28sp / 36sp line / 0sp letter / Normal
headlineSmall:  24sp / 32sp line / 0sp letter / Normal
```

### **Title Styles** (Card Titles)
```kotlin
titleLarge:  22sp / 28sp line / 0sp letter / Normal
titleMedium: 16sp / 24sp line / 0.15sp letter / Medium
titleSmall:  14sp / 20sp line / 0.1sp letter / Medium
```

### **Body Styles** (Content Text)
```kotlin
bodyLarge:  16sp / 24sp line / 0.5sp letter / Normal
bodyMedium: 14sp / 20sp line / 0.25sp letter / Normal
bodySmall:  12sp / 16sp line / 0.4sp letter / Normal
```

### **Label Styles** (Buttons, Tags)
```kotlin
labelLarge:  14sp / 20sp line / 0.1sp letter / Medium
labelMedium: 12sp / 16sp line / 0.5sp letter / Medium
labelSmall:  11sp / 16sp line / 0.5sp letter / Medium
```

---

## 🚀 Usage

### **Basic Theme Usage**
```kotlin
@Composable
fun MyApp() {
    MizanTheme {
        // Your app content
        // Automatically uses Premium Dark theme
    }
}
```

### **Theme Variants**
```kotlin
// Premium Theme (default)
MizanTheme(
    darkTheme = true,
    usePremiumTheme = true
) { /* content */ }

// Material3 Theme
MizanTheme(
    darkTheme = false,
    usePremiumTheme = false
) { /* content */ }

// Dynamic Color (Android 12+)
MizanTheme(
    dynamicColor = true
) { /* content */ }

// Premium-only shorthand
PremiumTheme(darkTheme = true) { /* content */ }
```

### **Accessing Colors**
```kotlin
// Material3 colors (from theme)
MaterialTheme.colorScheme.primary
MaterialTheme.colorScheme.surface
MaterialTheme.colorScheme.onBackground

// Premium colors (direct access)
PremiumColors.Primary
PremiumColors.BgPrimary
PremiumColors.TextSecondary
PremiumColors.CategoryFood

// Gradients
Box(modifier = Modifier.background(PremiumColors.GradientPrimary))
```

### **Typography Usage**
```kotlin
Text(
    text = "Headline",
    style = MaterialTheme.typography.headlineLarge
)

Text(
    text = "Body text",
    style = MaterialTheme.typography.bodyMedium
)

Text(
    text = "Label",
    style = MaterialTheme.typography.labelSmall
)
```

---

## 📊 Design Token Mapping

### **CSS → Compose Color Mapping**

| CSS Variable | Compose Equivalent | Value |
|--------------|-------------------|-------|
| `--premium-primary` | `PremiumPrimary` | #667EEA |
| `--premium-bg-primary` (dark) | `PremiumBgPrimaryDark` | #0F0F23 |
| `--premium-text-primary` (dark) | `PremiumTextPrimaryDark` | #FFFFFF |
| `--premium-surface-2` (dark) | `PremiumSurface2Dark` | #232339 |
| `--premium-gradient-primary` | `GradientPrimary` | Linear gradient |
| `--md-sys-color-primary` (light) | `M3PrimaryLight` | #6750A4 |
| `--md-sys-color-surface` (dark) | `M3SurfaceDark` | #1C1B1F |

### **CSS → Compose Typography Mapping**

| CSS Variable | Compose Equivalent | Size |
|--------------|-------------------|------|
| `--premium-text-display-xl` | `displayLarge` | 57sp |
| `--premium-text-heading-xl` | `headlineLarge` | 32sp |
| `--premium-text-body-lg` | `bodyLarge` | 16sp |
| `--md-sys-typescale-display-large` | `displayLarge` | 57sp |
| `--md-sys-typescale-title-medium` | `titleMedium` | 16sp |

---

## 🎯 Key Features

### ✅ **Complete Color System**
- 70+ color definitions
- Light and dark theme support
- Premium and Material3 variants
- Category colors for transactions
- 5 gradient brushes

### ✅ **Material3 Integration**
- Full Material3 color scheme
- Surface container levels (5 levels)
- Inverse colors for contrast
- Surface tint for elevation
- Proper outline variants

### ✅ **Typography Scale**
- 15 text styles (3 display, 3 headline, 3 title, 3 body, 3 label)
- Proper line heights and letter spacing
- Font weight variations
- Material3 compliant

### ✅ **Theme Switching**
- Automatic dark/light mode detection
- Manual theme override
- Dynamic color support (Android 12+)
- Premium vs Material3 toggle

### ✅ **Status Bar Integration**
- Automatic status bar color matching
- Light/dark status bar icons
- Proper window insets handling

---

## 📈 Statistics

| Metric | Count |
|--------|-------|
| **Total Colors** | 90+ |
| **Premium Colors** | 50+ |
| **Material3 Colors** | 40+ |
| **Category Colors** | 9 |
| **Gradients** | 5 |
| **Typography Styles** | 15 |
| **Color Schemes** | 4 (Premium Light/Dark, M3 Light/Dark) |
| **Total Lines** | 746 (Color: 277, Theme: 312, Type: 157) |

---

## 🔄 Migration Notes

### **From Old Theme**
The existing theme files have been completely replaced with:
1. **Comprehensive color palette** from CSS analysis
2. **Proper Material3 color schemes** with all roles
3. **Complete typography system** with 15 variants
4. **PremiumColors object** for convenient access
5. **Theme switching logic** with Premium/M3 support

### **Breaking Changes**
- Old color names may need updating to new naming convention
- Typography now uses Material3 scale (displayLarge, headlineMedium, etc.)
- Theme composable now has `usePremiumTheme` parameter

### **Benefits**
- ✅ 1:1 mapping with web design system
- ✅ Consistent colors across platforms
- ✅ Proper Material3 compliance
- ✅ Better dark mode support
- ✅ Gradient support built-in
- ✅ Category colors for transactions

---

## 🎨 Design System Alignment

**Web CSS** → **Android Compose**
- `premium-theme.css` → `Color.kt` (Premium colors)
- `material3-theme.css` → `Color.kt` (M3 colors) + `Theme.kt` (ColorSchemes)
- Typography CSS → `Type.kt` (Material3 Typography)

**The Android theme is now a perfect 1:1 translation of the web design system!**
