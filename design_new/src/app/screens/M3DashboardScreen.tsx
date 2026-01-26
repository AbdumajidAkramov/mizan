/**
 * M3 DashboardScreen
 * Material Design 3 dashboard with financial overview
 * Maps to Jetpack Compose Screen composable
 */

import { PieChart, Pie, Cell, ResponsiveContainer, LineChart, Line, XAxis, YAxis, Tooltip } from 'recharts';
import { M3BalanceCard } from '../components/molecules/M3BalanceCard';
import { M3BudgetProgressCard } from '../components/molecules/M3BudgetProgressCard';
import { M3CategoryCard } from '../components/molecules/M3CategoryCard';
import { M3Surface } from '../components/atoms/M3Surface';
import { LoadingSkeleton } from '../components/atoms/LoadingSkeleton';
import { ErrorState } from '../components/molecules/ErrorState';
import type { DashboardSummary, UiState, CategorySpending } from '../../types/domain';

export interface M3DashboardScreenProps {
  /** Dashboard data state (MVI pattern) */
  dashboardState: UiState<DashboardSummary>;
  /** User display name */
  userDisplayName?: string;
}

/**
 * Material 3 Dashboard Screen
 * Complete financial overview with M3 components
 * 
 * @example
 * <M3DashboardScreen
 *   dashboardState={{ status: 'success', data: dashboardData }}
 *   userDisplayName="John Doe"
 * />
 */
export function M3DashboardScreen({
  dashboardState,
  userDisplayName = 'User',
}: M3DashboardScreenProps) {
  // MVI State: Loading
  if (dashboardState.status === 'loading') {
    return (
      <div className="flex flex-col gap-[var(--md-sys-spacing-lg)]">
        <LoadingSkeleton width="100%" height={200} radius="lg" />
        <LoadingSkeleton width="100%" height={120} radius="lg" />
        <LoadingSkeleton width="100%" height={300} radius="lg" />
      </div>
    );
  }

  // MVI State: Error
  if (dashboardState.status === 'error') {
    return (
      <ErrorState
        message={dashboardState.error || 'Failed to load dashboard'}
      />
    );
  }

  // MVI State: No Data
  if (!dashboardState.data) {
    return null;
  }

  const data = dashboardState.data;

  return (
    <div className="flex flex-col gap-[var(--md-sys-spacing-lg)]">
      {/* Welcome Header - M3 Typography */}
      <div className="flex items-center justify-between">
        <div>
          <p className="body-medium text-[var(--md-sys-color-on-surface-variant)]">
            Welcome back,
          </p>
          <h1 className="headline-medium text-[var(--md-sys-color-on-surface)] mt-[var(--md-sys-spacing-xs)]">
            {userDisplayName}
          </h1>
        </div>
        {/* Avatar - M3 Component */}
        <div className="
          w-[48px] h-[48px] 
          rounded-full 
          bg-gradient-to-br 
          from-[var(--md-sys-color-primary)] 
          to-[var(--md-sys-color-secondary)]
        " />
      </div>

      {/* Balance Card - Extra Large Shape (28dp) */}
      <M3BalanceCard
        totalBalance={data.totalBalance}
        monthlyExpenses={data.monthlyExpenses}
        savingsAmount={data.monthlySavings}
      />

      {/* Budget Progress - Medium Shape (12dp) */}
      <M3BudgetProgressCard
        spentAmount={data.monthlyExpenses}
        budgetLimit={data.budgetLimit}
        percentageUsed={data.budgetPercentageUsed}
      />

      {/* Spending by Category - M3 Surface */}
      <M3Surface
        elevation={1}
        shape="medium"
        className="p-[var(--md-sys-spacing-lg)]"
      >
        <h3 className="title-large text-[var(--md-sys-color-on-surface)] mb-[var(--md-sys-spacing-md)]">
          Spending by Category
        </h3>
        <div className="flex items-center gap-[var(--md-sys-spacing-lg)]">
          {/* M3 Chart */}
          <div className="w-[160px] h-[160px] flex-shrink-0" style={{ minWidth: '160px', minHeight: '160px' }}>
            <ResponsiveContainer width={160} height={160}>
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
          <div className="flex-1 space-y-[var(--md-sys-spacing-sm)]">
            {data.topCategories.map((category: CategorySpending) => (
              <div key={category.category} className="flex items-center justify-between">
                <div className="flex items-center gap-[var(--md-sys-spacing-sm)]">
                  <div
                    className="w-[12px] h-[12px] rounded-full"
                    style={{ backgroundColor: category.colorToken }}
                  />
                  <span className="body-medium text-[var(--md-sys-color-on-surface)]">
                    {category.categoryLabel}
                  </span>
                </div>
                <span className="body-medium text-[var(--md-sys-color-on-surface-variant)]">
                  ${category.totalAmount.toFixed(0)}
                </span>
              </div>
            ))}
          </div>
        </div>
      </M3Surface>

      {/* Weekly Spending Trend */}
      <M3Surface
        elevation={1}
        shape="medium"
        className="p-[var(--md-sys-spacing-lg)]"
      >
        <h3 className="title-large text-[var(--md-sys-color-on-surface)] mb-[var(--md-sys-spacing-md)]">
          This Week's Spending
        </h3>
        <ResponsiveContainer width="100%" height={200}>
          <LineChart data={data.weeklySpending}>
            <XAxis
              dataKey="dayLabel"
              axisLine={false}
              tickLine={false}
              tick={{ fill: 'var(--md-sys-color-on-surface-variant)', fontSize: 12 }}
            />
            <YAxis
              axisLine={false}
              tickLine={false}
              tick={{ fill: 'var(--md-sys-color-on-surface-variant)', fontSize: 12 }}
            />
            <Tooltip
              contentStyle={{
                backgroundColor: 'var(--md-sys-elevation-level1)',
                border: 'none',
                borderRadius: 'var(--md-sys-shape-corner-small)',
                fontSize: '12px',
              }}
            />
            <Line
              type="monotone"
              dataKey="totalAmount"
              stroke="var(--md-sys-color-primary)"
              strokeWidth={3}
              dot={{ fill: 'var(--md-sys-color-primary)', r: 5 }}
              activeDot={{ r: 7 }}
            />
          </LineChart>
        </ResponsiveContainer>
      </M3Surface>
    </div>
  );
}