/**
 * Premium Statistics Screen
 * Advanced analytics with charts and insights
 */

import { useState } from 'react';
import { PieChart, Pie, Cell, ResponsiveContainer, BarChart, Bar, XAxis, YAxis, Tooltip, LineChart, Line } from 'recharts';
import { PremiumCard } from '../components/premium/PremiumCard';
import { TrendingUp, TrendingDown, Calendar, DollarSign } from 'lucide-react';
import type { UiState, DashboardSummary } from '../../types/domain';
import { MOCK_DASHBOARD_SUMMARY } from '../../mocks/data';

export interface PremiumStatisticsScreenProps {
  dashboardState: UiState<DashboardSummary>;
}

export function PremiumStatisticsScreen({ dashboardState }: PremiumStatisticsScreenProps) {
  const [selectedPeriod, setSelectedPeriod] = useState<'week' | 'month' | 'year'>('month');

  const data = dashboardState.data || MOCK_DASHBOARD_SUMMARY;

  // Month comparison data
  const monthComparisonData = [
    { month: 'Jan', amount: 1450 },
    { month: 'Feb', amount: 1820 },
    { month: 'Mar', amount: 1650 },
    { month: 'Apr', amount: 2100 },
    { month: 'May', amount: 1880 },
    { month: 'Jun', amount: 2350 },
  ];

  // Category breakdown for bar chart
  const categoryBarData = data.topCategories.map(cat => ({
    name: cat.categoryLabel.split(' ')[0], // First word
    value: cat.totalAmount,
    color: cat.colorToken,
  }));

  return (
    <div className="flex flex-col gap-[var(--premium-space-lg)] animate-fade-in-up">
      {/* Header */}
      <div className="flex items-center justify-between">
        <h1 className="heading-xl text-[var(--premium-text-primary)]">
          Statistics
        </h1>
        <div className="
          px-[var(--premium-space-md)] py-[var(--premium-space-sm)]
          bg-gradient-to-r from-[#667eea] to-[#764ba2]
          rounded-[var(--premium-radius-full)]
          body-sm text-white font-medium
        ">
          January 2026
        </div>
      </div>

      {/* Period Selector */}
      <div className="flex gap-[var(--premium-space-sm)]">
        {(['week', 'month', 'year'] as const).map(period => (
          <button
            key={period}
            onClick={() => setSelectedPeriod(period)}
            className={`
              flex-1
              py-[var(--premium-space-sm)]
              rounded-[var(--premium-radius-md)]
              body-md font-medium
              transition-all duration-200
              ${selectedPeriod === period
                ? 'bg-[var(--premium-surface-3)] text-[var(--premium-text-primary)]'
                : 'bg-transparent text-[var(--premium-text-tertiary)] hover:bg-[var(--premium-surface-2)]'
              }
            `}
          >
            {period.charAt(0).toUpperCase() + period.slice(1)}
          </button>
        ))}
      </div>

      {/* Summary Stats */}
      <div className="grid grid-cols-2 gap-[var(--premium-space-md)]">
        <PremiumCard variant="glass" className="p-[var(--premium-space-md)]">
          <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-sm)]">
            <div className="
              w-[36px] h-[36px]
              bg-gradient-to-br from-[var(--premium-success)] to-[#4facfe]
              rounded-[var(--premium-radius-md)]
              flex items-center justify-center
            ">
              <TrendingUp size={18} className="text-white" />
            </div>
            <div className="flex-1">
              <p className="body-xs text-[var(--premium-text-muted)]">Income</p>
              <p className="heading-md text-[var(--premium-text-primary)]">
                $3,850
              </p>
            </div>
          </div>
          <div className="flex items-center gap-[4px]">
            <span className="body-xs text-[var(--premium-success)]">+12.5%</span>
            <span className="body-xs text-[var(--premium-text-muted)]">vs last month</span>
          </div>
        </PremiumCard>

        <PremiumCard variant="glass" className="p-[var(--premium-space-md)]">
          <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-sm)]">
            <div className="
              w-[36px] h-[36px]
              bg-gradient-to-br from-[var(--premium-error)] to-[var(--premium-secondary)]
              rounded-[var(--premium-radius-md)]
              flex items-center justify-center
            ">
              <TrendingDown size={18} className="text-white" />
            </div>
            <div className="flex-1">
              <p className="body-xs text-[var(--premium-text-muted)]">Expenses</p>
              <p className="heading-md text-[var(--premium-text-primary)]">
                ${data.monthlyExpenses.toLocaleString()}
              </p>
            </div>
          </div>
          <div className="flex items-center gap-[4px]">
            <span className="body-xs text-[var(--premium-error)]">-8.3%</span>
            <span className="body-xs text-[var(--premium-text-muted)]">vs last month</span>
          </div>
        </PremiumCard>
      </div>

      {/* Monthly Trend Chart */}
      <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
        <div className="mb-[var(--premium-space-md)]">
          <h3 className="heading-md text-[var(--premium-text-primary)] mb-[4px]">
            Monthly Trend
          </h3>
          <p className="body-sm text-[var(--premium-text-tertiary)]">
            Last 6 months spending pattern
          </p>
        </div>

        <ResponsiveContainer width="100%" height={200}>
          <LineChart data={monthComparisonData}>
            <defs>
              <linearGradient id="lineGradient" x1="0" y1="0" x2="1" y2="0">
                <stop offset="0%" stopColor="#667eea" />
                <stop offset="100%" stopColor="#764ba2" />
              </linearGradient>
            </defs>
            <XAxis
              dataKey="month"
              axisLine={false}
              tickLine={false}
              tick={{ fill: 'var(--premium-text-tertiary)', fontSize: 12 }}
            />
            <YAxis
              axisLine={false}
              tickLine={false}
              tick={{ fill: 'var(--premium-text-tertiary)', fontSize: 12 }}
            />
            <Tooltip
              contentStyle={{
                backgroundColor: 'var(--premium-surface-3)',
                border: '1px solid var(--premium-glass-border)',
                borderRadius: 'var(--premium-radius-sm)',
                fontSize: '12px',
                color: 'var(--premium-text-primary)',
              }}
            />
            <Line
              type="monotone"
              dataKey="amount"
              stroke="url(#lineGradient)"
              strokeWidth={3}
              dot={{ fill: '#667eea', strokeWidth: 2, r: 4 }}
              activeDot={{ r: 6 }}
            />
          </LineChart>
        </ResponsiveContainer>
      </PremiumCard>

      {/* Category Breakdown */}
      <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
        <div className="mb-[var(--premium-space-md)]">
          <h3 className="heading-md text-[var(--premium-text-primary)] mb-[4px]">
            Category Breakdown
          </h3>
          <p className="body-sm text-[var(--premium-text-tertiary)]">
            Spending by category
          </p>
        </div>

        <div className="flex items-center gap-[var(--premium-space-lg)]">
          {/* Donut Chart */}
          <div className="w-[160px] h-[160px] flex-shrink-0">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={data.topCategories}
                  cx="50%"
                  cy="50%"
                  innerRadius={50}
                  outerRadius={75}
                  paddingAngle={2}
                  dataKey="totalAmount"
                >
                  {data.topCategories.map((category, index) => (
                    <Cell key={`cell-${index}`} fill={category.colorToken} />
                  ))}
                </Pie>
              </PieChart>
            </ResponsiveContainer>
          </div>

          {/* Legend */}
          <div className="flex-1 space-y-[var(--premium-space-sm)]">
            {data.topCategories.map(category => (
              <div key={category.category} className="flex items-center justify-between">
                <div className="flex items-center gap-[var(--premium-space-sm)]">
                  <div
                    className="w-[12px] h-[12px] rounded-full"
                    style={{ backgroundColor: category.colorToken }}
                  />
                  <span className="body-md text-[var(--premium-text-secondary)]">
                    {category.categoryLabel}
                  </span>
                </div>
                <div className="text-right">
                  <p className="body-md text-[var(--premium-text-primary)] font-medium">
                    ${category.totalAmount.toFixed(0)}
                  </p>
                  <p className="body-xs text-[var(--premium-text-muted)]">
                    {category.percentage}%
                  </p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </PremiumCard>

      {/* Bar Chart */}
      <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
        <div className="mb-[var(--premium-space-md)]">
          <h3 className="heading-md text-[var(--premium-text-primary)] mb-[4px]">
            Top Categories
          </h3>
          <p className="body-sm text-[var(--premium-text-tertiary)]">
            Highest spending categories
          </p>
        </div>

        <ResponsiveContainer width="100%" height={220}>
          <BarChart data={categoryBarData}>
            <XAxis
              dataKey="name"
              axisLine={false}
              tickLine={false}
              tick={{ fill: 'var(--premium-text-tertiary)', fontSize: 12 }}
            />
            <YAxis
              axisLine={false}
              tickLine={false}
              tick={{ fill: 'var(--premium-text-tertiary)', fontSize: 12 }}
            />
            <Tooltip
              contentStyle={{
                backgroundColor: 'var(--premium-surface-3)',
                border: '1px solid var(--premium-glass-border)',
                borderRadius: 'var(--premium-radius-sm)',
                fontSize: '12px',
                color: 'var(--premium-text-primary)',
              }}
            />
            <Bar dataKey="value" radius={[8, 8, 0, 0]}>
              {categoryBarData.map((entry, index) => (
                <Cell key={`cell-${index}`} fill={entry.color} />
              ))}
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      </PremiumCard>

      {/* Insights */}
      <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
        <div className="mb-[var(--premium-space-md)]">
          <h3 className="heading-md text-[var(--premium-text-primary)] mb-[4px]">
            💡 Insights
          </h3>
        </div>

        <div className="space-y-[var(--premium-space-md)]">
          <div className="flex gap-[var(--premium-space-md)]">
            <div className="w-[4px] bg-gradient-to-b from-[#667eea] to-[#764ba2] rounded-full" />
            <div>
              <p className="body-md text-[var(--premium-text-primary)] mb-[4px]">
                Your spending decreased by 8.3% this month
              </p>
              <p className="body-sm text-[var(--premium-text-tertiary)]">
                Great job managing your expenses!
              </p>
            </div>
          </div>

          <div className="flex gap-[var(--premium-space-md)]">
            <div className="w-[4px] bg-gradient-to-b from-[#f5576c] to-[#ffa34d] rounded-full" />
            <div>
              <p className="body-md text-[var(--premium-text-primary)] mb-[4px]">
                Bills category is 30% of total spending
              </p>
              <p className="body-sm text-[var(--premium-text-tertiary)]">
                Consider reviewing your subscriptions
              </p>
            </div>
          </div>

          <div className="flex gap-[var(--premium-space-md)]">
            <div className="w-[4px] bg-gradient-to-b from-[#00f2fe] to-[#4facfe] rounded-full" />
            <div>
              <p className="body-md text-[var(--premium-text-primary)] mb-[4px]">
                You saved $1,245 this month
              </p>
              <p className="body-sm text-[var(--premium-text-tertiary)]">
                You're on track to meet your savings goal
              </p>
            </div>
          </div>
        </div>
      </PremiumCard>
    </div>
  );
}
