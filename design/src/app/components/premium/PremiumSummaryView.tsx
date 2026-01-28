/**
 * Premium Summary View - Money Manager Style
 * Comprehensive analytics with donut charts, bar charts, and financial metrics
 * 
 * @design RealByte Money Manager Analytics Pattern
 * @architecture Data visualization with Recharts
 * @interactions Toggle expense/income, interactive charts
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
  Legend,
} from 'recharts';
import {
  TrendingUp,
  TrendingDown,
  Wallet,
  DollarSign,
  CreditCard,
  ChevronLeft,
  ChevronRight,
} from 'lucide-react';
import type { Transaction, TransactionCategory } from '../../../types/domain';

export interface PremiumSummaryViewProps {
  /** Array of transactions to analyze */
  transactions: Transaction[];
  /** Current selected month */
  selectedMonth: Date;
  /** Callback when month changes */
  onMonthChange: (date: Date) => void;
}

type AnalyticsMode = 'expense' | 'income';

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
 * PremiumSummaryView Component
 */
export function PremiumSummaryView({
  transactions,
  selectedMonth,
  onMonthChange,
}: PremiumSummaryViewProps) {
  const [analyticsMode, setAnalyticsMode] = useState<AnalyticsMode>('expense');

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

  return (
    <div className="flex flex-col gap-[var(--premium-space-lg)] animate-[fadeIn_0.3s_ease-out]">
      {/* Month Selector */}
      <div
        className="
          flex items-center justify-between
          p-[var(--premium-space-md)]
          rounded-[var(--premium-radius-lg)]
          bg-[var(--premium-surface-2)]
          border border-[var(--premium-glass-border)]
        "
      >
        <button
          onClick={handlePreviousMonth}
          className="
            w-[36px] h-[36px]
            rounded-full
            bg-[var(--premium-surface-3)]
            hover:bg-[var(--premium-emerald)]/20
            flex items-center justify-center
            transition-all duration-200
            active:scale-95
          "
        >
          <ChevronLeft size={20} className="text-[var(--premium-text-primary)]" />
        </button>

        <div className="text-center">
          <p className="heading-md text-[var(--premium-text-primary)]">
            {selectedMonth.toLocaleDateString('en-US', { month: 'long', year: 'numeric' })}
          </p>
          <p className="body-xs text-[var(--premium-text-tertiary)]">
            Analytics & Insights
          </p>
        </div>

        <button
          onClick={handleNextMonth}
          className="
            w-[36px] h-[36px]
            rounded-full
            bg-[var(--premium-surface-3)]
            hover:bg-[var(--premium-emerald)]/20
            flex items-center justify-center
            transition-all duration-200
            active:scale-95
          "
        >
          <ChevronRight size={20} className="text-[var(--premium-text-primary)]" />
        </button>
      </div>

      {/* Key Financial Metrics Cards */}
      <div className="grid grid-cols-3 gap-[var(--premium-space-sm)]">
        {/* Total Income */}
        <div
          className="
            p-[var(--premium-space-md)]
            rounded-[var(--premium-radius-lg)]
            bg-[var(--premium-emerald)]/10
            border border-[var(--premium-emerald)]/20
            text-center
          "
        >
          <div className="flex items-center justify-center gap-[4px] mb-[4px]">
            <TrendingUp size={14} className="text-[var(--premium-emerald)]" />
            <p className="body-xs text-[var(--premium-emerald)] uppercase tracking-wide font-medium">
              Income
            </p>
          </div>
          <p className="heading-lg text-[var(--premium-emerald)]">
            ${totalIncome.toLocaleString('en-US', { minimumFractionDigits: 0, maximumFractionDigits: 0 })}
          </p>
        </div>

        {/* Total Expenses */}
        <div
          className="
            p-[var(--premium-space-md)]
            rounded-[var(--premium-radius-lg)]
            bg-[#f5576c]/10
            border border-[#f5576c]/20
            text-center
          "
        >
          <div className="flex items-center justify-center gap-[4px] mb-[4px]">
            <TrendingDown size={14} className="text-[#f5576c]" />
            <p className="body-xs text-[#f5576c] uppercase tracking-wide font-medium">
              Expenses
            </p>
          </div>
          <p className="heading-lg text-[#f5576c]">
            ${totalExpense.toLocaleString('en-US', { minimumFractionDigits: 0, maximumFractionDigits: 0 })}
          </p>
        </div>

        {/* Net Savings */}
        <div
          className={`
            p-[var(--premium-space-md)]
            rounded-[var(--premium-radius-lg)]
            text-center
            ${
              netSavings >= 0
                ? 'bg-[var(--premium-emerald)]/10 border border-[var(--premium-emerald)]/20'
                : 'bg-[#f5576c]/10 border border-[#f5576c]/20'
            }
          `}
        >
          <div className="flex items-center justify-center gap-[4px] mb-[4px]">
            <Wallet size={14} className={netSavings >= 0 ? 'text-[var(--premium-emerald)]' : 'text-[#f5576c]'} />
            <p
              className={`
                body-xs uppercase tracking-wide font-medium
                ${netSavings >= 0 ? 'text-[var(--premium-emerald)]' : 'text-[#f5576c]'}
              `}
            >
              Savings
            </p>
          </div>
          <p className={`heading-lg ${netSavings >= 0 ? 'text-[var(--premium-emerald)]' : 'text-[#f5576c]'}`}>
            {netSavings >= 0 ? '+' : '-'}${Math.abs(netSavings).toLocaleString('en-US', {
              minimumFractionDigits: 0,
              maximumFractionDigits: 0,
            })}
          </p>
        </div>
      </div>

      {/* Expense/Income Toggle & Donut Chart */}
      <div
        className="
          p-[var(--premium-space-lg)]
          rounded-[var(--premium-radius-lg)]
          bg-[var(--premium-surface-2)]
          border border-[var(--premium-glass-border)]
        "
      >
        {/* Toggle Header */}
        <div className="flex items-center justify-between mb-[var(--premium-space-lg)]">
          <h3 className="heading-md text-[var(--premium-text-primary)]">
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
                ${
                  analyticsMode === 'expense'
                    ? 'bg-[#f5576c] text-white shadow-[0_2px_8px_rgba(245,87,108,0.3)]'
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
                ${
                  analyticsMode === 'income'
                    ? 'bg-[var(--premium-emerald)] text-white shadow-[0_2px_8px_rgba(16,185,129,0.3)]'
                    : 'text-[var(--premium-text-secondary)]'
                }
              `}
            >
              Income
            </button>
          </div>
        </div>

        {/* Chart and Legend */}
        {categoryData.length > 0 ? (
          <div className="flex flex-col md:flex-row gap-[var(--premium-space-lg)] items-center">
            {/* Donut Chart */}
            <div className="flex-shrink-0">
              <ResponsiveContainer width={250} height={250}>
                <PieChart>
                  <Pie
                    data={categoryData}
                    cx="50%"
                    cy="50%"
                    innerRadius={70}
                    outerRadius={100}
                    paddingAngle={2}
                    dataKey="amount"
                  >
                    {categoryData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} />
                    ))}
                  </Pie>
                  <Tooltip content={<CustomTooltip />} />
                </PieChart>
              </ResponsiveContainer>

              {/* Center Label */}
              <div className="text-center -mt-[160px] pointer-events-none">
                <p className="body-xs text-[var(--premium-text-tertiary)] mb-[4px]">
                  Total {analyticsMode === 'expense' ? 'Expenses' : 'Income'}
                </p>
                <p className={`heading-lg font-bold ${analyticsMode === 'expense' ? 'text-[#f5576c]' : 'text-[var(--premium-emerald)]'}`}>
                  ${(analyticsMode === 'expense' ? totalExpense : totalIncome).toLocaleString('en-US', {
                    minimumFractionDigits: 0,
                    maximumFractionDigits: 0,
                  })}
                </p>
              </div>
            </div>

            {/* Legend */}
            <div className="flex-1 w-full">
              <div className="space-y-[var(--premium-space-sm)]">
                {categoryData.map((item, index) => (
                  <div
                    key={item.category}
                    className="
                      flex items-center justify-between
                      p-[var(--premium-space-sm)]
                      rounded-[var(--premium-radius-md)]
                      bg-[var(--premium-surface-3)]
                      hover:bg-[var(--premium-surface-4)]
                      transition-all duration-200
                    "
                    style={{
                      animationDelay: `${index * 50}ms`,
                    }}
                  >
                    <div className="flex items-center gap-[var(--premium-space-sm)] flex-1 min-w-0">
                      {/* Color Indicator */}
                      <div
                        className="w-[12px] h-[12px] rounded-full flex-shrink-0"
                        style={{ backgroundColor: item.color }}
                      />

                      {/* Category Label */}
                      <div className="flex-1 min-w-0">
                        <p className="body-sm font-medium text-[var(--premium-text-primary)] truncate">
                          {item.label}
                        </p>
                        <p className="body-xs text-[var(--premium-text-tertiary)]">
                          {item.count} transaction{item.count !== 1 ? 's' : ''}
                        </p>
                      </div>
                    </div>

                    {/* Amount and Percentage */}
                    <div className="text-right flex-shrink-0">
                      <p className="body-md font-semibold text-[var(--premium-text-primary)]">
                        ${item.amount.toLocaleString('en-US', { minimumFractionDigits: 0, maximumFractionDigits: 0 })}
                      </p>
                      <p className="body-xs text-[var(--premium-text-tertiary)]">
                        {item.percentage.toFixed(1)}%
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        ) : (
          <div className="text-center py-[var(--premium-space-4xl)]">
            <p className="body-md text-[var(--premium-text-tertiary)]">
              No {analyticsMode} data for this month
            </p>
          </div>
        )}
      </div>

      {/* Weekly Trends Bar Chart */}
      <div
        className="
          p-[var(--premium-space-lg)]
          rounded-[var(--premium-radius-lg)]
          bg-[var(--premium-surface-2)]
          border border-[var(--premium-glass-border)]
        "
      >
        <h3 className="heading-md text-[var(--premium-text-primary)] mb-[var(--premium-space-lg)]">
          Weekly Trends
        </h3>

        {weeklyData.length > 0 ? (
          <ResponsiveContainer width="100%" height={280}>
            <BarChart data={weeklyData}>
              <CartesianGrid strokeDasharray="3 3" stroke="var(--premium-glass-border)" />
              <XAxis
                dataKey="week"
                stroke="var(--premium-text-tertiary)"
                style={{ fontSize: '12px' }}
              />
              <YAxis
                stroke="var(--premium-text-tertiary)"
                style={{ fontSize: '12px' }}
                tickFormatter={(value) => `$${value}`}
              />
              <Tooltip content={<CustomBarTooltip />} />
              <Bar dataKey="income" fill="#10b981" radius={[8, 8, 0, 0]} />
              <Bar dataKey="expense" fill="#f5576c" radius={[8, 8, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        ) : (
          <div className="text-center py-[var(--premium-space-4xl)]">
            <p className="body-md text-[var(--premium-text-tertiary)]">
              No data available
            </p>
          </div>
        )}
      </div>

      {/* Two Column Layout: Top Categories + Account Distribution */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-[var(--premium-space-md)]">
        {/* Top 5 Categories */}
        <div
          className="
            p-[var(--premium-space-lg)]
            rounded-[var(--premium-radius-lg)]
            bg-[var(--premium-surface-2)]
            border border-[var(--premium-glass-border)]
          "
        >
          <h3 className="heading-md text-[var(--premium-text-primary)] mb-[var(--premium-space-md)]">
            Top 5 Categories
          </h3>

          <div className="space-y-[var(--premium-space-sm)]">
            {topCategories.length > 0 ? (
              topCategories.map((item, index) => (
                <div
                  key={item.category}
                  className="
                    flex items-center gap-[var(--premium-space-sm)]
                    p-[var(--premium-space-sm)]
                    rounded-[var(--premium-radius-md)]
                    bg-[var(--premium-surface-3)]
                  "
                >
                  {/* Rank */}
                  <div
                    className="
                      w-[28px] h-[28px]
                      rounded-full
                      flex items-center justify-center
                      body-sm font-bold
                    "
                    style={{
                      backgroundColor: `${item.color}20`,
                      color: item.color,
                    }}
                  >
                    {index + 1}
                  </div>

                  {/* Category Info */}
                  <div className="flex-1 min-w-0">
                    <p className="body-sm font-medium text-[var(--premium-text-primary)] truncate">
                      {item.label}
                    </p>
                    <p className="body-xs text-[var(--premium-text-tertiary)]">
                      {item.percentage.toFixed(1)}% of total
                    </p>
                  </div>

                  {/* Amount */}
                  <p className="body-md font-semibold text-[var(--premium-text-primary)]">
                    ${item.amount.toLocaleString('en-US', { minimumFractionDigits: 0, maximumFractionDigits: 0 })}
                  </p>
                </div>
              ))
            ) : (
              <p className="text-center py-[var(--premium-space-lg)] body-sm text-[var(--premium-text-tertiary)]">
                No category data
              </p>
            )}
          </div>
        </div>

        {/* Account Distribution */}
        <div
          className="
            p-[var(--premium-space-lg)]
            rounded-[var(--premium-radius-lg)]
            bg-[var(--premium-surface-2)]
            border border-[var(--premium-glass-border)]
          "
        >
          <h3 className="heading-md text-[var(--premium-text-primary)] mb-[var(--premium-space-md)]">
            Account Usage
          </h3>

          <div className="space-y-[var(--premium-space-sm)]">
            {accountData.length > 0 ? (
              accountData.map((account, index) => (
                <div
                  key={account.accountId}
                  className="
                    p-[var(--premium-space-sm)]
                    rounded-[var(--premium-radius-md)]
                    bg-[var(--premium-surface-3)]
                  "
                >
                  <div className="flex items-center justify-between mb-[8px]">
                    <div className="flex items-center gap-[var(--premium-space-sm)]">
                      {/* Account Icon */}
                      <div
                        className="
                          w-[32px] h-[32px]
                          rounded-full
                          flex items-center justify-center
                          bg-[var(--premium-emerald)]/20
                        "
                      >
                        {index === 0 ? (
                          <Wallet size={16} className="text-[var(--premium-emerald)]" />
                        ) : (
                          <CreditCard size={16} className="text-[var(--premium-emerald)]" />
                        )}
                      </div>

                      {/* Account Name */}
                      <div>
                        <p className="body-sm font-medium text-[var(--premium-text-primary)]">
                          {account.accountLabel}
                        </p>
                        <p className="body-xs text-[var(--premium-text-tertiary)]">
                          {account.transactionCount} transaction{account.transactionCount !== 1 ? 's' : ''}
                        </p>
                      </div>
                    </div>

                    {/* Amount */}
                    <div className="text-right">
                      <p className="body-md font-semibold text-[var(--premium-text-primary)]">
                        ${account.amount.toLocaleString('en-US', {
                          minimumFractionDigits: 0,
                          maximumFractionDigits: 0,
                        })}
                      </p>
                      <p className="body-xs text-[var(--premium-text-tertiary)]">
                        {account.percentage.toFixed(1)}%
                      </p>
                    </div>
                  </div>

                  {/* Progress Bar */}
                  <div className="h-[4px] bg-[var(--premium-surface-4)] rounded-full overflow-hidden">
                    <div
                      className="h-full bg-[var(--premium-emerald)] rounded-full transition-all duration-500"
                      style={{ width: `${account.percentage}%` }}
                    />
                  </div>
                </div>
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
 * Analyze category data for donut chart
 */
function analyzeCategoryData(
  transactions: Transaction[],
  mode: AnalyticsMode
): {
  categoryData: CategoryData[];
  totalIncome: number;
  totalExpense: number;
  netSavings: number;
} {
  const filteredTransactions = transactions.filter((t) => t.type === mode);

  // Group by category
  const categoryMap = new Map<TransactionCategory, { amount: number; count: number }>();

  filteredTransactions.forEach((txn) => {
    const category = txn.category || 'other';
    const existing = categoryMap.get(category) || { amount: 0, count: 0 };
    categoryMap.set(category, {
      amount: existing.amount + txn.amount,
      count: existing.count + 1,
    });
  });

  // Calculate total
  const total = Array.from(categoryMap.values()).reduce((sum, val) => sum + val.amount, 0);

  // Convert to array and calculate percentages
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

  // Calculate totals
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

  // Get first and last day of month
  const firstDay = new Date(year, month, 1);
  const lastDay = new Date(year, month + 1, 0);

  // Generate weeks
  const weeks: WeeklyData[] = [];
  let currentWeekStart = new Date(firstDay);
  let weekNumber = 1;

  while (currentWeekStart <= lastDay) {
    const currentWeekEnd = new Date(currentWeekStart);
    currentWeekEnd.setDate(currentWeekEnd.getDate() + 6);

    const weekEnd = currentWeekEnd > lastDay ? lastDay : currentWeekEnd;

    // Filter transactions for this week
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
      week: `Week ${weekNumber}`,
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
  mode: AnalyticsMode
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
          p-[var(--premium-space-sm)]
          rounded-[var(--premium-radius-md)]
          bg-[var(--premium-surface-2)]
          border border-[var(--premium-glass-border)]
          shadow-[var(--premium-shadow-lg)]
        "
      >
        <p className="body-sm font-medium text-[var(--premium-text-primary)] mb-[4px]">
          {data.label}
        </p>
        <p className="body-md font-bold text-[var(--premium-text-primary)]">
          ${data.amount.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
        </p>
        <p className="body-xs text-[var(--premium-text-tertiary)]">
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
          p-[var(--premium-space-sm)]
          rounded-[var(--premium-radius-md)]
          bg-[var(--premium-surface-2)]
          border border-[var(--premium-glass-border)]
          shadow-[var(--premium-shadow-lg)]
        "
      >
        <p className="body-sm font-medium text-[var(--premium-text-primary)] mb-[8px]">
          {payload[0].payload.week}
        </p>
        <div className="space-y-[4px]">
          <div className="flex items-center justify-between gap-[var(--premium-space-md)]">
            <span className="body-xs text-[var(--premium-emerald)]">Income:</span>
            <span className="body-sm font-semibold text-[var(--premium-emerald)]">
              ${payload[0].value.toLocaleString('en-US', { minimumFractionDigits: 0 })}
            </span>
          </div>
          <div className="flex items-center justify-between gap-[var(--premium-space-md)]">
            <span className="body-xs text-[#f5576c]">Expense:</span>
            <span className="body-sm font-semibold text-[#f5576c]">
              ${payload[1].value.toLocaleString('en-US', { minimumFractionDigits: 0 })}
            </span>
          </div>
        </div>
      </div>
    );
  }
  return null;
}
