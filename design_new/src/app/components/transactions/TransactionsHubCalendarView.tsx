/**
 * Transactions Hub - Calendar View
 * Full-month calendar displaying daily transaction totals with visual indicators
 */

import { useState } from 'react';
import { PremiumCard } from '../premium/PremiumCard';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import type { Transaction } from '../../../types/domain';

export interface TransactionsHubCalendarViewProps {
  transactions: Transaction[];
  currentDate: Date;
  onDateSelect?: (date: Date) => void;
  onTransactionClick?: (transaction: Transaction) => void;
}

interface DayData {
  date: Date;
  dayNumber: number;
  isCurrentMonth: boolean;
  income: number;
  expense: number;
  total: number;
  transactionCount: number;
}

export function TransactionsHubCalendarView({
  transactions,
  currentDate,
  onDateSelect,
  onTransactionClick,
}: TransactionsHubCalendarViewProps) {
  const [selectedDate, setSelectedDate] = useState<Date | null>(null);

  // Get calendar days for current month
  const getCalendarDays = (): DayData[] => {
    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();
    
    // First day of month
    const firstDay = new Date(year, month, 1);
    const startingDayOfWeek = firstDay.getDay();
    
    // Last day of month
    const lastDay = new Date(year, month + 1, 0);
    const daysInMonth = lastDay.getDate();
    
    const days: DayData[] = [];
    
    // Previous month days
    const prevMonthLastDay = new Date(year, month, 0).getDate();
    for (let i = startingDayOfWeek - 1; i >= 0; i--) {
      const date = new Date(year, month - 1, prevMonthLastDay - i);
      days.push({
        date,
        dayNumber: prevMonthLastDay - i,
        isCurrentMonth: false,
        income: 0,
        expense: 0,
        total: 0,
        transactionCount: 0,
      });
    }
    
    // Current month days with transaction data
    for (let day = 1; day <= daysInMonth; day++) {
      const date = new Date(year, month, day);
      const dateKey = date.toISOString().split('T')[0];
      
      // Calculate transactions for this day
      const dayTransactions = transactions.filter(txn => {
        const txnDate = new Date(txn.timestamp);
        return txnDate.toISOString().split('T')[0] === dateKey;
      });
      
      const income = dayTransactions
        .filter(t => t.type === 'income')
        .reduce((sum, t) => sum + t.amount, 0);
      
      const expense = dayTransactions
        .filter(t => t.type === 'expense')
        .reduce((sum, t) => sum + t.amount, 0);
      
      days.push({
        date,
        dayNumber: day,
        isCurrentMonth: true,
        income,
        expense,
        total: income - expense,
        transactionCount: dayTransactions.length,
      });
    }
    
    // Next month days to fill the grid
    const remainingDays = 42 - days.length; // 6 rows * 7 days
    for (let day = 1; day <= remainingDays; day++) {
      const date = new Date(year, month + 1, day);
      days.push({
        date,
        dayNumber: day,
        isCurrentMonth: false,
        income: 0,
        expense: 0,
        total: 0,
        transactionCount: 0,
      });
    }
    
    return days;
  };

  const calendarDays = getCalendarDays();
  const weekDays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

  const handleDateClick = (dayData: DayData) => {
    if (!dayData.isCurrentMonth) return;
    setSelectedDate(dayData.date);
    onDateSelect?.(dayData.date);
  };

  // Get transactions for selected date
  const selectedDateTransactions = selectedDate
    ? transactions.filter(txn => {
        const txnDate = new Date(txn.timestamp);
        return txnDate.toISOString().split('T')[0] === selectedDate.toISOString().split('T')[0];
      })
    : [];

  return (
    <div className="flex flex-col gap-[var(--premium-space-lg)] animate-fade-in-up">
      {/* Calendar Card */}
      <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
        {/* Weekday Headers */}
        <div className="grid grid-cols-7 gap-[4px] mb-[var(--premium-space-md)]">
          {weekDays.map(day => (
            <div key={day} className="text-center">
              <p className="body-xs text-[var(--premium-text-tertiary)] font-medium">
                {day}
              </p>
            </div>
          ))}
        </div>

        {/* Calendar Grid */}
        <div className="grid grid-cols-7 gap-[4px]">
          {calendarDays.map((dayData, index) => {
            const isSelected = selectedDate && 
              dayData.date.toISOString().split('T')[0] === selectedDate.toISOString().split('T')[0];
            const isToday = new Date().toISOString().split('T')[0] === dayData.date.toISOString().split('T')[0];

            return (
              <button
                key={index}
                onClick={() => handleDateClick(dayData)}
                disabled={!dayData.isCurrentMonth}
                className={`
                  aspect-square
                  rounded-[var(--premium-radius-sm)]
                  flex flex-col items-center justify-center
                  gap-[2px]
                  transition-all duration-200
                  ${!dayData.isCurrentMonth 
                    ? 'opacity-30 cursor-not-allowed' 
                    : 'hover:bg-[var(--premium-surface-2)] cursor-pointer'
                  }
                  ${isSelected 
                    ? 'bg-gradient-to-br from-[var(--premium-emerald)] to-[var(--premium-emerald-dark)] shadow-[var(--premium-shadow-md)]' 
                    : ''
                  }
                  ${isToday && !isSelected 
                    ? 'border-2 border-[var(--premium-emerald)]' 
                    : ''
                  }
                `}
              >
                {/* Day Number */}
                <p className={`
                  body-sm font-medium
                  ${isSelected 
                    ? 'text-white' 
                    : dayData.isCurrentMonth 
                      ? 'text-[var(--premium-text-primary)]' 
                      : 'text-[var(--premium-text-muted)]'
                  }
                `}>
                  {dayData.dayNumber}
                </p>

                {/* Transaction Indicators */}
                {dayData.isCurrentMonth && dayData.transactionCount > 0 && (
                  <div className="flex items-center gap-[2px]">
                    {dayData.income > 0 && (
                      <div className={`
                        w-[4px] h-[4px] rounded-full
                        ${isSelected 
                          ? 'bg-white' 
                          : 'bg-[var(--premium-success)]'
                        }
                      `} />
                    )}
                    {dayData.expense > 0 && (
                      <div className={`
                        w-[4px] h-[4px] rounded-full
                        ${isSelected 
                          ? 'bg-white' 
                          : 'bg-[var(--premium-error)]'
                        }
                      `} />
                    )}
                  </div>
                )}
              </button>
            );
          })}
        </div>
      </PremiumCard>

      {/* Selected Date Transactions */}
      {selectedDate && (
        <div className="space-y-[var(--premium-space-md)] animate-fade-in-up">
          <div className="flex items-center justify-between">
            <h3 className="heading-sm text-[var(--premium-text-primary)]">
              {selectedDate.toLocaleDateString('en-US', { 
                weekday: 'long', 
                month: 'long', 
                day: 'numeric' 
              })}
            </h3>
            <button
              onClick={() => setSelectedDate(null)}
              className="body-sm text-[var(--premium-text-tertiary)] hover:text-[var(--premium-text-primary)]"
            >
              Clear
            </button>
          </div>

          {selectedDateTransactions.length > 0 ? (
            <div className="space-y-[8px]">
              {selectedDateTransactions.map(transaction => (
                <PremiumCard
                  key={transaction.id}
                  variant="glass"
                  hover
                  onClick={() => onTransactionClick?.(transaction)}
                  className="p-[var(--premium-space-md)]"
                >
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="body-md text-[var(--premium-text-primary)] font-medium">
                        {transaction.title}
                      </p>
                      <p className="body-xs text-[var(--premium-text-tertiary)] mt-[2px]">
                        {transaction.category}
                      </p>
                    </div>
                    <p className={`
                      heading-sm
                      ${transaction.type === 'income' 
                        ? 'text-[var(--premium-success)]' 
                        : 'text-[var(--premium-error)]'
                      }
                    `}>
                      {transaction.type === 'income' ? '+' : '-'}${transaction.amount.toFixed(2)}
                    </p>
                  </div>
                </PremiumCard>
              ))}
            </div>
          ) : (
            <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
              <p className="body-md text-[var(--premium-text-tertiary)] text-center">
                No transactions on this date
              </p>
            </PremiumCard>
          )}
        </div>
      )}

      {/* Legend */}
      <PremiumCard variant="glass" className="p-[var(--premium-space-md)]">
        <div className="flex items-center justify-center gap-[var(--premium-space-lg)]">
          <div className="flex items-center gap-[6px]">
            <div className="w-[8px] h-[8px] rounded-full bg-[var(--premium-success)]" />
            <p className="body-xs text-[var(--premium-text-tertiary)]">Income</p>
          </div>
          <div className="flex items-center gap-[6px]">
            <div className="w-[8px] h-[8px] rounded-full bg-[var(--premium-error)]" />
            <p className="body-xs text-[var(--premium-text-tertiary)]">Expense</p>
          </div>
          <div className="flex items-center gap-[6px]">
            <div className="w-[16px] h-[16px] rounded border-2 border-[var(--premium-emerald)]" />
            <p className="body-xs text-[var(--premium-text-tertiary)]">Today</p>
          </div>
        </div>
      </PremiumCard>
    </div>
  );
}
