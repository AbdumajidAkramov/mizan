package dev.esbi.mizan.feature.newtransaction.categoryselect

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.R
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
fun CategorySelectionSheet(
    isVisible: Boolean,
    categories: List<Category>,
    selectedParentId: Long?,
    selectedChildId: Long?,
    onParentSelected: (Category) -> Unit,
    onChildSelected: (Category) -> Unit,
    onBack: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!isVisible) return

    var searchQuery by remember { mutableStateOf("") }
    var showSubcategories by remember { mutableStateOf(false) }
    var selectedParent by remember { mutableStateOf<Category?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Update subcategory view when parent is selected
    LaunchedEffect(selectedParentId) {
        Log.d("CategorySelectionSheet", "categories: $categories")
        selectedParent = categories.find { it.id == selectedParentId }
        showSubcategories = selectedParentId != null
    }

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
                    .widthIn(max = 400.dp)
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.xxl))
                    .background(MizanTheme.premium.background.primary)
                    .padding(MizanTheme.premium.spacing.lg)
            ) {
                AnimatedContent(
                    targetState = showSubcategories,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith 
                        fadeOut(animationSpec = tween(300))
                    },
                    label = "content_animation"
                ) { showSub ->
                    if (showSub) {
                        // Subcategory View
                        selectedParent?.let { parent ->
                            Column {
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
                                            .clickable { 
                                                showSubcategories = false
                                                onBack()
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_arrow_back),
                                            contentDescription = "Back",
                                            tint = MizanTheme.premium.text.secondary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))
                                    
                                    // Parent Name
                                    Text(
                                        text = parent.name,
                                        style = MizanTheme.typography.headingMd,
                                        color = MizanTheme.premium.text.primary,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(4.dp))
                                
                                Text(
                                    text = "Choose a subcategory (optional)",
                                    style = MizanTheme.typography.bodySm,
                                    color = MizanTheme.premium.text.tertiary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 40.dp)
                                )
                                
                                Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg))
                                
                                // Subcategory List
                                val subcategories = categories
                                    .filter { it.parentId == parent.id }
                                    .sortedBy { it.orderIndex }
                                
                                // No Subcategory Option
                                CategoryItem(
                                    label = "No Subcategory",
                                    description = "Skip subcategory selection",
                                    isSelected = selectedChildId == null,
                                    onClick = {
                                        onChildSelected(parent)
                                        onDismiss()
                                    }
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                // Subcategories
                                subcategories.forEach { subcategory ->
                                    CategoryItem(
                                        label = subcategory.name,
                                        description = "",
                                        isSelected = selectedChildId == subcategory.id,
                                        onClick = {
                                            onChildSelected(subcategory)
                                            onDismiss()
                                        }
                                    )
                                    
                                    if (subcategory != subcategories.last()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                            }
                        }
                    } else {
                        // Parent Category View
                        Column {
                            // Header
                            Text(
                                text = "Select Category",
                                style = MizanTheme.typography.headingMd,
                                color = MizanTheme.premium.text.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            Text(
                                text = "Choose a category for this transaction",
                                style = MizanTheme.typography.bodySm,
                                color = MizanTheme.premium.text.tertiary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg))
                            
                            // Search Bar
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(
                                        text = "Search categories...",
                                        style = MizanTheme.typography.bodyMd,
                                        color = MizanTheme.premium.text.tertiary
                                    )
                                },
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = MizanTheme.premium.colors.surface2,
                                    focusedContainerColor = MizanTheme.premium.colors.surface2,
                                    unfocusedTextColor = MizanTheme.premium.text.primary,
                                    focusedTextColor = MizanTheme.premium.text.primary,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedIndicatorColor = MizanTheme.premium.colors.emerald,
                                    cursorColor = MizanTheme.premium.colors.emerald
                                ),
                                shape = RoundedCornerShape(MizanTheme.premium.radius.xl),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Search
                                ),
                                keyboardActions = KeyboardActions(
                                    onSearch = { keyboardController?.hide() }
                                )
                            )
                            
                            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg))
                            
                            // Category List
                            val parentCategories = categories
                                .filter { it.parentId == null }
                                .filter { 
                                    searchQuery.isEmpty() || 
                                    it.name.contains(searchQuery, ignoreCase = true) 
                                }
                                .sortedBy { it.orderIndex }
                            
                            parentCategories.forEach { category ->
                                val hasChildren = categories.any { it.parentId == category.id }
                                
                                CategoryItem(
                                    label = category.name,
                                    description = if (hasChildren) "Has subcategories" else "",
                                    isSelected = selectedParentId == category.id && selectedChildId == null,
                                    onClick = {
                                        if (hasChildren) {
                                            showSubcategories = true
                                        } else {
                                            onParentSelected(category)
                                            onDismiss()
                                        }
                                    }
                                )
                                
                                if (category != parentCategories.last()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryItem(
    label: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(200),
        label = "scale"
    )

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
                isPressed = true
                onClick()
                isPressed = false
            }
            .padding(MizanTheme.premium.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
    ) {
        // Text Column
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                style = MizanTheme.typography.bodyMd,
                color = if (isSelected) MizanTheme.premium.colors.emerald 
                       else MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Medium
            )
            
            if (description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = description,
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary
                )
            }
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
