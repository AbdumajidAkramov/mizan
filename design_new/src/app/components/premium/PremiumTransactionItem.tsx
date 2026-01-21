/**
 * Premium Transaction List Item
 * Glassmorphism with smooth hover effects
 */

import { CategoryIcon } from '../atoms/CategoryIcon';
import { PremiumCard } from './PremiumCard';
import { ArrowUpRight, ArrowDownLeft } from 'lucide-react';
import type { Transaction } from '../../../types/domain';
import { getCategoryMetadata } from '../../../mocks/data';

export interface PremiumTransactionItemProps {
  transaction: Transaction;
  onClick?: (transaction: Transaction) => void;
}

export function PremiumTransactionItem({ transaction, onClick }: PremiumTransactionItemProps) {
  const categoryMeta = getCategoryMetadata(transaction.category);
  const categoryColor = categoryMeta?.colorToken || 'var(--premium-primary)';
  
  const date = new Date(transaction.timestamp);
  const formattedTime = date.toLocaleTimeString('en-US', { 
    hour: '2-digit',
    minute: '2-digit',
  });
  const formattedDate = date.toLocaleDateString('en-US', { 
    month: 'short', 
    day: 'numeric',
  });

  return (
    <PremiumCard
      variant="glass"
      hover
      onClick={() => onClick?.(transaction)}
      className="p-[var(--premium-space-md)] animate-fade-in-up"
    >
      <div className="flex items-center gap-[var(--premium-space-md)]">
        {/* Category Icon */}
        <div
          className="
            w-[48px] h-[48px]
            rounded-[var(--premium-radius-md)]
            flex items-center justify-center
            flex-shrink-0
            relative
            overflow-hidden
          "
          style={{ 
            background: `linear-gradient(135deg, ${categoryColor}40, ${categoryColor}20)`,
          }}
        >
          <div 
            className="absolute inset-0 opacity-20"
            style={{ background: categoryColor }}
          />
          <CategoryIcon category={transaction.category} size={24} color={categoryColor} />
        </div>

        {/* Transaction Details */}
        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-[var(--premium-space-xs)] mb-[2px]">
            <h4 className="body-lg text-[var(--premium-text-primary)] font-medium truncate">
              {transaction.title}
            </h4>
            {transaction.type === 'income' ? (
              <ArrowDownLeft size={14} className="text-[var(--premium-success)] flex-shrink-0" />
            ) : (
              <ArrowUpRight size={14} className="text-[var(--premium-error)] flex-shrink-0" />
            )}
          </div>
          <div className="flex items-center gap-[var(--premium-space-sm)]">
            <p className="body-sm text-[var(--premium-text-tertiary)]">
              {formattedTime}
            </p>
            <span className="w-[3px] h-[3px] rounded-full bg-[var(--premium-text-muted)]" />
            <p className="body-sm text-[var(--premium-text-tertiary)]">
              {formattedDate}
            </p>
          </div>
        </div>

        {/* Amount */}
        <div className="text-right flex-shrink-0">
          <p 
            className={`
              heading-md
              ${transaction.type === 'income' ? 'text-[var(--premium-success)]' : 'text-[var(--premium-text-primary)]'}
            `}
          >
            {transaction.type === 'income' ? '+' : '-'}${Math.abs(transaction.amount).toFixed(2)}
          </p>
          <p className="body-xs text-[var(--premium-text-muted)]">
            {categoryMeta?.label || transaction.category}
          </p>
        </div>
      </div>
    </PremiumCard>
  );
}
