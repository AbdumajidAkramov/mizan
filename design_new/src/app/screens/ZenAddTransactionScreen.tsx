/**
 * Zen Add Transaction Screen - Full-Screen Experience
 * Material 3 Top App Bar with elegant, zen-inspired layout
 * Professional full-screen architecture
 */

import { useState, useEffect } from 'react';
import { ArrowLeft, Check, ArrowUpRight, ArrowDownLeft, ArrowLeftRight, Camera } from 'lucide-react';
import { ZenCalculatorKeypad } from '../components/premium/ZenCalculatorKeypad';
import { FloatingAIVoice } from '../components/premium/FloatingAIVoice';
import { BottomSheet } from '../components/premium/BottomSheet';
import { PremiumCategoryPicker } from '../components/premium/PremiumCategoryPicker';
import { PremiumAccountSelector } from '../components/premium/PremiumAccountSelector';
import { PremiumScanInput } from '../components/premium/PremiumScanInput';
import type { TransactionCategory, TransactionType } from '../../types/domain';
import { Calendar, FileText } from 'lucide-react';

export interface ZenAddTransactionScreenProps {
  onClose: () => void;
  onSave: (transaction: {
    amount: number;
    type: TransactionType;
    category?: TransactionCategory;
    fromAccountId?: string;
    toAccountId?: string;
    date: Date;
    notes?: string;
  }) => void;
}

type VoiceParseResult = {
  amount?: number;
  type?: TransactionType;
  category?: string;
  fromAccount?: string;
  toAccount?: string;
  description?: string;
};

export function ZenAddTransactionScreen({
  onClose,
  onSave,
}: ZenAddTransactionScreenProps) {
  // Core State
  const [transactionType, setTransactionType] = useState<TransactionType>('expense');
  const [displayValue, setDisplayValue] = useState('0');
  const [calculationString, setCalculationString] = useState('');
  const [currentOperator, setCurrentOperator] = useState<string | null>(null);
  const [previousValue, setPreviousValue] = useState<number | null>(null);

  // Voice & Scan State
  const [isListening, setIsListening] = useState(false);
  const [isScanning, setIsScanning] = useState(false);
  const [ghostData, setGhostData] = useState<VoiceParseResult | null>(null);

  // Transaction Details
  const [selectedCategory, setSelectedCategory] = useState<TransactionCategory>();
  const [fromAccountId, setFromAccountId] = useState<string>();
  const [toAccountId, setToAccountId] = useState<string>();
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [notes, setNotes] = useState('');

  // UI State
  const [showCategoryDrawer, setShowCategoryDrawer] = useState(false);
  const [showAccountDrawer, setShowAccountDrawer] = useState(false);
  const [showConfirmDrawer, setShowConfirmDrawer] = useState(false);

  // Calculator Logic
  const handleNumberClick = (num: string) => {
    if (displayValue === '0') {
      setDisplayValue(num);
    } else if (displayValue.length < 12) {
      setDisplayValue(displayValue + num);
    }
  };

  const handleOperatorClick = (operator: string) => {
    if (currentOperator && previousValue !== null) {
      handleEquals();
    }
    setPreviousValue(parseFloat(displayValue));
    setCurrentOperator(operator);
    setDisplayValue('0');
    setCalculationString(`${displayValue} ${operator}`);
  };

  const handleEquals = () => {
    if (currentOperator && previousValue !== null) {
      const current = parseFloat(displayValue);
      let result = 0;

      switch (currentOperator) {
        case '+':
          result = previousValue + current;
          break;
        case '-':
          result = previousValue - current;
          break;
        case '*':
          result = previousValue * current;
          break;
        case '/':
          result = current !== 0 ? previousValue / current : 0;
          break;
      }

      setDisplayValue(result.toFixed(2).replace(/\.?0+$/, ''));
      setCalculationString('');
      setCurrentOperator(null);
      setPreviousValue(null);
    }
  };

  const handleDelete = () => {
    if (displayValue.length === 1) {
      setDisplayValue('0');
    } else {
      setDisplayValue(displayValue.slice(0, -1));
    }
  };

  const handleDecimal = () => {
    if (!displayValue.includes('.') && displayValue.length < 10) {
      setDisplayValue(displayValue + '.');
    }
  };

  const handleClear = () => {
    setDisplayValue('0');
    setCalculationString('');
    setCurrentOperator(null);
    setPreviousValue(null);
  };

  // Type Selection Handler
  const handleTypeSelect = (type: TransactionType) => {
    setTransactionType(type);
    
    // Morph UI based on type
    if (parseFloat(displayValue) > 0) {
      if (type === 'transfer') {
        setTimeout(() => setShowAccountDrawer(true), 300);
      } else {
        setTimeout(() => setShowCategoryDrawer(true), 300);
      }
    }
  };

  // Voice AI Handler
  const handleVoiceToggle = () => {
    if (isListening) {
      setIsListening(false);
      setGhostData(null);
    } else {
      setIsListening(true);
      simulateVoiceRecognition();
    }
  };

  const simulateVoiceRecognition = () => {
    setTimeout(() => {
      const examples = [
        {
          text: "Transfer $2000 from Bank to Cash",
          result: {
            amount: 2000,
            type: 'transfer' as const,
            fromAccount: 'bank-checking',
            toAccount: 'cash',
          }
        },
        {
          text: "Dinner $45",
          result: {
            amount: 45,
            type: 'expense' as const,
            category: 'food',
            description: 'Dinner',
          }
        },
      ];

      const example = examples[Math.floor(Math.random() * examples.length)];
      
      // Ghost UI effect - show data populating
      setGhostData(example.result);
      
      setTimeout(() => {
        if (example.result.amount) {
          setDisplayValue(example.result.amount.toString());
        }
        if (example.result.type) {
          setTransactionType(example.result.type);
        }
        if (example.result.fromAccount) {
          setFromAccountId(example.result.fromAccount);
        }
        if (example.result.toAccount) {
          setToAccountId(example.result.toAccount);
        }
        if (example.result.category) {
          setSelectedCategory(example.result.category as TransactionCategory);
        }
        
        setIsListening(false);
        setGhostData(null);
        
        // Open appropriate drawer
        setTimeout(() => {
          if (example.result.type === 'transfer') {
            setShowAccountDrawer(true);
          } else {
            setShowCategoryDrawer(true);
          }
        }, 500);
      }, 2000);
    }, 1500);
  };

  // Scan Handler
  const handleScanToggle = () => {
    setIsScanning(!isScanning);
  };

  const handleUploadImage = (file: File) => {
    console.log('Processing:', file.name);
    setTimeout(() => {
      setDisplayValue('32.50');
      setIsScanning(false);
    }, 1500);
  };

  // Category Selection
  const handleCategorySelect = (category: TransactionCategory) => {
    setSelectedCategory(category);
    setShowCategoryDrawer(false);
    setTimeout(() => setShowConfirmDrawer(true), 300);
  };

  // Transfer Accounts Set
  const handleAccountsConfirm = () => {
    if (fromAccountId && toAccountId) {
      setShowAccountDrawer(false);
      setTimeout(() => setShowConfirmDrawer(true), 300);
    }
  };

  // Save Transaction
  const handleSave = () => {
    const amount = parseFloat(displayValue);
    if (amount <= 0) return;

    if (transactionType === 'transfer') {
      if (!fromAccountId || !toAccountId) return;
      onSave({
        amount,
        type: transactionType,
        fromAccountId,
        toAccountId,
        date: selectedDate,
        notes: notes || undefined,
      });
    } else {
      if (!selectedCategory) return;
      onSave({
        amount,
        type: transactionType,
        category: selectedCategory,
        date: selectedDate,
        notes: notes || undefined,
      });
    }
    onClose();
  };

  const getTypeConfig = (type: TransactionType) => {
    switch (type) {
      case 'expense':
        return {
          color: '#f5576c',
          label: 'Expense',
          icon: ArrowUpRight,
        };
      case 'income':
        return {
          color: '#4facfe',
          label: 'Income',
          icon: ArrowDownLeft,
        };
      case 'transfer':
        return {
          color: 'var(--premium-emerald)',
          label: 'Transfer',
          icon: ArrowLeftRight,
        };
    }
  };

  // Check if ready to save
  const canSave = () => {
    const hasAmount = parseFloat(displayValue) > 0;
    if (transactionType === 'transfer') {
      return hasAmount && fromAccountId && toAccountId;
    }
    return hasAmount && selectedCategory;
  };

  return (
    <div className="
      fixed inset-0 z-[100]
      bg-[var(--premium-bg-primary)]
      flex flex-col
    ">
      {/* Material 3 Top App Bar */}
      <div className="
        sticky top-0 z-50
        bg-[var(--premium-bg-primary)]
        border-b border-[var(--premium-glass-border)]
      ">
        <div className="
          max-w-lg mx-auto
          h-[64px]
          px-[var(--premium-space-md)]
          flex items-center justify-between
        ">
          {/* Left: Back Button */}
          <button
            onClick={onClose}
            className="
              w-[40px] h-[40px]
              rounded-full
              flex items-center justify-center
              text-[var(--premium-text-primary)]
              hover:bg-[var(--premium-surface-2)]
              active:scale-95
              transition-all duration-200
            "
            aria-label="Back"
          >
            <ArrowLeft size={24} strokeWidth={2} />
          </button>

          {/* Center: Title */}
          <h1 className="heading-5 text-[var(--premium-text-primary)]">
            Add Transaction
          </h1>

          {/* Right: Save Button (only show when ready) */}
          {canSave() ? (
            <button
              onClick={handleSave}
              className="
                h-[40px]
                px-[var(--premium-space-md)]
                rounded-[var(--premium-radius-full)]
                bg-[var(--premium-emerald)]
                text-white
                font-medium
                flex items-center gap-[8px]
                hover:bg-[var(--premium-emerald-dark)]
                active:scale-95
                transition-all duration-200
              "
            >
              <Check size={18} strokeWidth={2.5} />
              Save
            </button>
          ) : (
            <div className="w-[40px]" /> // Spacer for alignment
          )}
        </div>
      </div>

      {/* Zen Background - Subtle Gradient Orbs */}
      <div className="fixed inset-0 pointer-events-none overflow-hidden -z-10">
        <div 
          className="absolute top-[-10%] right-[-10%] w-[60%] h-[60%] rounded-full opacity-[0.03] blur-[150px]"
          style={{ background: 'radial-gradient(circle, var(--premium-emerald) 0%, transparent 70%)' }}
        />
        <div 
          className="absolute bottom-[-10%] left-[-10%] w-[50%] h-[50%] rounded-full opacity-[0.03] blur-[120px]"
          style={{ background: 'radial-gradient(circle, #667eea 0%, transparent 70%)' }}
        />
      </div>

      {/* Main Content - Zen Layout */}
      <div className="
        flex-1 
        overflow-y-auto
        flex flex-col
      ">
        <div className="
          max-w-lg mx-auto w-full
          flex-1
          flex flex-col
          px-[var(--premium-space-lg)]
          py-[var(--premium-space-xl)]
        ">
          {/* Top Section - Amount & Type Selector */}
          <div className="flex-1 flex flex-col items-center justify-center pb-[var(--premium-space-2xl)]">
            
            {/* Calculation String */}
            {calculationString && (
              <div className="
                mb-[var(--premium-space-md)]
                body-sm font-light text-[var(--premium-text-tertiary)]
                animate-[fadeIn_0.3s_ease-out]
              ">
                {calculationString}
              </div>
            )}

            {/* Beautiful Animated Amount Display */}
            <div className="
              relative
              mb-[var(--premium-space-2xl)]
            ">
              {/* Glow Effect when amount > 0 */}
              {parseFloat(displayValue) > 0 && (
                <div className="
                  absolute inset-0
                  blur-[60px]
                  bg-[var(--premium-emerald)]
                  opacity-10
                  animate-pulse
                " style={{ animationDuration: '3s' }} />
              )}

              {/* Amount Text with Ghost Effect */}
              <div className="relative">
                <div className={`
                  text-[72px] font-extralight
                  text-[var(--premium-text-primary)]
                  leading-none
                  tracking-tight
                  transition-all duration-500
                  ${ghostData ? 'opacity-30 blur-sm' : 'opacity-100 blur-0'}
                `}>
                  ${displayValue}
                </div>

                {/* Ghost UI - Populating Data Animation */}
                {ghostData && (
                  <div className="
                    absolute inset-0
                    text-[72px] font-extralight
                    text-[var(--premium-emerald)]
                    leading-none
                    tracking-tight
                    animate-pulse
                  ">
                    ${ghostData.amount || displayValue}
                  </div>
                )}
              </div>
            </div>

            {/* Minimalist Triple Selector - Morphing UI */}
            <div className="
              relative
              flex items-center gap-[var(--premium-space-sm)]
              p-[6px]
              backdrop-blur-[30px]
              bg-[var(--premium-glass-bg)]
              border border-[var(--premium-glass-border)]
              rounded-[var(--premium-radius-full)]
              transition-all duration-500
            ">
              {(['expense', 'income', 'transfer'] as TransactionType[]).map((type) => {
                const config = getTypeConfig(type);
                const Icon = config.icon;
                const isActive = transactionType === type;

                return (
                  <button
                    key={type}
                    onClick={() => handleTypeSelect(type)}
                    className={`
                      relative
                      px-[var(--premium-space-lg)]
                      py-[var(--premium-space-sm)]
                      rounded-[var(--premium-radius-full)]
                      flex items-center gap-[8px]
                      transition-all duration-300
                      ${isActive
                        ? 'text-white'
                        : 'text-[var(--premium-text-tertiary)] hover:text-[var(--premium-text-primary)]'
                      }
                    `}
                  >
                    {/* Active Background - Morphing */}
                    {isActive && (
                      <div 
                        className="
                          absolute inset-0
                          rounded-[var(--premium-radius-full)]
                          transition-all duration-500
                          shadow-[0_4px_20px_rgba(0,0,0,0.2)]
                        "
                        style={{ 
                          background: config.color,
                        }}
                      />
                    )}

                    {/* Content */}
                    <Icon 
                      size={16} 
                      strokeWidth={2}
                      className="relative z-10"
                    />
                    <span className="relative z-10 body-sm font-medium">
                      {config.label}
                    </span>
                  </button>
                );
              })}
            </div>

            {/* Subtle Hint Text */}
            <div className="
              mt-[var(--premium-space-lg)]
              body-xs font-light text-[var(--premium-text-muted)]
            ">
              {transactionType === 'transfer' 
                ? 'Move money between accounts'
                : transactionType === 'income'
                ? 'Record money received'
                : 'Track your spending'}
            </div>
          </div>

          {/* Bottom Section - Premium Keypad */}
          {!isScanning && (
            <div className="
              pb-[var(--premium-space-md)]
              animate-[slideUp_0.4s_ease-out]
            ">
              <ZenCalculatorKeypad
                onNumberClick={handleNumberClick}
                onOperatorClick={handleOperatorClick}
                onDelete={handleDelete}
                onDecimal={handleDecimal}
                onEquals={handleEquals}
                onClear={handleClear}
              />
            </div>
          )}

          {/* Scan Mode */}
          {isScanning && (
            <div className="
              flex-1
              flex items-center justify-center
              pb-[var(--premium-space-xl)]
              animate-[fadeIn_0.3s_ease-out]
            ">
              <PremiumScanInput
                isScanning={isScanning}
                onStartScan={() => {}}
                onStopScan={handleScanToggle}
                onUploadImage={handleUploadImage}
              />
            </div>
          )}
        </div>
      </div>

      {/* Floating AI Voice Button */}
      <FloatingAIVoice
        isListening={isListening}
        onToggle={handleVoiceToggle}
      />

      {/* Floating Camera Button */}
      {!isScanning && (
        <button
          onClick={handleScanToggle}
          className="
            fixed bottom-[120px] left-[var(--premium-space-lg)] z-50
            w-[56px] h-[56px]
            rounded-full
            backdrop-blur-[30px]
            bg-[var(--premium-glass-bg)]
            border-2 border-[var(--premium-glass-border)]
            flex items-center justify-center
            text-[var(--premium-text-primary)]
            hover:border-[var(--premium-emerald)]
            hover:scale-105
            active:scale-95
            transition-all duration-200
          "
        >
          <Camera size={24} strokeWidth={2} />
        </button>
      )}

      {/* Category Drawer - Progressive Disclosure */}
      <BottomSheet
        isOpen={showCategoryDrawer}
        onClose={() => setShowCategoryDrawer(false)}
        title="Choose Category"
      >
        <PremiumCategoryPicker
          selectedCategory={selectedCategory}
          onSelectCategory={handleCategorySelect}
        />
      </BottomSheet>

      {/* Account Transfer Drawer */}
      <BottomSheet
        isOpen={showAccountDrawer}
        onClose={() => setShowAccountDrawer(false)}
        title="Transfer Between Accounts"
      >
        <div className="space-y-[var(--premium-space-xl)]">
          <PremiumAccountSelector
            label="From Account"
            selectedAccountId={fromAccountId}
            onSelectAccount={setFromAccountId}
            excludeAccountId={toAccountId}
          />

          {/* Visual Arrow Indicator */}
          <div className="flex justify-center">
            <div className="
              w-[48px] h-[48px]
              rounded-full
              bg-[var(--premium-surface-2)]
              flex items-center justify-center
            ">
              <ArrowDownLeft 
                size={24} 
                className="text-[var(--premium-emerald)]"
              />
            </div>
          </div>

          <PremiumAccountSelector
            label="To Account"
            selectedAccountId={toAccountId}
            onSelectAccount={setToAccountId}
            excludeAccountId={fromAccountId}
          />

          <button
            onClick={handleAccountsConfirm}
            disabled={!fromAccountId || !toAccountId}
            className={`
              w-full
              h-[56px]
              rounded-[var(--premium-radius-full)]
              font-medium text-[18px]
              transition-all duration-200
              ${fromAccountId && toAccountId
                ? 'bg-[var(--premium-emerald)] text-white active:scale-95'
                : 'bg-[var(--premium-surface-3)] text-[var(--premium-text-muted)] cursor-not-allowed'
              }
            `}
          >
            Continue
          </button>
        </div>
      </BottomSheet>

      {/* Confirm Drawer */}
      <BottomSheet
        isOpen={showConfirmDrawer}
        onClose={() => setShowConfirmDrawer(false)}
        title="Confirm Transaction"
      >
        <div className="space-y-[var(--premium-space-lg)]">
          {/* Summary Card - Glassmorphism */}
          <div className="
            p-[var(--premium-space-xl)]
            rounded-[var(--premium-radius-2xl)]
            backdrop-blur-[30px]
            bg-[var(--premium-glass-bg)]
            border border-[var(--premium-glass-border)]
          ">
            <div className="flex items-start justify-between mb-[var(--premium-space-md)]">
              <div>
                <p className="body-sm text-[var(--premium-text-tertiary)] mb-[8px]">
                  {getTypeConfig(transactionType).label}
                </p>
                <h2 className="text-[48px] font-light text-[var(--premium-text-primary)] leading-none">
                  ${displayValue}
                </h2>
              </div>
              <div 
                className="
                  w-[56px] h-[56px]
                  rounded-[var(--premium-radius-xl)]
                  flex items-center justify-center
                "
                style={{ 
                  background: `${getTypeConfig(transactionType).color}20` 
                }}
              >
                {(() => {
                  const Icon = getTypeConfig(transactionType).icon;
                  return <Icon size={28} style={{ color: getTypeConfig(transactionType).color }} />;
                })()}
              </div>
            </div>

            {transactionType === 'transfer' ? (
              <div className="space-y-[4px]">
                <p className="body-md text-[var(--premium-text-secondary)]">
                  From: <span className="font-medium capitalize">{fromAccountId?.replace('-', ' ')}</span>
                </p>
                <p className="body-md text-[var(--premium-text-secondary)]">
                  To: <span className="font-medium capitalize">{toAccountId?.replace('-', ' ')}</span>
                </p>
              </div>
            ) : (
              <p className="body-lg text-[var(--premium-text-secondary)] capitalize">
                {selectedCategory}
              </p>
            )}
          </div>

          {/* Optional Date & Notes */}
          <div className="space-y-[var(--premium-space-sm)]">
            <div className="
              p-[var(--premium-space-md)]
              rounded-[var(--premium-radius-lg)]
              bg-[var(--premium-surface-2)]
              flex items-center gap-[var(--premium-space-sm)]
            ">
              <Calendar size={18} className="text-[var(--premium-text-tertiary)]" />
              <span className="body-sm text-[var(--premium-text-secondary)]">
                {selectedDate.toLocaleDateString('en-US', {
                  month: 'short',
                  day: 'numeric',
                  year: 'numeric',
                })}
              </span>
            </div>

            <textarea
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              placeholder="Add a note (optional)..."
              className="
                w-full
                min-h-[80px]
                p-[var(--premium-space-md)]
                rounded-[var(--premium-radius-lg)]
                bg-[var(--premium-surface-2)]
                text-[var(--premium-text-primary)]
                placeholder:text-[var(--premium-text-muted)]
                border border-transparent
                focus:border-[var(--premium-emerald)]/30
                outline-none
                resize-none
                body-sm
                transition-all duration-200
              "
            />
          </div>

          {/* Elegant Save Button */}
          <button
            onClick={handleSave}
            className="
              w-full
              h-[56px]
              rounded-[var(--premium-radius-full)]
              bg-[var(--premium-emerald)]
              text-white
              font-medium text-[18px]
              hover:bg-[var(--premium-emerald-dark)]
              active:scale-[0.98]
              transition-all duration-200
              shadow-[0_8px_24px_rgba(16,185,129,0.3)]
            "
          >
            Save Transaction
          </button>
        </div>
      </BottomSheet>
    </div>
  );
}
