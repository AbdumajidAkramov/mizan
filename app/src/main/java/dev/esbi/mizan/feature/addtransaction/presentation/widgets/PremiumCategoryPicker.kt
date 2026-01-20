package dev.esbi.mizan.feature.addtransaction.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons

@Composable
fun PremiumCategoryPicker(
    selectedCategory: String?,
    onSelectCategory: (String) -> Unit
) {
    // Mock Data from CATEGORY_METADATA
    val categories = listOf(
        "food" to Color(0xFFFF6B9D),
        "transport" to Color(0xFF4FACFE),
        "shopping" to Color(0xFFFFA34D),
        "bills" to Color(0xFF00D2FF),
        "entertainment" to Color(0xFFC471F5),
        "health" to Color(0xFFFF6B6B)
    )

    Column(verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)) {
        // Grid manually or LazyVerticalGrid
        val rows = categories.chunked(3)
        rows.forEach { rowCats ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowCats.forEach { (id, color) ->
                    val isSelected = selectedCategory == id
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                            .background(if (isSelected) MizanTheme.premium.colors.surface3 else MizanTheme.premium.colors.surface2)
                            .clickable { onSelectCategory(id) }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(MizanTheme.premium.radius.md))
                                    .background(color.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                // CategoryIcon(id) bu yerda chaqiriladi
                                Icon(
                                    icon = IconValue(Icons.ic_mic),
                                    modifier = Modifier.size(24.dp),
                                    tint = color
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                id.capitalize(Locale.current),
                                style = MizanTheme.typography.bodySm,
                                color = if (isSelected) MizanTheme.premium.text.primary else MizanTheme.premium.text.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}