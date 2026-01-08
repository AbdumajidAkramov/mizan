/**
 * M3 Card Component
 * Material Design 3 card with elevation using surface tints
 * Maps 1:1 to Jetpack Compose Card()
 */

import type { ReactNode } from 'react';

export type M3CardVariant = 'elevated' | 'filled' | 'outlined';

export interface M3CardProps {
  /** Card content */
  children: ReactNode;
  /** Card variant */
  variant?: M3CardVariant;
  /** Click handler (makes card interactive) */
  onClick?: () => void;
  /** Additional CSS classes */
  className?: string;
}

/**
 * M3 Card variant styles
 * Using surface tints for elevation (no box shadows)
 */
const variantStyles: Record<M3CardVariant, string> = {
  elevated: `
    bg-[var(--md-sys-elevation-level1)]
    text-[var(--md-sys-color-on-surface)]
  `,
  filled: `
    bg-[var(--md-sys-color-surface-variant)]
    text-[var(--md-sys-color-on-surface)]
  `,
  outlined: `
    bg-[var(--md-sys-color-surface)]
    text-[var(--md-sys-color-on-surface)]
    border border-[var(--md-sys-color-outline-variant)]
  `,
};

/**
 * Material 3 Card Component
 * 
 * @example
 * <M3Card variant="elevated">
 *   <h3>Card Title</h3>
 *   <p>Card content goes here</p>
 * </M3Card>
 */
export function M3Card({
  children,
  variant = 'elevated',
  onClick,
  className = '',
}: M3CardProps) {
  const isInteractive = !!onClick;

  return (
    <div
      onClick={onClick}
      className={`
        rounded-[var(--md-sys-shape-corner-medium)]
        transition-all duration-200
        ${variantStyles[variant]}
        ${isInteractive ? 'cursor-pointer hover:bg-opacity-90 active:scale-[0.98]' : ''}
        ${className}
      `}
    >
      {children}
    </div>
  );
}
