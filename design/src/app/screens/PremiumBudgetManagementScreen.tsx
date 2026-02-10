/**
 * Premium Budget Management Screen
 * Track spending limits by category with visual progress indicators
 * 
 * @features
 * - Total budget overview with circular progress ring
 * - Category-wise budget tracking with utilization bars
 * - Color-coded warnings (green/orange/red)
 * - Glassmorphism design matching Mizan aesthetic
 * 
 * @architecture Material 3 with glassmorphism
 * @design Deep dark blue/purple (#1A1A2E) background, 8dp grid system
 */

import { useState } from 'react';
import {
  ChevronLeft,
  Plus,
  Wallet,
  ShoppingBag,
  Car,
  Home,
  Utensils,
  Coffee,
  Heart,
  Smartphone,
  PiggyBank,
  AlertTriangle,
} from 'lucide-react';

/**
 * Budget Category Interface
 */
export interface BudgetCategory {
  id: string;
  categoryId: string;
  categoryName: string;
  categoryIcon: typeof Wallet;
  categoryColor: string;
  limit: number; // in UZS
  spent: number; // in UZS
  period: 'monthly';
}

/**
 * Budget State Interface
 */
export interface BudgetState {
  budgets: BudgetCategory[];
  totalBudget: number;
  totalSpent: number;
  utilizationPercent: number;
}

export interface PremiumBudgetManagementScreenProps {
  /** Callback when back button is pressed */
  onBack: () => void;
  /** Callback when user wants to create a new budget */
  onNavigateToCreateBudget?: () => void;
  /** Callback when user wants to edit a budget */
  onEditBudget?: (budgetId: string) => void;
}

// Mock Budget Data
const MOCK_BUDGET_DATA: BudgetCategory[] = [
  {
    id: 'budget-1',
    categoryId: 'food-dining',
    categoryName: 'Food & Dining',
    categoryIcon: Utensils,
    categoryColor: '#10b981',
    limit: 800000,
    spent: 520000,
    period: 'monthly',
  },
  {
    id: 'budget-2',
    categoryId: 'transportation',
    categoryName: 'Transportation',
    categoryIcon: Car,
    categoryColor: '#667eea',
    limit: 500000,
    spent: 480000,
    period: 'monthly',
  },
  {
    id: 'budget-3',
    categoryId: 'shopping',
    categoryName: 'Shopping',
    categoryIcon: ShoppingBag,
    categoryColor: '#f5576c',
    limit: 1200000,
    spent: 1350000,
    period: 'monthly',
  },
  {
    id: 'budget-4',
    categoryId: 'housing',
    categoryName: 'Housing',
    categoryIcon: Home,
    categoryColor: '#4facfe',
    limit: 2000000,
    spent: 1200000,
    period: 'monthly',
  },
  {
    id: 'budget-5',
    categoryId: 'entertainment',
    categoryName: 'Entertainment',
    categoryIcon: Coffee,
    categoryColor: '#c471f5',
    limit: 500000,
    spent: 200000,
    period: 'monthly',
  },
];

/**
 * Calculate budget state from budget data
 */
function calculateBudgetState(budgets: BudgetCategory[]): BudgetState {
  const totalBudget = budgets.reduce((sum, budget) => sum + budget.limit, 0);
  const totalSpent = budgets.reduce((sum, budget) => sum + budget.spent, 0);
  const utilizationPercent = totalBudget > 0 ? (totalSpent / totalBudget) * 100 : 0;

  return {
    budgets,
    totalBudget,
    totalSpent,
    utilizationPercent,
  };
}

/**
 * Format number with spaces: "5 000 000"
 */
function formatUZS(amount: number): string {
  return Math.floor(Math.abs(amount))
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ' ');
}

/**
 * Get utilization color based on percentage
 */
function getUtilizationColor(percent: number): string {
  if (percent >= 100) return '#EF4444'; // Red
  if (percent >= 80) return '#F59E0B'; // Orange
  return '#10B981'; // Emerald Green
}

/**
 * Circular Progress Ring Component
 */
function CircularProgress({ percent, size = 120, strokeWidth = 8 }: { percent: number; size?: number; strokeWidth?: number }) {
  const radius = (size - strokeWidth) / 2;
  const circumference = 2 * Math.PI * radius;
  const offset = circumference - (Math.min(percent, 100) / 100) * circumference;
  const color = getUtilizationColor(percent);

  return (
    <div className="relative" style={{ width: size, height: size }}>
      {/* Background Circle */}
      <svg width={size} height={size} className="transform -rotate-90">
        <circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          stroke="white"
          strokeOpacity="0.1"
          strokeWidth={strokeWidth}
          fill="none"
        />
        {/* Progress Circle */}
        <circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          stroke={color}
          strokeWidth={strokeWidth}
          fill="none"
          strokeDasharray={circumference}
          strokeDashoffset={offset}
          strokeLinecap="round"
          className="transition-all duration-500"
        />
      </svg>
      {/* Percentage Text */}
      <div className="absolute inset-0 flex items-center justify-center">
        <span className="text-[28px] font-bold text-white">
          {Math.round(percent)}%
        </span>
      </div>
    </div>
  );
}

/**
 * Premium Budget Management Screen Component
 */
export function PremiumBudgetManagementScreen({
  onBack,
  onNavigateToCreateBudget,
  onEditBudget,
}: PremiumBudgetManagementScreenProps) {
  const [budgets] = useState<BudgetCategory[]>(MOCK_BUDGET_DATA);
  const budgetState = calculateBudgetState(budgets);

  return (
    <div className="h-screen bg-[#1A1A2E] flex flex-col overflow-hidden">
      {/* Header */}
      <div
        className="
          flex-shrink-0
          px-[var(--premium-space-lg)]
          pt-[var(--premium-space-xl)]
          pb-[var(--premium-space-md)]
        "
      >
        <div className="flex items-center gap-[var(--premium-space-md)] mb-[var(--premium-space-md)]">
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
              Budget Manager
            </h1>
            <p className="body-sm text-white/60">
              Track your spending limits
            </p>
          </div>
        </div>
      </div>

      {/* Content - Scrollable */}
      <div className="flex-1 overflow-y-auto px-[var(--premium-space-lg)] pb-[var(--premium-space-xl)]">
        <div className="space-y-[var(--premium-space-xl)]">
          {/* Total Budget Summary Card - Premium Glass with Circular Progress */}
          <div
            className="
              relative
              p-[var(--premium-space-xl)]
              rounded-[var(--premium-radius-2xl)]
              bg-gradient-to-br from-white/10 to-white/5
              backdrop-blur-2xl
              border border-white/20
              overflow-hidden
            "
          >
            {/* Emerald Glow Effect */}
            <div
              className="
                absolute -top-[50%] -right-[20%]
                w-[200px] h-[200px]
                rounded-full
                bg-[#10B981]
                opacity-20
                blur-[80px]
              "
            />

            <div className="relative z-10">
              <div className="flex items-center justify-between mb-[var(--premium-space-lg)]">
                <div className="flex-1">
                  <p className="body-sm text-white/60 mb-[8px]">
                    Total Budget
                  </p>
                  
                  {/* Total Budget Amount */}
                  <div className="flex items-baseline gap-[4px] mb-[4px]">
                    <span className="text-[32px] font-bold text-white tracking-tight">
                      {formatUZS(budgetState.totalBudget)}
                    </span>
                    <span className="text-[16px] font-medium text-white/50 ml-[4px]">
                      UZS
                    </span>
                  </div>

                  {/* Spent Amount */}
                  <div className="flex items-baseline gap-[4px]">
                    <span className="text-[18px] font-semibold" style={{ color: getUtilizationColor(budgetState.utilizationPercent) }}>
                      {formatUZS(budgetState.totalSpent)}
                    </span>
                    <span className="text-[14px] font-medium text-white/40">
                      spent
                    </span>
                  </div>
                </div>

                {/* Circular Progress Ring */}
                <CircularProgress percent={budgetState.utilizationPercent} size={100} strokeWidth={8} />
              </div>

              {/* Remaining Budget Chip */}
              <div
                className="
                  inline-flex items-center gap-[6px]
                  px-[12px] py-[6px]
                  rounded-full
                  bg-[#10B981]/20
                  border border-[#10B981]/30
                "
              >
                <PiggyBank size={14} className="text-[#10B981]" />
                <span className="body-xs font-semibold text-[#10B981]">
                  {formatUZS(budgetState.totalBudget - budgetState.totalSpent)} UZS remaining
                </span>
              </div>
            </div>
          </div>

          {/* Budget Category List */}
          <div className="space-y-[var(--premium-space-md)]">
            {/* Section Header */}
            <div className="flex items-center justify-between px-[4px]">
              <h3 className="heading-sm text-white/90 font-semibold">
                Category Budgets
              </h3>
              <span className="body-xs text-white/40">
                {budgets.length} {budgets.length === 1 ? 'category' : 'categories'}
              </span>
            </div>

            {/* Budget Items */}
            <div className="space-y-[8px]">
              {budgets.map((budget) => {
                const Icon = budget.categoryIcon;
                const utilizationPercent = budget.limit > 0 ? (budget.spent / budget.limit) * 100 : 0;
                const color = getUtilizationColor(utilizationPercent);
                const isOverBudget = utilizationPercent > 100;

                return (
                  <button
                    key={budget.id}
                    onClick={() => onEditBudget?.(budget.id)}
                    className="
                      w-full
                      p-[var(--premium-space-md)]
                      rounded-[var(--premium-radius-xl)]
                      bg-white/5
                      backdrop-blur-xl
                      border border-white/10
                      hover:bg-white/10
                      hover:border-white/20
                      transition-all duration-200
                      active:scale-[0.98]
                    "
                  >
                    {/* Top Row: Icon, Name, Percentage */}
                    <div className="flex items-center gap-[var(--premium-space-md)] mb-[8px]">
                      {/* Category Icon */}
                      <div
                        className="
                          w-[48px] h-[48px]
                          rounded-[var(--premium-radius-lg)]
                          flex items-center justify-center
                          flex-shrink-0
                        "
                        style={{ 
                          backgroundColor: `${budget.categoryColor}20`,
                          border: `1px solid ${budget.categoryColor}30`
                        }}
                      >
                        <Icon size={24} style={{ color: budget.categoryColor }} />
                      </div>

                      {/* Category Name & Spent/Limit */}
                      <div className="flex-1 min-w-0 text-left">
                        <p className="body-md font-medium text-white truncate mb-[2px]">
                          {budget.categoryName}
                        </p>
                        <p className="body-xs text-white/60">
                          {formatUZS(budget.spent)} / {formatUZS(budget.limit)} UZS
                        </p>
                      </div>

                      {/* Percentage Badge */}
                      <div
                        className="
                          flex items-center gap-[4px]
                          px-[10px] py-[4px]
                          rounded-full
                          flex-shrink-0
                        "
                        style={{ 
                          backgroundColor: `${color}20`,
                          border: `1px solid ${color}30`
                        }}
                      >
                        {isOverBudget && <AlertTriangle size={12} style={{ color }} />}
                        <span className="body-xs font-bold" style={{ color }}>
                          {Math.round(utilizationPercent)}%
                        </span>
                      </div>
                    </div>

                    {/* Mini Progress Bar */}
                    <div className="w-full h-[4px] bg-white/10 rounded-full overflow-hidden">
                      <div
                        className="h-full rounded-full transition-all duration-500"
                        style={{ 
                          width: `${Math.min(utilizationPercent, 100)}%`,
                          backgroundColor: color
                        }}
                      />
                    </div>
                  </button>
                );
              })}
            </div>
          </div>

          {/* Add New Budget Button - Inline Dashed Style */}
          <button
            onClick={onNavigateToCreateBudget}
            className="
              w-full
              p-[var(--premium-space-md)]
              rounded-[var(--premium-radius-xl)]
              bg-transparent
              border-2 border-dashed border-white/20
              hover:border-[#10B981]/50
              hover:bg-[#10B981]/5
              transition-all duration-200
              active:scale-[0.98]
              flex items-center gap-[var(--premium-space-md)]
            "
          >
            {/* Plus Icon */}
            <div
              className="
                w-[48px] h-[48px]
                rounded-full
                bg-[#10B981]/20
                border border-[#10B981]/30
                flex items-center justify-center
                flex-shrink-0
              "
            >
              <Plus size={24} className="text-[#10B981]" strokeWidth={2.5} />
            </div>

            {/* Text */}
            <div className="flex-1 text-left">
              <p className="body-md font-medium text-white">
                Add New Budget
              </p>
              <p className="body-xs text-white/40">
                Set spending limit for a category
              </p>
            </div>
          </button>
        </div>
      </div>
    </div>
  );
}
