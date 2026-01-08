/**
 * Premium Calendar Component
 * Beautiful month view calendar
 */

import { useState } from 'react';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import { PremiumCard } from './PremiumCard';

export interface PremiumCalendarProps {
  selectedDate?: Date;
  onSelectDate: (date: Date) => void;
  transactionDates?: Date[]; // Dates with transactions
}

export function PremiumCalendar({
  selectedDate = new Date(),
  onSelectDate,
  transactionDates = [],
}: PremiumCalendarProps) {
  const [currentMonth, setCurrentMonth] = useState(new Date());

  const daysOfWeek = ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'];

  // Get days in month
  const getDaysInMonth = (date: Date) => {
    const year = date.getFullYear();
    const month = date.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const daysInMonth = lastDay.getDate();
    const startingDayOfWeek = firstDay.getDay();

    const days: (Date | null)[] = [];

    // Add empty cells for days before month starts
    for (let i = 0; i < startingDayOfWeek; i++) {
      days.push(null);
    }

    // Add days of month
    for (let i = 1; i <= daysInMonth; i++) {
      days.push(new Date(year, month, i));
    }

    return days;
  };

  const days = getDaysInMonth(currentMonth);

  const previousMonth = () => {
    setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() - 1));
  };

  const nextMonth = () => {
    setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() + 1));
  };

  const isToday = (date: Date | null) => {
    if (!date) return false;
    const today = new Date();
    return (
      date.getDate() === today.getDate() &&
      date.getMonth() === today.getMonth() &&
      date.getFullYear() === today.getFullYear()
    );
  };

  const isSelected = (date: Date | null) => {
    if (!date || !selectedDate) return false;
    return (
      date.getDate() === selectedDate.getDate() &&
      date.getMonth() === selectedDate.getMonth() &&
      date.getFullYear() === selectedDate.getFullYear()
    );
  };

  const hasTransaction = (date: Date | null) => {
    if (!date) return false;
    return transactionDates.some(
      (txnDate) =>
        txnDate.getDate() === date.getDate() &&
        txnDate.getMonth() === date.getMonth() &&
        txnDate.getFullYear() === date.getFullYear()
    );
  };

  return (
    <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
      {/* Header */}
      <div className="flex items-center justify-between mb-[var(--premium-space-lg)]">
        <button
          onClick={previousMonth}
          className="
            w-[36px] h-[36px]
            bg-[var(--premium-surface-2)]
            rounded-full
            flex items-center justify-center
            hover:bg-[var(--premium-surface-3)]
            transition-all
          "
        >
          <ChevronLeft size={20} className="text-[var(--premium-text-secondary)]" />
        </button>

        <h3 className="heading-md text-[var(--premium-text-primary)]">
          {currentMonth.toLocaleDateString('en-US', { month: 'long', year: 'numeric' })}
        </h3>

        <button
          onClick={nextMonth}
          className="
            w-[36px] h-[36px]
            bg-[var(--premium-surface-2)]
            rounded-full
            flex items-center justify-center
            hover:bg-[var(--premium-surface-3)]
            transition-all
          "
        >
          <ChevronRight size={20} className="text-[var(--premium-text-secondary)]" />
        </button>
      </div>

      {/* Days of week */}
      <div className="grid grid-cols-7 gap-[var(--premium-space-xs)] mb-[var(--premium-space-sm)]">
        {daysOfWeek.map((day) => (
          <div
            key={day}
            className="
              h-[36px]
              flex items-center justify-center
              body-xs text-[var(--premium-text-muted)]
              font-medium
            "
          >
            {day}
          </div>
        ))}
      </div>

      {/* Calendar grid */}
      <div className="grid grid-cols-7 gap-[var(--premium-space-xs)]">
        {days.map((day, index) => {
          if (!day) {
            return <div key={`empty-${index}`} className="h-[40px]" />;
          }

          const selected = isSelected(day);
          const today = isToday(day);
          const transaction = hasTransaction(day);

          return (
            <button
              key={day.toISOString()}
              onClick={() => onSelectDate(day)}
              className={`
                relative
                h-[40px]
                rounded-[var(--premium-radius-sm)]
                body-sm
                transition-all duration-200
                ${selected
                  ? 'bg-gradient-to-r from-[#667eea] to-[#764ba2] text-white scale-95'
                  : today
                  ? 'bg-[var(--premium-surface-3)] text-[var(--premium-text-primary)] font-medium'
                  : 'text-[var(--premium-text-secondary)] hover:bg-[var(--premium-surface-2)] active:scale-95'
                }
              `}
            >
              {day.getDate()}
              
              {/* Transaction dot */}
              {transaction && !selected && (
                <div className="
                  absolute bottom-[4px] left-1/2 -translate-x-1/2
                  w-[4px] h-[4px]
                  rounded-full
                  bg-[var(--premium-primary)]
                " />
              )}
            </button>
          );
        })}
      </div>
    </PremiumCard>
  );
}
