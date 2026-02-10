/**
 * Premium Advanced Statistics Screen
 * Comprehensive financial analytics with Wallet-style complexity and Mizan glassmorphism
 * 
 * @features
 * - Cash Flow Barometer (Income vs Expense ratio)
 * - Net Worth Trend (Area chart)
 * - Spending by Category (Donut chart with top 3 breakdown)
 * - Daily Average & Frequency widgets
 * - Merchant/Description breakdown
 * - Time-frame selector (Day/Week/Month/Year/Custom)
 * - Account filter
 * 
 * @architecture Material 3 with glassmorphism, recharts for data visualization
 * @design Deep dark blue/purple (#1A1A2E) background, 8dp grid system
 */

import { useState } from 'react';
import {
  ChevronLeft,
  TrendingUp,
  TrendingDown,
  PiggyBank,
  Calendar,
  Filter,
  Receipt,
  Store,
  Wallet,
  ShoppingBag,
  Car,
  Home,
  Utensils,
  Coffee,
} from 'lucide-react';
import {
  AreaChart,
  Area,
  PieChart,
  Pie,
  Cell,
  ResponsiveContainer,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
} from 'recharts';

/**
 * Time Frame Type
 */
export type TimeFrame = 'day' | 'week' | 'month' | 'year' | 'custom';

/**
 * Category Spending Interface
 */
export interface CategorySpending {
  id: string;
  name: string;
  amount: number;
  percentage: number;
  color: string;
  icon: typeof Wallet;
  transactionCount: number;
}

/**
 * Merchant Spending Interface
 */
export interface MerchantSpending {
  id: string;
  name: string;
  amount: number;
  transactionCount: number;
  lastTransactionDate: string;
}

/**
 * Net Worth Data Point
 */
export interface NetWorthDataPoint {
  date: string;
  balance: number;
}

/**
 * Statistics Summary Interface
 */
export interface StatisticsSummary {
  totalIncome: number;
  totalExpense: number;
  savingsRate: number; // percentage
  netWorth: number;
  averageDailyExpense: number;
  transactionCount: number;
  categoryBreakdown: CategorySpending[];
  merchantBreakdown: MerchantSpending[];
  netWorthTrend: NetWorthDataPoint[];
}

export interface PremiumAdvancedStatisticsScreenProps {
  /** Callback when back button is pressed */
  onBack: () => void;
  /** Callback when filter button is pressed */
  onOpenFilter?: () => void;
}

// Mock Data
const MOCK_STATISTICS: StatisticsSummary = {
  totalIncome: 15000000,
  totalExpense: 11250000,
  savingsRate: 25,
  netWorth: 3750000,
  averageDailyExpense: 375000,
  transactionCount: 87,
  categoryBreakdown: [
    {
      id: 'food',
      name: 'Food & Dining',
      amount: 3500000,
      percentage: 31,
      color: '#10b981',
      icon: Utensils,
      transactionCount: 28,
    },
    {
      id: 'transport',
      name: 'Transportation',
      amount: 2800000,
      percentage: 25,
      color: '#667eea',
      icon: Car,
      transactionCount: 18,
    },
    {
      id: 'shopping',
      name: 'Shopping',
      amount: 2250000,
      percentage: 20,
      color: '#f5576c',
      icon: ShoppingBag,
      transactionCount: 15,
    },
    {
      id: 'housing',
      name: 'Housing',
      amount: 1700000,
      percentage: 15,
      color: '#4facfe',
      icon: Home,
      transactionCount: 8,
    },
    {
      id: 'entertainment',
      name: 'Entertainment',
      amount: 1000000,
      percentage: 9,
      color: '#c471f5',
      icon: Coffee,
      transactionCount: 18,
    },
  ],
  merchantBreakdown: [
    { id: 'm1', name: 'Korzinka', amount: 1850000, transactionCount: 12, lastTransactionDate: '2026-02-08' },
    { id: 'm2', name: 'Starbucks', amount: 980000, transactionCount: 14, lastTransactionDate: '2026-02-09' },
    { id: 'm3', name: 'Yandex Taxi', amount: 750000, transactionCount: 22, lastTransactionDate: '2026-02-08' },
    { id: 'm4', name: 'Steam', amount: 620000, transactionCount: 3, lastTransactionDate: '2026-02-05' },
    { id: 'm5', name: 'Netflix', amount: 120000, transactionCount: 1, lastTransactionDate: '2026-02-01' },
  ],
  netWorthTrend: [
    { date: 'Feb 1', balance: 500000 },
    { date: 'Feb 3', balance: 850000 },
    { date: 'Feb 5', balance: 1200000 },
    { date: 'Feb 7', balance: 2100000 },
    { date: 'Feb 9', balance: 3750000 },
  ],
};

/**
 * Format number with spaces: "12 345 678"
 */
function formatUZS(amount: number): string {
  return Math.floor(Math.abs(amount))
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ' ');
}

/**
 * Time Frame Chip Component
 */
function TimeFrameChip({
  label,
  isSelected,
  onClick,
}: {
  label: string;
  isSelected: boolean;
  onClick: () => void;
}) {
  return (
    <button
      onClick={onClick}
      className={`
        px-[16px] py-[8px]
        rounded-[var(--premium-radius-lg)]
        transition-all duration-200
        active:scale-95
        whitespace-nowrap
        ${
          isSelected
            ? 'bg-[#10B981] text-white font-semibold'
            : 'bg-white/5 text-white/60 font-medium hover:bg-white/10'
        }
      `}
    >
      <span className="body-sm">{label}</span>
    </button>
  );
}

/**
 * Custom Tooltip for Area Chart
 */
function CustomAreaTooltip({ active, payload }: any) {
  if (active && payload && payload.length) {
    return (
      <div
        className="
          px-[12px] py-[8px]
          rounded-[var(--premium-radius-lg)]
          bg-white/10
          backdrop-blur-xl
          border border-white/20
        "
      >
        <p className="body-xs text-white/60 mb-[2px]">{payload[0].payload.date}</p>
        <p className="body-sm font-semibold text-white">
          {formatUZS(payload[0].value)} UZS
        </p>
      </div>
    );
  }
  return null;
}

/**
 * Custom Label for Donut Chart
 */
function CustomDonutLabel({ cx, cy, totalExpense }: any) {
  return (
    <g>
      <text x={cx} y={cy - 10} textAnchor="middle" dominantBaseline="middle" className="fill-white/40 text-[12px]">
        Total
      </text>
      <text x={cx} y={cy + 10} textAnchor="middle" dominantBaseline="middle" className="fill-white text-[18px] font-bold">
        {formatUZS(totalExpense)}
      </text>
      <text x={cx} y={cy + 28} textAnchor="middle" dominantBaseline="middle" className="fill-white/30 text-[10px]">
        UZS
      </text>
    </g>
  );
}

/**
 * Premium Advanced Statistics Screen Component
 */
export function PremiumAdvancedStatisticsScreen({
  onBack,
  onOpenFilter,
}: PremiumAdvancedStatisticsScreenProps) {
  const [timeFrame, setTimeFrame] = useState<TimeFrame>('month');
  const [statistics] = useState<StatisticsSummary>(MOCK_STATISTICS);

  return (
    <div className="fixed inset-0 z-50 bg-[#1A1A2E] flex flex-col">
      {/* Header */}
      <div
        className="
          flex-shrink-0
          px-[var(--premium-space-lg)]
          pt-[var(--premium-space-xl)]
          pb-[var(--premium-space-md)]
        "
      >
        <div className="flex items-center justify-between mb-[var(--premium-space-md)]">
          <div className="flex items-center gap-[var(--premium-space-md)]">
            <button
              onClick={onBack}
              className="
                w-[40px] h-[40px]
                rounded-full
                bg-white/5
                backdrop-blur-xl
                border border-white/10
                hover:bg-white/10
                flex items-center justify-center
                transition-all duration-200
                active:scale-95
              "
            >
              <ChevronLeft size={20} className="text-white" />
            </button>
            <div>
              <h1 className="heading-lg text-white font-semibold">
                Statistics Hub
              </h1>
              <p className="body-sm text-white/60">
                Deep financial insights
              </p>
            </div>
          </div>

          {/* Filter Button */}
          <button
            onClick={onOpenFilter}
            className="
              w-[40px] h-[40px]
              rounded-full
              bg-white/5
              backdrop-blur-xl
              border border-white/10
              hover:bg-white/10
              flex items-center justify-center
              transition-all duration-200
              active:scale-95
            "
          >
            <Filter size={20} className="text-white" />
          </button>
        </div>

        {/* Time Frame Selector - Horizontal Scroll */}
        <div className="overflow-x-auto -mx-[var(--premium-space-lg)] px-[var(--premium-space-lg)] pb-[8px]">
          <div className="flex gap-[8px]">
            <TimeFrameChip label="Day" isSelected={timeFrame === 'day'} onClick={() => setTimeFrame('day')} />
            <TimeFrameChip label="Week" isSelected={timeFrame === 'week'} onClick={() => setTimeFrame('week')} />
            <TimeFrameChip label="Month" isSelected={timeFrame === 'month'} onClick={() => setTimeFrame('month')} />
            <TimeFrameChip label="Year" isSelected={timeFrame === 'year'} onClick={() => setTimeFrame('year')} />
            <TimeFrameChip label="Custom" isSelected={timeFrame === 'custom'} onClick={() => setTimeFrame('custom')} />
          </div>
        </div>
      </div>

      {/* Content - Scrollable */}
      <div className="flex-1 overflow-y-auto px-[var(--premium-space-lg)] pb-[var(--premium-space-2xl)]">
        <div className="space-y-[var(--premium-space-xl)]">
          {/* Module 1: Cash Flow Barometer */}
          <div
            className="
              p-[var(--premium-space-xl)]
              rounded-[var(--premium-radius-xl)]
              bg-white/5
              backdrop-blur-xl
              border border-white/10
            "
          >
            <h3 className="heading-sm text-white font-semibold mb-[var(--premium-space-md)]">
              Cash Flow Barometer
            </h3>

            {/* Income vs Expense Row */}
            <div className="flex items-center justify-between mb-[var(--premium-space-md)]">
              <div className="flex items-center gap-[8px]">
                <TrendingUp size={16} className="text-[#10B981]" />
                <span className="body-sm text-white/80">Income</span>
              </div>
              <span className="body-sm font-semibold text-[#10B981]">
                {formatUZS(statistics.totalIncome)} UZS
              </span>
            </div>

            <div className="flex items-center justify-between mb-[var(--premium-space-md)]">
              <div className="flex items-center gap-[8px]">
                <TrendingDown size={16} className="text-[#F43F5E]" />
                <span className="body-sm text-white/80">Expense</span>
              </div>
              <span className="body-sm font-semibold text-[#F43F5E]">
                {formatUZS(statistics.totalExpense)} UZS
              </span>
            </div>

            {/* Horizontal Bar */}
            <div className="h-[40px] w-full bg-white/5 rounded-full overflow-hidden flex mb-[var(--premium-space-md)]">
              <div
                className="h-full bg-gradient-to-r from-[#10B981] to-[#10B981]/80 flex items-center justify-center"
                style={{ width: `${(statistics.totalIncome / (statistics.totalIncome + statistics.totalExpense)) * 100}%` }}
              >
                {statistics.totalIncome > statistics.totalExpense * 0.3 && (
                  <span className="body-xs font-bold text-white">Income</span>
                )}
              </div>
              <div
                className="h-full bg-gradient-to-r from-[#F43F5E]/80 to-[#F43F5E] flex items-center justify-center"
                style={{ width: `${(statistics.totalExpense / (statistics.totalIncome + statistics.totalExpense)) * 100}%` }}
              >
                {statistics.totalExpense > statistics.totalIncome * 0.3 && (
                  <span className="body-xs font-bold text-white">Expense</span>
                )}
              </div>
            </div>

            {/* Insight Text */}
            <div
              className="
                p-[12px]
                rounded-[var(--premium-radius-lg)]
                bg-[#10B981]/10
                border border-[#10B981]/20
              "
            >
              <p className="body-sm text-white/80 text-center">
                You've saved <span className="font-bold text-[#10B981]">{statistics.savingsRate}%</span> of your income this {timeFrame}
              </p>
            </div>
          </div>

          {/* Module 2: Net Worth Trend (Area Chart) */}
          <div
            className="
              p-[var(--premium-space-xl)]
              rounded-[var(--premium-radius-xl)]
              bg-white/5
              backdrop-blur-xl
              border border-white/10
            "
          >
            <div className="flex items-center justify-between mb-[var(--premium-space-md)]">
              <h3 className="heading-sm text-white font-semibold">
                Net Worth Trend
              </h3>
              <div className="flex items-center gap-[6px] px-[10px] py-[4px] rounded-full bg-[#0EA5E9]/20 border border-[#0EA5E9]/30">
                <TrendingUp size={14} className="text-[#0EA5E9]" />
                <span className="body-xs font-bold text-[#0EA5E9]">+{statistics.savingsRate}%</span>
              </div>
            </div>

            {/* Current Net Worth */}
            <div className="mb-[var(--premium-space-lg)]">
              <p className="body-xs text-white/40 mb-[4px]">Current Balance</p>
              <div className="flex items-baseline gap-[4px]">
                <span className="text-[28px] font-bold text-white">
                  {formatUZS(statistics.netWorth)}
                </span>
                <span className="text-[14px] font-medium text-white/40">UZS</span>
              </div>
            </div>

            {/* Area Chart */}
            <div className="h-[180px] -mx-[8px]">
              <ResponsiveContainer width="100%" height="100%">
                <AreaChart data={statistics.netWorthTrend}>
                  <defs>
                    <linearGradient id="colorBalance" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#0EA5E9" stopOpacity={0.3} />
                      <stop offset="95%" stopColor="#0EA5E9" stopOpacity={0} />
                    </linearGradient>
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.05)" />
                  <XAxis
                    dataKey="date"
                    stroke="rgba(255,255,255,0.3)"
                    style={{ fontSize: '10px', fill: 'rgba(255,255,255,0.4)' }}
                    tickLine={false}
                  />
                  <YAxis
                    stroke="rgba(255,255,255,0.3)"
                    style={{ fontSize: '10px', fill: 'rgba(255,255,255,0.4)' }}
                    tickLine={false}
                    tickFormatter={(value) => `${(value / 1000000).toFixed(1)}M`}
                  />
                  <Tooltip content={<CustomAreaTooltip />} />
                  <Area
                    type="monotone"
                    dataKey="balance"
                    stroke="#0EA5E9"
                    strokeWidth={3}
                    fill="url(#colorBalance)"
                  />
                </AreaChart>
              </ResponsiveContainer>
            </div>
          </div>

          {/* Module 3: Spending by Category (Donut Chart) */}
          <div
            className="
              p-[var(--premium-space-xl)]
              rounded-[var(--premium-radius-xl)]
              bg-white/5
              backdrop-blur-xl
              border border-white/10
            "
          >
            <h3 className="heading-sm text-white font-semibold mb-[var(--premium-space-lg)]">
              Spending by Category
            </h3>

            {/* Donut Chart */}
            <div className="h-[220px] flex items-center justify-center mb-[var(--premium-space-lg)]">
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={statistics.categoryBreakdown}
                    cx="50%"
                    cy="50%"
                    innerRadius={60}
                    outerRadius={90}
                    paddingAngle={2}
                    dataKey="amount"
                    label={false}
                  >
                    {statistics.categoryBreakdown.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} />
                    ))}
                  </Pie>
                  <Tooltip
                    content={({ active, payload }) => {
                      if (active && payload && payload.length) {
                        return (
                          <div className="px-[12px] py-[8px] rounded-[var(--premium-radius-lg)] bg-white/10 backdrop-blur-xl border border-white/20">
                            <p className="body-xs text-white/60 mb-[2px]">{payload[0].payload.name}</p>
                            <p className="body-sm font-semibold text-white">
                              {formatUZS(payload[0].value)} UZS
                            </p>
                            <p className="body-xs text-white/40">{payload[0].payload.percentage}%</p>
                          </div>
                        );
                      }
                      return null;
                    }}
                  />
                  {/* Center Label */}
                  <text x="50%" y="50%" textAnchor="middle" dominantBaseline="middle">
                    <tspan x="50%" dy="-10" className="fill-white/40 text-[12px]">Total</tspan>
                    <tspan x="50%" dy="20" className="fill-white text-[18px] font-bold">{formatUZS(statistics.totalExpense)}</tspan>
                    <tspan x="50%" dy="18" className="fill-white/30 text-[10px]">UZS</tspan>
                  </text>
                </PieChart>
              </ResponsiveContainer>
            </div>

            {/* Top 3 Categories */}
            <div className="space-y-[8px]">
              <h4 className="body-sm text-white/60 mb-[8px]">Top 3 Categories</h4>
              {statistics.categoryBreakdown.slice(0, 3).map((category, index) => {
                const Icon = category.icon;
                return (
                  <div
                    key={category.id}
                    className="
                      p-[12px]
                      rounded-[var(--premium-radius-lg)]
                      bg-white/5
                      border border-white/10
                      flex items-center gap-[12px]
                    "
                  >
                    {/* Rank Badge */}
                    <div
                      className="
                        w-[28px] h-[28px]
                        rounded-full
                        flex items-center justify-center
                        font-bold text-[12px]
                      "
                      style={{
                        backgroundColor: `${category.color}20`,
                        color: category.color,
                      }}
                    >
                      {index + 1}
                    </div>

                    {/* Category Icon */}
                    <div
                      className="
                        w-[36px] h-[36px]
                        rounded-[var(--premium-radius-md)]
                        flex items-center justify-center
                      "
                      style={{
                        backgroundColor: `${category.color}20`,
                        border: `1px solid ${category.color}30`,
                      }}
                    >
                      <Icon size={18} style={{ color: category.color }} />
                    </div>

                    {/* Category Info */}
                    <div className="flex-1">
                      <p className="body-sm font-medium text-white">{category.name}</p>
                      <p className="body-xs text-white/40">
                        {category.transactionCount} transactions
                      </p>
                    </div>

                    {/* Amount & Percentage */}
                    <div className="text-right">
                      <p className="body-sm font-semibold text-white">
                        {formatUZS(category.amount)}
                      </p>
                      <p className="body-xs" style={{ color: category.color }}>
                        {category.percentage}%
                      </p>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>

          {/* Module 4: Daily Average & Frequency */}
          <div className="grid grid-cols-2 gap-[var(--premium-space-md)]">
            {/* Average Daily Expense */}
            <div
              className="
                p-[var(--premium-space-lg)]
                rounded-[var(--premium-radius-xl)]
                bg-white/5
                backdrop-blur-xl
                border border-white/10
              "
            >
              <div
                className="
                  w-[40px] h-[40px]
                  rounded-[var(--premium-radius-lg)]
                  bg-[#F43F5E]/20
                  border border-[#F43F5E]/30
                  flex items-center justify-center
                  mb-[12px]
                "
              >
                <Calendar size={20} className="text-[#F43F5E]" />
              </div>
              <p className="body-xs text-white/40 mb-[4px]">Daily Average</p>
              <p className="text-[20px] font-bold text-white mb-[2px]">
                {formatUZS(statistics.averageDailyExpense)}
              </p>
              <p className="body-xs text-white/30">UZS per day</p>
            </div>

            {/* Transaction Count */}
            <div
              className="
                p-[var(--premium-space-lg)]
                rounded-[var(--premium-radius-xl)]
                bg-white/5
                backdrop-blur-xl
                border border-white/10
              "
            >
              <div
                className="
                  w-[40px] h-[40px]
                  rounded-[var(--premium-radius-lg)]
                  bg-[#10B981]/20
                  border border-[#10B981]/30
                  flex items-center justify-center
                  mb-[12px]
                "
              >
                <Receipt size={20} className="text-[#10B981]" />
              </div>
              <p className="body-xs text-white/40 mb-[4px]">Total Transactions</p>
              <p className="text-[20px] font-bold text-white mb-[2px]">
                {statistics.transactionCount}
              </p>
              <p className="body-xs text-white/30">this {timeFrame}</p>
            </div>
          </div>

          {/* Module 5: Merchant/Description Breakdown */}
          <div
            className="
              p-[var(--premium-space-xl)]
              rounded-[var(--premium-radius-xl)]
              bg-white/5
              backdrop-blur-xl
              border border-white/10
            "
          >
            <div className="flex items-center gap-[8px] mb-[var(--premium-space-lg)]">
              <Store size={20} className="text-white/60" />
              <h3 className="heading-sm text-white font-semibold">
                Top Merchants
              </h3>
            </div>

            <div className="space-y-[8px]">
              {statistics.merchantBreakdown.map((merchant, index) => (
                <div
                  key={merchant.id}
                  className="
                    p-[12px]
                    rounded-[var(--premium-radius-lg)]
                    bg-white/5
                    border border-white/10
                    hover:bg-white/10
                    transition-all duration-200
                  "
                >
                  <div className="flex items-center gap-[12px]">
                    {/* Rank */}
                    <div className="w-[24px] text-center">
                      <span className="body-sm font-bold text-white/40">
                        {index + 1}
                      </span>
                    </div>

                    {/* Merchant Icon */}
                    <div
                      className="
                        w-[40px] h-[40px]
                        rounded-full
                        bg-white/10
                        flex items-center justify-center
                      "
                    >
                      <Store size={18} className="text-white/60" />
                    </div>

                    {/* Merchant Info */}
                    <div className="flex-1">
                      <p className="body-md font-medium text-white">
                        {merchant.name}
                      </p>
                      <p className="body-xs text-white/40">
                        {merchant.transactionCount} transactions
                      </p>
                    </div>

                    {/* Amount */}
                    <div className="text-right">
                      <p className="body-sm font-semibold text-white">
                        {formatUZS(merchant.amount)}
                      </p>
                      <p className="body-xs text-white/30">UZS</p>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
