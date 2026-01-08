/**
 * M3 Button Component
 * Strictly follows Material Design 3 specifications
 * Maps 1:1 to Jetpack Compose Button()
 */

import type { ReactNode } from 'react';

export type M3ButtonVariant = 'filled' | 'filled-tonal' | 'outlined' | 'text' | 'elevated';
export type M3ButtonSize = 'default' | 'large';

export interface M3ButtonProps {
  /** Button content */
  children: ReactNode;
  /** M3 button variant */
  variant?: M3ButtonVariant;
  /** Button size */
  size?: M3ButtonSize;
  /** Disabled state */
  disabled?: boolean;
  /** Full width button */
  fullWidth?: boolean;
  /** Click handler */
  onClick?: () => void;
  /** Button type for forms */
  type?: 'button' | 'submit' | 'reset';
  /** Leading icon */
  icon?: ReactNode;
  /** Additional CSS classes */
  className?: string;
}

/**
 * M3 Button Variant Styles
 * Using M3 color tokens and surface tints (no shadows)
 */
const variantStyles: Record<M3ButtonVariant, string> = {
  'filled': `
    bg-[var(--md-sys-color-primary)]
    text-[var(--md-sys-color-on-primary)]
    hover:bg-[var(--md-sys-color-primary)]/90
  `,
  'filled-tonal': `
    bg-[var(--md-sys-color-secondary-container)]
    text-[var(--md-sys-color-on-secondary-container)]
    hover:bg-[var(--md-sys-color-secondary-container)]/80
  `,
  'elevated': `
    bg-[var(--md-sys-elevation-level1)]
    text-[var(--md-sys-color-primary)]
    hover:bg-[var(--md-sys-elevation-level2)]
  `,
  'outlined': `
    border border-[var(--md-sys-color-outline)]
    text-[var(--md-sys-color-primary)]
    bg-transparent
    hover:bg-[var(--md-sys-color-primary)]/[0.08]
  `,
  'text': `
    text-[var(--md-sys-color-primary)]
    bg-transparent
    hover:bg-[var(--md-sys-color-primary)]/[0.08]
  `,
};

const sizeStyles: Record<M3ButtonSize, string> = {
  default: 'h-[40px] px-[var(--md-sys-spacing-lg)]',
  large: 'h-[48px] px-[var(--md-sys-spacing-xl)]',
};

/**
 * Material 3 Button Component
 * 
 * @example
 * <M3Button variant="filled" onClick={handleClick}>
 *   Add Expense
 * </M3Button>
 */
export function M3Button({
  children,
  variant = 'filled',
  size = 'default',
  disabled = false,
  fullWidth = false,
  onClick,
  type = 'button',
  icon,
  className = '',
}: M3ButtonProps) {
  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`
        rounded-[var(--md-sys-shape-corner-full)]
        transition-all duration-200
        label-large
        flex items-center justify-center gap-[var(--md-sys-spacing-sm)]
        ${variantStyles[variant]}
        ${sizeStyles[size]}
        ${fullWidth ? 'w-full' : ''}
        ${disabled ? 'opacity-40 cursor-not-allowed' : 'cursor-pointer active:scale-95'}
        ${className}
      `}
    >
      {icon && <span className="flex items-center">{icon}</span>}
      {children}
    </button>
  );
}
