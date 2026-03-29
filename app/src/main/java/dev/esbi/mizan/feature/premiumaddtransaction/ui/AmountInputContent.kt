package dev.esbi.mizan.feature.premiumaddtransaction.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import dev.esbi.mizan.presentation.feature.premiumaddtransaction.ui.QuickTemplate
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmountInputHeader(
    showTemplates: Boolean,
    isEditMode: Boolean,
    onTemplatesToggle: () -> Unit,
    onClose: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = if (isEditMode) "Edit Transaction" else "New Transaction",
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
                modifier = Modifier.padding(end = 16.dp),
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
                        painter = painterResource(id = Icons.ic_wand_sparkles),
                        contentDescription = "Templates",
                        tint = if (showTemplates) Color.White
                        else MizanTheme.premium.text.secondary,
                        modifier = Modifier.size(18.dp)
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
fun TemplatesCarousel(
    templateList: List<QuickTemplate>,
    onTemplateClick: (QuickTemplate) -> Unit,
    onManageClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MizanTheme.premium.background.primary)
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

            items(templateList) { template ->
                TemplateCard(
                    template = template,
                    onClick = { onTemplateClick(template) }
                )
            }

        }
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .background(color = MizanTheme.premium.background.secondary)
        )
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
                text = "$${String.format(Locale.CANADA, "%.2f", template.amount)}",
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
fun TransactionTypeChip(
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
        // Dropdown chevron indicator
        Icon(
            painter = painterResource(id = Icons.ic_chevron_down),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
fun CategoryChip(
    categoryName: String?,
    subCategoryName: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
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
        modifier = modifier
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
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
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
fun AccountChip(
    accountName: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPlaceholder = accountName == null
    val displayText = accountName ?: "+ Account"
    val borderColor = MizanTheme.premium.text.tertiary
    val cornerRadius = 50f // Full rounded

    Row(
        modifier = modifier
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
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isPlaceholder) {
            Icon(
                painter = painterResource(id = Icons.ic_wallet),
                contentDescription = null,
                tint = MizanTheme.premium.colors.emerald,
                modifier = Modifier.size(14.dp)
            )
        }
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
/*

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
*/
