/**
 * Premium Transactions Hub Screen
 * Multi-view transaction center with Daily, Calendar, Monthly, Summary, and Description tabs
 * RealByte-style comprehensive transaction management
 * 
 * @architecture Material 3 with progressive disclosure
 * @design Glassmorphism with emerald green accents
 */

import { useState, useEffect } from 'react';
import {
  Calendar,
  FileText,
  BarChart3,
  List,
  Plus,
  ChevronLeft,
  TrendingUp,
  TrendingDown,
  Wallet,
  ShoppingBag,
  Car,
  Home,
  Utensils,
  Coffee,
} from 'lucide-react';
import type { Transaction, TransactionCategory } from '../../types/domain';

// Transaction Hub Tab Type
type TransactionHubTab = 'daily' | 'calendar' | 'monthly' | 'summary' | 'description';

export interface PremiumTransactionsHubScreenProps {
  /** Callback when user wants to add a new transaction */
  onAddTransaction?: () => void;
  /** Callback when user wants to go back */
  onBack?: () => void;
  /** Initial tab to show */
  initialTab?: TransactionHubTab;
  /** Newly added transaction ID to highlight */
  highlightTransactionId?: string;
}

// Mock Transactions Data - Extended
const MOCK_TRANSACTIONS_EXTENDED: Transaction[] = [
  {
    id: 'txn-1',
    amount: 45.50,
    type: 'expense',
    category: 'food',
    accountId: 'acc-1',
    title: 'Grocery Shopping',
    notes: 'Weekly groceries from Whole Foods',
    timestamp: new Date().toISOString(),
  },
  {
    id: 'txn-2',
    amount: 1250.00,
    type: 'income',
    category: 'income',
    accountId: 'acc-1',
    title: 'Monthly Salary',
    notes: 'January salary payment',
    timestamp: new Date().toISOString(),
  },
  {
    id: 'txn-3',
    amount: 89.99,
    type: 'expense',
    category: 'transport',
    accountId: 'acc-2',
    title: 'Gas Station',
    notes: 'Shell - Full tank',
    timestamp: new Date(Date.now() - 86400000).toISOString(), // Yesterday
  },
  {
    id: 'txn-4',
    amount: 25.00,
    type: 'expense',
    category: 'shopping',
    accountId: 'acc-1',
    title: 'Coffee Shop',
    notes: 'Morning latte and croissant',
    timestamp: new Date().toISOString(),
  },
  {
    id: 'txn-5',
    amount: 450.00,
    type: 'expense',
    category: 'bills',
    accountId: 'acc-1',
    title: 'Electric Bill',
    notes: 'Monthly electricity payment',
    timestamp: new Date(Date.now() - 172800000).toISOString(), // 2 days ago
  },
];

// Category Metadata
const CATEGORY_ICONS: Record<TransactionCategory, any> = {
  food: Utensils,
  transport: Car,
  shopping: ShoppingBag,
  bills: Home,
  entertainment: Coffee,
  health: Wallet,
  travel: Wallet,
  tech: Wallet,
  income: TrendingUp,
};

const CATEGORY_COLORS: Record<TransactionCategory, string> = {
  food: '#ff6b9d',
  transport: '#4facfe',
  shopping: '#ffa34d',
  bills: '#00d2ff',
  entertainment: '#c471f5',
  health: '#ff6b6b',
  travel: '#667eea',
  tech: '#00f2a0',
  income: '#00f2fe',
};

const CATEGORY_LABELS: Record<TransactionCategory, string> = {
  food: 'Food & Dining',
  transport: 'Transportation',
  shopping: 'Shopping',
  bills: 'Bills & Utilities',
  entertainment: 'Entertainment',
  health: 'Health & Wellness',
  travel: 'Travel',
  tech: 'Technology',
  income: 'Income',
};

// Account Names Mock
const ACCOUNT_NAMES: Record<string, string> = {
  'acc-1': 'Main Wallet',
  'acc-2': 'Chase Card',
  'acc-3': 'Savings',
};

/**
 * PremiumTransactionsHubScreen Component
 */
export function PremiumTransactionsHubScreen({
  onAddTransaction,
  onBack,
  initialTab = 'daily',
  highlightTransactionId,
}: PremiumTransactionsHubScreenProps) {
  const [activeTab, setActiveTab] = useState<TransactionHubTab>(initialTab);
  const [selectedMonth, setSelectedMonth] = useState(new Date());
  const [transactions, setTransactions] = useState<Transaction[]>(MOCK_TRANSACTIONS_EXTENDED);

  // Calculate Monthly Summary
  const currentMonthTransactions = transactions.filter((txn) => {
    const txnDate = new Date(txn.timestamp);
    return (
      txnDate.getMonth() === selectedMonth.getMonth() &&
      txnDate.getFullYear() === selectedMonth.getFullYear()
    );
  });

  const monthlyIncome = currentMonthTransactions
    .filter((t) => t.type === 'income')
    .reduce((sum, t) => sum + t.amount, 0);

  const monthlyExpense = currentMonthTransactions
    .filter((t) => t.type === 'expense')
    .reduce((sum, t) => sum + t.amount, 0);

  const monthlyTotal = monthlyIncome - monthlyExpense;

  // Group transactions by date for Daily View
  const groupedByDate = currentMonthTransactions.reduce((groups, txn) => {
    const date = new Date(txn.timestamp).toLocaleDateString('en-US', {
      weekday: 'long',
      month: 'long',
      day: 'numeric',
      year: 'numeric',
    });
    if (!groups[date]) groups[date] = [];
    groups[date].push(txn);
    return groups;
  }, {} as Record<string, Transaction[]>);

  // Sort dates (most recent first)
  const sortedDates = Object.keys(groupedByDate).sort((a, b) => {
    return new Date(b).getTime() - new Date(a).getTime();
  });

  /**
   * Render Transaction Item
   */
  const renderTransactionItem = (txn: Transaction, index: number) => {
    const CategoryIcon = CATEGORY_ICONS[txn.category || 'food'];
    const categoryColor = CATEGORY_COLORS[txn.category || 'food'];
    const isHighlighted = txn.id === highlightTransactionId;

    return (
      <div
        key={txn.id}
        className={`
          p-[var(--premium-space-md)]
          rounded-[var(--premium-radius-lg)]
          bg-[var(--premium-surface-2)]
          hover:bg-[var(--premium-surface-3)]
          transition-all duration-200
          flex items-center gap-[var(--premium-space-md)]
          ${isHighlighted ? 'animate-[pulse_1s_ease-in-out_3] bg-[var(--premium-emerald)]/10 border-2 border-[var(--premium-emerald)]' : 'border-2 border-transparent'}
        `}
        style={{
          animationDelay: `${index * 50}ms`,
        }}
      >
        {/* Category Icon */}
        <div
          className="
            w-[48px] h-[48px]
            rounded-[var(--premium-radius-lg)]
            flex items-center justify-center
            flex-shrink-0
          "
          style={{ backgroundColor: `${categoryColor}20` }}
        >
          <CategoryIcon size={24} style={{ color: categoryColor }} />
        </div>

        {/* Transaction Details */}
        <div className="flex-1 min-w-0">
          <div className="flex items-start justify-between gap-[var(--premium-space-sm)] mb-[4px]">
            <div className="flex-1 min-w-0">
              <p className="body-md font-medium text-[var(--premium-text-primary)] truncate">
                {txn.title}
              </p>
              <p className="body-sm text-[var(--premium-text-tertiary)] truncate">
                {CATEGORY_LABELS[txn.category || 'food']} • {ACCOUNT_NAMES[txn.accountId] || 'Unknown Account'}
              </p>
            </div>
            <p
              className={`
                body-lg font-semibold flex-shrink-0
                ${txn.type === 'income' ? 'text-[var(--premium-emerald)]' : 'text-[#f5576c]'}
              `}
            >
              {txn.type === 'income' ? '+' : '-'}${txn.amount.toLocaleString('en-US', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2,
              })}
            </p>
          </div>
          {txn.notes && (
            <p className="body-xs text-[var(--premium-text-muted)] truncate">
              {txn.notes}
            </p>
          )}
        </div>
      </div>
    );
  };

  /**
   * Render Daily View
   */
  const renderDailyView = () => {
    return (
      <div className="space-y-[var(--premium-space-lg)] animate-[fadeIn_0.3s_ease-out]">
        {sortedDates.length === 0 ? (
          <div className="text-center py-[var(--premium-space-4xl)]">
            <Wallet size={48} className="text-[var(--premium-text-muted)] mx-auto mb-[var(--premium-space-md)]" />
            <p className="body-lg text-[var(--premium-text-secondary)]">No transactions yet</p>
            <p className="body-sm text-[var(--premium-text-tertiary)] mt-[4px]">
              Add your first transaction to get started
            </p>
          </div>
        ) : (
          sortedDates.map((date) => {
            const dayTransactions = groupedByDate[date];
            const dayTotal = dayTransactions.reduce((sum, txn) => {
              return sum + (txn.type === 'income' ? txn.amount : -txn.amount);
            }, 0);

            return (
              <div key={date} className="space-y-[var(--premium-space-sm)]">
                {/* Date Header */}
                <div className="flex items-center justify-between px-[4px]">
                  <p className="body-sm font-medium text-[var(--premium-text-primary)]">
                    {date}
                  </p>
                  <p
                    className={`
                      body-sm font-semibold
                      ${dayTotal >= 0 ? 'text-[var(--premium-emerald)]' : 'text-[#f5576c]'}
                    `}
                  >
                    {dayTotal >= 0 ? '+' : ''}${dayTotal.toLocaleString('en-US', {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    })}
                  </p>
                </div>

                {/* Transactions List */}
                <div className="space-y-[var(--premium-space-sm)]">
                  {dayTransactions.map((txn, index) => renderTransactionItem(txn, index))}
                </div>
              </div>
            );
          })
        )}
      </div>
    );
  };

  /**
   * Render Calendar View
   */
  const renderCalendarView = () => {
    return (
      <div className="flex items-center justify-center py-[var(--premium-space-4xl)] animate-[fadeIn_0.3s_ease-out]">
        <div className="text-center">
          <Calendar size={48} className="text-[var(--premium-text-muted)] mx-auto mb-[var(--premium-space-md)]" />
          <p className="body-lg text-[var(--premium-text-secondary)]">Calendar View</p>
          <p className="body-sm text-[var(--premium-text-tertiary)] mt-[4px]">
            Coming soon
          </p>
        </div>
      </div>
    );
  };

  /**
   * Render Monthly View
   */
  const renderMonthlyView = () => {
    return (
      <div className="flex items-center justify-center py-[var(--premium-space-4xl)] animate-[fadeIn_0.3s_ease-out]">
        <div className="text-center">
          <BarChart3 size={48} className="text-[var(--premium-text-muted)] mx-auto mb-[var(--premium-space-md)]" />
          <p className="body-lg text-[var(--premium-text-secondary)]">Monthly View</p>
          <p className="body-sm text-[var(--premium-text-tertiary)] mt-[4px]">
            Coming soon
          </p>
        </div>
      </div>
    );
  };

  /**
   * Render Summary View
   */
  const renderSummaryView = () => {
    return (
      <div className="flex items-center justify-center py-[var(--premium-space-4xl)] animate-[fadeIn_0.3s_ease-out]">
        <div className="text-center">
          <BarChart3 size={48} className="text-[var(--premium-text-muted)] mx-auto mb-[var(--premium-space-md)]" />
          <p className="body-lg text-[var(--premium-text-secondary)]">Summary View</p>
          <p className="body-sm text-[var(--premium-text-tertiary)] mt-[4px]">
            Coming soon
          </p>
        </div>
      </div>
    );
  };

  /**
   * Render Description View
   */
  const renderDescriptionView = () => {
    return (
      <div className="flex items-center justify-center py-[var(--premium-space-4xl)] animate-[fadeIn_0.3s_ease-out]">
        <div className="text-center">
          <FileText size={48} className="text-[var(--premium-text-muted)] mx-auto mb-[var(--premium-space-md)]" />
          <p className="body-lg text-[var(--premium-text-secondary)]">Description View</p>
          <p className="body-sm text-[var(--premium-text-tertiary)] mt-[4px]">
            Coming soon
          </p>
        </div>
      </div>
    );
  };

  /**
   * Render Active Tab Content
   */
  const renderTabContent = () => {
    switch (activeTab) {
      case 'daily':
        return renderDailyView();
      case 'calendar':
        return renderCalendarView();
      case 'monthly':
        return renderMonthlyView();
      case 'summary':
        return renderSummaryView();
      case 'description':
        return renderDescriptionView();
      default:
        return renderDailyView();
    }
  };

  return (
    <div className="h-screen bg-[var(--premium-bg-primary)] flex flex-col overflow-hidden animate-[slideInRight_0.3s_ease-out]">
      {/* Top App Bar */}
      <div
        className="
          flex-shrink-0
          bg-[var(--premium-bg-secondary)]
          border-b border-[var(--premium-glass-border)]
          px-[var(--premium-space-lg)]
          py-[var(--premium-space-md)]
        "
      >
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-[var(--premium-space-md)]">
            {onBack && (
              <button
                onClick={onBack}
                className="
                  w-[40px] h-[40px]
                  rounded-full
                  bg-[var(--premium-surface-2)]
                  hover:bg-[var(--premium-surface-3)]
                  flex items-center justify-center
                  transition-all duration-200
                  active:scale-95
                "
              >
                <ChevronLeft size={20} className="text-[var(--premium-text-primary)]" />
              </button>
            )}
            <div>
              <h1 className="heading-lg text-[var(--premium-text-primary)]">
                Transactions
              </h1>
              <p className="body-sm text-[var(--premium-text-tertiary)]">
                {selectedMonth.toLocaleDateString('en-US', { month: 'long', year: 'numeric' })}
              </p>
            </div>
          </div>

          {/* Add Transaction Button */}
          <button
            onClick={onAddTransaction}
            className="
              w-[48px] h-[48px]
              rounded-full
              bg-[var(--premium-emerald)]
              hover:bg-[var(--premium-emerald-dark)]
              flex items-center justify-center
              transition-all duration-200
              active:scale-95
              shadow-[0_4px_16px_rgba(16,185,129,0.3)]
            "
          >
            <Plus size={24} className="text-white" strokeWidth={2.5} />
          </button>
        </div>
      </div>

      {/* Monthly Summary Section */}
      <div
        className="
          flex-shrink-0
          bg-[var(--premium-bg-secondary)]
          px-[var(--premium-space-lg)]
          py-[var(--premium-space-lg)]
          border-b border-[var(--premium-glass-border)]
        "
      >
        <div className="grid grid-cols-3 gap-[var(--premium-space-md)]">
          {/* Income */}
          <div
            className="
              p-[var(--premium-space-md)]
              rounded-[var(--premium-radius-lg)]
              bg-[var(--premium-emerald)]/10
              border border-[var(--premium-emerald)]/20
            "
          >
            <div className="flex items-center gap-[8px] mb-[4px]">
              <TrendingUp size={16} className="text-[var(--premium-emerald)]" />
              <p className="body-xs text-[var(--premium-emerald)] uppercase tracking-wide font-medium">
                Income
              </p>
            </div>
            <p className="heading-md text-[var(--premium-emerald)]">
              ${monthlyIncome.toLocaleString('en-US', { minimumFractionDigits: 2 })}
            </p>
          </div>

          {/* Expense */}
          <div
            className="
              p-[var(--premium-space-md)]
              rounded-[var(--premium-radius-lg)]
              bg-[#f5576c]/10
              border border-[#f5576c]/20
            "
          >
            <div className="flex items-center gap-[8px] mb-[4px]">
              <TrendingDown size={16} className="text-[#f5576c]" />
              <p className="body-xs text-[#f5576c] uppercase tracking-wide font-medium">
                Expense
              </p>
            </div>
            <p className="heading-md text-[#f5576c]">
              ${monthlyExpense.toLocaleString('en-US', { minimumFractionDigits: 2 })}
            </p>
          </div>

          {/* Total */}
          <div
            className={`
              p-[var(--premium-space-md)]
              rounded-[var(--premium-radius-lg)]
              ${monthlyTotal >= 0 ? 'bg-[var(--premium-emerald)]/10 border border-[var(--premium-emerald)]/20' : 'bg-[#f5576c]/10 border border-[#f5576c]/20'}
            `}
          >
            <div className="flex items-center gap-[8px] mb-[4px]">
              <Wallet size={16} className={monthlyTotal >= 0 ? 'text-[var(--premium-emerald)]' : 'text-[#f5576c]'} />
              <p
                className={`
                  body-xs uppercase tracking-wide font-medium
                  ${monthlyTotal >= 0 ? 'text-[var(--premium-emerald)]' : 'text-[#f5576c]'}
                `}
              >
                Total
              </p>
            </div>
            <p className={`heading-md ${monthlyTotal >= 0 ? 'text-[var(--premium-emerald)]' : 'text-[#f5576c]'}`}>
              ${Math.abs(monthlyTotal).toLocaleString('en-US', { minimumFractionDigits: 2 })}
            </p>
          </div>
        </div>
      </div>

      {/* Tab Navigation */}
      <div
        className="
          flex-shrink-0
          bg-[var(--premium-bg-secondary)]
          px-[var(--premium-space-lg)]
          py-[var(--premium-space-sm)]
          border-b border-[var(--premium-glass-border)]
          overflow-x-auto
          scrollbar-hide
        "
      >
        <div className="flex gap-[var(--premium-space-sm)] min-w-min">
          {[
            { id: 'daily' as const, label: 'Daily', icon: List },
            { id: 'calendar' as const, label: 'Calendar', icon: Calendar },
            { id: 'monthly' as const, label: 'Monthly', icon: BarChart3 },
            { id: 'summary' as const, label: 'Summary', icon: BarChart3 },
            { id: 'description' as const, label: 'Description', icon: FileText },
          ].map((tab) => {
            const Icon = tab.icon;
            const isActive = activeTab === tab.id;

            return (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id)}
                className={`
                  px-[var(--premium-space-lg)]
                  py-[var(--premium-space-sm)]
                  rounded-[var(--premium-radius-full)]
                  font-medium
                  transition-all duration-200
                  flex items-center gap-[8px]
                  whitespace-nowrap
                  ${
                    isActive
                      ? 'bg-[var(--premium-emerald)] text-white shadow-[0_2px_8px_rgba(16,185,129,0.3)]'
                      : 'bg-[var(--premium-surface-2)] text-[var(--premium-text-secondary)] hover:bg-[var(--premium-surface-3)] active:scale-95'
                  }
                `}
              >
                <Icon size={16} />
                <span className="body-sm">{tab.label}</span>
              </button>
            );
          })}
        </div>
      </div>

      {/* Tab Content - Scrollable */}
      <div className="flex-1 overflow-y-auto px-[var(--premium-space-lg)] py-[var(--premium-space-lg)]">
        {renderTabContent()}
      </div>
    </div>
  );
}