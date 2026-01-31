/**
 * Premium Category Selector - Overlay Modal
 * Elegant vertical card list for category selection
 * Matches Transaction Type Selector style
 * 
 * @architecture Material 3 with glassmorphism
 * @design Emerald Green (#10B981) accents, text-only cards
 */

import { Check, Plus, FolderPlus } from 'lucide-react';
import type { TransactionCategory } from '../../types/domain';

export interface CategoryOption {
  id: TransactionCategory;
  label: string;
  description: string;
}

export interface PremiumCategorySelectorProps {
  /** Whether the overlay is open */
  isOpen: boolean;
  /** Current selected category */
  selectedCategory?: TransactionCategory;
  /** Available categories to display */
  categories: CategoryOption[];
  /** Callback when category is selected */
  onSelectCategory: (category: TransactionCategory) => void;
  /** Callback when overlay is closed */
  onClose: () => void;
  /** Callback when user wants to navigate to Manage Categories screen */
  onNavigateToManageCategories?: () => void;
  /** Optional custom title */
  title?: string;
  /** Optional custom subtitle */
  subtitle?: string;
}

export function PremiumCategorySelector({
  isOpen,
  selectedCategory,
  categories,
  onSelectCategory,
  onClose,
  onNavigateToManageCategories,
  title = "Select Category",
  subtitle = "Choose a category for this transaction",
}: PremiumCategorySelectorProps) {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-[110] flex items-center justify-center animate-[fadeIn_0.2s_ease-out]">
      {/* Backdrop */}
      <div
        className="absolute inset-0 bg-black/40 backdrop-blur-sm"
        onClick={onClose}
      />

      {/* Overlay Modal */}
      <div
        className="
          relative
          w-full
          max-w-[400px]
          max-h-[80vh]
          mx-[var(--premium-space-lg)]
          bg-[var(--premium-surface)]
          rounded-[var(--premium-radius-2xl)]
          shadow-[var(--premium-shadow-2xl)]
          overflow-hidden
          animate-[scaleIn_0.2s_ease-out]
          flex flex-col
        "
      >
        {/* Header - Fixed */}
        <div className="px-[var(--premium-space-lg)] pt-[var(--premium-space-lg)] pb-[var(--premium-space-md)] flex-shrink-0">
          <h3 className="heading-md text-[var(--premium-text-primary)]">
            {title}
          </h3>
          <p className="body-sm text-[var(--premium-text-tertiary)] mt-[4px]">
            {subtitle}
          </p>
        </div>

        {/* Category List - Scrollable */}
        <div className="flex-1 overflow-y-auto px-[var(--premium-space-lg)] pb-[var(--premium-space-lg)]">
          {categories.length === 0 ? (
            /* Empty State */
            <div className="flex flex-col items-center justify-center py-[var(--premium-space-2xl)] text-center">
              {/* Illustration Icon */}
              <div
                className="
                  w-[80px] h-[80px]
                  rounded-full
                  bg-[var(--premium-emerald)]/10
                  border-2 border-[var(--premium-emerald)]/20
                  flex items-center justify-center
                  mb-[var(--premium-space-lg)]
                  animate-[pulse_2s_ease-in-out_infinite]
                "
              >
                <FolderPlus size={40} className="text-[var(--premium-emerald)]" />
              </div>

              {/* Empty State Message */}
              <h4 className="heading-sm text-[var(--premium-text-primary)] mb-[8px]">
                No categories yet
              </h4>
              <p className="body-sm text-[var(--premium-text-tertiary)] mb-[var(--premium-space-lg)] max-w-[280px]">
                Let's create your first category to organize your transactions
              </p>

              {/* Create New Category Button */}
              {onNavigateToManageCategories && (
                <button
                  onClick={() => {
                    onNavigateToManageCategories();
                    onClose();
                  }}
                  className="
                    px-[var(--premium-space-xl)]
                    py-[var(--premium-space-md)]
                    rounded-[var(--premium-radius-full)]
                    bg-[var(--premium-emerald)]
                    hover:bg-[var(--premium-emerald-dark)]
                    text-white
                    font-medium
                    transition-all duration-200
                    active:scale-95
                    shadow-[0_4px_16px_rgba(16,185,129,0.3)]
                    flex items-center gap-[8px]
                  "
                >
                  <Plus size={20} strokeWidth={2.5} />
                  <span className="body-md">Create New Category</span>
                </button>
              )}
            </div>
          ) : (
            /* Category List with Inline "Add New" Item */
            <div className="space-y-[8px]">
              {categories.map((category) => {
                const isSelected = selectedCategory === category.id;

                return (
                  <button
                    key={category.id}
                    onClick={() => {
                      onSelectCategory(category.id);
                      onClose();
                    }}
                    className={`
                      w-full
                      p-[var(--premium-space-md)]
                      rounded-[var(--premium-radius-xl)]
                      flex items-center gap-[var(--premium-space-md)]
                      transition-all duration-200
                      ${isSelected
                        ? 'bg-[var(--premium-emerald)]/10 border-2 border-[var(--premium-emerald)] shadow-[0_0_0_4px_rgba(16,185,129,0.1)]'
                        : 'bg-[var(--premium-surface-2)] border-2 border-transparent hover:bg-[var(--premium-surface-3)]'
                      }
                      active:scale-[0.98]
                    `}
                  >
                    {/* Text Content */}
                    <div className="flex-1 text-left">
                      <p
                        className={`
                          body-md font-medium mb-[2px]
                          ${isSelected ? 'text-[var(--premium-emerald)]' : 'text-[var(--premium-text-primary)]'}
                        `}
                      >
                        {category.label}
                      </p>
                      <p className="body-sm text-[var(--premium-text-tertiary)]">
                        {category.description}
                      </p>
                    </div>

                    {/* Check Icon - Only show when selected */}
                    {isSelected && (
                      <div
                        className="
                          w-[24px] h-[24px]
                          rounded-full
                          bg-[var(--premium-emerald)]
                          flex items-center justify-center
                          flex-shrink-0
                        "
                      >
                        <Check size={14} className="text-white" strokeWidth={3} />
                      </div>
                    )}
                  </button>
                );
              })}

              {/* Inline "Add New / Manage Categories" Item - Always at the end */}
              {onNavigateToManageCategories && (
                <button
                  onClick={() => {
                    onNavigateToManageCategories();
                    onClose();
                  }}
                  className="
                    w-full
                    p-[var(--premium-space-md)]
                    rounded-[var(--premium-radius-xl)]
                    flex items-center gap-[var(--premium-space-md)]
                    transition-all duration-200
                    bg-[var(--premium-surface-2)]/40
                    border-2 border-dashed border-[var(--premium-emerald)]/30
                    hover:bg-[var(--premium-emerald)]/5
                    hover:border-[var(--premium-emerald)]
                    active:scale-[0.98]
                  "
                >
                  {/* Plus Icon */}
                  <div
                    className="
                      w-[32px] h-[32px]
                      rounded-full
                      bg-[var(--premium-emerald)]/10
                      border border-[var(--premium-emerald)]/30
                      flex items-center justify-center
                      flex-shrink-0
                    "
                  >
                    <Plus size={18} className="text-[var(--premium-emerald)]" strokeWidth={2.5} />
                  </div>

                  {/* Text Content */}
                  <div className="flex-1 text-left">
                    <p className="body-md font-medium text-[var(--premium-emerald)] mb-[2px]">
                      Add New Category
                    </p>
                    <p className="body-sm text-[var(--premium-text-tertiary)]">
                      Manage & customize your categories
                    </p>
                  </div>
                </button>
              )}
            </div>
          )}
        </div>
      </div>

      <style>{`
        @keyframes scaleIn {
          from {
            opacity: 0;
            transform: scale(0.9);
          }
          to {
            opacity: 1;
            transform: scale(1);
          }
        }
      `}</style>
    </div>
  );
}