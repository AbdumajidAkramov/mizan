package uz.esbi.gradle

import org.gradle.accessors.dm.LibrariesForDeps
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the
import java.io.File
import java.io.FileInputStream
import java.util.Properties

fun Project.localProperties(dir: File = rootDir): Properties {
    val properties = Properties()
    val file = File(dir, "local.properties").takeIf(File::exists)
    file?.let(::FileInputStream)?.let(properties::load)
    return properties
}

@Suppress("UNCHECKED_CAST")
fun <T> Project.propertyOrDefault(key: String, defaultValue: Any): T {
    val property = runCatching { property(key) }.getOrNull()?.toString()
    return when (defaultValue) {
        is Boolean -> property?.toBooleanStrictOrNull() ?: defaultValue
        is Int -> property?.toIntOrNull() ?: defaultValue
        is Long -> property?.toLongOrNull() ?: defaultValue
        is String -> property ?: defaultValue
        else -> unsupportedTypeError()
    } as T
}

@Suppress("UNCHECKED_CAST")
fun <T> Project.propertyOrNull(key: String, clazz: Class<T>): T? {
    val property = runCatching { property(key) }
        .getOrNull()
        ?.toString()
        ?.takeIf(String::isNotBlank)
        ?: return null

    return when (clazz) {
        Boolean::class.java -> property.toBooleanStrictOrNull()
        Int::class.java -> property.toIntOrNull()
        Long::class.java -> property.toLongOrNull()
        String::class.java -> property
        else -> unsupportedTypeError()
    } as T
}

@Suppress("UNCHECKED_CAST")
fun <T> Project.propertyOrThrow(key: String, clazz: Class<T>): T {
    val property = requireNotNull(property(key)).toString()
    return when (clazz) {
        Boolean::class.java -> property.toBooleanStrict()
        Int::class.java -> property.toInt()
        Long::class.java -> property.toLong()
        String::class.java -> property
        else -> unsupportedTypeError()
    } as T
}

// workaround https://github.com/gradle/gradle/issues/15383
fun Project.withDeps(block: (deps: LibrariesForDeps) -> Unit) {
    if (project.name != "gradle-kotlin-dsl-accessors") {
        block(the())
    }
}

private fun unsupportedTypeError(): Nothing {
    error(message = "Unsupported type encountered while getting property value")
}
