package dev.esbi.mizan.feature.managecategories.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.R
import dev.esbi.mizan.feature.managecategories.store.ManageCategoriesStore
import dev.esbi.mizan.ui.theme.colors.MizanTheme

/**
 * Add/Edit Category Bottom Sheet
 * 
 * Modal bottom sheet for creating or editing categories
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCategorySheet(
    isVisible: Boolean,
    editingCategory: ManageCategoriesStore.CategoryItem?,
    onDismiss: () -> Unit,
    onSave: (ManageCategoriesStore.CategoryItem) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = MizanTheme.premium.background.primary,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = MizanTheme.premium.spacing.sm)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(MizanTheme.premium.text.tertiary.copy(alpha = 0.4f))
                )
            }
        ) {
            AddEditCategoryContent(
                editingCategory = editingCategory,
                onSave = onSave,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
private fun AddEditCategoryContent(
    editingCategory: ManageCategoriesStore.CategoryItem?,
    onSave: (ManageCategoriesStore.CategoryItem) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember(editingCategory?.name) { 
        mutableStateOf(editingCategory?.name ?: "") 
    }
    var selectedIcon by remember(editingCategory?.iconName) { 
        mutableStateOf(editingCategory?.iconName ?: "ic_category") 
    }
    var selectedColor by remember(editingCategory?.color) { 
        mutableStateOf(editingCategory?.color ?: "#10B981") 
    }
    var selectedType by remember(editingCategory?.type) { 
        mutableStateOf(editingCategory?.type ?: "EXPENSE") 
    }

    val isEditing = editingCategory != null
    val isValid = name.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MizanTheme.premium.spacing.lg)
    ) {
        // Header
        Text(
            text = if (isEditing) "Edit Category" else "Add New Category",
            style = MizanTheme.typography.headingMd,
            color = MizanTheme.premium.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.xl))

        // Form Fields
        Column(
            verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.lg)
        ) {
            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { 
                    Text(
                        "Category Name",
                        style = MizanTheme.typography.bodySm,
                        color = MizanTheme.premium.text.secondary
                    ) 
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                ),
                shape = RoundedCornerShape(MizanTheme.premium.radius.md)
            )

            // Type Selection
            Text(
                text = "Category Type",
                style = MizanTheme.typography.bodyMd,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Medium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
            ) {
                CategoryTypeButton(
                    text = "Expense",
                    isSelected = selectedType == "EXPENSE",
                    onClick = { selectedType = "EXPENSE" },
                    modifier = Modifier.weight(1f)
                )
                CategoryTypeButton(
                    text = "Income",
                    isSelected = selectedType == "INCOME",
                    onClick = { selectedType = "INCOME" },
                    modifier = Modifier.weight(1f)
                )
            }

            // Icon Selection
            Text(
                text = "Icon",
                style = MizanTheme.typography.bodyMd,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Medium
            )

            IconSelector(
                selectedIcon = selectedIcon,
                onIconSelected = { selectedIcon = it }
            )

            // Color Selection
            Text(
                text = "Color",
                style = MizanTheme.typography.bodyMd,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Medium
            )

            ColorSelector(
                selectedColor = selectedColor,
                onColorSelected = { selectedColor = it }
            )
        }

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.xl))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
        ) {
            // Cancel Button
            Button(
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(MizanTheme.premium.radius.lg),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MizanTheme.premium.colors.surface2,
                    contentColor = MizanTheme.premium.text.primary
                )
            ) {
                Text(
                    text = "Cancel",
                    style = MizanTheme.typography.bodyMd,
                    fontWeight = FontWeight.Medium
                )
            }

            // Save Button
            Button(
                onClick = {
                    if (isValid) {
                        val category = ManageCategoriesStore.CategoryItem(
                            id = editingCategory?.id ?: 0L,
                            name = name.trim(),
                            iconName = selectedIcon,
                            color = selectedColor,
                            type = selectedType,
                            order = editingCategory?.order ?: 0,
                            isSubcategory = editingCategory?.isSubcategory ?: false,
                            parentCategoryId = editingCategory?.parentCategoryId,
                            subcategories = editingCategory?.subcategories ?: emptyList()
                        )
                        onSave(category)
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = isValid,
                shape = RoundedCornerShape(MizanTheme.premium.radius.lg),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MizanTheme.premium.colors.emerald,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (isEditing) "Update" else "Create",
                    style = MizanTheme.typography.bodyMd,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg))
    }
}

@Composable
private fun CategoryTypeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(MizanTheme.premium.radius.md))
            .background(
                if (isSelected) MizanTheme.premium.colors.emerald 
                else MizanTheme.premium.colors.surface2
            )
            .clickable { onClick() }
            .padding(vertical = MizanTheme.premium.spacing.md),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MizanTheme.typography.bodySm,
            color = if (isSelected) Color.White else MizanTheme.premium.text.primary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun IconSelector(
    selectedIcon: String,
    onIconSelected: (String) -> Unit
) {
    val icons = listOf(
        "ic_category", "ic_shopping", "ic_food", "ic_transport",
        "ic_entertainment", "ic_health", "ic_education", "ic_home",
        "ic_work", "ic_travel", "ic_gift", "ic_other"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(6),
        horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm),
        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm),
        modifier = Modifier.height(120.dp)
    ) {
        items(icons) { icon ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (selectedIcon == icon) MizanTheme.premium.colors.emerald.copy(alpha = 0.2f)
                        else MizanTheme.premium.colors.surface2
                    )
                    .border(
                        width = if (selectedIcon == icon) 2.dp else 1.dp,
                        color = if (selectedIcon == icon) MizanTheme.premium.colors.emerald
                        else MizanTheme.premium.glass.border,
                        shape = CircleShape
                    )
                    .clickable { onIconSelected(icon) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(R.drawable.ic_category),
                    contentDescription = null,
                    tint = if (selectedIcon == icon) MizanTheme.premium.colors.emerald
                    else MizanTheme.premium.text.secondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ColorSelector(
    selectedColor: String,
    onColorSelected: (String) -> Unit
) {
    val colors = listOf(
        "#10B981", "#3B82F6", "#F59E0B", "#EF4444", "#8B5CF6",
        "#EC4899", "#14B8A6", "#F97316", "#6366F1", "#84CC16"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(10),
        horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.xs),
        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.xs),
        modifier = Modifier.height(60.dp)
    ) {
        items(colors) { color ->
            val colorValue = try {
                Color(android.graphics.Color.parseColor(color))
            } catch (e: Exception) {
                MizanTheme.premium.colors.emerald
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(colorValue)
                    .border(
                        width = if (selectedColor == color) 3.dp else 1.dp,
                        color = if (selectedColor == color) MizanTheme.premium.text.primary
                        else MizanTheme.premium.glass.border,
                        shape = CircleShape
                    )
                    .clickable { onColorSelected(color) },
                contentAlignment = Alignment.Center
            ) {
                if (selectedColor == color) {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(R.drawable.ic_check),
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
