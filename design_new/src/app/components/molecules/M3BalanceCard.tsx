/**
 * M3 BalanceCard Molecule Component
 * Material Design 3 card with primary gradient and surface tints
 * Maps to Jetpack Compose Card with gradient background
 */

import { Wallet, TrendingDown, PiggyBank } from 'lucide-react';
import { M3Surface } from '../atoms/M3Surface';

export interface M3BalanceCardProps {
  /** Total account balance */
  totalBalance: number;
  /** Monthly expenses */
  monthlyExpenses: number;
  /** Savings amount */
  savingsAmount: number;
}

/**
 * Material 3 Balance Card
 * Hero card with gradient using M3 primary colors
 * Shape: Extra Large (28dp corners)
 * 
 * @example
 * <M3BalanceCard
 *   totalBalance={8450.50}
 *   monthlyExpenses={1750}
 *   savingsAmount={1245}
 * />
 */
export function M3BalanceCard({
  totalBalance,
  monthlyExpenses,
  savingsAmount,
}: M3BalanceCardProps) {
  return (
    <div
      className="
        rounded-[var(--md-sys-shape-corner-extra-large)]
        p-[var(--md-sys-spacing-lg)]
        bg-gradient-to-br 
        from-[var(--md-sys-color-primary)] 
        to-[var(--md-sys-color-secondary)]
        text-[var(--md-sys-color-on-primary)]
      "
    >
      {/* Header */}
      <div className="flex items-center gap-[var(--md-sys-spacing-sm)] mb-[var(--md-sys-spacing-md)]">
        <Wallet size={20} className="opacity-90" />
        <p className="body-medium opacity-90">Total Balance</p>
      </div>

      {/* Balance Amount - Display Typography */}
      <h1 className="display-small mb-[var(--md-sys-spacing-lg)]">
        ${totalBalance.toLocaleString('en-US', { minimumFractionDigits: 2 })}
      </h1>

      {/* Sub Cards */}
      <div className="flex gap-[var(--md-sys-spacing-md)]">
        {/* Expenses Card */}
        <M3Surface
          elevation={0}
          shape="medium"
          className="
            flex-1 
            bg-white/20 
            backdrop-blur-sm 
            p-[var(--md-sys-spacing-md)]
          "
        >
          <div className="flex items-center gap-[var(--md-sys-spacing-sm)] mb-[var(--md-sys-spacing-sm)]">
            <div className="w-[32px] h-[32px] rounded-full bg-white/30 flex items-center justify-center">
              <TrendingDown size={16} />
            </div>
            <p className="label-small opacity-90">Expenses</p>
          </div>
          <p className="title-large">
            ${monthlyExpenses.toLocaleString('en-US', { minimumFractionDigits: 2 })}
          </p>
        </M3Surface>

        {/* Savings Card */}
        <M3Surface
          elevation={0}
          shape="medium"
          className="
            flex-1 
            bg-white/20 
            backdrop-blur-sm 
            p-[var(--md-sys-spacing-md)]
          "
        >
          <div className="flex items-center gap-[var(--md-sys-spacing-sm)] mb-[var(--md-sys-spacing-sm)]">
            <div className="w-[32px] h-[32px] rounded-full bg-white/30 flex items-center justify-center">
              <PiggyBank size={16} />
            </div>
            <p className="label-small opacity-90">Savings</p>
          </div>
          <p className="title-large">
            ${savingsAmount.toLocaleString('en-US', { minimumFractionDigits: 2 })}
          </p>
        </M3Surface>
      </div>
    </div>
  );
}
