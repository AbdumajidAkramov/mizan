/**
 * DashboardSummarySection Organism Component
 * Main dashboard view with balance, budget, and charts
 */

import { PieChart, Pie, Cell, ResponsiveContainer, LineChart, Line, XAxis, YAxis, Tooltip } from 'recharts';
import { BalanceCard } from '../molecules/BalanceCard';
import { BudgetProgressCard } from '../molecules/BudgetProgressCard';
import { CategoryCard } from '../molecules/CategoryCard';
import { LoadingSkeleton } from '../atoms/LoadingSkeleton';
import { ErrorState } from '../molecules/ErrorState';
import type { DashboardSummary, UiState, CategorySpending } from '../../../types/domain';

export interface DashboardSummarySectionProps {
  /** UI state wrapper with dashboard data */
  dashboardState: UiState<DashboardSummary>;
  /** Optional user display name */
  userDisplayName?: string;
}

/**
 * Material 3 Dashboard Summary Organism
 * Complete dashboard view with all financial metrics
 */
export function DashboardSummarySection({
  dashboardState,
  userDisplayName = 'User',
}: DashboardSummarySectionProps) {
  // Handle loading state
  if (dashboardState.status === 'loading') {
    return (
      <div className="flex flex-col gap-[var(--spacing-lg)]">
        <LoadingSkeleton width="100%" height={200} radius="lg" />
        <LoadingSkeleton width="100%" height={120} radius="lg" />
        <LoadingSkeleton width="100%" height={300} radius="lg" />
      </div>
    );
  }

  // Handle error state
  if (dashboardState.status === 'error') {
    return (
      <ErrorState
        message={dashboardState.error || 'Failed to load dashboard'}
      />
    );
  }

  // Handle no data
  if (!dashboardState.data) {
    return null;
  }

  const data = dashboardState.data;

  return (
    <div className="flex flex-col gap-[var(--spacing-lg)]">
      {/* Welcome Header */}
      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm text-[var(--color-on-surface-variant)]">Welcome back,</p>
          <h1 className="text-[var(--color-on-surface)] mt-[var(--spacing-xs)]">
            {userDisplayName}
          </h1>
        </div>
        <div className="w-[48px] h-[48px] rounded-full bg-gradient-to-br from-[var(--color-primary)] to-[var(--color-secondary)]" />
      </div>

      {/* Balance Card */}
      <BalanceCard
        totalBalance={data.totalBalance}
        monthlyExpenses={data.monthlyExpenses}
        savingsAmount={data.monthlySavings}
      />

      {/* Budget Progress */}
      <BudgetProgressCard
        spentAmount={data.monthlyExpenses}
        budgetLimit={data.budgetLimit}
        percentageUsed={data.budgetPercentageUsed}
      />

      {/* Spending by Category */}
      <div className="bg-[var(--color-surface)] rounded-2xl p-[var(--spacing-lg)] shadow-[var(--elevation-1)] border border-[var(--color-outline-variant)]">
        <h3 className="text-[var(--color-on-surface)] mb-[var(--spacing-md)]">
          Spending by Category
        </h3>
        <div className="flex items-center gap-[var(--spacing-lg)]">
          {/* Pie Chart */}
          <div className="w-[160px] h-[160px] flex-shrink-0">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={data.topCategories}
                  cx="50%"
                  cy="50%"
                  innerRadius={50}
                  outerRadius={70}
                  paddingAngle={5}
                  dataKey="totalAmount"
                >
                  {data.topCategories.map((category: CategorySpending, index: number) => (
                    <Cell key={`cell-${index}`} fill={category.colorToken} />
                  ))}
                </Pie>
              </PieChart>
            </ResponsiveContainer>
          </div>

          {/* Category Legend */}
          <div className="flex-1 space-y-[var(--spacing-sm)]">
            {data.topCategories.map((category: CategorySpending) => (
              <div key={category.category} className="flex items-center justify-between">
                <div className="flex items-center gap-[var(--spacing-sm)]">
                  <div
                    className="w-[12px] h-[12px] rounded-full"
                    style={{ backgroundColor: category.colorToken }}
                  />
                  <span className="text-sm text-[var(--color-on-surface)]">
                    {category.categoryLabel}
                  </span>
                </div>
                <span className="text-sm text-[var(--color-on-surface-variant)]">
                  ${category.totalAmount.toFixed(0)}
                </span>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Weekly Spending Trend */}
      <div className="bg-[var(--color-surface)] rounded-2xl p-[var(--spacing-lg)] shadow-[var(--elevation-1)] border border-[var(--color-outline-variant)]">
        <h3 className="text-[var(--color-on-surface)] mb-[var(--spacing-md)]">
          This Week's Spending
        </h3>
        <ResponsiveContainer width="100%" height={200}>
          <LineChart data={data.weeklySpending}>
            <XAxis
              dataKey="dayLabel"
              axisLine={false}
              tickLine={false}
              tick={{ fill: 'var(--color-on-surface-variant)', fontSize: 12 }}
            />
            <YAxis
              axisLine={false}
              tickLine={false}
              tick={{ fill: 'var(--color-on-surface-variant)', fontSize: 12 }}
            />
            <Tooltip
              contentStyle={{
                backgroundColor: 'var(--color-surface)',
                border: '1px solid var(--color-outline-variant)',
                borderRadius: '8px',
                fontSize: '12px',
              }}
            />
            <Line
              type="monotone"
              dataKey="totalAmount"
              stroke="var(--color-primary)"
              strokeWidth={3}
              dot={{ fill: 'var(--color-primary)', r: 5 }}
              activeDot={{ r: 7 }}
            />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}
