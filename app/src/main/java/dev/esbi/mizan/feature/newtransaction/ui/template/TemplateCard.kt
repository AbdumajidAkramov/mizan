package dev.esbi.mizan.feature.newtransaction.ui.template

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.presentation.feature.addtransaction.model.QuickTemplate
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.util.Locale

@Composable
private fun TemplateCard(
    template: QuickTemplate,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(MizanTheme.premium.colors.surface2)
            .border(
                width = 1.dp,
                color = MizanTheme.premium.glass.border,
                shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
            )
            .clickable { onClick() }
            .padding(MizanTheme.premium.spacing.md)
    ) {
        Column {
            // Template Name Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = MizanTheme.premium.spacing.sm)
            ) {
                Text(
                    text = template.name,
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.primary,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            // Amount
            Text(
                text = "$${String.format(Locale.CANADA, "%.2f", template.amount)}",
                style = MizanTheme.typography.headingSm,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Category Info
            Text(
                text = "${template.category} • ${template.subcategory}",
                style = MizanTheme.typography.bodyXs,
                color = MizanTheme.premium.text.tertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Account Info
            Text(
                text = template.accountName,
                style = MizanTheme.typography.bodyXs,
                color = MizanTheme.premium.text.secondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
