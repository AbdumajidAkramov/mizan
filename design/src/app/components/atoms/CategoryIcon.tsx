/**
 * CategoryIcon Atom Component
 * Displays category icon with consistent styling
 */

import { Utensils, Car, ShoppingBag, Receipt, Film, Heart, Plane, Smartphone, TrendingUp } from 'lucide-react';
import type { TransactionCategory } from '../../../types/domain';

export interface CategoryIconProps {
  /** Category identifier */
  category: TransactionCategory;
  /** Icon size in pixels (must follow 8dp grid) */
  size?: 16 | 20 | 24 | 32;
  /** Icon color */
  color?: string;
  /** Additional CSS classes */
  className?: string;
}

/** Icon mapping for each category */
const CATEGORY_ICONS: Record<TransactionCategory, typeof Utensils> = {
  food: Utensils,
  transport: Car,
  shopping: ShoppingBag,
  bills: Receipt,
  entertainment: Film,
  health: Heart,
  travel: Plane,
  tech: Smartphone,
  income: TrendingUp,
};

/**
 * Material 3 Category Icon Component
 * Maps to Android Icon composable
 */
export function CategoryIcon({ category, size = 24, color, className = '' }: CategoryIconProps) {
  const IconComponent = CATEGORY_ICONS[category];
  
  return <IconComponent size={size} color={color} className={className} />;
}