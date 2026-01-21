/**
 * Premium Glassmorphism Card Component
 * Sophisticated glass effect with backdrop blur
 */

import type { ReactNode } from 'react';

export interface PremiumCardProps {
  children: ReactNode;
  variant?: 'glass' | 'solid' | 'gradient';
  hover?: boolean;
  glow?: boolean;
  onClick?: () => void;
  className?: string;
}

const variantStyles = {
  glass: `
    bg-[var(--premium-glass-bg)]
    backdrop-blur-[20px]
    border border-[var(--premium-glass-border)]
    shadow-[var(--premium-shadow-md)]
  `,
  solid: `
    bg-[var(--premium-surface-3)]
    border border-[var(--premium-surface-4)]
    shadow-[var(--premium-shadow-lg)]
  `,
  gradient: `
    bg-gradient-to-br from-[var(--premium-surface-3)] to-[var(--premium-surface-2)]
    border border-[var(--premium-glass-border)]
    shadow-[var(--premium-shadow-lg)]
  `,
};

export function PremiumCard({
  children,
  variant = 'glass',
  hover = false,
  glow = false,
  onClick,
  className = '',
}: PremiumCardProps) {
  const isInteractive = !!onClick || hover;

  return (
    <div
      onClick={onClick}
      className={`
        rounded-[var(--premium-radius-xl)]
        transition-all duration-[var(--premium-transition-base)]
        ${variantStyles[variant]}
        ${isInteractive ? 'cursor-pointer hover:bg-[rgba(255,255,255,0.08)] hover:shadow-[var(--premium-shadow-xl)] hover:-translate-y-1' : ''}
        ${glow ? 'hover:shadow-[var(--premium-glow-primary)]' : ''}
        ${className}
      `}
    >
      {children}
    </div>
  );
}
