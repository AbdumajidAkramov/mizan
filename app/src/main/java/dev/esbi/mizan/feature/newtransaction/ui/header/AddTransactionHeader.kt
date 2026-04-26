package dev.esbi.mizan.feature.newtransaction.ui.header
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionHeader(
    showTemplates: Boolean,
    isEditMode: Boolean,
    onTemplatesToggle: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = if (isEditMode) "Edit Transaction" else "New Transaction",
                style = MizanTheme.premium.typography.headingSm,
                color = MizanTheme.premium.text.primary
            )
        },
        navigationIcon = {
            IconButton(onClick = onClose) {
                MizanIcon(
                    icon = IconValue(Icons.ic_arrow_back),
                    contentDescription = "Back",
                    tint = MizanTheme.premium.text.primary
                )
            }
        },
/*
        actions = {
            Row(
                modifier = Modifier.padding(end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Templates Toggle Button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            if (showTemplates) MizanTheme.premium.colors.emerald
                            else MizanTheme.premium.colors.surface2
                        )
                        .clickable { onTemplatesToggle() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = Icons.ic_wand_sparkles),
                        contentDescription = "Templates",
                        tint = if (showTemplates) Color.White
                        else MizanTheme.premium.text.secondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
*/
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}
