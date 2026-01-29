/**
 * Premium Budget Modal - Add/Edit Budget
 * Full-screen modal with Material 3 design patterns
 * Supports budget creation and editing with smart suggestions
 * 
 * @architecture Progressive disclosure with step-by-step flow
 * @design Glassmorphism with emerald green accents
 */

import { useState, useEffect } from 'react';
import {
  X,
  DollarSign,
  Calendar,
  Bell,
  TrendingUp,
  AlertCircle,
  ChevronRight,
  Sparkles,
  Check,
  Info,
} from 'lucide-react';
import { CategoryIcon } from '../atoms/CategoryIcon';
import { PremiumCard } from './PremiumCard';
import { PremiumButton } from './PremiumButton';
import { PremiumCalculatorKeypad } from './PremiumCalculatorKeypad';
import type { TransactionCategory } from '../../types/domain';
import { getCategoryMetadata, CATEGORY_METADATA } from '../../mocks/data';

// Budget Period Type
export type BudgetPeriod = 'weekly' | 'monthly' | 'yearly' | 'custom';

// Alert Threshold Type
export interface BudgetAlert {
  enabled: boolean;
  threshold: number; // Percentage (e.g., 80 for 80%)
}

// Budget Data Interface
export interface BudgetData {
  id?: string;
  category: TransactionCategory;
  amount: number;
  period: BudgetPeriod;
  alert: BudgetAlert;
  startDate?: Date;
  endDate?: Date;
}

// Smart Budget Suggestion Interface
interface BudgetSuggestion {
  amount: number;
  reason: string;
  basedOn: 'avg-spending' | 'category-benchmark' | 'income-ratio';
  confidence: 'high' | 'medium' | 'low';
}

// Props Interface
export interface PremiumBudgetModalProps {
  /** Whether the modal is open */
  isOpen: boolean;
  /** Callback when modal is closed */
  onClose: () => void;
  /** Callback when budget is saved */
  onSave: (budget: BudgetData) => void;
  /** Existing budget data (for edit mode) */
  existingBudget?: BudgetData;
  /** User's average monthly income for recommendations */
  monthlyIncome?: number;
  /** Historical spending data for smart suggestions */
  categorySpendingHistory?: Record<TransactionCategory, number[]>;
}

// Modal Steps
type ModalStep = 'category' | 'amount' | 'period' | 'alerts' | 'review';

// Period Configuration
const PERIOD_OPTIONS: Array<{
  value: BudgetPeriod;
  label: string;
  description: string;
  icon: typeof Calendar;
}> = [
  {
    value: 'weekly',
    label: 'Weekly',
    description: 'Reset every week',
    icon: Calendar,
  },
  {
    value: 'monthly',
    label: 'Monthly',
    description: 'Reset every month',
    icon: Calendar,
  },
  {
    value: 'yearly',
    label: 'Yearly',
    description: 'Reset every year',
    icon: Calendar,
  },
];

// Alert Threshold Presets
const ALERT_PRESETS = [
  { value: 50, label: '50%', color: '#10b981' },
  { value: 75, label: '75%', color: '#f59e0b' },
  { value: 90, label: '90%', color: '#ef4444' },
];

export function PremiumBudgetModal({
  isOpen,
  onClose,
  onSave,
  existingBudget,
  monthlyIncome = 5000,
  categorySpendingHistory = {},
}: PremiumBudgetModalProps) {
  const isEditMode = !!existingBudget;
  
  // State Management
  const [currentStep, setCurrentStep] = useState<ModalStep>('category');
  const [selectedCategory, setSelectedCategory] = useState<TransactionCategory | undefined>(
    existingBudget?.category
  );
  const [budgetAmount, setBudgetAmount] = useState(existingBudget?.amount.toString() || '');
  const [selectedPeriod, setSelectedPeriod] = useState<BudgetPeriod>(
    existingBudget?.period || 'monthly'
  );
  const [alertEnabled, setAlertEnabled] = useState(existingBudget?.alert.enabled ?? true);
  const [alertThreshold, setAlertThreshold] = useState(existingBudget?.alert.threshold ?? 80);
  const [showCalculator, setShowCalculator] = useState(false);
  const [showSuggestions, setShowSuggestions] = useState(false);

  // Reset state when modal opens/closes
  useEffect(() => {
    if (isOpen) {
      if (existingBudget) {
        setSelectedCategory(existingBudget.category);
        setBudgetAmount(existingBudget.amount.toString());
        setSelectedPeriod(existingBudget.period);
        setAlertEnabled(existingBudget.alert.enabled);
        setAlertThreshold(existingBudget.alert.threshold);
        setCurrentStep('amount'); // Skip category selection in edit mode
      } else {
        setCurrentStep('category');
        setSelectedCategory(undefined);
        setBudgetAmount('');
        setSelectedPeriod('monthly');
        setAlertEnabled(true);
        setAlertThreshold(80);
      }
      setShowCalculator(false);
      setShowSuggestions(false);
    }
  }, [isOpen, existingBudget]);

  // Calculate smart budget suggestion
  const getSmartSuggestion = (category: TransactionCategory): BudgetSuggestion | null => {
    const history = categorySpendingHistory[category];
    
    if (history && history.length > 0) {
      // Calculate average spending
      const avgSpending = history.reduce((sum, val) => sum + val, 0) / history.length;
      
      // Add 15% buffer for realistic budgeting
      const suggestedAmount = Math.round(avgSpending * 1.15);
      
      return {
        amount: suggestedAmount,
        reason: `Based on your average spending of $${avgSpending.toFixed(0)}`,
        basedOn: 'avg-spending',
        confidence: history.length >= 3 ? 'high' : 'medium',
      };
    }
    
    // Fallback to income-based recommendation
    const categoryMeta = getCategoryMetadata(category);
    const recommendedPercentage = getCategoryBudgetPercentage(category);
    const suggestedAmount = Math.round(monthlyIncome * recommendedPercentage);
    
    return {
      amount: suggestedAmount,
      reason: `Recommended ${(recommendedPercentage * 100).toFixed(0)}% of monthly income`,
      basedOn: 'income-ratio',
      confidence: 'medium',
    };
  };

  // Get recommended budget percentage based on category
  const getCategoryBudgetPercentage = (category: TransactionCategory): number => {
    const budgetMap: Partial<Record<TransactionCategory, number>> = {
      'food': 0.15,
      'transport': 0.10,
      'bills': 0.25,
      'shopping': 0.10,
      'entertainment': 0.08,
      'health': 0.08,
      'education': 0.10,
      'travel': 0.05,
      'gifts': 0.03,
      'other': 0.05,
    };
    
    return budgetMap[category] || 0.05;
  };

  // Handle category selection
  const handleCategorySelect = (category: TransactionCategory) => {
    setSelectedCategory(category);
    setCurrentStep('amount');
    
    // Auto-suggest budget amount
    const suggestion = getSmartSuggestion(category);
    if (suggestion && !existingBudget) {
      setShowSuggestions(true);
    }
  };

  // Handle calculator input
  const handleCalculatorChange = (value: string) => {
    setBudgetAmount(value);
  };

  // Handle next step
  const handleNext = () => {
    if (currentStep === 'category' && selectedCategory) {
      setCurrentStep('amount');
    } else if (currentStep === 'amount' && budgetAmount) {
      setCurrentStep('period');
    } else if (currentStep === 'period') {
      setCurrentStep('alerts');
    } else if (currentStep === 'alerts') {
      setCurrentStep('review');
    }
  };

  // Handle back
  const handleBack = () => {
    if (currentStep === 'amount') {
      if (isEditMode) {
        onClose();
      } else {
        setCurrentStep('category');
      }
    } else if (currentStep === 'period') {
      setCurrentStep('amount');
    } else if (currentStep === 'alerts') {
      setCurrentStep('period');
    } else if (currentStep === 'review') {
      setCurrentStep('alerts');
    }
  };

  // Handle save
  const handleSave = () => {
    if (!selectedCategory || !budgetAmount) return;
    
    const budgetData: BudgetData = {
      id: existingBudget?.id,
      category: selectedCategory,
      amount: parseFloat(budgetAmount),
      period: selectedPeriod,
      alert: {
        enabled: alertEnabled,
        threshold: alertThreshold,
      },
    };
    
    onSave(budgetData);
    onClose();
  };

  // Apply smart suggestion
  const applySuggestion = () => {
    if (!selectedCategory) return;
    const suggestion = getSmartSuggestion(selectedCategory);
    if (suggestion) {
      setBudgetAmount(suggestion.amount.toString());
      setShowSuggestions(false);
    }
  };

  if (!isOpen) return null;

  const categoryMeta = selectedCategory ? getCategoryMetadata(selectedCategory) : null;
  const smartSuggestion = selectedCategory ? getSmartSuggestion(selectedCategory) : null;

  return (
    <div className="fixed inset-0 z-50 flex items-end md:items-center justify-center">
      {/* Backdrop */}
      <div
        className="absolute inset-0 bg-black/60 backdrop-blur-sm"
        onClick={onClose}
      />

      {/* Modal Content */}
      <div
        className="
          relative
          w-full
          md:max-w-[480px]
          max-h-[90vh]
          md:max-h-[85vh]
          bg-[var(--premium-surface)]
          md:rounded-[var(--premium-radius-3xl)]
          rounded-t-[var(--premium-radius-3xl)]
          shadow-[var(--premium-shadow-2xl)]
          overflow-hidden
          flex
          flex-col
          animate-slide-up
        "
      >
        {/* Header */}
        <div className="
          relative
          bg-gradient-to-r from-[#10b981] via-[#059669] to-[#047857]
          px-[var(--premium-space-lg)]
          py-[var(--premium-space-xl)]
        ">
          {/* Background orbs */}
          <div className="absolute top-0 right-0 w-[120px] h-[120px] bg-white/10 rounded-full blur-[50px]" />
          
          <div className="relative z-10 flex items-center justify-between">
            <div className="flex-1">
              <h2 className="heading-lg text-white mb-[4px]">
                {isEditMode ? 'Edit Budget' : 'Set Budget'}
              </h2>
              <p className="body-sm text-white/70">
                {currentStep === 'category' && 'Choose a category'}
                {currentStep === 'amount' && 'Set your budget amount'}
                {currentStep === 'period' && 'Select budget period'}
                {currentStep === 'alerts' && 'Configure alerts'}
                {currentStep === 'review' && 'Review and confirm'}
              </p>
            </div>
            
            <button
              onClick={onClose}
              className="
                w-[40px] h-[40px]
                rounded-full
                bg-white/10
                backdrop-blur-md
                flex items-center justify-center
                hover:bg-white/20
                transition-all
              "
            >
              <X size={20} className="text-white" />
            </button>
          </div>

          {/* Progress Steps */}
          <div className="relative z-10 mt-[var(--premium-space-lg)] flex gap-[8px]">
            {['category', 'amount', 'period', 'alerts', 'review'].map((step, index) => {
              const stepIndex = ['category', 'amount', 'period', 'alerts', 'review'].indexOf(currentStep);
              const isActive = index <= stepIndex;
              
              return (
                <div
                  key={step}
                  className="flex-1 h-[4px] rounded-full bg-white/20 overflow-hidden"
                >
                  <div
                    className={`
                      h-full bg-white rounded-full transition-all duration-500
                      ${isActive ? 'w-full' : 'w-0'}
                    `}
                  />
                </div>
              );
            })}
          </div>
        </div>

        {/* Content */}
        <div className="flex-1 overflow-y-auto px-[var(--premium-space-lg)] py-[var(--premium-space-xl)]">
          
          {/* Step 1: Category Selection */}
          {currentStep === 'category' && (
            <div className="space-y-[var(--premium-space-md)]">
              <div className="flex items-center gap-[8px] mb-[var(--premium-space-lg)]">
                <Sparkles size={20} className="text-[var(--premium-accent)]" />
                <p className="body-sm text-[var(--premium-text-secondary)]">
                  Select the category you want to budget for
                </p>
              </div>

              <div className="grid grid-cols-2 gap-[var(--premium-space-md)]">
                {Object.keys(CATEGORY_METADATA).filter(cat => !cat.startsWith('income')).map((categoryKey) => {
                  const category = categoryKey as TransactionCategory;
                  const meta = getCategoryMetadata(category);
                  const isSelected = selectedCategory === category;
                  
                  return (
                    <button
                      key={category}
                      onClick={() => handleCategorySelect(category)}
                      className={`
                        relative
                        p-[var(--premium-space-lg)]
                        rounded-[var(--premium-radius-xl)]
                        border-2
                        transition-all
                        ${isSelected
                          ? 'border-[var(--premium-accent)] bg-[var(--premium-accent)]/10'
                          : 'border-[var(--premium-border)] bg-[var(--premium-surface-variant)]'
                        }
                        hover:border-[var(--premium-accent)]/50
                        hover:shadow-[var(--premium-shadow-md)]
                      `}
                    >
                      {isSelected && (
                        <div className="absolute top-[8px] right-[8px] w-[20px] h-[20px] bg-[var(--premium-accent)] rounded-full flex items-center justify-center">
                          <Check size={12} className="text-white" />
                        </div>
                      )}
                      
                      <div
                        className="w-[48px] h-[48px] rounded-[var(--premium-radius-md)] flex items-center justify-center mb-[var(--premium-space-sm)] mx-auto"
                        style={{
                          background: `linear-gradient(135deg, ${meta?.colorToken}40, ${meta?.colorToken}20)`,
                        }}
                      >
                        <CategoryIcon category={category} size={24} />
                      </div>
                      
                      <p className="body-md font-medium text-[var(--premium-text-primary)] text-center">
                        {meta?.label}
                      </p>
                    </button>
                  );
                })}
              </div>
            </div>
          )}

          {/* Step 2: Amount Input */}
          {currentStep === 'amount' && selectedCategory && (
            <div className="space-y-[var(--premium-space-lg)]">
              {/* Category Display */}
              <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
                <div className="flex items-center gap-[var(--premium-space-md)]">
                  <div
                    className="w-[56px] h-[56px] rounded-[var(--premium-radius-lg)] flex items-center justify-center flex-shrink-0"
                    style={{
                      background: `linear-gradient(135deg, ${categoryMeta?.colorToken}40, ${categoryMeta?.colorToken}20)`,
                    }}
                  >
                    <CategoryIcon category={selectedCategory} size={28} />
                  </div>
                  
                  <div className="flex-1">
                    <p className="body-sm text-[var(--premium-text-tertiary)] mb-[2px]">
                      Category
                    </p>
                    <p className="heading-md text-[var(--premium-text-primary)]">
                      {categoryMeta?.label}
                    </p>
                  </div>
                </div>
              </PremiumCard>

              {/* Smart Suggestion */}
              {smartSuggestion && showSuggestions && (
                <div
                  className="
                    p-[var(--premium-space-lg)]
                    rounded-[var(--premium-radius-xl)]
                    bg-gradient-to-r from-[var(--premium-accent)]/10 to-[var(--premium-accent)]/5
                    border border-[var(--premium-accent)]/20
                  "
                >
                  <div className="flex items-start gap-[var(--premium-space-md)]">
                    <div className="w-[40px] h-[40px] rounded-full bg-[var(--premium-accent)]/20 flex items-center justify-center flex-shrink-0">
                      <Sparkles size={20} className="text-[var(--premium-accent)]" />
                    </div>
                    
                    <div className="flex-1">
                      <p className="body-md font-medium text-[var(--premium-text-primary)] mb-[4px]">
                        Smart Suggestion
                      </p>
                      <p className="body-sm text-[var(--premium-text-secondary)] mb-[var(--premium-space-md)]">
                        {smartSuggestion.reason}
                      </p>
                      
                      <div className="flex items-center gap-[var(--premium-space-md)]">
                        <div className="flex-1">
                          <p className="display-sm text-[var(--premium-accent)]">
                            ${smartSuggestion.amount.toLocaleString()}
                          </p>
                          <p className="body-xs text-[var(--premium-text-tertiary)]">
                            {smartSuggestion.confidence === 'high' && '🎯 High confidence'}
                            {smartSuggestion.confidence === 'medium' && '💡 Medium confidence'}
                            {smartSuggestion.confidence === 'low' && 'ℹ️ Low confidence'}
                          </p>
                        </div>
                        
                        <PremiumButton
                          variant="primary"
                          size="sm"
                          onClick={applySuggestion}
                        >
                          Apply
                        </PremiumButton>
                      </div>
                    </div>
                  </div>
                </div>
              )}

              {/* Amount Display */}
              <div className="text-center py-[var(--premium-space-xl)]">
                <p className="body-sm text-[var(--premium-text-tertiary)] mb-[var(--premium-space-sm)]">
                  {selectedPeriod.charAt(0).toUpperCase() + selectedPeriod.slice(1)} Budget
                </p>
                <div className="flex items-center justify-center gap-[8px]">
                  <DollarSign size={40} className="text-[var(--premium-text-tertiary)]" />
                  <p className="display-lg text-[var(--premium-text-primary)]">
                    {budgetAmount || '0'}
                  </p>
                </div>
              </div>

              {/* Calculator */}
              {!showCalculator ? (
                <PremiumButton
                  variant="secondary"
                  fullWidth
                  onClick={() => setShowCalculator(true)}
                  leftIcon={<Calculator size={20} />}
                >
                  Enter Amount
                </PremiumButton>
              ) : (
                <div className="space-y-[var(--premium-space-md)]">
                  <PremiumCalculatorKeypad
                    value={budgetAmount}
                    onChange={handleCalculatorChange}
                    placeholder="0"
                  />
                  
                  <div className="flex gap-[var(--premium-space-sm)]">
                    {[100, 500, 1000].map((preset) => (
                      <button
                        key={preset}
                        onClick={() => setBudgetAmount(preset.toString())}
                        className="
                          flex-1
                          py-[var(--premium-space-sm)]
                          rounded-[var(--premium-radius-lg)]
                          bg-[var(--premium-surface-variant)]
                          border border-[var(--premium-border)]
                          body-sm text-[var(--premium-text-secondary)]
                          hover:border-[var(--premium-accent)]
                          hover:text-[var(--premium-accent)]
                          transition-all
                        "
                      >
                        ${preset}
                      </button>
                    ))}
                  </div>
                </div>
              )}
            </div>
          )}

          {/* Step 3: Period Selection */}
          {currentStep === 'period' && (
            <div className="space-y-[var(--premium-space-md)]">
              <div className="flex items-center gap-[8px] mb-[var(--premium-space-lg)]">
                <Calendar size={20} className="text-[var(--premium-accent)]" />
                <p className="body-sm text-[var(--premium-text-secondary)]">
                  How often should this budget reset?
                </p>
              </div>

              {PERIOD_OPTIONS.map((option) => {
                const isSelected = selectedPeriod === option.value;
                
                return (
                  <button
                    key={option.value}
                    onClick={() => setSelectedPeriod(option.value)}
                    className={`
                      w-full
                      p-[var(--premium-space-lg)]
                      rounded-[var(--premium-radius-xl)]
                      border-2
                      transition-all
                      ${isSelected
                        ? 'border-[var(--premium-accent)] bg-[var(--premium-accent)]/10'
                        : 'border-[var(--premium-border)] bg-[var(--premium-surface-variant)]'
                      }
                      hover:border-[var(--premium-accent)]/50
                    `}
                  >
                    <div className="flex items-center gap-[var(--premium-space-md)]">
                      <div className={`
                        w-[48px] h-[48px]
                        rounded-[var(--premium-radius-md)]
                        flex items-center justify-center
                        ${isSelected
                          ? 'bg-[var(--premium-accent)]/20'
                          : 'bg-[var(--premium-background)]'
                        }
                      `}>
                        <Calendar
                          size={24}
                          className={isSelected ? 'text-[var(--premium-accent)]' : 'text-[var(--premium-text-tertiary)]'}
                        />
                      </div>
                      
                      <div className="flex-1 text-left">
                        <p className="body-md font-medium text-[var(--premium-text-primary)] mb-[2px]">
                          {option.label}
                        </p>
                        <p className="body-sm text-[var(--premium-text-tertiary)]">
                          {option.description}
                        </p>
                      </div>
                      
                      {isSelected && (
                        <div className="w-[24px] h-[24px] bg-[var(--premium-accent)] rounded-full flex items-center justify-center">
                          <Check size={14} className="text-white" />
                        </div>
                      )}
                    </div>
                  </button>
                );
              })}

              {/* Preview */}
              {budgetAmount && (
                <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="body-sm text-[var(--premium-text-tertiary)] mb-[4px]">
                        {selectedPeriod.charAt(0).toUpperCase() + selectedPeriod.slice(1)} Budget
                      </p>
                      <p className="heading-lg text-[var(--premium-text-primary)]">
                        ${parseFloat(budgetAmount).toLocaleString()}
                      </p>
                    </div>
                    
                    {selectedPeriod === 'weekly' && (
                      <div className="text-right">
                        <p className="body-xs text-[var(--premium-text-tertiary)] mb-[2px]">
                          Monthly equivalent
                        </p>
                        <p className="body-md text-[var(--premium-text-secondary)]">
                          ~${(parseFloat(budgetAmount) * 4.33).toFixed(0)}
                        </p>
                      </div>
                    )}
                    
                    {selectedPeriod === 'yearly' && (
                      <div className="text-right">
                        <p className="body-xs text-[var(--premium-text-tertiary)] mb-[2px]">
                          Monthly equivalent
                        </p>
                        <p className="body-md text-[var(--premium-text-secondary)]">
                          ~${(parseFloat(budgetAmount) / 12).toFixed(0)}
                        </p>
                      </div>
                    )}
                  </div>
                </PremiumCard>
              )}
            </div>
          )}

          {/* Step 4: Alerts Configuration */}
          {currentStep === 'alerts' && (
            <div className="space-y-[var(--premium-space-lg)]">
              <div className="flex items-center gap-[8px] mb-[var(--premium-space-lg)]">
                <Bell size={20} className="text-[var(--premium-accent)]" />
                <p className="body-sm text-[var(--premium-text-secondary)]">
                  Get notified when you're approaching your budget limit
                </p>
              </div>

              {/* Enable/Disable Toggle */}
              <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-[var(--premium-space-md)]">
                    <div className="w-[48px] h-[48px] rounded-[var(--premium-radius-md)] bg-[var(--premium-accent)]/10 flex items-center justify-center">
                      <Bell size={24} className="text-[var(--premium-accent)]" />
                    </div>
                    
                    <div>
                      <p className="body-md font-medium text-[var(--premium-text-primary)] mb-[2px]">
                        Budget Alerts
                      </p>
                      <p className="body-sm text-[var(--premium-text-tertiary)]">
                        Receive notifications
                      </p>
                    </div>
                  </div>
                  
                  <button
                    onClick={() => setAlertEnabled(!alertEnabled)}
                    className={`
                      relative w-[52px] h-[32px] rounded-full transition-all
                      ${alertEnabled ? 'bg-[var(--premium-accent)]' : 'bg-[var(--premium-border)]'}
                    `}
                  >
                    <div
                      className={`
                        absolute top-[4px] w-[24px] h-[24px] bg-white rounded-full
                        shadow-md transition-all
                        ${alertEnabled ? 'left-[24px]' : 'left-[4px]'}
                      `}
                    />
                  </button>
                </div>
              </PremiumCard>

              {/* Threshold Selection */}
              {alertEnabled && (
                <>
                  <div>
                    <p className="body-md text-[var(--premium-text-primary)] mb-[var(--premium-space-sm)]">
                      Alert me at
                    </p>
                    <p className="body-sm text-[var(--premium-text-tertiary)] mb-[var(--premium-space-md)]">
                      You'll be notified when spending reaches this percentage
                    </p>
                    
                    <div className="grid grid-cols-3 gap-[var(--premium-space-sm)]">
                      {ALERT_PRESETS.map((preset) => {
                        const isSelected = alertThreshold === preset.value;
                        
                        return (
                          <button
                            key={preset.value}
                            onClick={() => setAlertThreshold(preset.value)}
                            className={`
                              relative
                              py-[var(--premium-space-lg)]
                              rounded-[var(--premium-radius-xl)]
                              border-2
                              transition-all
                              ${isSelected
                                ? 'border-[var(--premium-accent)] bg-[var(--premium-accent)]/10'
                                : 'border-[var(--premium-border)] bg-[var(--premium-surface-variant)]'
                              }
                            `}
                            style={isSelected ? {
                              borderColor: preset.color,
                              backgroundColor: `${preset.color}20`,
                            } : undefined}
                          >
                            {isSelected && (
                              <div
                                className="absolute top-[8px] right-[8px] w-[20px] h-[20px] rounded-full flex items-center justify-center"
                                style={{ backgroundColor: preset.color }}
                              >
                                <Check size={12} className="text-white" />
                              </div>
                            )}
                            
                            <p
                              className="heading-md text-center"
                              style={isSelected ? { color: preset.color } : undefined}
                            >
                              {preset.label}
                            </p>
                          </button>
                        );
                      })}
                    </div>
                  </div>

                  {/* Preview */}
                  {budgetAmount && (
                    <PremiumCard
                      variant="glass"
                      className="p-[var(--premium-space-lg)] bg-gradient-to-r from-amber-500/10 to-orange-500/10 border border-amber-500/20"
                    >
                      <div className="flex items-start gap-[var(--premium-space-md)]">
                        <AlertCircle size={20} className="text-amber-500 mt-[2px] flex-shrink-0" />
                        <div className="flex-1">
                          <p className="body-sm text-[var(--premium-text-primary)] mb-[4px]">
                            You'll be alerted when you spend
                          </p>
                          <p className="heading-md text-amber-500">
                            ${(parseFloat(budgetAmount) * (alertThreshold / 100)).toFixed(0)}
                          </p>
                          <p className="body-xs text-[var(--premium-text-tertiary)] mt-[4px]">
                            {alertThreshold}% of ${parseFloat(budgetAmount).toLocaleString()} budget
                          </p>
                        </div>
                      </div>
                    </PremiumCard>
                  )}
                </>
              )}
            </div>
          )}

          {/* Step 5: Review */}
          {currentStep === 'review' && selectedCategory && budgetAmount && (
            <div className="space-y-[var(--premium-space-lg)]">
              <div className="flex items-center gap-[8px] mb-[var(--premium-space-lg)]">
                <Check size={20} className="text-[var(--premium-accent)]" />
                <p className="body-sm text-[var(--premium-text-secondary)]">
                  Review your budget settings
                </p>
              </div>

              {/* Summary Card */}
              <PremiumCard variant="glass" className="p-[var(--premium-space-xl)]">
                {/* Category */}
                <div className="flex items-center gap-[var(--premium-space-lg)] pb-[var(--premium-space-lg)] border-b border-[var(--premium-border)]">
                  <div
                    className="w-[64px] h-[64px] rounded-[var(--premium-radius-lg)] flex items-center justify-center flex-shrink-0"
                    style={{
                      background: `linear-gradient(135deg, ${categoryMeta?.colorToken}40, ${categoryMeta?.colorToken}20)`,
                    }}
                  >
                    <CategoryIcon category={selectedCategory} size={32} />
                  </div>
                  
                  <div className="flex-1">
                    <p className="body-sm text-[var(--premium-text-tertiary)] mb-[4px]">
                      Category
                    </p>
                    <p className="heading-lg text-[var(--premium-text-primary)]">
                      {categoryMeta?.label}
                    </p>
                  </div>
                </div>

                {/* Budget Amount */}
                <div className="py-[var(--premium-space-lg)] border-b border-[var(--premium-border)]">
                  <p className="body-sm text-[var(--premium-text-tertiary)] mb-[8px]">
                    Budget Amount
                  </p>
                  <p className="display-md text-[var(--premium-text-primary)]">
                    ${parseFloat(budgetAmount).toLocaleString()}
                  </p>
                  <p className="body-sm text-[var(--premium-text-secondary)] mt-[4px]">
                    {selectedPeriod.charAt(0).toUpperCase() + selectedPeriod.slice(1)} budget
                  </p>
                </div>

                {/* Period */}
                <div className="py-[var(--premium-space-lg)] border-b border-[var(--premium-border)]">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="body-sm text-[var(--premium-text-tertiary)] mb-[4px]">
                        Period
                      </p>
                      <p className="body-md font-medium text-[var(--premium-text-primary)]">
                        {selectedPeriod.charAt(0).toUpperCase() + selectedPeriod.slice(1)}
                      </p>
                    </div>
                    <Calendar size={20} className="text-[var(--premium-text-tertiary)]" />
                  </div>
                </div>

                {/* Alerts */}
                <div className="pt-[var(--premium-space-lg)]">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="body-sm text-[var(--premium-text-tertiary)] mb-[4px]">
                        Alerts
                      </p>
                      <p className="body-md font-medium text-[var(--premium-text-primary)]">
                        {alertEnabled ? `Enabled at ${alertThreshold}%` : 'Disabled'}
                      </p>
                    </div>
                    <Bell size={20} className={alertEnabled ? 'text-[var(--premium-accent)]' : 'text-[var(--premium-text-tertiary)]'} />
                  </div>
                </div>
              </PremiumCard>

              {/* Info Note */}
              <div className="flex items-start gap-[var(--premium-space-sm)] p-[var(--premium-space-md)] rounded-[var(--premium-radius-lg)] bg-[var(--premium-surface-variant)]">
                <Info size={16} className="text-[var(--premium-text-tertiary)] mt-[2px] flex-shrink-0" />
                <p className="body-sm text-[var(--premium-text-secondary)]">
                  You can edit this budget anytime from the Budget Management screen.
                </p>
              </div>
            </div>
          )}
        </div>

        {/* Footer Actions */}
        <div className="
          px-[var(--premium-space-lg)]
          py-[var(--premium-space-lg)]
          bg-[var(--premium-surface-variant)]
          border-t border-[var(--premium-border)]
          flex gap-[var(--premium-space-md)]
        ">
          {currentStep !== 'category' && (
            <PremiumButton
              variant="secondary"
              onClick={handleBack}
              className="flex-1"
            >
              Back
            </PremiumButton>
          )}
          
          {currentStep === 'review' ? (
            <PremiumButton
              variant="primary"
              onClick={handleSave}
              className="flex-1"
              disabled={!selectedCategory || !budgetAmount}
            >
              {isEditMode ? 'Update Budget' : 'Create Budget'}
            </PremiumButton>
          ) : (
            <PremiumButton
              variant="primary"
              onClick={handleNext}
              className="flex-1"
              disabled={
                (currentStep === 'category' && !selectedCategory) ||
                (currentStep === 'amount' && !budgetAmount)
              }
              rightIcon={<ChevronRight size={20} />}
            >
              Continue
            </PremiumButton>
          )}
        </div>
      </div>
    </div>
  );
}