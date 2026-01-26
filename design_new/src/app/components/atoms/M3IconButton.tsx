/**
 * M3 IconButton Component
 * Material Design 3 icon-only button with state layers
 * Maps 1:1 to Jetpack Compose IconButton()
 */

import type { ReactNode } from 'react';

export type M3IconButtonVariant = 'standard' | 'filled' | 'filled-tonal' | 'outlined';

export interface M3IconButtonProps {
  /** Icon element (from lucide-react) */
  icon: ReactNode;
  /** Accessibility label */
  label: string;
  /** Button variant */
  variant?: M3IconButtonVariant;
  /** Click handler */
  onClick?: () => void;
  /** Disabled state */
  disabled?: boolean;
  /** Additional CSS classes */
  className?: string;
}

/**
 * M3 IconButton variant styles
 * 48x48dp touch target (Material 3 standard)
 */
const variantStyles: Record<M3IconButtonVariant, string> = {
  standard: `
    bg-transparent
    text-[var(--md-sys-color-on-surface-variant)]
    hover:bg-[var(--md-sys-color-on-surface)]/[0.08]
  `,
  filled: `
    bg-[var(--md-sys-color-primary)]
    text-[var(--md-sys-color-on-primary)]
    hover:bg-[var(--md-sys-color-primary)]/90
  `,
  'filled-tonal': `
    bg-[var(--md-sys-color-secondary-container)]
    text-[var(--md-sys-color-on-secondary-container)]
    hover:bg-[var(--md-sys-color-secondary-container)]/80
  `,
  outlined: `
    border border-[var(--md-sys-color-outline)]
    text-[var(--md-sys-color-on-surface-variant)]
    bg-transparent
    hover:bg-[var(--md-sys-color-on-surface)]/[0.08]
  `,
};

/**
 * Material 3 IconButton Component
 * 
 * @example
 * <M3IconButton 
 *   icon={<X size={20} />}
 *   label="Close"
 *   variant="filled-tonal"
 *   onClick={handleClose}
 * />
 */
export function M3IconButton({
  icon,
  label,
  variant = 'standard',
  onClick,
  disabled = false,
  className = '',
}: M3IconButtonProps) {
  return (
    <button
      onClick={onClick}
      disabled={disabled}
      aria-label={label}
      className={`
        w-[48px] h-[48px]
        rounded-full
        flex items-center justify-center
        transition-all duration-200
        active:scale-90
        ${variantStyles[variant]}
        ${disabled ? 'opacity-40 cursor-not-allowed' : 'cursor-pointer'}
        ${className}
      `}
    >
      {icon}
    </button>
  );
}
