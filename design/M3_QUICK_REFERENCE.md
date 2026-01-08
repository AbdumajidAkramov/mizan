# M3 Quick Reference Card
## Material Design 3 Cheat Sheet

---

## 🎨 Colors (Top 10)

```css
--md-sys-color-primary              #6750A4    /* Brand color */
--md-sys-color-on-primary           #FFFFFF    /* Text on primary */
--md-sys-color-primary-container    #EADDFF    /* Filled buttons */
--md-sys-color-secondary-container  #E8DEF8    /* Tonal buttons */
--md-sys-color-surface              #FFFBFE    /* Background */
--md-sys-color-on-surface           #1C1B1F    /* Text */
--md-sys-color-surface-variant      #E7E0EC    /* Alt surface */
--md-sys-color-outline              #79747E    /* Borders */
--md-sys-color-error                #B3261E    /* Errors */
--md-sys-color-surface-container-low #F7F2FA   /* Elevation 1 */
```

---

## 📐 Shapes

```css
Extra Large  28dp  /* Hero cards (BalanceCard) */
Large        16dp  /* FAB */
Medium       12dp  /* Standard cards */
Small         8dp  /* Compact elements */
Full       9999dp  /* Pills, chips, circles */
```

---

## 📝 Typography (Top 8)

```
Display Small    36px/44px/400  /* Balance: $8,450.50 */
Headline Medium  28px/36px/400  /* Screen titles */
Headline Small   24px/32px/400  /* Section headers */
Title Large      22px/28px/400  /* List headers */
Title Medium     16px/24px/500  /* Card titles */
Body Large       16px/24px/400  /* Body text */
Body Medium      14px/20px/400  /* Default body */
Label Large      14px/20px/500  /* Buttons */
```

---

## 📏 Spacing (8dp Grid)

```
xs   4px   0.5×   /* Tight gaps */
sm   8px   1×     /* Standard gaps */
md   16px  2×     /* Card padding */
lg   24px  3×     /* Large padding */
xl   32px  4×     /* XL padding */
```

---

## 🏗️ Elevation (Surface Tints)

```
Level 0  surface                     /* Base */
Level 1  surface-container-low       /* Cards */
Level 2  surface-container           /* Elevated */
Level 3  surface-container-high      /* Dialog */
```

**Key**: NO box shadows, use bg color changes!

---

## 🧩 Component Quick Start

### Button
```tsx
<M3Button variant="filled">Primary</M3Button>
<M3Button variant="filled-tonal">Secondary</M3Button>
<M3Button variant="outlined">Tertiary</M3Button>
```

### IconButton
```tsx
<M3IconButton 
  icon={<X size={20} />}
  label="Close"
  variant="filled-tonal"
/>
```

### Card
```tsx
<M3Card variant="elevated">
  <div className="p-[var(--md-sys-spacing-md)]">
    Content
  </div>
</M3Card>
```

### Surface
```tsx
<M3Surface elevation={1} shape="medium">
  {children}
</M3Surface>
```

### Typography
```tsx
<h1 className="headline-medium">Title</h1>
<p className="body-medium">Text</p>
<span className="label-large">Button</span>
```

---

## 🎯 Common Patterns

### Card with Padding
```tsx
<M3Surface 
  elevation={1} 
  shape="medium"
  className="p-[var(--md-sys-spacing-md)]"
>
  {content}
</M3Surface>
```

### Hero Card (28dp)
```tsx
<div className="
  rounded-[var(--md-sys-shape-corner-extra-large)]
  p-[var(--md-sys-spacing-lg)]
  bg-gradient-to-br from-[var(--md-sys-color-primary)] to-[var(--md-sys-color-secondary)]
">
  {hero content}
</div>
```

### List Item
```tsx
<M3TransactionListItem
  transaction={transaction}
  onClick={handleClick}
/>
```

### Button Group
```tsx
<div className="flex gap-[var(--md-sys-spacing-sm)]">
  <M3Button variant="filled">Save</M3Button>
  <M3Button variant="outlined">Cancel</M3Button>
</div>
```

---

## 🔄 State Management (MVI)

```tsx
// Define state
const [state, setState] = useState<UiState<Data>>({
  status: 'loading'
});

// State transitions
'idle' → 'loading' → 'success' | 'error' | 'empty'

// Render based on state
if (state.status === 'loading') return <LoadingSkeleton />;
if (state.status === 'error') return <ErrorState />;
if (state.status === 'empty') return <EmptyState />;
return <Content data={state.data} />;
```

---

## 🎨 Color Usage Rules

```tsx
/* Background */
bg-[var(--md-sys-color-surface)]

/* Text */
text-[var(--md-sys-color-on-surface)]

/* Button - Filled */
bg-[var(--md-sys-color-primary)]
text-[var(--md-sys-color-on-primary)]

/* Button - Tonal */
bg-[var(--md-sys-color-secondary-container)]
text-[var(--md-sys-color-on-secondary-container)]

/* Border */
border-[var(--md-sys-color-outline)]

/* Hover State */
hover:bg-[var(--md-sys-color-on-surface)]/[0.08]
```

---

## 📱 Screen Layout

```tsx
<div className="max-w-lg mx-auto px-[var(--md-sys-spacing-md)] pt-[var(--md-sys-spacing-lg)]">
  <h1 className="headline-medium mb-[var(--md-sys-spacing-lg)]">
    Screen Title
  </h1>
  
  <div className="flex flex-col gap-[var(--md-sys-spacing-md)]">
    {/* Content cards */}
  </div>
</div>
```

---

## ✅ Pre-flight Checklist

Before using a component:
- [ ] Using correct M3 color token
- [ ] Using M3 shape size
- [ ] Using M3 typography class
- [ ] Following 8dp spacing
- [ ] Using surface tint (not shadow)
- [ ] Proper elevation level
- [ ] State layers for interactions

---

## 🚀 Quick Commands

```bash
# View theme tokens
cat src/styles/material3-theme.css

# Find all M3 components
ls src/app/components/atoms/M3*.tsx
ls src/app/components/molecules/M3*.tsx

# Run app
npm run dev
```

---

## 🎯 Android Translation

| React | Kotlin |
|-------|--------|
| `<M3Button variant="filled">` | `Button()` |
| `<M3Card variant="elevated">` | `Card(colors = elevatedCardColors())` |
| `<M3Surface elevation={1}>` | `Surface(tonalElevation = 1.dp)` |
| `className="headline-medium"` | `style = MaterialTheme.typography.headlineMedium` |
| `bg-[var(--md-sys-color-primary)]` | `containerColor = MaterialTheme.colorScheme.primary` |
| `p-[var(--md-sys-spacing-md)]` | `Modifier.padding(16.dp)` |

---

## 📚 Full Docs

- **M3 Guide**: `/MATERIAL3_GUIDE.md`
- **Implementation**: `/M3_IMPLEMENTATION_SUMMARY.md`
- **Architecture**: `/ARCHITECTURE.md`
- **Android Translation**: `/ANDROID_TRANSLATION_GUIDE.md`

---

**Keep this card handy while building M3 components! 🚀**
