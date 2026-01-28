/**
 * Premium Calendar View - Money Manager Style
 * High-density month calendar with income/expense/balance per day
 * 
 * @design RealByte Money Manager UX Pattern
 * @architecture Compact, information-dense layout for mobile
 * @interactions Tap to drill-down, Long-press to add transaction
 */

import { useState, useEffect } from 'react';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import type { Transaction } from '../../../types/domain';

export interface PremiumCalendarViewProps {
  /** Array of transactions to display */
  transactions: Transaction[];
  /** Current selected month */
  selectedMonth: Date;
  /** Callback when month changes */
  onMonthChange: (date: Date) => void;
  /** Callback when user taps a date (drill-down to Daily view) */
  onDateTap: (date: Date) => void;
  /** Callback when user long-presses a date (add transaction) */
  onDateLongPress: (date: Date) => void;
}

interface DaySummary {
  date: Date;
  income: number;
  expense: number;
  balance: number;
  transactionCount: number;
}

/**
 * PremiumCalendarView Component
 */
export function PremiumCalendarView({
  transactions,
  selectedMonth,
  onMonthChange,
  onDateTap,
  onDateLongPress,
}: PremiumCalendarViewProps) {
  const [longPressTimer, setLongPressTimer] = useState<NodeJS.Timeout | null>(null);

  // Get calendar data for the selected month
  const calendarData = generateCalendarData(selectedMonth, transactions);

  // Calculate monthly totals
  const monthlyIncome = calendarData.reduce((sum, day) => sum + (day?.income || 0), 0);
  const monthlyExpense = calendarData.reduce((sum, day) => sum + (day?.expense || 0), 0);
  const monthlyBalance = monthlyIncome - monthlyExpense;

  // Month navigation
  const handlePreviousMonth = () => {
    const newDate = new Date(selectedMonth.getFullYear(), selectedMonth.getMonth() - 1, 1);
    onMonthChange(newDate);
  };

  const handleNextMonth = () => {
    const newDate = new Date(selectedMonth.getFullYear(), selectedMonth.getMonth() + 1, 1);
    onMonthChange(newDate);
  };

  // Handle touch interactions
  const handleTouchStart = (day: DaySummary | null) => {
    if (!day) return;

    // Start long-press timer (500ms)
    const timer = setTimeout(() => {
      onDateLongPress(day.date);
      // Haptic feedback (if available)
      if ('vibrate' in navigator) {
        navigator.vibrate(50);
      }
    }, 500);

    setLongPressTimer(timer);
  };

  const handleTouchEnd = (day: DaySummary | null) => {
    if (!day) return;

    // Clear long-press timer
    if (longPressTimer) {
      clearTimeout(longPressTimer);
      setLongPressTimer(null);
    }
  };

  const handleTap = (day: DaySummary | null) => {
    if (!day) return;

    // Clear long-press timer if active
    if (longPressTimer) {
      clearTimeout(longPressTimer);
      setLongPressTimer(null);
    }

    // Trigger drill-down
    onDateTap(day.date);
  };

  // Cleanup on unmount
  useEffect(() => {
    return () => {
      if (longPressTimer) {
        clearTimeout(longPressTimer);
      }
    };
  }, [longPressTimer]);

  return (
    <div className="flex flex-col gap-[var(--premium-space-md)] animate-[fadeIn_0.3s_ease-out]">
      {/* Month/Year Selector */}
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
            {calendarData.filter(d => d?.transactionCount > 0).length} days with transactions
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

      {/* Calendar Grid */}
      <div
        className="
          rounded-[var(--premium-radius-lg)]
          bg-[var(--premium-surface-2)]
          border border-[var(--premium-glass-border)]
          p-[var(--premium-space-sm)]
          overflow-hidden
        "
      >
        {/* Weekday Headers */}
        <div className="grid grid-cols-7 gap-[2px] mb-[2px]">
          {['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'].map((day) => (
            <div
              key={day}
              className="
                text-center
                py-[6px]
                body-xs font-semibold
                text-[var(--premium-text-tertiary)]
                uppercase tracking-wide
              "
            >
              {day}
            </div>
          ))}
        </div>

        {/* Calendar Days Grid */}
        <div className="grid grid-cols-7 gap-[2px]">
          {calendarData.map((day, index) => {
            if (!day) {
              // Empty cell (days from previous/next month)
              return (
                <div
                  key={`empty-${index}`}
                  className="
                    aspect-square
                    bg-[var(--premium-surface-1)]/30
                    rounded-[var(--premium-radius-sm)]
                  "
                />
              );
            }

            const isToday = isSameDay(day.date, new Date());
            const hasTransactions = day.transactionCount > 0;

            return (
              <button
                key={day.date.toISOString()}
                onTouchStart={() => handleTouchStart(day)}
                onTouchEnd={() => handleTouchEnd(day)}
                onMouseDown={() => handleTouchStart(day)}
                onMouseUp={() => handleTouchEnd(day)}
                onMouseLeave={() => {
                  if (longPressTimer) {
                    clearTimeout(longPressTimer);
                    setLongPressTimer(null);
                  }
                }}
                onClick={() => handleTap(day)}
                className={`
                  aspect-square
                  rounded-[var(--premium-radius-sm)]
                  p-[4px]
                  flex flex-col items-center justify-center
                  transition-all duration-200
                  active:scale-95
                  ${
                    isToday
                      ? 'bg-[var(--premium-emerald)]/20 border-2 border-[var(--premium-emerald)]'
                      : hasTransactions
                      ? 'bg-[var(--premium-surface-3)] hover:bg-[var(--premium-surface-4)] border border-[var(--premium-glass-border)]'
                      : 'bg-[var(--premium-surface-1)] hover:bg-[var(--premium-surface-2)] border border-transparent'
                  }
                `}
              >
                {/* Date Number */}
                <div
                  className={`
                    body-xs font-medium mb-[2px]
                    ${
                      isToday
                        ? 'text-[var(--premium-emerald)] font-bold'
                        : hasTransactions
                        ? 'text-[var(--premium-text-primary)]'
                        : 'text-[var(--premium-text-muted)]'
                    }
                  `}
                >
                  {day.date.getDate()}
                </div>

                {/* Transaction Data (only if has transactions) */}
                {hasTransactions ? (
                  <>
                    {/* Income (Green) */}
                    {day.income > 0 && (
                      <div className="body-2xs text-[var(--premium-emerald)] leading-tight">
                        +{formatCompactAmount(day.income)}
                      </div>
                    )}

                    {/* Expense (Red) */}
                    {day.expense > 0 && (
                      <div className="body-2xs text-[#f5576c] leading-tight">
                        -{formatCompactAmount(day.expense)}
                      </div>
                    )}

                    {/* Net Balance (Bold) */}
                    <div
                      className={`
                        body-2xs font-bold leading-tight mt-[1px]
                        ${day.balance >= 0 ? 'text-[var(--premium-text-primary)]' : 'text-[var(--premium-text-secondary)]'}
                      `}
                    >
                      {formatCompactAmount(Math.abs(day.balance))}
                    </div>
                  </>
                ) : (
                  /* Empty day indicator */
                  <div className="w-[3px] h-[3px] rounded-full bg-[var(--premium-text-muted)]/20 mt-[4px]" />
                )}
              </button>
            );
          })}
        </div>
      </div>

      {/* Monthly Summary Bar */}
      <div
        className="
          p-[var(--premium-space-md)]
          rounded-[var(--premium-radius-lg)]
          bg-[var(--premium-surface-2)]
          border border-[var(--premium-glass-border)]
        "
      >
        <p className="body-xs text-[var(--premium-text-tertiary)] uppercase tracking-wide font-medium mb-[var(--premium-space-sm)]">
          Monthly Summary
        </p>

        <div className="grid grid-cols-3 gap-[var(--premium-space-sm)]">
          {/* Total Income */}
          <div className="text-center">
            <p className="body-xs text-[var(--premium-text-tertiary)] mb-[2px]">Income</p>
            <p className="body-md font-bold text-[var(--premium-emerald)]">
              ${monthlyIncome.toLocaleString('en-US', { minimumFractionDigits: 0, maximumFractionDigits: 0 })}
            </p>
          </div>

          {/* Total Expense */}
          <div className="text-center">
            <p className="body-xs text-[var(--premium-text-tertiary)] mb-[2px]">Expense</p>
            <p className="body-md font-bold text-[#f5576c]">
              ${monthlyExpense.toLocaleString('en-US', { minimumFractionDigits: 0, maximumFractionDigits: 0 })}
            </p>
          </div>

          {/* Net Balance */}
          <div className="text-center">
            <p className="body-xs text-[var(--premium-text-tertiary)] mb-[2px]">Balance</p>
            <p
              className={`
                body-md font-bold
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

      {/* Interaction Hint */}
      <div
        className="
          text-center
          py-[var(--premium-space-sm)]
          px-[var(--premium-space-md)]
          rounded-[var(--premium-radius-md)]
          bg-[var(--premium-surface-2)]/50
          border border-[var(--premium-glass-border)]/50
        "
      >
        <p className="body-xs text-[var(--premium-text-muted)]">
          <span className="text-[var(--premium-emerald)]">Tap</span> a date to view details •{' '}
          <span className="text-[var(--premium-emerald)]">Long-press</span> to add transaction
        </p>
      </div>
    </div>
  );
}

/**
 * Generate calendar data for a given month
 */
function generateCalendarData(
  selectedMonth: Date,
  transactions: Transaction[]
): (DaySummary | null)[] {
  const year = selectedMonth.getFullYear();
  const month = selectedMonth.getMonth();

  // Get first day of month and total days
  const firstDay = new Date(year, month, 1);
  const lastDay = new Date(year, month + 1, 0);
  const daysInMonth = lastDay.getDate();

  // Get day of week (0 = Sunday, 1 = Monday, etc.)
  // Adjust to Monday = 0
  let firstDayOfWeek = firstDay.getDay() - 1;
  if (firstDayOfWeek === -1) firstDayOfWeek = 6; // Sunday becomes 6

  // Create array to hold calendar data
  const calendarData: (DaySummary | null)[] = [];

  // Add empty cells for days before month starts
  for (let i = 0; i < firstDayOfWeek; i++) {
    calendarData.push(null);
  }

  // Add days of the month
  for (let day = 1; day <= daysInMonth; day++) {
    const date = new Date(year, month, day);
    
    // Filter transactions for this day
    const dayTransactions = transactions.filter((txn) => {
      const txnDate = new Date(txn.timestamp);
      return isSameDay(txnDate, date);
    });

    // Calculate totals
    const income = dayTransactions
      .filter((t) => t.type === 'income')
      .reduce((sum, t) => sum + t.amount, 0);

    const expense = dayTransactions
      .filter((t) => t.type === 'expense')
      .reduce((sum, t) => sum + t.amount, 0);

    const balance = income - expense;

    calendarData.push({
      date,
      income,
      expense,
      balance,
      transactionCount: dayTransactions.length,
    });
  }

  return calendarData;
}

/**
 * Check if two dates are the same day
 */
function isSameDay(date1: Date, date2: Date): boolean {
  return (
    date1.getFullYear() === date2.getFullYear() &&
    date1.getMonth() === date2.getMonth() &&
    date1.getDate() === date2.getDate()
  );
}

/**
 * Format amount in compact notation for calendar cells
 */
function formatCompactAmount(amount: number): string {
  if (amount >= 1000) {
    return `${(amount / 1000).toFixed(1)}k`;
  }
  return amount.toFixed(0);
}
