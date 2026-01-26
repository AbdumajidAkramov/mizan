/**
 * CategoryCard Molecule Component
 * Displays category spending summary
 */

import { CategoryIcon } from '../atoms/CategoryIcon';
import { AmountText } from '../atoms/AmountText';
import type { CategorySpending } from '../../../types/domain';

export interface CategoryCardProps {
  /** Category spending data */
  categorySpending: CategorySpending;
  /** Optional click handler */
  onClick?: (category: CategorySpending) => void;
}

/**
 * Material 3 Category Card
 * Shows spending by category with progress indicator
 */
export function CategoryCard({ categorySpending, onClick }: CategoryCardProps) {
  return (
    <button
      onClick={() => onClick?.(categorySpending)}
      className="
        w-full
        bg-[var(--color-surface)]
        rounded-2xl
        p-[var(--spacing-md)]
        shadow-[var(--elevation-1)]
        hover:shadow-[var(--elevation-2)]
        transition-all duration-200
        active:scale-98
        border border-[var(--color-outline-variant)]
      "
    >
      {/* Header Row */}
      <div className="flex items-center justify-between mb-[var(--spacing-md)]">
        <div className="flex items-center gap-[var(--spacing-sm)]">
          <div
            className="
              w-[40px] h-[40px]
              rounded-full
              flex items-center justify-center
            "
            style={{
              backgroundColor: `${categorySpending.colorToken}30`,
              color: categorySpending.colorToken,
            }}
          >
            <CategoryIcon category={categorySpending.category} size={20} />
          </div>
          <div className="text-left">
            <p className="text-sm text-[var(--color-on-surface)]">
              {categorySpending.categoryLabel}
            </p>
            <p className="text-xs text-[var(--color-on-surface-variant)] mt-[var(--spacing-xs)]">
              {categorySpending.transactionCount} transactions
            </p>
          </div>
        </div>
        <AmountText amount={categorySpending.totalAmount} size="base" />
      </div>

      {/* Progress Bar */}
      <div className="w-full bg-[var(--color-surface-variant)] rounded-full h-[8px]">
        <div
          className="h-[8px] rounded-full transition-all duration-300"
          style={{
            width: `${categorySpending.percentage}%`,
            backgroundColor: categorySpending.colorToken,
          }}
        />
      </div>
      <p className="text-xs text-[var(--color-on-surface-variant)] mt-[var(--spacing-xs)] text-right">
        {categorySpending.percentage}% of total
      </p>
    </button>
  );
}
