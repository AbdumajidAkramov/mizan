/**
 * BalanceCard Molecule Component
 * Displays user's total balance with gradient background
 */

import { Wallet, TrendingDown, PiggyBank } from 'lucide-react';
import { AmountText } from '../atoms/AmountText';

export interface BalanceCardProps {
  /** Total account balance */
  totalBalance: number;
  /** Monthly expenses */
  monthlyExpenses: number;
  /** Savings amount */
  savingsAmount: number;
}

/**
 * Material 3 Balance Card
 * Hero card showing financial summary
 */
export function BalanceCard({
  totalBalance,
  monthlyExpenses,
  savingsAmount,
}: BalanceCardProps) {
  return (
    <div
      className="
        rounded-[28px]
        p-[var(--spacing-lg)]
        shadow-[var(--elevation-3)]
        bg-gradient-to-br from-[var(--color-primary)] to-[var(--color-secondary)]
        text-[var(--color-on-primary)]
      "
    >
      {/* Header */}
      <div className="flex items-center gap-[var(--spacing-sm)] mb-[var(--spacing-md)]">
        <Wallet size={20} className="opacity-90" />
        <p className="text-sm opacity-90">Total Balance</p>
      </div>

      {/* Balance Amount */}
      <h2 className="text-4xl mb-[var(--spacing-lg)]">
        ${totalBalance.toLocaleString('en-US', { minimumFractionDigits: 2 })}
      </h2>

      {/* Sub Cards */}
      <div className="flex gap-[var(--spacing-md)]">
        {/* Expenses Card */}
        <div className="flex-1 bg-white/20 backdrop-blur-sm rounded-2xl p-[var(--spacing-md)]">
          <div className="flex items-center gap-[var(--spacing-sm)] mb-[var(--spacing-sm)]">
            <div className="w-[32px] h-[32px] rounded-full bg-white/30 flex items-center justify-center">
              <TrendingDown size={16} />
            </div>
            <p className="text-xs opacity-90">Expenses</p>
          </div>
          <AmountText amount={monthlyExpenses} size="lg" className="text-[var(--color-on-primary)]" />
        </div>

        {/* Savings Card */}
        <div className="flex-1 bg-white/20 backdrop-blur-sm rounded-2xl p-[var(--spacing-md)]">
          <div className="flex items-center gap-[var(--spacing-sm)] mb-[var(--spacing-sm)]">
            <div className="w-[32px] h-[32px] rounded-full bg-white/30 flex items-center justify-center">
              <PiggyBank size={16} />
            </div>
            <p className="text-xs opacity-90">Savings</p>
          </div>
          <AmountText amount={savingsAmount} size="lg" className="text-[var(--color-on-primary)]" />
        </div>
      </div>
    </div>
  );
}
