/**
 * Global Time Selector Component
 * 
 * Centered month/year selector with navigation arrows
 * Shows contextual information about the selected period
 * 
 * @design Glassmorphism with Material 3 spacing
 * @architecture Controlled component with callback-based state management
 */

import { ChevronLeft, ChevronRight } from 'lucide-react';

export interface GlobalTimeSelectorProps {
  /** Currently selected date */
  selectedDate: Date;
  /** Callback when date changes */
  onDateChange: (newDate: Date) => void;
  /** Number of days with transactions in the selected period */
  daysWithTransactions?: number;
  /** Total amount for the selected period */
  totalAmount?: number;
  /** Optional secondary context text override */
  contextText?: string;
}

export function GlobalTimeSelector({
  selectedDate,
  onDateChange,
  daysWithTransactions,
  totalAmount,
  contextText,
}: GlobalTimeSelectorProps) {
  /**
   * Navigate to previous month
   */
  const handlePreviousMonth = () => {
    const newDate = new Date(selectedDate);
    newDate.setMonth(newDate.getMonth() - 1);
    onDateChange(newDate);
  };

  /**
   * Navigate to next month
   */
  const handleNextMonth = () => {
    const newDate = new Date(selectedDate);
    newDate.setMonth(newDate.getMonth() + 1);
    onDateChange(newDate);
  };

  /**
   * Format month and year
   */
  const monthYear = selectedDate.toLocaleDateString('en-US', {
    month: 'long',
    year: 'numeric',
  });

  /**
   * Generate context text
   */
  const getContextText = (): string => {
    if (contextText) return contextText;
    
    if (daysWithTransactions !== undefined) {
      return `${daysWithTransactions} day${daysWithTransactions !== 1 ? 's' : ''} with transactions`;
    }
    
    if (totalAmount !== undefined) {
      const formattedAmount = Math.abs(totalAmount).toLocaleString('en-US', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
      });
      return totalAmount >= 0 ? `Total: +$${formattedAmount}` : `Total: -$${formattedAmount}`;
    }
    
    return 'No transactions';
  };

  return (
    <div
      className="
        flex-shrink-0
        bg-[var(--premium-bg-secondary)]
        px-[var(--premium-space-lg)]
        py-[var(--premium-space-md)]
        border-b border-[var(--premium-glass-border)]
      "
    >
      <div
        className="
          flex items-center justify-center gap-[var(--premium-space-md)]
          px-[var(--premium-space-md)]
          py-[12px]
          rounded-[var(--premium-radius-xl)]
          bg-[var(--premium-surface-2)]/60
          backdrop-blur-xl
          border border-[var(--premium-glass-border)]
        "
      >
        {/* Previous Month Button */}
        <button
          onClick={handlePreviousMonth}
          className="
            w-[32px] h-[32px]
            rounded-full
            bg-[var(--premium-surface-2)]
            hover:bg-[var(--premium-surface-3)]
            flex items-center justify-center
            transition-all duration-200
            active:scale-95
            border border-[var(--premium-glass-border)]
          "
          aria-label="Previous month"
        >
          <ChevronLeft size={18} className="text-[var(--premium-text-secondary)]" />
        </button>

        {/* Month/Year Display */}
        <div className="flex flex-col items-center min-w-[180px]">
          <p className="heading-md text-[var(--premium-text-primary)] font-semibold">
            {monthYear}
          </p>
          <p className="body-xs text-[var(--premium-text-tertiary)] mt-[2px]">
            {getContextText()}
          </p>
        </div>

        {/* Next Month Button */}
        <button
          onClick={handleNextMonth}
          className="
            w-[32px] h-[32px]
            rounded-full
            bg-[var(--premium-surface-2)]
            hover:bg-[var(--premium-surface-3)]
            flex items-center justify-center
            transition-all duration-200
            active:scale-95
            border border-[var(--premium-glass-border)]
          "
          aria-label="Next month"
        >
          <ChevronRight size={18} className="text-[var(--premium-text-secondary)]" />
        </button>
      </div>
    </div>
  );
}
