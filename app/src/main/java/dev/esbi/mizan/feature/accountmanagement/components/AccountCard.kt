package dev.esbi.mizan.feature.accountmanagement.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.presentation.feature.accountmanagement.store.AccountManagementStore
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

@Composable
fun AccountCard(
    account: AccountManagementStore.AccountItem,
    onTap: () -> Unit,
    onArchive: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accountColor = MizanTheme.premium.colors.emerald

    val currencyFormat = NumberFormat.getNumberInstance(Locale("uz", "UZ")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    val isNegative = account.balance < 0

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(MizanTheme.premium.colors.surface2)
            .border(
                width = 1.dp,
                color = MizanTheme.premium.glass.border,
                shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
            )
            .clickable { onTap() }
            .padding(MizanTheme.premium.spacing.md)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
        ) {
            // Account Icon (first letter of name)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.md))
                    .background(accountColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = account.name.take(1).uppercase(),
                    color = accountColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            // Account Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = account.name,
                    style = MizanTheme.typography.bodyMd.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MizanTheme.premium.text.primary,
                    maxLines = 1
                )

                Text(
                    text = account.groupName,
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary
                )
            }

            // Balance
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "${if (isNegative) "-" else ""}${currencyFormat.format(abs(account.balance))}",
                        style = MizanTheme.premium.typography.headingSm.copy(
                            fontSize = 16.sp
                        ),
                        color = if (isNegative) {
                            Color(0xFFf5576c)
                        } else {
                            MizanTheme.premium.text.primary
                        }
                    )
                }
                Text(
                    text = account.currencyCode,
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Chevron
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MizanTheme.premium.text.tertiary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
