/**
 * Premium Add Transaction Screen - Enhanced Omni-Input
 * Supports Expense, Income, and Transfer with AI Voice
 * Material 3 design with progressive disclosure
 */

import { useState, useEffect } from 'react';
import { X, Calculator, Mic, Camera, ChevronRight, ArrowUpRight, ArrowDownLeft, ArrowLeftRight } from 'lucide-react';
import { PremiumCategoryPicker } from '../components/premium/PremiumCategoryPicker';
import { PremiumAccountSelector } from '../components/premium/PremiumAccountSelector';
import { PremiumCalculatorKeypad } from '../components/premium/PremiumCalculatorKeypad';
import { PremiumEnhancedVoiceInput, type VoiceParseResult } from '../components/premium/PremiumEnhancedVoiceInput';
import { PremiumScanInput } from '../components/premium/PremiumScanInput';
import { PremiumCalendar } from '../components/premium/PremiumCalendar';
import type { TransactionCategory, TransactionType } from '../../types/domain';
import { Calendar, FileText } from 'lucide-react';

export interface PremiumAddTransactionScreenProps {
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

type InputMode = 'manual' | 'voice' | 'scan';
type FlowState = 'amount' | 'type' | 'details' | 'confirm';

export function PremiumAddTransactionScreen({
  onClose,
  onSave,
}: PremiumAddTransactionScreenProps) {
  // Input Mode State
  const [inputMode, setInputMode] = useState<InputMode>('manual');
  const [flowState, setFlowState] = useState<FlowState>('amount');

  // Manual Input State
  const [displayValue, setDisplayValue] = useState('0');
  const [calculationString, setCalculationString] = useState('');
  const [currentOperator, setCurrentOperator] = useState<string | null>(null);
  const [previousValue, setPreviousValue] = useState<number | null>(null);

  // Voice Input State
  const [isListening, setIsListening] = useState(false);

  // Scan Input State
  const [isScanning, setIsScanning] = useState(false);

  // Transaction State
  const [transactionType, setTransactionType] = useState<TransactionType>('expense');
  const [selectedCategory, setSelectedCategory] = useState<TransactionCategory>();
  const [fromAccountId, setFromAccountId] = useState<string>();
  const [toAccountId, setToAccountId] = useState<string>();
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [notes, setNotes] = useState('');
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [showNotesInput, setShowNotesInput] = useState(false);

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

  const handleNextToType = () => {
    if (parseFloat(displayValue) > 0) {
      setFlowState('type');
    }
  };

  const handleSelectType = (type: TransactionType) => {
    setTransactionType(type);
    setFlowState('details');
  };

  const handleSelectCategory = (category: TransactionCategory) => {
    setSelectedCategory(category);
    setFlowState('confirm');
  };

  const handleTransferAccountsSet = () => {
    if (fromAccountId && toAccountId) {
      setFlowState('confirm');
    }
  };

  // Voice Input Handlers with AI Parsing
  const handleStartListening = () => {
    setIsListening(true);
  };

  const handleStopListening = () => {
    setIsListening(false);
  };

  const handleVoiceParsed = (result: VoiceParseResult) => {
    // Set amount
    if (result.amount) {
      setDisplayValue(result.amount.toString());
    }

    // Set type and navigate
    if (result.type) {
      setTransactionType(result.type);
      
      // Handle different transaction types
      if (result.type === 'transfer' && result.fromAccount && result.toAccount) {
        setFromAccountId(result.fromAccount);
        setToAccountId(result.toAccount);
        setFlowState('confirm');
      } else if ((result.type === 'expense' || result.type === 'income') && result.category) {
        setSelectedCategory(result.category as TransactionCategory);
        setFlowState('confirm');
      } else {
        setFlowState('type');
      }
    }

    // Set notes/description
    if (result.description) {
      setNotes(result.description);
    }

    // Switch back to manual mode
    setInputMode('manual');
  };

  // Scan Input Handlers
  const handleStartScan = () => {
    setIsScanning(true);
  };

  const handleStopScan = () => {
    setIsScanning(false);
  };

  const handleUploadImage = (file: File) => {
    console.log('Processing image:', file.name);
    setTimeout(() => {
      setDisplayValue('32.50');
      setInputMode('manual');
      setIsScanning(false);
    }, 1500);
  };

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

  const handleModeChange = (mode: InputMode) => {
    setInputMode(mode);
    if (mode === 'voice') {
      setIsListening(false);
    } else if (mode === 'scan') {
      setIsScanning(false);
    }
  };

  // Check if all required fields are filled
  const isFormValid = () => {
    const hasAmount = parseFloat(displayValue) > 0;
    if (transactionType === 'transfer') {
      return hasAmount && fromAccountId && toAccountId;
    }
    return hasAmount && selectedCategory;
  };

  const getTypeColor = (type: TransactionType) => {
    switch (type) {
      case 'expense':
        return {
          bg: 'bg-[#f5576c]/10',
          text: 'text-[#f5576c]',
          border: 'border-[#f5576c]',
          icon: ArrowUpRight,
        };
      case 'income':
        return {
          bg: 'bg-[#4facfe]/10',
          text: 'text-[#4facfe]',
          border: 'border-[#4facfe]',
          icon: ArrowDownLeft,
        };
      case 'transfer':
        return {
          bg: 'bg-[var(--premium-emerald)]/10',
          text: 'text-[var(--premium-emerald)]',
          border: 'border-[var(--premium-emerald)]',
          icon: ArrowLeftRight,
        };
    }
  };

  return (
    <div className="
      fixed inset-0 z-[100]
      bg-[var(--premium-bg-primary)]
      flex flex-col
    ">
      {/* Minimal Header */}
      <div className="
        px-[var(--premium-space-lg)]
        pt-[var(--premium-space-lg)]
        pb-[var(--premium-space-md)]
        flex items-center justify-between
      ">
        <div className="flex items-center gap-[8px]">
          {flowState !== 'amount' && (
            <button
              onClick={() => {
                if (flowState === 'type') setFlowState('amount');
                else if (flowState === 'details') setFlowState('type');
                else if (flowState === 'confirm') setFlowState('details');
              }}
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
            >
              <ChevronRight size={20} className="rotate-180" />
            </button>
          )}
          <h1 className="body-md font-medium text-[var(--premium-text-secondary)]">
            {flowState === 'amount' && 'New Transaction'}
            {flowState === 'type' && 'Transaction Type'}
            {flowState === 'details' && (transactionType === 'transfer' ? 'Select Accounts' : 'Choose Category')}
            {flowState === 'confirm' && 'Confirm & Save'}
          </h1>
        </div>
        <button
          onClick={onClose}
          aria-label="Close"
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
        >
          <X size={20} />
        </button>
      </div>

      {/* Main Content Area */}
      <div className="flex-1 flex flex-col overflow-hidden">
        
        {/* AMOUNT INPUT STATE */}
        {flowState === 'amount' && (
          <>
            {/* Amount Display - Top Section */}
            <div className="
              flex-1
              flex flex-col items-center justify-center
              px-[var(--premium-space-lg)]
              pb-[var(--premium-space-xl)]
            ">
              {/* Calculation String */}
              {calculationString && (
                <p className="body-sm text-[var(--premium-text-tertiary)] mb-[8px]">
                  {calculationString}
                </p>
              )}

              {/* Large Amount Display */}
              <div className="
                text-[64px] font-bold
                text-[var(--premium-text-primary)]
                leading-none
                mb-[var(--premium-space-md)]
                transition-all duration-200
              ">
                ${displayValue}
              </div>

              {/* Input Mode Switcher */}
              <div className="
                flex items-center gap-[var(--premium-space-lg)]
                p-[var(--premium-space-sm)]
                bg-[var(--premium-surface-1)]
                rounded-[var(--premium-radius-full)]
              ">
                <button
                  onClick={() => handleModeChange('manual')}
                  className={`
                    w-[48px] h-[48px]
                    rounded-full
                    flex items-center justify-center
                    transition-all duration-200
                    ${inputMode === 'manual'
                      ? 'bg-[var(--premium-surface-3)] text-[var(--premium-text-primary)] shadow-[var(--premium-shadow-sm)]'
                      : 'text-[var(--premium-text-tertiary)] hover:text-[var(--premium-text-secondary)]'
                    }
                  `}
                  aria-label="Manual Input"
                >
                  <Calculator size={22} strokeWidth={2} />
                </button>

                <button
                  onClick={() => handleModeChange('voice')}
                  className={`
                    w-[48px] h-[48px]
                    rounded-full
                    flex items-center justify-center
                    transition-all duration-200
                    ${inputMode === 'voice'
                      ? 'bg-[var(--premium-emerald)] text-white shadow-[var(--premium-shadow-md)]'
                      : 'text-[var(--premium-text-tertiary)] hover:text-[var(--premium-text-secondary)]'
                    }
                  `}
                  aria-label="Voice Input"
                >
                  <Mic size={22} strokeWidth={2} />
                </button>

                <button
                  onClick={() => handleModeChange('scan')}
                  className={`
                    w-[48px] h-[48px]
                    rounded-full
                    flex items-center justify-center
                    transition-all duration-200
                    ${inputMode === 'scan'
                      ? 'bg-[var(--premium-surface-3)] text-[var(--premium-text-primary)] shadow-[var(--premium-shadow-sm)]'
                      : 'text-[var(--premium-text-tertiary)] hover:text-[var(--premium-text-secondary)]'
                    }
                  `}
                  aria-label="Scan Receipt"
                >
                  <Camera size={22} strokeWidth={2} />
                </button>
              </div>
            </div>

            {/* Input Method Content - Bottom Section with Material 3 Elevation */}
            <div className="
              bg-[var(--premium-bg-secondary)]
              border-t border-[var(--premium-glass-border)]
              shadow-[var(--premium-shadow-lg)]
              rounded-t-[var(--premium-radius-2xl)]
              overflow-hidden
            ">
              {inputMode === 'manual' && (
                <div className="p-[var(--premium-space-lg)]">
                  <PremiumCalculatorKeypad
                    onNumberClick={handleNumberClick}
                    onOperatorClick={handleOperatorClick}
                    onDelete={handleDelete}
                    onDecimal={handleDecimal}
                    onEquals={handleEquals}
                    onClear={handleClear}
                  />
                  
                  <button
                    onClick={handleNextToType}
                    disabled={parseFloat(displayValue) <= 0}
                    className={`
                      w-full
                      h-[56px]
                      mt-[var(--premium-space-md)]
                      rounded-[var(--premium-radius-full)]
                      font-medium text-[18px]
                      flex items-center justify-center gap-[8px]
                      transition-all duration-200
                      ${parseFloat(displayValue) > 0
                        ? 'bg-[var(--premium-emerald)] text-white active:scale-95'
                        : 'bg-[var(--premium-surface-3)] text-[var(--premium-text-muted)] cursor-not-allowed'
                      }
                    `}
                  >
                    Next
                    <ChevronRight size={20} />
                  </button>
                </div>
              )}

              {inputMode === 'voice' && (
                <div className="py-[var(--premium-space-xl)]">
                  <PremiumEnhancedVoiceInput
                    isListening={isListening}
                    onStartListening={handleStartListening}
                    onStopListening={handleStopListening}
                    onVoiceParsed={handleVoiceParsed}
                  />
                </div>
              )}

              {inputMode === 'scan' && (
                <div className="py-[var(--premium-space-xl)]">
                  <PremiumScanInput
                    isScanning={isScanning}
                    onStartScan={handleStartScan}
                    onStopScan={handleStopScan}
                    onUploadImage={handleUploadImage}
                  />
                </div>
              )}
            </div>
          </>
        )}

        {/* TYPE SELECTION STATE */}
        {flowState === 'type' && (
          <div className="flex-1 flex items-center justify-center px-[var(--premium-space-lg)]">
            <div className="w-full max-w-md space-y-[var(--premium-space-md)]">
              {/* Amount Summary */}
              <div className="text-center mb-[var(--premium-space-xl)]">
                <p className="body-sm text-[var(--premium-text-tertiary)] mb-[4px]">
                  Amount
                </p>
                <p className="heading-2xl text-[var(--premium-text-primary)]">
                  ${displayValue}
                </p>
              </div>

              <p className="body-md text-[var(--premium-text-tertiary)] text-center mb-[var(--premium-space-md)]">
                What type of transaction?
              </p>

              {/* Expense Option */}
              <button
                onClick={() => handleSelectType('expense')}
                className="
                  w-full
                  p-[var(--premium-space-lg)]
                  rounded-[var(--premium-radius-xl)]
                  bg-[var(--premium-surface-2)]
                  hover:bg-[var(--premium-surface-3)]
                  active:scale-[0.98]
                  transition-all duration-200
                  flex items-center gap-[var(--premium-space-md)]
                "
              >
                <div className="
                  w-[56px] h-[56px]
                  rounded-[var(--premium-radius-lg)]
                  bg-[#f5576c]/10
                  flex items-center justify-center
                ">
                  <ArrowUpRight size={28} strokeWidth={2} style={{ color: '#f5576c' }} />
                </div>
                <div className="flex-1 text-left">
                  <h3 className="heading-5 text-[var(--premium-text-primary)] mb-[2px]">
                    Expense
                  </h3>
                  <p className="body-sm text-[var(--premium-text-tertiary)]">
                    Money spent on purchases
                  </p>
                </div>
                <ChevronRight size={20} className="text-[var(--premium-text-muted)]" />
              </button>

              {/* Income Option */}
              <button
                onClick={() => handleSelectType('income')}
                className="
                  w-full
                  p-[var(--premium-space-lg)]
                  rounded-[var(--premium-radius-xl)]
                  bg-[var(--premium-surface-2)]
                  hover:bg-[var(--premium-surface-3)]
                  active:scale-[0.98]
                  transition-all duration-200
                  flex items-center gap-[var(--premium-space-md)]
                "
              >
                <div className="
                  w-[56px] h-[56px]
                  rounded-[var(--premium-radius-lg)]
                  bg-[#4facfe]/10
                  flex items-center justify-center
                ">
                  <ArrowDownLeft size={28} strokeWidth={2} style={{ color: '#4facfe' }} />
                </div>
                <div className="flex-1 text-left">
                  <h3 className="heading-5 text-[var(--premium-text-primary)] mb-[2px]">
                    Income
                  </h3>
                  <p className="body-sm text-[var(--premium-text-tertiary)]">
                    Money received
                  </p>
                </div>
                <ChevronRight size={20} className="text-[var(--premium-text-muted)]" />
              </button>

              {/* Transfer Option */}
              <button
                onClick={() => handleSelectType('transfer')}
                className="
                  w-full
                  p-[var(--premium-space-lg)]
                  rounded-[var(--premium-radius-xl)]
                  bg-[var(--premium-surface-2)]
                  hover:bg-[var(--premium-surface-3)]
                  active:scale-[0.98]
                  transition-all duration-200
                  flex items-center gap-[var(--premium-space-md)]
                "
              >
                <div className="
                  w-[56px] h-[56px]
                  rounded-[var(--premium-radius-lg)]
                  bg-[var(--premium-emerald)]/10
                  flex items-center justify-center
                ">
                  <ArrowLeftRight size={28} strokeWidth={2} style={{ color: 'var(--premium-emerald)' }} />
                </div>
                <div className="flex-1 text-left">
                  <h3 className="heading-5 text-[var(--premium-text-primary)] mb-[2px]">
                    Transfer
                  </h3>
                  <p className="body-sm text-[var(--premium-text-tertiary)]">
                    Move money between accounts
                  </p>
                </div>
                <ChevronRight size={20} className="text-[var(--premium-text-muted)]" />
              </button>
            </div>
          </div>
        )}

        {/* DETAILS STATE - Category or Accounts */}
        {flowState === 'details' && (
          <div className="flex-1 overflow-y-auto px-[var(--premium-space-lg)] py-[var(--premium-space-xl)] animate-[slideUp_0.3s_ease-out]">
            <div className="max-w-md mx-auto">
              {/* Amount & Type Summary */}
              <div className="text-center mb-[var(--premium-space-xl)]">
                <p className="body-sm text-[var(--premium-text-tertiary)] mb-[4px]">
                  {transactionType === 'expense' ? 'Expense' : transactionType === 'income' ? 'Income' : 'Transfer'}
                </p>
                <p className="heading-2xl text-[var(--premium-text-primary)]">
                  ${displayValue}
                </p>
              </div>

              {transactionType === 'transfer' ? (
                <div className="space-y-[var(--premium-space-xl)]">
                  <PremiumAccountSelector
                    label="From Account"
                    selectedAccountId={fromAccountId}
                    onSelectAccount={setFromAccountId}
                    excludeAccountId={toAccountId}
                  />
                  
                  <PremiumAccountSelector
                    label="To Account"
                    selectedAccountId={toAccountId}
                    onSelectAccount={setToAccountId}
                    excludeAccountId={fromAccountId}
                  />

                  <button
                    onClick={handleTransferAccountsSet}
                    disabled={!fromAccountId || !toAccountId}
                    className={`
                      w-full
                      h-[56px]
                      rounded-[var(--premium-radius-full)]
                      font-medium text-[18px]
                      flex items-center justify-center gap-[8px]
                      transition-all duration-200
                      ${fromAccountId && toAccountId
                        ? 'bg-[var(--premium-emerald)] text-white active:scale-95'
                        : 'bg-[var(--premium-surface-3)] text-[var(--premium-text-muted)] cursor-not-allowed'
                      }
                    `}
                  >
                    Next
                    <ChevronRight size={20} />
                  </button>
                </div>
              ) : (
                <>
                  <p className="body-md text-[var(--premium-text-tertiary)] mb-[var(--premium-space-lg)] text-center">
                    Choose a category
                  </p>
                  <PremiumCategoryPicker
                    selectedCategory={selectedCategory}
                    onSelectCategory={handleSelectCategory}
                  />
                </>
              )}
            </div>
          </div>
        )}

        {/* CONFIRM STATE */}
        {flowState === 'confirm' && (
          <div className="flex-1 overflow-y-auto px-[var(--premium-space-lg)] py-[var(--premium-space-xl)]">
            <div className="max-w-md mx-auto space-y-[var(--premium-space-lg)]">
              
              {/* Summary Card */}
              <div className={`
                p-[var(--premium-space-xl)]
                rounded-[var(--premium-radius-xl)]
                ${getTypeColor(transactionType).bg}
                border-2
                ${getTypeColor(transactionType).border}
              `}>
                <div className="flex items-start justify-between mb-[var(--premium-space-md)]">
                  <div>
                    <p className={`body-sm mb-[8px] ${getTypeColor(transactionType).text}`}>
                      {transactionType === 'expense' ? 'Expense' : transactionType === 'income' ? 'Income' : 'Transfer'}
                    </p>
                    <h2 className="heading-3xl text-[var(--premium-text-primary)]">
                      ${displayValue}
                    </h2>
                  </div>
                  <div className={`
                    w-[48px] h-[48px]
                    rounded-[var(--premium-radius-md)]
                    ${getTypeColor(transactionType).bg}
                    flex items-center justify-center
                  `}>
                    {(() => {
                      const Icon = getTypeColor(transactionType).icon;
                      return <Icon size={24} className={getTypeColor(transactionType).text} />;
                    })()}
                  </div>
                </div>

                {transactionType === 'transfer' ? (
                  <div className="space-y-[8px]">
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

              {/* Date */}
              <button
                onClick={() => setShowDatePicker(!showDatePicker)}
                className="
                  w-full
                  p-[var(--premium-space-md)]
                  rounded-[var(--premium-radius-lg)]
                  bg-[var(--premium-surface-2)]
                  hover:bg-[var(--premium-surface-3)]
                  transition-all duration-200
                  flex items-center justify-between
                "
              >
                <div className="flex items-center gap-[var(--premium-space-sm)]">
                  <Calendar size={20} className="text-[var(--premium-text-tertiary)]" />
                  <span className="body-md text-[var(--premium-text-primary)]">
                    {selectedDate.toLocaleDateString('en-US', {
                      month: 'short',
                      day: 'numeric',
                      year: 'numeric',
                    })}
                  </span>
                </div>
              </button>

              {showDatePicker && (
                <div className="p-[var(--premium-space-md)] bg-[var(--premium-surface-2)] rounded-[var(--premium-radius-lg)]">
                  <PremiumCalendar
                    selectedDate={selectedDate}
                    onSelectDate={(date) => {
                      setSelectedDate(date);
                      setShowDatePicker(false);
                    }}
                  />
                </div>
              )}

              {/* Notes */}
              {!showNotesInput ? (
                <button
                  onClick={() => setShowNotesInput(true)}
                  className="
                    w-full
                    p-[var(--premium-space-md)]
                    rounded-[var(--premium-radius-lg)]
                    bg-[var(--premium-surface-2)]
                    hover:bg-[var(--premium-surface-3)]
                    transition-all duration-200
                    flex items-center justify-between
                  "
                >
                  <div className="flex items-center gap-[var(--premium-space-sm)]">
                    <FileText size={20} className="text-[var(--premium-text-tertiary)]" />
                    <span className="body-md text-[var(--premium-text-tertiary)]">
                      Add a note (optional)
                    </span>
                  </div>
                </button>
              ) : (
                <div className="p-[var(--premium-space-md)] bg-[var(--premium-surface-2)] rounded-[var(--premium-radius-lg)]">
                  <textarea
                    value={notes}
                    onChange={(e) => setNotes(e.target.value)}
                    placeholder="Add a note..."
                    autoFocus
                    className="
                      w-full
                      min-h-[80px]
                      bg-transparent
                      text-[var(--premium-text-primary)]
                      placeholder:text-[var(--premium-text-muted)]
                      border-none
                      outline-none
                      resize-none
                      body-md
                    "
                  />
                </div>
              )}

              {/* Save Button - Only show when valid */}
              {isFormValid() && (
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
                    animate-[slideUp_0.3s_ease-out]
                  "
                >
                  Save Transaction
                </button>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
