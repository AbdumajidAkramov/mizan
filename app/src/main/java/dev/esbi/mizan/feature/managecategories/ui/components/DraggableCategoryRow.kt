package dev.esbi.mizan.feature.managecategories.ui.components

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.R
import dev.esbi.mizan.feature.managecategories.store.ManageCategoriesStore
import dev.esbi.mizan.ui.theme.colors.MizanTheme

/**
 * Draggable Category Row Component
 * 
 * Supports drag-and-drop reordering, expand/collapse for subcategories,
 * and edit/delete actions
 */
@Composable
fun DraggableCategoryRow(
    category: ManageCategoriesStore.CategoryItem,
    index: Int,
    isExpanded: Boolean,
    onEdit: (ManageCategoriesStore.CategoryItem) -> Unit,
    onDelete: (Long) -> Unit,
    onToggleExpand: (Long) -> Unit,
    onMove: (fromIndex: Int, toIndex: Int) -> Unit
) {
    var isDragging by remember { mutableStateOf(false) }
    val dragAlpha by animateFloatAsState(
        targetValue = if (isDragging) 0.5f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "dragAlpha"
    )

    val chevronRotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "chevronRotation"
    )

    val defaultColor = MizanTheme.premium.colors.emerald
    val categoryColor = remember(category.color, defaultColor) {
        try {
            Color(android.graphics.Color.parseColor(category.color))
        } catch (e: Exception) {
            defaultColor
        }
    }

    Column {
        // Main Category Row
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                .background(MizanTheme.premium.colors.surface2)
                .border(
                    width = 1.dp,
                    color = MizanTheme.premium.glass.border,
                    shape = RoundedCornerShape(MizanTheme.premium.radius.lg)
                )
                .clickable { /* Handle row click if needed */ }
                .padding(MizanTheme.premium.spacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
            ) {
                // Drag Handle
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .clickable { 
                            // For now, implement simple move up/down
                            // In a full implementation, this would start drag
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(R.drawable.ic_drag_handle),
                        contentDescription = "Drag to reorder",
                        tint = MizanTheme.premium.text.tertiary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Category Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(MizanTheme.premium.radius.md))
                        .background(
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    categoryColor.copy(alpha = 0.4f),
                                    categoryColor.copy(alpha = 0.2f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Category icon would go here
                    // For now, using a placeholder
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(categoryColor)
                    )
                }

                // Category Info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = category.name,
                        style = MizanTheme.typography.bodyMd,
                        color = MizanTheme.premium.text.primary,
                        fontWeight = FontWeight.Medium
                    )
                    
                    // Show subcategory count if has subcategories
                    if (category.subcategories.isNotEmpty()) {
                        Text(
                            text = "${category.subcategories.size} subcategories",
                            style = MizanTheme.typography.labelSm,
                            color = MizanTheme.premium.text.tertiary
                        )
                    }
                }

                // Expand Button (if has subcategories)
                if (category.subcategories.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MizanTheme.premium.colors.surface3)
                            .clickable { onToggleExpand(category.id) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = androidx.compose.ui.res.painterResource(R.drawable.ic_chevron_right),
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            tint = MizanTheme.premium.text.secondary,
                            modifier = Modifier
                                .size(16.dp)
                                .rotate(chevronRotation)
                        )
                    }
                }

                // Edit Button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MizanTheme.premium.colors.surface3)
                        .clickable { onEdit(category) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(R.drawable.ic_edit),
                        contentDescription = "Edit",
                        tint = MizanTheme.premium.text.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Delete Button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MizanTheme.premium.colors.surface3)
                        .clickable { onDelete(category.id) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(R.drawable.ic_delete),
                        contentDescription = "Delete",
                        tint = MizanTheme.premium.text.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.sm))

        // Subcategories (expanded)
        androidx.compose.animation.AnimatedVisibility(
            visible = isExpanded && category.subcategories.isNotEmpty(),
            enter = androidx.compose.animation.expandVertically(
                animationSpec = tween(durationMillis = 300)
            ),
            exit = androidx.compose.animation.shrinkVertically(
                animationSpec = tween(durationMillis = 300)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(start = MizanTheme.premium.spacing.xl)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
            ) {
                category.subcategories.forEach { subcategory ->
                    SubcategoryRow(
                        subcategory = subcategory,
                        categoryColor = categoryColor,
                        onEdit = { /* Handle subcategory edit */ }
                    )
                }
            }
        }
    }
}

@Composable
private fun SubcategoryRow(
    subcategory: ManageCategoriesStore.CategoryItem,
    categoryColor: Color,
    onEdit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
            .background(MizanTheme.premium.colors.surface1)
            .padding(MizanTheme.premium.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
    ) {
        // Color indicator
        Box(
            modifier = Modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(categoryColor)
        )

        // Subcategory name
        Text(
            text = subcategory.name,
            style = MizanTheme.typography.bodyMd,
            color = MizanTheme.premium.text.secondary,
            modifier = Modifier.weight(1f)
        )

        // Edit button
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(MizanTheme.premium.colors.surface3)
                .clickable { onEdit() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = androidx.compose.ui.res.painterResource(R.drawable.ic_edit),
                contentDescription = "Edit",
                tint = MizanTheme.premium.text.tertiary,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
