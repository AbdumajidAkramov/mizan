package dev.esbi.mizan.ui.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons
import kotlinx.coroutines.delay

enum class MizanToastStatus {
    SUCCESS, ERROR, ATTENTION
}

@Composable
fun getToastAssets(status: MizanToastStatus) = when (status) {
    MizanToastStatus.SUCCESS -> Triple(
        MizanTheme.premium.colors.emerald, // Rang
        Icons.ic_check, // Ikonka
        "Muvaffaqiyatli!" // Default sarlavha
    )

    MizanToastStatus.ERROR -> Triple(
        MizanTheme.premium.colors.error,
        Icons.ic_close,
        "Xatolik!"
    )

    MizanToastStatus.ATTENTION -> Triple(
        MizanTheme.premium.colors.warning,
        Icons.ic_close,
        "Diqqat!"
    )
}

@Composable
fun MizanToastHost(
    message: String,
    status: MizanToastStatus,
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    // Box fillMaxSize butun ekranni egallaydi
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f) // Barcha UI elementlaridan ustida turishi uchun
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter) // Ekranning yuqori markaziga tekislash
                .statusBarsPadding() // Status bar (soat, signal) ostidan boshlanishi uchun
        ) {
            MizanToast(
                message = message,
                status = status,
                isVisible = isVisible,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
fun MizanToast(
    message: String,
    status: MizanToastStatus,
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    val (color, icon, title) = getToastAssets(status)

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
    ) {
        // Avtomatik yopish uchun timer
        LaunchedEffect(isVisible) {
            if (isVisible) {
                delay(3000)
                onDismiss()
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MizanTheme.premium.spacing.lg)
                .graphicsLayer(shadowElevation = 8f, shape = RoundedCornerShape(MizanTheme.premium.radius.lg)),
            shape = RoundedCornerShape(MizanTheme.premium.radius.lg),
            color = MizanTheme.premium.background.primary,
            border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
        ) {
            Row(
                modifier = Modifier
                    .padding(MizanTheme.premium.spacing.md),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
            ) {
                // Status Ikonkasi orqa foni bilan
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    MizanIcon(
                        icon = IconValue(icon),
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Matn qismi
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MizanTheme.typography.labelSm.copy(
                            fontWeight = FontWeight.Bold,
                            color = color
                        )
                    )
                    Text(
                        text = message,
                        style = MizanTheme.typography.bodySm.copy(
                            color = MizanTheme.premium.text.secondary
                        )
                    )
                }

                // Yopish tugmasi
                IconButton(onClick = onDismiss) {
                    MizanIcon(
                        icon = IconValue(Icons.ic_close),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MizanTheme.premium.text.tertiary
                    )
                }
            }
        }
    }
}
/*
@Composable
fun MizanToast(
    message: String,
    status: MizanToastStatus,
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    val (color, icon, title) = getToastAssets(status)

    AnimatedVisibility(
        visible = isVisible,
        // Tepadan silliq tushish (Slide + Fade)
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(animationSpec = tween(300)),
        // Tepaga silliq qaytib chiqib ketish
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(animationSpec = tween(300))
    ) {
        // Avvalgi Surface va UI kodi o'zgarishsiz qoladi...
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MizanTheme.premium.spacing.lg,
                    vertical = MizanTheme.premium.spacing.sm
                )
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(MizanTheme.premium.radius.lg)
                ),
            shape = RoundedCornerShape(MizanTheme.premium.radius.lg),
            color = Color.White
        ) {
            // Toast kontenti (Row, Icon, Text...)

        }
    }
}*/
