package dev.esbi.mizan.feature.newtransaction.categorychooser

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore.CategoryChooserIntent.SelectParentCategory
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore.CategoryChooserIntent.SelectSubCategory
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.TextWhite
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.utils.annotatedString

// Neon category colors
private object CategoryColors {
    val FoodPink = Color(0xFFFF6B9D)
    val TransportBlue = Color(0xFF4DA6FF)
    val ShoppingOrange = Color(0xFFFFAA5C)
    val BillsCyan = Color(0xFF4DD4E8)
    val EntertainmentPurple = Color(0xFFB366FF)
    val HealthCoral = Color(0xFFFF6B8A)
    val TravelIndigo = Color(0xFF7B8CFF)
    val TechnologyGreen = Color(0xFF4DDFB8)
    val Default = Color(0xFF8B8B9E)
}

// Card background color - dark slate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChooserContent(
    amount: String,
    state: CategoryChooserState,
    accept: (NewTransactionStore.Intent) -> Unit
) {
    val tintColor = when (state.transactionType) {
        Transaction.Type.INCOME -> MizanTheme.premium.colors.emerald
        else -> CategoryColors.FoodPink
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MizanTheme.premium.spacing.lg)
            .padding(top = MizanTheme.premium.spacing.lg, bottom = MizanTheme.premium.spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header

        Text(
            text = state.transactionType.name.lowercase().replaceFirstChar { it.uppercase() },
            style = MizanTheme.premium.typography.labelMd,
            color = tintColor
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = amount.annotatedString(),
            style = MizanTheme.premium.typography.displayMd,
            color = tintColor
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Choose a category",
            style = MizanTheme.premium.typography.bodySm,
            color = MizanTheme.premium.text.tertiary
        )

        Spacer(Modifier.height(20.dp))

        // Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when {
                state.isLoading -> {
                    LoadingView()
                }

                state.error != null -> {
                    ErrorView(
                        error = state.error,
                        onRetry = {
                            accept(NewTransactionStore.CategoryChooserIntent.RetryLoad)
                        }
                    )
                }

                else -> {
                    CategoryAccordionList(
                        state = state,
                        selectionTint = tintColor,
                        onParentClick = { parent ->
                            accept(SelectParentCategory(parent))
                        },
                        onSubCategoryClick = { sub ->
                            accept(SelectSubCategory(sub))
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Footer
        Button(
            onClick = { accept(NewTransactionStore.CategoryChooserIntent.Continue) },
            enabled = state.selectedCategory != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = tintColor,
                disabledContainerColor = MizanTheme.premium.colors.surface2,
                contentColor = MizanTheme.premium.text.primary,
                disabledContentColor = MizanTheme.premium.text.tertiary
            )
        ) {
            Text(
                text = "Continue",
                style = MizanTheme.premium.typography.labelLg,
                color = TextWhite
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoryAccordionList(
    state: CategoryChooserState,
    selectionTint: Color,
    onParentClick: (Category) -> Unit,
    onSubCategoryClick: (Category) -> Unit
) {
    val parents = state.mainCategories
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(parents, key = { it.id }) { parent ->
            val subCategories = state.categories
                .filter { it.parentId == parent.id }
                .sortedBy { it.orderIndex }

            val isSelected = state.selectedParentId == parent.id

            val expanded = state.selectedParentId == parent.id && subCategories.isNotEmpty()

            val parentModifier = if (isSelected) {
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        color = selectionTint.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable {
                        onParentClick(parent)
                    }
                    .padding(2.dp)
                    .animateContentSize()
            } else {
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
                        onParentClick(parent)
                    }
                    .animateContentSize()
            }

            Surface(
                modifier = parentModifier,
                color = if (isSelected) {
                    selectionTint.copy(alpha = 0.15f)
                } else {
                    MizanTheme.premium.colors.surface2
                },
                shape = RoundedCornerShape(20.dp),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                border = if (isSelected) {
                    androidx.compose.foundation.BorderStroke(2.dp, selectionTint)
                } else {
                    null
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = parent.name,
                        style = MizanTheme.premium.typography.bodyMd,
                        color = if (isSelected) selectionTint else MizanTheme.premium.text.primary,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (expanded) {
                        Spacer(Modifier.height(10.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            subCategories.forEach { sub ->
                                SubCategoryChip(
                                    label = sub.name,
                                    selected = state.selectedCategory?.id == sub.id,
                                    selectionTint = selectionTint,
                                    onClick = { onSubCategoryClick(sub) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SubCategoryChip(
    label: String,
    selected: Boolean,
    selectionTint: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .clickable { onClick() },
        color = if (selected) selectionTint else MizanTheme.premium.colors.surface3,
        shape = RoundedCornerShape(999.dp),
        border = null
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MizanTheme.premium.typography.labelMd,
            color = if (selected) TextWhite else MizanTheme.premium.text.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
internal fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = MizanTheme.premium.colors.primary,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Loading categories...",
                style = MizanTheme.premium.typography.bodyMd,
                color = MizanTheme.premium.text.secondary
            )
        }
    }
}

@Composable
internal fun ErrorView(
    error: String,
    onRetry: () -> Unit
) {
    Log.d("ErrorView", "Error: $error")
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Oops!",
                style = MizanTheme.premium.typography.headingLg,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Bold
            )

            // Matn qismi vertikal skroll bo'ladigan qilindi
            Box(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = error,
                    style = MizanTheme.premium.typography.bodyMd,
                    color = MizanTheme.premium.text.secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()) // Skroll qo'shish
                        .padding(horizontal = 8.dp)
                )
            }


            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MizanTheme.premium.colors.primary
                )
            ) {
                Text(
                    text = "Retry",
                    style = MizanTheme.premium.typography.labelMd,
                    color = Color.White
                )
            }
        }
    }
}


// Helper function to get neon color based on category name
private fun getCategoryColor(categoryName: String): Color {
    val name = categoryName.lowercase()
    return when {
        name.contains("food") || name.contains("dining") || name.contains("restaurant") -> CategoryColors.FoodPink
        name.contains("transport") || name.contains("car") || name.contains("taxi") || name.contains(
            "bus"
        ) -> CategoryColors.TransportBlue

        name.contains("shopping") || name.contains("shop") -> CategoryColors.ShoppingOrange
        name.contains("bill") || name.contains("utilit") -> CategoryColors.BillsCyan
        name.contains("entertainment") || name.contains("movie") || name.contains("game") -> CategoryColors.EntertainmentPurple
        name.contains("health") || name.contains("fitness") || name.contains("medical") -> CategoryColors.HealthCoral
        name.contains("travel") || name.contains("flight") || name.contains("vacation") -> CategoryColors.TravelIndigo
        name.contains("tech") || name.contains("electronic") || name.contains("phone") -> CategoryColors.TechnologyGreen
        else -> CategoryColors.Default
    }
}

// Helper function to map icon names to actual icon resources
private fun getIcon(iconName: String): IconValue {
    return when (iconName) {
        "restaurant" -> IconValue(dev.esbi.mizan.ui.utils.Icons.ic_home)
        "directions_car" -> IconValue(dev.esbi.mizan.ui.utils.Icons.ic_home)
        "shopping_bag" -> IconValue(dev.esbi.mizan.ui.utils.Icons.ic_home)
        "movie" -> IconValue(dev.esbi.mizan.ui.utils.Icons.ic_home)
        "favorite" -> IconValue(dev.esbi.mizan.ui.utils.Icons.ic_home)
        "lunch_dining" -> IconValue(dev.esbi.mizan.ui.utils.Icons.ic_home)
        "coffee" -> IconValue(dev.esbi.mizan.ui.utils.Icons.ic_home)
        "local_taxi" -> IconValue(dev.esbi.mizan.ui.utils.Icons.ic_home)
        "directions_bus" -> IconValue(dev.esbi.mizan.ui.utils.Icons.ic_home)
        "work" -> IconValue(dev.esbi.mizan.ui.utils.Icons.ic_home)
        "computer" -> IconValue(dev.esbi.mizan.ui.utils.Icons.ic_home)
        "trending_up" -> IconValue(dev.esbi.mizan.ui.utils.Icons.ic_home)
        "receipt" -> IconValue(dev.esbi.mizan.ui.utils.Icons.ic_home)
        "flight" -> IconValue(dev.esbi.mizan.ui.utils.Icons.ic_home)
        "smartphone" -> IconValue(dev.esbi.mizan.ui.utils.Icons.ic_home)
        "fitness_center" -> IconValue(dev.esbi.mizan.ui.utils.Icons.ic_home)
        else -> IconValue(dev.esbi.mizan.ui.utils.Icons.ic_home)
    }
}

