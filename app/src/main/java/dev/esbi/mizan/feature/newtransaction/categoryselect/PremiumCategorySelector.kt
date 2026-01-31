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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.feature.addtransaction.presentation.widgets.dashedBorder
import dev.esbi.mizan.ui.theme.colors.MizanTheme

/**
 * Premium Category Selector - Parent Categories View
 * Displays a list of parent categories with elegant styling
 */
@Composable
fun PremiumCategorySelector(
    categories: List<Category>,
    selectedCategoryId: Long?,
    onCategoryClick: (Category) -> Unit,
    onNavigateToManageCategories: () -> Unit,
    hasChildren: (Category) -> Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Header
        Text(
            text = "Select Category",
            style = MizanTheme.typography.headingLg,
            color = MizanTheme.premium.text.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Choose a category for this transaction",
            style = MizanTheme.typography.bodySm,
            color = MizanTheme.premium.text.tertiary
        )

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg))

        // Category List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(
                items = categories,
                key = { it.id }
            ) { category ->
                CategoryItemCard(
                    name = category.name,
                    description = "Track your ${category.name.lowercase()} expenses",
                    isSelected = selectedCategoryId == category.id,
                    onClick = { onCategoryClick(category) }
                )
            }
            item {
                PremiumAddCategoryItem(
                    onNavigateToManageCategories = onNavigateToManageCategories,
                )
            }
        }
    }
}

/**
 * Premium styled category item card
 */
@Composable
fun CategoryItemCard(
    name: String,
    description: String,
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
                text = name,
                style = MizanTheme.typography.bodyMd,
                color = textColor,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = description,
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

@Composable
fun PremiumAddCategoryItem(
    onNavigateToManageCategories: (() -> Unit)?,
) {
    onNavigateToManageCategories?.let { navigate ->
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()

        // Active:scale-[0.98] effekti
        val scale by animateFloatAsState(
            targetValue = if (isPressed) 0.98f else 1f,
            label = "scale_animation"
        )

        Surface(
            onClick = navigate,
            modifier = Modifier
                .fillMaxWidth()
                .dashedBorder(
                    color = MizanTheme.premium.colors.emerald.copy(alpha = 0.3f),
                    strokeWidth = 2.dp,
                    dashLength = 8.dp,  // Chiziq uzunligi
                    gapLength = 6.dp,   // Chiziqlar orasidagi masofa
                    cornerRadius = MizanTheme.premium.radius.xl
                )
                .graphicsLayer(scaleX = scale, scaleY = scale),
            // PremiumDesignSystem dagi radiuslardan foydalanamiz
            shape = RoundedCornerShape(MizanTheme.premium.radius.xl),
            // Emerald rangining 40% transparent holati (bg-emerald/40)
            color = MizanTheme.premium.colors.emerald.copy(alpha = 0.05f),
            interactionSource = interactionSource
        ) {
            Row(
                modifier = Modifier
                    .padding(MizanTheme.premium.spacing.md), // DesignSystem dagi spacing
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
            ) {
                // Plus Icon Container (Circle)
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MizanTheme.premium.colors.emerald.copy(alpha = 0.1f))
                        .border(
                            width = 1.dp,
                            color = MizanTheme.premium.colors.emerald.copy(alpha = 0.3f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MizanTheme.premium.colors.emerald
                    )
                }

                // Text Content Part
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Add New Category",
                        style = MizanTheme.typography.bodyLg.copy(
                            fontWeight = FontWeight.Medium,
                            color = MizanTheme.premium.colors.emerald
                        )
                    )
                    Text(
                        text = "Manage & customize your categories",
                        style = MizanTheme.typography.bodySm.copy(
                            color = MizanTheme.premium.text.tertiary // DesignSystem dagi rang
                        )
                    )
                }
            }
        }
    }
}