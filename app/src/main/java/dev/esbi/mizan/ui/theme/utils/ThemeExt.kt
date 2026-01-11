@file:Suppress("unused")

package dev.esbi.mizan.ui.theme.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import dev.esbi.mizan.ui.theme.LocalMizanCornerRadius
import dev.esbi.mizan.ui.theme.LocalMizanTypography
import dev.esbi.mizan.ui.theme.MizanCornerRadius
import dev.esbi.mizan.ui.theme.MizanTypography

// colors
/*
val functionalColors: MizanFunctionalColors
    @Composable
    @ReadOnlyComposable
    get() = LocalMizanColors.current.functional

val primitiveColors: MizanPrimitiveColors
    @Composable
    @ReadOnlyComposable
    get() = LocalMizanColors.current.primitive

val semanticColors: MizanSemanticColors
    @Composable
    @ReadOnlyComposable
    get() = LocalMizanColors.current.semantic

//// corner radius
*/

val cornerRadius: MizanCornerRadius
    @Composable
    @ReadOnlyComposable
    get() = LocalMizanCornerRadius.current


// typography
val typography: MizanTypography
    @Composable
    @ReadOnlyComposable
    get() = LocalMizanTypography.current

