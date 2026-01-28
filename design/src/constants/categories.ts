/**
 * Category Constants
 * Centralized category metadata for icons and labels
 */

import {
  Utensils,
  Car,
  ShoppingBag,
  Home,
  Coffee,
  Wallet,
  TrendingUp,
  type LucideIcon,
} from 'lucide-react';
import type { TransactionCategory } from '../types/domain';

export const CATEGORY_ICONS: Record<TransactionCategory, LucideIcon> = {
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

export const CATEGORY_LABELS: Record<TransactionCategory, string> = {
  'food-dining': 'Food & Dining',
  'transportation': 'Transportation',
  'shopping': 'Shopping',
  'bills-utilities': 'Bills & Utilities',
  'entertainment': 'Entertainment',
  'healthcare': 'Healthcare',
  'travel': 'Travel',
  'technology': 'Technology',
  'income-salary': 'Income',
  'other': 'Other',
};

export const CATEGORY_COLORS: Record<TransactionCategory, string> = {
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
