/**
 * AddExpenseForm Organism Component
 * Form for creating new expense entries
 */

import { useState } from 'react';
import { X } from 'lucide-react';
import { Button } from '../atoms/Button';
import { IconButton } from '../atoms/IconButton';
import { CategoryIcon } from '../atoms/CategoryIcon';
import type { ExpenseFormData, TransactionCategory } from '../../../types/domain';
import { CATEGORY_METADATA } from '../../../mocks/data';

export interface AddExpenseFormProps {
  /** Callback when form is submitted */
  onSubmit: (formData: ExpenseFormData) => void;
  /** Callback when form is cancelled */
  onCancel: () => void;
  /** Loading state during submission */
  isSubmitting?: boolean;
}

/**
 * Material 3 Add Expense Form
 * Modal form for creating expenses
 */
export function AddExpenseForm({
  onSubmit,
  onCancel,
  isSubmitting = false,
}: AddExpenseFormProps) {
  const [formData, setFormData] = useState<ExpenseFormData>({
    amount: '',
    category: '',
    description: '',
    date: new Date().toISOString().split('T')[0],
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    // Validate
    const newErrors: Record<string, string> = {};
    if (!formData.amount || parseFloat(formData.amount) <= 0) {
      newErrors.amount = 'Please enter a valid amount';
    }
    if (!formData.category) {
      newErrors.category = 'Please select a category';
    }
    if (!formData.description.trim()) {
      newErrors.description = 'Please enter a description';
    }

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    onSubmit(formData);
  };

  // Filter out income category for expense form
  const expenseCategories = CATEGORY_METADATA.filter(cat => cat.id !== 'income');

  return (
    <div className="fixed inset-0 bg-black/50 z-50 flex items-end sm:items-center justify-center p-[var(--spacing-md)]">
      <div className="bg-[var(--color-surface)] rounded-t-[28px] sm:rounded-[28px] w-full max-w-lg max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="sticky top-0 bg-[var(--color-surface)] border-b border-[var(--color-outline-variant)] p-[var(--spacing-lg)] flex items-center justify-between">
          <h2 className="text-[var(--color-on-surface)]">Add Expense</h2>
          <IconButton
            icon={<X size={20} />}
            label="Close"
            onClick={onCancel}
          />
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-[var(--spacing-lg)] space-y-[var(--spacing-lg)]">
          {/* Amount Input */}
          <div>
            <label className="block text-sm text-[var(--color-on-surface-variant)] mb-[var(--spacing-sm)]">
              Amount
            </label>
            <div className="relative">
              <span className="absolute left-[var(--spacing-md)] top-1/2 -translate-y-1/2 text-2xl text-[var(--color-on-surface-variant)]">
                $
              </span>
              <input
                type="number"
                step="0.01"
                value={formData.amount}
                onChange={(e) => {
                  setFormData({ ...formData, amount: e.target.value });
                  setErrors({ ...errors, amount: '' });
                }}
                placeholder="0.00"
                className="
                  w-full 
                  pl-[40px] pr-[var(--spacing-md)] 
                  py-[var(--spacing-md)]
                  text-2xl
                  bg-[var(--color-surface-variant)]
                  rounded-2xl
                  border-2 border-[var(--color-outline-variant)]
                  focus:border-[var(--color-primary)]
                  focus:outline-none
                  transition-colors
                  text-[var(--color-on-surface)]
                "
                disabled={isSubmitting}
              />
            </div>
            {errors.amount && (
              <p className="text-sm text-[var(--color-error)] mt-[var(--spacing-xs)]">
                {errors.amount}
              </p>
            )}
          </div>

          {/* Category Selection */}
          <div>
            <label className="block text-sm text-[var(--color-on-surface-variant)] mb-[var(--spacing-sm)]">
              Category
            </label>
            <div className="grid grid-cols-4 gap-[var(--spacing-sm)]">
              {expenseCategories.map((category) => {
                const isSelected = formData.category === category.id;
                return (
                  <button
                    key={category.id}
                    type="button"
                    onClick={() => {
                      setFormData({ ...formData, category: category.id as TransactionCategory });
                      setErrors({ ...errors, category: '' });
                    }}
                    disabled={isSubmitting}
                    className={`
                      flex flex-col items-center gap-[var(--spacing-sm)]
                      p-[var(--spacing-sm)]
                      rounded-2xl
                      transition-all
                      ${isSelected
                        ? 'ring-2 ring-offset-2 ring-[var(--color-primary)]'
                        : 'hover:bg-[var(--color-surface-variant)]'
                      }
                    `}
                    style={{
                      backgroundColor: isSelected ? `${category.colorToken}30` : 'var(--color-surface-variant)',
                      color: isSelected ? category.colorToken : 'var(--color-on-surface-variant)',
                    }}
                  >
                    <CategoryIcon category={category.id as TransactionCategory} size={24} />
                    <span className="text-xs">{category.label.split(' ')[0]}</span>
                  </button>
                );
              })}
            </div>
            {errors.category && (
              <p className="text-sm text-[var(--color-error)] mt-[var(--spacing-xs)]">
                {errors.category}
              </p>
            )}
          </div>

          {/* Description */}
          <div>
            <label className="block text-sm text-[var(--color-on-surface-variant)] mb-[var(--spacing-sm)]">
              Description
            </label>
            <input
              type="text"
              value={formData.description}
              onChange={(e) => {
                setFormData({ ...formData, description: e.target.value });
                setErrors({ ...errors, description: '' });
              }}
              placeholder="What did you spend on?"
              className="
                w-full
                px-[var(--spacing-md)]
                py-[var(--spacing-sm)]
                bg-[var(--color-surface-variant)]
                rounded-2xl
                border-2 border-[var(--color-outline-variant)]
                focus:border-[var(--color-primary)]
                focus:outline-none
                transition-colors
                text-[var(--color-on-surface)]
              "
              disabled={isSubmitting}
            />
            {errors.description && (
              <p className="text-sm text-[var(--color-error)] mt-[var(--spacing-xs)]">
                {errors.description}
              </p>
            )}
          </div>

          {/* Date */}
          <div>
            <label className="block text-sm text-[var(--color-on-surface-variant)] mb-[var(--spacing-sm)]">
              Date
            </label>
            <input
              type="date"
              value={formData.date}
              onChange={(e) => setFormData({ ...formData, date: e.target.value })}
              className="
                w-full
                px-[var(--spacing-md)]
                py-[var(--spacing-sm)]
                bg-[var(--color-surface-variant)]
                rounded-2xl
                border-2 border-[var(--color-outline-variant)]
                focus:border-[var(--color-primary)]
                focus:outline-none
                transition-colors
                text-[var(--color-on-surface)]
              "
              disabled={isSubmitting}
            />
          </div>

          {/* Submit Button */}
          <Button
            type="submit"
            variant="filled"
            size="large"
            fullWidth
            disabled={isSubmitting}
          >
            {isSubmitting ? 'Adding...' : 'Add Expense'}
          </Button>
        </form>
      </div>
    </div>
  );
}
