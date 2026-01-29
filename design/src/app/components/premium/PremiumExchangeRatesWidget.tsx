/**
 * Premium Exchange Rates Widget
 * Live currency exchange rates with glassmorphism design
 */

import { useState } from 'react';
import { RefreshCw, TrendingUp, TrendingDown } from 'lucide-react';
import { PremiumCard } from './PremiumCard';

export interface ExchangeRate {
  currencyPair: string;
  baseCurrency: string;
  quoteCurrency: string;
  rate: number;
  changePercent: number;
  flag: string; // Currency emoji flag
}

export interface PremiumExchangeRatesWidgetProps {
  /** Exchange rates data */
  rates?: ExchangeRate[];
  /** Callback when refresh button is clicked */
  onRefresh?: () => void;
  /** Loading state */
  isLoading?: boolean;
  /** Last update timestamp */
  lastUpdate?: Date;
}

// Mock exchange rates data for Uzbekistan Som (UZS)
const DEFAULT_RATES: ExchangeRate[] = [
  {
    currencyPair: 'USD/UZS',
    baseCurrency: 'USD',
    quoteCurrency: 'UZS',
    rate: 12850.0,
    changePercent: 0.45,
    flag: '🇺🇸',
  },
  {
    currencyPair: 'EUR/UZS',
    baseCurrency: 'EUR',
    quoteCurrency: 'UZS',
    rate: 13920.5,
    changePercent: -0.23,
    flag: '🇪🇺',
  },
  {
    currencyPair: 'RUB/UZS',
    baseCurrency: 'RUB',
    quoteCurrency: 'UZS',
    rate: 138.75,
    changePercent: 0.12,
    flag: '🇷🇺',
  },
  {
    currencyPair: 'GBP/UZS',
    baseCurrency: 'GBP',
    quoteCurrency: 'UZS',
    rate: 16245.0,
    changePercent: 0.67,
    flag: '🇬🇧',
  },
  {
    currencyPair: 'CNY/UZS',
    baseCurrency: 'CNY',
    quoteCurrency: 'UZS',
    rate: 1776.3,
    changePercent: -0.15,
    flag: '🇨🇳',
  },
];

export function PremiumExchangeRatesWidget({
  rates = DEFAULT_RATES,
  onRefresh,
  isLoading = false,
  lastUpdate,
}: PremiumExchangeRatesWidgetProps) {
  const [isRefreshing, setIsRefreshing] = useState(false);

  const handleRefresh = async () => {
    if (isRefreshing) return;
    
    setIsRefreshing(true);
    
    // Call external refresh handler if provided
    if (onRefresh) {
      await onRefresh();
    }
    
    // Simulate API call
    setTimeout(() => {
      setIsRefreshing(false);
    }, 1000);
  };

  const formatRate = (rate: number) => {
    return rate.toLocaleString('en-US', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    });
  };

  const formatTime = (date?: Date) => {
    if (!date) return 'Just now';
    const now = new Date();
    const diff = Math.floor((now.getTime() - date.getTime()) / 1000);
    
    if (diff < 60) return 'Just now';
    if (diff < 3600) return `${Math.floor(diff / 60)}m ago`;
    if (diff < 86400) return `${Math.floor(diff / 3600)}h ago`;
    return date.toLocaleDateString();
  };

  return (
    <PremiumCard variant="glass">
      <div className="p-[var(--premium-space-lg)]">
        {/* Header */}
        <div className="flex items-center justify-between mb-[var(--premium-space-lg)]">
          <div>
            <h3 className="heading-md text-[var(--premium-text-primary)] mb-[4px]">
              Exchange Rates
            </h3>
            <p className="body-xs text-[var(--premium-text-tertiary)]">
              {formatTime(lastUpdate)}
            </p>
          </div>
          
          <button
            onClick={handleRefresh}
            disabled={isRefreshing || isLoading}
            className={`
              w-[32px] h-[32px]
              rounded-full
              bg-[var(--premium-surface-2)]
              hover:bg-[var(--premium-emerald)]/10
              border border-[var(--premium-glass-border)]
              flex items-center justify-center
              transition-all duration-200
              active:scale-95
              disabled:opacity-50 disabled:cursor-not-allowed
              ${isRefreshing || isLoading ? 'animate-spin' : ''}
            `}
            aria-label="Refresh rates"
          >
            <RefreshCw 
              size={16} 
              strokeWidth={2.5}
              className="text-[var(--premium-text-secondary)]"
            />
          </button>
        </div>

        {/* Exchange Rates List */}
        <div className="space-y-[var(--premium-space-md)]">
          {(isLoading ? Array(3).fill(null) : rates).map((rate, index) => (
            <div
              key={rate ? rate.currencyPair : `skeleton-${index}`}
              className={`
                flex items-center justify-between
                p-[var(--premium-space-md)]
                rounded-[var(--premium-radius-lg)]
                bg-[var(--premium-surface-1)]
                hover:bg-[var(--premium-surface-2)]
                border border-transparent
                hover:border-[var(--premium-glass-border)]
                transition-all duration-200
                ${isLoading ? 'animate-pulse' : ''}
              `}
            >
              {isLoading ? (
                // Loading skeleton
                <>
                  <div className="flex items-center gap-[var(--premium-space-md)] flex-1">
                    <div className="w-[32px] h-[32px] rounded-full bg-[var(--premium-surface-3)]" />
                    <div className="space-y-[4px] flex-1">
                      <div className="h-[16px] w-[80px] bg-[var(--premium-surface-3)] rounded" />
                      <div className="h-[12px] w-[60px] bg-[var(--premium-surface-3)] rounded" />
                    </div>
                  </div>
                  <div className="text-right space-y-[4px]">
                    <div className="h-[18px] w-[100px] bg-[var(--premium-surface-3)] rounded ml-auto" />
                    <div className="h-[14px] w-[60px] bg-[var(--premium-surface-3)] rounded ml-auto" />
                  </div>
                </>
              ) : (
                // Actual rate data
                <>
                  {/* Left side: Flag + Currency Pair */}
                  <div className="flex items-center gap-[var(--premium-space-md)]">
                    {/* Currency Flag */}
                    <div
                      className="
                        w-[32px] h-[32px]
                        rounded-full
                        bg-[var(--premium-surface-2)]
                        border border-[var(--premium-glass-border)]
                        flex items-center justify-center
                        text-[18px]
                      "
                    >
                      {rate.flag}
                    </div>
                    
                    {/* Currency Info */}
                    <div>
                      <p className="body-md font-medium text-[var(--premium-text-primary)]">
                        {rate.currencyPair}
                      </p>
                      <p className="body-xs text-[var(--premium-text-tertiary)]">
                        {rate.baseCurrency} to {rate.quoteCurrency}
                      </p>
                    </div>
                  </div>

                  {/* Right side: Rate + Change */}
                  <div className="text-right">
                    {/* Current Rate */}
                    <p className="body-md font-semibold text-[var(--premium-text-primary)] mb-[2px]">
                      {formatRate(rate.rate)}
                    </p>
                    
                    {/* Daily Change */}
                    <div
                      className={`
                        inline-flex items-center gap-[4px]
                        px-[8px] py-[2px]
                        rounded-[var(--premium-radius-full)]
                        body-xs font-medium
                        ${
                          rate.changePercent >= 0
                            ? 'bg-[var(--premium-emerald)]/10 text-[var(--premium-emerald)]'
                            : 'bg-[var(--premium-error)]/10 text-[var(--premium-error)]'
                        }
                      `}
                    >
                      {rate.changePercent >= 0 ? (
                        <TrendingUp size={12} strokeWidth={2.5} />
                      ) : (
                        <TrendingDown size={12} strokeWidth={2.5} />
                      )}
                      <span>
                        {rate.changePercent >= 0 ? '+' : ''}
                        {rate.changePercent.toFixed(2)}%
                      </span>
                    </div>
                  </div>
                </>
              )}
            </div>
          ))}
        </div>

        {/* Footer Note */}
        <div
          className="
            mt-[var(--premium-space-lg)]
            pt-[var(--premium-space-md)]
            border-t border-[var(--premium-glass-border)]
          "
        >
          <p className="body-xs text-[var(--premium-text-tertiary)] text-center">
            Rates are indicative and may vary
          </p>
        </div>
      </div>
    </PremiumCard>
  );
}
