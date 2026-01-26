/**
 * BudgetProgressCard Molecule Component
 * Displays monthly budget with progress indicator
 */

export interface BudgetProgressCardProps {
  /** Amount spent this month */
  spentAmount: number;
  /** Total budget limit */
  budgetLimit: number;
  /** Percentage of budget used */
  percentageUsed: number;
}

/**
 * Material 3 Budget Progress Card
 * Shows budget consumption with linear progress
 */
export function BudgetProgressCard({
  spentAmount,
  budgetLimit,
  percentageUsed,
}: BudgetProgressCardProps) {
  const isOverBudget = percentageUsed > 100;
  const progressColor = isOverBudget 
    ? 'var(--color-error)' 
    : percentageUsed > 80 
    ? 'var(--color-tertiary)' 
    : 'var(--color-primary)';

  return (
    <div
      className="
        bg-[var(--color-surface)]
        rounded-2xl
        p-[var(--spacing-lg)]
        shadow-[var(--elevation-1)]
        border border-[var(--color-outline-variant)]
      "
    >
      {/* Header */}
      <div className="flex items-center justify-between mb-[var(--spacing-md)]">
        <div>
          <h3 className="text-[var(--color-on-surface)]">Monthly Budget</h3>
          <p className="text-sm text-[var(--color-on-surface-variant)] mt-[var(--spacing-xs)]">
            ${spentAmount.toLocaleString()} of ${budgetLimit.toLocaleString()}
          </p>
        </div>
        <div className="text-right">
          <p 
            className="text-2xl"
            style={{ color: progressColor }}
          >
            {Math.round(percentageUsed)}%
          </p>
          <p className="text-sm text-[var(--color-on-surface-variant)]">spent</p>
        </div>
      </div>

      {/* Progress Bar */}
      <div className="w-full bg-[var(--color-surface-variant)] rounded-full h-[12px]">
        <div
          className="h-[12px] rounded-full transition-all duration-300"
          style={{
            width: `${Math.min(percentageUsed, 100)}%`,
            backgroundColor: progressColor,
          }}
        />
      </div>

      {/* Warning if over budget */}
      {isOverBudget && (
        <p className="text-sm text-[var(--color-error)] mt-[var(--spacing-sm)]">
          ⚠️ You've exceeded your budget by ${(spentAmount - budgetLimit).toFixed(2)}
        </p>
      )}
    </div>
  );
}
