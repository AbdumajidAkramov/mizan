/**
 * Premium Cash Flow Card
 * Visual representation of income vs expenses
 */

import { ArrowDownLeft, ArrowUpRight } from 'lucide-react';
import { PremiumCard } from './PremiumCard';
import { BarChart, Bar, ResponsiveContainer, Cell } from 'recharts';

export interface PremiumCashFlowCardProps {
  /** Monthly income */
  income: number;
  /** Monthly expenses */
  expenses: number;
  /** Mini chart data for visualization */
  chartData?: { name: string; income: number; expense: number }[];
}

export function PremiumCashFlowCard({
  income,
  expenses,
  chartData = [
    { name: 'Week 1', income: 950, expense: 520 },
    { name: 'Week 2', income: 0, expense: 680 },
    { name: 'Week 3', income: 2900, expense: 720 },
    { name: 'Week 4', income: 0, expense: 685 },
  ],
}: PremiumCashFlowCardProps) {
  const netFlow = income - expenses;
  const isPositive = netFlow > 0;

  return (
    <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
      <div className="mb-[var(--premium-space-md)]">
        <p className="body-sm text-[var(--premium-text-tertiary)] mb-[var(--premium-space-sm)]">
          Monthly Cash Flow
        </p>
        <div className={`
          inline-flex items-center gap-[var(--premium-space-sm)]
          px-[var(--premium-space-md)] py-[var(--premium-space-sm)]
          rounded-[var(--premium-radius-md)]
          ${isPositive ? 'bg-[var(--premium-success)]/20' : 'bg-[var(--premium-error)]/20'}
        `}>
          <h3 className={`heading-lg ${isPositive ? 'text-[var(--premium-success)]' : 'text-[var(--premium-error)]'}`}>
            {isPositive ? '+' : ''}{netFlow.toLocaleString()}
          </h3>
        </div>
      </div>

      {/* Income & Expense breakdown */}
      <div className="grid grid-cols-2 gap-[var(--premium-space-md)] mb-[var(--premium-space-md)]">
        <div className="flex items-center gap-[var(--premium-space-sm)]">
          <div className="
            w-[32px] h-[32px]
            bg-[var(--premium-success)]/20
            rounded-full
            flex items-center justify-center
          ">
            <ArrowDownLeft size={16} className="text-[var(--premium-success)]" />
          </div>
          <div>
            <p className="body-xs text-[var(--premium-text-muted)]">Income</p>
            <p className="body-md text-[var(--premium-text-primary)] font-medium">
              ${income.toLocaleString()}
            </p>
          </div>
        </div>

        <div className="flex items-center gap-[var(--premium-space-sm)]">
          <div className="
            w-[32px] h-[32px]
            bg-[var(--premium-error)]/20
            rounded-full
            flex items-center justify-center
          ">
            <ArrowUpRight size={16} className="text-[var(--premium-error)]" />
          </div>
          <div>
            <p className="body-xs text-[var(--premium-text-muted)]">Expenses</p>
            <p className="body-md text-[var(--premium-text-primary)] font-medium">
              ${expenses.toLocaleString()}
            </p>
          </div>
        </div>
      </div>

      {/* Mini bar chart */}
      <div className="h-[50px] w-full min-h-[50px]" style={{ minHeight: '50px', height: '50px' }}>
        <ResponsiveContainer width="100%" height={50}>
          <BarChart data={chartData}>
            <Bar dataKey="income" radius={[4, 4, 0, 0]}>
              <Cell fill="var(--premium-emerald)" />
            </Bar>
            <Bar dataKey="expenses" radius={[4, 4, 0, 0]}>
              <Cell fill="var(--premium-error)" />
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      </div>
    </PremiumCard>
  );
}