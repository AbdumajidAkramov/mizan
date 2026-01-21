# Mizan - Navigation Architecture

## Screen Hierarchy & Navigation Flow

This document outlines the **complete navigation structure** for the Mizan Expense Manager app. Each screen is designed as a **standalone, decoupled component** ready for direct mapping to Android Fragments/Destinations.

---

## 1. Main Navigation Stack

```
PremiumApp (Root Container)
├── PremiumDashboardScreen (Tab: Home)
├── PremiumStatisticsScreen (Tab: Statistics)
├── PremiumFinancialMirrorScreen (Tab: Mirror)
├── PremiumProfileScreen (Tab: Profile)
└── PremiumBottomNav (Persistent Bottom Navigation)
```

**Navigation Type:** Tab-based navigation  
**Android Equivalent:** `BottomNavigationView` with `NavHostFragment`

---

## 2. Modal/Overlay Screens

### 2.1 Add Transaction Flow
**Entry Point:** Floating Action Button (FAB) in Bottom Nav  
**Exit:** Close button or Save & return to previous screen

```
PremiumAddTransactionScreen (Full-Screen Overlay)
├── Step 1: Amount Input
├── Step 2: Type Selection (Expense/Income/Transfer)
├── Step 3: Category Selection → PremiumCategoryPickerEnhanced
└── Step 4: Details & Confirmation
```

**Android Equivalent:** Full-screen `DialogFragment` or separate Activity with `FLAG_ACTIVITY_NO_ANIMATION`

---

## 3. Category Management Flow (STANDALONE)

### 3.1 Category Selection Screen
**Component:** `PremiumCategoryPickerEnhanced`  
**Location:** `/src/app/components/premium/PremiumCategoryPickerEnhanced.tsx`  
**Context:** Used within Add Transaction Flow

**Navigation Trigger:**
```tsx
<PremiumCategoryPickerEnhanced
  selectedCategory={selectedCategory}
  onSelectCategory={handleSelectCategory}
  onManageCategories={navigateToManageCategories} // ← Trigger
/>
```

**UI Elements:**
- Grid of main categories (3 columns)
- "Manage" button (gear icon) at the end of grid
- Click "Manage" → Navigate to `ManageCategoriesScreen`

---

### 3.2 Manage Categories Screen (STANDALONE)
**Component:** `ManageCategoriesScreen`  
**Location:** `/src/app/screens/ManageCategoriesScreen.tsx`  
**Type:** Full-screen, independent destination

**Navigation Flow:**
```
Category Selection → Click "Manage" → ManageCategoriesScreen
ManageCategoriesScreen → Click "Back" → Category Selection
```

**Android Mapping:**
```kotlin
// Navigation Graph
<fragment
    android:id="@+id/manageCategoriesFragment"
    android:name="com.mizan.ui.categories.ManageCategoriesFragment"
    android:label="Manage Categories">
    <action
        android:id="@+id/action_back_to_category_selection"
        app:destination="@id/categorySelectionFragment"
        app:popUpTo="@id/categorySelectionFragment"
        app:popUpToInclusive="false" />
</fragment>
```

**Props Interface:**
```tsx
export interface ManageCategoriesScreenProps {
  /** Callback when back button is clicked */
  onBack: () => void;
}
```

**Usage Example:**
```tsx
{showManageCategories && (
  <ManageCategoriesScreen
    onBack={() => {
      setShowManageCategories(false);
      setShowAddTransaction(true); // Return to Add Transaction
    }}
  />
)}
```

**Key Features:**
- **Drag & Drop:** Reorder categories using `react-dnd`
- **CRUD Operations:** Add, Edit, Delete categories
- **Expandable Lists:** View subcategories
- **Modal Integration:** Uses `AddEditCategoryModal` for add/edit actions

---

### 3.3 Add/Edit Category Modal
**Component:** `AddEditCategoryModal`  
**Location:** `/src/app/components/premium/AddEditCategoryModal.tsx`  
**Type:** Modal Dialog (reusable component)

**Android Mapping:**
```kotlin
// BottomSheetDialogFragment
class AddEditCategoryBottomSheet : BottomSheetDialogFragment() {
    // Full-screen on phones, dialog on tablets
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
}
```

**Props Interface:**
```tsx
export interface AddEditCategoryModalProps {
  isOpen: boolean;
  editCategory?: CategoryData;
  onClose: () => void;
  onSave: (category: CategoryData) => void;
}
```

**UI Components:**
- **Icon Picker:** 6-column grid, 30 icons from lucide-react
- **Color Picker:** 6-column grid, 12 brand colors
- **Name Input:** Text field with validation
- **Type Toggle:** Main Category vs. Subcategory
- **Parent Selector:** Dropdown (only for subcategories)
- **Preview Card:** Live preview of category appearance

---

## 4. Navigation State Management

### 4.1 React Implementation (Current)
```tsx
function PremiumAppContent() {
  const [showAddTransaction, setShowAddTransaction] = useState(false);
  const [showManageCategories, setShowManageCategories] = useState(false);

  // Navigation Handlers
  const openAddTransaction = () => setShowAddTransaction(true);
  const openManageCategories = () => {
    setShowAddTransaction(false);
    setShowManageCategories(true);
  };
  const closeManageCategories = () => {
    setShowManageCategories(false);
    setShowAddTransaction(true);
  };
}
```

### 4.2 Android Navigation Component Mapping
```kotlin
// NavHostFragment setup
class PremiumActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        navController = findNavController(R.id.nav_host_fragment)
        
        // Setup bottom navigation
        binding.bottomNav.setupWithNavController(navController)
        
        // FAB click listener
        binding.fab.setOnClickListener {
            navController.navigate(R.id.addTransactionFragment)
        }
    }
}

// In CategorySelectionFragment
binding.btnManageCategories.setOnClickListener {
    findNavController().navigate(R.id.action_categorySelection_to_manageCategories)
}

// In ManageCategoriesFragment
binding.toolbarBackButton.setOnClickListener {
    findNavController().navigateUp()
}
```

---

## 5. Data Flow Architecture

### 5.1 Category Data Model
```tsx
interface CategoryItem {
  id: string;
  name: string;
  icon: string;              // Icon name from lucide-react
  color: string;             // Hex color value
  colorToken: string;        // CSS variable reference
  isSubcategory: boolean;
  parentCategory?: TransactionCategory;
  subcategories?: string[];
}
```

**Android Equivalent:**
```kotlin
data class CategoryItem(
    val id: String,
    val name: String,
    val icon: String,           // Resource ID or icon name
    val color: String,          // Color hex or resource
    val colorToken: String,     // Theme attribute reference
    val isSubcategory: Boolean,
    val parentCategory: TransactionCategory? = null,
    val subcategories: List<String> = emptyList()
)
```

### 5.2 Data Persistence
**React:** In-memory state (to be replaced with Supabase)  
**Android:** 
- Room Database for local storage
- ViewModel + LiveData/Flow for reactive updates
- Repository pattern for data abstraction

---

## 6. Screen Independence Checklist

### ✅ ManageCategoriesScreen
- ✅ No direct dependencies on Dashboard
- ✅ No direct dependencies on Add Transaction screen
- ✅ Self-contained state management
- ✅ Callback-based navigation (onBack)
- ✅ Can be rendered independently
- ✅ Reusable in any navigation context
- ✅ Android-ready architecture

### ✅ AddEditCategoryModal
- ✅ Completely reusable component
- ✅ Props-driven state
- ✅ No screen dependencies
- ✅ Can be used in any screen
- ✅ Callback-based actions (onClose, onSave)

---

## 7. Visual Flow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                     PremiumApp (Root)                       │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │Dashboard │  │Statistics│  │  Mirror  │  │ Profile  │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
│                                                             │
│                    ┌─────────────────┐                      │
│                    │  FAB (+ Button) │                      │
│                    └────────┬────────┘                      │
└─────────────────────────────┼───────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│         PremiumAddTransactionScreen (Overlay)               │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │  Amount  │→ │   Type   │→ │ Category │→ │ Confirm  │   │
│  └──────────┘  └──────────┘  └────┬─────┘  └──────────┘   │
│                                    │                        │
│                     ┌──────────────┴────────────┐           │
│                     │ PremiumCategoryPicker     │           │
│                     │ Enhanced                  │           │
│                     │  ┌──────────────────────┐ │           │
│                     │  │ "Manage" Button ⚙️   │ │           │
│                     │  └─────────┬────────────┘ │           │
└─────────────────────────────────┼────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────┐
│      ManageCategoriesScreen (STANDALONE SCREEN)             │
│  ┌──────────────────────────────────────────────────────┐   │
│  │ ← Back | Manage Categories              │ + Add New │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                             │
│  ╔════════════════════════════════════════════════════╗   │
│  ║ 🟰 Food & Dining          ▸  ✏️  🗑️               ║   │
│  ║ 🟰 Transport              ▸  ✏️  🗑️               ║   │
│  ║ 🟰 Shopping               ▸  ✏️  🗑️               ║   │
│  ╚════════════════════════════════════════════════════╝   │
│                                                             │
│                     (Drag & Drop enabled)                   │
│                                                             │
│  Click "+ Add New" or "✏️ Edit" triggers:                   │
│                      ▼                                      │
│  ┌────────────────────────────────────────────────┐         │
│  │   AddEditCategoryModal (BottomSheet)           │         │
│  │   ┌────────────────────────────────────────┐   │         │
│  │   │ Icon Picker (6 cols, 30 icons)         │   │         │
│  │   │ Color Picker (6 cols, 12 colors)       │   │         │
│  │   │ Name Input                              │   │         │
│  │   │ Type Toggle (Main/Sub)                  │   │         │
│  │   │ Parent Selector (if Sub)                │   │         │
│  │   │ Preview Card                            │   │         │
│  │   └────────────────────────────────────────┘   │         │
│  └────────────────────────────────────────────────┘         │
└─────────────────────────────────────────────────────────────┘
```

---

## 8. File Structure

```
src/
├── app/
│   ├── screens/
│   │   ├── ManageCategoriesScreen.tsx          ← STANDALONE SCREEN
│   │   ├── PremiumAddTransactionScreen.tsx     ← Overlay Screen
│   │   ├── PremiumDashboardScreen.tsx
│   │   ├── PremiumStatisticsScreen.tsx
│   │   ├── PremiumFinancialMirrorScreen.tsx
│   │   └── PremiumProfileScreen.tsx
│   │
│   └── components/
│       └── premium/
│           ├── AddEditCategoryModal.tsx        ← Reusable Modal
│           ├── PremiumCategoryPickerEnhanced.tsx
│           └── PremiumBottomNav.tsx
│
└── mocks/
    └── data.ts                                  ← Category data source
```

---

## 9. Android Implementation Checklist

### For Android Architect:

- [ ] Create `ManageCategoriesFragment` extending `Fragment`
- [ ] Create `AddEditCategoryBottomSheet` extending `BottomSheetDialogFragment`
- [ ] Setup Navigation Graph with proper actions
- [ ] Implement drag-and-drop using `ItemTouchHelper`
- [ ] Create `CategoryViewModel` with MVI pattern
- [ ] Setup Room entities for Category persistence
- [ ] Implement Material Motion transitions
- [ ] Add proper back stack management
- [ ] Ensure theme consistency (Light/Dark mode)
- [ ] Implement proper state restoration on configuration changes

---

## 10. Key Takeaways

1. **ManageCategoriesScreen is FULLY DECOUPLED** from Dashboard
2. **Only accessible via Category Selection screen**
3. **Callback-based navigation** makes it router-agnostic
4. **Standalone state management** - no dependencies
5. **Android-ready architecture** - direct Fragment mapping
6. **Clean separation of concerns** for easy porting

---

## Questions or Clarifications?

This architecture ensures that **Windsurf AI** or any Android developer can:
- Understand the complete navigation flow
- Map React screens to Android Fragments 1:1
- Implement proper back navigation
- Maintain screen independence and reusability
