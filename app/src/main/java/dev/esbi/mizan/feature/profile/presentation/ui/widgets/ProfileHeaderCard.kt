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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.R
import dev.esbi.mizan.domain.model.profile.UserProfile
import dev.esbi.mizan.ui.theme.TextWhite
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.theme.shadows.premiumShadow

@Composable
internal fun ProfileHeaderCard(
    profile: UserProfile
) {
    // Profile Header Card
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .premiumShadow(MizanTheme.premium.shadows.md)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF667EEA),
                        Color(0xFF764BA2),
                        Color(0xFFF5576C)
                    )
                )
            )
            .padding(24.dp)
    ) {
        // Background orbs
        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.TopEnd)
                .background(Color.White.copy(alpha = 0.1f), CircleShape)
                .blur(50.dp)
        )
        Box(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.BottomStart)
                .background(Color.Black.copy(alpha = 0.1f), CircleShape)
                .blur(40.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = profile.avatarInitials,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Info
            Column {
                Text(
                    text = profile.name,
                    style = MizanTheme.typography.headingXl,
                    color = TextWhite,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.profile_premium_member),
                    style = MizanTheme.typography.bodyMd,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoBadge(
                        label = stringResource(R.string.profile_member_since),
                        value = profile.memberSince
                    )
                    InfoBadge(
                        label = stringResource(R.string.profile_transactions),
                        value = profile.totalTransactions.toString()
                    )
                }
            }
        }
    }
}
