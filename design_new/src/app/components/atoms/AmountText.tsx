/**
 * AmountText Atom Component
 * Displays monetary amounts with consistent formatting
 */

import type { TransactionType } from '../../../types/domain';

export interface AmountTextProps {
  /** Amount in USD */
  amount: number;
  /** Transaction type for color coding */
  type?: TransactionType;
  /** Show plus/minus prefix */
  showSign?: boolean;
  /** Text size variant */
  size?: 'sm' | 'base' | 'lg' | 'xl' | '2xl';
  /** Additional CSS classes */
  className?: string;
}

const sizeStyles = {
  sm: 'text-sm',
  base: 'text-base',
  lg: 'text-lg',
  xl: 'text-xl',
  '2xl': 'text-2xl',
};

/**
 * Material 3 Amount Text Component
 * Formats currency with proper styling
 */
export function AmountText({
  amount,
  type,
  showSign = false,
  size = 'base',
  className = '',
}: AmountTextProps) {
  const formattedAmount = amount.toLocaleString('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });

  const sign = showSign ? (type === 'income' ? '+' : '-') : '';
  
  const colorClass = type === 'income' 
    ? 'text-[var(--color-category-income)]' 
    : type === 'expense'
    ? 'text-[var(--color-on-surface)]'
    : 'text-[var(--color-on-surface)]';

  return (
    <span className={`${sizeStyles[size]} ${colorClass} ${className}`}>
      {sign}${formattedAmount}
    </span>
  );
}
