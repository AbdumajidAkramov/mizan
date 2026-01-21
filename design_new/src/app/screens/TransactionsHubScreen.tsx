/**
 * Transactions Hub Screen
 * Unified transactions management with 5 view modes: Daily, Calendar, Monthly, Summary, Description
 * Implements comprehensive transaction tracking with Material 3 design and glassmorphism
 */

import { useState } from 'react';
import { 
  ArrowLeft, 
  Search, 
  Filter, 
  Star, 
  Plus,
  ChevronLeft,
  ChevronRight
} from 'lucide-react';
import { PremiumCard } from '../components/premium/PremiumCard';
import { TransactionsHubDailyView } from '../components/transactions/TransactionsHubDailyView';
import { TransactionsHubCalendarView } from '../components/transactions/TransactionsHubCalendarView';
import { TransactionsHubMonthlyView } from '../components/transactions/TransactionsHubMonthlyView';
import { TransactionsHubSummaryView } from '../components/transactions/TransactionsHubSummaryView';
import { TransactionsHubDescriptionView } from '../components/transactions/TransactionsHubDescriptionView';
import type { Transaction } from '../../types/domain';
import { MOCK_TRANSACTIONS } from '../../mocks/data';

export interface TransactionsHubScreenProps {
  transactions?: Transaction[];
  onBack?: () => void;
  onSearch?: () => void;
  onFilter?: () => void;
  onFavoriteToggle?: () => void;
  onAddTransaction?: () => void;
  onTransactionClick?: (transaction: Transaction) => void;
}

type ViewMode = 'daily' | 'calendar' | 'monthly' | 'summary' | 'description';

export function TransactionsHubScreen({
  transactions = MOCK_TRANSACTIONS,
  onBack,
  onSearch,
  onFilter,
  onFavoriteToggle,
  onAddTransaction,
  onTransactionClick,
}: TransactionsHubScreenProps) {
  const [activeView, setActiveView] = useState<ViewMode>('daily');
  const [currentDate, setCurrentDate] = useState(new Date());
  const [isFavorite, setIsFavorite] = useState(false);

  // Format current date display
  const formatDateDisplay = () => {
    const month = currentDate.toLocaleDateString('en-US', { month: 'short' });
    const year = currentDate.getFullYear();
    return `${month} ${year}`;
  };

  // Navigate to previous month
  const handlePreviousMonth = () => {
    setCurrentDate(prev => {
      const newDate = new Date(prev);
      newDate.setMonth(newDate.getMonth() - 1);
      return newDate;
    });
  };

  // Navigate to next month
  const handleNextMonth = () => {
    setCurrentDate(prev => {
      const newDate = new Date(prev);
      newDate.setMonth(newDate.getMonth() + 1);
      return newDate;
    });
  };

  const handleFavoriteClick = () => {
    setIsFavorite(!isFavorite);
    onFavoriteToggle?.();
  };

  const tabs: { id: ViewMode; label: string }[] = [
    { id: 'daily', label: 'Daily' },
    { id: 'calendar', label: 'Calendar' },
    { id: 'monthly', label: 'Monthly' },
    { id: 'summary', label: 'Summary' },
    { id: 'description', label: 'Description' },
  ];

  return (
    <div className="relative min-h-screen bg-[var(--premium-bg-primary)] pb-[120px]">
      {/* Top App Bar */}
      <div className="sticky top-0 z-50 bg-[var(--premium-bg-primary)] border-b border-[var(--premium-surface-2)]">
        <div className="flex items-center justify-between px-[var(--premium-space-lg)] py-[var(--premium-space-md)]">
          {/* Left: Back Button */}
          <button
            onClick={onBack}
            className="
              w-[40px] h-[40px]
              rounded-full
              flex items-center justify-center
              bg-[var(--premium-surface-2)]
              hover:bg-[var(--premium-surface-3)]
              transition-all duration-200
            "
          >
            <ArrowLeft size={20} className="text-[var(--premium-text-primary)]" />
          </button>

          {/* Center: Title */}
          <h1 className="heading-lg text-[var(--premium-text-primary)]">
            Trans.
          </h1>

          {/* Right: Action Buttons */}
          <div className="flex items-center gap-[8px]">
            <button
              onClick={onSearch}
              className="
                w-[40px] h-[40px]
                rounded-full
                flex items-center justify-center
                bg-[var(--premium-surface-2)]
                hover:bg-[var(--premium-surface-3)]
                transition-all duration-200
              "
            >
              <Search size={18} className="text-[var(--premium-text-primary)]" />
            </button>

            <button
              onClick={onFilter}
              className="
                w-[40px] h-[40px]
                rounded-full
                flex items-center justify-center
                bg-[var(--premium-surface-2)]
                hover:bg-[var(--premium-surface-3)]
                transition-all duration-200
              "
            >
              <Filter size={18} className="text-[var(--premium-text-primary)]" />
            </button>

            <button
              onClick={handleFavoriteClick}
              className={`
                w-[40px] h-[40px]
                rounded-full
                flex items-center justify-center
                transition-all duration-200
                ${isFavorite 
                  ? 'bg-gradient-to-br from-[var(--premium-emerald)] to-[var(--premium-emerald-dark)] shadow-[var(--premium-glow-primary)]' 
                  : 'bg-[var(--premium-surface-2)] hover:bg-[var(--premium-surface-3)]'
                }
              `}
            >
              <Star 
                size={18} 
                className={isFavorite ? 'text-white fill-white' : 'text-[var(--premium-text-primary)]'} 
              />
            </button>
          </div>
        </div>

        {/* Date Selector */}
        <div className="flex items-center justify-center gap-[var(--premium-space-lg)] px-[var(--premium-space-lg)] py-[var(--premium-space-md)] border-t border-[var(--premium-surface-2)]">
          <button
            onClick={handlePreviousMonth}
            className="
              w-[32px] h-[32px]
              rounded-full
              flex items-center justify-center
              bg-[var(--premium-surface-2)]
              hover:bg-[var(--premium-surface-3)]
              transition-all duration-200
            "
          >
            <ChevronLeft size={16} className="text-[var(--premium-text-primary)]" />
          </button>

          <div className="text-center">
            <p className="heading-md text-[var(--premium-text-primary)]">
              {formatDateDisplay()}
            </p>
          </div>

          <button
            onClick={handleNextMonth}
            className="
              w-[32px] h-[32px]
              rounded-full
              flex items-center justify-center
              bg-[var(--premium-surface-2)]
              hover:bg-[var(--premium-surface-3)]
              transition-all duration-200
            "
          >
            <ChevronRight size={16} className="text-[var(--premium-text-primary)]" />
          </button>
        </div>

        {/* Tabs */}
        <div className="overflow-x-auto scrollbar-hide px-[var(--premium-space-lg)] py-[var(--premium-space-sm)] border-t border-[var(--premium-surface-2)]">
          <div className="flex gap-[8px] min-w-min">
            {tabs.map(tab => (
              <button
                key={tab.id}
                onClick={() => setActiveView(tab.id)}
                className={`
                  px-[var(--premium-space-lg)] py-[8px]
                  rounded-[var(--premium-radius-full)]
                  body-sm font-medium
                  whitespace-nowrap
                  transition-all duration-200
                  ${activeView === tab.id
                    ? 'bg-gradient-to-r from-[var(--premium-emerald)] to-[var(--premium-emerald-dark)] text-white shadow-[var(--premium-shadow-md)]'
                    : 'bg-[var(--premium-surface-2)] text-[var(--premium-text-secondary)] hover:bg-[var(--premium-surface-3)]'
                  }
                `}
              >
                {tab.label}
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Content Area */}
      <div className="px-[var(--premium-space-lg)] pt-[var(--premium-space-lg)]">
        {activeView === 'daily' && (
          <TransactionsHubDailyView
            transactions={transactions}
            currentDate={currentDate}
            onTransactionClick={onTransactionClick}
          />
        )}
        {activeView === 'calendar' && (
          <TransactionsHubCalendarView
            transactions={transactions}
            currentDate={currentDate}
            onDateSelect={(date) => setCurrentDate(date)}
            onTransactionClick={onTransactionClick}
          />
        )}
        {activeView === 'monthly' && (
          <TransactionsHubMonthlyView
            transactions={transactions}
            currentDate={currentDate}
            onTransactionClick={onTransactionClick}
          />
        )}
        {activeView === 'summary' && (
          <TransactionsHubSummaryView
            transactions={transactions}
            currentDate={currentDate}
          />
        )}
        {activeView === 'description' && (
          <TransactionsHubDescriptionView
            transactions={transactions}
            currentDate={currentDate}
            onTransactionClick={onTransactionClick}
          />
        )}
      </div>

      {/* Floating Action Button */}
      <button
        onClick={onAddTransaction}
        className="
          fixed bottom-[24px] right-[24px]
          w-[56px] h-[56px]
          rounded-full
          bg-gradient-to-br from-[var(--premium-emerald)] to-[var(--premium-emerald-dark)]
          flex items-center justify-center
          shadow-[var(--premium-shadow-xl)]
          hover:shadow-[var(--premium-glow-primary)]
          hover:scale-110
          active:scale-95
          transition-all duration-200
          z-50
        "
      >
        <Plus size={28} className="text-white" strokeWidth={2.5} />
      </button>
    </div>
  );
}
