package dev.esbi.mizan.feature.transfer.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.R
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.feature.transfer.presentation.TransferViewModel
import dev.esbi.mizan.feature.transfer.presentation.store.TransferStore
import dev.esbi.mizan.ui.theme.colors.MizanTheme

private val Emerald = Color(0xFF10B981)
private val EmeraldDark = Color(0xFF059669)

@Composable
fun TransferScreen(
    viewModel: TransferViewModel,
    onBack: () -> Unit = {},
    onTransferSuccess: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState(initial = TransferStore.State())

    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is TransferStore.Label.TransferSuccess -> onTransferSuccess()
                is TransferStore.Label.ShowError -> {}
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MizanTheme.premium.background.primary)
    ) {
        // Zen background glow orbs
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(300.dp)
                .graphicsLayer { alpha = 0.03f }
                .background(
                    Brush.radialGradient(colors = listOf(Emerald, Color.Transparent)),
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(250.dp)
                .graphicsLayer { alpha = 0.03f }
                .background(
                    Brush.radialGradient(colors = listOf(Color(0xFF667EEA), Color.Transparent)),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            // Top App Bar
            TransferTopBar(
                canSubmit = state.canSubmit,
                isLoading = state.isLoading,
                onBack = onBack,
                onSubmit = { viewModel.onIntent(TransferStore.Intent.SubmitTransfer) }
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = MizanTheme.premium.spacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(MizanTheme.premium.spacing.xl))

                // Amount Display
                AmountDisplay(
                    amountText = state.amountText,
                    onAmountChange = { viewModel.onIntent(TransferStore.Intent.InputAmount(it)) }
                )

                Spacer(Modifier.height(MizanTheme.premium.spacing.xl * 2))

                // From Account Carousel
                AccountCarouselSection(
                    label = "From Account",
                    accounts = state.accounts,
                    selectedAccount = state.sourceAccount,
                    onSelect = { viewModel.onIntent(TransferStore.Intent.SelectSource(it)) }
                )

                Spacer(Modifier.height(MizanTheme.premium.spacing.md))

                // Swap / Arrow indicator
                SwapIndicator(
                    canContinue = state.canSubmit,
                    onClick = { viewModel.onIntent(TransferStore.Intent.SwapAccounts) }
                )

                Spacer(Modifier.height(MizanTheme.premium.spacing.md))

                // To Account Carousel
                AccountCarouselSection(
                    label = "To Account",
                    accounts = state.availableDestinations,
                    selectedAccount = state.destinationAccount,
                    onSelect = { viewModel.onIntent(TransferStore.Intent.SelectDestination(it)) }
                )

                Spacer(Modifier.height(MizanTheme.premium.spacing.xl))

                // Helper text / Confirmation summary
                TransferHelperContent(state)

                Spacer(Modifier.height(120.dp))
            }
        }
    }
}

// ── Top App Bar ─────────────────────────────────────────────────────────

@Composable
private fun TransferTopBar(
    canSubmit: Boolean,
    isLoading: Boolean,
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MizanTheme.premium.glass.border,
                shape = RoundedCornerShape(0.dp)
            )
            .padding(horizontal = MizanTheme.premium.spacing.md, vertical = 12.dp)
    ) {
        // Back button
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(40.dp)
                .clip(CircleShape)
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_notification),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        // Title
        Text(
            "Transfer",
            style = MizanTheme.premium.typography.headingSm,
            color = MizanTheme.premium.text.primary,
            modifier = Modifier.align(Alignment.Center)
        )

        // Continue button
        if (canSubmit) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clip(RoundedCornerShape(50))
                    .background(Emerald)
                    .clickable(enabled = !isLoading) { onSubmit() }
                    .padding(horizontal = MizanTheme.premium.spacing.md, vertical = 8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("✓", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "Transfer",
                            style = MizanTheme.premium.typography.labelMd,
                            color = Color.White
                        )
                    }
                }
            }
        } else {
            Spacer(Modifier.size(40.dp).align(Alignment.CenterEnd))
        }
    }
}

// ── Amount Display ──────────────────────────────────────────────────────

@Composable
private fun AmountDisplay(
    amountText: String,
    onAmountChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Transfer Amount",
            style = MizanTheme.premium.typography.bodySm,
            color = MizanTheme.premium.text.tertiary
        )
        Spacer(Modifier.height(8.dp))

        BasicTextField(
            value = amountText,
            onValueChange = { newText ->
                val filtered = newText.filter { it.isDigit() || it == '.' }
                onAmountChange(filtered)
            },
            textStyle = TextStyle(
                fontSize = 56.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            cursorBrush = SolidColor(Emerald),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (amountText.isEmpty()) {
                        Text(
                            "0",
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Light,
                            color = Color.White.copy(alpha = 0.2f),
                            textAlign = TextAlign.Center
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(Modifier.height(4.dp))
        Text(
            "UZS",
            style = MizanTheme.premium.typography.bodySm,
            color = Color.White.copy(alpha = 0.4f)
        )
    }
}

// ── Account Carousel Section ────────────────────────────────────────────

@Composable
private fun AccountCarouselSection(
    label: String,
    accounts: List<Account>,
    selectedAccount: Account?,
    onSelect: (Account) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            label,
            style = MizanTheme.premium.typography.labelMd,
            color = MizanTheme.premium.text.tertiary,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        if (accounts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
                    .background(MizanTheme.premium.glass.bg)
                    .border(1.dp, MizanTheme.premium.glass.border, RoundedCornerShape(MizanTheme.premium.radius.xl)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No accounts available",
                    style = MizanTheme.premium.typography.bodySm,
                    color = Color.White.copy(alpha = 0.4f)
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                accounts.forEach { account ->
                    AccountCard(
                        account = account,
                        isSelected = selectedAccount?.id == account.id,
                        onClick = { onSelect(account) }
                    )
                }
            }
        }
    }
}

// ── Account Card ────────────────────────────────────────────────────────

@Composable
private fun AccountCard(
    account: Account,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val accentColor = try {
        Color(android.graphics.Color.parseColor(account.color ?: "#667EEA"))
    } catch (_: Exception) {
        Color(0xFF667EEA)
    }
    val borderColor = if (isSelected) Emerald else MizanTheme.premium.glass.border
    val bgAlpha = if (isSelected) 0.15f else 0.08f
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = tween(200),
        label = "cardScale"
    )

    Box(
        modifier = Modifier
            .width(160.dp)
            .scale(scale)
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = bgAlpha),
                        Color.White.copy(alpha = bgAlpha * 0.5f)
                    )
                )
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
            )
            .clickable { onClick() }
            .padding(MizanTheme.premium.spacing.md)
    ) {
        Column {
            // Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                    .background(accentColor.copy(alpha = 0.2f))
                    .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(MizanTheme.premium.radius.lg)),
                contentAlignment = Alignment.Center
            ) {
                Text(accountTypeEmoji(account.type), fontSize = 20.sp)
            }

            Spacer(Modifier.height(10.dp))

            // Name
            Text(
                account.name,
                style = MizanTheme.premium.typography.labelMd,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(2.dp))

            // Balance
            Text(
                "${formatAmount(account.balance)} ${account.currency.code}",
                style = MizanTheme.premium.typography.bodySm,
                color = Color.White.copy(alpha = 0.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Selected indicator
            if (isSelected) {
                Spacer(Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Emerald.copy(alpha = 0.2f))
                        .border(1.dp, Emerald.copy(alpha = 0.3f), RoundedCornerShape(50))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        "Selected",
                        style = MizanTheme.premium.typography.labelSm,
                        color = Emerald
                    )
                }
            }
        }
    }
}

// ── Swap Indicator ──────────────────────────────────────────────────────

@Composable
private fun SwapIndicator(
    canContinue: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (canContinue) 0.20f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )
    val arrowColor = if (canContinue) Emerald else MizanTheme.premium.text.tertiary

    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(MizanTheme.premium.glass.bg)
            .border(2.dp, MizanTheme.premium.glass.border, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Pulse glow
        if (canContinue) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape)
                    .background(Emerald.copy(alpha = pulseAlpha))
            )
        }
        Text("↕", fontSize = 28.sp, color = arrowColor)
    }
}

// ── Helper Content (status text / confirmation summary) ─────────────────

@Composable
private fun TransferHelperContent(state: TransferStore.State) {
    when {
        state.sourceAccount == null -> {
            Text(
                "Select source account to begin transfer",
                style = MizanTheme.premium.typography.bodySm,
                color = MizanTheme.premium.text.tertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        state.destinationAccount == null -> {
            Text(
                "Now select destination account",
                style = MizanTheme.premium.typography.bodySm,
                color = MizanTheme.premium.text.tertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        state.canSubmit -> {
            // Confirmation card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
                    .background(Emerald.copy(alpha = 0.05f))
                    .border(1.dp, Emerald.copy(alpha = 0.20f), RoundedCornerShape(MizanTheme.premium.radius.xl))
                    .padding(MizanTheme.premium.spacing.lg)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Ready to transfer",
                        style = MizanTheme.premium.typography.labelMd,
                        color = Emerald
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        buildString {
                            append("${formatAmount(state.amount)} UZS from ")
                            append(state.sourceAccount!!.name)
                            append(" to ")
                            append(state.destinationAccount!!.name)
                        },
                        style = MizanTheme.premium.typography.bodySm,
                        color = Color.White.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        state.amount <= 0 && state.sourceAccount != null && state.destinationAccount != null -> {
            Text(
                "Enter amount to transfer",
                style = MizanTheme.premium.typography.bodySm,
                color = MizanTheme.premium.text.tertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    // Error message
    state.error?.let { error ->
        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                .background(Color(0xFFEF4444).copy(alpha = 0.1f))
                .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.2f), RoundedCornerShape(MizanTheme.premium.radius.lg))
                .padding(MizanTheme.premium.spacing.md)
        ) {
            Text(
                error,
                style = MizanTheme.premium.typography.bodySm,
                color = Color(0xFFEF4444),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ── Utils ───────────────────────────────────────────────────────────────

private fun formatAmount(amount: Double): String =
    String.format("%,.0f", amount).replace(',', ' ')

private fun accountTypeEmoji(type: Account.Type): String = when (type) {
    Account.Type.CASH -> "💵"
    Account.Type.CARD -> "💳"
    Account.Type.SAVINGS -> "🐷"
    Account.Type.DEBT -> "📋"
    Account.Type.INVESTMENT -> "📈"
}
