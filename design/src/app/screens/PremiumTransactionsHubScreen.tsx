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
import { PremiumCalendarView } from '../components/premium/PremiumCalendarView';
import { PremiumMonthlyView } from '../components/premium/PremiumMonthlyView';
import { MobilePremiumSummaryView } from '../components/premium/MobilePremiumSummaryView';
import { PremiumDescriptionView } from '../components/premium/PremiumDescriptionView';
import { GlobalTimeSelector } from '../components/premium/GlobalTimeSelector';

// Transaction Hub Tab Type
type TransactionHubTab = 'daily' | 'calendar' | 'monthly' | 'summary' | 'description';

export interface PremiumTransactionsHubScreenProps {
  /** Callback when user wants to add a new transaction */
  onAddTransaction?: () => void;
  /** Callback when user wants to edit a transaction */
  onEditTransaction?: (transaction: any) => void;
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
    category: 'food-dining',
    categoryLabel: 'Food & Dining',
    subcategoryLabel: 'Groceries',
    accountId: 'acc-1',
    accountLabel: 'Main Wallet',
    description: 'Weekly groceries from Whole Foods',
    timestamp: new Date().toISOString(),
    title: 'Weekly Groceries',
    notes: 'Weekly groceries from Whole Foods',
  },
  {
    id: 'txn-2',
    amount: 1250.00,
    type: 'income',
    category: 'income-salary',
    categoryLabel: 'Income',
    subcategoryLabel: 'Salary',
    accountId: 'acc-1',
    accountLabel: 'Main Wallet',
    description: 'January salary payment',
    timestamp: new Date().toISOString(),
    title: 'Salary Payment',
    notes: 'January salary payment',
  },
  {
    id: 'txn-3',
    amount: 89.99,
    type: 'expense',
    category: 'transportation',
    categoryLabel: 'Transportation',
    subcategoryLabel: 'Fuel',
    accountId: 'acc-2',
    accountLabel: 'Chase Card',
    description: 'Shell - Full tank',
    timestamp: new Date(Date.now() - 86400000).toISOString(), // Yesterday
    title: 'Gas Station',
    notes: 'Shell - Full tank',
  },
  {
    id: 'txn-4',
    amount: 25.00,
    type: 'expense',
    category: 'food-dining',
    categoryLabel: 'Food & Dining',
    subcategoryLabel: 'Coffee',
    accountId: 'acc-1',
    accountLabel: 'Main Wallet',
    description: 'Morning latte and croissant',
    timestamp: new Date().toISOString(),
    title: 'Starbucks',
    notes: 'Morning latte and croissant',
  },
  {
    id: 'txn-5',
    amount: 450.00,
    type: 'expense',
    category: 'bills-utilities',
    categoryLabel: 'Bills & Utilities',
    subcategoryLabel: 'Electricity',
    accountId: 'acc-1',
    accountLabel: 'Main Wallet',
    description: 'Monthly electricity payment',
    timestamp: new Date(Date.now() - 172800000).toISOString(), // 2 days ago
    title: 'Electric Bill',
    notes: 'Monthly electricity payment',
  },
  {
    id: 'txn-6',
    amount: 120.00,
    type: 'expense',
    category: 'shopping',
    categoryLabel: 'Shopping',
    subcategoryLabel: 'Clothing',
    accountId: 'acc-2',
    accountLabel: 'Chase Card',
    description: 'New sneakers from Nike',
    timestamp: new Date(Date.now() - 259200000).toISOString(), // 3 days ago
    title: 'Nike Store',
    notes: 'New sneakers from Nike',
  },
  {
    id: 'txn-7',
    amount: 75.50,
    type: 'expense',
    category: 'entertainment',
    categoryLabel: 'Entertainment',
    subcategoryLabel: 'Movies',
    accountId: 'acc-1',
    accountLabel: 'Main Wallet',
    description: 'Movie tickets and popcorn',
    timestamp: new Date(Date.now() - 345600000).toISOString(), // 4 days ago
    title: 'Cinema',
    notes: 'Movie tickets and popcorn',
  },
  {
    id: 'txn-8',
    amount: 200.00,
    type: 'expense',
    category: 'healthcare',
    categoryLabel: 'Healthcare',
    subcategoryLabel: 'Doctor Visit',
    accountId: 'acc-1',
    accountLabel: 'Main Wallet',
    description: 'Annual checkup',
    timestamp: new Date(Date.now() - 604800000).toISOString(), // 1 week ago
    title: 'Medical Center',
    notes: 'Annual checkup',
  },
  {
    id: 'txn-9',
    amount: 500.00,
    type: 'income',
    category: 'income-salary',
    categoryLabel: 'Income',
    subcategoryLabel: 'Freelance',
    accountId: 'acc-1',
    accountLabel: 'Main Wallet',
    description: 'Freelance project payment',
    timestamp: new Date(Date.now() - 691200000).toISOString(), // 8 days ago
    title: 'Freelance Work',
    notes: 'Freelance project payment',
  },
  {
    id: 'txn-10',
    amount: 35.00,
    type: 'expense',
    category: 'food-dining',
    categoryLabel: 'Food & Dining',
    subcategoryLabel: 'Restaurant',
    accountId: 'acc-2',
    accountLabel: 'Chase Card',
    description: 'Lunch at Italian restaurant',
    timestamp: new Date(Date.now() - 777600000).toISOString(), // 9 days ago
    title: 'Italian Restaurant',
    notes: 'Lunch at Italian restaurant',
  },
];

// Category Metadata
const CATEGORY_ICONS: Record<TransactionCategory, any> = {
  'food-dining': Utensils,
  'transportation': Car,
  'shopping': ShoppingBag,
  'bills-utilities': Home,
  'entertainment': Coffee,
  'healthcare': Wallet,
  'travel': Wallet,
  'technology': Wallet,
  'income-salary': TrendingUp,
  'other': Wallet,
};

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
  'healthcare': 'Health & Wellness',
  'travel': 'Travel',
  'technology': 'Technology',
  'income-salary': 'Income',
  'other': 'Other',
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
  onEditTransaction,
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

  // Calculate days with transactions
  const daysWithTransactions = new Set(
    currentMonthTransactions.map((txn) => new Date(txn.timestamp).toDateString())
  ).size;

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
   * Handle Transaction Click (Edit Mode)
   */
  const handleTransactionClick = (txn: Transaction) => {
    if (onEditTransaction) {
      // Convert Transaction to EditableTransaction format
      const editableTransaction = {
        id: txn.id,
        amount: txn.amount,
        type: txn.type,
        category: txn.category,
        subcategory: txn.subcategoryLabel,
        accountId: txn.accountId,
        date: new Date(txn.timestamp),
        notes: txn.notes,
      };
      onEditTransaction(editableTransaction);
    }
  };

  /**
   * Render Transaction Item
   */
  const renderTransactionItem = (txn: Transaction, index: number) => {
    const CategoryIcon = CATEGORY_ICONS[txn.category || 'food-dining'];
    const categoryColor = CATEGORY_COLORS[txn.category || 'food-dining'];
    const isHighlighted = txn.id === highlightTransactionId;

    return (
      <button
        key={txn.id}
        onClick={() => handleTransactionClick(txn)}
        className={`
          w-full
          p-[var(--premium-space-md)]
          rounded-[var(--premium-radius-lg)]
          bg-[var(--premium-surface-2)]
          hover:bg-[var(--premium-surface-3)]
          active:scale-[0.98]
          transition-all duration-200
          flex items-center gap-[var(--premium-space-md)]
          text-left
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
                {CATEGORY_LABELS[txn.category || 'food-dining']} • {ACCOUNT_NAMES[txn.accountId] || 'Unknown Account'}
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
      </button>
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
      <PremiumCalendarView
        transactions={transactions}
        selectedMonth={selectedMonth}
        onMonthChange={(newMonth) => setSelectedMonth(newMonth)}
        onDateTap={(date) => {
          // Drill-down: Switch to Daily tab and scroll to date
          setActiveTab('daily');
          // In a real implementation, you would scroll to the specific date section
          console.log('Drill-down to date:', date);
        }}
        onDateLongPress={(date) => {
          // Long-press: Open Add Transaction with pre-selected date
          if (onAddTransaction) {
            onAddTransaction();
            // In a real implementation, you would pass the date to the transaction screen
            console.log('Add transaction for date:', date);
          }
        }}
      />
    );
  };

  /**
   * Render Monthly View
   */
  const renderMonthlyView = () => {
    return (
      <PremiumMonthlyView
        transactions={transactions}
        selectedMonth={selectedMonth}
        onMonthChange={(newMonth) => setSelectedMonth(newMonth)}
        onEditTransaction={handleTransactionClick}
        onDeleteTransaction={(transactionId) => {
          console.log('Delete transaction:', transactionId);
          // In a real implementation, this would show a confirmation dialog
          setTransactions((prev) => prev.filter((t) => t.id !== transactionId));
        }}
        onTransactionTap={handleTransactionClick}
      />
    );
  };

  /**
   * Render Summary View
   */
  const renderSummaryView = () => {
    return (
      <MobilePremiumSummaryView
        transactions={transactions}
        selectedMonth={selectedMonth}
        onMonthChange={(newMonth) => setSelectedMonth(newMonth)}
      />
    );
  };

  /**
   * Render Description View
   */
  const renderDescriptionView = () => {
    return (
      <PremiumDescriptionView
        transactions={transactions}
        onTransactionTap={handleTransactionClick}
      />
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
              ${Number(monthlyIncome || 0).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
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
              ${Number(monthlyExpense || 0).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
            </p>
          </div>

          {/* Total */}
          <div
            className={`
              p-[var(--premium-space-md)]
              rounded-[var(--premium-radius-lg)]
              ${Number(monthlyTotal || 0) >= 0 ? 'bg-[var(--premium-emerald)]/10 border border-[var(--premium-emerald)]/20' : 'bg-[#f5576c]/10 border border-[#f5576c]/20'}
            `}
          >
            <div className="flex items-center gap-[8px] mb-[4px]">
              <Wallet size={16} className={Number(monthlyTotal || 0) >= 0 ? 'text-[var(--premium-emerald)]' : 'text-[#f5576c]'} />
              <p
                className={`
                  body-xs uppercase tracking-wide font-medium
                  ${Number(monthlyTotal || 0) >= 0 ? 'text-[var(--premium-emerald)]' : 'text-[#f5576c]'}
                `}
              >
                Total
              </p>
            </div>
            <p className={`heading-md ${Number(monthlyTotal || 0) >= 0 ? 'text-[var(--premium-emerald)]' : 'text-[#f5576c]'}`}>
              ${Math.abs(Number(monthlyTotal || 0)).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
            </p>
          </div>
        </div>
      </div>

      {/* Global Time Selector */}
      <GlobalTimeSelector
        selectedDate={selectedMonth}
        onDateChange={(newDate) => setSelectedMonth(newDate)}
        daysWithTransactions={daysWithTransactions}
        totalAmount={monthlyTotal}
      />

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