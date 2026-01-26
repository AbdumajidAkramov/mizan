package dev.esbi.mizan.feature.newtransaction.categorychooser

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
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


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MizanTheme.premium.spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(MizanTheme.premium.spacing.lg))
        Text(
            "Amount",
            style = MizanTheme.typography.bodySm,
            color = MizanTheme.premium.text.tertiary
        )
        Text(
            amount.annotatedString(),
            style = MizanTheme.typography.displayMd,
            color = MizanTheme.premium.text.primary
        )
        Spacer(Modifier.weight(1f))
        Text(
            "Choose a category",
            style = MizanTheme.typography.bodySm,
            color = MizanTheme.premium.text.tertiary
        )

        Spacer(Modifier.height(32.dp))

        Box(
            modifier = Modifier
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
                    CategoryGrid(
                        state = state,
                        onCategoryClick = { category ->
                            if (state.selectedParentId != null) {
                                accept(
                                    NewTransactionStore.CategoryChooserIntent.SelectSubCategory(
                                        category
                                    )
                                )
                            } else {
                                accept(
                                    NewTransactionStore.CategoryChooserIntent.SelectParentCategory(
                                        category
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }

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

@Composable
internal fun CategoryGrid(
    state: CategoryChooserState,
    onCategoryClick: (Category) -> Unit
) {
    val currentCategories = state.currentCategories

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(currentCategories) { category ->
            CategoryItem(
                category = category,
                onClick = { onCategoryClick(category) }
            )
        }
    }
}


@Composable
internal fun CategoryItem(
    category: Category,
    onClick: () -> Unit
) {
    val neonColor = getCategoryColor(category.name)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        color = MizanTheme.premium.colors.surface2
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon - larger, no background
            MizanIcon(
                icon = getIcon(category.iconName),
                modifier = Modifier.size(36.dp),
                tint = neonColor
            )

            Spacer(Modifier.height(12.dp))

            // Label - muted white
            Text(
                text = category.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MizanTheme.premium.text.secondary,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
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

