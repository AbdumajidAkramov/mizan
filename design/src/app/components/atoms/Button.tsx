/**
 * Button Atom Component
 * Material 3 Design - Filled, Outlined, and Text variants
 */

import type { ReactNode } from 'react';

export type ButtonVariant = 'filled' | 'outlined' | 'text' | 'elevated';
export type ButtonSize = 'small' | 'medium' | 'large';

export interface ButtonProps {
  /** Button content */
  children: ReactNode;
  /** Visual variant following Material 3 */
  variant?: ButtonVariant;
  /** Size variant - maps to 8dp grid */
  size?: ButtonSize;
  /** Disabled state */
  disabled?: boolean;
  /** Full width button */
  fullWidth?: boolean;
  /** Click handler */
  onClick?: () => void;
  /** Button type for forms */
  type?: 'button' | 'submit' | 'reset';
  /** Additional CSS classes */
  className?: string;
}

const variantStyles: Record<ButtonVariant, string> = {
  filled: 'bg-[var(--color-primary)] text-[var(--color-on-primary)] shadow-[var(--elevation-1)] hover:shadow-[var(--elevation-2)]',
  elevated: 'bg-[var(--color-surface-variant)] text-[var(--color-on-surface-variant)] shadow-[var(--elevation-2)] hover:shadow-[var(--elevation-3)]',
  outlined: 'border border-[var(--color-outline)] text-[var(--color-primary)] bg-transparent hover:bg-[var(--color-primary-container)]',
  text: 'text-[var(--color-primary)] bg-transparent hover:bg-[var(--color-primary-container)]',
};

const sizeStyles: Record<ButtonSize, string> = {
  small: 'h-[32px] px-[var(--spacing-md)] text-sm',
  medium: 'h-[40px] px-[var(--spacing-lg)] text-base',
  large: 'h-[48px] px-[var(--spacing-xl)] text-base',
};

/**
 * Material 3 Button Component
 * Maps 1:1 to Android Button composable
 */
export function Button({
  children,
  variant = 'filled',
  size = 'medium',
  disabled = false,
  fullWidth = false,
  onClick,
  type = 'button',
  className = '',
}: ButtonProps) {
  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`
        rounded-full
        transition-all duration-200
        font-medium
        ${variantStyles[variant]}
        ${sizeStyles[size]}
        ${fullWidth ? 'w-full' : ''}
        ${disabled ? 'opacity-40 cursor-not-allowed' : 'cursor-pointer active:scale-95'}
        ${className}
      `}
    >
      {children}
    </button>
  );
}
