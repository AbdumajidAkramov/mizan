/**
 * M3 BudgetProgressCard Molecule Component
 * Material Design 3 card with linear progress indicator
 * Maps to Jetpack Compose Card with LinearProgressIndicator
 */

import { M3Surface } from '../atoms/M3Surface';

export interface M3BudgetProgressCardProps {
  /** Amount spent this month */
  spentAmount: number;
  /** Total budget limit */
  budgetLimit: number;
  /** Percentage of budget used */
  percentageUsed: number;
}

/**
 * Material 3 Budget Progress Card
 * Shows budget consumption with M3 progress indicator
 * 
 * @example
 * <M3BudgetProgressCard
 *   spentAmount={1750}
 *   budgetLimit={2500}
 *   percentageUsed={70}
 * />
 */
export function M3BudgetProgressCard({
  spentAmount,
  budgetLimit,
  percentageUsed,
}: M3BudgetProgressCardProps) {
  const isOverBudget = percentageUsed > 100;
  const isWarning = percentageUsed > 80 && percentageUsed <= 100;
  
  const progressColor = isOverBudget 
    ? 'var(--md-sys-color-error)' 
    : isWarning
    ? 'var(--md-sys-color-tertiary)' 
    : 'var(--md-sys-color-primary)';

  return (
    <M3Surface
      elevation={1}
      shape="medium"
      className="p-[var(--md-sys-spacing-lg)]"
    >
      {/* Header */}
      <div className="flex items-center justify-between mb-[var(--md-sys-spacing-md)]">
        <div>
          <h3 className="title-large text-[var(--md-sys-color-on-surface)]">
            Monthly Budget
          </h3>
          <p className="body-medium text-[var(--md-sys-color-on-surface-variant)] mt-[var(--md-sys-spacing-xs)]">
            ${spentAmount.toLocaleString()} of ${budgetLimit.toLocaleString()}
          </p>
        </div>
        <div className="text-right">
          <p 
            className="headline-medium"
            style={{ color: progressColor }}
          >
            {Math.round(percentageUsed)}%
          </p>
          <p className="body-small text-[var(--md-sys-color-on-surface-variant)]">
            spent
          </p>
        </div>
      </div>

      {/* M3 Linear Progress Indicator (12dp height) */}
      <div className="w-full bg-[var(--md-sys-color-surface-variant)] rounded-full h-[12px]">
        <div
          className="h-[12px] rounded-full transition-all duration-300"
          style={{
            width: `${Math.min(percentageUsed, 100)}%`,
            backgroundColor: progressColor,
          }}
        />
      </div>

      {/* Warning/Error Message */}
      {isOverBudget && (
        <p className="body-small text-[var(--md-sys-color-error)] mt-[var(--md-sys-spacing-sm)]">
          ⚠️ You've exceeded your budget by ${(spentAmount - budgetLimit).toFixed(2)}
        </p>
      )}
      {isWarning && !isOverBudget && (
        <p className="body-small text-[var(--md-sys-color-tertiary)] mt-[var(--md-sys-spacing-sm)]">
          ⚠️ You're approaching your budget limit
        </p>
      )}
    </M3Surface>
  );
}
