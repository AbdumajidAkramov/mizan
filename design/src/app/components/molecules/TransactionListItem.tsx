/**
 * TransactionListItem Molecule Component
 * Single transaction row in list view
 */

import { CategoryIcon } from '../atoms/CategoryIcon';
import { AmountText } from '../atoms/AmountText';
import { ArrowUpRight, ArrowDownLeft } from 'lucide-react';
import type { Transaction } from '../../../types/domain';
import { getCategoryMetadata } from '../../../mocks/data';

export interface TransactionListItemProps {
  /** Transaction data */
  transaction: Transaction;
  /** Click handler for item */
  onClick?: (transaction: Transaction) => void;
}

/**
 * Material 3 Transaction List Item
 * Maps to Android ListItem with three-line layout
 */
export function TransactionListItem({ transaction, onClick }: TransactionListItemProps) {
  const categoryMeta = getCategoryMetadata(transaction.category);
  const categoryColor = categoryMeta?.colorToken || 'var(--color-outline)';
  
  const date = new Date(transaction.timestamp);
  const formattedDate = date.toLocaleDateString('en-US', { 
    month: 'short', 
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });

  return (
    <button
      onClick={() => onClick?.(transaction)}
      className="
        w-full
        bg-[var(--color-surface)]
        rounded-2xl
        p-[var(--spacing-md)]
        shadow-[var(--elevation-1)]
        flex items-center gap-[var(--spacing-md)]
        transition-all duration-200
        hover:shadow-[var(--elevation-2)]
        active:scale-98
        border border-[var(--color-outline-variant)]
      "
    >
      {/* Category Icon Container */}
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

      {/* Transaction Details */}
      <div className="flex-1 min-w-0 text-left">
        <h4 className="text-[var(--color-on-surface)] truncate">
          {transaction.title}
        </h4>
        <p className="text-sm text-[var(--color-on-surface-variant)] mt-[var(--spacing-xs)]">
          {formattedDate}
        </p>
      </div>

      {/* Amount and Type Indicator */}
      <div className="flex flex-col items-end gap-[var(--spacing-xs)]">
        <AmountText 
          amount={transaction.amount}
          type={transaction.type}
          showSign
          size="base"
        />
        <div className="flex items-center gap-[var(--spacing-xs)]">
          {transaction.type === 'income' ? (
            <ArrowDownLeft size={12} style={{ color: 'var(--color-category-income)' }} />
          ) : (
            <ArrowUpRight size={12} style={{ color: 'var(--color-error)' }} />
          )}
          <span className="text-xs text-[var(--color-on-surface-variant)]">
            {categoryMeta?.label || transaction.category}
          </span>
        </div>
      </div>
    </button>
  );
}
