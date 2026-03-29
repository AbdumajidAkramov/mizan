package dev.esbi.mizan.ui.components

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.PremiumColors
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun PremiumCalendar(
    modifier: Modifier = Modifier,
    selectedDate: Calendar = Calendar.getInstance(),
    onSelectDate: (Calendar) -> Unit,
    transactionDates: List<Calendar> = emptyList()
) {
    var currentMonth by remember { mutableStateOf(Calendar.getInstance()) }
    val daysOfWeek = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")

    PremiumCard(
        variant = PremiumCardVariant.Glass,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        currentMonth = (currentMonth.clone() as Calendar).apply {
                            add(Calendar.MONTH, -1)
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(PremiumColors.Surface2)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_media_previous),
                        contentDescription = "Previous month",
                        tint = PremiumColors.TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = SimpleDateFormat(
                        "MMMM yyyy",
                        Locale.getDefault()
                    ).format(currentMonth.time),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PremiumColors.TextPrimary
                )

                IconButton(
                    onClick = {
                        currentMonth = (currentMonth.clone() as Calendar).apply {
                            add(Calendar.MONTH, 1)
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(PremiumColors.Surface2)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_media_next),
                        contentDescription = "Next month",
                        tint = PremiumColors.TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = PremiumColors.TextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            CalendarGrid(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                transactionDates = transactionDates,
                onSelectDate = onSelectDate
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    currentMonth: Calendar,
    selectedDate: Calendar,
    transactionDates: List<Calendar>,
    onSelectDate: (Calendar) -> Unit
) {
    val days = getDaysInMonth(currentMonth)

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(days) { day ->
            if (day == null) {
                Box(modifier = Modifier.aspectRatio(1f))
            } else {
                CalendarDayCell(
                    day = day,
                    isSelected = isSameDay(day, selectedDate),
                    isToday = isSameDay(day, Calendar.getInstance()),
                    hasTransaction = transactionDates.any { isSameDay(it, day) },
                    onClick = { onSelectDate(day) }
                )
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    day: Calendar,
    isSelected: Boolean,
    isToday: Boolean,
    hasTransaction: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(
                when {
                    isSelected -> Brush.horizontalGradient(
                        colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
                    )

                    isToday -> Brush.linearGradient(
                        colors = listOf(
                            PremiumColors.Surface3,
                            PremiumColors.Surface3
                        )
                    )

                    else -> Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent
                        )
                    )
                }
            )
            .clickable(onClick = onClick)
            .then(if (isSelected) Modifier.scale(0.95f) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = day.get(Calendar.DAY_OF_MONTH).toString(),
                fontSize = 12.sp,
                fontWeight = if (isToday) FontWeight.Medium else FontWeight.Normal,
                color = when {
                    isSelected -> Color.White
                    isToday -> PremiumColors.TextPrimary
                    else -> PremiumColors.TextSecondary
                }
            )
            if (hasTransaction && !isSelected) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF667EEA))
                )
            }
        }
    }
}

private fun getDaysInMonth(date: Calendar): List<Calendar?> {
    val year = date.get(Calendar.YEAR)
    val month = date.get(Calendar.MONTH)

    val firstDay = Calendar.getInstance().apply {
        set(year, month, 1)
    }
    val lastDay = Calendar.getInstance().apply {
        set(year, month + 1, 0)
    }

    val daysInMonth = lastDay.get(Calendar.DAY_OF_MONTH)
    val startingDayOfWeek = firstDay.get(Calendar.DAY_OF_WEEK) - 1

    val days = mutableListOf<Calendar?>()

    repeat(startingDayOfWeek) {
        days.add(null)
    }

    for (i in 1..daysInMonth) {
        days.add(Calendar.getInstance().apply {
            set(year, month, i)
        })
    }

    return days
}

private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
            cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
}

@Preview
@Composable
fun PremiumCalendarPreview() {
    MizanTheme {
        PremiumCalendar(
            selectedDate = Calendar.getInstance(),
            onSelectDate = {}
        )
    }
}