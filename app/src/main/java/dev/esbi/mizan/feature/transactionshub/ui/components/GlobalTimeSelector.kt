package dev.esbi.mizan.feature.transactionshub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Global Time Selector Component
 * Centered month/year selector with navigation arrows
 * Shows contextual information about the selected period
 */
@Composable
fun GlobalTimeSelector(
    currentMonth: YearMonth,
    daysWithTransactions: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onMonthSelected: (YearMonth) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var popupDisplayedYear by remember(currentMonth) { mutableIntStateOf(currentMonth.year) }

    val monthNames =
        listOf("Yan", "Fev", "Mar", "Apr", "May", "Iyun", "Iyul", "Avg", "Sen", "Okt", "Noy", "Dek")

    val monthFormatter = remember {
        DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
    }

    val contextText = when {
        daysWithTransactions > 0 -> "$daysWithTransactions day${if (daysWithTransactions != 1) "s" else ""} with transactions"
        else -> "No transactions"
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
                .padding(horizontal = MizanTheme.premium.spacing.md, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Previous Month Button
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MizanTheme.premium.colors.surface2)
                    .border(1.dp, MizanTheme.premium.glass.border, CircleShape)
                    .clickable { onPreviousMonth() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(id = Icons.ic_chevron_left),
                    contentDescription = "Previous month",
                    tint = MizanTheme.premium.text.secondary,
                    modifier = Modifier.size(18.dp)
                )
            }
            val interactionSource = remember { MutableInteractionSource() }
            // Month/Year Display
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { isExpanded = true }
                    .padding(horizontal = MizanTheme.premium.spacing.md)
            ) {
                Text(
                    text = currentMonth.format(monthFormatter),
                    style = MizanTheme.typography.headingMd,
                    color = MizanTheme.premium.text.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = contextText,
                    style = MizanTheme.typography.labelSm,
                    color = MizanTheme.premium.text.tertiary
                )
            }

            // Next Month Button
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MizanTheme.premium.colors.surface2)
                    .border(1.dp, MizanTheme.premium.glass.border, CircleShape)
                    .clickable { onNextMonth() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(id = Icons.ic_chevron_right),
                    contentDescription = "Next month",
                    tint = MizanTheme.premium.text.secondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // 2. POPUP MENU (Click bo'lganda tagidan ochiladi)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp)
                .background(color = Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier
                    .background(MizanTheme.premium.background.secondary)
                    .padding(MizanTheme.premium.spacing.sm)
            ) {
                // -- YIL NAVIGATSIYASI (Popup ichida) --
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MizanTheme.premium.spacing.sm),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { popupDisplayedYear-- }) {
                        MizanIcon(
                            icon = IconValue(Icons.ic_chevron_left),
                            contentDescription = "Oldingi yil",
                            tint = MizanTheme.premium.text.primary
                        )
                    }
                    Text(
                        text = popupDisplayedYear.toString(),
                        style = MizanTheme.typography.headingLg,
                        color = MizanTheme.premium.text.primary,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { popupDisplayedYear++ }) {
                        MizanIcon(
                            icon = IconValue(Icons.ic_chevron_right),
                            contentDescription = "Keyingi yil",
                            tint = MizanTheme.premium.text.primary
                        )
                    }
                }

                // -- 4x3 GRID OYLAR --
                Column {
                    for (row in 0..2) { // 3 ta qator (0, 1, 2)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (col in 0..3) { // 4 ta ustun (0, 1, 2, 3)
                                // Index endi row * 4 + col orqali topiladi
                                val monthIndex = row * 4 + col
                                val monthNumber = monthIndex + 1
                                val isSelected =
                                    (currentMonth.year == popupDisplayedYear && currentMonth.monthValue == monthNumber)

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(MizanTheme.premium.radius.sm))
                                        .background(
                                            // Tanlangan bo'lsa MizanTheme error rangi (qizil) shaffof qilib beriladi
                                            if (isSelected) MizanTheme.premium.colors.error.copy(
                                                alpha = 0.15f
                                            )
                                            else Color.Transparent
                                        )
                                        .clickable {
                                            onMonthSelected(
                                                YearMonth.of(
                                                    popupDisplayedYear,
                                                    monthNumber
                                                )
                                            )
                                            isExpanded = false
                                        }
                                        .padding(
                                            vertical = MizanTheme.premium.spacing.sm,
                                            horizontal = MizanTheme.premium.spacing.md
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = monthNames[monthIndex],
                                        style = MizanTheme.typography.bodyMd,
                                        // Qizil (error) yoki standart matn rangi
                                        color = if (isSelected) MizanTheme.premium.colors.error else MizanTheme.premium.text.primary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
