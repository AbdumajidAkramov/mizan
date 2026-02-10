/**
 * Premium Confirmation Dialog
 * Elegant glassmorphism confirmation modal for destructive actions
 * 
 * @architecture Material 3 with glassmorphism
 * @design Emerald Green (#10B981) accents for primary, Red for destructive
 */

import { AlertTriangle, X } from 'lucide-react';

export interface PremiumConfirmDialogProps {
  /** Whether dialog is open */
  isOpen: boolean;
  /** Dialog title */
  title: string;
  /** Dialog message/description */
  message: string;
  /** Confirm button text */
  confirmText?: string;
  /** Cancel button text */
  cancelText?: string;
  /** Callback when confirmed */
  onConfirm: () => void;
  /** Callback when cancelled/closed */
  onCancel: () => void;
  /** Whether this is a destructive action (red styling) */
  isDestructive?: boolean;
}

export function PremiumConfirmDialog({
  isOpen,
  title,
  message,
  confirmText = 'Confirm',
  cancelText = 'Cancel',
  onConfirm,
  onCancel,
  isDestructive = false,
}: PremiumConfirmDialogProps) {
  if (!isOpen) return null;

  return (
    <>
      {/* Backdrop */}
      <div
        className="fixed inset-0 bg-black/50 backdrop-blur-sm z-[200] animate-[fadeIn_0.2s_ease-out]"
        onClick={onCancel}
      />

      {/* Dialog */}
      <div className="fixed inset-0 flex items-center justify-center z-[200] p-[var(--premium-space-md)]">
        <div
          className="
            w-full max-w-[400px]
            bg-[var(--premium-bg-secondary)]
            rounded-[var(--premium-radius-2xl)]
            overflow-hidden
            shadow-[var(--premium-shadow-2xl)]
            animate-[scaleIn_0.2s_ease-out]
          "
          onClick={(e) => e.stopPropagation()}
        >
          {/* Header */}
          <div className="
            p-[var(--premium-space-lg)]
            flex items-start gap-[var(--premium-space-md)]
            border-b border-[var(--premium-glass-border)]
          ">
            {/* Icon */}
            <div
              className={`
                w-[48px] h-[48px]
                rounded-full
                flex items-center justify-center
                flex-shrink-0
                ${isDestructive
                  ? 'bg-[var(--premium-error)]/10'
                  : 'bg-[var(--premium-emerald)]/10'
                }
              `}
            >
              <AlertTriangle
                size={24}
                className={isDestructive ? 'text-[var(--premium-error)]' : 'text-[var(--premium-emerald)]'}
              />
            </div>

            {/* Title */}
            <div className="flex-1">
              <h3 className="heading-md text-[var(--premium-text-primary)]">
                {title}
              </h3>
            </div>

            {/* Close Button */}
            <button
              onClick={onCancel}
              className="
                w-[32px] h-[32px]
                rounded-full
                bg-[var(--premium-surface-2)]
                hover:bg-[var(--premium-surface-3)]
                flex items-center justify-center
                text-[var(--premium-text-secondary)]
                transition-all duration-200
                active:scale-95
                flex-shrink-0
              "
              aria-label="Close"
            >
              <X size={16} />
            </button>
          </div>

          {/* Message */}
          <div className="p-[var(--premium-space-lg)]">
            <p className="body-md text-[var(--premium-text-secondary)] leading-relaxed">
              {message}
            </p>
          </div>

          {/* Actions */}
          <div className="
            p-[var(--premium-space-lg)]
            border-t border-[var(--premium-glass-border)]
            flex gap-[var(--premium-space-md)]
          ">
            <button
              onClick={onCancel}
              className="
                flex-1
                p-[var(--premium-space-md)]
                rounded-[var(--premium-radius-lg)]
                bg-[var(--premium-surface-2)]
                hover:bg-[var(--premium-surface-3)]
                text-[var(--premium-text-secondary)]
                font-medium
                transition-all duration-200
                active:scale-95
              "
            >
              {cancelText}
            </button>
            <button
              onClick={() => {
                onConfirm();
                onCancel(); // Close dialog after confirmation
              }}
              className={`
                flex-1
                p-[var(--premium-space-md)]
                rounded-[var(--premium-radius-lg)]
                text-white
                font-medium
                transition-all duration-200
                active:scale-95
                ${isDestructive
                  ? 'bg-gradient-to-r from-[var(--premium-error)] to-[var(--premium-error-dark)] hover:shadow-[0_0_20px_rgba(255,107,107,0.3)]'
                  : 'bg-gradient-to-r from-[var(--premium-emerald)] to-[var(--premium-emerald-dark)] hover:shadow-[var(--premium-glow-success)]'
                }
              `}
            >
              {confirmText}
            </button>
          </div>
        </div>
      </div>
    </>
  );
}
