package dev.esbi.mizan.feature.addtransaction.presentation.widgets


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.TextWhite
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons
import dev.esbi.mizan.ui.utils.dashedBorder

@Composable
fun PremiumScanInput(
    isScanning: Boolean,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MizanTheme.premium.spacing.xxl),
        contentAlignment = Alignment.Center
    ) {
        if (!isScanning) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MizanTheme.premium.colors.surface2)
                        .clickable(onClick = onStartScan),
                    contentAlignment = Alignment.Center
                ) {
                    MizanIcon(
                        icon = IconValue(Icons.ic_camera_alt),
                        modifier = Modifier.size(48.dp),
                        tint = MizanTheme.premium.text.primary
                    )
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    "Receipt Scanning",
                    style = MizanTheme.typography.headingMd,
                    color = MizanTheme.premium.text.primary
                )
                Text(
                    "Scan a receipt to extract details",
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary
                )
            }
        } else {
            DocScanner(onStopScan)
        }
    }
}

@Composable
private fun DocScanner(
    onStopScan: () -> Unit
) {
    // 1. Animatsiya holatini saqlash
    // 0f = Tepa, 1f = Past
    val scanProgress = remember { Animatable(0f) }
    var isMovingDown by remember { mutableStateOf(true) }

    // 2. Animatsiya tsiklini ishga tushirish
    LaunchedEffect(Unit) {
        while (true) {
            isMovingDown = true
            // Pastga tushish (Tepadan Pastga)
            scanProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
            )

            isMovingDown = false
            // Tepaga chiqish (Pastdan Tepaga)
            scanProgress.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
            )
        }
    }

    // 3. Konteyner va Chizish
    Column() {
        Box(
            modifier = Modifier
                .width(300.dp)
                .fillMaxHeight()
                .heightIn(300.dp, 400.dp)
                .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
                .background(Color.Black.copy(0.1f))
                .dashedBorder(
                    color = MizanTheme.premium.colors.success, // Yashil rang
                    strokeWidth = 2.dp,
                    dashLength = 12.dp, // Chiziq uzunligi
                    gapLength = 8.dp,   // Bo'shliq uzunligi
                    cornerRadius = 24.dp // Radius (radius.xl ga moslab)
                )
        ) {
            // Bizga piksellarda balandlik kerak emas, BiasAlignment orqali hal qilamiz.
            // scanProgress 0..1 orasi, Bias -1..1 orasi.
            // Konvertatsiya: (value * 2) - 1
            val biasY = (scanProgress.value * 2) - 1

            val gradientHeight = 80.dp // Gradient uzunligi
            val lineColor = MizanTheme.premium.colors.success

            // --- SCANNER GROUP (Chiziq + Gradient) ---
            // Bu butun harakatlanuvchi qism (Bias orqali yuradi)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(BiasAlignment(0f, biasY)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isMovingDown) {
                    // A) Pastga yurganda: Gradient TEPADA, Chiziq PASTDA

                    // Gradient (Yuqoridan(Shaffof) -> Pastga(Yashil))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(gradientHeight)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        lineColor.copy(alpha = 0.5f)
                                    )
                                )
                            )
                    )
                    // Asosiy Chiziq
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(lineColor)
                    )
                } else {
                    // B) Tepaga yurganda: Chiziq TEPADA, Gradient PASTDA

                    // Asosiy Chiziq
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(lineColor)
                    )
                    // Gradient (Yuqoridan(Yashil) -> Pastga(Shaffof))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(gradientHeight)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        lineColor.copy(alpha = 0.5f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(56.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MizanTheme.premium.colors.primaryDark.copy(alpha = 0.5f),
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Position receipt within frame",
                    style = MizanTheme.typography.bodySm,
                    color = TextWhite,
                    modifier = Modifier
                )

            }
        }

    }
}

@Preview
@Composable
fun PremiumScanInputPreview() {
    dev.esbi.mizan.ui.theme.MizanTheme() {
        PremiumScanInput(
            isScanning = true,
            onStartScan = {},
            onStopScan = {}
        )
    }
}
