/**
 * M3 StatisticsScreen
 * Material Design 3 analytics and spending insights screen
 * Maps to Jetpack Compose Screen with Charts
 */

import { BarChart, Bar, XAxis, YAxis, ResponsiveContainer } from 'recharts';
import { TrendingUp, TrendingDown, Calendar } from 'lucide-react';
import { M3CategoryCard } from '../components/molecules/M3CategoryCard';
import { M3Surface } from '../components/atoms/M3Surface';
import { M3IconButton } from '../components/atoms/M3IconButton';
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

export interface M3StatisticsScreenProps {
  /** Top spending categories */
  topCategories: CategorySpending[];
}

/**
 * Material 3 Statistics Screen
 * Analytics and insights view with M3 components
 * 
 * @example
 * <M3StatisticsScreen topCategories={categorySpendingData} />
 */
export function M3StatisticsScreen({ topCategories }: M3StatisticsScreenProps) {
  const currentMonth = 1750;
  const lastMonth = 1920;
  const change = ((currentMonth - lastMonth) / lastMonth) * 100;

  return (
    <div className="flex flex-col gap-[var(--md-sys-spacing-lg)]">
      {/* Header with Period Selector */}
      <div className="flex items-center justify-between">
        <h1 className="headline-medium text-[var(--md-sys-color-on-surface)]">
          Statistics
        </h1>
        <M3Surface
          elevation={1}
          shape="full"
          className="
            flex items-center gap-[var(--md-sys-spacing-sm)] 
            px-[var(--md-sys-spacing-md)] 
            py-[var(--md-sys-spacing-sm)]
          "
        >
          <Calendar size={16} className="text-[var(--md-sys-color-on-surface-variant)]" />
          <span className="label-large text-[var(--md-sys-color-on-surface)]">This Month</span>
        </M3Surface>
      </div>

      {/* Summary Cards Grid */}
      <div className="grid grid-cols-2 gap-[var(--md-sys-spacing-md)]">
        {/* Total Spent Card */}
        <M3Surface
          elevation={1}
          shape="medium"
          className="p-[var(--md-sys-spacing-md)]"
        >
          <div className="flex items-center gap-[var(--md-sys-spacing-sm)] mb-[var(--md-sys-spacing-sm)]">
            <div className="
              w-[32px] h-[32px] 
              rounded-full 
              bg-[var(--md-sys-color-error-container)] 
              text-[var(--md-sys-color-on-error-container)] 
              flex items-center justify-center
            ">
              <TrendingUp size={16} />
            </div>
            <p className="body-small text-[var(--md-sys-color-on-surface-variant)]">
              Total Spent
            </p>
          </div>
          <p className="headline-small text-[var(--md-sys-color-on-surface)] mb-[var(--md-sys-spacing-xs)]">
            ${currentMonth.toLocaleString()}
          </p>
          <div className="flex items-center gap-[var(--md-sys-spacing-xs)]">
            <TrendingDown size={12} style={{ color: 'var(--md-category-income-primary)' }} />
            <span className="label-small" style={{ color: 'var(--md-category-income-primary)' }}>
              {Math.abs(change).toFixed(1)}% less
            </span>
          </div>
        </M3Surface>

        {/* Average Daily Card */}
        <M3Surface
          elevation={1}
          shape="medium"
          className="p-[var(--md-sys-spacing-md)]"
        >
          <div className="flex items-center gap-[var(--md-sys-spacing-sm)] mb-[var(--md-sys-spacing-sm)]">
            <div className="
              w-[32px] h-[32px] 
              rounded-full 
              bg-[var(--md-sys-color-primary-container)] 
              text-[var(--md-sys-color-on-primary-container)] 
              flex items-center justify-center
            ">
              <TrendingDown size={16} />
            </div>
            <p className="body-small text-[var(--md-sys-color-on-surface-variant)]">
              Avg. Daily
            </p>
          </div>
          <p className="headline-small text-[var(--md-sys-color-on-surface)] mb-[var(--md-sys-spacing-xs)]">
            ${(currentMonth / 30).toFixed(2)}
          </p>
          <p className="label-small text-[var(--md-sys-color-on-surface-variant)]">
            Based on 30 days
          </p>
        </M3Surface>
      </div>

      {/* Monthly Spending Trend - M3 Chart */}
      <M3Surface
        elevation={1}
        shape="medium"
        className="p-[var(--md-sys-spacing-lg)]"
      >
        <h3 className="title-large text-[var(--md-sys-color-on-surface)] mb-[var(--md-sys-spacing-md)]">
          6 Months Overview
        </h3>
        <ResponsiveContainer width="100%" height={220}>
          <BarChart data={monthlyData}>
            <XAxis
              dataKey="month"
              axisLine={false}
              tickLine={false}
              tick={{ fill: 'var(--md-sys-color-on-surface-variant)', fontSize: 12 }}
            />
            <YAxis
              axisLine={false}
              tickLine={false}
              tick={{ fill: 'var(--md-sys-color-on-surface-variant)', fontSize: 12 }}
            />
            <Bar
              dataKey="amount"
              fill="var(--md-sys-color-primary)"
              radius={[8, 8, 0, 0]}
              barSize={30}
            />
          </BarChart>
        </ResponsiveContainer>
      </M3Surface>

      {/* Top Spending Categories */}
      <div>
        <h3 className="title-large text-[var(--md-sys-color-on-surface)] mb-[var(--md-sys-spacing-md)]">
          Top Categories
        </h3>
        <div className="space-y-[var(--md-sys-spacing-sm)]">
          {topCategories.map((category) => (
            <M3CategoryCard key={category.category} categorySpending={category} />
          ))}
        </div>
      </div>

      {/* Monthly Comparison - M3 Container */}
      <M3Surface
        elevation={0}
        shape="medium"
        className="
          bg-gradient-to-br 
          from-[var(--md-sys-color-primary-container)] 
          to-[var(--md-sys-color-secondary-container)] 
          p-[var(--md-sys-spacing-lg)]
        "
      >
        <h3 className="title-large text-[var(--md-sys-color-on-surface)] mb-[var(--md-sys-spacing-md)]">
          Monthly Comparison
        </h3>
        <div className="flex items-center justify-between">
          <div>
            <p className="body-small text-[var(--md-sys-color-on-surface-variant)] mb-[var(--md-sys-spacing-xs)]">
              This Month
            </p>
            <p className="headline-medium text-[var(--md-sys-color-primary)]">
              ${currentMonth}
            </p>
          </div>
          <div className="
            w-[48px] h-[48px] 
            rounded-full 
            bg-[var(--md-sys-color-surface)] 
            flex items-center justify-center
          ">
            <TrendingDown size={24} style={{ color: 'var(--md-category-income-primary)' }} />
          </div>
          <div className="text-right">
            <p className="body-small text-[var(--md-sys-color-on-surface-variant)] mb-[var(--md-sys-spacing-xs)]">
              Last Month
            </p>
            <p className="headline-medium text-[var(--md-sys-color-on-surface-variant)]">
              ${lastMonth}
            </p>
          </div>
        </div>
        <M3Surface
          elevation={0}
          shape="small"
          className="
            mt-[var(--md-sys-spacing-md)] 
            p-[var(--md-sys-spacing-sm)] 
            bg-[var(--md-sys-color-surface)]
          "
        >
          <p className="body-medium text-center text-[var(--md-sys-color-on-surface)]">
            You saved <span style={{ color: 'var(--md-category-income-primary)' }}>${lastMonth - currentMonth}</span> this month! 🎉
          </p>
        </M3Surface>
      </M3Surface>
    </div>
  );
}
