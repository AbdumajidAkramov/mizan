package dev.esbi.mizan.ui.kit.atoms

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

enum class CategoryType {
    FOOD,
    TRANSPORT,
    SHOPPING,
    BILLS,
    ENTERTAINMENT,
    HEALTH,
    TRAVEL,
    TECH,
    INCOME
}

enum class IconSize(val dp: Int) {
    SMALL(16),
    MEDIUM(20),
    LARGE(24),
    XLARGE(32)
}

private val CATEGORY_ICON_MAP = mapOf(
    CategoryType.FOOD to android.R.drawable.ic_menu_recent_history,
    CategoryType.TRANSPORT to android.R.drawable.ic_menu_directions,
    CategoryType.SHOPPING to android.R.drawable.ic_search_category_default,
    CategoryType.BILLS to android.R.drawable.ic_menu_agenda,
    CategoryType.ENTERTAINMENT to android.R.drawable.ic_menu_gallery,
    CategoryType.HEALTH to android.R.drawable.ic_menu_add,
    CategoryType.TRAVEL to android.R.drawable.ic_menu_mapmode,
    CategoryType.TECH to android.R.drawable.ic_menu_manage,
    CategoryType.INCOME to android.R.drawable.arrow_up_float
)

@Composable
fun CategoryIcon(
    category: CategoryType,
    modifier: Modifier = Modifier,
    size: IconSize = IconSize.LARGE,
    color: Color? = null
) {
    val iconRes = CATEGORY_ICON_MAP[category] ?: android.R.drawable.ic_menu_help
    
    Icon(
        painter = painterResource(iconRes),
        contentDescription = category.name,
        tint = color ?: Color.Unspecified,
        modifier = modifier.then(Modifier.size(size.dp.dp))
    )
}
