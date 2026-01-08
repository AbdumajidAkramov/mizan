/**
 * EmptyState Molecule Component
 * Displays when no data is available
 */

import type { ReactNode } from 'react';

export interface EmptyStateProps {
  /** Icon to display */
  icon: ReactNode;
  /** Title text */
  title: string;
  /** Description text */
  description: string;
  /** Optional action button */
  action?: ReactNode;
}

/**
 * Material 3 Empty State Component
 * Shows when lists or sections have no data
 */
export function EmptyState({ icon, title, description, action }: EmptyStateProps) {
  return (
    <div className="flex flex-col items-center justify-center py-[var(--spacing-3xl)] px-[var(--spacing-lg)]">
      <div className="
        w-[80px] h-[80px]
        rounded-full
        bg-[var(--color-surface-variant)]
        text-[var(--color-on-surface-variant)]
        flex items-center justify-center
        mb-[var(--spacing-lg)]
      ">
        {icon}
      </div>
      <h3 className="text-[var(--color-on-surface)] mb-[var(--spacing-sm)]">
        {title}
      </h3>
      <p className="text-sm text-[var(--color-on-surface-variant)] text-center mb-[var(--spacing-lg)] max-w-[280px]">
        {description}
      </p>
      {action}
    </div>
  );
}
