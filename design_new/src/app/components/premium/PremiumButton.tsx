/**
 * Premium Button Component
 * Modern gradients with hover effects
 */

import type { ReactNode } from 'react';

export type PremiumButtonVariant = 'gradient-primary' | 'gradient-secondary' | 'glass' | 'outline' | 'ghost';
export type PremiumButtonSize = 'sm' | 'md' | 'lg' | 'xl';

export interface PremiumButtonProps {
  children: ReactNode;
  variant?: PremiumButtonVariant;
  size?: PremiumButtonSize;
  fullWidth?: boolean;
  icon?: ReactNode;
  iconPosition?: 'left' | 'right';
  disabled?: boolean;
  onClick?: () => void;
  type?: 'button' | 'submit' | 'reset';
  className?: string;
}

const variantStyles: Record<PremiumButtonVariant, string> = {
  'gradient-primary': `
    bg-gradient-to-r from-[#667eea] to-[#764ba2]
    text-white
    shadow-[var(--premium-shadow-md)]
    hover:shadow-[var(--premium-glow-primary)]
  `,
  'gradient-secondary': `
    bg-gradient-to-r from-[#f093fb] to-[#f5576c]
    text-white
    shadow-[var(--premium-shadow-md)]
    hover:shadow-[var(--premium-glow-secondary)]
  `,
  glass: `
    bg-[var(--premium-glass-bg)]
    backdrop-blur-[20px]
    border border-[var(--premium-glass-border)]
    text-[var(--premium-text-primary)]
    hover:bg-[rgba(255,255,255,0.1)]
  `,
  outline: `
    bg-transparent
    border-2 border-[var(--premium-primary)]
    text-[var(--premium-primary)]
    hover:bg-[var(--premium-primary)]/10
  `,
  ghost: `
    bg-transparent
    text-[var(--premium-text-secondary)]
    hover:bg-[var(--premium-surface-2)]
  `,
};

const sizeStyles: Record<PremiumButtonSize, string> = {
  sm: 'h-[36px] px-[var(--premium-space-md)] text-[14px]',
  md: 'h-[44px] px-[var(--premium-space-lg)] text-[16px]',
  lg: 'h-[52px] px-[var(--premium-space-xl)] text-[18px]',
  xl: 'h-[60px] px-[var(--premium-space-2xl)] text-[20px]',
};

export function PremiumButton({
  children,
  variant = 'gradient-primary',
  size = 'md',
  fullWidth = false,
  icon,
  iconPosition = 'left',
  disabled = false,
  onClick,
  type = 'button',
  className = '',
}: PremiumButtonProps) {
  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`
        rounded-[var(--premium-radius-full)]
        font-medium
        transition-all duration-[var(--premium-transition-base)]
        flex items-center justify-center gap-[var(--premium-space-sm)]
        ${variantStyles[variant]}
        ${sizeStyles[size]}
        ${fullWidth ? 'w-full' : ''}
        ${disabled ? 'opacity-40 cursor-not-allowed' : 'cursor-pointer active:scale-95'}
        ${className}
      `}
    >
      {icon && iconPosition === 'left' && <span className="flex items-center">{icon}</span>}
      {children}
      {icon && iconPosition === 'right' && <span className="flex items-center">{icon}</span>}
    </button>
  );
}
