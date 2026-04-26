package dev.esbi.mizan.feature.addtransaction2.presentation.dialog

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun PremiumDatePickerDialog(
    initialDate: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit
) {
    // Dialog oynasi
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false) // Ekranni to'liq boshqarish uchun
    ) {
        // Dialogning asosiy konteyneri (Kartochka)
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f) // Ekran kengligining 90%ini egallaydi
                .wrapContentHeight(),
            shape = RoundedCornerShape(MizanTheme.premium.radius.xl), // Katta radius
            color = MizanTheme.premium.background.secondary, // To'q fon (Dark mode uchun)
            tonalElevation = 8.dp
        ) {
            CalendarContent(
                initialDate = initialDate,
                onDateSelected = onDateSelected
            )
        }
    }
}

@Composable
private fun CalendarContent(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    // Hozir ko'rilayotgan oy (Default: tanlangan sana yoki bugun)
    var currentMonth by remember { mutableStateOf(YearMonth.from(initialDate)) }
    var selectedDate by remember { mutableStateOf(initialDate) }

    Column(
        modifier = Modifier.padding(MizanTheme.premium.spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- 1. HEADER (Oy va Yil + Navigatsiya) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MizanTheme.premium.spacing.lg),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Orqaga tugmasi
            NavigationButton(
                icon = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                onClick = { currentMonth = currentMonth.minusMonths(1) }
            )

            // Oy nomi (January 2026)
            Text(
                text = "${
                    currentMonth.month.getDisplayName(
                        TextStyle.FULL,
                        Locale.ENGLISH
                    )
                } ${currentMonth.year}",
                style = MizanTheme.typography.headingMd,
                color = MizanTheme.premium.text.primary
            )

            // Oldinga tugmasi
            NavigationButton(
                icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                onClick = { currentMonth = currentMonth.plusMonths(1) }
            )
        }

        // --- 2. HAFTA KUNLARI (Su, Mo, Tu...) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Yakshanbadan boshlanadigan qilib ro'yxat tuzamiz
            val daysOfWeek = listOf(
                DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
            )

            daysOfWeek.forEach { dayOfWeek ->
                Text(
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                        .take(2), // Su, Mo
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.sm))

        // --- 3. KUNLAR GRIDI ---
        // Oyning birinchi kuni qaysi haftaning kuniga to'g'ri kelishini hisoblash
        val firstDayOfMonth = currentMonth.atDay(1)
        // DayOfWeek.MONDAY = 1 ... SUNDAY = 7. 
        // Bizning kalendar Sunday(Yakshanba)dan boshlanadi, shuning uchun offsetni to'g'irlaymiz.
        // Sunday(7) -> 0, Monday(1) -> 1, ...
        val startOffset =
            if (firstDayOfMonth.dayOfWeek == DayOfWeek.SUNDAY) 0 else firstDayOfMonth.dayOfWeek.value

        val daysInMonth = currentMonth.lengthOfMonth()
        val totalCells = startOffset + daysInMonth

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(280.dp), // Grid balandligi
            verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.xs),
            horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.xs)
        ) {
            // Bo'sh kataklar (Oy boshlanishidan oldingi kunlar)
            items(startOffset) {
                Box(modifier = Modifier.size(40.dp)) // Bo'sh joy
            }

            // Haqiqiy kunlar
            items(daysInMonth) { index ->
                val day = index + 1
                val date = currentMonth.atDay(day)
                val isSelected = date == selectedDate
                val isToday = date == LocalDate.now()

                DayItem(
                    day = day,
                    isSelected = isSelected,
                    isToday = isToday,
                    onClick = {
                        selectedDate = date
                        onDateSelected(date)
                    }
                )
            }
        }
    }
}

@Composable
private fun NavigationButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(36.dp)
            .background(MizanTheme.premium.colors.surface2, CircleShape)
            .clip(CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MizanTheme.premium.text.secondary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun DayItem(
    day: Int,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    // Tanlangan holatdagi fon (Gradient yoki Solid rang)
    // Rasmdagi (image_ca570e.png) ko'k/binafsha rang: 0xFF667EEA
    val backgroundColor = if (isSelected) MizanTheme.premium.colors.primary else Color.Transparent
    val textColor = if (isSelected) Color.White else MizanTheme.premium.text.primary

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp)) // Rasmga mos 'Squircle' shakli
            .background(backgroundColor)
            .clickable { onClick() }
            .then(
                // Agar bugungi kun bo'lsa-yu, tanlanmagan bo'lsa, border qo'shamiz (ixtiyoriy)
                if (isToday && !isSelected)
                    Modifier.border(
                        1.dp,
                        MizanTheme.premium.colors.surface3,
                        RoundedCornerShape(12.dp)
                    )
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            style = MizanTheme.typography.bodyMd,
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )
    }
}