package dev.esbi.mizan.feature.newtransaction.categoryselect

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.R
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.ui.theme.colors.MizanTheme

/**
 * Premium Subcategory Selector - Child Categories View
 * Displays subcategories with back navigation and "No Subcategory" option
 */
@Composable
fun PremiumSubcategorySelector(
    parentCategory: Category,
    subcategories: List<Category>,
    selectedSubcategoryId: Long?,
    onSubcategoryClick: (Category?) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Header with Back Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MizanTheme.premium.colors.surface2)
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_left),
                    contentDescription = "Back",
                    tint = MizanTheme.premium.text.secondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Parent Category Name
            Text(
                text = parentCategory.name,
                style = MizanTheme.typography.headingLg,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Subtitle with left padding to align with title
        Text(
            text = "Choose a subcategory (optional)",
            style = MizanTheme.typography.bodySm,
            color = MizanTheme.premium.text.tertiary,
            modifier = Modifier.padding(start = 44.dp)
        )

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg))

        // Subcategory List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Subcategory Items
            items(
                items = subcategories,
                key = { it.id }
            ) { subcategory ->
                SubcategoryItemCard(
                    name = subcategory.name,
                    isSelected = selectedSubcategoryId == subcategory.id,
                    onClick = { onSubcategoryClick(subcategory) }
                )
            }
        }
    }
}

/**
 * "No Subcategory" option card with special styling
 */
@Composable
private fun NoSubcategoryItem(
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(150),
        label = "scale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            MizanTheme.premium.colors.emerald.copy(alpha = 0.1f)
        } else {
            MizanTheme.premium.colors.surface2
        },
        animationSpec = tween(200),
        label = "backgroundColor"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) {
            MizanTheme.premium.colors.emerald
        } else {
            Color.Transparent
        },
        animationSpec = tween(200),
        label = "borderColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) {
            MizanTheme.premium.colors.emerald
        } else {
            MizanTheme.premium.text.primary
        },
        animationSpec = tween(200),
        label = "textColor"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Text Content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "No Subcategory",
                style = MizanTheme.typography.bodyMd,
                color = textColor,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Skip subcategory selection",
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
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

/**
 * Simple subcategory item card (text only)
 */
@Composable
private fun SubcategoryItemCard(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(150),
        label = "scale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            MizanTheme.premium.colors.emerald.copy(alpha = 0.1f)
        } else {
            MizanTheme.premium.colors.surface2
        },
        animationSpec = tween(200),
        label = "backgroundColor"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) {
            MizanTheme.premium.colors.emerald
        } else {
            Color.Transparent
        },
        animationSpec = tween(200),
        label = "borderColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) {
            MizanTheme.premium.colors.emerald
        } else {
            MizanTheme.premium.text.primary
        },
        animationSpec = tween(200),
        label = "textColor"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Text Content
        Text(
            text = name,
            style = MizanTheme.typography.bodyMd,
            color = textColor,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

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
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
