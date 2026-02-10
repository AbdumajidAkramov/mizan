/**
 * Premium Floating Action Button (FAB)
 * Material 3 style FAB with emerald green gradient
 * 
 * @architecture Material 3 with glassmorphism
 * @design Emerald Green (#10B981) primary color
 */

import { Plus } from 'lucide-react';

export interface PremiumFABProps {
  /** Callback when FAB is clicked */
  onClick: () => void;
  /** Optional custom icon (defaults to Plus) */
  icon?: React.ReactNode;
  /** Optional label text */
  label?: string;
  /** Position on screen */
  position?: 'bottom-right' | 'bottom-center' | 'bottom-left';
  /** Size variant */
  size?: 'normal' | 'large';
  /** Optional custom className */
  className?: string;
}

export function PremiumFAB({
  onClick,
  icon,
  label,
  position = 'bottom-right',
  size = 'normal',
  className = '',
}: PremiumFABProps) {
  const positionStyles = {
    'bottom-right': 'right-[var(--premium-space-lg)] bottom-[var(--premium-space-lg)]',
    'bottom-center': 'left-1/2 -translate-x-1/2 bottom-[var(--premium-space-lg)]',
    'bottom-left': 'left-[var(--premium-space-lg)] bottom-[var(--premium-space-lg)]',
  };

  const sizeStyles = {
    normal: label
      ? 'px-[var(--premium-space-lg)] py-[var(--premium-space-md)] rounded-[var(--premium-radius-full)]'
      : 'w-[56px] h-[56px] rounded-full',
    large: label
      ? 'px-[var(--premium-space-xl)] py-[var(--premium-space-lg)] rounded-[var(--premium-radius-full)]'
      : 'w-[64px] h-[64px] rounded-full',
  };

  const iconSize = size === 'large' ? 28 : 24;

  return (
    <button
      onClick={onClick}
      className={`
        fixed z-50
        ${positionStyles[position]}
        ${sizeStyles[size]}
        bg-gradient-to-r from-[var(--premium-emerald)] to-[var(--premium-emerald-dark)]
        hover:shadow-[var(--premium-glow-success)]
        shadow-[var(--premium-shadow-xl)]
        text-white
        font-medium
        flex items-center justify-center gap-[var(--premium-space-sm)]
        transition-all duration-200
        active:scale-95
        hover:scale-105
        ${className}
      `}
      aria-label={label || 'Add new'}
    >
      {icon || <Plus size={iconSize} strokeWidth={2.5} />}
      {label && (
        <span className={size === 'large' ? 'body-lg' : 'body-md'}>
          {label}
        </span>
      )}
    </button>
  );
}
