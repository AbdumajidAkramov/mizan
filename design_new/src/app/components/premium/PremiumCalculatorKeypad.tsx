/**
 * Premium Calculator Keypad Component
 * Full calculator with math operators for quick calculations
 */

import { Delete } from 'lucide-react';

export interface PremiumCalculatorKeypadProps {
  onNumberClick: (num: string) => void;
  onOperatorClick: (operator: string) => void;
  onDelete: () => void;
  onDecimal: () => void;
  onEquals: () => void;
  onClear: () => void;
}

const keypadLayout = [
  ['C', '/', '*', 'delete'],
  ['7', '8', '9', '-'],
  ['4', '5', '6', '+'],
  ['1', '2', '3', '='],
  ['0', '00', '.', '='],
];

export function PremiumCalculatorKeypad({
  onNumberClick,
  onOperatorClick,
  onDelete,
  onDecimal,
  onEquals,
  onClear,
}: PremiumCalculatorKeypadProps) {
  const handlePress = (value: string) => {
    if (value === 'delete') {
      onDelete();
    } else if (value === '.') {
      onDecimal();
    } else if (value === '=') {
      onEquals();
    } else if (value === 'C') {
      onClear();
    } else if (['+', '-', '*', '/'].includes(value)) {
      onOperatorClick(value);
    } else {
      onNumberClick(value);
    }
  };

  const getButtonStyle = (btn: string) => {
    const isOperator = ['+', '-', '*', '/', '='].includes(btn);
    const isDelete = btn === 'delete';
    const isClear = btn === 'C';
    const isSpecial = isOperator || isDelete || isClear;

    if (btn === '=') {
      return `
        bg-[var(--premium-emerald)]
        text-white
        font-semibold
        hover:bg-[var(--premium-emerald-dark)]
      `;
    }

    if (isOperator) {
      return `
        bg-[var(--premium-surface-3)]
        text-[var(--premium-emerald)]
        font-semibold
        hover:bg-[var(--premium-surface-4)]
      `;
    }

    if (isDelete || isClear) {
      return `
        bg-[var(--premium-surface-2)]
        text-[var(--premium-error)]
        hover:bg-[var(--premium-surface-3)]
      `;
    }

    return `
      bg-[var(--premium-surface-2)]
      text-[var(--premium-text-primary)]
      hover:bg-[var(--premium-surface-3)]
    `;
  };

  return (
    <div className="grid grid-cols-4 gap-[10px]">
      {keypadLayout.flat().map((btn, idx) => {
        const isDelete = btn === 'delete';
        const isZeroWide = btn === '0';
        
        // Skip rendering duplicate '=' in last row since it spans
        if (idx === 19 && btn === '=') return null;

        return (
          <button
            key={`${btn}-${idx}`}
            onClick={() => handlePress(btn)}
            className={`
              ${isZeroWide ? 'col-span-1' : ''}
              ${idx === 15 ? 'row-span-2' : ''}
              h-[56px]
              rounded-[var(--premium-radius-lg)]
              flex items-center justify-center
              transition-all duration-150
              active:scale-95
              ${getButtonStyle(btn)}
            `}
          >
            {isDelete ? (
              <Delete size={22} strokeWidth={2} />
            ) : (
              <span className="text-[20px] font-medium">{btn}</span>
            )}
          </button>
        );
      })}
    </div>
  );
}
