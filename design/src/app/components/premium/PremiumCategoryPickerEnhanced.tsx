/**
 * Premium Category Picker with Subcategory Support
 * Multi-level category selection with smooth animations and Material 3 design
 * 
 * @architecture MVI Pattern - Explicit UI State Management
 * @design Material 3 with 8dp grid system and glassmorphism
 */

import { useState } from 'react';
import { ChevronLeft, Check, Settings, Plus } from 'lucide-react';
import { CategoryIcon } from '../atoms/CategoryIcon';
import type { TransactionCategory } from '../../../types/domain';
import { CATEGORY_METADATA, CATEGORY_SUBCATEGORIES } from '../../../mocks/data';

/**
 * Selection state for category and subcategory
 */
interface CategorySelection {
  /** Main category ID */
  mainCategory?: TransactionCategory;
  /** Selected subcategory name */
  subcategory?: string;
}

export interface PremiumCategoryPickerEnhancedProps {
  /** Currently selected category */
  selectedCategory?: TransactionCategory;
  /** Currently selected subcategory */
  selectedSubcategory?: string;
  /** Callback when category selection is complete */
  onSelectCategory: (category: TransactionCategory, subcategory?: string) => void;
  /** Optional callback to update toolbar title */
  onTitleChange?: (title: string) => void;
  /** Optional callback when Manage Categories is clicked */
  onManageCategories?: () => void;
}

/**
 * View state for the picker
 */
type PickerView = 'main-categories' | 'subcategories';

export function PremiumCategoryPickerEnhanced({
  selectedCategory,
  selectedSubcategory,
  onSelectCategory,
  onTitleChange,
  onManageCategories,
}: PremiumCategoryPickerEnhancedProps) {
  const [currentView, setCurrentView] = useState<PickerView>('main-categories');
  const [selectedMainCategory, setSelectedMainCategory] = useState<TransactionCategory | undefined>(
    selectedCategory
  );
  const [selectedSub, setSelectedSub] = useState<string | undefined>(selectedSubcategory);

  /**
   * Handle main category selection
   * Transitions to subcategory view
   */
  const handleMainCategoryClick = (category: TransactionCategory) => {
    setSelectedMainCategory(category);
    setCurrentView('subcategories');
    
    // Update toolbar title
    const categoryMeta = CATEGORY_METADATA.find(c => c.id === category);
    if (onTitleChange && categoryMeta) {
      onTitleChange(`${categoryMeta.label}`);
    }
  };

  /**
   * Handle subcategory selection
   * Completes the selection and calls parent callback
   */
  const handleSubcategoryClick = (subcategory: string) => {
    setSelectedSub(subcategory);
    
    if (selectedMainCategory) {
      // Call parent callback with both main category and subcategory
      onSelectCategory(selectedMainCategory, subcategory);
    }
  };

  /**
   * Handle back button from subcategories
   * Returns to main category view
   */
  const handleBackToMainCategories = () => {
    setCurrentView('main-categories');
    setSelectedMainCategory(undefined);
    setSelectedSub(undefined);
    
    // Reset toolbar title
    if (onTitleChange) {
      onTitleChange('Choose Category');
    }
  };

  return (
    <div className="relative">
      {/* Main Categories View */}
      <div
        className={`
          transition-all duration-300
          ${currentView === 'main-categories'
            ? 'opacity-100 scale-100 pointer-events-auto'
            : 'opacity-0 scale-95 pointer-events-none absolute inset-0'
          }
        `}
        style={{
          transformOrigin: 'center',
        }}
      >
        <div className="grid grid-cols-3 gap-[var(--premium-space-md)]">
          {CATEGORY_METADATA.filter(cat => cat.id !== 'income').map((category) => {
            const isSelected = selectedCategory === category.id;

            return (
              <button
                key={category.id}
                onClick={() => handleMainCategoryClick(category.id as TransactionCategory)}
                className={`
                  relative
                  p-[var(--premium-space-md)]
                  rounded-[var(--premium-radius-lg)]
                  transition-all duration-200
                  ${isSelected
                    ? 'bg-[var(--premium-surface-3)] scale-95'
                    : 'bg-[var(--premium-surface-2)] hover:bg-[var(--premium-surface-3)] active:scale-95'
                  }
                `}
              >
                {/* Selection Indicator */}
                {isSelected && (
                  <div className="
                    absolute top-[8px] right-[8px]
                    w-[20px] h-[20px]
                    bg-gradient-to-r from-[#667eea] to-[#764ba2]
                    rounded-full
                    flex items-center justify-center
                  ">
                    <svg
                      width="12"
                      height="12"
                      viewBox="0 0 12 12"
                      fill="none"
                      xmlns="http://www.w3.org/2000/svg"
                    >
                      <path
                        d="M2 6L5 9L10 3"
                        stroke="white"
                        strokeWidth="2"
                        strokeLinecap="round"
                        strokeLinejoin="round"
                      />
                    </svg>
                  </div>
                )}

                {/* Icon */}
                <div
                  className="
                    w-[48px] h-[48px]
                    mx-auto
                    rounded-[var(--premium-radius-md)]
                    flex items-center justify-center
                    mb-[var(--premium-space-sm)]
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

                {/* Label */}
                <p className={`
                  body-sm text-center
                  ${isSelected
                    ? 'text-[var(--premium-text-primary)] font-medium'
                    : 'text-[var(--premium-text-secondary)]'
                  }
                `}>
                  {category.label}
                </p>
              </button>
            );
          })}

          {/* Manage Categories Button */}
          {onManageCategories && (
            <button
              onClick={onManageCategories}
              className="
                relative
                p-[var(--premium-space-md)]
                rounded-[var(--premium-radius-lg)]
                transition-all duration-200
                bg-[var(--premium-surface-2)]
                hover:bg-[var(--premium-surface-3)]
                active:scale-95
                border-2 border-dashed border-[var(--premium-text-tertiary)]/30
              "
            >
              {/* Icon */}
              <div
                className="
                  w-[48px] h-[48px]
                  mx-auto
                  rounded-[var(--premium-radius-md)]
                  flex items-center justify-center
                  mb-[var(--premium-space-sm)]
                  bg-[var(--premium-emerald)]/10
                "
              >
                <Settings
                  size={24}
                  className="text-[var(--premium-emerald)]"
                />
              </div>

              {/* Label */}
              <p className="body-sm text-center text-[var(--premium-text-secondary)]">
                Manage
              </p>
            </button>
          )}
        </div>
      </div>

      {/* Subcategories View */}
      <div
        className={`
          transition-all duration-300
          ${currentView === 'subcategories'
            ? 'opacity-100 scale-100 pointer-events-auto'
            : 'opacity-0 scale-95 pointer-events-none absolute inset-0'
          }
        `}
        style={{
          transformOrigin: 'center',
        }}
      >
        {selectedMainCategory && (
          <div className="space-y-[var(--premium-space-lg)]">
            {/* Back Button & Breadcrumb */}
            <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-md)]">
              <button
                onClick={handleBackToMainCategories}
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
                aria-label="Back to categories"
              >
                <ChevronLeft size={20} />
              </button>
              
              <div className="flex items-center gap-[8px]">
                <span className="body-sm text-[var(--premium-text-tertiary)]">
                  Categories
                </span>
                <ChevronLeft size={16} className="rotate-180 text-[var(--premium-text-tertiary)]" />
                <span className="body-md font-medium text-[var(--premium-text-primary)]">
                  {CATEGORY_METADATA.find(c => c.id === selectedMainCategory)?.label}
                </span>
              </div>
            </div>

            {/* Selected Main Category Display */}
            <div
              className="
                p-[var(--premium-space-lg)]
                rounded-[var(--premium-radius-xl)]
                bg-[var(--premium-surface-2)]
                flex items-center gap-[var(--premium-space-md)]
                border-2 border-transparent
              "
            >
              <div
                className="
                  w-[56px] h-[56px]
                  rounded-[var(--premium-radius-lg)]
                  flex items-center justify-center
                  flex-shrink-0
                "
                style={{
                  background: `linear-gradient(135deg, ${
                    CATEGORY_METADATA.find(c => c.id === selectedMainCategory)?.colorToken
                  }40, ${
                    CATEGORY_METADATA.find(c => c.id === selectedMainCategory)?.colorToken
                  }20)`,
                }}
              >
                <CategoryIcon
                  category={selectedMainCategory}
                  size={28}
                  color={CATEGORY_METADATA.find(c => c.id === selectedMainCategory)?.colorToken}
                />
              </div>
              <div className="flex-1">
                <p className="body-xs text-[var(--premium-text-tertiary)] mb-[4px]">
                  Main Category
                </p>
                <p className="body-lg font-medium text-[var(--premium-text-primary)]">
                  {CATEGORY_METADATA.find(c => c.id === selectedMainCategory)?.label}
                </p>
              </div>
            </div>

            {/* Subcategories List */}
            <div>
              <p className="body-sm text-[var(--premium-text-tertiary)] mb-[var(--premium-space-md)]">
                Select Subcategory
              </p>
              
              <div className="grid grid-cols-2 gap-[var(--premium-space-sm)]">
                {CATEGORY_SUBCATEGORIES[selectedMainCategory]?.map((subcategory) => {
                  const isSelected = selectedSub === subcategory;
                  
                  return (
                    <button
                      key={subcategory}
                      onClick={() => handleSubcategoryClick(subcategory)}
                      className={`
                        relative
                        p-[var(--premium-space-md)]
                        rounded-[var(--premium-radius-lg)]
                        transition-all duration-200
                        flex items-center gap-[8px]
                        text-left
                        ${isSelected
                          ? 'bg-[var(--premium-emerald)]/10 border-2 border-[var(--premium-emerald)] scale-[0.98]'
                          : 'bg-[var(--premium-surface-2)] border-2 border-transparent hover:bg-[var(--premium-surface-3)] active:scale-[0.98]'
                        }
                      `}
                    >
                      {/* Selection Checkmark */}
                      {isSelected && (
                        <div className="
                          w-[20px] h-[20px]
                          rounded-full
                          bg-[var(--premium-emerald)]
                          flex items-center justify-center
                          flex-shrink-0
                        ">
                          <Check size={14} strokeWidth={3} className="text-white" />
                        </div>
                      )}
                      
                      {/* Subcategory Label */}
                      <span className={`
                        body-md flex-1
                        ${isSelected
                          ? 'text-[var(--premium-emerald)] font-medium'
                          : 'text-[var(--premium-text-secondary)]'
                        }
                      `}>
                        {subcategory}
                      </span>
                    </button>
                  );
                })}
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}