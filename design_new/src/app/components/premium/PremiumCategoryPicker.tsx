/**
 * Premium Category Picker
 * Beautiful grid of category options
 */

import { CategoryIcon } from '../atoms/CategoryIcon';
import type { TransactionCategory } from '../../../types/domain';
import { CATEGORY_METADATA } from '../../../mocks/data';

export interface PremiumCategoryPickerProps {
  selectedCategory?: TransactionCategory;
  onSelectCategory: (category: TransactionCategory) => void;
}

export function PremiumCategoryPicker({
  selectedCategory,
  onSelectCategory,
}: PremiumCategoryPickerProps) {
  return (
    <div className="grid grid-cols-3 gap-[var(--premium-space-md)]">
      {CATEGORY_METADATA.filter(cat => cat.id !== 'income').map((category) => {
        const isSelected = selectedCategory === category.id;

        return (
          <button
            key={category.id}
            onClick={() => onSelectCategory(category.id as TransactionCategory)}
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
    </div>
  );
}
