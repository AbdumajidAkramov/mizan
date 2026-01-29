/**
 * Premium Subcategory Selector - Overlay Modal
 * Elegant vertical card list for subcategory selection
 * Follows same premium style as Category Selector
 * 
 * @architecture Material 3 with glassmorphism
 * @design Emerald Green (#10B981) accents, text-only cards
 */

import { Check, ChevronLeft } from 'lucide-react';

export interface SubcategoryOption {
  id: string;
  label: string;
}

export interface PremiumSubcategorySelectorProps {
  /** Whether the overlay is open */
  isOpen: boolean;
  /** Parent category label for display */
  categoryLabel: string;
  /** Current selected subcategory */
  selectedSubcategory?: string;
  /** Available subcategories to display */
  subcategories: SubcategoryOption[];
  /** Callback when subcategory is selected */
  onSelectSubcategory: (subcategory: string) => void;
  /** Callback to go back to category selection */
  onBack: () => void;
  /** Callback when overlay is closed */
  onClose: () => void;
  /** Optional: Allow skipping subcategory selection */
  allowSkip?: boolean;
}

export function PremiumSubcategorySelector({
  isOpen,
  categoryLabel,
  selectedSubcategory,
  subcategories,
  onSelectSubcategory,
  onBack,
  onClose,
  allowSkip = true,
}: PremiumSubcategorySelectorProps) {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-[120] flex items-center justify-center animate-[fadeIn_0.2s_ease-out]">
      {/* Backdrop */}
      <div
        className="absolute inset-0 bg-black/40 backdrop-blur-sm"
        onClick={onClose}
      />

      {/* Overlay Modal */}
      <div
        className="
          relative
          w-full
          max-w-[400px]
          max-h-[80vh]
          mx-[var(--premium-space-lg)]
          bg-[var(--premium-surface)]
          rounded-[var(--premium-radius-2xl)]
          shadow-[var(--premium-shadow-2xl)]
          overflow-hidden
          animate-[scaleIn_0.2s_ease-out]
          flex flex-col
        "
      >
        {/* Header - Fixed */}
        <div className="px-[var(--premium-space-lg)] pt-[var(--premium-space-lg)] pb-[var(--premium-space-md)] flex-shrink-0">
          {/* Back Button + Title */}
          <div className="flex items-center gap-[var(--premium-space-sm)] mb-[4px]">
            <button
              onClick={onBack}
              className="
                w-[32px] h-[32px]
                rounded-full
                bg-[var(--premium-surface-2)]
                flex items-center justify-center
                text-[var(--premium-text-secondary)]
                hover:bg-[var(--premium-surface-3)]
                active:scale-95
                transition-all duration-200
              "
              aria-label="Go back"
            >
              <ChevronLeft size={20} />
            </button>
            <h3 className="heading-md text-[var(--premium-text-primary)]">
              {categoryLabel}
            </h3>
          </div>
          <p className="body-sm text-[var(--premium-text-tertiary)] ml-[40px]">
            Choose a subcategory (optional)
          </p>
        </div>

        {/* Subcategory List - Scrollable */}
        <div className="flex-1 overflow-y-auto px-[var(--premium-space-lg)] pb-[var(--premium-space-lg)]">
          <div className="space-y-[8px]">
            {/* Skip Option - No Subcategory */}
            {allowSkip && (
              <button
                onClick={() => {
                  onSelectSubcategory('');
                  onClose();
                }}
                className={`
                  w-full
                  p-[var(--premium-space-md)]
                  rounded-[var(--premium-radius-xl)]
                  flex items-center gap-[var(--premium-space-md)]
                  transition-all duration-200
                  ${!selectedSubcategory
                    ? 'bg-[var(--premium-emerald)]/10 border-2 border-[var(--premium-emerald)] shadow-[0_0_0_4px_rgba(16,185,129,0.1)]'
                    : 'bg-[var(--premium-surface-2)] border-2 border-transparent hover:bg-[var(--premium-surface-3)]'
                  }
                  active:scale-[0.98]
                `}
              >
                {/* Text Content */}
                <div className="flex-1 text-left">
                  <p
                    className={`
                      body-md font-medium mb-[2px]
                      ${!selectedSubcategory ? 'text-[var(--premium-emerald)]' : 'text-[var(--premium-text-primary)]'}
                    `}
                  >
                    No Subcategory
                  </p>
                  <p className="body-sm text-[var(--premium-text-tertiary)]">
                    Skip subcategory selection
                  </p>
                </div>

                {/* Check Icon */}
                {!selectedSubcategory && (
                  <div
                    className="
                      w-[24px] h-[24px]
                      rounded-full
                      bg-[var(--premium-emerald)]
                      flex items-center justify-center
                      flex-shrink-0
                    "
                  >
                    <Check size={14} className="text-white" strokeWidth={3} />
                  </div>
                )}
              </button>
            )}

            {/* Subcategory Options */}
            {subcategories.map((subcategory) => {
              const isSelected = selectedSubcategory === subcategory.id;

              return (
                <button
                  key={subcategory.id}
                  onClick={() => {
                    onSelectSubcategory(subcategory.id);
                    onClose();
                  }}
                  className={`
                    w-full
                    p-[var(--premium-space-md)]
                    rounded-[var(--premium-radius-xl)]
                    flex items-center gap-[var(--premium-space-md)]
                    transition-all duration-200
                    ${isSelected
                      ? 'bg-[var(--premium-emerald)]/10 border-2 border-[var(--premium-emerald)] shadow-[0_0_0_4px_rgba(16,185,129,0.1)]'
                      : 'bg-[var(--premium-surface-2)] border-2 border-transparent hover:bg-[var(--premium-surface-3)]'
                    }
                    active:scale-[0.98]
                  `}
                >
                  {/* Text Content */}
                  <div className="flex-1 text-left">
                    <p
                      className={`
                        body-md font-medium
                        ${isSelected ? 'text-[var(--premium-emerald)]' : 'text-[var(--premium-text-primary)]'}
                      `}
                    >
                      {subcategory.label}
                    </p>
                  </div>

                  {/* Check Icon */}
                  {isSelected && (
                    <div
                      className="
                        w-[24px] h-[24px]
                        rounded-full
                        bg-[var(--premium-emerald)]
                        flex items-center justify-center
                        flex-shrink-0
                      "
                    >
                      <Check size={14} className="text-white" strokeWidth={3} />
                    </div>
                  )}
                </button>
              );
            })}
          </div>
        </div>
      </div>

      <style>{`
        @keyframes scaleIn {
          from {
            opacity: 0;
            transform: scale(0.9);
          }
          to {
            opacity: 1;
            transform: scale(1);
          }
        }
      `}</style>
    </div>
  );
}
