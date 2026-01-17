package dev.esbi.mizan.ui.kit.glass

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.feature.dashboard.presentation.widgets.HeaderSection
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: CornerBasedShape = RoundedCornerShape(MizanTheme.premium.radius.lg),
    content: @Composable () -> Unit
) {
    Box(
        Modifier
            .clip(cornerRadius)
            .background(MizanTheme.premium.background.secondary.copy(0.95f))
//            .border(
//                1.dp,
//                MizanTheme.premium.glass.border,
//                cornerRadius
//            )
            .then(modifier)
    ) { content() }
}

@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun GlassCardPreview() {
    MizanTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background
                )
        ) {
            GlassCard(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)

            ) {
                HeaderSection()
            }
        }
    }
}
