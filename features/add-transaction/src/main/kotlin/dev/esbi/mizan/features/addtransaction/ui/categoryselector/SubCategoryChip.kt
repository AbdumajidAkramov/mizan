package dev.esbi.mizan.features.addtransaction.ui.categoryselector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.design.theme.colors.MizanTheme

@Composable
internal fun SubCategoryChip(
    label: String,
    isSelected: Boolean,
    tintColor: Color,
    onClick: () -> Unit
) {
    val background = if (isSelected) tintColor else MizanTheme.premium.colors.surface3
    val textColor = if (isSelected) Color.White else MizanTheme.premium.text.secondary

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(MizanTheme.premium.radius.full))
            .clickable { onClick() },
        color = background,
        shape = RoundedCornerShape(MizanTheme.premium.radius.full)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MizanTheme.typography.labelMd,
            color = textColor,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
