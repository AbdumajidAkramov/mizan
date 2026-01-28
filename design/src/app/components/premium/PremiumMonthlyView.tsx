/**
 * Premium Monthly View - Money Manager Style
 * Weekly grouping with expandable sections and swipe gestures
 * 
 * @design RealByte Money Manager UX Pattern
 * @architecture Weekly grouping, compact transaction rows
 * @interactions Expand/Collapse weeks, Swipe to Edit/Delete
 */

import { useState, useRef } from 'react';
import {
  ChevronDown,
  ChevronRight,
  ChevronLeft,
  Edit2,
  Trash2,
  TrendingUp,
  TrendingDown,
  ShoppingBag,
  Car,
  Home,
  Utensils,
  Coffee,
  Plane,
  Heart,
  Smartphone,
  Zap,
  DollarSign,
} from 'lucide-react';
import type { Transaction, TransactionCategory } from '../../../types/domain';

export interface PremiumMonthlyViewProps {
  /** Array of transactions to display */
  transactions: Transaction[];
  /** Current selected month */
  selectedMonth: Date;
  /** Callback when month changes */
  onMonthChange: (date: Date) => void;
  /** Callback when edit transaction */
  onEditTransaction?: (transaction: Transaction) => void;
  /** Callback when delete transaction */
  onDeleteTransaction?: (transactionId: string) => void;
  /** Callback when transaction is tapped */
  onTransactionTap?: (transaction: Transaction) => void;
}

interface WeekSummary {
  weekNumber: number;
  startDate: Date;
  endDate: Date;
  income: number;
  expense: number;
  balance: number;
  transactions: Transaction[];
}

interface SwipeState {
  transactionId: string | null;
  offsetX: number;
  direction: 'left' | 'right' | null;
}

/**
 * Get category icon based on category type
 */
function getCategoryIcon(category: TransactionCategory, size: number = 20) {
  const iconMap: Record<TransactionCategory, React.ReactNode> = {
    'food-dining': <Utensils size={size} />,
    'transportation': <Car size={size} />,
    'shopping': <ShoppingBag size={size} />,
    'bills-utilities': <Zap size={size} />,
    'entertainment': <Coffee size={size} />,
    'healthcare': <Heart size={size} />,
    'travel': <Plane size={size} />,
    'technology': <Smartphone size={size} />,
    'income-salary': <DollarSign size={size} />,
    'other': <DollarSign size={size} />,
  };

  return iconMap[category] || <DollarSign size={size} />;
}

/**
 * Get category color based on category type
 */
function getCategoryColor(category: TransactionCategory): string {
  const colorMap: Record<TransactionCategory, string> = {
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

  return colorMap[category] || '#a0aec0';
}

/**
 * PremiumMonthlyView Component
 */
export function PremiumMonthlyView({
  transactions,
  selectedMonth,
  onMonthChange,
  onEditTransaction,
  onDeleteTransaction,
  onTransactionTap,
}: PremiumMonthlyViewProps) {
  // State for expanded weeks
  const [expandedWeeks, setExpandedWeeks] = useState<Set<number>>(new Set([1, 2, 3, 4, 5]));

  // State for swipe gestures
  const [swipeState, setSwipeState] = useState<SwipeState>({
    transactionId: null,
    offsetX: 0,
    direction: null,
  });

  // Touch tracking
  const touchStartX = useRef<number>(0);
  const touchStartY = useRef<number>(0);
  const isSwiping = useRef<boolean>(false);

  // Group transactions by weeks
  const weeklyData = groupTransactionsByWeek(transactions, selectedMonth);

  // Calculate monthly totals
  const monthlyIncome = weeklyData.reduce((sum, week) => sum + week.income, 0);
  const monthlyExpense = weeklyData.reduce((sum, week) => sum + week.expense, 0);
  const monthlyBalance = monthlyIncome - monthlyExpense;

  // Toggle week expansion
  const toggleWeek = (weekNumber: number) => {
    setExpandedWeeks((prev) => {
      const next = new Set(prev);
      if (next.has(weekNumber)) {
        next.delete(weekNumber);
      } else {
        next.add(weekNumber);
      }
      return next;
    });
  };

  // Month navigation
  const handlePreviousMonth = () => {
    const newDate = new Date(selectedMonth.getFullYear(), selectedMonth.getMonth() - 1, 1);
    onMonthChange(newDate);
  };

  const handleNextMonth = () => {
    const newDate = new Date(selectedMonth.getFullYear(), selectedMonth.getMonth() + 1, 1);
    onMonthChange(newDate);
  };

  // Swipe gesture handlers
  const handleTouchStart = (e: React.TouchEvent, transactionId: string) => {
    touchStartX.current = e.touches[0].clientX;
    touchStartY.current = e.touches[0].clientY;
    isSwiping.current = false;
  };

  const handleTouchMove = (e: React.TouchEvent, transactionId: string) => {
    if (!touchStartX.current) return;

    const currentX = e.touches[0].clientX;
    const currentY = e.touches[0].clientY;
    const deltaX = currentX - touchStartX.current;
    const deltaY = currentY - touchStartY.current;

    // Determine if this is a horizontal swipe (not vertical scroll)
    if (!isSwiping.current && Math.abs(deltaX) > Math.abs(deltaY) && Math.abs(deltaX) > 10) {
      isSwiping.current = true;
    }

    if (isSwiping.current) {
      e.preventDefault();
      const direction = deltaX < 0 ? 'left' : 'right';
      const maxSwipe = direction === 'left' ? -120 : 80;
      const offset = Math.max(Math.min(deltaX, 80), -120);

      setSwipeState({
        transactionId,
        offsetX: offset,
        direction,
      });
    }
  };

  const handleTouchEnd = (transaction: Transaction) => {
    if (!isSwiping.current) {
      // This was a tap, not a swipe
      if (onTransactionTap) {
        onTransactionTap(transaction);
      }
      setSwipeState({ transactionId: null, offsetX: 0, direction: null });
      return;
    }

    const threshold = 60;

    if (Math.abs(swipeState.offsetX) > threshold) {
      // Trigger action
      if (swipeState.direction === 'left' && onEditTransaction) {
        onEditTransaction(transaction);
      } else if (swipeState.direction === 'right' && onDeleteTransaction) {
        onDeleteTransaction(transaction.id);
      }
    }

    // Reset swipe state
    setSwipeState({ transactionId: null, offsetX: 0, direction: null });
    isSwiping.current = false;
    touchStartX.current = 0;
    touchStartY.current = 0;
  };

  return (
    <div className="flex flex-col h-full overflow-hidden animate-[fadeIn_0.3s_ease-out]">
      {/* Sticky Monthly Summary Header */}
      <div
        className="
          sticky top-0 z-10
          bg-[var(--premium-bg-primary)]
          border-b border-[var(--premium-glass-border)]
          shadow-[var(--premium-shadow-sm)]
        "
      >
        {/* Month Selector */}
        <div
          className="
            flex items-center justify-between
            p-[var(--premium-space-md)]
            bg-[var(--premium-surface-2)]
            border-b border-[var(--premium-glass-border)]
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
              {transactions.length} transactions
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

        {/* Monthly Summary */}
        <div
          className="
            p-[var(--premium-space-md)]
            bg-[var(--premium-surface-2)]
          "
        >
          <div className="grid grid-cols-3 gap-[var(--premium-space-sm)]">
            {/* Total Income */}
            <div className="text-center">
              <p className="body-xs text-[var(--premium-text-tertiary)] mb-[2px]">Income</p>
              <p className="body-lg font-bold text-[var(--premium-emerald)]">
                ${monthlyIncome.toLocaleString('en-US', { minimumFractionDigits: 0, maximumFractionDigits: 0 })}
              </p>
            </div>

            {/* Total Expense */}
            <div className="text-center">
              <p className="body-xs text-[var(--premium-text-tertiary)] mb-[2px]">Expense</p>
              <p className="body-lg font-bold text-[#f5576c]">
                ${monthlyExpense.toLocaleString('en-US', { minimumFractionDigits: 0, maximumFractionDigits: 0 })}
              </p>
            </div>

            {/* Net Balance */}
            <div className="text-center">
              <p className="body-xs text-[var(--premium-text-tertiary)] mb-[2px]">Balance</p>
              <p
                className={`
                  body-lg font-bold
                  ${monthlyBalance >= 0 ? 'text-[var(--premium-emerald)]' : 'text-[#f5576c]'}
                `}
              >
                {monthlyBalance >= 0 ? '+' : '-'}${Math.abs(monthlyBalance).toLocaleString('en-US', {
                  minimumFractionDigits: 0,
                  maximumFractionDigits: 0,
                })}
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Weekly Sections - Scrollable */}
      <div className="flex-1 overflow-y-auto px-[var(--premium-space-md)] py-[var(--premium-space-md)]">
        <div className="space-y-[var(--premium-space-md)]">
          {weeklyData.map((week) => {
            const isExpanded = expandedWeeks.has(week.weekNumber);

            return (
              <div
                key={week.weekNumber}
                className="
                  rounded-[var(--premium-radius-lg)]
                  bg-[var(--premium-surface-2)]
                  border border-[var(--premium-glass-border)]
                  overflow-hidden
                  transition-all duration-300
                "
              >
                {/* Week Header - Tappable */}
                <button
                  onClick={() => toggleWeek(week.weekNumber)}
                  className="
                    w-full
                    p-[var(--premium-space-md)]
                    flex items-center justify-between
                    hover:bg-[var(--premium-surface-3)]
                    transition-all duration-200
                    active:scale-[0.98]
                  "
                >
                  <div className="flex items-center gap-[var(--premium-space-sm)]">
                    {/* Expand/Collapse Icon */}
                    {isExpanded ? (
                      <ChevronDown size={20} className="text-[var(--premium-text-tertiary)]" />
                    ) : (
                      <ChevronRight size={20} className="text-[var(--premium-text-tertiary)]" />
                    )}

                    {/* Date Range */}
                    <div className="text-left">
                      <p className="body-md font-semibold text-[var(--premium-text-primary)]">
                        {formatDateRange(week.startDate, week.endDate)}
                      </p>
                      <p className="body-xs text-[var(--premium-text-tertiary)]">
                        {week.transactions.length} transactions
                      </p>
                    </div>
                  </div>

                  {/* Week Totals */}
                  <div className="text-right">
                    <div className="flex items-center gap-[var(--premium-space-sm)] justify-end mb-[2px]">
                      {week.income > 0 && (
                        <span className="body-sm text-[var(--premium-emerald)]">
                          +${week.income.toLocaleString('en-US', { minimumFractionDigits: 0, maximumFractionDigits: 0 })}
                        </span>
                      )}
                      {week.expense > 0 && (
                        <span className="body-sm text-[#f5576c]">
                          -${week.expense.toLocaleString('en-US', { minimumFractionDigits: 0, maximumFractionDigits: 0 })}
                        </span>
                      )}
                    </div>
                    <p
                      className={`
                        body-md font-bold
                        ${week.balance >= 0 ? 'text-[var(--premium-text-primary)]' : 'text-[var(--premium-text-secondary)]'}
                      `}
                    >
                      {week.balance >= 0 ? '+' : ''}${week.balance.toLocaleString('en-US', {
                        minimumFractionDigits: 0,
                        maximumFractionDigits: 0,
                      })}
                    </p>
                  </div>
                </button>

                {/* Transaction List - Expandable */}
                {isExpanded && (
                  <div className="border-t border-[var(--premium-glass-border)]">
                    {week.transactions.map((transaction) => {
                      const isThisTransactionSwiping = swipeState.transactionId === transaction.id;
                      const swipeOffset = isThisTransactionSwiping ? swipeState.offsetX : 0;

                      return (
                        <div
                          key={transaction.id}
                          className="relative overflow-hidden"
                        >
                          {/* Swipe Actions Background */}
                          {isThisTransactionSwiping && (
                            <div className="absolute inset-0 flex items-center justify-between px-[var(--premium-space-md)]">
                              {/* Right swipe - Delete */}
                              {swipeState.direction === 'right' && (
                                <div className="flex items-center gap-[var(--premium-space-sm)] text-[#f5576c]">
                                  <Trash2 size={20} />
                                  <span className="body-sm font-medium">Delete</span>
                                </div>
                              )}

                              {/* Left swipe - Edit */}
                              {swipeState.direction === 'left' && (
                                <div className="ml-auto flex items-center gap-[var(--premium-space-sm)] text-[var(--premium-emerald)]">
                                  <span className="body-sm font-medium">Edit</span>
                                  <Edit2 size={20} />
                                </div>
                              )}
                            </div>
                          )}

                          {/* Transaction Row - Swipeable */}
                          <div
                            onTouchStart={(e) => handleTouchStart(e, transaction.id)}
                            onTouchMove={(e) => handleTouchMove(e, transaction.id)}
                            onTouchEnd={() => handleTouchEnd(transaction)}
                            style={{
                              transform: `translateX(${swipeOffset}px)`,
                              transition: isSwiping.current ? 'none' : 'transform 0.3s ease-out',
                            }}
                            className="
                              bg-[var(--premium-surface-2)]
                              p-[var(--premium-space-sm)]
                              border-b border-[var(--premium-glass-border)]
                              last:border-b-0
                              flex items-center gap-[var(--premium-space-sm)]
                            "
                          >
                            {/* Category Icon */}
                            <div
                              className="
                                w-[40px] h-[40px]
                                rounded-full
                                flex items-center justify-center
                                flex-shrink-0
                              "
                              style={{
                                backgroundColor: `${getCategoryColor(transaction.category)}20`,
                                color: getCategoryColor(transaction.category),
                              }}
                            >
                              {getCategoryIcon(transaction.category, 18)}
                            </div>

                            {/* Transaction Info */}
                            <div className="flex-1 min-w-0">
                              <div className="flex items-center gap-[var(--premium-space-xs)] mb-[2px]">
                                <p className="body-md font-medium text-[var(--premium-text-primary)] truncate">
                                  {transaction.categoryLabel || transaction.category}
                                </p>
                                {transaction.subcategoryLabel && (
                                  <span className="body-xs text-[var(--premium-text-tertiary)]">
                                    • {transaction.subcategoryLabel}
                                  </span>
                                )}
                              </div>
                              <p className="body-xs text-[var(--premium-text-tertiary)] truncate">
                                {transaction.accountLabel || transaction.accountId} 
                                {transaction.description && ` • ${transaction.description}`}
                              </p>
                            </div>

                            {/* Amount */}
                            <div className="text-right flex-shrink-0">
                              <p
                                className={`
                                  body-md font-bold
                                  ${transaction.type === 'income' ? 'text-[var(--premium-emerald)]' : 'text-[#f5576c]'}
                                `}
                              >
                                {transaction.type === 'income' ? '+' : '-'}$
                                {transaction.amount.toLocaleString('en-US', {
                                  minimumFractionDigits: 2,
                                  maximumFractionDigits: 2,
                                })}
                              </p>
                            </div>
                          </div>
                        </div>
                      );
                    })}
                  </div>
                )}
              </div>
            );
          })}

          {/* Empty State */}
          {weeklyData.length === 0 && (
            <div className="text-center py-[var(--premium-space-4xl)]">
              <p className="heading-md text-[var(--premium-text-tertiary)] mb-[var(--premium-space-sm)]">
                No transactions this month
              </p>
              <p className="body-sm text-[var(--premium-text-muted)]">
                Add your first transaction to get started
              </p>
            </div>
          )}
        </div>
      </div>

      {/* Interaction Hint */}
      <div
        className="
          text-center
          py-[var(--premium-space-sm)]
          px-[var(--premium-space-md)]
          bg-[var(--premium-surface-2)]/50
          border-t border-[var(--premium-glass-border)]/50
        "
      >
        <p className="body-xs text-[var(--premium-text-muted)]">
          <span className="text-[var(--premium-emerald)]">Tap</span> week to expand •{' '}
          <span className="text-[var(--premium-emerald)]">Swipe</span> transaction to edit/delete
        </p>
      </div>
    </div>
  );
}

/**
 * Group transactions by weeks within a month
 */
function groupTransactionsByWeek(
  transactions: Transaction[],
  selectedMonth: Date
): WeekSummary[] {
  const year = selectedMonth.getFullYear();
  const month = selectedMonth.getMonth();

  // Get first and last day of month
  const firstDay = new Date(year, month, 1);
  const lastDay = new Date(year, month + 1, 0);

  // Generate weeks
  const weeks: WeekSummary[] = [];
  let currentWeekStart = new Date(firstDay);
  let weekNumber = 1;

  while (currentWeekStart <= lastDay) {
    // Calculate week end (7 days later or end of month)
    const currentWeekEnd = new Date(currentWeekStart);
    currentWeekEnd.setDate(currentWeekEnd.getDate() + 6);
    
    // Don't exceed month boundary
    const weekEnd = currentWeekEnd > lastDay ? lastDay : currentWeekEnd;

    // Filter transactions for this week
    const weekTransactions = transactions.filter((txn) => {
      const txnDate = new Date(txn.timestamp);
      return txnDate >= currentWeekStart && txnDate <= weekEnd;
    });

    // Calculate totals
    const income = weekTransactions
      .filter((t) => t.type === 'income')
      .reduce((sum, t) => sum + t.amount, 0);

    const expense = weekTransactions
      .filter((t) => t.type === 'expense')
      .reduce((sum, t) => sum + t.amount, 0);

    const balance = income - expense;

    weeks.push({
      weekNumber,
      startDate: new Date(currentWeekStart),
      endDate: new Date(weekEnd),
      income,
      expense,
      balance,
      transactions: weekTransactions,
    });

    // Move to next week
    currentWeekStart = new Date(weekEnd);
    currentWeekStart.setDate(currentWeekStart.getDate() + 1);
    weekNumber++;
  }

  return weeks.filter((week) => week.transactions.length > 0);
}

/**
 * Format date range for week header
 */
function formatDateRange(startDate: Date, endDate: Date): string {
  const startMonth = startDate.getMonth() + 1;
  const startDay = startDate.getDate();
  const endMonth = endDate.getMonth() + 1;
  const endDay = endDate.getDate();

  if (startMonth === endMonth) {
    return `${startMonth.toString().padStart(2, '0')}/${startDay.toString().padStart(2, '0')} ~ ${endDay
      .toString()
      .padStart(2, '0')}`;
  } else {
    return `${startMonth.toString().padStart(2, '0')}/${startDay.toString().padStart(2, '0')} ~ ${endMonth
      .toString()
      .padStart(2, '0')}/${endDay.toString().padStart(2, '0')}`;
  }
}
