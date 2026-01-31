package dev.esbi.mizan.feature.newtransaction.transactiontype

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.R
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.ui.theme.colors.MizanTheme

data class TransactionTypeInfo(
    val type: TransactionType,
    val label: String,
    val description: String,
    val iconRes: Int,
    val color: Color,
    val bgColor: Color
)

@Composable
fun PremiumTransactionTypeSelector(
    isVisible: Boolean,
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit,
    onDismiss: () -> Unit
) {
    if (!isVisible) return

    val types = listOf(
        TransactionTypeInfo(
            type = TransactionType.EXPENSE,
            label = "Expense",
            description = "Money spent",
            iconRes = R.drawable.ic_trend_up,
            color = Color(0xFFF5576C),
            bgColor = Color(0x1AF5576C) // 10% opacity
        ),
        TransactionTypeInfo(
            type = TransactionType.INCOME,
            label = "Income",
            description = "Money received",
            iconRes = R.drawable.ic_down_trend,
            color = Color(0xFF4FACFE),
            bgColor = Color(0x1A4FACFE) // 10% opacity
        ),
        TransactionTypeInfo(
            type = TransactionType.TRANSFER,
            label = "Transfer",
            description = "Move between accounts",
            iconRes = R.drawable.ic_swap_horizontal,
            color = Color(0xFF10B981),
            bgColor = Color(0x1A10B981) // 10% opacity
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MizanTheme.premium.glass.bg)
            .clickable { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MizanTheme.premium.spacing.lg)
                .align(Alignment.Center)
        ) {
            // Main Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.xxl))
                    .background(MizanTheme.premium.background.primary)
                    .padding(MizanTheme.premium.spacing.lg)
            ) {
                // Header
                Text(
                    text = "Transaction Type",
                    style = MizanTheme.typography.headingMd,
                    color = MizanTheme.premium.text.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Choose the type of transaction",
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg))

                // Type Options
                types.forEach { typeInfo ->
                    val isSelected = selectedType == typeInfo.type
                    var isPressed by remember { mutableStateOf(false) }
                    val scale by animateFloatAsState(
                        targetValue = if (isPressed) 0.98f else 1f,
                        animationSpec = tween(200),
                        label = "scale"
                    )

                    TransactionTypeItem(
                        typeInfo = typeInfo,
                        isSelected = isSelected,
                        scale = scale,
                        onClick = {
                            onTypeSelected(typeInfo.type)
                            onDismiss()
                        },
                        onPress = { isPressed = true },
                        onRelease = { isPressed = false }
                    )

                    if (typeInfo.type != TransactionType.TRANSFER) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionTypeItem(
    typeInfo: TransactionTypeInfo,
    isSelected: Boolean,
    scale: Float,
    onClick: () -> Unit,
    onPress: () -> Unit,
    onRelease: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(
                if (isSelected) {
                    MizanTheme.premium.colors.emerald.copy(alpha = 0.1f)
                } else {
                    MizanTheme.premium.colors.surface2
                }
            )
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) MizanTheme.premium.colors.emerald else Color.Transparent,
                shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
            )
            .clickable {
                onPress()
                onClick()
                onRelease()
            }
            .padding(MizanTheme.premium.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
    ) {
        // Icon Container
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                .background(if (isSelected) typeInfo.color else typeInfo.bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = typeInfo.iconRes),
                contentDescription = null,
                tint = if (isSelected) Color.White else typeInfo.color,
                modifier = Modifier.size(24.dp)
            )
        }
        
        // Text Column
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = typeInfo.label,
                style = MizanTheme.typography.bodyMd,
                color = if (isSelected) MizanTheme.premium.colors.emerald 
                       else MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(2.dp))
            
            Text(
                text = typeInfo.description,
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary
            )
        }
        
        // Check Icon (only when selected)
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MizanTheme.premium.colors.emerald),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        } else {
            Spacer(modifier = Modifier.width(24.dp))
        }
    }
}
