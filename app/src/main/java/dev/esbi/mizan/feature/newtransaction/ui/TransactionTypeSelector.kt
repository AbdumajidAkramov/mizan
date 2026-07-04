package dev.esbi.mizan.feature.newtransaction.ui

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.newtransaction.color
import dev.esbi.mizan.presentation.feature.addtransaction.model.TransactionType
import dev.esbi.mizan.design.kit.icon.IconValue
import dev.esbi.mizan.design.kit.icon.MizanIcon
import dev.esbi.mizan.design.theme.MizanTheme
import dev.esbi.mizan.design.theme.colors.MizanTheme
import dev.esbi.mizan.design.theme.shadows.premiumShadow
import dev.esbi.mizan.design.utils.IconRes


internal data class TypeConfig(
    val label: String,
    val color: Color,
    val icon: IconValue,
    val caption: String
)

@Composable
internal fun getTypeConfig(type: TransactionType): TypeConfig {
    return when (type) {
        TransactionType.EXPENSE -> TypeConfig(
            label = "Expense",
            color = Color(0xFFF5576C), // PremiumDesignSystem.colors.secondary
            icon = IconValue(IconRes.ic_arrow_up),
            caption = "Track your spending"
        )

        TransactionType.INCOME -> TypeConfig(
            label = "Income",
            color = Color(0xFF4FACFE), // PremiumDesignSystem.colors.success (variant)
            icon = IconValue(IconRes.ic_arrow_down),
            caption = "Record money received"
        )

        TransactionType.TRANSFER -> TypeConfig(
            label = "Transfer",
            color = Color(0xFF10B981), // PremiumDesignSystem.colors.success (emerald)
            icon = IconValue(IconRes.ic_swap_horizontal),
            caption = "Move money between accounts"
        )
    }
}

// 2. MAIN COMPONENT
@Composable
fun TransactionTypeSelector(
    selectedType: Transaction.Type,
    onTypeSelect: (TransactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    val config = getTypeConfig(selectedType)
    val text = remember { mutableStateOf(config.caption) }

    // Container (Glass effect)
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .premiumShadow(
                    shadowInfo = MizanTheme.premium.shadows.glowSecondary
                        .copy(
                            color = selectedType.color().copy(alpha = 0.3f),
                        ),
                    borderRadius = MizanTheme.premium.radius.md
                )
                .clip(shape = RoundedCornerShape(MizanTheme.premium.radius.full)) // rounded-full
                .background(color = MizanTheme.premium.glass.bg) // backdrop-blur & bg-glass
                .border(
                    width = 1.dp,
                    color = MizanTheme.premium.glass.border,
                    shape = RoundedCornerShape(MizanTheme.premium.radius.full)
                )
                .padding(6.dp), // p-[6px]
            horizontalArrangement = Arrangement.spacedBy(8.dp), // gap-[var(--premium-space-sm)]
            verticalAlignment = Alignment.CenterVertically
        ) {
            TransactionType.entries.forEach { type ->
                TransactionTypeButton(
                    type = type,
                    isActive = selectedType == type,
                    onSelect = {
                        onTypeSelect(type)
                    }
                )
            }
        }
        Text(
            modifier = Modifier.padding(top = MizanTheme.premium.spacing.md),
            text = text.value,
            style = MizanTheme.typography.bodyXs,
            color = MizanTheme.premium.text.muted
        )
    }
}

// 3. INDIVIDUAL BUTTON
@Composable
private fun TransactionTypeButton(
    type: TransactionType,
    isActive: Boolean,
    onSelect: () -> Unit
) {
    val config = getTypeConfig(type)

    // Matn rangi animatsiyasi (Active bo'lsa Oq, bo'lmasa kulrang)
    val contentColor by animateColorAsState(
        targetValue = if (isActive) {
            MizanTheme.premium.colors.white
        } else {
            MizanTheme.premium.text.secondary
        },
        animationSpec = tween(300),
        label = "textColor"
    )

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null // Ripple yo'q (Reactdagi kabi minimalist)
            ) { onSelect() },
        contentAlignment = Alignment.Center
    ) {
        // A) ACTIVE BACKGROUND (Morphing Effect)
        // React kodidagi: {isActive && (<div ... />)} qismi
        AnimatedVisibility(
            visible = isActive,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300)),
            modifier = Modifier.matchParentSize() // Boxni to'liq egallaydi
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(MizanTheme.premium.radius.full),
                        spotColor = config.color.copy(alpha = 0.5f)
                    )
                    .background(config.color, CircleShape)
            )
        }

        // B) CONTENT (Icon + Label)
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp), // px-lg py-sm
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MizanIcon(
                icon = config.icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = config.label,
                style = MizanTheme.typography.bodySm,
                fontWeight = FontWeight.Medium,
                color = contentColor
            )
        }
    }
}

// 4. PREVIEW
@Preview(
    showBackground = true,
    backgroundColor = 0xFF111827,
    uiMode = UI_MODE_NIGHT_YES,
    name = "UI_MODE_NIGHT_YES"
)
@Composable
private fun SelectorPreviewNight() {
    MizanTheme {
        Box(modifier = Modifier.padding(24.dp)) {
            TransactionTypeSelector(
                modifier = Modifier.fillMaxWidth(),
                selectedType = TransactionType.INCOME,
                onTypeSelect = {}
            )
        }
    }
}

// 4. PREVIEW
@Preview(
    showBackground = true,
    backgroundColor = 0xFF111827,
    uiMode = UI_MODE_NIGHT_NO,
    name = "UI_MODE_NIGHT_NO"
)
@Composable
private fun SelectorPreview() {
    MizanTheme {
        Box(modifier = Modifier.padding(24.dp)) {
            TransactionTypeSelector(
                modifier = Modifier.fillMaxWidth(),
                selectedType = TransactionType.EXPENSE,
                onTypeSelect = {}
            )
        }
    }
}