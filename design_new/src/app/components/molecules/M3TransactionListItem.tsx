/**
 * M3 TransactionListItem Molecule Component
 * Material Design 3 three-line list item
 * Maps 1:1 to Jetpack Compose ListItem(threeLine = true)
 */

import { CategoryIcon } from '../atoms/CategoryIcon';
import { AmountText } from '../atoms/AmountText';
import { M3Surface } from '../atoms/M3Surface';
import { ArrowUpRight, ArrowDownLeft } from 'lucide-react';
import type { Transaction } from '../../../types/domain';
import { getCategoryMetadata } from '../../../mocks/data';

export interface M3TransactionListItemProps {
  /** Transaction data */
  transaction: Transaction;
  /** Click handler for item */
  onClick?: (transaction: Transaction) => void;
}

/**
 * Material 3 Transaction List Item
 * Three-line layout with M3 surface tints
 * 
 * @example
 * <M3TransactionListItem
 *   transaction={transaction}
 *   onClick={handleClick}
 * />
 */
export function M3TransactionListItem({ transaction, onClick }: M3TransactionListItemProps) {
  const categoryMeta = getCategoryMetadata(transaction.category);
  const categoryColor = categoryMeta?.colorToken || 'var(--md-sys-color-outline)';
  
  const date = new Date(transaction.timestamp);
  const formattedDate = date.toLocaleDateString('en-US', { 
    month: 'short', 
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });

  return (
    <M3Surface
      elevation={1}
      shape="medium"
      onClick={() => onClick?.(transaction)}
      className="
        p-[var(--md-sys-spacing-md)]
        flex items-center gap-[var(--md-sys-spacing-md)]
      "
    >
      {/* Leading: Category Icon Container */}
      <div
        className="
          w-[48px] h-[48px]
          rounded-full
          flex items-center justify-center
          flex-shrink-0
        "
        style={{ 
          backgroundColor: `${categoryColor}20`,
          color: categoryColor,
        }}
      >
        <CategoryIcon category={transaction.category} size={24} />
      </div>

      {/* Center: Transaction Details (Three-line) */}
      <div className="flex-1 min-w-0">
        <h4 className="title-medium text-[var(--md-sys-color-on-surface)] truncate">
          {transaction.title}
        </h4>
        <p className="body-small text-[var(--md-sys-color-on-surface-variant)] mt-[var(--md-sys-spacing-xs)]">
          {formattedDate}
        </p>
      </div>

      {/* Trailing: Amount and Type Indicator */}
      <div className="flex flex-col items-end gap-[var(--md-sys-spacing-xs)]">
        <AmountText 
          amount={transaction.amount}
          type={transaction.type}
          showSign
          size="base"
        />
        <div className="flex items-center gap-[var(--md-sys-spacing-xs)]">
          {transaction.type === 'income' ? (
            <ArrowDownLeft size={12} style={{ color: 'var(--md-category-income-primary)' }} />
          ) : (
            <ArrowUpRight size={12} style={{ color: 'var(--md-sys-color-error)' }} />
          )}
          <span className="label-small text-[var(--md-sys-color-on-surface-variant)]">
            {categoryMeta?.label || transaction.category}
          </span>
        </div>
      </div>
    </M3Surface>
  );
}
