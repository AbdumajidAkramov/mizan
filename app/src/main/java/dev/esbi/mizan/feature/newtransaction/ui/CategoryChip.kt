package dev.esbi.mizan.feature.newtransaction.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.theme.colors.MizanTheme

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
