/**
 * Premium Numeric Keypad Component
 * Sleek, minimal keypad with Material 3 design
 */

import { Delete } from 'lucide-react';

export interface PremiumNumericKeypadProps {
  onNumberClick: (num: string) => void;
  onDelete: () => void;
  onDecimal: () => void;
}

const keypadButtons = [
  ['1', '2', '3'],
  ['4', '5', '6'],
  ['7', '8', '9'],
  ['.', '0', 'delete'],
];

export function PremiumNumericKeypad({
  onNumberClick,
  onDelete,
  onDecimal,
}: PremiumNumericKeypadProps) {
  const handlePress = (value: string) => {
    if (value === 'delete') {
      onDelete();
    } else if (value === '.') {
      onDecimal();
    } else {
      onNumberClick(value);
    }
  };

  return (
    <div className="grid grid-cols-3 gap-[12px]">
      {keypadButtons.flat().map((btn, idx) => {
        const isDelete = btn === 'delete';
        const isDecimal = btn === '.';

        return (
          <button
            key={idx}
            onClick={() => handlePress(btn)}
            className={`
              h-[56px]
              rounded-[var(--premium-radius-lg)]
              flex items-center justify-center
              transition-all duration-150
              active:scale-95
              ${isDelete
                ? 'bg-[var(--premium-surface-2)] text-[var(--premium-text-tertiary)]'
                : 'bg-[var(--premium-surface-2)] text-[var(--premium-text-primary)]'
              }
              hover:bg-[var(--premium-surface-3)]
            `}
          >
            {isDelete ? (
              <Delete size={22} strokeWidth={2} />
            ) : (
              <span className="text-[22px] font-medium">{btn}</span>
            )}
          </button>
        );
      })}
    </div>
  );
}