package uz.esbi.gradle

import java.util.Properties

@Suppress("UNCHECKED_CAST")
operator fun <T> Properties.get(key: String, defaultValue: T): T {
    val property = getProperty(key, defaultValue.toString())
    return when (defaultValue) {
        is Boolean -> property.toBooleanStrictOrNull()
        is Byte -> property.toByteOrNull()
        is Long -> property.toLongOrNull()
        else -> error(message = "Unsupported type encountered while getting property value")
    } as T ?: defaultValue
}
