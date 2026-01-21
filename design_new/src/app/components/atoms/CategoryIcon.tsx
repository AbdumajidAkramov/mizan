/**
 * CategoryIcon Component
 * Renders category icons from lucide-react based on category metadata
 */

import * as Icons from 'lucide-react';
import type { TransactionCategory } from '../../../types/domain';
import { CATEGORY_METADATA } from '../../../mocks/data';

export interface CategoryIconProps {
  category: TransactionCategory;
  size?: number;
  color?: string;
  className?: string;
}

/**
 * Renders the appropriate icon for a given category
 */
export function CategoryIcon({
  category,
  size = 24,
  color,
  className = '',
}: CategoryIconProps) {
  // Get category metadata
  const metadata = CATEGORY_METADATA.find((cat) => cat.id === category);
  
  if (!metadata) {
    // Fallback to a generic icon
    return <Icons.Tag size={size} color={color} className={className} />;
  }

  // Get the icon component from lucide-react
  const IconComponent = (Icons as any)[metadata.iconName] || Icons.Tag;

  return <IconComponent size={size} color={color} className={className} />;
}
