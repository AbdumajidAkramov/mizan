/**
 * StatisticsScreen
 * Screen displaying spending analytics and statistics
 */

import { BarChart, Bar, XAxis, YAxis, ResponsiveContainer } from 'recharts';
import { TrendingUp, TrendingDown, Calendar } from 'lucide-react';
import { CategoryCard } from '../components/molecules/CategoryCard';
import type { CategorySpending } from '../../types/domain';

// Mock monthly data
const monthlyData = [
  { month: 'Jan', amount: 1750 },
  { month: 'Feb', amount: 1920 },
  { month: 'Mar', amount: 1680 },
  { month: 'Apr', amount: 2100 },
  { month: 'May', amount: 1890 },
  { month: 'Jun', amount: 2050 },
];

export interface StatisticsScreenProps {
  /** Top spending categories */
  topCategories: CategorySpending[];
}

/**
 * Statistics Screen Component
 * Analytics and insights view
 */
export function StatisticsScreen({ topCategories }: StatisticsScreenProps) {
  const currentMonth = 1750;
  const lastMonth = 1920;
  const change = ((currentMonth - lastMonth) / lastMonth) * 100;

  return (
    <div className="flex flex-col gap-[var(--spacing-lg)] pb-[var(--spacing-3xl)]">
      {/* Header */}
      <div className="flex items-center justify-between">
        <h1 className="text-[var(--color-on-surface)]">Statistics</h1>
        <button className="flex items-center gap-[var(--spacing-sm)] px-[var(--spacing-md)] py-[var(--spacing-sm)] bg-[var(--color-surface)] rounded-full border border-[var(--color-outline-variant)] shadow-[var(--elevation-1)]">
          <Calendar size={16} className="text-[var(--color-on-surface-variant)]" />
          <span className="text-sm text-[var(--color-on-surface)]">This Month</span>
        </button>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-2 gap-[var(--spacing-md)]">
        <div className="bg-[var(--color-surface)] rounded-2xl p-[var(--spacing-md)] shadow-[var(--elevation-1)] border border-[var(--color-outline-variant)]">
          <div className="flex items-center gap-[var(--spacing-sm)] mb-[var(--spacing-sm)]">
            <div className="w-[32px] h-[32px] rounded-full bg-[var(--color-error-container)] text-[var(--color-on-error-container)] flex items-center justify-center">
              <TrendingUp size={16} />
            </div>
            <p className="text-sm text-[var(--color-on-surface-variant)]">Total Spent</p>
          </div>
          <p className="text-2xl text-[var(--color-on-surface)] mb-[var(--spacing-xs)]">
            ${currentMonth.toLocaleString()}
          </p>
          <div className="flex items-center gap-[var(--spacing-xs)]">
            <TrendingDown size={12} style={{ color: 'var(--color-category-income)' }} />
            <span className="text-xs" style={{ color: 'var(--color-category-income)' }}>
              {Math.abs(change).toFixed(1)}% less than last month
            </span>
          </div>
        </div>

        <div className="bg-[var(--color-surface)] rounded-2xl p-[var(--spacing-md)] shadow-[var(--elevation-1)] border border-[var(--color-outline-variant)]">
          <div className="flex items-center gap-[var(--spacing-sm)] mb-[var(--spacing-sm)]">
            <div className="w-[32px] h-[32px] rounded-full bg-[var(--color-primary-container)] text-[var(--color-on-primary-container)] flex items-center justify-center">
              <TrendingDown size={16} />
            </div>
            <p className="text-sm text-[var(--color-on-surface-variant)]">Avg. Daily</p>
          </div>
          <p className="text-2xl text-[var(--color-on-surface)] mb-[var(--spacing-xs)]">
            ${(currentMonth / 30).toFixed(2)}
          </p>
          <p className="text-xs text-[var(--color-on-surface-variant)]">Based on 30 days</p>
        </div>
      </div>

      {/* Monthly Spending Trend */}
      <div className="bg-[var(--color-surface)] rounded-2xl p-[var(--spacing-lg)] shadow-[var(--elevation-1)] border border-[var(--color-outline-variant)]">
        <h3 className="text-[var(--color-on-surface)] mb-[var(--spacing-md)]">
          6 Months Overview
        </h3>
        <ResponsiveContainer width="100%" height={220}>
          <BarChart data={monthlyData}>
            <XAxis
              dataKey="month"
              axisLine={false}
              tickLine={false}
              tick={{ fill: 'var(--color-on-surface-variant)', fontSize: 12 }}
            />
            <YAxis
              axisLine={false}
              tickLine={false}
              tick={{ fill: 'var(--color-on-surface-variant)', fontSize: 12 }}
            />
            <Bar
              dataKey="amount"
              fill="var(--color-primary)"
              radius={[8, 8, 0, 0]}
              barSize={30}
            />
          </BarChart>
        </ResponsiveContainer>
      </div>

      {/* Top Spending Categories */}
      <div>
        <h3 className="text-[var(--color-on-surface)] mb-[var(--spacing-md)]">
          Top Categories
        </h3>
        <div className="space-y-[var(--spacing-sm)]">
          {topCategories.map((category) => (
            <CategoryCard key={category.category} categorySpending={category} />
          ))}
        </div>
      </div>

      {/* Monthly Comparison */}
      <div className="bg-gradient-to-br from-[var(--color-primary-container)] to-[var(--color-secondary-container)] rounded-2xl p-[var(--spacing-lg)] border border-[var(--color-outline-variant)]">
        <h3 className="text-[var(--color-on-surface)] mb-[var(--spacing-md)]">
          Monthly Comparison
        </h3>
        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm text-[var(--color-on-surface-variant)] mb-[var(--spacing-xs)]">
              This Month
            </p>
            <p className="text-2xl text-[var(--color-primary)]">${currentMonth}</p>
          </div>
          <div className="w-[48px] h-[48px] rounded-full bg-[var(--color-surface)] flex items-center justify-center">
            <TrendingDown size={24} style={{ color: 'var(--color-category-income)' }} />
          </div>
          <div className="text-right">
            <p className="text-sm text-[var(--color-on-surface-variant)] mb-[var(--spacing-xs)]">
              Last Month
            </p>
            <p className="text-2xl text-[var(--color-on-surface-variant)]">${lastMonth}</p>
          </div>
        </div>
        <div className="mt-[var(--spacing-md)] p-[var(--spacing-sm)] bg-[var(--color-surface)] rounded-xl">
          <p className="text-sm text-center text-[var(--color-on-surface)]">
            You saved <span style={{ color: 'var(--color-category-income)' }}>${lastMonth - currentMonth}</span> this month! 🎉
          </p>
        </div>
      </div>
    </div>
  );
}
