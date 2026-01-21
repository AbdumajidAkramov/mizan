# Navigation Implementation Guide

## Current React Implementation

This document shows the **exact implementation** of the decoupled navigation system for the Manage Categories feature.

---

## 1. State Management in PremiumApp.tsx

```tsx
function PremiumAppContent() {
  // Navigation State
  const [activeTab, setActiveTab] = useState<PremiumNavTab>('home');
  const [showAddTransaction, setShowAddTransaction] = useState(false);
  const [showManageCategories, setShowManageCategories] = useState(false);
  
  // ... other state
}
```

**Key Points:**
- `showManageCategories` is completely independent of `activeTab`
- No coupling to Dashboard or any other tab
- Simple boolean flag for show/hide

---

## 2. Navigation Handlers

```tsx
// Open Add Transaction (from FAB)
const handleAddExpense = () => {
  setShowAddTransaction(true);
};

// Close Add Transaction
const handleCloseAddExpense = () => {
  setShowAddTransaction(false);
};

// Navigate from Category Selection → Manage Categories
const handleNavigateToManageCategories = () => {
  setShowAddTransaction(false);      // Hide Add Transaction
  setShowManageCategories(true);     // Show Manage Categories
};

// Navigate back from Manage Categories → Category Selection
const handleBackFromManageCategories = () => {
  setShowManageCategories(false);    // Hide Manage Categories
  setShowAddTransaction(true);       // Show Add Transaction (Category Selection)
};
```

**Android Equivalent:**
```kotlin
// In Activity or Navigation Host
fun navigateToManageCategories() {
    navController.navigate(R.id.action_categorySelection_to_manageCategories)
}

fun navigateBackToCategorySelection() {
    navController.navigateUp()
}
```

---

## 3. Screen Rendering Logic

```tsx
return (
  <div className="min-h-screen bg-[var(--premium-bg-primary)]">
    {/* Main Content - Tab Navigation */}
    <main>{renderScreen()}</main>
    
    {/* Bottom Navigation */}
    <PremiumBottomNav
      activeTab={activeTab}
      onTabChange={setActiveTab}
      onAddExpense={handleAddExpense}
    />

    {/* OVERLAY SCREEN 1: Add Transaction */}
    {showAddTransaction && (
      <PremiumAddTransactionScreen
        onClose={handleCloseAddExpense}
        onSave={handleSaveTransaction}
        onManageCategories={handleNavigateToManageCategories}
      />
    )}

    {/* OVERLAY SCREEN 2: Manage Categories (STANDALONE) */}
    {showManageCategories && (
      <ManageCategoriesScreen
        onBack={handleBackFromManageCategories}
      />
    )}
  </div>
);
```

**Key Architecture:**
- ✅ Manage Categories is rendered **separately** from Add Transaction
- ✅ No nesting or parent-child relationship
- ✅ Both are top-level overlays
- ✅ Independent lifecycle

---

## 4. PremiumAddTransactionScreen Integration

```tsx
// In PremiumAddTransactionScreen.tsx
export interface PremiumAddTransactionScreenProps {
  onClose: () => void;
  onSave: (transaction: TransactionData) => void;
  onManageCategories?: () => void;  // ← Optional navigation callback
}

// Category Selection Step
{flowState === 'details' && transactionType !== 'transfer' && (
  <PremiumCategoryPickerEnhanced
    selectedCategory={selectedCategory}
    selectedSubcategory={selectedSubcategory}
    onSelectCategory={handleSelectCategory}
    onManageCategories={onManageCategories}  // ← Pass through
  />
)}
```

**Android Equivalent:**
```kotlin
// In CategorySelectionFragment
class CategorySelectionFragment : Fragment() {
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.categoryPicker.setOnManageCategoriesClickListener {
            // Navigate to Manage Categories
            findNavController().navigate(
                R.id.action_categorySelection_to_manageCategories
            )
        }
    }
}
```

---

## 5. PremiumCategoryPickerEnhanced Integration

```tsx
// In PremiumCategoryPickerEnhanced.tsx
export interface PremiumCategoryPickerEnhancedProps {
  selectedCategory?: TransactionCategory;
  selectedSubcategory?: string;
  onSelectCategory: (category: TransactionCategory, subcategory?: string) => void;
  onTitleChange?: (title: string) => void;
  onManageCategories?: () => void;  // ← Navigation callback
}

// In the render method - "Manage" button
{onManageCategories && (
  <button
    onClick={onManageCategories}
    className="..."
  >
    <Settings size={24} className="text-[var(--premium-emerald)]" />
    <p>Manage</p>
  </button>
)}
```

**Android Equivalent:**
```kotlin
// In CategoryPickerView (Custom View or Adapter)
class CategoryPickerAdapter(
    private val onManageClick: () -> Unit
) : RecyclerView.Adapter<ViewHolder>() {
    
    // Add "Manage" item at the end
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == itemCount - 1) {
            holder.bind(ManageItem())
            holder.itemView.setOnClickListener { onManageClick() }
        }
    }
}
```

---

## 6. ManageCategoriesScreen (STANDALONE)

```tsx
// In ManageCategoriesScreen.tsx
export interface ManageCategoriesScreenProps {
  /** Callback when back button is clicked */
  onBack: () => void;
}

export function ManageCategoriesScreen({ onBack }: ManageCategoriesScreenProps) {
  // COMPLETELY INDEPENDENT STATE
  const [categories, setCategories] = useState<CategoryItem[]>(...);
  const [showAddEditModal, setShowAddEditModal] = useState(false);
  const [editingCategory, setEditingCategory] = useState<CategoryData>();
  const [expandedCategories, setExpandedCategories] = useState<Set<string>>(new Set());

  // All logic is self-contained
  const handleMoveCategory = useCallback(...);
  const handleEditCategory = (category: CategoryItem) => {...};
  const handleDeleteCategory = (id: string) => {...};
  const handleSaveCategory = (categoryData: CategoryData) => {...};

  return (
    <DndProvider backend={HTML5Backend}>
      <div className="min-h-screen">
        {/* Top Toolbar with Back Button */}
        <button onClick={onBack}>
          <ArrowLeft />
        </button>
        
        {/* Content */}
        {/* ... */}
        
        {/* Add/Edit Modal */}
        <AddEditCategoryModal
          isOpen={showAddEditModal}
          editCategory={editingCategory}
          onClose={...}
          onSave={handleSaveCategory}
        />
      </div>
    </DndProvider>
  );
}
```

**Android Equivalent:**
```kotlin
// ManageCategoriesFragment.kt
class ManageCategoriesFragment : Fragment() {
    
    // ViewModel for state management
    private val viewModel: ManageCategoriesViewModel by viewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Setup toolbar with back navigation
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        
        // Setup RecyclerView with ItemTouchHelper for drag-and-drop
        val itemTouchHelper = ItemTouchHelper(DragDropCallback())
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        
        // Observe ViewModel
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            adapter.submitList(categories)
        }
        
        // Add button click
        binding.btnAddNew.setOnClickListener {
            showAddEditBottomSheet(null)
        }
    }
    
    private fun showAddEditBottomSheet(category: CategoryItem?) {
        AddEditCategoryBottomSheet.newInstance(category).show(
            childFragmentManager,
            AddEditCategoryBottomSheet.TAG
        )
    }
}
```

---

## 7. Complete Navigation Flow

```
User Journey:
1. User clicks FAB → Opens Add Transaction Screen
2. User selects "Expense" type → Proceeds to Category Selection
3. User sees Category Grid with "Manage" button
4. User clicks "Manage" button
   ├─ onManageCategories() callback fires
   ├─ PremiumApp: setShowAddTransaction(false)
   ├─ PremiumApp: setShowManageCategories(true)
   └─ ManageCategoriesScreen renders
5. User edits/adds categories
6. User clicks Back button
   ├─ onBack() callback fires
   ├─ PremiumApp: setShowManageCategories(false)
   ├─ PremiumApp: setShowAddTransaction(true)
   └─ Returns to Category Selection (Add Transaction Screen)
```

**State Timeline:**
```
State: { showAddTransaction: false, showManageCategories: false }
↓ (FAB click)
State: { showAddTransaction: true, showManageCategories: false }
↓ (Manage click)
State: { showAddTransaction: false, showManageCategories: true }
↓ (Back click)
State: { showAddTransaction: true, showManageCategories: false }
↓ (Close transaction)
State: { showAddTransaction: false, showManageCategories: false }
```

---

## 8. Props Chain Visualization

```
PremiumApp
  ↓ onManageCategories={() => { ... }}
PremiumAddTransactionScreen
  ↓ onManageCategories={onManageCategories}
PremiumCategoryPickerEnhanced
  ↓ onClick={onManageCategories}
[Manage Button]

---

PremiumApp
  ↓ onBack={() => { ... }}
ManageCategoriesScreen
  ↓ onClick={onBack}
[Back Button in Toolbar]
```

---

## 9. Decoupling Verification

### ✅ ManageCategoriesScreen is NOT:
- ❌ Nested inside PremiumAddTransactionScreen
- ❌ Nested inside PremiumDashboardScreen
- ❌ Part of the tab navigation
- ❌ Dependent on any parent screen's state

### ✅ ManageCategoriesScreen IS:
- ✅ A top-level component in PremiumApp
- ✅ Conditionally rendered based on its own state flag
- ✅ Completely independent and reusable
- ✅ Can be rendered from anywhere by setting `showManageCategories = true`
- ✅ Uses callback pattern for navigation (no hard-coded routes)

---

## 10. Android Implementation Example

### Navigation Graph (nav_graph.xml)
```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/nav_graph"
    app:startDestination="@id/dashboardFragment">

    <!-- Main Tabs -->
    <fragment
        android:id="@+id/dashboardFragment"
        android:name="com.mizan.ui.dashboard.DashboardFragment"
        android:label="Dashboard" />

    <!-- Add Transaction Flow -->
    <fragment
        android:id="@+id/addTransactionFragment"
        android:name="com.mizan.ui.transaction.AddTransactionFragment"
        android:label="Add Transaction">
        
        <action
            android:id="@+id/action_to_categorySelection"
            app:destination="@id/categorySelectionFragment" />
    </fragment>

    <!-- Category Selection (Part of Add Transaction Flow) -->
    <fragment
        android:id="@+id/categorySelectionFragment"
        android:name="com.mizan.ui.category.CategorySelectionFragment"
        android:label="Choose Category">
        
        <!-- Navigate to Manage Categories -->
        <action
            android:id="@+id/action_to_manageCategories"
            app:destination="@id/manageCategoriesFragment" />
    </fragment>

    <!-- STANDALONE: Manage Categories -->
    <fragment
        android:id="@+id/manageCategoriesFragment"
        android:name="com.mizan.ui.category.ManageCategoriesFragment"
        android:label="Manage Categories" />
</navigation>
```

### ViewModel Example
```kotlin
// ManageCategoriesViewModel.kt
class ManageCategoriesViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    private val _categories = MutableLiveData<List<CategoryItem>>()
    val categories: LiveData<List<CategoryItem>> = _categories
    
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    init {
        loadCategories()
    }
    
    fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                categoryRepository.getAllCategories()
                    .collect { categoriesList ->
                        _categories.value = categoriesList
                        _uiState.value = UiState.Success
                    }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message)
            }
        }
    }
    
    fun reorderCategories(fromPosition: Int, toPosition: Int) {
        _categories.value?.let { currentList ->
            val mutableList = currentList.toMutableList()
            val item = mutableList.removeAt(fromPosition)
            mutableList.add(toPosition, item)
            _categories.value = mutableList
            
            // Persist to database
            viewModelScope.launch {
                categoryRepository.updateCategoryOrder(mutableList)
            }
        }
    }
    
    fun deleteCategory(categoryId: String) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(categoryId)
        }
    }
}
```

---

## 11. Summary

**The ManageCategoriesScreen is:**
1. **Fully decoupled** - No dependencies on Dashboard or any other screen
2. **Standalone** - Can be rendered independently
3. **Callback-based** - Uses props for navigation (onBack)
4. **Reusable** - Can be integrated into any navigation system
5. **Android-ready** - Direct mapping to Fragment architecture

**Navigation is clean:**
- Simple state flags (`showManageCategories`)
- Callback props for navigation
- No hard-coded routes or dependencies
- Easy to translate to Android Navigation Component

**This architecture ensures:**
- Clear separation of concerns
- Easy maintenance and testing
- Straightforward Android porting
- Predictable navigation flow
