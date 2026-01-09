package dev.esbi.mizan.ui.kit.icon

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImagePainter

@Composable
fun Icon(
    icon: IconValue,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    tint: Color? = null,
    asyncImagePainter: ((AsyncImagePainter) -> Unit)? = null
) {
    val painter = icon.painter

    if (painter is AsyncImagePainter && asyncImagePainter != null) {
        painter.apply(asyncImagePainter)
    }

    Image(
        modifier = modifier,
        painter = painter,
        contentDescription = contentDescription,
        alignment = alignment,
        contentScale = contentScale,
        colorFilter = tint?.let { ColorFilter.tint(it) }
    )
}
