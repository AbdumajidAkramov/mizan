/**
 * Mock Data for Expense Manager Application
 * Realistic test data for development and demonstration
 */

import type { 
  Transaction, 
  UserAccount, 
  CategoryMetadata, 
  DashboardSummary,
  CategorySpending,
  DailySpending,
  MonthlyComparison
} from '../types/domain';

/**
 * Category configuration with UI metadata
 */
export const CATEGORY_METADATA: CategoryMetadata[] = [
  { id: 'food', label: 'Food & Dining', iconName: 'Utensils', colorToken: 'var(--color-category-food)' },
  { id: 'transport', label: 'Transport', iconName: 'Car', colorToken: 'var(--color-category-transport)' },
  { id: 'shopping', label: 'Shopping', iconName: 'ShoppingBag', colorToken: 'var(--color-category-shopping)' },
  { id: 'bills', label: 'Bills & Utilities', iconName: 'Receipt', colorToken: 'var(--color-category-bills)' },
  { id: 'entertainment', label: 'Entertainment', iconName: 'Film', colorToken: 'var(--color-category-entertainment)' },
  { id: 'health', label: 'Health & Fitness', iconName: 'Heart', colorToken: 'var(--color-category-health)' },
  { id: 'travel', label: 'Travel', iconName: 'Plane', colorToken: 'var(--color-category-travel)' },
  { id: 'tech', label: 'Technology', iconName: 'Smartphone', colorToken: 'var(--color-category-tech)' },
  { id: 'income', label: 'Income', iconName: 'TrendingUp', colorToken: 'var(--color-category-income)' },
];

/**
 * Subcategory configuration for enhanced category selection
 * Maps main categories to their subcategories
 */
export const CATEGORY_SUBCATEGORIES: Record<string, string[]> = {
  food: ['Restaurants', 'Groceries', 'Fast Food', 'Coffee Shop', 'Delivery'],
  transport: ['Fuel', 'Taxi', 'Public Transport', 'Maintenance', 'Parking'],
  shopping: ['Clothing', 'Electronics', 'Home & Garden', 'Books', 'Personal Care'],
  bills: ['Electricity', 'Water', 'Internet', 'Phone', 'Rent', 'Insurance'],
  entertainment: ['Movies', 'Concerts', 'Games', 'Streaming', 'Hobbies'],
  health: ['Doctor', 'Pharmacy', 'Gym', 'Wellness', 'Sports'],
  travel: ['Flights', 'Hotels', 'Car Rental', 'Tours', 'Activities'],
  tech: ['Software', 'Hardware', 'Subscriptions', 'Apps', 'Services'],
};

/**
 * Mock user account data
 */
export const MOCK_USER_ACCOUNT: UserAccount = {
  id: 'user_001',
  displayName: 'John Doe',
  email: 'john.doe@email.com',
  totalBalance: 8450.50,
  savingsAmount: 1245.00,
};

/**
 * Mock transaction data - 20 realistic entries
 */
export const MOCK_TRANSACTIONS: Transaction[] = [
  {
    id: 'txn_001',
    title: 'Whole Foods Market',
    amount: 127.45,
    category: 'food',
    timestamp: '2026-01-08T14:30:00Z',
    type: 'expense',
    notes: 'Weekly grocery shopping',
  },
  {
    id: 'txn_002',
    title: 'Uber Ride to Airport',
    amount: 45.20,
    category: 'transport',
    timestamp: '2026-01-08T08:15:00Z',
    type: 'expense',
    notes: 'Business trip',
  },
  {
    id: 'txn_003',
    title: 'Salary Deposit',
    amount: 4500.00,
    category: 'income',
    timestamp: '2026-01-07T09:00:00Z',
    type: 'income',
    notes: 'Monthly salary - January 2026',
  },
  {
    id: 'txn_004',
    title: 'Starbucks Coffee',
    amount: 6.80,
    category: 'food',
    timestamp: '2026-01-07T15:45:00Z',
    type: 'expense',
    notes: 'Afternoon coffee break',
  },
  {
    id: 'txn_005',
    title: 'Netflix Subscription',
    amount: 15.99,
    category: 'entertainment',
    timestamp: '2026-01-06T00:00:00Z',
    type: 'expense',
    notes: 'Monthly subscription',
  },
  {
    id: 'txn_006',
    title: 'Electricity Bill',
    amount: 89.50,
    category: 'bills',
    timestamp: '2026-01-05T10:00:00Z',
    type: 'expense',
    notes: 'December electricity usage',
  },
  {
    id: 'txn_007',
    title: 'Amazon Purchase',
    amount: 156.30,
    category: 'shopping',
    timestamp: '2026-01-05T16:20:00Z',
    type: 'expense',
    notes: 'Books and office supplies',
  },
  {
    id: 'txn_008',
    title: 'Gym Membership',
    amount: 65.00,
    category: 'health',
    timestamp: '2026-01-04T12:00:00Z',
    type: 'expense',
    notes: 'Monthly gym membership',
  },
  {
    id: 'txn_009',
    title: 'Shell Gas Station',
    amount: 52.75,
    category: 'transport',
    timestamp: '2026-01-04T18:30:00Z',
    type: 'expense',
    notes: 'Full tank refuel',
  },
  {
    id: 'txn_010',
    title: 'Pizza Hut Dinner',
    amount: 38.90,
    category: 'food',
    timestamp: '2026-01-03T19:00:00Z',
    type: 'expense',
    notes: 'Family dinner',
  },
  {
    id: 'txn_011',
    title: 'Internet Bill',
    amount: 79.99,
    category: 'bills',
    timestamp: '2026-01-03T00:00:00Z',
    type: 'expense',
    notes: 'Monthly fiber internet',
  },
  {
    id: 'txn_012',
    title: 'Movie Tickets',
    amount: 32.00,
    category: 'entertainment',
    timestamp: '2026-01-02T20:15:00Z',
    type: 'expense',
    notes: 'Weekend movie - 2 tickets',
  },
  {
    id: 'txn_013',
    title: 'Apple Store',
    amount: 89.99,
    category: 'tech',
    timestamp: '2026-01-02T14:00:00Z',
    type: 'expense',
    notes: 'AirPods case replacement',
  },
  {
    id: 'txn_014',
    title: 'Target Shopping',
    amount: 143.65,
    category: 'shopping',
    timestamp: '2026-01-01T11:30:00Z',
    type: 'expense',
    notes: 'Home essentials',
  },
  {
    id: 'txn_015',
    title: 'Freelance Project',
    amount: 850.00,
    category: 'income',
    timestamp: '2025-12-31T16:00:00Z',
    type: 'income',
    notes: 'Website design project',
  },
  {
    id: 'txn_016',
    title: 'CVS Pharmacy',
    amount: 42.30,
    category: 'health',
    timestamp: '2025-12-30T10:45:00Z',
    type: 'expense',
    notes: 'Prescription medication',
  },
  {
    id: 'txn_017',
    title: 'Spotify Premium',
    amount: 10.99,
    category: 'entertainment',
    timestamp: '2025-12-29T00:00:00Z',
    type: 'expense',
    notes: 'Monthly music subscription',
  },
  {
    id: 'txn_018',
    title: 'Hotel Booking',
    amount: 245.00,
    category: 'travel',
    timestamp: '2025-12-28T13:20:00Z',
    type: 'expense',
    notes: 'Weekend getaway booking',
  },
  {
    id: 'txn_019',
    title: 'Water Bill',
    amount: 34.50,
    category: 'bills',
    timestamp: '2025-12-27T00:00:00Z',
    type: 'expense',
    notes: 'Monthly water utility',
  },
  {
    id: 'txn_020',
    title: 'Subway Lunch',
    amount: 12.50,
    category: 'food',
    timestamp: '2025-12-26T12:30:00Z',
    type: 'expense',
    notes: 'Quick lunch',
  },
];

/**
 * Calculate category spending from transactions
 */
export const MOCK_CATEGORY_SPENDING: CategorySpending[] = [
  {
    category: 'bills',
    categoryLabel: 'Bills & Utilities',
    totalAmount: 204.99,
    percentage: 30,
    transactionCount: 3,
    colorToken: 'var(--color-category-bills)',
  },
  {
    category: 'shopping',
    categoryLabel: 'Shopping',
    totalAmount: 299.95,
    percentage: 26,
    transactionCount: 2,
    colorToken: 'var(--color-category-shopping)',
  },
  {
    category: 'food',
    categoryLabel: 'Food & Dining',
    totalAmount: 185.65,
    percentage: 20,
    transactionCount: 4,
    colorToken: 'var(--color-category-food)',
  },
  {
    category: 'transport',
    categoryLabel: 'Transport',
    totalAmount: 97.95,
    percentage: 12,
    transactionCount: 2,
    colorToken: 'var(--color-category-transport)',
  },
  {
    category: 'health',
    categoryLabel: 'Health & Fitness',
    totalAmount: 107.30,
    percentage: 12,
    transactionCount: 2,
    colorToken: 'var(--color-category-health)',
  },
];

/**
 * Weekly spending trend
 */
export const MOCK_DAILY_SPENDING: DailySpending[] = [
  { date: '2026-01-02', dayLabel: 'Mon', totalAmount: 185.65 },
  { date: '2026-01-03', dayLabel: 'Tue', totalAmount: 118.89 },
  { date: '2026-01-04', dayLabel: 'Wed', totalAmount: 117.75 },
  { date: '2026-01-05', dayLabel: 'Thu', totalAmount: 245.80 },
  { date: '2026-01-06', dayLabel: 'Fri', totalAmount: 15.99 },
  { date: '2026-01-07', dayLabel: 'Sat', totalAmount: 52.00 },
  { date: '2026-01-08', dayLabel: 'Sun', totalAmount: 172.65 },
];

/**
 * Dashboard summary data
 */
export const MOCK_DASHBOARD_SUMMARY: DashboardSummary = {
  totalBalance: 8450.50,
  monthlyExpenses: 1750.00,
  monthlySavings: 1245.00,
  budgetLimit: 2500.00,
  budgetPercentageUsed: 70,
  topCategories: MOCK_CATEGORY_SPENDING,
  weeklySpending: MOCK_DAILY_SPENDING,
  recentTransactions: MOCK_TRANSACTIONS.slice(0, 8), // Add recent transactions
};

/**
 * Monthly comparison data
 */
export const MOCK_MONTHLY_COMPARISON: MonthlyComparison = {
  currentMonth: {
    label: 'January 2026',
    totalAmount: 1750.00,
  },
  previousMonth: {
    label: 'December 2025',
    totalAmount: 1920.00,
  },
  difference: -170.00,
  percentageChange: -8.85,
};

/**
 * Get category metadata by ID
 */
export function getCategoryMetadata(categoryId: string): CategoryMetadata | undefined {
  return CATEGORY_METADATA.find(cat => cat.id === categoryId);
}

/**
 * Filter transactions by type
 */
export function filterTransactionsByType(
  transactions: Transaction[],
  type?: 'expense' | 'income'
): Transaction[] {
  if (!type) return transactions;
  return transactions.filter(txn => txn.type === type);
}

/**
 * Calculate total amount for transactions
 */
export function calculateTotalAmount(transactions: Transaction[]): number {
  return transactions.reduce((sum, txn) => {
    return txn.type === 'expense' ? sum + txn.amount : sum;
  }, 0);
}