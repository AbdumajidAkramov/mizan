/**
 * Premium Balance Card
 * Hero card with stunning gradients and glassmorphism
 */

import { Wallet, TrendingUp, TrendingDown, Eye, EyeOff } from 'lucide-react';
import { PremiumCard } from './PremiumCard';
import { useState } from 'react';

export interface PremiumBalanceCardProps {
  totalBalance: number;
  monthlyIncome: number;
  monthlyExpenses: number;
}

export function PremiumBalanceCard({
  totalBalance,
  monthlyIncome,
  monthlyExpenses,
}: PremiumBalanceCardProps) {
  const [balanceVisible, setBalanceVisible] = useState(true);

  return (
    <div
      className="
        relative
        rounded-[var(--premium-radius-2xl)]
        p-[var(--premium-space-xl)]
        bg-gradient-to-br from-[#667eea] via-[#764ba2] to-[#f5576c]
        shadow-[var(--premium-shadow-xl)]
        overflow-hidden
      "
    >
      {/* Animated background gradient orbs */}
      <div className="absolute top-0 right-0 w-[200px] h-[200px] bg-white/10 rounded-full blur-[60px]" />
      <div className="absolute bottom-0 left-0 w-[180px] h-[180px] bg-black/10 rounded-full blur-[50px]" />
      
      {/* Content */}
      <div className="relative z-10">
        {/* Header */}
        <div className="flex items-center justify-between mb-[var(--premium-space-lg)]">
          <div className="flex items-center gap-[var(--premium-space-sm)]">
            <div className="
              w-[40px] h-[40px]
              bg-white/20
              backdrop-blur-sm
              rounded-full
              flex items-center justify-center
            ">
              <Wallet size={20} className="text-white" />
            </div>
            <div>
              <p className="body-sm text-white/80">Total Balance</p>
            </div>
          </div>
          
          <button
            onClick={() => setBalanceVisible(!balanceVisible)}
            className="
              w-[40px] h-[40px]
              bg-white/10
              backdrop-blur-sm
              rounded-full
              flex items-center justify-center
              hover:bg-white/20
              transition-all duration-200
            "
          >
            {balanceVisible ? (
              <Eye size={18} className="text-white" />
            ) : (
              <EyeOff size={18} className="text-white" />
            )}
          </button>
        </div>

        {/* Balance Amount */}
        <div className="mb-[var(--premium-space-xl)]">
          {balanceVisible ? (
            <h1 className="display-lg text-white">
              ${totalBalance.toLocaleString('en-US', { minimumFractionDigits: 2 })}
            </h1>
          ) : (
            <h1 className="display-lg text-white">
              ••••••
            </h1>
          )}
        </div>

        {/* Income & Expense Cards */}
        <div className="flex gap-[var(--premium-space-md)]">
          {/* Income */}
          <div className="
            flex-1
            bg-white/10
            backdrop-blur-md
            rounded-[var(--premium-radius-lg)]
            p-[var(--premium-space-md)]
            border border-white/20
          ">
            <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-xs)]">
              <div className="
                w-[28px] h-[28px]
                bg-[var(--premium-success)]
                rounded-full
                flex items-center justify-center
              ">
                <TrendingUp size={14} className="text-white" />
              </div>
              <p className="body-sm text-white/70">Income</p>
            </div>
            <p className="heading-md text-white">
              ${monthlyIncome.toLocaleString()}
            </p>
          </div>

          {/* Expenses */}
          <div className="
            flex-1
            bg-white/10
            backdrop-blur-md
            rounded-[var(--premium-radius-lg)]
            p-[var(--premium-space-md)]
            border border-white/20
          ">
            <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-xs)]">
              <div className="
                w-[28px] h-[28px]
                bg-[var(--premium-error)]
                rounded-full
                flex items-center justify-center
              ">
                <TrendingDown size={14} className="text-white" />
              </div>
              <p className="body-sm text-white/70">Expenses</p>
            </div>
            <p className="heading-md text-white">
              ${monthlyExpenses.toLocaleString()}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
