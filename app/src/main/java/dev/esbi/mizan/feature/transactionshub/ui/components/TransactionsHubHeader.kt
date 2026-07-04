package dev.esbi.mizan.feature.transactionshub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.design.kit.icon.IconValue
import dev.esbi.mizan.design.kit.icon.MizanIcon
import dev.esbi.mizan.design.theme.colors.MizanTheme
import dev.esbi.mizan.design.utils.IconRes
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Header component for TransactionsHub screen
 * Contains back button, title, month subtitle, and add transaction FAB
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsHubHeader(
    currentMonth: YearMonth,
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
    TopAppBar(
        title = {
            // Title and Subtitle
            Column(modifier = modifier) {
                Text(
                    text = "Transactions",
                    style = MizanTheme.typography.headingLg,
                    color = MizanTheme.premium.text.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = currentMonth.format(monthFormatter),
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary
                )
            }

        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                MizanIcon(
                    icon = IconValue(IconRes.ic_arrow_back),
                    contentDescription = "Back",
                    tint = MizanTheme.premium.text.primary
                )
            }
        },
        actions = {
            // Add Transaction FAB
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MizanTheme.premium.colors.emerald)
                    .clickable(
                        onClick = {
                            onAddClick()
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Transaction",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MizanTheme.premium.background.primary
        )
    )
}
