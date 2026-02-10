/**
 * Manage Categories Screen
 * 
 * STANDALONE FULL-SCREEN COMPONENT
 * This is a completely independent screen with no dependencies on Dashboard or other screens.
 * 
 * @architecture MVI Pattern - Explicit UI State Management
 * @design Material 3 with 8dp grid system and glassmorphism
 * @navigation Callback-based navigation for easy integration with React Router or Android Navigation
 * 
 * ANDROID MAPPING:
 * - This should be a separate Fragment/Destination in Android Navigation Component
 * - Navigate here from CategorySelectionScreen via "Manage" button
 * - Back navigation returns to CategorySelectionScreen
 * 
 * USAGE:
 * ```tsx
 * <ManageCategoriesScreen
 *   onBack={() => navigateTo('category-selection')}
 * />
 * ```
 */

import { useState, useCallback } from 'react';
import { DndProvider, useDrag, useDrop } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import { ArrowLeft, Plus, GripVertical, Edit2, Trash2, ChevronRight } from 'lucide-react';
import { AddEditCategoryModal, type CategoryData } from '../components/premium/AddEditCategoryModal';
import { PremiumConfirmDialog } from '../components/premium/PremiumConfirmDialog';
import { CategoryIcon } from '../components/atoms/CategoryIcon';
import { CATEGORY_METADATA, CATEGORY_SUBCATEGORIES } from '../../mocks/data';
import type { TransactionCategory } from '../../types/domain';

/**
 * Category item for drag and drop
 */
interface CategoryItem {
  id: string;
  name: string;
  icon: string;
  color: string;
  colorToken: string;
  isSubcategory: boolean;
  parentCategory?: TransactionCategory;
  subcategories?: string[];
}

const DRAG_TYPE = 'category-item';

/**
 * Draggable Category Row Component
 */
interface DraggableCategoryRowProps {
  category: CategoryItem;
  index: number;
  onMove: (dragIndex: number, hoverIndex: number) => void;
  onEdit: (category: CategoryItem) => void;
  onDelete: (id: string) => void;
  onExpand: (id: string) => void;
  isExpanded: boolean;
}

function DraggableCategoryRow({
  category,
  index,
  onMove,
  onEdit,
  onDelete,
  onExpand,
  isExpanded,
}: DraggableCategoryRowProps) {
  const [{ isDragging }, drag, preview] = useDrag({
    type: DRAG_TYPE,
    item: { index },
    collect: (monitor) => ({
      isDragging: monitor.isDragging(),
    }),
  });

  const [{ isOver }, drop] = useDrop({
    accept: DRAG_TYPE,
    hover: (item: { index: number }) => {
      if (item.index !== index) {
        onMove(item.index, index);
        item.index = index;
      }
    },
    collect: (monitor) => ({
      isOver: monitor.isOver(),
    }),
  });

  const hasSubcategories = category.subcategories && category.subcategories.length > 0;

  return (
    <>
      <div
        ref={(node) => preview(drop(node))}
        className={`
          transition-all duration-200
          ${isDragging ? 'opacity-50' : 'opacity-100'}
          ${isOver ? 'bg-[var(--premium-surface-3)]' : ''}
        `}
      >
        <div
          className="
            p-[var(--premium-space-md)]
            rounded-[var(--premium-radius-lg)]
            bg-[var(--premium-surface-2)]
            hover:bg-[var(--premium-surface-3)]
            flex items-center gap-[var(--premium-space-md)]
            transition-all duration-200
            mb-[var(--premium-space-sm)]
          "
        >
          {/* Drag Handle */}
          <div
            ref={drag}
            className="cursor-move text-[var(--premium-text-tertiary)] hover:text-[var(--premium-text-secondary)]"
          >
            <GripVertical size={20} />
          </div>

          {/* Category Icon & Info */}
          <div
            className="
              w-[48px] h-[48px]
              rounded-[var(--premium-radius-md)]
              flex items-center justify-center
              flex-shrink-0
            "
            style={{
              background: `linear-gradient(135deg, ${category.colorToken}40, ${category.colorToken}20)`,
            }}
          >
            <CategoryIcon
              category={category.id as TransactionCategory}
              size={24}
              color={category.colorToken}
            />
          </div>

          <div className="flex-1 min-w-0">
            <p className="body-md font-medium text-[var(--premium-text-primary)]">
              {category.name}
            </p>
            {hasSubcategories && (
              <p className="body-xs text-[var(--premium-text-tertiary)]">
                {category.subcategories!.length} subcategories
              </p>
            )}
          </div>

          {/* Expand Button (if has subcategories) */}
          {hasSubcategories && (
            <button
              onClick={() => onExpand(category.id)}
              className="
                w-[32px] h-[32px]
                rounded-full
                bg-[var(--premium-surface-3)]
                hover:bg-[var(--premium-surface-4)]
                flex items-center justify-center
                transition-all duration-200
              "
            >
              <ChevronRight
                size={16}
                className={`
                  text-[var(--premium-text-secondary)]
                  transition-transform duration-200
                  ${isExpanded ? 'rotate-90' : ''}
                `}
              />
            </button>
          )}

          {/* Edit Button */}
          <button
            onClick={() => onEdit(category)}
            className="
              w-[32px] h-[32px]
              rounded-full
              bg-[var(--premium-surface-3)]
              hover:bg-[var(--premium-emerald)]/10
              hover:text-[var(--premium-emerald)]
              text-[var(--premium-text-secondary)]
              flex items-center justify-center
              transition-all duration-200
            "
          >
            <Edit2 size={16} />
          </button>

          {/* Delete Button */}
          <button
            onClick={() => onDelete(category.id)}
            className="
              w-[32px] h-[32px]
              rounded-full
              bg-[var(--premium-surface-3)]
              hover:bg-[var(--premium-error)]/10
              hover:text-[var(--premium-error)]
              text-[var(--premium-text-secondary)]
              flex items-center justify-center
              transition-all duration-200
            "
          >
            <Trash2 size={16} />
          </button>
        </div>
      </div>

      {/* Subcategories (expanded) */}
      {isExpanded && hasSubcategories && (
        <div className="ml-[var(--premium-space-xl)] space-y-[var(--premium-space-sm)] mb-[var(--premium-space-md)]">
          {category.subcategories!.map((subcat) => (
            <div
              key={subcat}
              className="
                p-[var(--premium-space-md)]
                rounded-[var(--premium-radius-lg)]
                bg-[var(--premium-surface-1)]
                flex items-center gap-[var(--premium-space-md)]
              "
            >
              <div
                className="w-[4px] h-[32px] rounded-full"
                style={{ backgroundColor: category.colorToken }}
              />
              <p className="body-md text-[var(--premium-text-secondary)] flex-1">
                {subcat}
              </p>
              <button
                className="
                  w-[28px] h-[28px]
                  rounded-full
                  bg-[var(--premium-surface-3)]
                  hover:bg-[var(--premium-emerald)]/10
                  hover:text-[var(--premium-emerald)]
                  text-[var(--premium-text-tertiary)]
                  flex items-center justify-center
                  transition-all duration-200
                "
              >
                <Edit2 size={14} />
              </button>
            </div>
          ))}
        </div>
      )}
    </>
  );
}

export interface ManageCategoriesScreenProps {
  /** Callback when back button is clicked */
  onBack: () => void;
}

export function ManageCategoriesScreen({ onBack }: ManageCategoriesScreenProps) {
  // Initialize categories from mock data
  const [categories, setCategories] = useState<CategoryItem[]>(() =>
    CATEGORY_METADATA.filter(cat => cat.id !== 'income').map(cat => ({
      id: cat.id,
      name: cat.label,
      icon: cat.iconName,
      color: cat.colorToken,
      colorToken: cat.colorToken,
      isSubcategory: false,
      subcategories: CATEGORY_SUBCATEGORIES[cat.id] || [],
    }))
  );

  const [showAddEditModal, setShowAddEditModal] = useState(false);
  const [editingCategory, setEditingCategory] = useState<CategoryData | undefined>();
  const [expandedCategories, setExpandedCategories] = useState<Set<string>>(new Set());
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
  const [categoryToDelete, setCategoryToDelete] = useState<string | undefined>();

  /**
   * Handle reordering categories
   */
  const handleMoveCategory = useCallback((dragIndex: number, hoverIndex: number) => {
    setCategories((prevCategories) => {
      const newCategories = [...prevCategories];
      const [draggedItem] = newCategories.splice(dragIndex, 1);
      newCategories.splice(hoverIndex, 0, draggedItem);
      return newCategories;
    });
  }, []);

  /**
   * Handle edit category
   */
  const handleEditCategory = (category: CategoryItem) => {
    setEditingCategory({
      id: category.id,
      name: category.name,
      icon: category.icon,
      color: category.color,
      colorToken: category.colorToken,
      isSubcategory: category.isSubcategory,
      parentCategory: category.parentCategory,
    });
    setShowAddEditModal(true);
  };

  /**
   * Handle delete category
   */
  const handleDeleteCategory = (id: string) => {
    setCategoryToDelete(id);
    setShowDeleteConfirm(true);
  };

  /**
   * Handle save category (add or update)
   */
  const handleSaveCategory = (categoryData: CategoryData) => {
    if (categoryData.id) {
      // Update existing
      setCategories((prev) =>
        prev.map((cat) =>
          cat.id === categoryData.id
            ? {
                ...cat,
                name: categoryData.name,
                icon: categoryData.icon,
                color: categoryData.color,
                colorToken: categoryData.colorToken,
                isSubcategory: categoryData.isSubcategory,
                parentCategory: categoryData.parentCategory,
              }
            : cat
        )
      );
    } else {
      // Add new
      const newCategory: CategoryItem = {
        id: `custom_${Date.now()}`,
        name: categoryData.name,
        icon: categoryData.icon,
        color: categoryData.color,
        colorToken: categoryData.colorToken,
        isSubcategory: categoryData.isSubcategory,
        parentCategory: categoryData.parentCategory,
        subcategories: [],
      };
      setCategories((prev) => [...prev, newCategory]);
    }

    setEditingCategory(undefined);
  };

  /**
   * Handle expand/collapse category
   */
  const handleToggleExpand = (id: string) => {
    setExpandedCategories((prev) => {
      const newSet = new Set(prev);
      if (newSet.has(id)) {
        newSet.delete(id);
      } else {
        newSet.add(id);
      }
      return newSet;
    });
  };

  /**
   * Handle add new category
   */
  const handleAddNew = () => {
    setEditingCategory(undefined);
    setShowAddEditModal(true);
  };

  /**
   * Handle confirm delete
   */
  const handleConfirmDelete = () => {
    if (categoryToDelete) {
      setCategories((prev) => prev.filter((cat) => cat.id !== categoryToDelete));
    }
    setShowDeleteConfirm(false);
    setCategoryToDelete(undefined);
  };

  /**
   * Handle cancel delete
   */
  const handleCancelDelete = () => {
    setShowDeleteConfirm(false);
    setCategoryToDelete(undefined);
  };

  return (
    <DndProvider backend={HTML5Backend}>
      <div className="
        min-h-screen
        bg-[var(--premium-bg-primary)]
        flex flex-col
      ">
        {/* Top Toolbar */}
        <div className="
          sticky top-0 z-10
          bg-[var(--premium-glass-bg)]
          backdrop-blur-[var(--premium-glass-blur)]
          border-b border-[var(--premium-glass-border)]
          px-[var(--premium-space-lg)]
          py-[var(--premium-space-md)]
        ">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-[var(--premium-space-md)]">
              <button
                onClick={onBack}
                className="
                  w-[40px] h-[40px]
                  rounded-full
                  bg-[var(--premium-surface-2)]
                  hover:bg-[var(--premium-surface-3)]
                  flex items-center justify-center
                  text-[var(--premium-text-secondary)]
                  transition-all duration-200
                  active:scale-95
                "
                aria-label="Back"
              >
                <ArrowLeft size={20} />
              </button>
              <h1 className="heading-h2">Manage Categories</h1>
            </div>

            <button
              onClick={handleAddNew}
              className="
                flex items-center gap-[8px]
                px-[var(--premium-space-lg)]
                py-[var(--premium-space-md)]
                rounded-[var(--premium-radius-lg)]
                bg-gradient-to-r from-[var(--premium-emerald)] to-[var(--premium-emerald-dark)]
                hover:shadow-[var(--premium-glow-success)]
                text-white
                font-medium
                transition-all duration-200
                active:scale-95
              "
            >
              <Plus size={20} />
              <span>Add New</span>
            </button>
          </div>
        </div>

        {/* Content */}
        <div className="flex-1 p-[var(--premium-space-lg)]">
          <div className="max-w-[768px] mx-auto">
            {/* Info Card */}
            <div className="
              p-[var(--premium-space-lg)]
              rounded-[var(--premium-radius-xl)]
              bg-[var(--premium-surface-2)]
              mb-[var(--premium-space-xl)]
            ">
              <p className="body-sm text-[var(--premium-text-secondary)]">
                Drag and drop to reorder categories. Click edit to modify or delete to remove.
                Expand categories to view their subcategories.
              </p>
            </div>

            {/* Categories List */}
            <div className="space-y-[var(--premium-space-sm)]">
              {categories.map((category, index) => (
                <DraggableCategoryRow
                  key={category.id}
                  category={category}
                  index={index}
                  onMove={handleMoveCategory}
                  onEdit={handleEditCategory}
                  onDelete={handleDeleteCategory}
                  onExpand={handleToggleExpand}
                  isExpanded={expandedCategories.has(category.id)}
                />
              ))}
            </div>

            {/* Empty State */}
            {categories.length === 0 && (
              <div className="
                py-[var(--premium-space-2xl)]
                text-center
              ">
                <div className="
                  w-[80px] h-[80px]
                  rounded-full
                  bg-[var(--premium-surface-2)]
                  flex items-center justify-center
                  mx-auto
                  mb-[var(--premium-space-lg)]
                ">
                  <Plus size={32} className="text-[var(--premium-text-tertiary)]" />
                </div>
                <p className="body-md text-[var(--premium-text-secondary)] mb-[var(--premium-space-md)]">
                  No categories yet
                </p>
                <button
                  onClick={handleAddNew}
                  className="
                    px-[var(--premium-space-xl)]
                    py-[var(--premium-space-md)]
                    rounded-[var(--premium-radius-lg)]
                    bg-gradient-to-r from-[var(--premium-emerald)] to-[var(--premium-emerald-dark)]
                    text-white
                    font-medium
                    transition-all duration-200
                    active:scale-95
                  "
                >
                  Create Your First Category
                </button>
              </div>
            )}
          </div>
        </div>

        {/* Add/Edit Modal */}
        <AddEditCategoryModal
          isOpen={showAddEditModal}
          editCategory={editingCategory}
          onClose={() => {
            setShowAddEditModal(false);
            setEditingCategory(undefined);
          }}
          onSave={handleSaveCategory}
        />

        {/* Delete Confirm Dialog */}
        <PremiumConfirmDialog
          isOpen={showDeleteConfirm}
          title="Delete Category"
          message="Are you sure you want to delete this category? This action cannot be undone."
          onConfirm={handleConfirmDelete}
          onCancel={handleCancelDelete}
        />
      </div>
    </DndProvider>
  );
}