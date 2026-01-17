package dev.esbi.mizan.ui.kit.premium

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.theme.colors.MizanTheme

// ==========================================
// 1. ENUMS (VARIANTS & SIZES)
// ==========================================

enum class ButtonVariant {
    Default,
    Destructive,
    Outline,
    Secondary,
    Ghost,
    Link
}

enum class ButtonSize {
    Default, // h-9 px-4 py-2
    Sm,      // h-8 px-3
    Lg,      // h-10 px-6
    Icon     // size-9
}

// ==========================================
// 2. COMPONENT
// ==========================================

@Composable
fun PremiumButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Default,
    size: ButtonSize = ButtonSize.Default,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    shape: Shape? = null, // Agar null bo'lsa, default radius olinadi
    content: @Composable RowScope.() -> Unit
) {
    // 1. Theme va State'larni aniqlash
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Radius (Reactdagi rounded-md ga moslashamiz -> radius.sm yoki radius.md)
    val buttonShape = shape ?: RoundedCornerShape(MizanTheme.premium.radius.sm)

    // 2. Ranglarni aniqlash (Variantga qarab)
    val colors = getButtonColors(variant)
    val backgroundColor = colors.containerColor
    val contentColor = colors.contentColor
    val border = colors.border

    // 3. O'lchamlarni aniqlash (Size ga qarab)
    val sizeMod = getButtonSizeModifier(size)
    val contentPadding = getButtonPadding(size)

    // 4. Styles (Alpha va Clickable)
    val alpha = if (enabled) {
        if (isPressed) 0.9f else 1f // Reactdagi active:opacity ga o'xshash effekt
    } else {
        0.5f // disabled:opacity-50
    }

    Surface(
        modifier = modifier
            .alpha(alpha)
            .then(sizeMod)
            .clip(buttonShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Ripple o'rniga alpha o'zgarishi (Premium feel) yoki Ripple qo'shish mumkin
                enabled = enabled,
                onClick = onClick,
                role = Role.Button
            ),
        color = if (variant == ButtonVariant.Outline || variant == ButtonVariant.Ghost || variant == ButtonVariant.Link) Color.Transparent else backgroundColor,
        contentColor = contentColor,
        border = border,
        shape = buttonShape
    ) {
        Row(
            modifier = Modifier.padding(contentPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon bo'lsa ko'rsatamiz
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp) // [&_svg]:size-4 (16dp)
                        .padding(end = if (size == ButtonSize.Icon) 0.dp else 8.dp) // gap-2
                )
            }

            // Link varianti uchun underline
            val textStyle = MizanTheme.typography.labelMd.copy(
                textDecoration = if (variant == ButtonVariant.Link && isPressed) TextDecoration.Underline else null
            )

            ProvideTextStyle(value = textStyle) {
                content()
            }
        }
    }
}

// ==========================================
// 3. HELPER FUNCTIONS & DATA
// ==========================================

private data class ButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val border: BorderStroke? = null
)

@Composable
private fun getButtonColors(variant: ButtonVariant): ButtonColors {
    return when (variant) {
        ButtonVariant.Default -> ButtonColors(
            containerColor = MizanTheme.premium.colors.primary,
            contentColor = Color.White // primary-foreground
        )

        ButtonVariant.Destructive -> ButtonColors(
            containerColor = MizanTheme.premium.colors.error,
            contentColor = Color.White
        )

        ButtonVariant.Outline -> ButtonColors(
            containerColor = Color.Transparent,
            contentColor = MizanTheme.premium.text.primary,
            border = BorderStroke(1.dp, MizanTheme.premium.colors.surface4) // border-input
        )

        ButtonVariant.Secondary -> ButtonColors(
            containerColor = MizanTheme.premium.colors.secondary,
            contentColor = Color.White // secondary-foreground
        )

        ButtonVariant.Ghost -> ButtonColors(
            containerColor = Color.Transparent,
            contentColor = MizanTheme.premium.text.primary
        )

        ButtonVariant.Link -> ButtonColors(
            containerColor = Color.Transparent,
            contentColor = MizanTheme.premium.colors.primary
        )
    }
}

@Composable
private fun getButtonSizeModifier(size: ButtonSize): Modifier {
    return when (size) {
        ButtonSize.Default -> Modifier.defaultMinSize(minHeight = 36.dp) // h-9 (36px)
        ButtonSize.Sm -> Modifier.defaultMinSize(minHeight = 32.dp)      // h-8 (32px)
        ButtonSize.Lg -> Modifier.defaultMinSize(minHeight = 40.dp)      // h-10 (40px)
        ButtonSize.Icon -> Modifier.size(36.dp)                          // size-9 (36px)
    }
}

@Composable
private fun getButtonPadding(size: ButtonSize): PaddingValues {
    return when (size) {
        ButtonSize.Default -> PaddingValues(horizontal = 16.dp, vertical = 8.dp) // px-4 py-2
        ButtonSize.Sm -> PaddingValues(horizontal = 12.dp, vertical = 0.dp)      // px-3
        ButtonSize.Lg -> PaddingValues(horizontal = 24.dp, vertical = 0.dp)      // px-8
        ButtonSize.Icon -> PaddingValues(0.dp)
    }
}

// ==========================================
// 4. PREVIEW
// ==========================================

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PremiumButtonPreview() {
    // MizanTheme contextida
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PremiumButton(onClick = {}, variant = ButtonVariant.Default) { Text("Default Button") }
        PremiumButton(onClick = {}, variant = ButtonVariant.Destructive) { Text("Destructive") }
        PremiumButton(onClick = {}, variant = ButtonVariant.Secondary) { Text("Secondary") }
        PremiumButton(onClick = {}, variant = ButtonVariant.Outline) { Text("Outline") }
        PremiumButton(onClick = {}, variant = ButtonVariant.Ghost) { Text("Ghost") }
        PremiumButton(onClick = {}, variant = ButtonVariant.Link) { Text("Link Button") }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PremiumButton(onClick = {}, size = ButtonSize.Sm) { Text("Small") }
            PremiumButton(onClick = {}, size = ButtonSize.Default) { Text("Default") }
            PremiumButton(onClick = {}, size = ButtonSize.Lg) { Text("Large") }
        }

        PremiumButton(onClick = {}, size = ButtonSize.Icon) { Text("+") }
    }
}