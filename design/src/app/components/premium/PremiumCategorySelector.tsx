/**
 * Premium Category Selector - Overlay Modal
 * Elegant vertical card list for category selection
 * Matches Transaction Type Selector style
 * 
 * @architecture Material 3 with glassmorphism
 * @design Emerald Green (#10B981) accents, text-only cards
 */

import { Check } from 'lucide-react';
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
          </div>
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
