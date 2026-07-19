package dev.esbi.mizan.design.kit.icon

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Immutable
data class IconValue(
    private val value: Any,
    @field:DrawableRes private val default: Int? = null,
    @field:DrawableRes private val placeHolder: Int? = null
) {

    val painter: Painter
        @Composable get() = when (value) {
            is ImageVector -> rememberVectorPainter(image = value)
            is Int -> painterResource(id = value)
            is String -> rememberAsyncImagePainter(
                model = value,
                placeholder = placeHolder?.let { painterResource(it) },
                error = default?.let { painterResource(it) }
            )

            is ImageRequest -> rememberAsyncImagePainter(
                model = value,
                placeholder = placeHolder?.let { painterResource(it) },
                error = default?.let { painterResource(it) }
            )

            else -> unresolvedTypeError(value)
        }

    constructor(
        imageVector: ImageVector
    ) : this(value = imageVector)

    constructor(
        @DrawableRes resId: Int
    ) : this(value = resId)

    constructor(
        url: String,
        @DrawableRes default: Int? = null,
        @DrawableRes placeHolder: Int? = null
    ) : this(
        value = url,
        default = default,
        placeHolder = placeHolder
    )

    constructor(
        imageRequest: ImageRequest,
        @DrawableRes default: Int? = null,
        @DrawableRes placeHolder: Int? = null
    ) : this(
        value = imageRequest,
        default = default,
        placeHolder = placeHolder
    )

    private fun unresolvedTypeError(type: Any): Nothing {
        error("Unresolved icon type: $type")
    }
}
