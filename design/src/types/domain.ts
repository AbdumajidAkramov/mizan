/**
 * Domain Models for Expense Manager Application
 * Follows MVI pattern with explicit state management
 */

/**
 * Represents a financial transaction in the system
 */
export interface Transaction {
  /** Unique identifier for the transaction */
  id: string;
  /** Human-readable title/description */
  title: string;
  /** Amount in USD */
  amount: number;
  /** Transaction category */
  category: TransactionCategory;
  /** ISO 8601 timestamp */
  timestamp: string;
  /** Transaction type - expense or income */
  type: TransactionType;
  /** Optional notes or description */
  notes?: string;
}

/**
 * Available transaction categories
 */
export type TransactionCategory = 
  | 'food' 
  | 'transport' 
  | 'shopping' 
  | 'bills' 
  | 'entertainment' 
  | 'health' 
  | 'travel' 
  | 'tech'
  | 'income';

/**
 * Transaction type enum
 */
export type TransactionType = 'expense' | 'income';

/**
 * Category metadata for UI rendering
 */
export interface CategoryMetadata {
  id: TransactionCategory;
  label: string;
  iconName: string;
  colorToken: string;
}

/**
 * Aggregated spending by category
 */
export interface CategorySpending {
  category: TransactionCategory;
  categoryLabel: string;
  totalAmount: number;
  percentage: number;
  transactionCount: number;
  colorToken: string;
}

/**
 * Monthly budget tracking
 */
export interface MonthlyBudget {
  month: string;
  budgetLimit: number;
  totalSpent: number;
  remainingAmount: number;
  percentageUsed: number;
}

/**
 * Daily spending data point
 */
export interface DailySpending {
  date: string;
  dayLabel: string;
  totalAmount: number;
}

/**
 * User account information
 */
export interface UserAccount {
  id: string;
  displayName: string;
  email: string;
  avatarUrl?: string;
  totalBalance: number;
  savingsAmount: number;
}

/**
 * MVI State wrapper for async operations
 */
export interface UiState<T> {
  status: 'idle' | 'loading' | 'success' | 'error' | 'empty';
  data?: T;
  error?: string;
}

/**
 * Dashboard summary metrics
 */
export interface DashboardSummary {
  totalBalance: number;
  monthlyExpenses: number;
  monthlySavings: number;
  budgetLimit: number;
  budgetPercentageUsed: number;
  topCategories: CategorySpending[];
  weeklySpending: DailySpending[];
}

/**
 * Statistics period
 */
export type StatisticsPeriod = 'week' | 'month' | 'year';

/**
 * Monthly comparison data
 */
export interface MonthlyComparison {
  currentMonth: {
    label: string;
    totalAmount: number;
  };
  previousMonth: {
    label: string;
    totalAmount: number;
  };
  difference: number;
  percentageChange: number;
}

/**
 * Expense form data
 */
export interface ExpenseFormData {
  amount: string;
  category: TransactionCategory | '';
  description: string;
  date: string;
}

/**
 * Form validation state
 */
export interface FormValidation {
  isValid: boolean;
  errors: {
    amount?: string;
    category?: string;
    description?: string;
  };
}
