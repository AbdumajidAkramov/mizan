# 🏷️ Category Management - Visual Quick Guide
## Premium Mizan Design System

**Date:** January 29, 2026  
**Status:** ✅ Production Ready

---

## 📱 **Screen Layout Preview**

```
┌──────────────────────────────────────────────────────┐
│  ← Manage Categories (8 categories)    [Add New]    │ ← Toolbar
├──────────────────────────────────────────────────────┤
│                                                      │
│  ╔════════════════════════════════════════════════╗ │
│  ║ ⣿ Drag the grip handle to reorder categories  ║ │ ← Info Card
│  ║   Click edit to modify or delete to remove.   ║ │
│  ╚════════════════════════════════════════════════╝ │
│                                                      │
│  ┌────────────────────────────────────────────────┐ │
│  │ ⣿⣿ 🍴 Food & Dining           2↓ ✎ 🗑     │ │ ← Category Row
│  │     8 subcategories                           │ │
│  └────────────────────────────────────────────────┘ │
│                                                      │
│  ┌────────────────────────────────────────────────┐ │
│  │ ⣿⣿ 🚗 Transportation         > ✎ 🗑      │ │
│  └────────────────────────────────────────────────┘ │
│                                                      │
│  ┌────────────────────────────────────────────────┐ │
│  │ ⣿⣿ 🛍️  Shopping               > ✎ 🗑      │ │
│  └────────────────────────────────────────────────┘ │
│                                                      │
│  ┌────────────────────────────────────────────────┐ │
│  │ ⣿⣿ 🧾 Bills & Utilities       > ✎ 🗑      │ │
│  └────────────────────────────────────────────────┘ │
│                                                      │
│                                          ┌────────┐  │
│                                          │ + Add  │  │ ← FAB
│                                          │Category│  │
│                                          └────────┘  │
└──────────────────────────────────────────────────────┘

Legend:
⣿⣿ = Drag handle
🍴 = Category icon (colored)
↓ = Expand button (with subcategories)
> = Expand button (collapsed)
✎ = Edit button
🗑 = Delete button
```

---

## 🎨 **Category Row Anatomy**

### **Collapsed Category**

```
┌───────────────────────────────────────────────────────────┐
│  ⣿⣿   🍴   Food & Dining              >    ✎    🗑      │
│       ↑        ↑                      ↑     ↑    ↑        │
│     Icon    Name                 Expand  Edit Delete     │
│   (48px)                         (32px) (32px) (32px)    │
└───────────────────────────────────────────────────────────┘
 ↑
Drag Handle (20px)
```

### **Expanded Category with Subcategories**

```
┌───────────────────────────────────────────────────────────┐
│  ⣿⣿   🍴   Food & Dining              ↓    ✎    🗑      │
│             8 subcategories                               │
└───────────────────────────────────────────────────────────┘
    ┌─────────────────────────────────────────────────┐
    │ ▌ Restaurants                             ✎     │
    ├─────────────────────────────────────────────────┤
    │ ▌ Groceries                               ✎     │
    ├─────────────────────────────────────────────────┤
    │ ▌ Coffee & Cafes                          ✎     │
    ├─────────────────────────────────────────────────┤
    │ ▌ Fast Food                               ✎     │
    ├─────────────────────────────────────────────────┤
    │ ▌ Delivery                                ✎     │
    └─────────────────────────────────────────────────┘
     ↑
    Colored bar (4px wide, category color)
```

---

## 🎭 **Visual States**

### **1. Default State**

```
┌──────────────────────────────────────────────────┐
│  ⣿⣿  🍴  Food & Dining        >  ✎  🗑       │
└──────────────────────────────────────────────────┘
Background: rgba(255, 255, 255, 0.7)
Border: 1px solid rgba(0, 0, 0, 0.08)
Shadow: Light
```

### **2. Hover State**

```
┌══════════════════════════════════════════════════┐
║  ⣿⣿  🍴  Food & Dining        >  ✎  🗑       ║ ← Emerald tint
└══════════════════════════════════════════════════┘
Border: 1px solid rgba(16, 185, 129, 0.3)
Shadow: Medium
```

### **3. Dragging State**

```
┌──────────────────────────────────────────────────┐
│  ⣿⣿  🍴  Food & Dining        >  ✎  🗑       │ ← 50% opacity
└──────────────────────────────────────────────────┘
Opacity: 0.5
Scale: 0.95
Cursor: grabbing
```

### **4. Drop Zone State**

```
┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
┃  ⣿⣿  🍴  Food & Dining        >  ✎  🗑       ┃ ← Emerald border
┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
Border: 2px solid #10B981
Background: rgba(16, 185, 129, 0.05)
Scale: 1.05
```

---

## 🎨 **Button States**

### **Edit Button**

```
Default:        Hover:          Active:
┌────┐         ┌────┐          ┌────┐
│ ✎  │   →     │ ✎  │    →     │ ✎  │
└────┘         └────┘          └────┘
Gray           Emerald         Emerald
bg-surface-2   bg-emerald/10   Scale 0.95
               text-emerald
```

### **Delete Button**

```
Default:        Hover:          Active:
┌────┐         ┌────┐          ┌────┐
│ 🗑  │   →     │ 🗑  │    →     │ 🗑  │
└────┘         └────┘          └────┘
Gray           Red             Red
bg-surface-2   bg-error/10     Scale 0.95
               text-error
```

### **Expand Button**

```
Collapsed:      Hover:          Expanded:
┌────┐         ┌────┐          ┌────┐
│ >  │   →     │ >  │    →     │ ↓  │
└────┘         └────┘          └────┘
                               Rotate 90°
```

---

## 💬 **Add/Edit Category Modal**

```
┌────────────────────────────────────────────────┐
│  Add Category                            ✕    │ ← Header
├────────────────────────────────────────────────┤
│                                                │
│  Category Name                                 │
│  ┌──────────────────────────────────────────┐ │
│  │ Groceries                                │ │
│  └──────────────────────────────────────────┘ │
│                                                │
│  Choose Icon                                   │
│  ┌───┬───┬───┬───┬───┬───┐                   │
│  │ 🍴│ 🚗│ 🛍️ │ 🧾│ 🎬│ ❤️ │                   │
│  ├───┼───┼───┼───┼───┼───┤                   │
│  │ ✈️ │ 📱│ 📈│ ☕│ 🏠│ 💼│                   │
│  ├───┼───┼───┼───┼───┼───┤                   │
│  │ 📚│ 🎵│ 📷│ 🏋️ │ 🎁│ ⚡│                   │
│  └───┴───┴───┴───┴───┴───┘                   │
│      Selected: 🍴                              │
│                                                │
│  Choose Color                                  │
│  ┌───┬───┬───┬───┬───┬───┐                   │
│  │🟥│🔵│🟠│🔷│🟣│🔴│                   │
│  ├───┼───┼───┼───┼───┼───┤                   │
│  │🟦│🟢│💚│🟡│🌹│🔵│                   │
│  └───┴───┴───┴───┴───┴───┘                   │
│      Selected: 🟠                              │
│                                                │
│  Category Type                                 │
│  ┌──────────────┬──────────────┐              │
│  │ Main Category│ Subcategory  │              │
│  │   [Active]   │              │              │
│  └──────────────┴──────────────┘              │
│                                                │
│  Preview                                       │
│  ┌──────────────────────────────────────────┐ │
│  │  🍴   Main Category                      │ │
│  │      Groceries                           │ │
│  └──────────────────────────────────────────┘ │
│                                                │
├────────────────────────────────────────────────┤
│  [  Cancel  ]         [  Create  ]            │ ← Footer
└────────────────────────────────────────────────┘

Icon Sizes:
- Icon button: 40px × 40px
- Icon inside: 20px
- Color button: 40px × 40px

Selected State:
- Icon: Emerald border + emerald bg
- Color: Emerald border + checkmark
```

---

## ⚠️ **Delete Confirmation Dialog**

```
┌────────────────────────────────────────────────┐
│                                                │
│    ⚠️                                          │
│                                                │
│    Delete Category                        ✕   │
│                                                │
│    Are you sure you want to delete this       │
│    category? This action cannot be undone     │
│    and may affect existing transactions.      │
│                                                │
│    [    Cancel    ]  [    Delete    ]         │
│                         ↑                      │
│                     Red button                 │
└────────────────────────────────────────────────┘

Icon:
- Size: 48px circle
- Background: Red 10% (destructive) or Emerald 10% (primary)
- Icon: AlertTriangle (24px)

Buttons:
- Cancel: Gray, outline style
- Delete: Red gradient, solid
- Confirm: Emerald gradient, solid (non-destructive)
```

---

## 🎯 **Floating Action Button (FAB)**

### **Icon Only**

```
       ┌────────┐
       │        │
       │   +    │  ← 56px × 56px circle
       │        │     Emerald gradient
       └────────┘     Shadow + glow
```

### **With Label**

```
       ┌──────────────┐
       │  +  Add      │  ← Auto width
       │   Category   │     56px height
       └──────────────┘     Rounded pill
```

### **States**

```
Default:         Hover:           Active:
┌────────┐      ┌────────┐       ┌────────┐
│   +    │  →   │   +    │   →   │   +    │
└────────┘      └────────┘       └────────┘
Scale: 1.0      Scale: 1.05      Scale: 0.95
Shadow: XL      Glow: Strong     
```

### **Positions**

```
Bottom-Right:          Bottom-Center:         Bottom-Left:
                       
                              │
                              ↓
                       ┌────────┐
         ┌────────┐    │   +    │    ┌────────┐
         │   +    │    └────────┘    │   +    │
         └────────┘                  └────────┘

X: right-24px     X: center         X: left-24px
Y: bottom-24px    Y: bottom-24px    Y: bottom-24px
```

---

## 📏 **Spacing Measurements**

### **Category Row**

```
┌───────────────────────────────────────────────┐
│ 16px padding                                  │
│                                               │
│  [20px] [16px] [48px] [16px] [...] [16px] [32px] [16px] [32px] [16px] [32px]
│   ↑      gap    icon    gap    text    gap   expand gap   edit   gap  delete
│  Grip                                                                    │
│                                               │
│ 16px padding                                  │
└───────────────────────────────────────────────┘
 ↑                                             ↑
 16px margin bottom

Total Height: ~80px (with padding + icon size)
```

### **Modal Layout**

```
┌────────────────────────────────────┐
│ 24px padding                       │
│                                    │
│  Title + Close Button              │
│                                    │
│ 24px padding                       │
├────────────────────────────────────┤ ← Border
│ 24px padding                       │
│                                    │
│  Form Fields                       │
│  (32px gap between fields)         │
│                                    │
│ 24px padding                       │
├────────────────────────────────────┤ ← Border
│ 24px padding                       │
│                                    │
│  Cancel / Save Buttons             │
│                                    │
│ 24px padding                       │
└────────────────────────────────────┘

Max Width: 480px
Max Height: 80vh (scrollable content)
```

---

## 🎨 **Color Palette**

### **Category Colors Available**

```
┌────┬────┬────┬────┬────┬────┐
│ 🌸 │ 🔷 │ 🍊 │ 💧 │ 🍇 │ ❤️  │
├────┼────┼────┼────┼────┼────┤
│ 🌀 │ 🌲 │ 💚 │ ⭐ │ 🌹 │ 🌊 │
└────┴────┴────┴────┴────┴────┘

1.  Pink      #ff6b9d  var(--premium-cat-food)
2.  Blue      #4facfe  var(--premium-cat-transport)
3.  Orange    #ffa34d  var(--premium-cat-shopping)
4.  Cyan      #00d2ff  var(--premium-cat-bills)
5.  Purple    #c471f5  var(--premium-cat-entertainment)
6.  Red       #ff6b6b  var(--premium-cat-health)
7.  Indigo    #667eea  var(--premium-cat-travel)
8.  Teal      #00f2a0  var(--premium-cat-tech)
9.  Emerald   #10b981  var(--premium-emerald)
10. Yellow    #fee140  var(--premium-warning)
11. Rose      #f5576c  var(--premium-secondary)
12. Sky       #00f2fe  var(--premium-cat-income)
```

### **UI Colors**

```
Primary (Emerald):
  Base:  #10B981
  Dark:  #059669
  Light: #34D399

Error (Red):
  Base:  #ff6b6b
  Dark:  #ee5253
  Light: #ff8a8a

Text:
  Primary:   #1a1a2e (darkest)
  Secondary: #4a5568 (medium)
  Tertiary:  #718096 (light)
  Muted:     #a0aec0 (lightest)

Surfaces:
  Level 1: rgba(0,0,0,0.02)
  Level 2: rgba(0,0,0,0.04)
  Level 3: rgba(0,0,0,0.08)
  Level 4: rgba(0,0,0,0.12)
```

---

## 🎬 **Animations**

### **Fade In**

```
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}
Duration: 200ms
Easing: ease-out
```

### **Scale In**

```
@keyframes scaleIn {
  from {
    opacity: 0;
    transform: scale(0.95);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}
Duration: 200ms
Easing: ease-out
```

### **Slide Down**

```
@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-8px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
Duration: 200ms
Easing: ease-out
```

### **Drag States**

```
Start Drag:
  - Opacity: 1 → 0.5 (200ms)
  - Scale: 1 → 0.95 (200ms)
  - Cursor: grab → grabbing

End Drag:
  - Opacity: 0.5 → 1 (200ms)
  - Scale: 0.95 → 1 (200ms)
  - Cursor: grabbing → grab
```

---

## 📱 **Empty State**

```
┌────────────────────────────────────────────────┐
│                                                │
│                                                │
│               ┌────────────┐                   │
│               │            │                   │
│               │     +      │  ← 96px circle    │
│               │            │     Dashed border │
│               └────────────┘     Emerald       │
│                                                │
│           No categories yet                    │
│                                                │
│     Create your first category to start        │
│     organizing your transactions               │
│                                                │
│          ┌──────────────────┐                  │
│          │ Create Your First│                  │
│          │    Category      │                  │
│          └──────────────────┘                  │
│                                                │
│                                                │
└────────────────────────────────────────────────┘

Icon Container:
- Size: 96px × 96px
- Background: Emerald 10% gradient
- Border: 2px dashed emerald/30
- Icon: Plus 40px

Text:
- Title: heading-lg (24px)
- Description: body-md (14px), max-width 320px
- Button: Emerald gradient, 16px padding
```

---

## 🔍 **Interactive Zones**

### **Touch Targets**

```
Minimum Size: 44px × 44px (Apple/Google guidelines)

┌──────────────────────────────────────────────┐
│  [Drag: 44px] [Icon: 48px] [Text: flexible] │
│  [Expand: 44px] [Edit: 44px] [Delete: 44px] │
└──────────────────────────────────────────────┘

Spacing between buttons: 16px
```

### **Drag Handle Area**

```
Visible: 20px icon
Clickable: 44px area (with padding)

┌────────┐
│  ⣿⣿   │  ← Visual: 20px
│        │  ← Touch area: 44px
│  ⣿⣿   │
└────────┘
```

---

## ✨ **Premium Details**

### **Glassmorphism Effect**

```
Layer Stack (top to bottom):

1. Content (text, icons)
   ↓
2. Semi-transparent background
   rgba(255, 255, 255, 0.7)
   ↓
3. Backdrop blur filter
   blur(20px)
   ↓
4. Subtle border
   rgba(0, 0, 0, 0.08)
   ↓
5. Shadow
   0 2px 8px rgba(0, 0, 0, 0.06)
   ↓
6. Background (shows through)
```

### **Gradient Usage**

```
Add Button:
  from: #10B981 (emerald)
  to: #059669 (emerald-dark)
  direction: left to right (0deg)

Delete Button (Confirm):
  from: #ff6b6b (error)
  to: #ee5253 (error-dark)
  direction: left to right (0deg)

Category Icon Background:
  from: {color}40 (40% opacity)
  to: {color}20 (20% opacity)
  direction: 135deg (diagonal)
```

---

## 🎯 **Key Interactions Summary**

| Action | Trigger | Visual Feedback |
|--------|---------|-----------------|
| **Hover Row** | Mouse over | Border → emerald tint, shadow increase |
| **Start Drag** | Click drag handle | Opacity 50%, scale 0.95, cursor grabbing |
| **Drag Over** | Drag over row | Emerald border, emerald bg, scale 1.05 |
| **Drop** | Release mouse | Snap to position, restore opacity |
| **Click Edit** | Click edit button | Modal opens (scale in animation) |
| **Click Delete** | Click delete button | Confirm dialog opens (fade + scale) |
| **Click Expand** | Click chevron | Chevron rotates 90°, subcategories slide down |
| **Click FAB** | Click + button | Scale to 0.95, modal opens |

---

## 🎊 **Design Highlights**

### **What Makes It Premium**

1. ✨ **Glassmorphism** - Semi-transparent cards with blur
2. 💚 **Emerald Accents** - Consistent brand color
3. 🎭 **Smooth Animations** - 200ms transitions everywhere
4. 📐 **8dp Grid** - Perfect Material 3 spacing
5. 🎯 **Touch Friendly** - 44px minimum targets
6. ♿ **Accessible** - WCAG AA compliant
7. 🔄 **Drag & Drop** - Intuitive reordering
8. 💬 **Confirmations** - Safe destructive actions
9. 📱 **Responsive** - Mobile to desktop
10. 🎨 **Visual Hierarchy** - Clear information structure

---

## 📚 **Quick Reference**

### **Component Files**

```
ManageCategoriesScreen.tsx           ← Main (top button)
ManageCategoriesScreenWithFAB.tsx   ← FAB variant
AddEditCategoryModal.tsx            ← Add/Edit form
PremiumConfirmDialog.tsx            ← Delete confirmation
PremiumFAB.tsx                      ← Floating button
```

### **Key Props**

```typescript
// Screen
onBack: () => void

// Modal
isOpen: boolean
editCategory?: CategoryData
onClose: () => void
onSave: (data) => void

// Dialog
isOpen: boolean
title: string
message: string
onConfirm: () => void
onCancel: () => void
isDestructive?: boolean

// FAB
onClick: () => void
label?: string
position?: 'bottom-right' | 'bottom-center' | 'bottom-left'
size?: 'normal' | 'large'
```

---

**Status: ✅ Production Ready**

Beautiful, functional, and ready to use! 🎉
