package dev.esbi.mizan.feature.dashboard.presentation.widgets.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.design.kit.glass.CardVariant
import dev.esbi.mizan.design.kit.glass.PremiumCard
import dev.esbi.mizan.design.kit.icon.IconValue
import dev.esbi.mizan.design.kit.icon.MizanIcon
import dev.esbi.mizan.design.theme.colors.MizanTheme
import dev.esbi.mizan.design.utils.IconRes
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun PremiumEmergencyFund(
    current: Double,
    goal: Double,
    targetMonths: Int = 6,
    modifier: Modifier = Modifier
) {
    // 0.0 dan 1.0 gacha oraliq (Compose uchun)
    val progressFraction = (current / goal).toFloat().coerceIn(0f, 1f)
    // 0 dan 100 gacha (Matn uchun)
    val percentageDisplay = (progressFraction * 100).roundToInt()

    val remaining = (goal - current).coerceAtLeast(0.0)
    val isComplete = current >= goal

    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }
    currencyFormat.maximumFractionDigits = 0 // Sentlarsiz ko'rsatish (Clean look)

    // Ranglarni aniqlash
    // React kodida "#4facfe" (Cyan/Blue) ishlatilgan. Bu bizning theme'dagi Success gradientiga yaqin.
    val activeColor =
        if (isComplete) MizanTheme.premium.colors.success else MizanTheme.premium.colors.success
    val activeGradient = if (isComplete) {
        // Complete bo'lsa: Success -> Blue
        Brush.horizontalGradient(
            listOf(MizanTheme.premium.colors.success, MizanTheme.premium.colors.primary)
        )
    } else {
        // Progress bo'lsa: Theme'dagi Success Gradient (Cyan -> Blue)
        MizanTheme.premium.gradients.success
    }

    PremiumCard(
        variant = CardVariant.Glass,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            // --- HEADER ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MizanTheme.premium.spacing.md),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Title & Icon
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MizanIcon(
                        icon = IconValue(IconRes.ic_shield), // Shield icon
                        contentDescription = null,
                        tint = activeColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))
                    Text(
                        text = "Emergency Fund",
                        style = MizanTheme.typography.headingMd,
                        color = MizanTheme.premium.text.primary
                    )
                }

                // Months Badge
                Box(
                    modifier = Modifier
                        .background(
                            activeColor.copy(alpha = 0.2f),
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$targetMonths months",
                        style = MizanTheme.typography.bodyXs.copy(fontWeight = FontWeight.Medium),
                        color = activeColor
                    )
                }
            }

            // --- PROGRESS INFO ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MizanTheme.premium.spacing.sm),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom // Baseline bo'yicha
            ) {
                // Left: Amount
                Column {
                    Text(
                        text = currencyFormat.format(current),
                        style = MizanTheme.typography.headingLg,
                        color = MizanTheme.premium.text.primary
                    )
                    Text(
                        text = "of ${currencyFormat.format(goal)} goal",
                        style = MizanTheme.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary
                    )
                }
                // Right: Percentage
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$percentageDisplay%",
                        style = MizanTheme.typography.headingMd,
                        color = activeColor
                    )
                    Text(
                        text = "complete",
                        style = MizanTheme.typography.bodyXs,
                        color = MizanTheme.premium.text.muted
                    )
                }
            }

            // --- CUSTOM PROGRESS BAR ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(CircleShape)
                    .background(MizanTheme.premium.colors.surface2) // Track background
            ) {
                // 1. Asosiy to'ldiruvchi chiziq (Gradient)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progressFraction)
                        .fillMaxHeight()
                        .background(activeGradient)
                )

                // 2. Glow effect (faqat complete bo'lmasa)
                if (!isComplete && progressFraction > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progressFraction)
                            .fillMaxHeight()
                            .blur(4.dp) // Glow effect
                            .background(activeGradient)
                    )
                }

                // 3. Milestones (25%, 50%, 75% da oq chiziqchalar)
                // Bularni absolute positioning orqali joylaymiz
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly // 25, 50, 75 ga to'g'ri keladi
                ) {
                    // SpaceEvenly 4 ta bo'lakka bo'ladi, bizga 3 ta chiziq kerak.
                    // Lekin aniq foiz kerak bo'lsa, Spacer(weight) ishlatgan ma'qul.
                    // React kodida aniq left: 25%, 50%, 75% berilgan.
                }

                // Aniqroq joylashish uchun 3 ta chiziqni qo'lda chizamiz
                listOf(0.25f, 0.5f, 0.75f).forEach { percent ->
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            // Bu Hack: Boxni surish uchun (fillMaxWidth(percent) ishlatib, oxirida chiziq chizamiz)
                            .fillMaxWidth(percent)
                            .fillMaxHeight()
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd) // O'ng chekkasida
                                .width(2.dp)
                                .fillMaxHeight()
                                .background(MizanTheme.premium.colors.surface1.copy(alpha = 0.5f)) // Chiziq rangi
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))

            // --- FOOTER / STATUS MESSAGE ---
            val footerBg = if (isComplete) MizanTheme.premium.colors.success.copy(alpha = 0.1f)
            else activeColor.copy(alpha = 0.1f)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(footerBg, RoundedCornerShape(MizanTheme.premium.radius.md))
                    .padding(MizanTheme.premium.spacing.md),
                verticalAlignment = Alignment.Top
            ) {
                if (isComplete) {
                    MizanIcon(
                        icon = IconValue(IconRes.ic_track_changes), // Target icon
                        contentDescription = null,
                        tint = MizanTheme.premium.colors.success,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))
                    Column {
                        Text(
                            text = "Goal Achieved! 🎉",
                            style = MizanTheme.typography.bodyMd.copy(fontWeight = FontWeight.Medium),
                            color = MizanTheme.premium.colors.success
                        )
                        Text(
                            text = "You're financially protected for $targetMonths months",
                            style = MizanTheme.typography.bodySm,
                            color = MizanTheme.premium.text.tertiary
                        )
                    }
                } else {
                    MizanIcon(
                        icon = IconValue(IconRes.ic_trend_up),
                        contentDescription = null,
                        tint = activeColor,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))
                    Column {
                        Text(
                            text = "${currencyFormat.format(remaining)} remaining",
                            style = MizanTheme.typography.bodyMd.copy(fontWeight = FontWeight.Medium),
                            color = MizanTheme.premium.text.primary
                        )
                        // Tavsiya: 1 yilda yig'ish uchun oyiga qancha
                        val monthlySave = (remaining / 12).roundToInt()
                        Text(
                            text = "Save ~${currencyFormat.format(monthlySave)}/month to reach goal in 1 year",
                            style = MizanTheme.typography.bodySm,
                            color = MizanTheme.premium.text.tertiary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF111827)
@Composable
fun PremiumEmergencyFundPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        PremiumEmergencyFund(
            current = 18500.0,
            goal = 15000.0,
            targetMonths = 6
        )
    }
}

@Preview(name = "Completed", showBackground = true, backgroundColor = 0xFF111827)
@Composable
fun PremiumEmergencyFundCompletedPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        PremiumEmergencyFund(
            current = 15500.0,
            goal = 15000.0,
            targetMonths = 6
        )
    }
}