package dev.esbi.mizan.feature.profile.presentation.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.R
import dev.esbi.mizan.domain.model.profile.UserProfile
import dev.esbi.mizan.ui.components.PremiumCard
import dev.esbi.mizan.ui.components.PremiumCardVariant
import dev.esbi.mizan.ui.kit.glass.PressCard
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.theme.shadows.premiumShadow
import java.text.NumberFormat
import java.util.Locale

@Composable
internal fun StatsGrid(
    profile: UserProfile,
) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
    }

    // Stats Grid
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            label = stringResource(R.string.profile_income),
            value = numberFormat.format(profile.totalIncome),
            gradient = listOf(Color(0xFF00F2FE), Color(0xFF4FACFE)),
            icon = R.drawable.ic_download,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            label = stringResource(R.string.profile_expenses),
            value = numberFormat.format(profile.totalExpense),
            gradient = listOf(Color(0xFFFF6B6B), Color(0xFFF5576C)),
            icon = R.drawable.ic_share,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            label = stringResource(R.string.profile_saved),
            value = numberFormat.format(profile.totalSaved),
            gradient = listOf(Color(0xFF667EEA), Color(0xFFF5576C)),
            icon = R.drawable.ic_star,
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
internal fun StatCard(
    label: String,
    value: String,
    gradient: List<Color>,
    icon: Int,
    modifier: Modifier = Modifier
) {

    PremiumCard(
        variant = PremiumCardVariant.Glass,
        modifier = modifier.premiumShadow(MizanTheme.premium.shadows.md)
    ) {
        PressCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Brush.linearGradient(gradient),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = label,
                    style = MizanTheme.typography.bodyXs,
                    color = MizanTheme.premium.text.muted,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value,
                    style = MizanTheme.typography.headingSm,
                    color = MizanTheme.premium.text.primary,
                )
            }
        }
    }
}
