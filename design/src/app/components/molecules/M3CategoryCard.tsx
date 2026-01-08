/**
 * M3 CategoryCard Molecule Component
 * Material Design 3 card showing category spending summary
 * Maps to Jetpack Compose Card with LinearProgressIndicator
 */

import { CategoryIcon } from '../atoms/CategoryIcon';
import { AmountText } from '../atoms/AmountText';
import { M3Surface } from '../atoms/M3Surface';
import type { CategorySpending } from '../../../types/domain';

export interface M3CategoryCardProps {
  /** Category spending data */
  categorySpending: CategorySpending;
  /** Optional click handler */
  onClick?: (category: CategorySpending) => void;
}

/**
 * Material 3 Category Card
 * Shows spending by category with M3 linear progress indicator
 * 
 * @example
 * <M3CategoryCard
 *   categorySpending={categoryData}
 *   onClick={handleCategoryClick}
 * />
 */
export function M3CategoryCard({ categorySpending, onClick }: M3CategoryCardProps) {
  return (
    <M3Surface
      elevation={1}
      shape="medium"
      onClick={() => onClick?.(categorySpending)}
      className="p-[var(--md-sys-spacing-md)]"
    >
      {/* Header Row */}
      <div className="flex items-center justify-between mb-[var(--md-sys-spacing-md)]">
        <div className="flex items-center gap-[var(--md-sys-spacing-sm)]">
          {/* Category Icon Container */}
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
          
          {/* Category Info */}
          <div>
            <p className="title-small text-[var(--md-sys-color-on-surface)]">
              {categorySpending.categoryLabel}
            </p>
            <p className="body-small text-[var(--md-sys-color-on-surface-variant)] mt-[var(--md-sys-spacing-xs)]">
              {categorySpending.transactionCount} transactions
            </p>
          </div>
        </div>
        
        {/* Amount */}
        <AmountText amount={categorySpending.totalAmount} size="base" />
      </div>

      {/* M3 Linear Progress Indicator */}
      <div className="w-full bg-[var(--md-sys-color-surface-variant)] rounded-full h-[8px]">
        <div
          className="h-[8px] rounded-full transition-all duration-300"
          style={{
            width: `${categorySpending.percentage}%`,
            backgroundColor: categorySpending.colorToken,
          }}
        />
      </div>
      
      {/* Progress Label */}
      <p className="label-small text-[var(--md-sys-color-on-surface-variant)] mt-[var(--md-sys-spacing-xs)] text-right">
        {categorySpending.percentage}% of total
      </p>
    </M3Surface>
  );
}
