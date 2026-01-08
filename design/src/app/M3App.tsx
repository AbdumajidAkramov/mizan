/**
 * Main Application Component
 * Expense Manager - Material Design 3 (M3)
 * Strictly follows M3 specifications for Android translation
 */

import { useState, useEffect } from 'react';
import { M3DashboardScreen } from './screens/M3DashboardScreen';
import { M3TransactionsScreen } from './screens/M3TransactionsScreen';
import { M3StatisticsScreen } from './screens/M3StatisticsScreen';
import { M3ProfileScreen } from './screens/M3ProfileScreen';
import { M3BottomNavigationBar, type M3NavigationTab } from './components/organisms/M3BottomNavigationBar';
import type { UiState, DashboardSummary, Transaction } from '../types/domain';
import {
  MOCK_USER_ACCOUNT,
  MOCK_DASHBOARD_SUMMARY,
  MOCK_TRANSACTIONS,
  MOCK_CATEGORY_SPENDING,
} from '../mocks/data';

/**
 * Root Application Component
 * Material Design 3 implementation with MVI architecture
 * 
 * @example
 * ReactDOM.createRoot(document.getElementById('root')!).render(<M3App />)
 */
export default function M3App() {
  const [activeTab, setActiveTab] = useState<M3NavigationTab>('home');
  const [showAddExpenseForm, setShowAddExpenseForm] = useState(false);

  // MVI State Management with explicit UI states
  const [dashboardState, setDashboardState] = useState<UiState<DashboardSummary>>({
    status: 'idle',
  });

  const [transactionsState, setTransactionsState] = useState<UiState<Transaction[]>>({
    status: 'idle',
  });

  /**
   * Simulate data loading on mount
   * In production, this would call actual API endpoints
   */
  useEffect(() => {
    loadDashboardData();
    loadTransactionsData();
  }, []);

  /**
   * Load dashboard data with MVI state transitions
   * idle → loading → success/error
   */
  const loadDashboardData = () => {
    setDashboardState({ status: 'loading' });
    
    // Simulate API call with delay
    setTimeout(() => {
      setDashboardState({
        status: 'success',
        data: MOCK_DASHBOARD_SUMMARY,
      });
    }, 500);
  };

  /**
   * Load transactions data with MVI state transitions
   * idle → loading → success/error/empty
   */
  const loadTransactionsData = () => {
    setTransactionsState({ status: 'loading' });
    
    // Simulate API call with delay
    setTimeout(() => {
      if (MOCK_TRANSACTIONS.length === 0) {
        setTransactionsState({ status: 'empty' });
      } else {
        setTransactionsState({
          status: 'success',
          data: MOCK_TRANSACTIONS,
        });
      }
    }, 600);
  };

  /**
   * Handle expense form submission
   * In production, this would POST to API
   */
  const handleAddExpense = () => {
    // For now, just close the form
    // In real app, would open M3 modal/bottom sheet
    console.log('Add expense clicked - would show M3 bottom sheet');
    setShowAddExpenseForm(true);
  };

  /**
   * Handle transaction click
   * Navigate to detail view or show bottom sheet
   */
  const handleTransactionClick = (transaction: Transaction) => {
    console.log('Transaction clicked:', transaction);
    // In real app, would navigate to detail screen
  };

  /**
   * Render current screen based on active tab
   * Maps to Android Navigation Component
   */
  const renderScreen = () => {
    switch (activeTab) {
      case 'home':
        return (
          <M3DashboardScreen
            dashboardState={dashboardState}
            userDisplayName={MOCK_USER_ACCOUNT.displayName}
          />
        );
      
      case 'transactions':
        return (
          <M3TransactionsScreen
            transactionsState={transactionsState}
            onTransactionClick={handleTransactionClick}
          />
        );
      
      case 'statistics':
        return (
          <M3StatisticsScreen
            topCategories={MOCK_CATEGORY_SPENDING}
          />
        );
      
      case 'profile':
        return (
          <M3ProfileScreen
            displayName={MOCK_USER_ACCOUNT.displayName}
            email={MOCK_USER_ACCOUNT.email}
            transactionCount={MOCK_TRANSACTIONS.length}
            categoriesCount={8}
            budgetsCount={3}
          />
        );
      
      default:
        return (
          <M3DashboardScreen
            dashboardState={dashboardState}
            userDisplayName={MOCK_USER_ACCOUNT.displayName}
          />
        );
    }
  };

  return (
    <div className="min-h-screen bg-[var(--md-sys-color-background)]">
      {/* Main Content Container */}
      <main className="max-w-lg mx-auto px-[var(--md-sys-spacing-md)] pt-[var(--md-sys-spacing-lg)] pb-[80px]">
        {renderScreen()}
      </main>

      {/* M3 Bottom Navigation Bar with FAB */}
      <M3BottomNavigationBar
        activeTab={activeTab}
        onTabChange={setActiveTab}
        onAddExpense={handleAddExpense}
      />

      {/* TODO: Add M3 Bottom Sheet for expense form */}
      {showAddExpenseForm && (
        <div className="fixed inset-0 bg-[var(--md-sys-color-scrim)]/50 z-50 flex items-end justify-center">
          <div className="bg-[var(--md-sys-color-surface)] w-full max-w-lg rounded-t-[var(--md-sys-shape-corner-extra-large)] p-[var(--md-sys-spacing-lg)]">
            <p className="title-large text-[var(--md-sys-color-on-surface)] mb-[var(--md-sys-spacing-md)]">
              Add Expense
            </p>
            <p className="body-medium text-[var(--md-sys-color-on-surface-variant)]">
              M3 Bottom Sheet implementation would go here
            </p>
            <button
              onClick={() => setShowAddExpenseForm(false)}
              className="
                mt-[var(--md-sys-spacing-lg)]
                w-full
                h-[40px]
                rounded-full
                bg-[var(--md-sys-color-primary)]
                text-[var(--md-sys-color-on-primary)]
                label-large
              "
            >
              Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
