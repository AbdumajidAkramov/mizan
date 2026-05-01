package dev.esbi.mizan.feature.newtransaction.inputtypes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionHandler(
    permission: String,
    permissionTitle: String,
    permissionDescription: String,
    onPermissionGranted: @Composable () -> Unit,
    onPermissionDenied: @Composable (String) -> Unit = { },
    modifier: Modifier = Modifier,
) {
    val permissionState = rememberPermissionState(permission)

    when {
        permissionState.status.isGranted -> {
            onPermissionGranted()
        }

        permissionState.status.shouldShowRationale -> {
            // Show rationale
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MizanTheme.premium.spacing.lg)
                    .then(modifier),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Permission Required",
                    style = MizanTheme.typography.headingLg,
                    color = MizanTheme.premium.text.primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(MizanTheme.premium.spacing.md))

                Text(
                    text = permissionDescription,
                    style = MizanTheme.typography.bodyMd,
                    color = MizanTheme.premium.text.secondary,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(MizanTheme.premium.spacing.lg))

                Button(
                    onClick = { permissionState.launchPermissionRequest() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MizanTheme.premium.colors.primary
                    )
                ) {
                    Text(
                        text = "Grant Permission",
                        style = MizanTheme.typography.labelLg,
                        color = Color.White
                    )
                }
            }
        }

        else -> {
            // Permission denied permanently
            onPermissionDenied(permission)
        }
    }
}

@Composable
fun PermissionDeniedScreen(
    permission: String,
    onRequestAgain: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MizanTheme.premium.spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Permission Denied",
            style = MizanTheme.typography.headingLg,
            color = MizanTheme.premium.colors.error,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(MizanTheme.premium.spacing.md))

        Text(
            text = "This feature requires $permission permission to work. Please enable it in app settings.",
            style = MizanTheme.typography.bodyMd,
            color = MizanTheme.premium.text.secondary,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(MizanTheme.premium.spacing.lg))

        Button(
            onClick = onRequestAgain,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MizanTheme.premium.colors.primary
            )
        ) {
            Text(
                text = "Request Again",
                style = MizanTheme.typography.labelLg,
                color = Color.White
            )
        }
    }
}
