package dev.esbi.mizan.feature.newtransaction.amountinput

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.R
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.feature.addtransaction.presentation.models.InputMode
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.feature.addtransaction.presentation.utils.AutoResizingText
import dev.esbi.mizan.feature.addtransaction.presentation.widgets.PremiumCalculatorKeypad
import dev.esbi.mizan.feature.newtransaction.amountinput.inputtypes.CameraInputStep
import dev.esbi.mizan.feature.newtransaction.amountinput.inputtypes.VoiceInputStep
import dev.esbi.mizan.feature.newtransaction.amountinput.widgets.InputModeContent
import dev.esbi.mizan.feature.newtransaction.store.AmountInputState
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons
import dev.esbi.mizan.utils.annotatedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AmountInputContent(
    state: AmountInputState = AmountInputState(),
    selectedAccount: Account? = null,
    selectedCategory: Category? = null,
    selectedSubCategory: Category? = null,
    onTypeClick: () -> Unit = {},
    onCategoryClick: () -> Unit = {},
    onAccountClick: () -> Unit = {},
    accept: (NewTransactionStore.Intent) -> Unit
) {
    var showTemplates by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AmountInputHeader(
                showTemplates = showTemplates,
                onTemplatesToggle = { showTemplates = !showTemplates },
                onClose = {}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MizanTheme.premium.background.primary)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Templates Carousel (Animated)
            AnimatedVisibility(
                visible = showTemplates,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                TemplatesCarousel(
                    onTemplateClick = { /* TODO: Apply template */ },
                    onManageClick = { /* TODO: Navigate to manage templates */ }
                )
            }

            // Amount Display Section (flex-1)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = MizanTheme.premium.spacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Calculation String (if any)
                if (state.keypadState.displayText.isNotEmpty()) {
                    Text(
                        text = state.keypadState.displayText,
                        style = MizanTheme.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Large Amount Display
                val formattedAmount = state.keypadState.amountText.annotatedString(
                    currency = state.keypadState.currency
                )
                AutoResizingText(
                    text = formattedAmount,
                    style = MizanTheme.typography.displayXl.copy(
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 64.sp
                    ),
                    color = MizanTheme.premium.text.primary,
                    maxLines = 1,
                    minFontSize = 24.sp,
                    modifier = Modifier.padding(vertical = MizanTheme.premium.spacing.md)
                )

                Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.sm))

                // Transaction Context Row (Type, Category, Account chips)
                TransactionContextRow(
                    transactionType = state.transactionType,
                    selectedCategory = selectedCategory,
                    selectedSubCategory = selectedSubCategory,
                    selectedAccount = selectedAccount,
                    onTypeClick = onTypeClick,
                    onCategoryClick = onCategoryClick,
                    onAccountClick = onAccountClick
                )

                Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))

                // Input Mode Selector (Calculator, Mic, Camera icons)
                InputModeContent(
                    inputMode = state.inputMode,
                    onModeChange = { mode ->
                        accept(NewTransactionStore.Intent.OnModeChange(mode))
                    }
                )
            }

            // Keypad Section - Bottom with glass effect
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MizanTheme.premium.glass.bg,
                        shape = RoundedCornerShape(
                            topStart = MizanTheme.premium.radius.xxl,
                            topEnd = MizanTheme.premium.radius.xxl
                        )
                    )
                    .border(
                        color = MizanTheme.premium.glass.border,
                        width = 1.dp,
                        shape = RoundedCornerShape(
                            topStart = MizanTheme.premium.radius.xxl,
                            topEnd = MizanTheme.premium.radius.xxl
                        )
                    )
                    .padding(MizanTheme.premium.spacing.lg)
            ) {
                when (state.inputMode) {
                    InputMode.Manual -> {
                        PremiumCalculatorKeypad(
                            onNumberClick = {
                                accept(NewTransactionStore.AmountInputIntent.OnNumberClick(it))
                            }
                        )

                        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))

                        // Next Button
                        NextButton(
                            enabled = state.keypadState.canSubmit,
                            onClick = {
                                accept(NewTransactionStore.Intent.TransactionTypesShow)
                            }
                        )
                    }

                    InputMode.Voice -> {
                        VoiceInputStep(
                            state = state.voiceInputState,
                            onStartListening = {
                                accept(NewTransactionStore.VoiceRecognitionIntent.OnStartListening)
                            },
                            onStopListening = {
                                accept(NewTransactionStore.VoiceRecognitionIntent.OnStopListening)
                            },
                            onVoiceRecognitionError = { error ->
                                accept(
                                    NewTransactionStore.VoiceRecognitionIntent.OnVoiceRecognitionError(
                                        error
                                    )
                                )
                            },
                            onSubmitVoice = { voiceText ->
                                accept(
                                    NewTransactionStore.VoiceRecognitionIntent.OnVoiceResult(
                                        voiceText
                                    )
                                )
                            }
                        )
                    }

                    InputMode.Scan -> {
                        CameraInputStep(
                            state = state.cameraInputState,
                            onStartScanning = { accept(NewTransactionStore.CameraScanIntent.OnStartCameraScan) },
                            onStopScanning = { accept(NewTransactionStore.CameraScanIntent.OnStopCameraScan) },
                            onAmountExtracted = {
                                accept(NewTransactionStore.CameraScanIntent.OnAmountExtracted(it))
                            },
                            onScanResult = { text, confidence ->
                                accept(
                                    NewTransactionStore.CameraScanIntent.OnReceiptScanResult(
                                        text,
                                        confidence
                                    )
                                )
                            },
                            onQRCodeScanned = { qrText ->
                                accept(
                                    NewTransactionStore.CameraScanIntent.OnQrCodeScanned(
                                        qrText
                                    )
                                )
                            },
                            onError = { error ->
                                accept(
                                    NewTransactionStore.CameraScanIntent.OnCameraScanError(
                                        error
                                    )
                                )
                            },
                            onNext = { }
                        )
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AmountInputHeader(
    showTemplates: Boolean,
    onTemplatesToggle: () -> Unit,
    onClose: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "New Transaction",
                style = MizanTheme.premium.typography.headingSm,
                color = MizanTheme.premium.text.primary
            )
        },
        navigationIcon = {
            IconButton(onClick = onClose) {
                MizanIcon(
                    icon = IconValue(Icons.ic_arrow_back),
                    contentDescription = "Back",
                    tint = MizanTheme.premium.text.primary
                )
            }
        },
        actions = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Templates Toggle Button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            if (showTemplates) MizanTheme.premium.colors.emerald
                            else MizanTheme.premium.colors.surface2
                        )
                        .clickable { onTemplatesToggle() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_wand_sparkles),
                        contentDescription = "Templates",
                        tint = if (showTemplates) Color.White
                        else MizanTheme.premium.text.secondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Close Button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MizanTheme.premium.colors.surface2)
                        .clickable { onClose() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Close",
                        tint = MizanTheme.premium.text.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MizanTheme.premium.background.primary
        )
    )
}

@Composable
private fun TemplatesCarousel(
    onTemplateClick: (QuickTemplate) -> Unit,
    onManageClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MizanTheme.premium.background.primary)
            .border(
                width = 1.dp,
                color = MizanTheme.premium.glass.border
            )
            .padding(
                horizontal = MizanTheme.premium.spacing.lg,
                vertical = MizanTheme.premium.spacing.md
            )
    ) {
        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quick Templates",
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.secondary,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "Manage",
                style = MizanTheme.typography.bodyXs,
                color = MizanTheme.premium.colors.emerald,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onManageClick() }
            )
        }

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.sm))

        // Templates LazyRow
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(mockTemplates) { template ->
                TemplateCard(
                    template = template,
                    onClick = { onTemplateClick(template) }
                )
            }
        }
    }
}

@Composable
private fun TemplateCard(
    template: QuickTemplate,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(MizanTheme.premium.colors.surface2)
            .border(
                width = 1.dp,
                color = MizanTheme.premium.glass.border,
                shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
            )
            .clickable { onClick() }
            .padding(MizanTheme.premium.spacing.md)
    ) {
        Column {
            // Template Name Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = MizanTheme.premium.spacing.sm)
            ) {
                // Type Icon
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(template.typeColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = template.iconRes),
                        contentDescription = null,
                        tint = template.typeColor,
                        modifier = Modifier.size(14.dp)
                    )
                }

                Text(
                    text = template.name,
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.primary,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            // Amount
            Text(
                text = "$${String.format("%.2f", template.amount)}",
                style = MizanTheme.typography.headingSm,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Category Info
            Text(
                text = "${template.category} • ${template.subcategory}",
                style = MizanTheme.typography.bodyXs,
                color = MizanTheme.premium.text.tertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Account Info
            Text(
                text = template.accountName,
                style = MizanTheme.typography.bodyXs,
                color = MizanTheme.premium.text.secondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun NextButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (enabled) {
        MizanTheme.premium.colors.emerald
    } else {
        MizanTheme.premium.colors.surface2
    }

    val textColor = if (enabled) {
        Color.White
    } else {
        MizanTheme.premium.text.muted
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(MizanTheme.premium.radius.full))
            .background(bgColor)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Next",
                style = MizanTheme.typography.bodyLg,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// Transaction Context Row - Shows Type, Category, Account chips
@Composable
private fun TransactionContextRow(
    transactionType: TransactionType?,
    selectedCategory: Category?,
    selectedSubCategory: Category?,
    selectedAccount: Account?,
    onTypeClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onAccountClick: () -> Unit
) {
    val typeColor = when (transactionType) {
        TransactionType.EXPENSE -> Color(0xFFF5576C) // Coral/Red
        TransactionType.INCOME -> MizanTheme.premium.colors.emerald
        TransactionType.TRANSFER -> MizanTheme.premium.colors.primary
        null -> MizanTheme.premium.text.secondary
    }

    val typeName = when (transactionType) {
        TransactionType.EXPENSE -> "Expense"
        TransactionType.INCOME -> "Income"
        TransactionType.TRANSFER -> "Transfer"
        null -> "Expense"
    }

    val typeIcon = when (transactionType) {
        TransactionType.EXPENSE -> R.drawable.ic_trend_up
        TransactionType.INCOME -> R.drawable.ic_down_trend
        TransactionType.TRANSFER -> R.drawable.ic_swap_horizontal
        null -> R.drawable.ic_trend_up
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
    ) {
        // First row: Type, Category, Account chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Transaction Type Chip (outlined with dropdown)
            TransactionTypeChip(
                label = typeName,
                color = typeColor,
                iconRes = typeIcon,
                onClick = onTypeClick
            )

            // Category Chip - filled or placeholder
            CategoryChip(
                categoryName = selectedCategory?.name,
                subCategoryName = selectedSubCategory?.name,
                onClick = onCategoryClick
            )

            // Account Chip - filled or placeholder
            AccountChip(
                accountName = selectedAccount?.name,
                onClick = onAccountClick
            )
        }
    }
}

@Composable
private fun TransactionTypeChip(
    label: String,
    color: Color,
    iconRes: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(MizanTheme.premium.radius.full))
            .border(
                width = 1.5.dp,
                color = color,
                shape = RoundedCornerShape(MizanTheme.premium.radius.full)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = label,
            style = MizanTheme.typography.bodySm,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun CategoryChip(
    categoryName: String?,
    subCategoryName: String?,
    onClick: () -> Unit
) {
    val isPlaceholder = categoryName == null
    val displayText = when {
        categoryName == null -> "+ Category"
        subCategoryName != null -> "$categoryName • $subCategoryName"
        else -> categoryName
    }

    val borderColor = MizanTheme.premium.text.tertiary
    val cornerRadius = 50f // Full rounded

    Row(
        modifier = Modifier
            .then(
                if (isPlaceholder) {
                    Modifier.drawBehind {
                        drawRoundRect(
                            color = borderColor,
                            style = Stroke(
                                width = 1.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(8f, 6f),
                                    0f
                                )
                            ),
                            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                        )
                    }
                } else {
                    Modifier
                        .clip(RoundedCornerShape(MizanTheme.premium.radius.full))
                        .background(MizanTheme.premium.colors.emerald.copy(alpha = 0.15f))
                }
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = displayText,
            style = MizanTheme.typography.bodySm,
            color = if (isPlaceholder) MizanTheme.premium.text.tertiary
            else MizanTheme.premium.colors.emerald,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun AccountChip(
    accountName: String?,
    onClick: () -> Unit
) {
    val isPlaceholder = accountName == null
    val displayText = accountName ?: "+ Account"
    val borderColor = MizanTheme.premium.text.tertiary
    val cornerRadius = 50f // Full rounded

    Row(
        modifier = Modifier
            .then(
                if (isPlaceholder) {
                    Modifier.drawBehind {
                        drawRoundRect(
                            color = borderColor,
                            style = Stroke(
                                width = 1.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(8f, 6f),
                                    0f
                                )
                            ),
                            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                        )
                    }
                } else {
                    Modifier
                        .clip(RoundedCornerShape(MizanTheme.premium.radius.full))
                        .background(MizanTheme.premium.colors.surface2)
                }
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isPlaceholder) {
            Icon(
                painter = painterResource(id = R.drawable.ic_wallet),
                contentDescription = null,
                tint = MizanTheme.premium.text.secondary,
                modifier = Modifier.size(14.dp)
            )
        }
        Text(
            text = displayText,
            style = MizanTheme.typography.bodySm,
            color = if (isPlaceholder) MizanTheme.premium.text.tertiary
            else MizanTheme.premium.text.primary,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// Data class for quick templates
data class QuickTemplate(
    val id: String,
    val name: String,
    val amount: Double,
    val category: String,
    val subcategory: String,
    val accountName: String,
    val typeColor: Color,
    val iconRes: Int
)

// Mock templates for preview
private val mockTemplates = listOf(
    QuickTemplate(
        id = "1",
        name = "Daily Lunch",
        amount = 12.50,
        category = "Food Dining",
        subcategory = "Restaurant",
        accountName = "Cash Wallet",
        typeColor = Color(0xFFF5576C),
        iconRes = R.drawable.ic_arrow_down
    ),
    QuickTemplate(
        id = "2",
        name = "Rent Payment",
        amount = 1500.00,
        category = "Bills Utilities",
        subcategory = "Rent",
        accountName = "Checking",
        typeColor = Color(0xFFF5576C),
        iconRes = R.drawable.ic_arrow_down
    )
)
