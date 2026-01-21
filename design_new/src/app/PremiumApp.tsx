/**
 * Premium Expense Manager App - Mizan
 * Clean, production-ready fintech UI with glassmorphism and gradients
 * 
 * Navigation Flow:
 * - Home (Dashboard) → Transactions Hub → Add Transaction → Manage Categories
 * - Statistics, Financial Mirror, Profile screens
 */

import React, { useState, useEffect } from 'react';
import { ThemeProvider } from './contexts/ThemeContext';
import { PremiumDashboardScreen } from './screens/PremiumDashboardScreen';
import { PremiumStatisticsScreen } from './screens/PremiumStatisticsScreen';
import { PremiumProfileScreen } from './screens/PremiumProfileScreen';
import { PremiumFinancialMirrorScreen } from './screens/PremiumFinancialMirrorScreen';
import { PremiumAddTransactionScreen } from './screens/PremiumAddTransactionScreen';
import { ManageCategoriesScreen } from './screens/ManageCategoriesScreen';
import { TransactionsHubScreen } from './screens/TransactionsHubScreen';
import { PremiumBottomNav, type PremiumNavTab } from './components/premium/PremiumBottomNav';
import type { UiState, DashboardSummary, Transaction, TransactionCategory, TransactionType } from '../types/domain';
import {
  MOCK_USER_ACCOUNT,
  MOCK_DASHBOARD_SUMMARY,
  MOCK_TRANSACTIONS,
} from '../mocks/data';

function PremiumAppContent() {
  // Navigation State
  const [activeTab, setActiveTab] = useState<PremiumNavTab>('home');
  const [showAddTransaction, setShowAddTransaction] = useState(false);
  const [showManageCategories, setShowManageCategories] = useState(false);
  const [showTransactionsHub, setShowTransactionsHub] = useState(false);

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
    setShowAddTransaction(true);
  };

  const handleCloseAddExpense = () => {
    setShowAddTransaction(false);
  };

  const handleSaveTransaction = (transaction: {
    amount: number;
    type: TransactionType;
    category?: TransactionCategory;
    fromAccountId?: string;
    toAccountId?: string;
    date: Date;
    notes?: string;
  }) => {
    // TODO: Add transaction to state/database
    console.log('Saving transaction:', transaction);
    
    if (transaction.type === 'transfer') {
      console.log(`Transfer $${transaction.amount} from ${transaction.fromAccountId} to ${transaction.toAccountId}`);
    } else {
      console.log(`${transaction.type} of $${transaction.amount} in category ${transaction.category}`);
    }
    
    setShowAddTransaction(false);
  };

  /**
   * Render current screen based on active tab
   */
  const renderScreen = () => {
    switch (activeTab) {
      case 'home':
        return (
          <PremiumDashboardScreen
            dashboardState={dashboardState}
            userDisplayName={MOCK_USER_ACCOUNT.displayName}
            onViewTransactionsHub={() => setShowTransactionsHub(true)}
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
            onViewTransactionsHub={() => setShowTransactionsHub(true)}
          />
        );
    }
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

      {/* Add Transaction Screen (Full-screen overlay) */}
      {showAddTransaction && (
        <PremiumAddTransactionScreen
          onClose={handleCloseAddExpense}
          onSave={handleSaveTransaction}
          onManageCategories={() => {
            setShowAddTransaction(false);
            setShowManageCategories(true);
          }}
        />
      )}

      {/* Manage Categories Screen (Full-screen overlay) */}
      {showManageCategories && (
        <ManageCategoriesScreen
          onBack={() => {
            setShowManageCategories(false);
            setShowAddTransaction(true);
          }}
        />
      )}

      {/* Transactions Hub Screen (Full-screen overlay) */}
      {showTransactionsHub && (
        <TransactionsHubScreen
          transactions={transactionsState.data}
          onBack={() => {
            setShowTransactionsHub(false);
          }}
          onAddTransaction={handleAddExpense}
          onTransactionClick={(txn) => {
            console.log('Transaction clicked:', txn);
          }}
        />
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