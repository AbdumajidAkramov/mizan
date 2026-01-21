# Theme Guide - Mizan Design System

A comprehensive developer guide for using the Mizan Theme Foundation in Jetpack Compose.

---

## 📚 Table of Contents

1. [Standard Colors (Material3)](#1-standard-colors-material3)
2. [Premium Colors](#2-premium-colors)
3. [Gradients](#3-gradients)
4. [Category Colors](#4-category-colors)
5. [Typography](#5-typography)
6. [Best Practices](#6-best-practices)
7. [Common Patterns](#7-common-patterns)

---

## 1. Standard Colors (Material3)

Use `MaterialTheme.colorScheme` for all standard Material3 color roles. These colors automatically adapt to light/dark theme.

### **Primary Colors**

```kotlin
@Composable
fun PrimaryButton() {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text("Primary Action")
    }
}
```

### **Background & Surface**

```kotlin
@Composable
fun MyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text(
                text = "Card Content",
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
```

### **Surface Variants & Containers**

```kotlin
@Composable
fun ElevatedCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 2.dp
    ) {
        Text(
            text = "Elevated Surface",
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun HighElevationCard() {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 4.dp
    ) {
        // Content
    }
}
```

### **Secondary & Tertiary**

```kotlin
@Composable
fun SecondaryButton() {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Text("Secondary Action")
    }
}

@Composable
fun TertiaryChip() {
    Surface(
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = "Tertiary",
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
```

### **Error Colors**

```kotlin
@Composable
fun ErrorMessage(message: String) {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}
```

### **Outline & Borders**

```kotlin
@Composable
fun OutlinedCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Text("Outlined Card")
    }
}

@Composable
fun SubtleBorder() {
    Box(
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant)
            .padding(16.dp)
    ) {
        Text("Subtle Border")
    }
}
```

---

## 2. Premium Colors

Use `PremiumColors` object for custom brand colors and design system colors that aren't part of Material3.

### **Brand Colors**

```kotlin
import dev.esbi.mizan.ui.theme.PremiumColors

@Composable
fun PremiumBrandButton() {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(
            containerColor = PremiumColors.Primary,
            contentColor = Color.White
        )
    ) {
        Text("Premium Action")
    }
}

@Composable
fun SecondaryBrandButton() {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(
            containerColor = PremiumColors.Secondary,
            contentColor = Color.White
        )
    ) {
        Text("Secondary Action")
    }
}
```

### **Status Colors**

```kotlin
@Composable
fun SuccessIndicator() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = PremiumColors.Success.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = PremiumColors.Success
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Success",
            color = PremiumColors.Success
        )
    }
}

@Composable
fun WarningBanner() {
    Surface(
        color = PremiumColors.Warning.copy(alpha = 0.15f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = PremiumColors.WarningDark
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Warning message",
                color = PremiumColors.WarningDark
            )
        }
    }
}

@Composable
fun ErrorAlert() {
    Surface(
        color = PremiumColors.Error.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "Error occurred",
            color = PremiumColors.ErrorDark,
            modifier = Modifier.padding(16.dp)
        )
    }
}
```

### **Background & Surface Colors**

```kotlin
@Composable
fun PremiumScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PremiumColors.BgPrimary)
    ) {
        Surface(
            color = PremiumColors.BgSecondary,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Premium Surface",
                color = PremiumColors.TextPrimary,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
```

### **Text Colors**

```kotlin
@Composable
fun TextHierarchy() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Primary Text",
            color = PremiumColors.TextPrimary,
            style = MaterialTheme.typography.titleLarge
        )
        
        Text(
            text = "Secondary Text",
            color = PremiumColors.TextSecondary,
            style = MaterialTheme.typography.bodyMedium
        )
        
        Text(
            text = "Tertiary Text",
            color = PremiumColors.TextTertiary,
            style = MaterialTheme.typography.bodySmall
        )
        
        Text(
            text = "Muted Text",
            color = PremiumColors.TextMuted,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
```

### **Surface Levels**

```kotlin
@Composable
fun SurfaceLevels() {
    Column(modifier = Modifier.padding(16.dp)) {
        // Level 1 - Lowest elevation
        Surface(
            color = PremiumColors.Surface1,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text("Surface Level 1", modifier = Modifier.padding(16.dp))
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Level 2 - Low elevation
        Surface(
            color = PremiumColors.Surface2,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text("Surface Level 2", modifier = Modifier.padding(16.dp))
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Level 3 - Medium elevation
        Surface(
            color = PremiumColors.Surface3,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text("Surface Level 3", modifier = Modifier.padding(16.dp))
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Level 4 - High elevation
        Surface(
            color = PremiumColors.Surface4,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text("Surface Level 4", modifier = Modifier.padding(16.dp))
        }
    }
}
```

### **Glass Effect**

```kotlin
@Composable
fun GlassmorphismCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(PremiumColors.GlassBg)
            .border(1.dp, PremiumColors.GlassBorder, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
    ) {
        Text(
            text = "Glass Effect Card",
            color = PremiumColors.TextPrimary,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp)
        )
    }
}
```

---

## 3. Gradients

Use `PremiumColors.Gradient*` for premium gradient effects. Apply using `Modifier.background(brush)`.

### **Primary Gradient**

```kotlin
@Composable
fun PrimaryGradientButton() {
    Box(
        modifier = Modifier
            .background(
                brush = PremiumColors.GradientPrimary,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { /* action */ }
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Gradient Button",
            color = Color.White,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
```

### **Secondary Gradient**

```kotlin
@Composable
fun SecondaryGradientCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PremiumColors.GradientSecondary)
                .padding(16.dp)
        ) {
            Text(
                text = "Secondary Gradient",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
```

### **Success Gradient**

```kotlin
@Composable
fun SuccessGradientBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(PremiumColors.GradientSuccess)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Transaction Successful",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
```

### **Warm Gradient**

```kotlin
@Composable
fun WarmGradientBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PremiumColors.GradientWarm)
    ) {
        // Content with warm gradient background
        Text(
            text = "Warm Vibes",
            color = Color.White,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
```

### **Cool Gradient**

```kotlin
@Composable
fun CoolGradientHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(PremiumColors.GradientCool)
            .padding(24.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Text(
            text = "Cool Header",
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}
```

### **Gradient with Transparency**

```kotlin
@Composable
fun GradientOverlay() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background image or content
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        
        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            PremiumColors.BgPrimary.copy(alpha = 0.9f)
                        )
                    )
                )
        )
        
        // Content on top
        Text(
            text = "Content",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
        )
    }
}
```

---

## 4. Category Colors

Use `PremiumColors.Category*` for transaction categories. These colors are static and don't change with theme.

### **Category Icons with Colors**

```kotlin
@Composable
fun CategoryChip(category: CategoryType) {
    val categoryColor = when (category) {
        CategoryType.FOOD -> PremiumColors.CategoryFood
        CategoryType.TRANSPORT -> PremiumColors.CategoryTransport
        CategoryType.SHOPPING -> PremiumColors.CategoryShopping
        CategoryType.BILLS -> PremiumColors.CategoryBills
        CategoryType.ENTERTAINMENT -> PremiumColors.CategoryEntertainment
        CategoryType.HEALTH -> PremiumColors.CategoryHealth
        CategoryType.TRAVEL -> PremiumColors.CategoryTravel
        CategoryType.TECH -> PremiumColors.CategoryTech
        CategoryType.INCOME -> PremiumColors.CategoryIncome
    }
    
    Surface(
        color = categoryColor.copy(alpha = 0.15f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = getCategoryIcon(category),
                contentDescription = null,
                tint = categoryColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = category.name,
                color = categoryColor,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
```

### **Category Indicator**

```kotlin
@Composable
fun CategoryIndicator(category: CategoryType) {
    val categoryColor = when (category) {
        CategoryType.FOOD -> PremiumColors.CategoryFood
        CategoryType.TRANSPORT -> PremiumColors.CategoryTransport
        CategoryType.SHOPPING -> PremiumColors.CategoryShopping
        CategoryType.BILLS -> PremiumColors.CategoryBills
        CategoryType.ENTERTAINMENT -> PremiumColors.CategoryEntertainment
        CategoryType.HEALTH -> PremiumColors.CategoryHealth
        CategoryType.TRAVEL -> PremiumColors.CategoryTravel
        CategoryType.TECH -> PremiumColors.CategoryTech
        CategoryType.INCOME -> PremiumColors.CategoryIncome
    }
    
    Box(
        modifier = Modifier
            .size(8.dp)
            .background(categoryColor, CircleShape)
    )
}
```

### **Category Card**

```kotlin
@Composable
fun CategoryCard(category: CategoryType, amount: Double) {
    val categoryColor = when (category) {
        CategoryType.FOOD -> PremiumColors.CategoryFood
        CategoryType.TRANSPORT -> PremiumColors.CategoryTransport
        CategoryType.SHOPPING -> PremiumColors.CategoryShopping
        CategoryType.BILLS -> PremiumColors.CategoryBills
        CategoryType.ENTERTAINMENT -> PremiumColors.CategoryEntertainment
        CategoryType.HEALTH -> PremiumColors.CategoryHealth
        CategoryType.TRAVEL -> PremiumColors.CategoryTravel
        CategoryType.TECH -> PremiumColors.CategoryTech
        CategoryType.INCOME -> PremiumColors.CategoryIncome
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category icon with colored background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(categoryColor.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getCategoryIcon(category),
                    contentDescription = null,
                    tint = categoryColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Category name and amount
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$${"%.2f".format(amount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Colored accent bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(48.dp)
                    .background(categoryColor, RoundedCornerShape(2.dp))
            )
        }
    }
}
```

### **Category List**

```kotlin
@Composable
fun CategoryList() {
    Column(modifier = Modifier.padding(16.dp)) {
        CategoryRow("Food", PremiumColors.CategoryFood, "$234.50")
        CategoryRow("Transport", PremiumColors.CategoryTransport, "$89.20")
        CategoryRow("Shopping", PremiumColors.CategoryShopping, "$456.78")
        CategoryRow("Bills", PremiumColors.CategoryBills, "$120.00")
        CategoryRow("Entertainment", PremiumColors.CategoryEntertainment, "$67.30")
        CategoryRow("Health", PremiumColors.CategoryHealth, "$145.90")
        CategoryRow("Travel", PremiumColors.CategoryTravel, "$890.00")
        CategoryRow("Tech", PremiumColors.CategoryTech, "$299.99")
        CategoryRow("Income", PremiumColors.CategoryIncome, "$3,500.00")
    }
}

@Composable
fun CategoryRow(name: String, color: Color, amount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = amount,
            style = MaterialTheme.typography.titleMedium,
            color = color
        )
    }
}
```

---

## 5. Typography

Use `MaterialTheme.typography` for all text styles. The typography system includes 15 variants.

### **Display Styles** (Hero Text)

```kotlin
@Composable
fun HeroSection() {
    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = "Welcome",
            style = MaterialTheme.typography.displayLarge,  // 57sp
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Text(
            text = "To Mizan",
            style = MaterialTheme.typography.displayMedium, // 45sp
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Text(
            text = "Your Finance App",
            style = MaterialTheme.typography.displaySmall,  // 36sp
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
```

### **Headline Styles** (Section Headers)

```kotlin
@Composable
fun SectionHeaders() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Recent Transactions",
            style = MaterialTheme.typography.headlineLarge,  // 32sp
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "This Month",
            style = MaterialTheme.typography.headlineMedium, // 28sp
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Categories",
            style = MaterialTheme.typography.headlineSmall,  // 24sp
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
```

### **Title Styles** (Card Titles)

```kotlin
@Composable
fun CardWithTitles() {
    Card(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Balance Overview",
                style = MaterialTheme.typography.titleLarge,   // 22sp
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Monthly Summary",
                style = MaterialTheme.typography.titleMedium,  // 16sp Medium
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Last Updated",
                style = MaterialTheme.typography.titleSmall,   // 14sp Medium
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
```

### **Body Styles** (Content Text)

```kotlin
@Composable
fun ContentText() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "This is large body text for important content.",
            style = MaterialTheme.typography.bodyLarge,   // 16sp
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "This is medium body text for regular content.",
            style = MaterialTheme.typography.bodyMedium,  // 14sp
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "This is small body text for secondary content.",
            style = MaterialTheme.typography.bodySmall,   // 12sp
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
```

### **Label Styles** (Buttons, Tags, Captions)

```kotlin
@Composable
fun LabelsAndTags() {
    Column(modifier = Modifier.padding(16.dp)) {
        // Large label - for prominent buttons
        Button(onClick = { }) {
            Text(
                text = "Large Button",
                style = MaterialTheme.typography.labelLarge  // 14sp Medium
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Medium label - for chips and tags
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Tag",
                style = MaterialTheme.typography.labelMedium,  // 12sp Medium
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Small label - for captions and metadata
        Text(
            text = "Caption or metadata",
            style = MaterialTheme.typography.labelSmall,   // 11sp Medium
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
```

### **Typography Combinations**

```kotlin
@Composable
fun TransactionItem(
    title: String,
    category: String,
    amount: String,
    date: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            // Transaction title - titleMedium
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Category - bodySmall
            Text(
                text = category,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(2.dp))
            
            // Date - labelSmall
            Text(
                text = date,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // Amount - titleLarge
        Text(
            text = amount,
            style = MaterialTheme.typography.titleLarge,
            color = PremiumColors.Primary
        )
    }
}
```

---

## 6. Best Practices

### **✅ DO: Use Theme Colors**

```kotlin
// ✅ Good - Uses theme colors
Text(
    text = "Hello",
    color = MaterialTheme.colorScheme.onBackground
)

Box(
    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
)
```

### **❌ DON'T: Hardcode Colors**

```kotlin
// ❌ Bad - Hardcoded color won't adapt to theme
Text(
    text = "Hello",
    color = Color.Black  // Won't work in dark mode!
)

Box(
    modifier = Modifier.background(Color.White)  // Won't work in dark mode!
)
```

### **✅ DO: Use Typography Styles**

```kotlin
// ✅ Good - Uses typography system
Text(
    text = "Title",
    style = MaterialTheme.typography.titleLarge
)
```

### **❌ DON'T: Hardcode Font Sizes**

```kotlin
// ❌ Bad - Hardcoded font size
Text(
    text = "Title",
    fontSize = 22.sp,
    fontWeight = FontWeight.Bold
)
```

### **✅ DO: Use PremiumColors for Brand Elements**

```kotlin
// ✅ Good - Premium colors for brand elements
Box(
    modifier = Modifier.background(PremiumColors.GradientPrimary)
)

Icon(tint = PremiumColors.CategoryFood)
```

### **✅ DO: Combine Theme and Premium Colors**

```kotlin
// ✅ Good - Mix theme colors with premium accents
Card(
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
    )
) {
    Row {
        Icon(tint = PremiumColors.CategoryFood)  // Premium accent
        Text(
            text = "Food",
            color = MaterialTheme.colorScheme.onSurface  // Theme color
        )
    }
}
```

---

## 7. Common Patterns

### **Pattern 1: Colored Status Badge**

```kotlin
@Composable
fun StatusBadge(status: TransactionStatus) {
    val (backgroundColor, textColor, text) = when (status) {
        TransactionStatus.SUCCESS -> Triple(
            PremiumColors.Success.copy(alpha = 0.15f),
            PremiumColors.Success,
            "Success"
        )
        TransactionStatus.PENDING -> Triple(
            PremiumColors.Warning.copy(alpha = 0.15f),
            PremiumColors.WarningDark,
            "Pending"
        )
        TransactionStatus.FAILED -> Triple(
            PremiumColors.Error.copy(alpha = 0.15f),
            PremiumColors.ErrorDark,
            "Failed"
        )
    }
    
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
```

### **Pattern 2: Gradient Header with Content**

```kotlin
@Composable
fun GradientHeader(
    title: String,
    subtitle: String,
    amount: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(PremiumColors.GradientPrimary)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = amount,
                style = MaterialTheme.typography.displaySmall,
                color = Color.White
            )
        }
    }
}
```

### **Pattern 3: Category Breakdown Chart**

```kotlin
@Composable
fun CategoryBreakdown(categories: List<CategoryData>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Spending by Category",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        categories.forEach { category ->
            CategoryBreakdownRow(
                name = category.name,
                color = getCategoryColor(category.type),
                amount = category.amount,
                percentage = category.percentage
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun CategoryBreakdownRow(
    name: String,
    color: Color,
    amount: Double,
    percentage: Float
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(color, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "$${"%.2f".format(amount)}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage)
                    .fillMaxHeight()
                    .background(
                        color = color,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

fun getCategoryColor(type: CategoryType): Color {
    return when (type) {
        CategoryType.FOOD -> PremiumColors.CategoryFood
        CategoryType.TRANSPORT -> PremiumColors.CategoryTransport
        CategoryType.SHOPPING -> PremiumColors.CategoryShopping
        CategoryType.BILLS -> PremiumColors.CategoryBills
        CategoryType.ENTERTAINMENT -> PremiumColors.CategoryEntertainment
        CategoryType.HEALTH -> PremiumColors.CategoryHealth
        CategoryType.TRAVEL -> PremiumColors.CategoryTravel
        CategoryType.TECH -> PremiumColors.CategoryTech
        CategoryType.INCOME -> PremiumColors.CategoryIncome
    }
}
```

### **Pattern 4: Glassmorphism Card**

```kotlin
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                color = PremiumColors.GlassBg,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = PremiumColors.GlassBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        content()
    }
}

// Usage
@Composable
fun GlassCardExample() {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Glass Card",
                style = MaterialTheme.typography.titleLarge,
                color = PremiumColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "With glassmorphism effect",
                style = MaterialTheme.typography.bodyMedium,
                color = PremiumColors.TextSecondary
            )
        }
    }
}
```

---

## 📚 Quick Reference

### **Color Access**

```kotlin
// Material3 Colors (theme-aware)
MaterialTheme.colorScheme.primary
MaterialTheme.colorScheme.surface
MaterialTheme.colorScheme.onBackground

// Premium Colors (direct access)
PremiumColors.Primary
PremiumColors.BgPrimary
PremiumColors.TextSecondary
PremiumColors.Surface2

// Category Colors
PremiumColors.CategoryFood
PremiumColors.CategoryTransport

// Gradients
PremiumColors.GradientPrimary
PremiumColors.GradientSecondary
```

### **Typography Access**

```kotlin
MaterialTheme.typography.displayLarge    // 57sp
MaterialTheme.typography.headlineLarge   // 32sp
MaterialTheme.typography.titleLarge      // 22sp
MaterialTheme.typography.bodyLarge       // 16sp
MaterialTheme.typography.labelLarge      // 14sp Medium
```

### **Common Modifiers**

```kotlin
// Solid background
Modifier.background(MaterialTheme.colorScheme.surface)

// Gradient background
Modifier.background(PremiumColors.GradientPrimary)

// Border
Modifier.border(1.dp, MaterialTheme.colorScheme.outline)

// Glass effect
Modifier
    .background(PremiumColors.GlassBg)
    .border(1.dp, PremiumColors.GlassBorder)
```

---

**Happy Theming! 🎨**

For more information, see `THEME_FOUNDATION.md` for complete color palette and design tokens.
