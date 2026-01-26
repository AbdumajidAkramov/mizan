/**
 * Manage Templates Screen
 * 
 * STANDALONE FULL-SCREEN COMPONENT
 * Allows users to view, reorder, edit, and delete transaction templates
 * 
 * @architecture MVI Pattern - Explicit UI State Management
 * @design Material 3 with 8dp grid system and glassmorphism
 * @navigation Callback-based navigation for easy integration
 * 
 * USAGE:
 * ```tsx
 * <ManageTemplatesScreen
 *   onBack={() => navigateTo('add-transaction')}
 * />
 * ```
 */

import { useState, useCallback } from 'react';
import { DndProvider, useDrag, useDrop } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import {
  ArrowLeft,
  Plus,
  GripVertical,
  Edit2,
  Trash2,
  ArrowUpRight,
  ArrowDownLeft,
  ArrowLeftRight,
  Wallet,
  Building2,
  CreditCard,
  Landmark,
  PiggyBank,
  Bookmark,
  X,
} from 'lucide-react';
import type { TransactionCategory, TransactionType } from '../../types/domain';

/**
 * Template Interface
 */
interface TransactionTemplate {
  id: string;
  name: string;
  amount: number;
  type: TransactionType;
  category?: TransactionCategory;
  subcategory?: string;
  accountId?: string;
  fromAccountId?: string;
  toAccountId?: string;
  notes?: string;
}

const DRAG_TYPE = 'template-item';

/**
 * Draggable Template Row Component
 */
interface DraggableTemplateRowProps {
  template: TransactionTemplate;
  index: number;
  onMove: (dragIndex: number, hoverIndex: number) => void;
  onEdit: (template: TransactionTemplate) => void;
  onDelete: (id: string) => void;
}

function DraggableTemplateRow({
  template,
  index,
  onMove,
  onEdit,
  onDelete,
}: DraggableTemplateRowProps) {
  const [{ isDragging }, drag, preview] = useDrag({
    type: DRAG_TYPE,
    item: { index },
    collect: (monitor) => ({
      isDragging: monitor.isDragging(),
    }),
  });

  const [{ isOver }, drop] = useDrop({
    accept: DRAG_TYPE,
    hover: (item: { index: number }) => {
      if (item.index !== index) {
        onMove(item.index, index);
        item.index = index;
      }
    },
    collect: (monitor) => ({
      isOver: monitor.isOver(),
    }),
  });

  const getTypeColor = (type: TransactionType) => {
    switch (type) {
      case 'expense':
        return {
          bg: 'bg-[#f5576c]/10',
          text: 'text-[#f5576c]',
          icon: ArrowUpRight,
        };
      case 'income':
        return {
          bg: 'bg-[#4facfe]/10',
          text: 'text-[#4facfe]',
          icon: ArrowDownLeft,
        };
      case 'transfer':
        return {
          bg: 'bg-[var(--premium-emerald)]/10',
          text: 'text-[var(--premium-emerald)]',
          icon: ArrowLeftRight,
        };
    }
  };

  const typeColor = getTypeColor(template.type);
  const TypeIcon = typeColor.icon;

  return (
    <div
      ref={(node) => preview(drop(node))}
      className={`
        transition-all duration-200
        ${isDragging ? 'opacity-50' : 'opacity-100'}
        ${isOver ? 'scale-[1.02]' : 'scale-100'}
      `}
    >
      <div
        className="
          flex items-center gap-[var(--premium-space-md)]
          p-[var(--premium-space-md)]
          rounded-[var(--premium-radius-xl)]
          bg-[var(--premium-surface-2)]
          border border-[var(--premium-glass-border)]
          hover:bg-[var(--premium-surface-3)]
          transition-all duration-200
        "
      >
        {/* Drag Handle */}
        <div
          ref={drag}
          className="
            cursor-grab active:cursor-grabbing
            text-[var(--premium-text-muted)]
            hover:text-[var(--premium-text-secondary)]
            transition-colors duration-200
          "
        >
          <GripVertical size={20} />
        </div>

        {/* Type Icon */}
        <div
          className={`
            w-[40px] h-[40px]
            rounded-[var(--premium-radius-lg)]
            ${typeColor.bg}
            flex items-center justify-center
            flex-shrink-0
          `}
        >
          <TypeIcon size={20} className={typeColor.text} />
        </div>

        {/* Template Info */}
        <div className="flex-1 min-w-0">
          <h3 className="body-md font-medium text-[var(--premium-text-primary)] mb-[2px] truncate">
            {template.name}
          </h3>
          <div className="flex items-center gap-[8px]">
            <p className="body-sm text-[var(--premium-text-secondary)]">
              ${template.amount.toFixed(2)}
            </p>
            {template.category && (
              <>
                <span className="text-[var(--premium-text-muted)]">•</span>
                <p className="body-xs text-[var(--premium-text-tertiary)] capitalize truncate">
                  {template.category.replace('-', ' ')}
                  {template.subcategory && ` • ${template.subcategory}`}
                </p>
              </>
            )}
          </div>
        </div>

        {/* Actions */}
        <div className="flex items-center gap-[8px]">
          <button
            onClick={() => onEdit(template)}
            className="
              w-[36px] h-[36px]
              rounded-full
              bg-[var(--premium-surface-3)]
              hover:bg-[var(--premium-emerald)]/15
              text-[var(--premium-text-secondary)]
              hover:text-[var(--premium-emerald)]
              flex items-center justify-center
              transition-all duration-200
              active:scale-95
            "
            aria-label="Edit Template"
          >
            <Edit2 size={16} />
          </button>
          <button
            onClick={() => onDelete(template.id)}
            className="
              w-[36px] h-[36px]
              rounded-full
              bg-[var(--premium-surface-3)]
              hover:bg-[#f5576c]/15
              text-[var(--premium-text-secondary)]
              hover:text-[#f5576c]
              flex items-center justify-center
              transition-all duration-200
              active:scale-95
            "
            aria-label="Delete Template"
          >
            <Trash2 size={16} />
          </button>
        </div>
      </div>
    </div>
  );
}

/**
 * Edit Template Modal Component
 */
interface EditTemplateModalProps {
  template: TransactionTemplate | null;
  onClose: () => void;
  onSave: (template: TransactionTemplate) => void;
}

function EditTemplateModal({ template, onClose, onSave }: EditTemplateModalProps) {
  const [name, setName] = useState(template?.name || '');
  const [amount, setAmount] = useState(template?.amount.toString() || '');

  if (!template) return null;

  const handleSave = () => {
    if (!name.trim() || !amount || parseFloat(amount) <= 0) return;
    
    onSave({
      ...template,
      name: name.trim(),
      amount: parseFloat(amount),
    });
    onClose();
  };

  return (
    <div
      className="
        fixed inset-0 z-[200]
        bg-black/50
        backdrop-blur-sm
        flex items-center justify-center
        p-[var(--premium-space-lg)]
        animate-[fadeIn_0.2s_ease-out]
      "
      onClick={onClose}
    >
      <div
        onClick={(e) => e.stopPropagation()}
        className="
          w-full max-w-md
          bg-[var(--premium-bg-primary)]
          rounded-[var(--premium-radius-2xl)]
          p-[var(--premium-space-xl)]
          border border-[var(--premium-glass-border)]
          shadow-[var(--premium-shadow-2xl)]
          animate-[slideUp_0.3s_ease-out]
        "
      >
        {/* Modal Header */}
        <div className="flex items-center justify-between mb-[var(--premium-space-xl)]">
          <h2 className="heading-4 text-[var(--premium-text-primary)]">
            Edit Template
          </h2>
          <button
            onClick={onClose}
            className="
              w-[32px] h-[32px]
              rounded-full
              bg-[var(--premium-surface-2)]
              hover:bg-[var(--premium-surface-3)]
              flex items-center justify-center
              text-[var(--premium-text-secondary)]
              transition-all duration-200
              active:scale-95
            "
          >
            <X size={20} />
          </button>
        </div>

        {/* Form */}
        <div className="space-y-[var(--premium-space-lg)]">
          {/* Template Name */}
          <div>
            <label className="body-sm text-[var(--premium-text-secondary)] mb-[8px] block">
              Template Name
            </label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="e.g., Daily Lunch"
              className="
                w-full
                px-[var(--premium-space-md)]
                py-[var(--premium-space-sm)]
                bg-[var(--premium-surface-2)]
                text-[var(--premium-text-primary)]
                placeholder:text-[var(--premium-text-muted)]
                border border-[var(--premium-glass-border)]
                rounded-[var(--premium-radius-lg)]
                outline-none
                focus:border-[var(--premium-emerald)]
                transition-all duration-200
                body-md
              "
            />
          </div>

          {/* Amount */}
          <div>
            <label className="body-sm text-[var(--premium-text-secondary)] mb-[8px] block">
              Amount
            </label>
            <input
              type="number"
              step="0.01"
              min="0"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              placeholder="0.00"
              className="
                w-full
                px-[var(--premium-space-md)]
                py-[var(--premium-space-sm)]
                bg-[var(--premium-surface-2)]
                text-[var(--premium-text-primary)]
                placeholder:text-[var(--premium-text-muted)]
                border border-[var(--premium-glass-border)]
                rounded-[var(--premium-radius-lg)]
                outline-none
                focus:border-[var(--premium-emerald)]
                transition-all duration-200
                body-md
              "
            />
          </div>

          {/* Save Button */}
          <button
            onClick={handleSave}
            disabled={!name.trim() || !amount || parseFloat(amount) <= 0}
            className={`
              w-full
              h-[48px]
              rounded-[var(--premium-radius-full)]
              font-medium text-[16px]
              transition-all duration-200
              ${
                name.trim() && amount && parseFloat(amount) > 0
                  ? 'bg-[var(--premium-emerald)] text-white hover:bg-[var(--premium-emerald-dark)] active:scale-95'
                  : 'bg-[var(--premium-surface-3)] text-[var(--premium-text-muted)] cursor-not-allowed'
              }
            `}
          >
            Save Changes
          </button>
        </div>
      </div>
    </div>
  );
}

/**
 * Props for ManageTemplatesScreen
 */
export interface ManageTemplatesScreenProps {
  /** Callback when user clicks back button */
  onBack: () => void;
}

/**
 * ManageTemplatesScreen Component
 */
export function ManageTemplatesScreen({ onBack }: ManageTemplatesScreenProps) {
  // Mock initial templates
  const [templates, setTemplates] = useState<TransactionTemplate[]>([
    {
      id: 'tmpl-1',
      name: 'Daily Lunch',
      amount: 12.50,
      type: 'expense',
      category: 'food-dining',
      subcategory: 'Restaurant',
      accountId: 'cash-wallet',
    },
    {
      id: 'tmpl-2',
      name: 'Rent Payment',
      amount: 1500.00,
      type: 'expense',
      category: 'bills-utilities',
      subcategory: 'Rent',
      accountId: 'bank-checking',
    },
    {
      id: 'tmpl-3',
      name: 'Salary',
      amount: 5000.00,
      type: 'income',
      category: 'income',
      accountId: 'bank-checking',
      notes: 'Monthly salary deposit',
    },
    {
      id: 'tmpl-4',
      name: 'Grocery Shopping',
      amount: 85.00,
      type: 'expense',
      category: 'food-dining',
      subcategory: 'Groceries',
      accountId: 'card-visa',
    },
    {
      id: 'tmpl-5',
      name: 'Gym Membership',
      amount: 45.00,
      type: 'expense',
      category: 'healthcare',
      subcategory: 'Fitness',
      accountId: 'bank-checking',
    },
    {
      id: 'tmpl-6',
      name: 'Savings Transfer',
      amount: 500.00,
      type: 'transfer',
      fromAccountId: 'bank-checking',
      toAccountId: 'bank-savings',
      notes: 'Monthly savings',
    },
  ]);

  const [editingTemplate, setEditingTemplate] = useState<TransactionTemplate | null>(null);

  const handleMove = useCallback((dragIndex: number, hoverIndex: number) => {
    setTemplates((prevTemplates) => {
      const newTemplates = [...prevTemplates];
      const draggedTemplate = newTemplates[dragIndex];
      newTemplates.splice(dragIndex, 1);
      newTemplates.splice(hoverIndex, 0, draggedTemplate);
      return newTemplates;
    });
  }, []);

  const handleEdit = (template: TransactionTemplate) => {
    setEditingTemplate(template);
  };

  const handleSaveEdit = (updatedTemplate: TransactionTemplate) => {
    setTemplates((prev) =>
      prev.map((t) => (t.id === updatedTemplate.id ? updatedTemplate : t))
    );
  };

  const handleDelete = (id: string) => {
    if (confirm('Are you sure you want to delete this template?')) {
      setTemplates((prev) => prev.filter((t) => t.id !== id));
    }
  };

  return (
    <DndProvider backend={HTML5Backend}>
      <div
        className="
          fixed inset-0 z-[100]
          bg-[var(--premium-bg-primary)]
          flex flex-col
        "
      >
        {/* Header */}
        <div
          className="
            px-[var(--premium-space-lg)]
            pt-[var(--premium-space-lg)]
            pb-[var(--premium-space-md)]
            border-b border-[var(--premium-glass-border)]
            bg-[var(--premium-bg-primary)]
          "
        >
          <div className="flex items-center gap-[var(--premium-space-md)]">
            <button
              onClick={onBack}
              className="
                w-[40px] h-[40px]
                rounded-full
                bg-[var(--premium-surface-2)]
                hover:bg-[var(--premium-surface-3)]
                flex items-center justify-center
                text-[var(--premium-text-secondary)]
                transition-all duration-200
                active:scale-95
              "
              aria-label="Back"
            >
              <ArrowLeft size={20} />
            </button>
            <div className="flex-1">
              <h1 className="heading-5 text-[var(--premium-text-primary)]">
                Manage Templates
              </h1>
              <p className="body-sm text-[var(--premium-text-tertiary)]">
                {templates.length} {templates.length === 1 ? 'template' : 'templates'}
              </p>
            </div>
          </div>
        </div>

        {/* Templates List */}
        <div className="flex-1 overflow-y-auto px-[var(--premium-space-lg)] py-[var(--premium-space-xl)]">
          <div className="max-w-2xl mx-auto space-y-[var(--premium-space-md)]">
            {templates.length === 0 ? (
              <div
                className="
                  flex flex-col items-center justify-center
                  py-[var(--premium-space-3xl)]
                  text-center
                "
              >
                <div
                  className="
                    w-[80px] h-[80px]
                    rounded-full
                    bg-[var(--premium-surface-2)]
                    flex items-center justify-center
                    mb-[var(--premium-space-lg)]
                  "
                >
                  <Bookmark size={36} className="text-[var(--premium-text-muted)]" />
                </div>
                <h3 className="heading-5 text-[var(--premium-text-primary)] mb-[8px]">
                  No Templates Yet
                </h3>
                <p className="body-md text-[var(--premium-text-tertiary)] max-w-sm">
                  Create transaction templates to quickly fill in recurring transactions
                </p>
              </div>
            ) : (
              <>
                {/* Hint */}
                <div
                  className="
                    p-[var(--premium-space-md)]
                    rounded-[var(--premium-radius-lg)]
                    bg-[var(--premium-emerald)]/10
                    border border-[var(--premium-emerald)]/20
                  "
                >
                  <p className="body-sm text-[var(--premium-text-secondary)]">
                    <span className="text-[var(--premium-emerald)] font-medium">
                      Tip:
                    </span>{' '}
                    Drag and drop to reorder your templates
                  </p>
                </div>

                {/* Templates */}
                {templates.map((template, index) => (
                  <DraggableTemplateRow
                    key={template.id}
                    template={template}
                    index={index}
                    onMove={handleMove}
                    onEdit={handleEdit}
                    onDelete={handleDelete}
                  />
                ))}
              </>
            )}
          </div>
        </div>

        {/* Edit Modal */}
        {editingTemplate && (
          <EditTemplateModal
            template={editingTemplate}
            onClose={() => setEditingTemplate(null)}
            onSave={handleSaveEdit}
          />
        )}
      </div>
    </DndProvider>
  );
}
