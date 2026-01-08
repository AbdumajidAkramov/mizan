/**
 * IconButton Atom Component
 * Material 3 Design - Icon-only interactive button
 */

import type { ReactNode } from 'react';

export interface IconButtonProps {
  /** Icon element (from lucide-react) */
  icon: ReactNode;
  /** Accessibility label */
  label: string;
  /** Click handler */
  onClick?: () => void;
  /** Disabled state */
  disabled?: boolean;
  /** Additional CSS classes */
  className?: string;
}

/**
 * Material 3 IconButton Component
 * 48x48dp touch target (Material 3 standard)
 */
export function IconButton({
  icon,
  label,
  onClick,
  disabled = false,
  className = '',
}: IconButtonProps) {
  return (
    <button
      onClick={onClick}
      disabled={disabled}
      aria-label={label}
      className={`
        w-[48px] h-[48px]
        rounded-full
        flex items-center justify-center
        bg-[var(--color-surface-variant)]
        text-[var(--color-on-surface-variant)]
        transition-all duration-200
        hover:bg-[var(--color-secondary-container)]
        active:scale-90
        ${disabled ? 'opacity-40 cursor-not-allowed' : 'cursor-pointer'}
        ${className}
      `}
    >
      {icon}
    </button>
  );
}
