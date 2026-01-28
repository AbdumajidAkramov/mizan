/**
 * Mobile-First Premium Summary View
 * Optimized for mobile screens with single-column scroll
 * 
 * @design Mobile-First with 100% responsive layout
 * @architecture Single column, card-based, vertical scroll
 * @interactions Touch-optimized with drill-down capabilities
 */

import { useState } from 'react';
import {
  PieChart,
  Pie,
  Cell,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from 'recharts';
import {
  TrendingUp,
  TrendingDown,
  Wallet,
  ChevronRight,
  ChevronLeft,
  ChevronDown,
} from 'lucide-react';
import type { Transaction, TransactionCategory } from '../../../types/domain';

export interface MobilePremiumSummaryViewProps {
  /** Array of transactions to analyze */
  transactions: Transaction[];
  /** Current selected month */
  selectedMonth: Date;
  /** Callback when month changes */
  onMonthChange: (date: Date) => void;
  /** Callback when user drills down into category details */
  onCategoryDrillDown?: (category: TransactionCategory) => void;
  /** Callback when user drills down into weekly details */
  onWeekDrillDown?: (weekNumber: number) => void;
  /** Callback when user drills down into account details */
  onAccountDrillDown?: (accountId: string) => void;
}

type TimeRange = 'this-month' | 'last-month' | 'last-3-months' | 'this-year';

interface CategoryData {
  category: TransactionCategory;
  label: string;
  amount: number;
  percentage: number;
  color: string;
  count: number;
}

interface WeeklyData {
  week: string;
  weekNumber: number;
  income: number;
  expense: number;
  balance: number;
}

interface AccountData {
  accountId: string;
  accountLabel: string;
  amount: number;
  percentage: number;
  transactionCount: number;
}

// Category Colors (consistent with app)
const CATEGORY_COLORS: Record<TransactionCategory, string> = {
  'food-dining': '#ff6b9d',
  'transportation': '#4facfe',
  'shopping': '#ffa34d',
  'bills-utilities': '#00d2ff',
  'entertainment': '#c471f5',
  'healthcare': '#ff6b6b',
  'travel': '#667eea',
  'technology': '#00f2a0',
  'income-salary': '#00f2fe',
  'other': '#a0aec0',
};

const CATEGORY_LABELS: Record<TransactionCategory, string> = {
  'food-dining': 'Food & Dining',
  'transportation': 'Transportation',
  'shopping': 'Shopping',
  'bills-utilities': 'Bills & Utilities',
  'entertainment': 'Entertainment',
  'healthcare': 'Healthcare',
  'travel': 'Travel',
  'technology': 'Technology',
  'income-salary': 'Income',
  'other': 'Other',
};

/**
 * Mobile-First Premium Summary View Component
 */
export function MobilePremiumSummaryView({
  transactions,
  selectedMonth,
  onMonthChange,
  onCategoryDrillDown,
  onWeekDrillDown,
  onAccountDrillDown,
}: MobilePremiumSummaryViewProps) {
  const [timeRange, setTimeRange] = useState<TimeRange>('this-month');
  const [analyticsMode, setAnalyticsMode] = useState<'expense' | 'income'>('expense');

  // Month navigation
  const handlePreviousMonth = () => {
    const newDate = new Date(selectedMonth.getFullYear(), selectedMonth.getMonth() - 1, 1);
    onMonthChange(newDate);
  };

  const handleNextMonth = () => {
    const newDate = new Date(selectedMonth.getFullYear(), selectedMonth.getMonth() + 1, 1);
    onMonthChange(newDate);
  };

  // Calculate analytics data
  const { categoryData, totalIncome, totalExpense, netSavings } = analyzeCategoryData(
    transactions,
    analyticsMode
  );

  const weeklyData = analyzeWeeklyTrends(transactions, selectedMonth);
  const accountData = analyzeAccountDistribution(transactions, analyticsMode);
  const topCategories = categoryData.slice(0, 5);

  // Calculate savings rate
  const savingsRate = totalIncome > 0 ? (netSavings / totalIncome) * 100 : 0;

  return (
    <div className="w-full min-h-screen bg-[var(--premium-bg-primary)] pb-[var(--premium-space-2xl)]">
      {/* Mobile Header - Month Selector */}
      <div
        className="
          sticky top-0 z-20
          bg-[var(--premium-bg-primary)]
          border-b border-[var(--premium-glass-border)]
          px-[var(--premium-space-md)]
          py-[var(--premium-space-md)]
        "
      >
        <div className="flex items-center justify-between">
          {/* Previous Month Button */}
          <button
            onClick={handlePreviousMonth}
            className="
              w-[48px] h-[48px]
              rounded-full
              bg-[var(--premium-surface-2)]
              hover:bg-[var(--premium-surface-3)]
              active:bg-[var(--premium-surface-4)]
              flex items-center justify-center
              transition-all duration-200
              active:scale-95
            "
          >
            <ChevronLeft size={24} className="text-[var(--premium-text-primary)]" />
          </button>

          {/* Month Display */}
          <div className="text-center">
            <p className="heading-md text-[var(--premium-text-primary)]">
              {selectedMonth.toLocaleDateString('en-US', { month: 'long', year: 'numeric' })}
            </p>
            <p className="body-xs text-[var(--premium-text-tertiary)] mt-[2px]">
              {transactions.length} transactions
            </p>
          </div>

          {/* Next Month Button */}
          <button
            onClick={handleNextMonth}
            className="
              w-[48px] h-[48px]
              rounded-full
              bg-[var(--premium-surface-2)]
              hover:bg-[var(--premium-surface-3)]
              active:bg-[var(--premium-surface-4)]
              flex items-center justify-center
              transition-all duration-200
              active:scale-95
            "
          >
            <ChevronRight size={24} className="text-[var(--premium-text-primary)]" />
          </button>
        </div>
      </div>

      {/* Single Column Scroll Container */}
      <div className="flex flex-col gap-[var(--premium-space-md)] px-[var(--premium-space-md)] pt-[var(--premium-space-md)]">
        
        {/* 1. KEY METRICS CARDS - Top Priority */}
        <div className="w-full space-y-[var(--premium-space-sm)]">
          {/* Total Income Card */}
          <div
            className="
              w-full
              p-[var(--premium-space-lg)]
              rounded-[var(--premium-radius-xl)]
              bg-[var(--premium-emerald)]/10
              border-2 border-[var(--premium-emerald)]/30
              active:scale-[0.98]
              transition-all duration-200
            "
          >
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-[var(--premium-space-sm)]">
                <div
                  className="
                    w-[48px] h-[48px]
                    rounded-[var(--premium-radius-lg)]
                    bg-[var(--premium-emerald)]/20
                    flex items-center justify-center
                  "
                >
                  <TrendingUp size={24} className="text-[var(--premium-emerald)]" />
                </div>
                <div>
                  <p className="body-md text-[var(--premium-emerald)] uppercase tracking-wide font-semibold">
                    Income
                  </p>
                  <p className="body-xs text-[var(--premium-emerald)]/70">
                    This month
                  </p>
                </div>
              </div>
              <div className="text-right">
                <p className="heading-xl text-[var(--premium-emerald)] font-bold">
                  ${formatAmount(totalIncome)}
                </p>
              </div>
            </div>
          </div>

          {/* Total Expenses Card */}
          <div
            className="
              w-full
              p-[var(--premium-space-lg)]
              rounded-[var(--premium-radius-xl)]
              bg-[#f5576c]/10
              border-2 border-[#f5576c]/30
              active:scale-[0.98]
              transition-all duration-200
            "
          >
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-[var(--premium-space-sm)]">
                <div
                  className="
                    w-[48px] h-[48px]
                    rounded-[var(--premium-radius-lg)]
                    bg-[#f5576c]/20
                    flex items-center justify-center
                  "
                >
                  <TrendingDown size={24} className="text-[#f5576c]" />
                </div>
                <div>
                  <p className="body-md text-[#f5576c] uppercase tracking-wide font-semibold">
                    Expenses
                  </p>
                  <p className="body-xs text-[#f5576c]/70">
                    This month
                  </p>
                </div>
              </div>
              <div className="text-right">
                <p className="heading-xl text-[#f5576c] font-bold">
                  ${formatAmount(totalExpense)}
                </p>
              </div>
            </div>
          </div>

          {/* Net Savings Card */}
          <div
            className={`
              w-full
              p-[var(--premium-space-lg)]
              rounded-[var(--premium-radius-xl)]
              border-2
              active:scale-[0.98]
              transition-all duration-200
              ${
                netSavings >= 0
                  ? 'bg-[var(--premium-emerald)]/10 border-[var(--premium-emerald)]/30'
                  : 'bg-[#f5576c]/10 border-[#f5576c]/30'
              }
            `}
          >
            <div className="flex items-center justify-between mb-[var(--premium-space-sm)]">
              <div className="flex items-center gap-[var(--premium-space-sm)]">
                <div
                  className={`
                    w-[48px] h-[48px]
                    rounded-[var(--premium-radius-lg)]
                    flex items-center justify-center
                    ${netSavings >= 0 ? 'bg-[var(--premium-emerald)]/20' : 'bg-[#f5576c]/20'}
                  `}
                >
                  <Wallet size={24} className={netSavings >= 0 ? 'text-[var(--premium-emerald)]' : 'text-[#f5576c]'} />
                </div>
                <div>
                  <p
                    className={`
                      body-md uppercase tracking-wide font-semibold
                      ${netSavings >= 0 ? 'text-[var(--premium-emerald)]' : 'text-[#f5576c]'}
                    `}
                  >
                    Net Savings
                  </p>
                  <p className={`body-xs ${netSavings >= 0 ? 'text-[var(--premium-emerald)]/70' : 'text-[#f5576c]/70'}`}>
                    Savings rate: {savingsRate.toFixed(1)}%
                  </p>
                </div>
              </div>
              <div className="text-right">
                <p className={`heading-xl font-bold ${netSavings >= 0 ? 'text-[var(--premium-emerald)]' : 'text-[#f5576c]'}`}>
                  {netSavings >= 0 ? '+' : '-'}${formatAmount(Math.abs(netSavings))}
                </p>
              </div>
            </div>

            {/* Savings Progress Bar */}
            <div className="w-full h-[8px] bg-[var(--premium-surface-3)] rounded-full overflow-hidden">
              <div
                className={`h-full rounded-full transition-all duration-700 ${
                  netSavings >= 0 ? 'bg-[var(--premium-emerald)]' : 'bg-[#f5576c]'
                }`}
                style={{ width: `${Math.min(Math.max(savingsRate, 0), 100)}%` }}
              />
            </div>
          </div>
        </div>

        {/* 2. EXPENSES BY CATEGORY - Donut Chart with Toggle */}
        <div
          className="
            w-full
            p-[var(--premium-space-lg)]
            rounded-[var(--premium-radius-xl)]
            bg-[var(--premium-surface-2)]
            border border-[var(--premium-glass-border)]
          "
        >
          {/* Header with Toggle */}
          <div className="flex items-center justify-between mb-[var(--premium-space-lg)]">
            <h3 className="heading-lg text-[var(--premium-text-primary)]">
              Category Breakdown
            </h3>

            {/* Expense/Income Toggle */}
            <div
              className="
                flex
                p-[4px]
                rounded-[var(--premium-radius-full)]
                bg-[var(--premium-surface-3)]
              "
            >
              <button
                onClick={() => setAnalyticsMode('expense')}
                className={`
                  px-[var(--premium-space-md)]
                  py-[var(--premium-space-xs)]
                  rounded-[var(--premium-radius-full)]
                  body-sm font-medium
                  transition-all duration-200
                  min-w-[80px]
                  ${
                    analyticsMode === 'expense'
                      ? 'bg-[#f5576c] text-white shadow-[0_2px_12px_rgba(245,87,108,0.4)]'
                      : 'text-[var(--premium-text-secondary)]'
                  }
                `}
              >
                Expense
              </button>
              <button
                onClick={() => setAnalyticsMode('income')}
                className={`
                  px-[var(--premium-space-md)]
                  py-[var(--premium-space-xs)]
                  rounded-[var(--premium-radius-full)]
                  body-sm font-medium
                  transition-all duration-200
                  min-w-[80px]
                  ${
                    analyticsMode === 'income'
                      ? 'bg-[var(--premium-emerald)] text-white shadow-[0_2px_12px_rgba(16,185,129,0.4)]'
                      : 'text-[var(--premium-text-secondary)]'
                  }
                `}
              >
                Income
              </button>
            </div>
          </div>

          {/* Donut Chart */}
          {categoryData.length > 0 ? (
            <>
              <div className="relative w-full" style={{ height: '280px' }}>
                <ResponsiveContainer width="100%" height="100%">
                  <PieChart>
                    <Pie
                      data={categoryData}
                      cx="50%"
                      cy="50%"
                      innerRadius="55%"
                      outerRadius="85%"
                      paddingAngle={3}
                      dataKey="amount"
                    >
                      {categoryData.map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={entry.color} />
                      ))}
                    </Pie>
                    <Tooltip content={<CustomTooltip />} />
                  </PieChart>
                </ResponsiveContainer>

                {/* Center Label - Positioned Absolutely */}
                <div className="absolute inset-0 flex items-center justify-center pointer-events-none">
                  <div className="text-center">
                    <p className="body-sm text-[var(--premium-text-tertiary)] mb-[4px]">
                      Total
                    </p>
                    <p className={`heading-xl font-bold ${analyticsMode === 'expense' ? 'text-[#f5576c]' : 'text-[var(--premium-emerald)]'}`}>
                      ${formatAmount(analyticsMode === 'expense' ? totalExpense : totalIncome)}
                    </p>
                  </div>
                </div>
              </div>

              {/* Compact Legend - Mobile Optimized */}
              <div className="mt-[var(--premium-space-lg)] space-y-[var(--premium-space-xs)]">
                {categoryData.map((item, index) => (
                  <button
                    key={item.category}
                    onClick={() => onCategoryDrillDown?.(item.category)}
                    className="
                      w-full
                      flex items-center justify-between
                      p-[var(--premium-space-sm)]
                      rounded-[var(--premium-radius-md)]
                      bg-[var(--premium-surface-3)]
                      hover:bg-[var(--premium-surface-4)]
                      active:scale-[0.98]
                      transition-all duration-200
                      min-h-[56px]
                    "
                  >
                    <div className="flex items-center gap-[var(--premium-space-sm)] flex-1 min-w-0">
                      {/* Color Indicator */}
                      <div
                        className="w-[16px] h-[16px] rounded-full flex-shrink-0"
                        style={{ backgroundColor: item.color }}
                      />

                      {/* Category Info */}
                      <div className="flex-1 min-w-0 text-left">
                        <p className="body-md font-medium text-[var(--premium-text-primary)] truncate">
                          {item.label}
                        </p>
                        <p className="body-xs text-[var(--premium-text-tertiary)]">
                          {item.count} transaction{item.count !== 1 ? 's' : ''}
                        </p>
                      </div>
                    </div>

                    {/* Amount and Percentage */}
                    <div className="flex items-center gap-[var(--premium-space-sm)] flex-shrink-0">
                      <div className="text-right">
                        <p className="body-lg font-bold text-[var(--premium-text-primary)]">
                          ${formatAmount(item.amount)}
                        </p>
                        <p className="body-xs text-[var(--premium-text-tertiary)]">
                          {item.percentage.toFixed(1)}%
                        </p>
                      </div>
                      <ChevronRight size={20} className="text-[var(--premium-text-tertiary)]" />
                    </div>
                  </button>
                ))}
              </div>
            </>
          ) : (
            <div className="text-center py-[var(--premium-space-4xl)]">
              <p className="body-md text-[var(--premium-text-tertiary)]">
                No {analyticsMode} data for this month
              </p>
            </div>
          )}
        </div>

        {/* 3. WEEKLY TRENDS - Bar Chart */}
        <div
          className="
            w-full
            p-[var(--premium-space-lg)]
            rounded-[var(--premium-radius-xl)]
            bg-[var(--premium-surface-2)]
            border border-[var(--premium-glass-border)]
          "
        >
          <h3 className="heading-lg text-[var(--premium-text-primary)] mb-[var(--premium-space-lg)]">
            Weekly Trends
          </h3>

          {weeklyData.length > 0 ? (
            <div className="w-full" style={{ height: '300px' }}>
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={weeklyData} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
                  <CartesianGrid strokeDasharray="3 3" stroke="var(--premium-glass-border)" vertical={false} />
                  <XAxis
                    dataKey="week"
                    stroke="var(--premium-text-tertiary)"
                    style={{ fontSize: '12px' }}
                    tickLine={false}
                  />
                  <YAxis
                    stroke="var(--premium-text-tertiary)"
                    style={{ fontSize: '12px' }}
                    tickFormatter={(value) => `$${value > 1000 ? (value / 1000).toFixed(0) + 'k' : value}`}
                    tickLine={false}
                  />
                  <Tooltip content={<CustomBarTooltip />} />
                  <Bar dataKey="income" fill="#10b981" radius={[8, 8, 0, 0]} />
                  <Bar dataKey="expense" fill="#f5576c" radius={[8, 8, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            </div>
          ) : (
            <div className="text-center py-[var(--premium-space-4xl)]">
              <p className="body-md text-[var(--premium-text-tertiary)]">
                No data available
              </p>
            </div>
          )}
        </div>

        {/* 4. TOP 5 CATEGORIES */}
        <div
          className="
            w-full
            p-[var(--premium-space-lg)]
            rounded-[var(--premium-radius-xl)]
            bg-[var(--premium-surface-2)]
            border border-[var(--premium-glass-border)]
          "
        >
          <h3 className="heading-lg text-[var(--premium-text-primary)] mb-[var(--premium-space-md)]">
            Top 5 Categories
          </h3>

          <div className="space-y-[var(--premium-space-xs)]">
            {topCategories.length > 0 ? (
              topCategories.map((item, index) => (
                <button
                  key={item.category}
                  onClick={() => onCategoryDrillDown?.(item.category)}
                  className="
                    w-full
                    flex items-center gap-[var(--premium-space-sm)]
                    p-[var(--premium-space-md)]
                    rounded-[var(--premium-radius-lg)]
                    bg-[var(--premium-surface-3)]
                    hover:bg-[var(--premium-surface-4)]
                    active:scale-[0.98]
                    transition-all duration-200
                    min-h-[64px]
                  "
                >
                  {/* Rank Badge */}
                  <div
                    className="
                      w-[40px] h-[40px]
                      rounded-full
                      flex items-center justify-center
                      body-lg font-bold
                      flex-shrink-0
                    "
                    style={{
                      backgroundColor: `${item.color}20`,
                      color: item.color,
                    }}
                  >
                    {index + 1}
                  </div>

                  {/* Category Info */}
                  <div className="flex-1 min-w-0 text-left">
                    <p className="body-md font-semibold text-[var(--premium-text-primary)] truncate">
                      {item.label}
                    </p>
                    <p className="body-xs text-[var(--premium-text-tertiary)]">
                      {item.percentage.toFixed(1)}% of total • {item.count} txn{item.count !== 1 ? 's' : ''}
                    </p>
                  </div>

                  {/* Amount */}
                  <div className="flex items-center gap-[var(--premium-space-xs)] flex-shrink-0">
                    <p className="body-lg font-bold text-[var(--premium-text-primary)]">
                      ${formatAmount(item.amount)}
                    </p>
                    <ChevronRight size={20} className="text-[var(--premium-text-tertiary)]" />
                  </div>
                </button>
              ))
            ) : (
              <p className="text-center py-[var(--premium-space-lg)] body-sm text-[var(--premium-text-tertiary)]">
                No category data
              </p>
            )}
          </div>
        </div>

        {/* 5. ACCOUNT USAGE */}
        <div
          className="
            w-full
            p-[var(--premium-space-lg)]
            rounded-[var(--premium-radius-xl)]
            bg-[var(--premium-surface-2)]
            border border-[var(--premium-glass-border)]
          "
        >
          <h3 className="heading-lg text-[var(--premium-text-primary)] mb-[var(--premium-space-md)]">
            Account Usage
          </h3>

          <div className="space-y-[var(--premium-space-sm)]">
            {accountData.length > 0 ? (
              accountData.map((account) => (
                <button
                  key={account.accountId}
                  onClick={() => onAccountDrillDown?.(account.accountId)}
                  className="
                    w-full
                    p-[var(--premium-space-md)]
                    rounded-[var(--premium-radius-lg)]
                    bg-[var(--premium-surface-3)]
                    hover:bg-[var(--premium-surface-4)]
                    active:scale-[0.98]
                    transition-all duration-200
                    min-h-[80px]
                  "
                >
                  <div className="flex items-center justify-between mb-[var(--premium-space-sm)]">
                    {/* Account Info */}
                    <div className="flex items-center gap-[var(--premium-space-sm)]">
                      <div
                        className="
                          w-[48px] h-[48px]
                          rounded-full
                          flex items-center justify-center
                          bg-[var(--premium-emerald)]/20
                        "
                      >
                        <Wallet size={24} className="text-[var(--premium-emerald)]" />
                      </div>
                      <div className="text-left">
                        <p className="body-md font-semibold text-[var(--premium-text-primary)]">
                          {account.accountLabel}
                        </p>
                        <p className="body-xs text-[var(--premium-text-tertiary)]">
                          {account.transactionCount} transaction{account.transactionCount !== 1 ? 's' : ''}
                        </p>
                      </div>
                    </div>

                    {/* Amount */}
                    <div className="flex items-center gap-[var(--premium-space-xs)]">
                      <div className="text-right">
                        <p className="body-lg font-bold text-[var(--premium-text-primary)]">
                          ${formatAmount(account.amount)}
                        </p>
                        <p className="body-xs text-[var(--premium-text-tertiary)]">
                          {account.percentage.toFixed(1)}%
                        </p>
                      </div>
                      <ChevronRight size={20} className="text-[var(--premium-text-tertiary)]" />
                    </div>
                  </div>

                  {/* Progress Bar */}
                  <div className="w-full h-[6px] bg-[var(--premium-surface-4)] rounded-full overflow-hidden">
                    <div
                      className="h-full bg-[var(--premium-emerald)] rounded-full transition-all duration-700"
                      style={{ width: `${account.percentage}%` }}
                    />
                  </div>
                </button>
              ))
            ) : (
              <p className="text-center py-[var(--premium-space-lg)] body-sm text-[var(--premium-text-tertiary)]">
                No account data
              </p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

/**
 * Format amount for mobile display (compact notation)
 */
function formatAmount(amount: number): string {
  if (amount >= 1000000) {
    return (amount / 1000000).toFixed(1) + 'M';
  } else if (amount >= 10000) {
    return (amount / 1000).toFixed(1) + 'k';
  } else if (amount >= 1000) {
    return (amount / 1000).toFixed(2) + 'k';
  }
  return amount.toFixed(0);
}

/**
 * Analyze category data for donut chart
 */
function analyzeCategoryData(
  transactions: Transaction[],
  mode: 'expense' | 'income'
): {
  categoryData: CategoryData[];
  totalIncome: number;
  totalExpense: number;
  netSavings: number;
} {
  const filteredTransactions = transactions.filter((t) => t.type === mode);

  const categoryMap = new Map<TransactionCategory, { amount: number; count: number }>();

  filteredTransactions.forEach((txn) => {
    const category = txn.category || 'other';
    const existing = categoryMap.get(category) || { amount: 0, count: 0 };
    categoryMap.set(category, {
      amount: existing.amount + txn.amount,
      count: existing.count + 1,
    });
  });

  const total = Array.from(categoryMap.values()).reduce((sum, val) => sum + val.amount, 0);

  const categoryData: CategoryData[] = Array.from(categoryMap.entries())
    .map(([category, data]) => ({
      category,
      label: CATEGORY_LABELS[category],
      amount: data.amount,
      percentage: total > 0 ? (data.amount / total) * 100 : 0,
      color: CATEGORY_COLORS[category],
      count: data.count,
    }))
    .sort((a, b) => b.amount - a.amount);

  const totalIncome = transactions
    .filter((t) => t.type === 'income')
    .reduce((sum, t) => sum + t.amount, 0);

  const totalExpense = transactions
    .filter((t) => t.type === 'expense')
    .reduce((sum, t) => sum + t.amount, 0);

  const netSavings = totalIncome - totalExpense;

  return { categoryData, totalIncome, totalExpense, netSavings };
}

/**
 * Analyze weekly trends
 */
function analyzeWeeklyTrends(transactions: Transaction[], selectedMonth: Date): WeeklyData[] {
  const year = selectedMonth.getFullYear();
  const month = selectedMonth.getMonth();

  const firstDay = new Date(year, month, 1);
  const lastDay = new Date(year, month + 1, 0);

  const weeks: WeeklyData[] = [];
  let currentWeekStart = new Date(firstDay);
  let weekNumber = 1;

  while (currentWeekStart <= lastDay) {
    const currentWeekEnd = new Date(currentWeekStart);
    currentWeekEnd.setDate(currentWeekEnd.getDate() + 6);

    const weekEnd = currentWeekEnd > lastDay ? lastDay : currentWeekEnd;

    const weekTransactions = transactions.filter((txn) => {
      const txnDate = new Date(txn.timestamp);
      return txnDate >= currentWeekStart && txnDate <= weekEnd;
    });

    const income = weekTransactions
      .filter((t) => t.type === 'income')
      .reduce((sum, t) => sum + t.amount, 0);

    const expense = weekTransactions
      .filter((t) => t.type === 'expense')
      .reduce((sum, t) => sum + t.amount, 0);

    weeks.push({
      week: `W${weekNumber}`,
      weekNumber,
      income,
      expense,
      balance: income - expense,
    });

    currentWeekStart = new Date(weekEnd);
    currentWeekStart.setDate(currentWeekStart.getDate() + 1);
    weekNumber++;
  }

  return weeks;
}

/**
 * Analyze account distribution
 */
function analyzeAccountDistribution(
  transactions: Transaction[],
  mode: 'expense' | 'income'
): AccountData[] {
  const filteredTransactions = transactions.filter((t) => t.type === mode);

  const accountMap = new Map<string, { label: string; amount: number; count: number }>();

  filteredTransactions.forEach((txn) => {
    const accountId = txn.accountId || 'unknown';
    const existing = accountMap.get(accountId) || {
      label: txn.accountLabel || 'Unknown Account',
      amount: 0,
      count: 0,
    };
    accountMap.set(accountId, {
      label: existing.label,
      amount: existing.amount + txn.amount,
      count: existing.count + 1,
    });
  });

  const total = Array.from(accountMap.values()).reduce((sum, val) => sum + val.amount, 0);

  return Array.from(accountMap.entries())
    .map(([accountId, data]) => ({
      accountId,
      accountLabel: data.label,
      amount: data.amount,
      percentage: total > 0 ? (data.amount / total) * 100 : 0,
      transactionCount: data.count,
    }))
    .sort((a, b) => b.amount - a.amount);
}

/**
 * Custom Tooltip for Donut Chart
 */
function CustomTooltip({ active, payload }: any) {
  if (active && payload && payload.length) {
    const data = payload[0].payload as CategoryData;
    return (
      <div
        className="
          p-[var(--premium-space-md)]
          rounded-[var(--premium-radius-lg)]
          bg-[var(--premium-surface-2)]
          border-2 border-[var(--premium-glass-border)]
          shadow-[0_8px_24px_rgba(0,0,0,0.2)]
          min-w-[160px]
        "
      >
        <p className="body-md font-semibold text-[var(--premium-text-primary)] mb-[4px]">
          {data.label}
        </p>
        <p className="body-lg font-bold text-[var(--premium-text-primary)]">
          ${data.amount.toLocaleString('en-US', { minimumFractionDigits: 0, maximumFractionDigits: 0 })}
        </p>
        <p className="body-xs text-[var(--premium-text-tertiary)] mt-[2px]">
          {data.percentage.toFixed(1)}% • {data.count} transaction{data.count !== 1 ? 's' : ''}
        </p>
      </div>
    );
  }
  return null;
}

/**
 * Custom Tooltip for Bar Chart
 */
function CustomBarTooltip({ active, payload }: any) {
  if (active && payload && payload.length) {
    return (
      <div
        className="
          p-[var(--premium-space-md)]
          rounded-[var(--premium-radius-lg)]
          bg-[var(--premium-surface-2)]
          border-2 border-[var(--premium-glass-border)]
          shadow-[0_8px_24px_rgba(0,0,0,0.2)]
          min-w-[140px]
        "
      >
        <p className="body-md font-semibold text-[var(--premium-text-primary)] mb-[8px]">
          {payload[0].payload.week}
        </p>
        <div className="space-y-[4px]">
          <div className="flex items-center justify-between gap-[var(--premium-space-md)]">
            <span className="body-sm text-[var(--premium-emerald)]">Income:</span>
            <span className="body-md font-bold text-[var(--premium-emerald)]">
              ${payload[0].value.toLocaleString('en-US', { minimumFractionDigits: 0 })}
            </span>
          </div>
          <div className="flex items-center justify-between gap-[var(--premium-space-md)]">
            <span className="body-sm text-[#f5576c]">Expense:</span>
            <span className="body-md font-bold text-[#f5576c]">
              ${payload[1].value.toLocaleString('en-US', { minimumFractionDigits: 0 })}
            </span>
          </div>
        </div>
      </div>
    );
  }
  return null;
}
