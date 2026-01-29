/**
 * Premium Add Transaction Screen - Enhanced Omni-Input
 * Supports Expense, Income, and Transfer with AI Voice
 * Material 3 design with progressive disclosure
 */

import { useState, useEffect } from "react";
import {
  X,
  Calculator,
  Mic,
  Camera,
  ChevronRight,
  ChevronDown,
  ArrowUpRight,
  ArrowDownLeft,
  ArrowLeftRight,
  Wallet,
  Building2,
  PiggyBank,
  CreditCard,
  Landmark,
  AlertCircle,
  Wand2,
  Check,
  Bookmark,
  Trash2,
} from "lucide-react";
import { PremiumCategoryPicker } from "../components/premium/PremiumCategoryPicker";
import { PremiumCategoryPickerEnhanced } from "../components/premium/PremiumCategoryPickerEnhanced";
import { PremiumCategorySelector } from "../components/premium/PremiumCategorySelector";
import { PremiumSubcategorySelector } from "../components/premium/PremiumSubcategorySelector";
import { PremiumAccountSelector } from "../components/premium/PremiumAccountSelector";
import { PremiumAccountBottomSheet } from "../components/premium/PremiumAccountBottomSheet";
import { PremiumTransactionTypeSelector } from "../components/premium/PremiumTransactionTypeSelector";
import { PremiumCalculatorKeypad } from "../components/premium/PremiumCalculatorKeypad";
import {
  PremiumEnhancedVoiceInput,
  type VoiceParseResult,
} from "../components/premium/PremiumEnhancedVoiceInput";
import { PremiumScanInput } from "../components/premium/PremiumScanInput";
import { PremiumCalendar } from "../components/premium/PremiumCalendar";
import type {
  TransactionCategory,
  TransactionType,
} from "../../types/domain";
import { Calendar, FileText } from "lucide-react";
import {
  CATEGORY_METADATA,
  CATEGORY_SUBCATEGORIES,
} from "../../mocks/data";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "../components/ui/alert-dialog";

// Account Interface with Group Support
interface Account {
  id: string;
  name: string;
  type: string;
  balance: number;
  icon: any;
  color: string;
  group: string; // AccountGroup name
  groupIcon: any; // Icon for the group
}

// Account data with hierarchical group structure
const MOCK_ACCOUNTS: Account[] = [
  // Cash Group
  {
    id: "cash-wallet",
    name: "Cash Wallet",
    type: "cash",
    balance: 1250.0,
    icon: Wallet,
    color: "#10b981",
    group: "Cash",
    groupIcon: Wallet,
  },
  {
    id: "cash-petty",
    name: "Petty Cash",
    type: "cash",
    balance: 350.0,
    icon: Wallet,
    color: "#10b981",
    group: "Cash",
    groupIcon: Wallet,
  },
  // Bank Accounts Group
  {
    id: "bank-checking",
    name: "Checking",
    type: "bank",
    balance: 5430.5,
    icon: Building2,
    color: "#667eea",
    group: "Bank Accounts",
    groupIcon: Building2,
  },
  {
    id: "bank-savings",
    name: "Savings",
    type: "savings",
    balance: 12500.0,
    icon: PiggyBank,
    color: "#4facfe",
    group: "Bank Accounts",
    groupIcon: Building2,
  },
  {
    id: "bank-business",
    name: "Business Account",
    type: "bank",
    balance: 8750.0,
    icon: Building2,
    color: "#667eea",
    group: "Bank Accounts",
    groupIcon: Building2,
  },
  // Credit Cards Group
  {
    id: "card-visa",
    name: "Visa",
    type: "credit",
    balance: -850.0,
    icon: CreditCard,
    color: "#f5576c",
    group: "Credit Cards",
    groupIcon: CreditCard,
  },
  {
    id: "card-mastercard",
    name: "MasterCard",
    type: "credit",
    balance: -1200.0,
    icon: CreditCard,
    color: "#f5576c",
    group: "Credit Cards",
    groupIcon: CreditCard,
  },
  {
    id: "card-uzcard",
    name: "Uzcard",
    type: "credit",
    balance: -450.0,
    icon: CreditCard,
    color: "#f5576c",
    group: "Credit Cards",
    groupIcon: CreditCard,
  },
  {
    id: "card-humo",
    name: "Humo",
    type: "credit",
    balance: -320.0,
    icon: CreditCard,
    color: "#f5576c",
    group: "Credit Cards",
    groupIcon: CreditCard,
  },
  // Investment Group
  {
    id: "investment-stocks",
    name: "Stocks",
    type: "investment",
    balance: 8200.0,
    icon: Landmark,
    color: "#c471f5",
    group: "Investments",
    groupIcon: Landmark,
  },
  {
    id: "investment-crypto",
    name: "Crypto",
    type: "investment",
    balance: 3500.0,
    icon: Landmark,
    color: "#c471f5",
    group: "Investments",
    groupIcon: Landmark,
  },
];

// Helper: Group accounts by their group property
const groupAccountsByType = (accounts: Account[]): Record<string, Account[]> => {
  return accounts.reduce((grouped, account) => {
    const group = account.group;
    if (!grouped[group]) {
      grouped[group] = [];
    }
    grouped[group].push(account);
    return grouped;
  }, {} as Record<string, Account[]>);
};

// Helper: Get unique group names in order
const getUniqueGroups = (accounts: Account[]): string[] => {
  const groups = accounts.map(acc => acc.group);
  return Array.from(new Set(groups));
};

// Transaction Template Interface
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

// Mock Templates Data
const MOCK_TEMPLATES: TransactionTemplate[] = [
  {
    id: "tmpl-1",
    name: "Daily Lunch",
    amount: 12.50,
    type: "expense",
    category: "food-dining",
    subcategory: "Restaurant",
    accountId: "cash-wallet",
  },
  {
    id: "tmpl-2",
    name: "Rent Payment",
    amount: 1500.00,
    type: "expense",
    category: "bills-utilities",
    subcategory: "Rent",
    accountId: "bank-checking",
  },
  {
    id: "tmpl-3",
    name: "Salary",
    amount: 5000.00,
    type: "income",
    category: "income",
    accountId: "bank-checking",
    notes: "Monthly salary deposit",
  },
  {
    id: "tmpl-4",
    name: "Grocery Shopping",
    amount: 85.00,
    type: "expense",
    category: "food-dining",
    subcategory: "Groceries",
    accountId: "card-visa",
  },
  {
    id: "tmpl-5",
    name: "Gym Membership",
    amount: 45.00,
    type: "expense",
    category: "healthcare",
    subcategory: "Fitness",
    accountId: "bank-checking",
  },
  {
    id: "tmpl-6",
    name: "Savings Transfer",
    amount: 500.00,
    type: "transfer",
    fromAccountId: "bank-checking",
    toAccountId: "bank-savings",
    notes: "Monthly savings",
  },
];

// Transaction interface for edit mode
export interface EditableTransaction {
  id: string;
  amount: number;
  type: TransactionType;
  category?: TransactionCategory;
  subcategory?: string;
  accountId?: string;
  fromAccountId?: string;
  toAccountId?: string;
  date: Date;
  notes?: string;
}

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
  /** Optional callback to navigate to Manage Categories screen */
  onManageCategories?: () => void;
  /** Optional callback to navigate to Manage Templates screen */
  onManageTemplates?: () => void;
  /** Optional transaction to edit (enables Edit Mode) */
  editTransaction?: EditableTransaction;
  /** Optional callback for deleting transaction (Edit Mode only) */
  onDelete?: (transactionId: string) => void;
}

type InputMode = "manual" | "voice" | "scan";
// FlowState DEPRECATED - Now using smart state-driven navigation

export function PremiumAddTransactionScreen({
  onClose,
  onSave,
  onManageCategories,
  onManageTemplates,
  editTransaction,
  onDelete,
}: PremiumAddTransactionScreenProps) {
  // Determine if we're in edit mode
  const isEditMode = !!editTransaction;

  // Input Mode State
  const [inputMode, setInputMode] =
    useState<InputMode>("manual");
  
  // Smart Navigation State (replaces old flowState)
  const [showConfirmScreen, setShowConfirmScreen] = useState(isEditMode);

  // Delete confirmation dialog state
  const [showDeleteDialog, setShowDeleteDialog] = useState(false);

  // Manual Input State
  const [displayValue, setDisplayValue] = useState("0");
  const [calculationString, setCalculationString] =
    useState("");
  const [currentOperator, setCurrentOperator] = useState<
    string | null
  >(null);
  const [previousValue, setPreviousValue] = useState<
    number | null
  >(null);

  // Voice Input State
  const [isListening, setIsListening] = useState(false);

  // Scan Input State
  const [isScanning, setIsScanning] = useState(false);

  // Transaction State
  const [transactionType, setTransactionType] =
    useState<TransactionType>("expense");
  const [selectedCategory, setSelectedCategory] =
    useState<TransactionCategory>();
  const [selectedSubcategory, setSelectedSubcategory] =
    useState<string>();
  const [selectedAccountId, setSelectedAccountId] = useState<string>(); // For expense/income
  const [fromAccountId, setFromAccountId] = useState<string>();
  const [toAccountId, setToAccountId] = useState<string>();
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [notes, setNotes] = useState("");
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [showNotesInput, setShowNotesInput] = useState(false);
  const [categoryToolbarTitle, setCategoryToolbarTitle] =
    useState("Choose Category");

  // Template State
  const [showTemplates, setShowTemplates] = useState(false);
  const [templates, setTemplates] = useState<TransactionTemplate[]>(MOCK_TEMPLATES);
  const [saveAsTemplate, setSaveAsTemplate] = useState(false);
  const [showTemplateNameInput, setShowTemplateNameInput] = useState(false);
  const [templateName, setTemplateName] = useState("");

  // Account BottomSheet State
  const [showAccountBottomSheet, setShowAccountBottomSheet] = useState(false);
  
  // Interactive Chip States
  const [showTransactionTypeSelector, setShowTransactionTypeSelector] = useState(false);
  const [showCategoryPicker, setShowCategoryPicker] = useState(false);
  const [showCategorySelector, setShowCategorySelector] = useState(false);
  const [showSubcategorySelector, setShowSubcategorySelector] = useState(false);
  const [pendingCategory, setPendingCategory] = useState<TransactionCategory>();

  // Pre-fill form in Edit Mode
  useEffect(() => {
    if (isEditMode && editTransaction) {
      setDisplayValue(editTransaction.amount.toString());
      setTransactionType(editTransaction.type);
      
      if (editTransaction.type === "transfer") {
        setFromAccountId(editTransaction.fromAccountId);
        setToAccountId(editTransaction.toAccountId);
      } else {
        setSelectedCategory(editTransaction.category);
        setSelectedSubcategory(editTransaction.subcategory);
        setSelectedAccountId(editTransaction.accountId);
      }
      
      setSelectedDate(editTransaction.date);
      if (editTransaction.notes) {
        setNotes(editTransaction.notes);
        setShowNotesInput(true);
      }
    }
  }, [isEditMode, editTransaction]);

  // Calculator Logic
  const handleNumberClick = (num: string) => {
    if (displayValue === "0") {
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
    setDisplayValue("0");
    setCalculationString(`${displayValue} ${operator}`);
  };

  const handleEquals = () => {
    if (currentOperator && previousValue !== null) {
      const current = parseFloat(displayValue);
      let result = 0;

      switch (currentOperator) {
        case "+":
          result = previousValue + current;
          break;
        case "-":
          result = previousValue - current;
          break;
        case "*":
          result = previousValue * current;
          break;
        case "/":
          result = current !== 0 ? previousValue / current : 0;
          break;
      }

      setDisplayValue(result.toFixed(2).replace(/\.?0+$/, ""));
      setCalculationString("");
      setCurrentOperator(null);
      setPreviousValue(null);
    }
  };

  const handleDelete = () => {
    if (displayValue.length === 1) {
      setDisplayValue("0");
    } else {
      setDisplayValue(displayValue.slice(0, -1));
    }
  };

  const handleDecimal = () => {
    if (
      !displayValue.includes(".") &&
      displayValue.length < 10
    ) {
      setDisplayValue(displayValue + ".");
    }
  };

  const handleClear = () => {
    setDisplayValue("0");
    setCalculationString("");
    setCurrentOperator(null);
    setPreviousValue(null);
  };

  const handleNextToType = () => {
    // DEPRECATED: Now using handleNextStep() with smart navigation
    if (parseFloat(displayValue) > 0) {
      // No longer needed - keeping for backwards compatibility
    }
  };

  const handleSelectType = (type: TransactionType) => {
    // DEPRECATED: Now using PremiumTransactionTypeSelector overlay
    setTransactionType(type);
    // No longer needed - keeping for backwards compatibility
  };

  const handleSelectCategory = (
    category: TransactionCategory,
    subcategory?: string,
  ) => {
    setSelectedCategory(category);
    setSelectedSubcategory(subcategory);
    // Don't advance to confirm yet - need account selection first
  };

  const handleTransferAccountsSet = () => {
    if (fromAccountId && toAccountId) {
      setShowConfirmScreen(true);
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
      if (
        result.type === "transfer" &&
        result.fromAccount &&
        result.toAccount
      ) {
        setFromAccountId(result.fromAccount);
        setToAccountId(result.toAccount);
        setShowConfirmScreen(true);
      } else if (
        (result.type === "expense" ||
          result.type === "income") &&
        result.category
      ) {
        setSelectedCategory(
          result.category as TransactionCategory,
        );
        setShowConfirmScreen(true);
      } else {
        // Partial data - stay on amount screen, user can fill missing fields
      }
    }

    // Set notes/description
    if (result.description) {
      setNotes(result.description);
    }

    // Switch back to manual mode
    setInputMode("manual");
  };

  // Scan Input Handlers
  const handleStartScan = () => {
    setIsScanning(true);
  };

  const handleStopScan = () => {
    setIsScanning(false);
  };

  const handleUploadImage = (file: File) => {
    console.log("Processing image:", file.name);
    setTimeout(() => {
      setDisplayValue("32.50");
      setInputMode("manual");
      setIsScanning(false);
    }, 1500);
  };

  // Template Selection Handler
  const handleSelectTemplate = (template: TransactionTemplate) => {
    // Auto-fill all fields from template
    setDisplayValue(template.amount.toString());
    setTransactionType(template.type);
    
    if (template.type === "transfer") {
      setFromAccountId(template.fromAccountId);
      setToAccountId(template.toAccountId);
    } else {
      setSelectedCategory(template.category);
      setSelectedSubcategory(template.subcategory);
      setSelectedAccountId(template.accountId);
    }
    
    if (template.notes) {
      setNotes(template.notes);
      setShowNotesInput(true);
    }
    
    // Hide templates - user can then use Next button
    setShowTemplates(false);
  };

  const handleDeleteTransaction = () => {
    if (isEditMode && editTransaction && onDelete) {
      onDelete(editTransaction.id);
      onClose();
    }
  };

  const handleSaveTransaction = () => {
    const amount = parseFloat(displayValue);
    if (amount <= 0) return;

    // If save as template is enabled, show template name input (not in edit mode)
    if (!isEditMode && saveAsTemplate && !showTemplateNameInput) {
      setShowTemplateNameInput(true);
      return;
    }

    // Save the template if needed (only in add mode)
    if (!isEditMode && saveAsTemplate && templateName.trim()) {
      const newTemplate: TransactionTemplate = {
        id: `tmpl-${Date.now()}`,
        name: templateName.trim(),
        amount,
        type: transactionType,
        notes: notes || undefined,
      };

      if (transactionType === "transfer") {
        newTemplate.fromAccountId = fromAccountId;
        newTemplate.toAccountId = toAccountId;
      } else {
        newTemplate.category = selectedCategory;
        newTemplate.subcategory = selectedSubcategory;
        newTemplate.accountId = selectedAccountId;
      }

      setTemplates([...templates, newTemplate]);
    }

    // Save the transaction
    if (transactionType === "transfer") {
      if (!fromAccountId || !toAccountId || fromAccountId === toAccountId) return;
      onSave({
        amount,
        type: transactionType,
        fromAccountId,
        toAccountId,
        date: selectedDate,
        notes: notes || undefined,
      });
    } else {
      // Expense/Income: require category AND account
      if (!selectedCategory || !selectedAccountId) return;
      onSave({
        amount,
        type: transactionType,
        category: selectedCategory,
        fromAccountId: selectedAccountId, // Use the selected account
        date: selectedDate,
        notes: notes || undefined,
      });
    }
    onClose();
  };

  const handleModeChange = (mode: InputMode) => {
    setInputMode(mode);
    if (mode === "voice") {
      setIsListening(false);
    } else if (mode === "scan") {
      setIsScanning(false);
    }
  };

  // Interactive Chip Handlers
  const handleTransactionTypeClick = () => {
    setShowTransactionTypeSelector(true);
  };

  const handleCategoryChipClick = () => {
    setShowCategorySelector(true);
  };

  const handleAccountChipClick = () => {
    setShowAccountBottomSheet(true);
  };

  // Smart Navigation System - State-Driven Routing
  const handleNextStep = () => {
    const hasAmount = parseFloat(displayValue) > 0;
    
    if (!hasAmount) {
      return; // Button should be disabled, but double-check
    }

    // For Transfer type: check transfer-specific fields
    if (transactionType === "transfer") {
      if (!fromAccountId) {
        // Need From Account - would open transfer flow
        // For now, transfer uses the details screen
        setShowConfirmScreen(true);
        return;
      }
      if (!toAccountId) {
        // Need To Account
        setShowConfirmScreen(true);
        return;
      }
      // Everything filled - go to confirm
      setShowConfirmScreen(true);
      return;
    }

    // For Expense/Income: check category and account
    if (!selectedCategory) {
      // Missing category - open category selector
      setShowCategorySelector(true);
      return;
    }

    if (!selectedAccountId) {
      // Missing account - open account selector
      setShowAccountBottomSheet(true);
      return;
    }

    // Everything filled - go to confirm screen
    setShowConfirmScreen(true);
  };

  // Auto-progression after category selection
  const handleCategorySelected = (category: TransactionCategory, subcategory?: string) => {
    setSelectedCategory(category);
    setSelectedSubcategory(subcategory);
    
    // Smart auto-progression: check what's missing next
    if (!selectedAccountId) {
      // Account is missing - open account selector immediately
      setTimeout(() => {
        setShowAccountBottomSheet(true);
      }, 300); // Small delay for smooth transition
    } else {
      // Everything is filled - go to confirm
      setTimeout(() => {
        setShowConfirmScreen(true);
      }, 300);
    }
  };

  // Auto-progression after account selection
  const handleAccountSelected = (accountId: string) => {
    setSelectedAccountId(accountId);
    
    // Smart auto-progression: check if everything is complete
    const hasAmount = parseFloat(displayValue) > 0;
    if (hasAmount && selectedCategory) {
      // Everything filled - go to confirm screen
      setTimeout(() => {
        setShowConfirmScreen(true);
      }, 300);
    }
  };

  // Check if all required fields are filled
  const isFormValid = () => {
    const hasAmount = parseFloat(displayValue) > 0;
    if (transactionType === "transfer") {
      return hasAmount && fromAccountId && toAccountId && fromAccountId !== toAccountId;
    }
    // For expense/income: require amount, category, AND account
    return hasAmount && selectedCategory && selectedAccountId;
  };

  // Check if Next button should be enabled
  const isNextButtonEnabled = () => {
    return parseFloat(displayValue) > 0;
  };

  const getTypeColor = (type: TransactionType) => {
    switch (type) {
      case "expense":
        return {
          bg: "bg-[#f5576c]/10",
          text: "text-[#f5576c]",
          border: "border-[#f5576c]",
          icon: ArrowUpRight,
        };
      case "income":
        return {
          bg: "bg-[#4facfe]/10",
          text: "text-[#4facfe]",
          border: "border-[#4facfe]",
          icon: ArrowDownLeft,
        };
      case "transfer":
        return {
          bg: "bg-[var(--premium-emerald)]/10",
          text: "text-[var(--premium-emerald)]",
          border: "border-[var(--premium-emerald)]",
          icon: ArrowLeftRight,
        };
    }
  };

  return (
    <div
      className="
      fixed inset-0 z-[100]
      bg-[var(--premium-bg-primary)]
      flex flex-col
    "
    >
      {/* Minimal Header */}
      <div
        className="
        px-[var(--premium-space-lg)]
        pt-[var(--premium-space-lg)]
        pb-[var(--premium-space-md)]
        flex items-center justify-between
      "
      >
        <div className="flex items-center gap-[8px]">
          {showConfirmScreen && (
            <button
              onClick={() => setShowConfirmScreen(false)}
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
              <ChevronRight size={20} className="rotate-180" />
            </button>
          )}
          <h1 className="body-md font-medium text-[var(--premium-text-secondary)]">
            {!showConfirmScreen && (isEditMode ? "Edit Transaction" : "New Transaction")}
            {showConfirmScreen && (isEditMode ? "Update Transaction" : "Confirm & Save")}
          </h1>
        </div>
        <div className="flex items-center gap-[8px]">
          {/* Templates Button - Only show on amount screen */}
          {!showConfirmScreen && (
            <button
              onClick={() => setShowTemplates(!showTemplates)}
              aria-label="Templates"
              className={`
                w-[32px] h-[32px]
                rounded-full
                flex items-center justify-center
                transition-all duration-200
                ${
                  showTemplates
                    ? "bg-[var(--premium-emerald)] text-white"
                    : "bg-[var(--premium-surface-2)] text-[var(--premium-text-secondary)] hover:bg-[var(--premium-surface-3)]"
                }
                active:scale-95
              `}
            >
              <Wand2 size={18} />
            </button>
          )}
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
      </div>

      {/* Template Carousel - Horizontal Scroll */}
      {!showConfirmScreen && showTemplates && (
        <div
          className="
            px-[var(--premium-space-lg)]
            pb-[var(--premium-space-md)]
            border-b border-[var(--premium-glass-border)]
            bg-[var(--premium-bg-primary)]
            animate-[slideDown_0.3s_ease-out]
          "
        >
          <div className="flex items-center justify-between mb-[var(--premium-space-sm)]">
            <p className="body-sm text-[var(--premium-text-secondary)] font-medium">
              Quick Templates
            </p>
            {onManageTemplates && (
              <button
                onClick={onManageTemplates}
                className="
                  body-xs text-[var(--premium-emerald)]
                  hover:text-[var(--premium-emerald-dark)]
                  transition-colors duration-200
                  font-medium
                "
              >
                Manage
              </button>
            )}
          </div>
          
          <div className="overflow-x-auto -mx-[var(--premium-space-lg)] px-[var(--premium-space-lg)] pb-[8px] scrollbar-hide">
            <div className="flex gap-[var(--premium-space-md)] min-w-min">
              {templates.map((template) => {
                const typeColor = getTypeColor(template.type);
                const Icon = typeColor.icon;
                const account = template.accountId 
                  ? MOCK_ACCOUNTS.find(acc => acc.id === template.accountId)
                  : null;
                
                return (
                  <button
                    key={template.id}
                    onClick={() => handleSelectTemplate(template)}
                    className="
                      flex-shrink-0
                      w-[200px]
                      p-[var(--premium-space-md)]
                      rounded-[var(--premium-radius-xl)]
                      bg-[var(--premium-surface-2)]
                      hover:bg-[var(--premium-surface-3)]
                      border border-[var(--premium-glass-border)]
                      transition-all duration-200
                      active:scale-95
                      text-left
                    "
                  >
                    {/* Template Name */}
                    <div className="flex items-center gap-[8px] mb-[var(--premium-space-sm)]">
                      <div
                        className={`
                          w-[24px] h-[24px]
                          rounded-[6px]
                          ${typeColor.bg}
                          flex items-center justify-center
                        `}
                      >
                        <Icon size={14} className={typeColor.text} />
                      </div>
                      <p className="body-sm font-medium text-[var(--premium-text-primary)] truncate flex-1">
                        {template.name}
                      </p>
                    </div>
                    
                    {/* Amount */}
                    <p className="heading-5 text-[var(--premium-text-primary)] mb-[4px]">
                      ${template.amount.toFixed(2)}
                    </p>
                    
                    {/* Category/Account Info */}
                    <div className="space-y-[2px]">
                      {template.category && (
                        <p className="body-xs text-[var(--premium-text-tertiary)] capitalize truncate">
                          {template.category.replace("-", " ")}
                          {template.subcategory && ` • ${template.subcategory}`}
                        </p>
                      )}
                      {account && (
                        <p className="body-xs text-[var(--premium-text-secondary)] truncate">
                          {account.name}
                        </p>
                      )}
                      {template.type === "transfer" && template.fromAccountId && template.toAccountId && (
                        <p className="body-xs text-[var(--premium-text-tertiary)] truncate">
                          {MOCK_ACCOUNTS.find(a => a.id === template.fromAccountId)?.name} → {MOCK_ACCOUNTS.find(a => a.id === template.toAccountId)?.name}
                        </p>
                      )}
                    </div>
                  </button>
                );
              })}
            </div>
          </div>
        </div>
      )}

      {/* Main Content Area */}
      <div className="flex-1 flex flex-col overflow-hidden">
        {/* AMOUNT INPUT STATE - Always shown unless on confirm screen */}
        {!showConfirmScreen && (
          <>
            {/* Amount Display - Top Section */}
            <div
              className="
              flex-1
              flex flex-col items-center justify-center
              px-[var(--premium-space-lg)]
              pb-[var(--premium-space-xl)]
            "
            >
              {/* Calculation String */}
              {calculationString && (
                <p className="body-sm text-[var(--premium-text-tertiary)] mb-[8px]">
                  {calculationString}
                </p>
              )}

              {/* Large Amount Display */}
              <div
                className="
                text-[64px] font-bold
                text-[var(--premium-text-primary)]
                leading-none
                mb-[var(--premium-space-md)]
                transition-all duration-200
              "
              >
                ${displayValue}
              </div>

              {/* Interactive Chips - Transaction Type, Category & Account */}
              <div className="flex flex-wrap items-center justify-center gap-[8px] mb-[var(--premium-space-lg)]">
                {/* Transaction Type Chip - Always visible, interactive */}
                {transactionType && (
                  <button
                    onClick={handleTransactionTypeClick}
                    className={`
                      group
                      px-[12px] py-[6px]
                      rounded-[var(--premium-radius-full)]
                      flex items-center gap-[6px]
                      ${getTypeColor(transactionType).bg}
                      border border-${getTypeColor(transactionType).border.replace('border-', '')}
                      hover:shadow-[0_0_0_4px_rgba(16,185,129,0.1)]
                      active:scale-95
                      transition-all duration-200
                      cursor-pointer
                    `}
                  >
                    {(() => {
                      const Icon = getTypeColor(transactionType).icon;
                      return (
                        <Icon
                          size={14}
                          className={getTypeColor(transactionType).text}
                        />
                      );
                    })()}
                    <span className={`body-xs font-medium ${getTypeColor(transactionType).text}`}>
                      {transactionType === "expense" ? "Expense" : transactionType === "income" ? "Income" : "Transfer"}
                    </span>
                    <ChevronDown 
                      size={12} 
                      className={`${getTypeColor(transactionType).text} opacity-60 group-hover:opacity-100 transition-opacity`}
                    />
                  </button>
                )}

                {/* Category Chip - Interactive when selected, placeholder when not */}
                {transactionType !== "transfer" && (
                  selectedCategory ? (
                    <button
                      onClick={handleCategoryChipClick}
                      className="
                        group
                        px-[12px] py-[6px]
                        rounded-[var(--premium-radius-full)]
                        bg-[var(--premium-emerald)]/15
                        border border-[var(--premium-emerald)]/30
                        flex items-center gap-[6px]
                        hover:bg-[var(--premium-emerald)]/25
                        hover:border-[var(--premium-emerald)]/50
                        hover:shadow-[0_0_0_4px_rgba(16,185,129,0.1)]
                        active:scale-95
                        transition-all duration-200
                        cursor-pointer
                        animate-[fadeIn_0.2s_ease-out]
                      "
                    >
                      <span className="body-xs font-medium text-[var(--premium-emerald)] capitalize">
                        {selectedCategory}
                      </span>
                      {selectedSubcategory && (
                        <>
                          <span className="text-[var(--premium-emerald)]/50">•</span>
                          <span className="body-xs text-[var(--premium-emerald)]/80">
                            {selectedSubcategory}
                          </span>
                        </>
                      )}
                      <ChevronDown 
                        size={12} 
                        className="text-[var(--premium-emerald)] opacity-60 group-hover:opacity-100 transition-opacity"
                      />
                    </button>
                  ) : (
                    <button
                      onClick={handleCategoryChipClick}
                      className="
                        group
                        px-[12px] py-[6px]
                        rounded-[var(--premium-radius-full)]
                        bg-[var(--premium-surface-2)]
                        border-2 border-dashed border-[var(--premium-border)]
                        flex items-center gap-[6px]
                        hover:bg-[var(--premium-emerald)]/10
                        hover:border-[var(--premium-emerald)]/50
                        active:scale-95
                        transition-all duration-200
                        cursor-pointer
                      "
                    >
                      <span className="body-xs font-medium text-[var(--premium-text-tertiary)] group-hover:text-[var(--premium-emerald)]">
                        + Category
                      </span>
                    </button>
                  )
                )}

                {/* Account Chip - Interactive when selected, placeholder when not */}
                {selectedAccountId ? (() => {
                  const account = MOCK_ACCOUNTS.find(acc => acc.id === selectedAccountId);
                  if (!account) return null;
                  const Icon = account.icon;
                  
                  return (
                    <button
                      onClick={handleAccountChipClick}
                      className="
                        group
                        px-[12px] py-[6px]
                        rounded-[var(--premium-radius-full)]
                        bg-[var(--premium-surface-3)]
                        border border-[var(--premium-glass-border)]
                        flex items-center gap-[6px]
                        hover:bg-[var(--premium-surface-4)]
                        hover:border-[var(--premium-emerald)]/30
                        hover:shadow-[0_0_0_4px_rgba(16,185,129,0.1)]
                        active:scale-95
                        transition-all duration-200
                        cursor-pointer
                        animate-[fadeIn_0.2s_ease-out]
                      "
                    >
                      <div 
                        className="w-[16px] h-[16px] rounded-[4px] flex items-center justify-center"
                        style={{ backgroundColor: `${account.color}20` }}
                      >
                        <Icon size={10} style={{ color: account.color }} />
                      </div>
                      <span className="body-xs font-medium text-[var(--premium-text-primary)]">
                        {account.name}
                      </span>
                      <ChevronDown 
                        size={12} 
                        className="text-[var(--premium-text-tertiary)] opacity-60 group-hover:opacity-100 transition-opacity"
                      />
                    </button>
                  );
                })() : (
                  <button
                    onClick={handleAccountChipClick}
                    className="
                      group
                      px-[12px] py-[6px]
                      rounded-[var(--premium-radius-full)]
                      bg-[var(--premium-surface-2)]
                      border-2 border-dashed border-[var(--premium-border)]
                      flex items-center gap-[6px]
                      hover:bg-[var(--premium-emerald)]/10
                      hover:border-[var(--premium-emerald)]/50
                      active:scale-95
                      transition-all duration-200
                      cursor-pointer
                    "
                  >
                    <span className="body-xs font-medium text-[var(--premium-text-tertiary)] group-hover:text-[var(--premium-emerald)]">
                      + Account
                    </span>
                  </button>
                )}
              </div>

              {/* Input Mode Switcher */}
              <div
                className="
                flex items-center gap-[var(--premium-space-lg)]
                p-[var(--premium-space-sm)]
                bg-[var(--premium-surface-1)]
                rounded-[var(--premium-radius-full)]
              "
              >
                <button
                  onClick={() => handleModeChange("manual")}
                  className={`
                    w-[48px] h-[48px]
                    rounded-full
                    flex items-center justify-center
                    transition-all duration-200
                    ${
                      inputMode === "manual"
                        ? "bg-[var(--premium-surface-3)] text-[var(--premium-text-primary)] shadow-[var(--premium-shadow-sm)]"
                        : "text-[var(--premium-text-tertiary)] hover:text-[var(--premium-text-secondary)]"
                    }
                  `}
                  aria-label="Manual Input"
                >
                  <Calculator size={22} strokeWidth={2} />
                </button>

                <button
                  onClick={() => handleModeChange("voice")}
                  className={`
                    w-[48px] h-[48px]
                    rounded-full
                    flex items-center justify-center
                    transition-all duration-200
                    ${
                      inputMode === "voice"
                        ? "bg-[var(--premium-emerald)] text-white shadow-[var(--premium-shadow-md)]"
                        : "text-[var(--premium-text-tertiary)] hover:text-[var(--premium-text-secondary)]"
                    }
                  `}
                  aria-label="Voice Input"
                >
                  <Mic size={22} strokeWidth={2} />
                </button>

                <button
                  onClick={() => handleModeChange("scan")}
                  className={`
                    w-[48px] h-[48px]
                    rounded-full
                    flex items-center justify-center
                    transition-all duration-200
                    ${
                      inputMode === "scan"
                        ? "bg-[var(--premium-surface-3)] text-[var(--premium-text-primary)] shadow-[var(--premium-shadow-sm)]"
                        : "text-[var(--premium-text-tertiary)] hover:text-[var(--premium-text-secondary)]"
                    }
                  `}
                  aria-label="Scan Receipt"
                >
                  <Camera size={22} strokeWidth={2} />
                </button>
              </div>
            </div>

            {/* Input Method Content - Bottom Section with Material 3 Elevation */}
            <div
              className="
              bg-[var(--premium-bg-secondary)]
              border-t border-[var(--premium-glass-border)]
              shadow-[var(--premium-shadow-lg)]
              rounded-t-[var(--premium-radius-2xl)]
              overflow-hidden
            "
            >
              {inputMode === "manual" && (
                <div className="p-[var(--premium-space-lg)]">
                  <PremiumCalculatorKeypad
                    onNumberClick={handleNumberClick}
                    onOperatorClick={handleOperatorClick}
                    onDelete={handleDelete}
                    onDecimal={handleDecimal}
                    onEquals={handleEquals}
                    onClear={handleClear}
                  />

                  {/* Smart Next Button - State-Driven Navigation */}
                  <button
                    onClick={handleNextStep}
                    disabled={!isNextButtonEnabled()}
                    className={`
                      w-full
                      h-[56px]
                      mt-[var(--premium-space-md)]
                      rounded-[var(--premium-radius-full)]
                      font-medium text-[18px]
                      flex items-center justify-center gap-[8px]
                      transition-all duration-200
                      shadow-[0_4px_16px_rgba(16,185,129,0.25)]
                      ${
                        isNextButtonEnabled()
                          ? "bg-[var(--premium-emerald)] text-white hover:bg-[var(--premium-emerald-dark)] active:scale-95"
                          : "bg-[var(--premium-surface-3)] text-[var(--premium-text-muted)] cursor-not-allowed shadow-none"
                      }
                    `}
                  >
                    {isFormValid() ? "Review & Save" : "Next"}
                    <ChevronRight size={20} />
                  </button>
                </div>
              )}

              {inputMode === "voice" && (
                <div className="py-[var(--premium-space-xl)]">
                  <PremiumEnhancedVoiceInput
                    isListening={isListening}
                    onStartListening={handleStartListening}
                    onStopListening={handleStopListening}
                    onVoiceParsed={handleVoiceParsed}
                  />
                </div>
              )}

              {inputMode === "scan" && (
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

        {/* TYPE SELECTION STATE - DEPRECATED: Now using PremiumTransactionTypeSelector overlay */}
        {false && (
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
                onClick={() => handleSelectType("expense")}
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
                <div
                  className="
                  w-[56px] h-[56px]
                  rounded-[var(--premium-radius-lg)]
                  bg-[#f5576c]/10
                  flex items-center justify-center
                "
                >
                  <ArrowUpRight
                    size={28}
                    strokeWidth={2}
                    style={{ color: "#f5576c" }}
                  />
                </div>
                <div className="flex-1 text-left">
                  <h3 className="heading-5 text-[var(--premium-text-primary)] mb-[2px]">
                    Expense
                  </h3>
                  <p className="body-sm text-[var(--premium-text-tertiary)]">
                    Money spent on purchases
                  </p>
                </div>
                <ChevronRight
                  size={20}
                  className="text-[var(--premium-text-muted)]"
                />
              </button>

              {/* Income Option */}
              <button
                onClick={() => handleSelectType("income")}
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
                <div
                  className="
                  w-[56px] h-[56px]
                  rounded-[var(--premium-radius-lg)]
                  bg-[#4facfe]/10
                  flex items-center justify-center
                "
                >
                  <ArrowDownLeft
                    size={28}
                    strokeWidth={2}
                    style={{ color: "#4facfe" }}
                  />
                </div>
                <div className="flex-1 text-left">
                  <h3 className="heading-5 text-[var(--premium-text-primary)] mb-[2px]">
                    Income
                  </h3>
                  <p className="body-sm text-[var(--premium-text-tertiary)]">
                    Money received
                  </p>
                </div>
                <ChevronRight
                  size={20}
                  className="text-[var(--premium-text-muted)]"
                />
              </button>

              {/* Transfer Option */}
              <button
                onClick={() => handleSelectType("transfer")}
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
                <div
                  className="
                  w-[56px] h-[56px]
                  rounded-[var(--premium-radius-lg)]
                  bg-[var(--premium-emerald)]/10
                  flex items-center justify-center
                "
                >
                  <ArrowLeftRight
                    size={28}
                    strokeWidth={2}
                    style={{ color: "var(--premium-emerald)" }}
                  />
                </div>
                <div className="flex-1 text-left">
                  <h3 className="heading-5 text-[var(--premium-text-primary)] mb-[2px]">
                    Transfer
                  </h3>
                  <p className="body-sm text-[var(--premium-text-tertiary)]">
                    Move money between accounts
                  </p>
                </div>
                <ChevronRight
                  size={20}
                  className="text-[var(--premium-text-muted)]"
                />
              </button>
            </div>
          </div>
        )}

        {/* DETAILS STATE - DEPRECATED: Now using PremiumCategorySelector and PremiumAccountBottomSheet overlays */}
        {false && (
          <div className="flex-1 overflow-y-auto px-[var(--premium-space-lg)] py-[var(--premium-space-xl)] animate-[slideUp_0.3s_ease-out]">
            <div className="max-w-md mx-auto">
              {/* Amount & Type Summary */}
              <div className="text-center mb-[var(--premium-space-xl)]">
                <p className="body-sm text-[var(--premium-text-tertiary)] mb-[4px]">
                  {transactionType === "expense"
                    ? "Expense"
                    : transactionType === "income"
                      ? "Income"
                      : "Transfer"}
                </p>
                <p className="heading-2xl text-[var(--premium-text-primary)]">
                  ${displayValue}
                </p>
              </div>

              {transactionType === "transfer" ? (
                <div className="space-y-[var(--premium-space-xl)]">
                  {/* From Account - Grouped Vertical Sections */}
                  <div>
                    <p className="body-sm text-[var(--premium-text-tertiary)] mb-[var(--premium-space-lg)] px-[4px]">
                      From Account
                    </p>
                    
                    {/* Group By Account Type */}
                    <div className="space-y-[var(--premium-space-lg)]">
                      {(() => {
                        const grouped = groupAccountsByType(
                          MOCK_ACCOUNTS.filter(acc => acc.id !== toAccountId)
                        );
                        return Object.keys(grouped).map((groupName) => {
                          const accounts = grouped[groupName];
                          const GroupIcon = accounts[0].groupIcon;
                          
                          return (
                            <div key={groupName} className="space-y-[var(--premium-space-sm)]">
                              {/* Group Header */}
                              <div className="flex items-center gap-[8px] px-[4px]">
                                <GroupIcon 
                                  size={14} 
                                  className="text-[var(--premium-text-tertiary)]"
                                />
                                <p className="body-xs text-[var(--premium-text-tertiary)] uppercase tracking-wide">
                                  {groupName}
                                </p>
                              </div>
                              
                              {/* Horizontal Carousel for Group */}
                              <div className="overflow-x-auto -mx-[var(--premium-space-lg)] px-[var(--premium-space-lg)] pb-[8px] scrollbar-hide">
                                <div className="flex gap-[var(--premium-space-md)] min-w-min">
                                  {accounts.map((account) => {
                                    const Icon = account.icon;
                                    const isSelected = fromAccountId === account.id;
                                    
                                    return (
                                      <button
                                        key={account.id}
                                        onClick={() => setFromAccountId(account.id)}
                                        className={`
                                          flex-shrink-0
                                          w-[160px]
                                          p-[var(--premium-space-md)]
                                          rounded-[var(--premium-radius-xl)]
                                          transition-all duration-200
                                          ${
                                            isSelected
                                              ? "bg-[var(--premium-emerald)]/15 border-2 border-[var(--premium-emerald)] shadow-[0_0_0_4px_rgba(16,185,129,0.1)]"
                                              : "bg-[var(--premium-surface-2)] border-2 border-transparent hover:bg-[var(--premium-surface-3)] active:scale-95"
                                          }
                                        `}
                                      >
                                        {/* Icon */}
                                        <div 
                                          className={`
                                            w-[40px] h-[40px]
                                            rounded-[var(--premium-radius-lg)]
                                            flex items-center justify-center
                                            mb-[var(--premium-space-sm)]
                                            transition-all duration-200
                                          `}
                                          style={{ 
                                            backgroundColor: isSelected ? account.color : `${account.color}20` 
                                          }}
                                        >
                                          <Icon 
                                            size={20} 
                                            style={{ color: isSelected ? 'white' : account.color }}
                                          />
                                        </div>
                                        
                                        {/* Account Name */}
                                        <p
                                          className={`
                                            body-sm font-medium mb-[4px] text-left
                                            ${isSelected ? "text-[var(--premium-emerald)]" : "text-[var(--premium-text-primary)]"}
                                          `}
                                        >
                                          {account.name}
                                        </p>
                                        
                                        {/* Balance */}
                                        <p
                                          className={`
                                            body-xs text-left
                                            ${account.balance < 0 ? "text-[#f5576c]" : "text-[var(--premium-text-secondary)]"}
                                          `}
                                        >
                                          ${Math.abs(account.balance).toLocaleString()}
                                        </p>
                                      </button>
                                    );
                                  })}
                                </div>
                              </div>
                            </div>
                          );
                        });
                      })()}
                    </div>
                  </div>

                  {/* Transfer Indicator with Animation */}
                  {fromAccountId && (
                    <div className="flex justify-center -my-[8px] animate-[fadeIn_0.2s_ease-out]">
                      <div
                        className="
                          w-[48px] h-[48px]
                          rounded-full
                          bg-gradient-to-br from-[var(--premium-emerald)]/20 to-[var(--premium-emerald)]/10
                          border border-[var(--premium-emerald)]/30
                          flex items-center justify-center
                          text-[var(--premium-emerald)]
                          shadow-[0_0_12px_rgba(16,185,129,0.15)]
                        "
                      >
                        <ArrowDownLeft
                          size={22}
                          className="rotate-180"
                          strokeWidth={2.5}
                        />
                      </div>
                    </div>
                  )}

                  {/* To Account - Grouped Vertical Sections */}
                  <div>
                    <p className="body-sm text-[var(--premium-text-tertiary)] mb-[var(--premium-space-lg)] px-[4px]">
                      To Account
                    </p>
                    
                    {/* Group By Account Type */}
                    <div className="space-y-[var(--premium-space-lg)]">
                      {(() => {
                        const grouped = groupAccountsByType(
                          MOCK_ACCOUNTS.filter(acc => acc.id !== fromAccountId)
                        );
                        return Object.keys(grouped).map((groupName) => {
                          const accounts = grouped[groupName];
                          const GroupIcon = accounts[0].groupIcon;
                          
                          return (
                            <div key={groupName} className="space-y-[var(--premium-space-sm)]">
                              {/* Group Header */}
                              <div className="flex items-center gap-[8px] px-[4px]">
                                <GroupIcon 
                                  size={14} 
                                  className="text-[var(--premium-text-tertiary)]"
                                />
                                <p className="body-xs text-[var(--premium-text-tertiary)] uppercase tracking-wide">
                                  {groupName}
                                </p>
                              </div>
                              
                              {/* Horizontal Carousel for Group */}
                              <div className="overflow-x-auto -mx-[var(--premium-space-lg)] px-[var(--premium-space-lg)] pb-[8px] scrollbar-hide">
                                <div className="flex gap-[var(--premium-space-md)] min-w-min">
                                  {accounts.map((account) => {
                                    const Icon = account.icon;
                                    const isSelected = toAccountId === account.id;
                                    
                                    return (
                                      <button
                                        key={account.id}
                                        onClick={() => setToAccountId(account.id)}
                                        className={`
                                          flex-shrink-0
                                          w-[160px]
                                          p-[var(--premium-space-md)]
                                          rounded-[var(--premium-radius-xl)]
                                          transition-all duration-200
                                          ${
                                            isSelected
                                              ? "bg-[var(--premium-emerald)]/15 border-2 border-[var(--premium-emerald)] shadow-[0_0_0_4px_rgba(16,185,129,0.1)]"
                                              : "bg-[var(--premium-surface-2)] border-2 border-transparent hover:bg-[var(--premium-surface-3)] active:scale-95"
                                          }
                                        `}
                                      >
                                        {/* Icon */}
                                        <div 
                                          className={`
                                            w-[40px] h-[40px]
                                            rounded-[var(--premium-radius-lg)]
                                            flex items-center justify-center
                                            mb-[var(--premium-space-sm)]
                                            transition-all duration-200
                                          `}
                                          style={{ 
                                            backgroundColor: isSelected ? account.color : `${account.color}20` 
                                          }}
                                        >
                                          <Icon 
                                            size={20} 
                                            style={{ color: isSelected ? 'white' : account.color }}
                                          />
                                        </div>
                                        
                                        {/* Account Name */}
                                        <p
                                          className={`
                                            body-sm font-medium mb-[4px] text-left
                                            ${isSelected ? "text-[var(--premium-emerald)]" : "text-[var(--premium-text-primary)]"}
                                          `}
                                        >
                                          {account.name}
                                        </p>
                                        
                                        {/* Balance */}
                                        <p
                                          className={`
                                            body-xs text-left
                                            ${account.balance < 0 ? "text-[#f5576c]" : "text-[var(--premium-text-secondary)]"}
                                          `}
                                        >
                                          ${Math.abs(account.balance).toLocaleString()}
                                        </p>
                                      </button>
                                    );
                                  })}
                                </div>
                              </div>
                            </div>
                          );
                        });
                      })()}
                    </div>
                  </div>

                  {/* Same Account Warning */}
                  {fromAccountId && toAccountId && fromAccountId === toAccountId && (
                    <div 
                      className="
                        flex items-start gap-[var(--premium-space-sm)]
                        p-[var(--premium-space-md)]
                        rounded-[var(--premium-radius-lg)]
                        bg-[#f5576c]/10
                        border border-[#f5576c]/20
                        animate-[slideDown_0.2s_ease-out]
                      "
                    >
                      <AlertCircle size={18} className="text-[#f5576c] flex-shrink-0 mt-[2px]" />
                      <p className="body-sm text-[#f5576c]">
                        Cannot transfer to the same account. Please select a different account.
                      </p>
                    </div>
                  )}

                  {/* Continue Button */}
                  {fromAccountId && toAccountId && fromAccountId !== toAccountId && (
                    <button
                      onClick={handleTransferAccountsSet}
                      className="
                        w-full
                        h-[56px]
                        rounded-[var(--premium-radius-full)]
                        bg-[var(--premium-emerald)]
                        text-white
                        font-medium text-[18px]
                        flex items-center justify-center gap-[8px]
                        hover:bg-[var(--premium-emerald-dark)]
                        active:scale-95
                        transition-all duration-200
                        animate-[slideUp_0.2s_ease-out]
                        shadow-[0_4px_16px_rgba(16,185,129,0.25)]
                      "
                    >
                      Continue
                      <ChevronRight size={20} />
                    </button>
                  )}
                </div>
              ) : (
                <>
                  <p className="body-md text-[var(--premium-text-tertiary)] mb-[var(--premium-space-lg)] text-center">
                    Choose a category
                  </p>

                  {/* Text-Only Category Grid with Expandable Subcategories */}
                  <div className="space-y-[var(--premium-space-sm)]">
                    {CATEGORY_METADATA.filter(
                      (cat) => cat.id !== "income",
                    ).map((category) => {
                      const isSelected =
                        selectedCategory === category.id;
                      const hasSubcategories =
                        CATEGORY_SUBCATEGORIES[category.id]
                          ?.length > 0;

                      return (
                        <div
                          key={category.id}
                          className="space-y-[4px]"
                        >
                          {/* Main Category Chip */}
                          <button
                            onClick={() => {
                              if (
                                selectedCategory === category.id
                              ) {
                                // Deselect if clicking the same category
                                setSelectedCategory(undefined);
                                setSelectedSubcategory(
                                  undefined,
                                );
                              } else {
                                setSelectedCategory(
                                  category.id as TransactionCategory,
                                );
                                setSelectedSubcategory(
                                  undefined,
                                );
                              }
                            }}
                            className={`
                              w-full
                              px-[var(--premium-space-lg)]
                              py-[var(--premium-space-md)]
                              rounded-[var(--premium-radius-lg)]
                              font-medium
                              transition-all duration-200
                              text-left
                              ${
                                isSelected
                                  ? "bg-[var(--premium-emerald)]/15 border-2 border-[var(--premium-emerald)] text-[var(--premium-emerald)] shadow-[0_0_0_4px_rgba(16,185,129,0.1)]"
                                  : "bg-[var(--premium-surface-2)] border-2 border-transparent text-[var(--premium-text-primary)] hover:bg-[var(--premium-surface-3)] active:scale-[0.98]"
                              }
                            `}
                          >
                            {category.label}
                          </button>

                          {/* Subcategories - Expanded under selected category */}
                          {isSelected && hasSubcategories && (
                            <div
                              className="
                              pl-[var(--premium-space-md)]
                              space-y-[4px]
                              animate-[slideDown_0.2s_ease-out]
                            "
                            >
                              <div className="flex flex-wrap gap-[8px] pt-[4px]">
                                {CATEGORY_SUBCATEGORIES[
                                  category.id
                                ].map((subcategory) => {
                                  const isSubSelected =
                                    selectedSubcategory ===
                                    subcategory;

                                  return (
                                    <button
                                      key={subcategory}
                                      onClick={() => {
                                        setSelectedSubcategory(
                                          subcategory,
                                        );
                                        handleSelectCategory(
                                          category.id as TransactionCategory,
                                          subcategory,
                                        );
                                      }}
                                      className={`
                                        px-[var(--premium-space-md)]
                                        py-[8px]
                                        rounded-[var(--premium-radius-full)]
                                        body-sm
                                        font-medium
                                        transition-all duration-200
                                        ${
                                          isSubSelected
                                            ? "bg-[var(--premium-emerald)] text-white shadow-[var(--premium-shadow-sm)]"
                                            : "bg-[var(--premium-surface-3)] text-[var(--premium-text-secondary)] hover:bg-[var(--premium-surface-4)] active:scale-95"
                                        }
                                      `}
                                    >
                                      {subcategory}
                                    </button>
                                  );
                                })}
                              </div>
                            </div>
                          )}
                        </div>
                      );
                    })}
                  </div>

                  {/* Account Field Card - Show when category is selected */}
                  {selectedCategory && (
                    <div className="mt-[var(--premium-space-xl)] space-y-[var(--premium-space-lg)] animate-[slideDown_0.3s_ease-out]">
                      <div className="h-[1px] bg-[var(--premium-glass-border)]" />
                      
                      {/* Account Field Card */}
                      <button
                        onClick={() => setShowAccountBottomSheet(true)}
                        className={`
                          w-full
                          p-[var(--premium-space-lg)]
                          rounded-[var(--premium-radius-xl)]
                          transition-all duration-200
                          flex items-center justify-between
                          ${
                            selectedAccountId
                              ? "bg-[var(--premium-emerald)]/15 border-2 border-[var(--premium-emerald)] shadow-[0_0_0_4px_rgba(16,185,129,0.1)]"
                              : "bg-[var(--premium-surface-2)] border-2 border-dashed border-[var(--premium-glass-border)] hover:border-[var(--premium-emerald)]/30 hover:bg-[var(--premium-surface-3)]"
                          }
                          active:scale-[0.98]
                        `}
                      >
                        <div className="flex items-center gap-[var(--premium-space-md)] flex-1">
                          {selectedAccountId ? (
                            // Selected Account Display
                            (() => {
                              const account = MOCK_ACCOUNTS.find(acc => acc.id === selectedAccountId);
                              if (!account) return null;
                              const Icon = account.icon;
                              
                              return (
                                <>
                                  <div
                                    className="
                                      w-[48px] h-[48px]
                                      rounded-[var(--premium-radius-lg)]
                                      flex items-center justify-center
                                      flex-shrink-0
                                    "
                                    style={{ backgroundColor: account.color }}
                                  >
                                    <Icon size={24} style={{ color: 'white' }} />
                                  </div>
                                  <div className="flex-1 text-left">
                                    <p className="body-md font-medium text-[var(--premium-emerald)] mb-[2px]">
                                      {account.name}
                                    </p>
                                    <p className={`
                                      body-sm
                                      ${account.balance < 0 ? 'text-[#f5576c]' : 'text-[var(--premium-text-secondary)]'}
                                    `}>
                                      ${Math.abs(account.balance).toLocaleString('en-US', {
                                        minimumFractionDigits: 2,
                                        maximumFractionDigits: 2,
                                      })}
                                    </p>
                                  </div>
                                </>
                              );
                            })()
                          ) : (
                            // Placeholder
                            <>
                              <div
                                className="
                                  w-[48px] h-[48px]
                                  rounded-[var(--premium-radius-lg)]
                                  bg-[var(--premium-surface-3)]
                                  flex items-center justify-center
                                  flex-shrink-0
                                "
                              >
                                <Wallet size={24} className="text-[var(--premium-text-muted)]" />
                              </div>
                              <div className="flex-1 text-left">
                                <p className="body-md font-medium text-[var(--premium-text-primary)] mb-[2px]">
                                  Select Account
                                </p>
                                <p className="body-sm text-[var(--premium-text-tertiary)]">
                                  Tap to choose from your accounts
                                </p>
                              </div>
                            </>
                          )}
                        </div>
                        <ChevronRight 
                          size={20} 
                          className={selectedAccountId ? "text-[var(--premium-emerald)]" : "text-[var(--premium-text-muted)]"}
                        />
                      </button>

                      {/* Continue Button - Only show when account is selected */}
                      {selectedAccountId && (
                        <button
                          onClick={() => setShowConfirmScreen(true)}
                          className="
                            w-full
                            h-[56px]
                            rounded-[var(--premium-radius-full)]
                            bg-[var(--premium-emerald)]
                            text-white
                            font-medium text-[18px]
                            flex items-center justify-center gap-[8px]
                            hover:bg-[var(--premium-emerald-dark)]
                            active:scale-95
                            transition-all duration-200
                            animate-[slideUp_0.2s_ease-out]
                            shadow-[0_4px_16px_rgba(16,185,129,0.25)]
                          "
                        >
                          Continue
                          <ChevronRight size={20} />
                        </button>
                      )}
                    </div>
                  )}
                </>
              )}
            </div>
          </div>
        )}

        {/* CONFIRM & SAVE SCREEN */}
        {showConfirmScreen && (
          <div className="flex-1 overflow-y-auto px-[var(--premium-space-lg)] py-[var(--premium-space-xl)]">
            <div className="max-w-md mx-auto space-y-[var(--premium-space-lg)]">
              {/* Summary Card */}
              <div
                className={`
                p-[var(--premium-space-xl)]
                rounded-[var(--premium-radius-xl)]
                ${getTypeColor(transactionType).bg}
                border-2
                ${getTypeColor(transactionType).border}
              `}
              >
                <div className="flex items-start justify-between mb-[var(--premium-space-md)]">
                  <div>
                    <p
                      className={`body-sm mb-[8px] ${getTypeColor(transactionType).text}`}
                    >
                      {transactionType === "expense"
                        ? "Expense"
                        : transactionType === "income"
                          ? "Income"
                          : "Transfer"}
                    </p>
                    <h2 className="heading-3xl text-[var(--premium-text-primary)]">
                      ${displayValue}
                    </h2>
                  </div>
                  <div
                    className={`
                    w-[48px] h-[48px]
                    rounded-[var(--premium-radius-md)]
                    ${getTypeColor(transactionType).bg}
                    flex items-center justify-center
                  `}
                  >
                    {(() => {
                      const Icon =
                        getTypeColor(transactionType).icon;
                      return (
                        <Icon
                          size={24}
                          className={
                            getTypeColor(transactionType).text
                          }
                        />
                      );
                    })()}
                  </div>
                </div>

                {transactionType === "transfer" ? (
                  <div className="space-y-[8px]">
                    <p className="body-md text-[var(--premium-text-secondary)]">
                      From:{" "}
                      <span className="font-medium capitalize">
                        {fromAccountId?.replace("-", " ")}
                      </span>
                    </p>
                    <p className="body-md text-[var(--premium-text-secondary)]">
                      To:{" "}
                      <span className="font-medium capitalize">
                        {toAccountId?.replace("-", " ")}
                      </span>
                    </p>
                  </div>
                ) : (
                  <div className="space-y-[8px]">
                    <div>
                      <p className="body-xs text-[var(--premium-text-tertiary)] mb-[4px]">
                        Category
                      </p>
                      <p className="body-lg text-[var(--premium-text-secondary)] capitalize">
                        {selectedCategory}
                      </p>
                      {selectedSubcategory && (
                        <p className="body-sm text-[var(--premium-text-tertiary)] mt-[2px]">
                          {selectedSubcategory}
                        </p>
                      )}
                    </div>
                    {selectedAccountId && (
                      <div>
                        <p className="body-xs text-[var(--premium-text-tertiary)] mb-[4px]">
                          Account
                        </p>
                        <p className="body-md text-[var(--premium-text-secondary)] font-medium capitalize">
                          {MOCK_ACCOUNTS.find(acc => acc.id === selectedAccountId)?.name}
                        </p>
                      </div>
                    )}
                  </div>
                )}
              </div>

              {/* Date */}
              <button
                onClick={() =>
                  setShowDatePicker(!showDatePicker)
                }
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
                  <Calendar
                    size={20}
                    className="text-[var(--premium-text-tertiary)]"
                  />
                  <span className="body-md text-[var(--premium-text-primary)]">
                    {selectedDate.toLocaleDateString("en-US", {
                      month: "short",
                      day: "numeric",
                      year: "numeric",
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
                    <FileText
                      size={20}
                      className="text-[var(--premium-text-tertiary)]"
                    />
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

              {/* Save as Template Toggle */}
              {isFormValid() && (
                <button
                  onClick={() => setSaveAsTemplate(!saveAsTemplate)}
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
                    <Bookmark
                      size={20}
                      className="text-[var(--premium-text-tertiary)]"
                    />
                    <span className="body-md text-[var(--premium-text-primary)]">
                      Save as Template
                    </span>
                  </div>
                  <div
                    className={`
                      w-[24px] h-[24px]
                      rounded-[6px]
                      flex items-center justify-center
                      transition-all duration-200
                      ${
                        saveAsTemplate
                          ? "bg-[var(--premium-emerald)] scale-100"
                          : "bg-[var(--premium-surface-3)] scale-90"
                      }
                    `}
                  >
                    {saveAsTemplate && (
                      <Check size={16} className="text-white" />
                    )}
                  </div>
                </button>
              )}

              {/* Template Name Input - Show when save as template is enabled */}
              {saveAsTemplate && showTemplateNameInput && (
                <div
                  className="
                    p-[var(--premium-space-md)]
                    bg-[var(--premium-surface-2)]
                    rounded-[var(--premium-radius-lg)]
                    space-y-[var(--premium-space-md)]
                    animate-[slideDown_0.2s_ease-out]
                  "
                >
                  <p className="body-sm text-[var(--premium-text-secondary)]">
                    Template Name
                  </p>
                  <input
                    type="text"
                    value={templateName}
                    onChange={(e) => setTemplateName(e.target.value)}
                    placeholder="e.g., Daily Lunch, Monthly Rent..."
                    autoFocus
                    className="
                      w-full
                      px-[var(--premium-space-md)]
                      py-[var(--premium-space-sm)]
                      bg-[var(--premium-surface-3)]
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
              )}

              {/* Action Buttons - Different for Edit vs Add Mode */}
              {isFormValid() && (
                <>
                  {isEditMode ? (
                    /* Edit Mode: Show Update and Delete buttons */
                    <div className="space-y-[var(--premium-space-md)]">
                      {/* Update Transaction Button */}
                      <button
                        onClick={handleSaveTransaction}
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
                        Update Transaction
                      </button>

                      {/* Delete Transaction Button */}
                      <button
                        onClick={() => setShowDeleteDialog(true)}
                        className="
                          w-full
                          h-[56px]
                          rounded-[var(--premium-radius-full)]
                          bg-[var(--premium-surface-2)]
                          text-[#f5576c]
                          font-medium text-[18px]
                          border-2 border-[#f5576c]/30
                          hover:bg-[#f5576c]/10
                          active:scale-[0.98]
                          transition-all duration-200
                          flex items-center justify-center gap-[8px]
                        "
                      >
                        <Trash2 size={20} />
                        Delete Transaction
                      </button>
                    </div>
                  ) : (
                    /* Add Mode: Show normal Save button */
                    <button
                      onClick={handleSaveTransaction}
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
                      {saveAsTemplate && !showTemplateNameInput
                        ? "Continue"
                        : "Save Transaction"}
                    </button>
                  )}
                </>
              )}
            </div>
          </div>
        )}
      </div>

      {/* Account BottomSheet for Expense/Income flows */}
      <PremiumAccountBottomSheet
        isOpen={showAccountBottomSheet}
        onClose={() => setShowAccountBottomSheet(false)}
        accounts={MOCK_ACCOUNTS}
        selectedAccountId={selectedAccountId}
        onSelectAccount={(accountId) => {
          setShowAccountBottomSheet(false);
          handleAccountSelected(accountId);
        }}
        title="Select Account"
      />

      {/* Transaction Type Selector Overlay */}
      <PremiumTransactionTypeSelector
        isOpen={showTransactionTypeSelector}
        selectedType={transactionType}
        onSelectType={(type) => {
          setTransactionType(type);
          // Clear category/account when switching types
          if (type === "transfer") {
            setSelectedCategory(undefined);
            setSelectedSubcategory(undefined);
            setSelectedAccountId(undefined);
          } else {
            setFromAccountId(undefined);
            setToAccountId(undefined);
          }
        }}
        onClose={() => setShowTransactionTypeSelector(false)}
      />

      {/* Category Selector Overlay */}
      <PremiumCategorySelector
        isOpen={showCategorySelector}
        selectedCategory={selectedCategory}
        categories={CATEGORY_METADATA
          .filter((cat) => cat.id !== "income")
          .map((cat) => ({
            id: cat.id as TransactionCategory,
            label: cat.label,
            description: `Track your ${cat.label.toLowerCase()} expenses`,
          }))}
        onSelectCategory={(category) => {
          // Check if category has subcategories
          const hasSubcategories = CATEGORY_SUBCATEGORIES[category]?.length > 0;
          
          if (hasSubcategories) {
            // Store category and show subcategory selector
            setPendingCategory(category);
            setShowCategorySelector(false);
            setShowSubcategorySelector(true);
          } else {
            // No subcategories, set category directly and auto-progress
            setShowCategorySelector(false);
            handleCategorySelected(category, undefined);
          }
        }}
        onClose={() => setShowCategorySelector(false)}
      />

      {/* Subcategory Selector Overlay */}
      <PremiumSubcategorySelector
        isOpen={showSubcategorySelector}
        categoryLabel={
          pendingCategory
            ? CATEGORY_METADATA.find((c) => c.id === pendingCategory)?.label || ""
            : ""
        }
        selectedSubcategory={selectedSubcategory}
        subcategories={
          pendingCategory
            ? (CATEGORY_SUBCATEGORIES[pendingCategory] || []).map((sub) => ({
                id: sub,
                label: sub,
              }))
            : []
        }
        onSelectSubcategory={(subcategory) => {
          if (pendingCategory) {
            setShowSubcategorySelector(false);
            setPendingCategory(undefined);
            handleCategorySelected(pendingCategory, subcategory || undefined);
          }
        }}
        onBack={() => {
          setShowSubcategorySelector(false);
          setShowCategorySelector(true);
          setPendingCategory(undefined);
        }}
        onClose={() => {
          setShowSubcategorySelector(false);
          setPendingCategory(undefined);
        }}
      />

      {/* Delete Confirmation Dialog */}
      <AlertDialog open={showDeleteDialog} onOpenChange={setShowDeleteDialog}>
        <AlertDialogContent className="
          bg-[var(--premium-surface-2)]
          border border-[var(--premium-glass-border)]
          text-[var(--premium-text-primary)]
          rounded-[var(--premium-radius-2xl)]
          max-w-[calc(100%-2rem)]
          sm:max-w-[425px]
        ">
          <AlertDialogHeader>
            <AlertDialogTitle className="
              text-[var(--premium-text-primary)]
              heading-5
              flex items-center gap-[8px]
            ">
              <div className="
                w-[40px] h-[40px]
                rounded-full
                bg-[#f5576c]/10
                flex items-center justify-center
              ">
                <Trash2 size={20} className="text-[#f5576c]" />
              </div>
              Delete Transaction?
            </AlertDialogTitle>
            <AlertDialogDescription className="
              text-[var(--premium-text-secondary)]
              body-md
              pt-[var(--premium-space-sm)]
            ">
              This action cannot be undone. This will permanently delete this transaction
              and update your account balance accordingly.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter className="flex-col sm:flex-row gap-[var(--premium-space-md)] mt-[var(--premium-space-lg)]">
            <AlertDialogCancel className="
              w-full sm:w-auto
              h-[48px]
              rounded-[var(--premium-radius-full)]
              bg-[var(--premium-surface-3)]
              text-[var(--premium-text-primary)]
              border border-[var(--premium-glass-border)]
              hover:bg-[var(--premium-surface-4)]
              font-medium
              transition-all duration-200
            ">
              Cancel
            </AlertDialogCancel>
            <AlertDialogAction
              onClick={handleDeleteTransaction}
              className="
                w-full sm:w-auto
                h-[48px]
                rounded-[var(--premium-radius-full)]
                bg-[#f5576c]
                text-white
                hover:bg-[#e03e54]
                font-medium
                transition-all duration-200
              "
            >
              Delete
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      {/* Chip Animations */}
      <style>{`
        @keyframes fadeIn {
          from {
            opacity: 0;
            transform: translateY(-4px);
          }
          to {
            opacity: 1;
            transform: translateY(0);
          }
        }

        /* Ripple effect on chip press */
        button:active::after {
          content: '';
          position: absolute;
          inset: 0;
          border-radius: inherit;
          background: radial-gradient(circle, rgba(16, 185, 129, 0.3) 0%, transparent 70%);
          animation: ripple 0.6s ease-out;
          pointer-events: none;
        }

        @keyframes ripple {
          from {
            transform: scale(0);
            opacity: 1;
          }
          to {
            transform: scale(2);
            opacity: 0;
          }
        }
      `}</style>
    </div>
  );
}