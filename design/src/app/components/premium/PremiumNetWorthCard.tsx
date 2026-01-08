/**
 * Premium Net Worth & Cash Flow Card
 * Shows total net worth with mini trend chart
 */

import { TrendingUp, TrendingDown, DollarSign } from 'lucide-react';
import { PremiumCard } from './PremiumCard';
import { LineChart, Line, ResponsiveContainer } from 'recharts';

export interface PremiumNetWorthCardProps {
  /** Current net worth */
  netWorth: number;
  /** Change from previous period */
  change: number;
  /** Change percentage */
  changePercent: number;
  /** Mini chart data for last 6 months */
  chartData?: { value: number }[];
}

export function PremiumNetWorthCard({
  netWorth,
  change,
  changePercent,
  chartData = [
    { value: 18500 },
    { value: 19200 },
    { value: 18800 },
    { value: 20100 },
    { value: 21300 },
    { value: 22450 },
  ],
}: PremiumNetWorthCardProps) {
  const isPositive = change >= 0;

  return (
    <PremiumCard variant="glass" className="p-[var(--premium-space-lg)] overflow-hidden relative">
      {/* Gradient orb */}
      <div className="absolute top-0 right-0 w-[120px] h-[120px] bg-gradient-to-br from-[#667eea]/20 to-[#764ba2]/20 rounded-full blur-[40px] pointer-events-none" />

      <div className="relative z-10">
        <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-md)]">
          <div className="
            w-[36px] h-[36px]
            bg-gradient-to-br from-[#667eea] to-[#764ba2]
            rounded-[var(--premium-radius-md)]
            flex items-center justify-center
          ">
            <DollarSign size={20} className="text-white" />
          </div>
          <div>
            <p className="body-xs text-[var(--premium-text-muted)]">Total Net Worth</p>
          </div>
        </div>

        <div className="mb-[var(--premium-space-md)]">
          <h2 className="display-sm text-[var(--premium-text-primary)] mb-[var(--premium-space-xs)]">
            ${netWorth.toLocaleString()}
          </h2>
          <div className="flex items-center gap-[var(--premium-space-sm)]">
            <div className={`
              flex items-center gap-[4px]
              px-[var(--premium-space-sm)] py-[4px]
              rounded-full
              ${isPositive ? 'bg-[var(--premium-success)]/20' : 'bg-[var(--premium-error)]/20'}
            `}>
              {isPositive ? (
                <TrendingUp size={14} className="text-[var(--premium-success)]" />
              ) : (
                <TrendingDown size={14} className="text-[var(--premium-error)]" />
              )}
              <span className={`body-xs font-medium ${isPositive ? 'text-[var(--premium-success)]' : 'text-[var(--premium-error)]'}`}>
                {isPositive ? '+' : ''}{change.toLocaleString()}
              </span>
            </div>
            <span className="body-sm text-[var(--premium-text-tertiary)]">
              ({isPositive ? '+' : ''}{changePercent}%) this month
            </span>
          </div>
        </div>

        {/* Mini trend chart */}
        <div className="h-[60px] -mx-[var(--premium-space-sm)]">
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={chartData}>
              <defs>
                <linearGradient id="netWorthGradient" x1="0" y1="0" x2="1" y2="0">
                  <stop offset="0%" stopColor="#667eea" />
                  <stop offset="100%" stopColor="#764ba2" />
                </linearGradient>
              </defs>
              <Line
                type="monotone"
                dataKey="value"
                stroke="url(#netWorthGradient)"
                strokeWidth={2}
                dot={false}
                animationDuration={1000}
              />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>
    </PremiumCard>
  );
}
