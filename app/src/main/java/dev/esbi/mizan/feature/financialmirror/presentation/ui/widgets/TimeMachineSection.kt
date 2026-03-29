package dev.esbi.mizan.feature.financialmirror.presentation.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.presentation.feature.financialmirror.domain.model.TimeMachineScenario
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons

@Composable
internal fun TimeMachineSection(scenarios: List<TimeMachineScenario>) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MizanIcon(
                icon = IconValue(Icons.ic_schedule),
                modifier = Modifier.size(24.dp),
                tint = MizanTheme.premium.colors.primary
            )
            Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
            Text(
                "Time Machine",
                style = MizanTheme.premium.typography.headingSm,
                color = MizanTheme.premium.text.primary
            )
            Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.sm))
                    .background(MizanTheme.premium.gradients.primary)
                    .padding(
                        horizontal = MizanTheme.premium.spacing.sm,
                        vertical = MizanTheme.premium.spacing.xs
                    )
            ) {
                Text(
                    "What If?",
                    style = MizanTheme.premium.typography.labelSm,
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(MizanTheme.premium.spacing.md))

        scenarios.forEach { scenario ->
            ScenarioCard(scenario)
            Spacer(Modifier.height(MizanTheme.premium.spacing.sm))
        }
    }
}

