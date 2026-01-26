/**
 * ErrorState Molecule Component
 * Displays error messages with retry option
 */

import { AlertCircle } from 'lucide-react';
import { Button } from '../atoms/Button';

export interface ErrorStateProps {
  /** Error message */
  message: string;
  /** Optional retry callback */
  onRetry?: () => void;
}

/**
 * Material 3 Error State Component
 * Shows when data loading fails
 */
export function ErrorState({ message, onRetry }: ErrorStateProps) {
  return (
    <div className="flex flex-col items-center justify-center py-[var(--spacing-3xl)] px-[var(--spacing-lg)]">
      <div className="
        w-[80px] h-[80px]
        rounded-full
        bg-[var(--color-error-container)]
        text-[var(--color-on-error-container)]
        flex items-center justify-center
        mb-[var(--spacing-lg)]
      ">
        <AlertCircle size={40} />
      </div>
      <h3 className="text-[var(--color-on-surface)] mb-[var(--spacing-sm)]">
        Something went wrong
      </h3>
      <p className="text-sm text-[var(--color-on-surface-variant)] text-center mb-[var(--spacing-lg)] max-w-[280px]">
        {message}
      </p>
      {onRetry && (
        <Button onClick={onRetry} variant="outlined">
          Try Again
        </Button>
      )}
    </div>
  );
}
