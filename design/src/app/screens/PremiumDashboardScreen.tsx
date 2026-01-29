/**
 * Premium Dashboard Screen
 * Stunning UI with gradients and glassmorphism
 */

import { PremiumCard } from '../components/premium/PremiumCard';
import { PremiumBalanceCard } from '../components/premium/PremiumBalanceCard';
import { PremiumTransactionItem } from '../components/premium/PremiumTransactionItem';
import { PremiumHealthScore } from '../components/premium/PremiumHealthScore';
import { PremiumNetWorthCard } from '../components/premium/PremiumNetWorthCard';
import { PremiumCashFlowCard } from '../components/premium/PremiumCashFlowCard';
import { PremiumEmergencyFund } from '../components/premium/PremiumEmergencyFund';
import { PremiumAIInsights } from '../components/premium/PremiumAIInsights';
import { PremiumExchangeRatesWidget } from '../components/premium/PremiumExchangeRatesWidget';
import { TrendingUp, PieChart, Wallet, Calendar, ArrowRight } from 'lucide-react';
import type { UiState, DashboardSummary, Transaction, CategorySpending } from '../../types/domain';
import { AreaChart, Area, ResponsiveContainer, PieChart as RechartsPieChart, Pie, Cell, XAxis, YAxis, Tooltip } from 'recharts';
import { MOCK_TRANSACTIONS } from '../../mocks/data';
import { LoadingSkeleton } from '../components/atoms/LoadingSkeleton';
import { ErrorState } from '../components/molecules/ErrorState';

export interface PremiumDashboardScreenProps {
  dashboardState: UiState<DashboardSummary>;
  userDisplayName?: string;
  /** Callback when user taps "See all" on Recent Transactions */
  onViewAllTransactions?: () => void;
}

export function PremiumDashboardScreen({
  dashboardState,
  userDisplayName = 'User',
  onViewAllTransactions,
}: PremiumDashboardScreenProps) {
  // Loading state
  if (dashboardState.status === 'loading') {
    return (
      <div className="flex flex-col gap-[var(--premium-space-lg)]">
        <LoadingSkeleton width="100%" height={240} radius="lg" />
        <LoadingSkeleton width="100%" height={160} radius="lg" />
        <LoadingSkeleton width="100%" height={300} radius="lg" />
      </div>
    );
  }

  // Error state
  if (dashboardState.status === 'error') {
    return <ErrorState message={dashboardState.error || 'Failed to load dashboard'} />;
  }

  if (!dashboardState.data) return null;

  const data = dashboardState.data;

  return (
    <div className="flex flex-col gap-[var(--premium-space-lg)] animate-fade-in-up">
      {/* Header with Greeting */}
      <div className="flex items-center justify-between">
        <div>
          <p className="body-md text-[var(--premium-text-tertiary)] mb-[4px]">
            Welcome back,
          </p>
          <h1 className="heading-xl text-[var(--premium-text-primary)]">
            {userDisplayName} 👋
          </h1>
        </div>
        
        {/* Avatar */}
        <div className="
          w-[52px] h-[52px]
          rounded-full
          bg-gradient-to-br from-[#667eea] to-[#764ba2]
          flex items-center justify-center
          heading-lg text-white
          shadow-[var(--premium-shadow-md)]
        ">
          {userDisplayName.split(' ').map(n => n[0]).join('')}
        </div>
      </div>

      {/* Premium Balance Card */}
      <PremiumBalanceCard
        totalBalance={data.totalBalance}
        monthlyIncome={data.totalBalance - data.monthlyExpenses + data.monthlySavings}
        monthlyExpenses={data.monthlyExpenses}
      />

      {/* NEW: Financial Health Score */}
      <PremiumHealthScore
        score={78}
        trend={5}
      />

      {/* NEW: Net Worth & Cash Flow Row */}
      <div className="grid grid-cols-1 gap-[var(--premium-space-md)]">
        <PremiumNetWorthCard
          netWorth={22450}
          change={1245}
          changePercent={5.9}
        />
        <PremiumCashFlowCard
          income={3850}
          expenses={data.monthlyExpenses}
        />
      </div>

      {/* NEW: Emergency Fund */}
      <PremiumEmergencyFund
        current={8500}
        goal={12000}
        targetMonths={6}
      />

      {/* Quick Stats Row */}
      <div className="grid grid-cols-2 gap-[var(--premium-space-md)]">
        {/* Budget Status */}
        <PremiumCard variant="glass" className="p-[var(--premium-space-md)]">
          <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-sm)]">
            <div className="
              w-[32px] h-[32px]
              bg-gradient-to-br from-[var(--premium-warning)] to-[var(--premium-error)]
              rounded-full
              flex items-center justify-center
            ">
              <TrendingUp size={16} className="text-white" />
            </div>
            <p className="body-sm text-[var(--premium-text-tertiary)]">Budget Used</p>
          </div>
          <div className="mb-[var(--premium-space-sm)]">
            <p className="heading-xl text-[var(--premium-text-primary)]">
              {Math.round(data.budgetPercentageUsed)}%
            </p>
            <p className="body-xs text-[var(--premium-text-muted)]">
              ${data.monthlyExpenses.toLocaleString()} of ${data.budgetLimit.toLocaleString()}
            </p>
          </div>
          {/* Progress bar */}
          <div className="w-full h-[6px] bg-[var(--premium-surface-2)] rounded-full overflow-hidden">
            <div
              className="h-full bg-gradient-to-r from-[var(--premium-warning)] to-[var(--premium-error)] rounded-full transition-all duration-500"
              style={{ width: `${Math.min(data.budgetPercentageUsed, 100)}%` }}
            />
          </div>
        </PremiumCard>

        {/* Savings */}
        <PremiumCard variant="glass" className="p-[var(--premium-space-md)]">
          <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-sm)]">
            <div className="
              w-[32px] h-[32px]
              bg-gradient-to-br from-[var(--premium-success)] to-[#4facfe]
              rounded-full
              flex items-center justify-center
            ">
              <Calendar size={16} className="text-white" />
            </div>
            <p className="body-sm text-[var(--premium-text-tertiary)]">This Month</p>
          </div>
          <div>
            <p className="heading-xl text-[var(--premium-text-primary)]">
              ${data.monthlySavings.toLocaleString()}
            </p>
            <p className="body-xs text-[var(--premium-success)]">
              +12.5% from last month
            </p>
          </div>
        </PremiumCard>
      </div>

      {/* Spending Chart */}
      <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
        <div className="flex items-center justify-between mb-[var(--premium-space-md)]">
          <h3 className="heading-md text-[var(--premium-text-primary)]">
            Spending Overview
          </h3>
          <button className="body-sm text-[var(--premium-primary)] flex items-center gap-[4px] hover:gap-[8px] transition-all">
            Details
            <ArrowRight size={14} />
          </button>
        </div>
        
        <div className="w-full min-h-[180px]">
          <ResponsiveContainer width="100%" height={180}>
            <AreaChart data={data.weeklySpending}>
              <defs>
                <linearGradient id="spendingGradient" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="0%" stopColor="var(--premium-primary)" stopOpacity={0.3} />
                  <stop offset="100%" stopColor="var(--premium-primary)" stopOpacity={0} />
                </linearGradient>
              </defs>
              <XAxis
                dataKey="day"
                axisLine={false}
                tickLine={false}
                tick={{ fontSize: 12, fill: 'var(--premium-text-muted)' }}
              />
              <YAxis
                axisLine={false}
                tickLine={false}
                tick={{ fontSize: 12, fill: 'var(--premium-text-muted)' }}
                tickFormatter={(value) => `$${value}`}
              />
              <Tooltip
                contentStyle={{
                  background: 'var(--premium-glass-bg)',
                  backdropFilter: 'blur(20px)',
                  border: '1px solid var(--premium-glass-border)',
                  borderRadius: 'var(--premium-radius-md)',
                  padding: '8px 12px',
                }}
                labelStyle={{ color: 'var(--premium-text-primary)' }}
              />
              <Area
                type="monotone"
                dataKey="amount"
                stroke="var(--premium-primary)"
                strokeWidth={2}
                fill="url(#spendingGradient)"
              />
            </AreaChart>
          </ResponsiveContainer>
        </div>
      </PremiumCard>

      {/* Top Categories */}
      <div>
        <div className="flex items-center justify-between mb-[var(--premium-space-md)]">
          <h3 className="heading-md text-[var(--premium-text-primary)]">
            Top Categories
          </h3>
          <button className="body-sm text-[var(--premium-primary)]">
            See all
          </button>
        </div>
        
        <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
          <div className="flex items-center gap-[var(--premium-space-lg)]">
            {/* Donut Chart */}
            <div className="w-[140px] h-[140px] min-w-[140px] min-h-[140px] flex-shrink-0" style={{ minWidth: '140px', minHeight: '140px' }}>
              <RechartsPieChart width={140} height={140}>
                <Pie
                  data={data.categorySpending || []}
                  cx="50%"
                  cy="50%"
                  innerRadius={40}
                  outerRadius={60}
                  paddingAngle={2}
                  dataKey="totalAmount"
                >
                  {(data.categorySpending || []).map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.colorToken} />
                  ))}
                </Pie>
              </RechartsPieChart>
            </div>

            {/* Category Legend */}
            <div className="flex-1 space-y-[var(--premium-space-sm)]">
              {(data.categorySpending || []).slice(0, 5).map((category) => (
                <div key={category.category} className="flex items-center justify-between">
                  <div className="flex items-center gap-[var(--premium-space-sm)]">
                    <div
                      className="w-[10px] h-[10px] rounded-full"
                      style={{ backgroundColor: category.colorToken }}
                    />
                    <span className="body-md text-[var(--premium-text-secondary)]">
                      {category.categoryLabel}
                    </span>
                  </div>
                  <span className="body-md text-[var(--premium-text-primary)] font-medium">
                    ${category.totalAmount.toFixed(0)}
                  </span>
                </div>
              ))}
            </div>
          </div>
        </PremiumCard>
      </div>

      {/* NEW: AI Insights */}
      <PremiumAIInsights />

      {/* NEW: Exchange Rates Widget */}
      <PremiumExchangeRatesWidget />

      {/* Recent Transactions */}
      <div>
        <div className="flex items-center justify-between mb-[var(--premium-space-md)]">
          <h3 className="heading-md text-[var(--premium-text-primary)]">
            Recent Transactions
          </h3>
          <button 
            onClick={onViewAllTransactions}
            className="
              body-sm font-medium text-[var(--premium-emerald)]
              flex items-center gap-[4px]
              hover:gap-[8px]
              transition-all duration-200
              active:scale-95
              px-[var(--premium-space-sm)]
              py-[4px]
              rounded-[var(--premium-radius-md)]
              hover:bg-[var(--premium-emerald)]/10
            "
          >
            See all
            <ArrowRight size={16} strokeWidth={2.5} />
          </button>
        </div>
        
        <div className="space-y-[var(--premium-space-sm)]">
          {dashboardState.data.recentTransactions?.slice(0, 5).map((transaction: Transaction) => (
            <PremiumTransactionItem
              key={transaction.id}
              transaction={transaction}
            />
          ))}
        </div>
      </div>
    </div>
  );
}