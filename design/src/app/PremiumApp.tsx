/**
 * Premium Expense Manager App
 * Stunning fintech UI with gradients and glassmorphism
 */

import { useState, useEffect } from 'react';
import { ThemeProvider } from './contexts/ThemeContext';
import { PremiumDashboardScreen } from './screens/PremiumDashboardScreen';
import { PremiumTransactionsScreen } from './screens/PremiumTransactionsScreen';
import { PremiumStatisticsScreen } from './screens/PremiumStatisticsScreen';
import { PremiumBudgetScreen } from './screens/PremiumBudgetScreen';
import { PremiumProfileScreen } from './screens/PremiumProfileScreen';
import { PremiumFinancialMirrorScreen } from './screens/PremiumFinancialMirrorScreen';
import { PremiumBottomNav, type PremiumNavTab } from './components/premium/PremiumBottomNav';
import { PremiumButton } from './components/premium/PremiumButton';
import { PremiumCard } from './components/premium/PremiumCard';
import { PremiumCategoryPicker } from './components/premium/PremiumCategoryPicker';
import { PremiumCalendar } from './components/premium/PremiumCalendar';
import { X, DollarSign, Calendar, Tag, FileText, ArrowUpRight, ArrowDownLeft } from 'lucide-react';
import type { UiState, DashboardSummary, Transaction, TransactionCategory } from '../types/domain';
import {
  MOCK_USER_ACCOUNT,
  MOCK_DASHBOARD_SUMMARY,
  MOCK_TRANSACTIONS,
} from '../mocks/data';

function PremiumAppContent() {
  const [activeTab, setActiveTab] = useState<PremiumNavTab>('home');
  const [showAddExpense, setShowAddExpense] = useState(false);
  const [addExpenseStep, setAddExpenseStep] = useState<'type' | 'amount' | 'category' | 'details'>('type');
  const [transactionType, setTransactionType] = useState<'expense' | 'income'>('expense');
  const [selectedCategory, setSelectedCategory] = useState<TransactionCategory>();
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [amount, setAmount] = useState('');

  // MVI State Management
  const [dashboardState, setDashboardState] = useState<UiState<DashboardSummary>>({
    status: 'idle',
  });

  const [transactionsState, setTransactionsState] = useState<UiState<Transaction[]>>({
    status: 'idle',
  });

  /**
   * Load data on mount
   */
  useEffect(() => {
    loadDashboardData();
    loadTransactionsData();
  }, []);

  const loadDashboardData = () => {
    setDashboardState({ status: 'loading' });
    
    setTimeout(() => {
      setDashboardState({
        status: 'success',
        data: MOCK_DASHBOARD_SUMMARY,
      });
    }, 800);
  };

  const loadTransactionsData = () => {
    setTransactionsState({ status: 'loading' });
    
    setTimeout(() => {
      setTransactionsState({
        status: 'success',
        data: MOCK_TRANSACTIONS,
      });
    }, 900);
  };

  const handleAddExpense = () => {
    setShowAddExpense(true);
    setAddExpenseStep('type');
    setTransactionType('expense');
    setSelectedCategory(undefined);
    setAmount('');
  };

  const handleCloseAddExpense = () => {
    setShowAddExpense(false);
    setAddExpenseStep('type');
  };

  const handleNext = () => {
    if (addExpenseStep === 'type') {
      setAddExpenseStep('amount');
    } else if (addExpenseStep === 'amount' && amount) {
      setAddExpenseStep('category');
    } else if (addExpenseStep === 'category' && selectedCategory) {
      setAddExpenseStep('details');
    }
  };

  /**
   * Render current screen
   */
  const renderScreen = () => {
    switch (activeTab) {
      case 'home':
        return (
          <PremiumDashboardScreen
            dashboardState={dashboardState}
            userDisplayName={MOCK_USER_ACCOUNT.displayName}
          />
        );
      
      case 'statistics':
        return (
          <PremiumStatisticsScreen
            dashboardState={dashboardState}
          />
        );
      
      case 'mirror':
        return <PremiumFinancialMirrorScreen />;
      
      case 'profile':
        return <PremiumProfileScreen />;
      
      default:
        return (
          <PremiumDashboardScreen
            dashboardState={dashboardState}
            userDisplayName={MOCK_USER_ACCOUNT.displayName}
          />
        );
    }
  };

  /**
   * Render Add Expense Modal Content
   */
  const renderAddExpenseContent = () => {
    // Type Selection
    if (addExpenseStep === 'type') {
      return (
        <>
          <div className="flex items-center justify-between mb-[var(--premium-space-xl)]">
            <h2 className="heading-xl text-[var(--premium-text-primary)]">
              Add Transaction
            </h2>
            <button
              onClick={handleCloseAddExpense}
              className="
                w-[40px] h-[40px]
                bg-[var(--premium-surface-2)]
                rounded-full
                flex items-center justify-center
                hover:bg-[var(--premium-surface-3)]
                transition-all
              "
            >
              <X size={20} className="text-[var(--premium-text-secondary)]" />
            </button>
          </div>

          <div className="space-y-[var(--premium-space-md)]">
            <p className="body-md text-[var(--premium-text-tertiary)] mb-[var(--premium-space-lg)]">
              What type of transaction?
            </p>

            <button
              onClick={() => setTransactionType('expense')}
              className={`
                w-full p-[var(--premium-space-xl)]
                rounded-[var(--premium-radius-xl)]
                transition-all duration-200
                ${transactionType === 'expense'
                  ? 'bg-gradient-to-r from-[#f093fb] to-[#f5576c] scale-95'
                  : 'bg-[var(--premium-surface-2)] hover:bg-[var(--premium-surface-3)]'
                }
              `}
            >
              <div className="flex items-center gap-[var(--premium-space-md)]">
                <div className={`
                  w-[56px] h-[56px]
                  rounded-full
                  flex items-center justify-center
                  ${transactionType === 'expense' ? 'bg-white/20' : 'bg-[var(--premium-error)]/20'}
                `}>
                  <ArrowUpRight size={28} className={transactionType === 'expense' ? 'text-white' : 'text-[var(--premium-error)]'} />
                </div>
                <div className="flex-1 text-left">
                  <h3 className={`heading-md mb-[4px] ${transactionType === 'expense' ? 'text-white' : 'text-[var(--premium-text-primary)]'}`}>
                    Expense
                  </h3>
                  <p className={`body-sm ${transactionType === 'expense' ? 'text-white/70' : 'text-[var(--premium-text-tertiary)]'}`}>
                    Money spent on purchases
                  </p>
                </div>
              </div>
            </button>

            <button
              onClick={() => setTransactionType('income')}
              className={`
                w-full p-[var(--premium-space-xl)]
                rounded-[var(--premium-radius-xl)]
                transition-all duration-200
                ${transactionType === 'income'
                  ? 'bg-gradient-to-r from-[#4facfe] to-[#00f2fe] scale-95'
                  : 'bg-[var(--premium-surface-2)] hover:bg-[var(--premium-surface-3)]'
                }
              `}
            >
              <div className="flex items-center gap-[var(--premium-space-md)]">
                <div className={`
                  w-[56px] h-[56px]
                  rounded-full
                  flex items-center justify-center
                  ${transactionType === 'income' ? 'bg-white/20' : 'bg-[var(--premium-success)]/20'}
                `}>
                  <ArrowDownLeft size={28} className={transactionType === 'income' ? 'text-white' : 'text-[var(--premium-success)]'} />
                </div>
                <div className="flex-1 text-left">
                  <h3 className={`heading-md mb-[4px] ${transactionType === 'income' ? 'text-white' : 'text-[var(--premium-text-primary)]'}`}>
                    Income
                  </h3>
                  <p className={`body-sm ${transactionType === 'income' ? 'text-white/70' : 'text-[var(--premium-text-tertiary)]'}`}>
                    Money received
                  </p>
                </div>
              </div>
            </button>

            <PremiumButton
              variant="gradient-primary"
              size="lg"
              fullWidth
              onClick={handleNext}
            >
              Continue
            </PremiumButton>
          </div>
        </>
      );
    }

    // Amount Entry
    if (addExpenseStep === 'amount') {
      return (
        <>
          <div className="flex items-center justify-between mb-[var(--premium-space-xl)]">
            <h2 className="heading-xl text-[var(--premium-text-primary)]">
              Enter Amount
            </h2>
            <button
              onClick={handleCloseAddExpense}
              className="
                w-[40px] h-[40px]
                bg-[var(--premium-surface-2)]
                rounded-full
                flex items-center justify-center
                hover:bg-[var(--premium-surface-3)]
                transition-all
              "
            >
              <X size={20} className="text-[var(--premium-text-secondary)]" />
            </button>
          </div>

          <div className="space-y-[var(--premium-space-lg)]">
            <div className="text-center py-[var(--premium-space-2xl)]">
              <DollarSign size={40} className="text-[var(--premium-text-muted)] mx-auto mb-[var(--premium-space-md)]" />
              <input
                type="number"
                inputMode="decimal"
                placeholder="0.00"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                autoFocus
                className="
                  w-full
                  bg-transparent
                  display-lg text-center text-[var(--premium-text-primary)]
                  outline-none
                  placeholder:text-[var(--premium-text-muted)]
                "
              />
              <p className="body-md text-[var(--premium-text-tertiary)] mt-[var(--premium-space-sm)]">
                {transactionType === 'expense' ? 'Expense Amount' : 'Income Amount'}
              </p>
            </div>

            <div className="flex gap-[var(--premium-space-md)]">
              <PremiumButton
                variant="ghost"
                size="lg"
                fullWidth
                onClick={() => setAddExpenseStep('type')}
              >
                Back
              </PremiumButton>
              <PremiumButton
                variant="gradient-primary"
                size="lg"
                fullWidth
                onClick={handleNext}
                disabled={!amount || parseFloat(amount) <= 0}
              >
                Continue
              </PremiumButton>
            </div>
          </div>
        </>
      );
    }

    // Category Selection
    if (addExpenseStep === 'category') {
      return (
        <>
          <div className="flex items-center justify-between mb-[var(--premium-space-xl)]">
            <h2 className="heading-xl text-[var(--premium-text-primary)]">
              Select Category
            </h2>
            <button
              onClick={handleCloseAddExpense}
              className="
                w-[40px] h-[40px]
                bg-[var(--premium-surface-2)]
                rounded-full
                flex items-center justify-center
                hover:bg-[var(--premium-surface-3)]
                transition-all
              "
            >
              <X size={20} className="text-[var(--premium-text-secondary)]" />
            </button>
          </div>

          <div className="space-y-[var(--premium-space-lg)]">
            <PremiumCategoryPicker
              selectedCategory={selectedCategory}
              onSelectCategory={setSelectedCategory}
            />

            <div className="flex gap-[var(--premium-space-md)]">
              <PremiumButton
                variant="ghost"
                size="lg"
                fullWidth
                onClick={() => setAddExpenseStep('amount')}
              >
                Back
              </PremiumButton>
              <PremiumButton
                variant="gradient-primary"
                size="lg"
                fullWidth
                onClick={handleNext}
                disabled={!selectedCategory}
              >
                Continue
              </PremiumButton>
            </div>
          </div>
        </>
      );
    }

    // Details & Submit
    if (addExpenseStep === 'details') {
      return (
        <>
          <div className="flex items-center justify-between mb-[var(--premium-space-xl)]">
            <h2 className="heading-xl text-[var(--premium-text-primary)]">
              Add Details
            </h2>
            <button
              onClick={handleCloseAddExpense}
              className="
                w-[40px] h-[40px]
                bg-[var(--premium-surface-2)]
                rounded-full
                flex items-center justify-center
                hover:bg-[var(--premium-surface-3)]
                transition-all
              "
            >
              <X size={20} className="text-[var(--premium-text-secondary)]" />
            </button>
          </div>

          <div className="space-y-[var(--premium-space-lg)]">
            {/* Date */}
            <div>
              <label className="label-sm text-[var(--premium-text-secondary)] mb-[var(--premium-space-sm)] block">
                Date
              </label>
              <PremiumCalendar
                selectedDate={selectedDate}
                onSelectDate={setSelectedDate}
              />
            </div>

            {/* Notes */}
            <div>
              <label className="label-sm text-[var(--premium-text-secondary)] mb-[var(--premium-space-sm)] block">
                Notes (Optional)
              </label>
              <div className="
                bg-[var(--premium-surface-2)]
                rounded-[var(--premium-radius-lg)]
                p-[var(--premium-space-md)]
                flex items-start gap-[var(--premium-space-sm)]
              ">
                <FileText size={20} className="text-[var(--premium-text-tertiary)] mt-[2px]" />
                <textarea
                  placeholder="Add a note..."
                  rows={3}
                  className="
                    flex-1 bg-transparent
                    body-md text-[var(--premium-text-primary)]
                    outline-none resize-none
                    placeholder:text-[var(--premium-text-muted)]
                  "
                />
              </div>
            </div>

            {/* Submit */}
            <div className="flex gap-[var(--premium-space-md)]">
              <PremiumButton
                variant="ghost"
                size="lg"
                fullWidth
                onClick={() => setAddExpenseStep('category')}
              >
                Back
              </PremiumButton>
              <PremiumButton
                variant="gradient-primary"
                size="lg"
                fullWidth
                onClick={() => {
                  // Handle submit
                  handleCloseAddExpense();
                }}
              >
                Add {transactionType === 'expense' ? 'Expense' : 'Income'}
              </PremiumButton>
            </div>
          </div>
        </>
      );
    }

    return null;
  };

  return (
    <div className="min-h-screen bg-[var(--premium-bg-primary)] relative overflow-hidden">
      {/* Animated Background Gradients */}
      <div className="fixed inset-0 pointer-events-none overflow-hidden">
        <div 
          className="absolute top-0 right-0 w-[500px] h-[500px] rounded-full opacity-20 blur-[120px]"
          style={{ background: 'radial-gradient(circle, #667eea 0%, transparent 70%)' }}
        />
        <div 
          className="absolute bottom-0 left-0 w-[400px] h-[400px] rounded-full opacity-20 blur-[100px]"
          style={{ background: 'radial-gradient(circle, #f5576c 0%, transparent 70%)' }}
        />
      </div>

      {/* Main Content */}
      <main className="
        relative z-10
        max-w-lg mx-auto 
        px-[var(--premium-space-md)] 
        pt-[var(--premium-space-2xl)] 
        pb-[120px]
      ">
        {renderScreen()}
      </main>

      {/* Premium Bottom Navigation */}
      <PremiumBottomNav
        activeTab={activeTab}
        onTabChange={setActiveTab}
        onAddExpense={handleAddExpense}
      />

      {/* Add Expense Modal */}
      {showAddExpense && (
        <div 
          className="
            fixed inset-0 z-50 
            bg-black/60 backdrop-blur-sm
            flex items-end justify-center
            animate-fade-in-up
          "
          onClick={handleCloseAddExpense}
        >
          <PremiumCard
            variant="solid"
            className="
              w-full max-w-lg 
              rounded-t-[var(--premium-radius-2xl)] rounded-b-none
              p-[var(--premium-space-xl)]
              max-h-[90vh]
              overflow-y-auto
            "
            onClick={(e) => e.stopPropagation()}
          >
            {renderAddExpenseContent()}
          </PremiumCard>
        </div>
      )}
    </div>
  );
}

export default function PremiumApp() {
  return (
    <ThemeProvider>
      <PremiumAppContent />
    </ThemeProvider>
  );
}