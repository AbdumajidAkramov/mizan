/**
 * Add/Edit Category Modal
 * Premium modal for creating or editing categories with icon/color pickers
 * 
 * @architecture MVI Pattern - Explicit UI State Management
 * @design Material 3 with 8dp grid system and glassmorphism
 * 
 * ANDROID MAPPING:
 * - This should be a BottomSheetDialogFragment or DialogFragment in Android
 * - Full-screen on mobile, dialog on tablet
 * - Material Motion transitions for enter/exit animations
 * 
 * COMPONENT ARCHITECTURE:
 * - Completely reusable, can be used in any screen
 * - Callback-based: onClose, onSave
 * - Props-driven state for editing existing categories
 */

import { useState, useEffect } from 'react';
import { X, Check, ChevronDown } from 'lucide-react';
import * as Icons from 'lucide-react';
import type { TransactionCategory } from '../../../types/domain';
import { CATEGORY_METADATA } from '../../../mocks/data';

/**
 * Available icons for category selection
 */
const AVAILABLE_ICONS = [
  'Utensils', 'Car', 'ShoppingBag', 'Receipt', 'Film', 'Heart',
  'Plane', 'Smartphone', 'TrendingUp', 'Coffee', 'Home', 'Briefcase',
  'Book', 'Music', 'Camera', 'Dumbbell', 'Gift', 'Zap',
  'Droplet', 'Sun', 'Moon', 'Star', 'Award', 'Target',
  'Wallet', 'CreditCard', 'PiggyBank', 'TrendingDown', 'BarChart', 'DollarSign'
] as const;

/**
 * Brand-approved colors from theme
 */
const AVAILABLE_COLORS = [
  { name: 'Pink', value: '#ff6b9d', token: 'var(--premium-cat-food)' },
  { name: 'Blue', value: '#4facfe', token: 'var(--premium-cat-transport)' },
  { name: 'Orange', value: '#ffa34d', token: 'var(--premium-cat-shopping)' },
  { name: 'Cyan', value: '#00d2ff', token: 'var(--premium-cat-bills)' },
  { name: 'Purple', value: '#c471f5', token: 'var(--premium-cat-entertainment)' },
  { name: 'Red', value: '#ff6b6b', token: 'var(--premium-cat-health)' },
  { name: 'Indigo', value: '#667eea', token: 'var(--premium-cat-travel)' },
  { name: 'Teal', value: '#00f2a0', token: 'var(--premium-cat-tech)' },
  { name: 'Emerald', value: '#10b981', token: 'var(--premium-emerald)' },
  { name: 'Yellow', value: '#fee140', token: 'var(--premium-warning)' },
  { name: 'Rose', value: '#f5576c', token: 'var(--premium-secondary)' },
  { name: 'Sky', value: '#00f2fe', token: 'var(--premium-cat-income)' },
];

export interface CategoryData {
  id?: string;
  name: string;
  icon: string;
  color: string;
  colorToken: string;
  isSubcategory: boolean;
  parentCategory?: TransactionCategory;
}

export interface AddEditCategoryModalProps {
  /** Whether modal is open */
  isOpen: boolean;
  /** Existing category data for editing */
  editCategory?: CategoryData;
  /** Callback when modal is closed */
  onClose: () => void;
  /** Callback when category is saved */
  onSave: (category: CategoryData) => void;
}

export function AddEditCategoryModal({
  isOpen,
  editCategory,
  onClose,
  onSave,
}: AddEditCategoryModalProps) {
  const [name, setName] = useState('');
  const [selectedIcon, setSelectedIcon] = useState<string>('Wallet');
  const [selectedColor, setSelectedColor] = useState(AVAILABLE_COLORS[0]);
  const [isSubcategory, setIsSubcategory] = useState(false);
  const [parentCategory, setParentCategory] = useState<TransactionCategory | undefined>();
  const [showParentDropdown, setShowParentDropdown] = useState(false);

  /**
   * Initialize form with edit data
   */
  useEffect(() => {
    if (editCategory) {
      setName(editCategory.name);
      setSelectedIcon(editCategory.icon);
      const color = AVAILABLE_COLORS.find(c => c.value === editCategory.color);
      if (color) setSelectedColor(color);
      setIsSubcategory(editCategory.isSubcategory);
      setParentCategory(editCategory.parentCategory);
    } else {
      // Reset form for new category
      setName('');
      setSelectedIcon('Wallet');
      setSelectedColor(AVAILABLE_COLORS[0]);
      setIsSubcategory(false);
      setParentCategory(undefined);
    }
  }, [editCategory, isOpen]);

  /**
   * Handle save action
   */
  const handleSave = () => {
    if (!name.trim()) return;
    if (isSubcategory && !parentCategory) return;

    onSave({
      id: editCategory?.id,
      name: name.trim(),
      icon: selectedIcon,
      color: selectedColor.value,
      colorToken: selectedColor.token,
      isSubcategory,
      parentCategory: isSubcategory ? parentCategory : undefined,
    });

    onClose();
  };

  /**
   * Render icon component from string name
   */
  const renderIcon = (iconName: string, size: number = 24, color?: string) => {
    const IconComponent = (Icons as any)[iconName];
    if (!IconComponent) return null;
    return <IconComponent size={size} color={color} />;
  };

  if (!isOpen) return null;

  return (
    <>
      {/* Backdrop */}
      <div
        className="fixed inset-0 bg-black/50 backdrop-blur-sm z-50"
        onClick={onClose}
      />

      {/* Modal */}
      <div className="fixed inset-0 flex items-end sm:items-center justify-center z-50 p-[var(--premium-space-md)]">
        <div
          className="
            w-full max-w-[480px]
            bg-[var(--premium-bg-secondary)]
            rounded-t-[var(--premium-radius-2xl)] sm:rounded-[var(--premium-radius-2xl)]
            overflow-hidden
            shadow-[var(--premium-shadow-xl)]
            animate-in slide-in-from-bottom duration-300
          "
          onClick={(e) => e.stopPropagation()}
        >
          {/* Header */}
          <div className="
            sticky top-0
            bg-[var(--premium-bg-secondary)]
            border-b border-[var(--premium-glass-border)]
            p-[var(--premium-space-lg)]
            flex items-center justify-between
            backdrop-blur-lg
          ">
            <h2 className="heading-h3">
              {editCategory ? 'Edit Category' : 'Add Category'}
            </h2>
            <button
              onClick={onClose}
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
              aria-label="Close"
            >
              <X size={20} />
            </button>
          </div>

          {/* Content */}
          <div className="p-[var(--premium-space-lg)] space-y-[var(--premium-space-xl)]">
            {/* Category Name Input */}
            <div>
              <label className="block body-sm font-medium text-[var(--premium-text-secondary)] mb-[var(--premium-space-sm)]">
                Category Name
              </label>
              <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="e.g., Groceries, Rent, Salary"
                className="
                  w-full
                  p-[var(--premium-space-md)]
                  rounded-[var(--premium-radius-lg)]
                  bg-[var(--premium-surface-2)]
                  border-2 border-transparent
                  text-[var(--premium-text-primary)]
                  placeholder:text-[var(--premium-text-tertiary)]
                  focus:border-[var(--premium-emerald)]
                  focus:outline-none
                  transition-all duration-200
                "
              />
            </div>

            {/* Icon Picker */}
            <div>
              <label className="block body-sm font-medium text-[var(--premium-text-secondary)] mb-[var(--premium-space-md)]">
                Choose Icon
              </label>
              <div className="grid grid-cols-6 gap-[var(--premium-space-sm)]">
                {AVAILABLE_ICONS.map((iconName) => {
                  const isSelected = selectedIcon === iconName;
                  return (
                    <button
                      key={iconName}
                      onClick={() => setSelectedIcon(iconName)}
                      className={`
                        aspect-square
                        rounded-[var(--premium-radius-md)]
                        flex items-center justify-center
                        transition-all duration-200
                        ${isSelected
                          ? 'bg-[var(--premium-emerald)]/10 border-2 border-[var(--premium-emerald)] scale-95'
                          : 'bg-[var(--premium-surface-2)] border-2 border-transparent hover:bg-[var(--premium-surface-3)] active:scale-95'
                        }
                      `}
                    >
                      <div className={isSelected ? 'text-[var(--premium-emerald)]' : 'text-[var(--premium-text-secondary)]'}>
                        {renderIcon(iconName, 20)}
                      </div>
                    </button>
                  );
                })}
              </div>
            </div>

            {/* Color Picker */}
            <div>
              <label className="block body-sm font-medium text-[var(--premium-text-secondary)] mb-[var(--premium-space-md)]">
                Choose Color
              </label>
              <div className="grid grid-cols-6 gap-[var(--premium-space-sm)]">
                {AVAILABLE_COLORS.map((color) => {
                  const isSelected = selectedColor.value === color.value;
                  return (
                    <button
                      key={color.name}
                      onClick={() => setSelectedColor(color)}
                      className={`
                        aspect-square
                        rounded-[var(--premium-radius-md)]
                        flex items-center justify-center
                        transition-all duration-200
                        ${isSelected
                          ? 'border-2 border-[var(--premium-emerald)] scale-95'
                          : 'border-2 border-transparent hover:scale-110 active:scale-95'
                        }
                      `}
                      style={{
                        background: `linear-gradient(135deg, ${color.value}80, ${color.value}40)`,
                      }}
                    >
                      {isSelected && (
                        <Check size={16} strokeWidth={3} className="text-white drop-shadow-lg" />
                      )}
                    </button>
                  );
                })}
              </div>
            </div>

            {/* Category Type Toggle */}
            <div>
              <label className="block body-sm font-medium text-[var(--premium-text-secondary)] mb-[var(--premium-space-md)]">
                Category Type
              </label>
              <div className="grid grid-cols-2 gap-[var(--premium-space-sm)]">
                <button
                  onClick={() => setIsSubcategory(false)}
                  className={`
                    p-[var(--premium-space-md)]
                    rounded-[var(--premium-radius-lg)]
                    transition-all duration-200
                    ${!isSubcategory
                      ? 'bg-[var(--premium-emerald)]/10 border-2 border-[var(--premium-emerald)] text-[var(--premium-emerald)] font-medium'
                      : 'bg-[var(--premium-surface-2)] border-2 border-transparent text-[var(--premium-text-secondary)] hover:bg-[var(--premium-surface-3)]'
                    }
                  `}
                >
                  Main Category
                </button>
                <button
                  onClick={() => setIsSubcategory(true)}
                  className={`
                    p-[var(--premium-space-md)]
                    rounded-[var(--premium-radius-lg)]
                    transition-all duration-200
                    ${isSubcategory
                      ? 'bg-[var(--premium-emerald)]/10 border-2 border-[var(--premium-emerald)] text-[var(--premium-emerald)] font-medium'
                      : 'bg-[var(--premium-surface-2)] border-2 border-transparent text-[var(--premium-text-secondary)] hover:bg-[var(--premium-surface-3)]'
                    }
                  `}
                >
                  Subcategory
                </button>
              </div>
            </div>

            {/* Parent Category Selector (if subcategory) */}
            {isSubcategory && (
              <div>
                <label className="block body-sm font-medium text-[var(--premium-text-secondary)] mb-[var(--premium-space-sm)]">
                  Parent Category
                </label>
                <div className="relative">
                  <button
                    onClick={() => setShowParentDropdown(!showParentDropdown)}
                    className="
                      w-full
                      p-[var(--premium-space-md)]
                      rounded-[var(--premium-radius-lg)]
                      bg-[var(--premium-surface-2)]
                      border-2 border-transparent
                      hover:bg-[var(--premium-surface-3)]
                      flex items-center justify-between
                      text-left
                      transition-all duration-200
                    "
                  >
                    <span className={parentCategory ? 'text-[var(--premium-text-primary)]' : 'text-[var(--premium-text-tertiary)]'}>
                      {parentCategory
                        ? CATEGORY_METADATA.find(c => c.id === parentCategory)?.label
                        : 'Select parent category'
                      }
                    </span>
                    <ChevronDown size={20} className="text-[var(--premium-text-tertiary)]" />
                  </button>

                  {/* Dropdown */}
                  {showParentDropdown && (
                    <div className="
                      absolute top-full mt-[8px] left-0 right-0
                      bg-[var(--premium-bg-secondary)]
                      rounded-[var(--premium-radius-lg)]
                      shadow-[var(--premium-shadow-lg)]
                      border border-[var(--premium-glass-border)]
                      overflow-hidden
                      z-10
                    ">
                      {CATEGORY_METADATA.filter(cat => cat.id !== 'income').map((category) => (
                        <button
                          key={category.id}
                          onClick={() => {
                            setParentCategory(category.id as TransactionCategory);
                            setShowParentDropdown(false);
                          }}
                          className="
                            w-full
                            p-[var(--premium-space-md)]
                            hover:bg-[var(--premium-surface-2)]
                            flex items-center gap-[var(--premium-space-sm)]
                            text-left
                            transition-all duration-200
                          "
                        >
                          <div
                            className="w-[8px] h-[8px] rounded-full flex-shrink-0"
                            style={{ backgroundColor: category.colorToken }}
                          />
                          <span className="text-[var(--premium-text-primary)]">
                            {category.label}
                          </span>
                        </button>
                      ))}
                    </div>
                  )}
                </div>
              </div>
            )}

            {/* Preview */}
            <div>
              <label className="block body-sm font-medium text-[var(--premium-text-secondary)] mb-[var(--premium-space-md)]">
                Preview
              </label>
              <div className="
                p-[var(--premium-space-lg)]
                rounded-[var(--premium-radius-xl)]
                bg-[var(--premium-surface-2)]
                flex items-center gap-[var(--premium-space-md)]
              ">
                <div
                  className="
                    w-[56px] h-[56px]
                    rounded-[var(--premium-radius-lg)]
                    flex items-center justify-center
                    flex-shrink-0
                  "
                  style={{
                    background: `linear-gradient(135deg, ${selectedColor.value}40, ${selectedColor.value}20)`,
                  }}
                >
                  <div style={{ color: selectedColor.value }}>
                    {renderIcon(selectedIcon, 28)}
                  </div>
                </div>
                <div>
                  <p className="body-xs text-[var(--premium-text-tertiary)]">
                    {isSubcategory ? 'Subcategory' : 'Main Category'}
                  </p>
                  <p className="body-lg font-medium text-[var(--premium-text-primary)]">
                    {name || 'Category Name'}
                  </p>
                  {isSubcategory && parentCategory && (
                    <p className="body-xs text-[var(--premium-text-tertiary)]">
                      under {CATEGORY_METADATA.find(c => c.id === parentCategory)?.label}
                    </p>
                  )}
                </div>
              </div>
            </div>
          </div>

          {/* Footer Actions */}
          <div className="
            sticky bottom-0
            bg-[var(--premium-bg-secondary)]
            border-t border-[var(--premium-glass-border)]
            p-[var(--premium-space-lg)]
            flex gap-[var(--premium-space-md)]
          ">
            <button
              onClick={onClose}
              className="
                flex-1
                p-[var(--premium-space-md)]
                rounded-[var(--premium-radius-lg)]
                bg-[var(--premium-surface-2)]
                hover:bg-[var(--premium-surface-3)]
                text-[var(--premium-text-secondary)]
                font-medium
                transition-all duration-200
                active:scale-95
              "
            >
              Cancel
            </button>
            <button
              onClick={handleSave}
              disabled={!name.trim() || (isSubcategory && !parentCategory)}
              className="
                flex-1
                p-[var(--premium-space-md)]
                rounded-[var(--premium-radius-lg)]
                bg-gradient-to-r from-[var(--premium-emerald)] to-[var(--premium-emerald-dark)]
                hover:shadow-[var(--premium-glow-success)]
                text-white
                font-medium
                transition-all duration-200
                active:scale-95
                disabled:opacity-50
                disabled:cursor-not-allowed
              "
            >
              {editCategory ? 'Update' : 'Create'}
            </button>
          </div>
        </div>
      </div>
    </>
  );
}