/**
 * Horizontal Account Carousel Component
 * Smooth scrolling carousel with snap behavior
 */

import { useRef } from 'react';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import { HorizontalAccountCard, type Account } from './HorizontalAccountCard';

export interface HorizontalAccountCarouselProps {
  label: string;
  accounts: Account[];
  selectedAccountId?: string;
  onSelectAccount: (accountId: string) => void;
}

export function HorizontalAccountCarousel({
  label,
  accounts,
  selectedAccountId,
  onSelectAccount,
}: HorizontalAccountCarouselProps) {
  const scrollRef = useRef<HTMLDivElement>(null);

  const scroll = (direction: 'left' | 'right') => {
    if (scrollRef.current) {
      const scrollAmount = 300;
      scrollRef.current.scrollBy({
        left: direction === 'left' ? -scrollAmount : scrollAmount,
        behavior: 'smooth',
      });
    }
  };

  return (
    <div className="space-y-[var(--premium-space-md)]">
      {/* Label */}
      <h3 className="
        body-md font-medium
        text-[var(--premium-text-secondary)]
        px-[var(--premium-space-md)]
      ">
        {label}
      </h3>

      {/* Carousel Container */}
      <div className="relative">
        {/* Left Scroll Button */}
        {accounts.length > 1 && (
          <button
            onClick={() => scroll('left')}
            className="
              absolute left-0 top-1/2 -translate-y-1/2 z-10
              w-[40px] h-[40px]
              rounded-full
              backdrop-blur-[20px]
              bg-[var(--premium-glass-bg)]
              border border-[var(--premium-glass-border)]
              flex items-center justify-center
              text-[var(--premium-text-primary)]
              hover:bg-[var(--premium-surface-3)]
              active:scale-95
              transition-all duration-200
              shadow-[var(--premium-shadow-md)]
              -ml-[20px]
            "
          >
            <ChevronLeft size={20} strokeWidth={2} />
          </button>
        )}

        {/* Scrollable Cards Container */}
        <div
          ref={scrollRef}
          className="
            flex gap-[var(--premium-space-md)]
            overflow-x-auto
            scroll-smooth
            snap-x snap-mandatory
            px-[var(--premium-space-md)]
            pb-[var(--premium-space-sm)]
            -mx-[var(--premium-space-md)]
            
            /* Hide scrollbar */
            scrollbar-hide
            [&::-webkit-scrollbar]:hidden
            [-ms-overflow-style:none]
            [scrollbar-width:none]
          "
        >
          {accounts.map((account) => (
            <div
              key={account.id}
              className="snap-center"
            >
              <HorizontalAccountCard
                account={account}
                isSelected={selectedAccountId === account.id}
                onSelect={() => onSelectAccount(account.id)}
              />
            </div>
          ))}
        </div>

        {/* Right Scroll Button */}
        {accounts.length > 1 && (
          <button
            onClick={() => scroll('right')}
            className="
              absolute right-0 top-1/2 -translate-y-1/2 z-10
              w-[40px] h-[40px]
              rounded-full
              backdrop-blur-[20px]
              bg-[var(--premium-glass-bg)]
              border border-[var(--premium-glass-border)]
              flex items-center justify-center
              text-[var(--premium-text-primary)]
              hover:bg-[var(--premium-surface-3)]
              active:scale-95
              transition-all duration-200
              shadow-[var(--premium-shadow-md)]
              -mr-[20px]
            "
          >
            <ChevronRight size={20} strokeWidth={2} />
          </button>
        )}
      </div>

      {/* Scroll Indicators */}
      {accounts.length > 1 && (
        <div className="flex justify-center gap-[6px] pt-[var(--premium-space-sm)]">
          {accounts.map((account) => (
            <div
              key={account.id}
              className={`
                h-[3px]
                rounded-full
                transition-all duration-300
                ${selectedAccountId === account.id
                  ? 'w-[24px] bg-[var(--premium-emerald)]'
                  : 'w-[8px] bg-[var(--premium-surface-3)]'
                }
              `}
            />
          ))}
        </div>
      )}
    </div>
  );
}
